package Graph.impGraph;

import director.Loader;
import director.Saver;
import Graph.api.DirectedWeightedGraph;
import Graph.api.DirectedWeightedGraphAlgorithms;
import Graph.api.NodeData;
import Graph.graphAlgo.BFS;
import Graph.graphAlgo.Dijkstra;

import java.io.IOException;
import java.util.*;

public class DwgMagic implements DirectedWeightedGraphAlgorithms {
    private DirectedWeightedGraph currGraph;
    private int isConnected;
    private int mc;
    private int center;

    /**
     * constructor
     * @param g - graph
     */
    public DwgMagic(DirectedWeightedGraph g) {
        // zeroing vars
        this.currGraph = g;
        this.isConnected = -1;
        this.mc = g.getMC();
        this.center = -1;
    }

    /**
     * copy const (deep copy)
     * @param g - graph
     */
    @Override
    public void init(DirectedWeightedGraph g) {
        // zeroing vars
        this.isConnected = -1;
        this.center = -1;
        this.mc = 0;
        this.currGraph = g;
    }

    /**
     * via interface
     * @return currGraph
     */
    @Override
    public DirectedWeightedGraph getGraph() {
        return this.currGraph;
    }

    /**
     * deepCopy
     * @return deep copy of the graph
     */
    @Override
    public DirectedWeightedGraph copy() {return new Dwg(this.currGraph);}

    /**
     * definition: graph is connected if and only if there is path from every node to every node
     * choose randomally vertex v of the currGraph and use DFS algorithm twice (for more details on dfs algo: https://en.wikipedia.org/wiki/Depth-first_search)
     * 1- use DFS on v at currGraph
     * 2- use DFS on v at transpose(currGraph)
     * O(|V|+|E|)
     * the assumption is - given v vertex of G, if possible to get to any other node in G from v and from every node to v - graph is connected
     * if both DFS finishingTime(v) is the highest -> means the assumption is true, else - false
     * credit to Doctor Nivash Gabriel for teaching us this algo and methods.
     * @return true if and only if the graph is connected
     */
    @Override
    public boolean isConnected() {
        boolean ans;
        if (this.currGraph.edgeSize() < this.currGraph.nodeSize()){
            return false;
        }
        if (this.mc == this.currGraph.getMC() && this.isConnected != -1){ // check if we already have updated data (can save O(|V|+|E|))
            ans = (1 == this.isConnected);
        }
        else {
            this.mc = this.currGraph.getMC(); // update mc
            BFS dfsObj = new BFS(this.currGraph); // use DFS algo
            Iterator <NodeData> it = this.currGraph.nodeIter();
            NodeData n;
            if (it.hasNext()){
                n = it.next();
            }
            else {
                return false;
            }
            ans = dfsObj.mainProcessIsConnected(n);
            // init obj isConnected var for future
            if (ans) {
                this.isConnected = 1;
            }
            else {
                this.isConnected = 0;
            }
        }
        return ans;
    }
    /**
     * ez kinda, just use once dijksta once for src to dest
     * O(|E|log|V| + |V|log|V|)
     * @param src - start node
     * @param dest - end (target) node
     * @return shortest distance from src to dest
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        Dijkstra dijObj = new Dijkstra(this.currGraph, this.currGraph.getNode(src));
        dijObj.mapPathDijkstra(this.currGraph.getNode(src)); // dijkstra algo main proccess
        return dijObj.shortestToSpecificNode(this.currGraph.getNode(dest));
    }

    public double shortestPathDistTranspose(int src, int dest) {
        Dijkstra dijObj = new Dijkstra(this.currGraph, this.currGraph.getNode(src));
        dijObj.mapPathDijkstraTranspose(this.currGraph.getNode(src)); // dijkstra algo main proccess
        return dijObj.shortestToSpecificNode(this.currGraph.getNode(dest));
    }


    /**
     * use once dijkstra but now we memorize parents of ea node
     * cost more memory but time run is same as dijkstra
     * more memory == 1 more hashmap that hold all the nodes of the graph (will be deleted in the end of the function)
     * O(|E|log|V| + |V|log|V|)
     * @param src - start node
     * @param dest - end (target) node
     * @return shortest path from src node to dest node
     */
    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        Dijkstra dijObj = new Dijkstra(this.currGraph, this.currGraph.getNode(src));
        dijObj.mapPathDijkstra(this.currGraph.getNode(src)); // dijksta algo main proccess
        return dijObj.shortestPathList(this.currGraph.getNode(dest));
    }

    /**
     * as above, returns the node_id and not the nodes itself
     * @param src - from node
     * @param dest - to node
     * @return list of integers, each int represent node_id
     */
    public LinkedList<Integer> shortestPathIntegers(int src, int dest) {
        Dijkstra dijObj = new Dijkstra(this.currGraph, this.currGraph.getNode(src));
        dijObj.mapPathDijkstra(this.currGraph.getNode(src)); // dijksta algo main proccess
        List<NodeData> nodeDataList = dijObj.shortestPathList(this.currGraph.getNode(dest));
        LinkedList<Integer> output = new LinkedList<>();
        for (NodeData node : nodeDataList){
            output.addLast(node.getKey());
        }
        return output;
    }
    /**
     * first term is that the graph isConnected, so - will start "center" process if and only if isConnected is true
     * using dijkstra * |V| => O((|V|)*O(|E|log|V| + |V|log|V|) => O(|V|*|E|log(|V|))
     * |V| is the amount of nodes in the graph (V for vertex)
     * |E| is the amount of edges in the graph (E for edges)
     * for more details on dijkstra algorithm: https://www.youtube.com/watch?v=pSqmAO-m7Lk || https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
     * uses threads to minimize time run, grants far better results while currGraph nodes,edges sizes is huge ^^
     * @return the Node which is the center (from that node, the sum of paths to all other nodes together is minimum compare to all other nodes)
     */
//     threads
    @Override
    public NodeData center() {
        NodeData ansNode = null; // init ans
        // isConnect=-1 -> we dont know yet this data
        // this.mc != graph.mc -> graph as been changed since last time we could check "isConnected"
        if (this.isConnected == -1 || this.mc != this.currGraph.getMC()){
            this.isConnected();
            this.center = -1;
        }
        if (this.center != -1){ // maybe no need to use center again
            return this.currGraph.getNode(this.center);
        }
        if (this.isConnected == 1){ // if not connected - ans is null, go out
            this.mc = this.currGraph.getMC();
            // init vars
            NodeData tempN;
            Iterator<NodeData> it = this.currGraph.nodeIter(); // ez to iterate all over the nodes one by one
            int idx=0; // serial num via iterations
            // init lists that and will split the dijobjects via serial num
            List<LinkedList<NodeData>> thNodes = new ArrayList<>();
            for (int i=0; i < 8; i++){
                thNodes.add(new LinkedList<>());
            }
            while (it.hasNext()){ // dijkstra on ea node as src one time
                tempN = it.next();
                // split all dijobj equally
                thNodes.get(idx%8).add(tempN);
                idx++;
            }
            // create the thread objects
            LinkedList<ThreadPool> thPool = new LinkedList<>();
            for (int i=0; i<8; i++){
                if (thNodes.get(i).size()>0){
                    thPool.add(new ThreadPool(thNodes.get(i), i, this.currGraph));
                    thPool.getLast().start();
                }
            }
            // start threads
            try{ // join all
                for (ThreadPool threadDij : thPool){
                    threadDij.join();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            // take lowest
            double x = Double.MAX_VALUE;
            for (ThreadPool threadPool : thPool){
                if (threadPool.shortest < x){
                    x = threadPool.shortest;
                    ansNode = this.currGraph.getNode(threadPool.centerForNodeList);
                }
            }
            this.center = ansNode.getKey(); // save center value - maybe we will save some resources in futrue :-P
        }
        return ansNode;
    }



    /**
     * via our teacher - this tsp is more realistic problem to implement from the origin theory
     * this tsp function retuns the shortest road between the cities along the whole currGraph
     * our tsp Algorithm:
     * running along the cities list and choose greedy the shortest path to one of the other cities
     * bullets:
     * 0.1 - creating helper list named remaining list with copy of cities list
     * 0.2 - creating output list named orderedCitiesTravel (which we will return)
     * 0.3 - creating map with all nodes_id keys and boolean value (visitedMap) and init it to all false
     * 1- running on the whole cities
     * 2- add the first item from remaining list to the ordered list (start of the path)
     * 2.1 - init var to be the last item in ordered list, and mark him at visitedMap as true
     * 3- within two steps do:
     * 3.1 - check if there is direct edge from that node to one of the other nodes at remainingCities which is unvisited yet -> take its minimal edge
     * 3.2 - 3.1 is false, then use dijkstra on the given node and take the minimal distance path between the remainingCities that r unvisitedNodes
     * 4 - add the whole path we got from 3.1 or 3.2 to ordered list (from 3.1 we add only the dest node, from 3.2 its whole path list without the first item)
     * 4.1 - remove all nodes we added to ordered list from remaining list if exists
     * 5 - go back to 2.1 till there is no nodes in remaining list
     * 6 - return list of shortest path along the cities, orginized
     ***** side note *****
     * if there is path value of inifinity as minimum path between two cities, algorithm is done immedietly and returns null (since no connection between both node in any way)
     * running time : O(n*|E|log(|V|)) while n is the amount of the cities BUT for every direct edge between two cities
     * running time is decreasing (lower bound is o(n^2))
     * @param cities - list of nodes
     * @return - shortest path as possible that moving over all the cities at least once
     */
    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        // phase 0.1
        if (cities == null || cities.size() == 0){
            return null;
        }
        // phase 0.2 0.3
        List<NodeData> orederedCitiesTravel = new ArrayList<>(); // output list
        List<NodeData> remainingCities = new LinkedList<>();
        // init map with all nodes keys as key and boolean false for visited
        NodeData tempCity;
        HashMap<Integer, Boolean> visitedMap = new HashMap<>();
        for (NodeData city : cities) {
            visitedMap.put(city.getKey(), false);
            remainingCities.add(city);
        }

        int tempDest;
        double weightOfTsp = 0; // be on alert if there is minimal path between two nodes which is infinity(not exist)
        // ********** step 1 and 2 *********
        orederedCitiesTravel.add(remainingCities.get(0));
        while (!remainingCities.isEmpty() && weightOfTsp < Double.MAX_VALUE) {
            // ************ step 2.1 ************
            tempCity = orederedCitiesTravel.get(orederedCitiesTravel.size()-1);
            remainingCities.remove(tempCity);
            // for ea city
            // check if there is direct edges to the other nodes
            // if there is - take the minimal weigthed edge between the edges of the unvisited nodes
            // if there is not - use dijkstra on the curr node and take the shortest path to unvisited node
                visitedMap.replace(tempCity.getKey(), true);
                // ************* step 3 ***********
                tempDest = minimalDirectRoad(tempCity.getKey(), visitedMap, remainingCities);
                if (tempDest == -1){ // ************* step 3.2 *********
                    Dijkstra dijObj = new Dijkstra(this.currGraph, tempCity);
                    dijObj.mapPathDijkstra(tempCity); // dijkstra algo
                    List<NodeData> unDirectedPath = minimalUndirectRoad(dijObj, visitedMap, remainingCities);
                    if (unDirectedPath == null){
                        return null;
                    }
                    weightOfTsp += dijObj.shortestToSpecificNode(unDirectedPath.get(unDirectedPath.size()-1));
                    unDirectedPath.remove(0);
                    orederedCitiesTravel.addAll(unDirectedPath); // step 4
                    remainingCities.removeAll(unDirectedPath); // step 4.1
                }
                else { // ************* step 3.1 *********
                    orederedCitiesTravel.add(this.currGraph.getNode(tempDest)); // step 4
                    remainingCities.remove(this.currGraph.getNode(tempDest)); // step 4.1
                    weightOfTsp += this.currGraph.getEdge(tempCity.getKey(), tempDest).getWeight();
                }
            }
        if (weightOfTsp > Double.MAX_VALUE){
            return null;
        }

        return orederedCitiesTravel; // end, step 6
    }

    /**
     * for TSP use only
     * step3 - found that there is no directed road between src to any other ramained node
     * ===> get the minimal path from src to one of the remaining nodes (the shortest between all remaining)
     * @param dijObj - dijkstra that has been used algo on src
     * @param visitedMap - map of visited
     * @param remainingCities - as writen above
     * @return minimal path between src to one of the remaining nodes
     */
    private List<NodeData> minimalUndirectRoad(Dijkstra dijObj, HashMap<Integer, Boolean> visitedMap, List<NodeData> remainingCities) {
        double tempDist = Double.MAX_VALUE;
        int tempKey = -1;
        for (NodeData city : remainingCities){ // just iterate all over all of them and save the minimal
            if (dijObj.shortestToSpecificNode(city) < tempDist && !visitedMap.get(city.getKey())){ // O(1) proccess - get val from map
                tempDist = dijObj.shortestToSpecificNode(city);
                tempKey = city.getKey();
            }
        }
        if (tempKey == -1){
            return null; // no path
        }
        else {
            return dijObj.shortestPathList(this.currGraph.getNode(tempKey)); // there is ans!
        }
    }

    /**
     * calculate the minimal between the direct edges of src to the remaining nodes
     * @param src - node
     * @param visitedMap - visited at tsp algo?
     * @param remainingCities - list of relevant nodes
     * @return closest directly node_id (if exists), if not exists returns -1
     */
    private int minimalDirectRoad(int src, HashMap<Integer, Boolean> visitedMap, List<NodeData> remainingCities) {
        // init vars
        double minRoad = Double.MAX_VALUE;
        int minDest = -1;
        for (NodeData city : remainingCities){ // iterate all over the list
            if (!visitedMap.get(city.getKey())){ // will be checked only of visited already
                if (this.currGraph.getEdge(src, city.getKey()) != null){ // there is direct edge?
                    if (this.currGraph.getEdge(src, city.getKey()).getWeight() < minRoad){ // update min only
                        minRoad = this.currGraph.getEdge(src, city.getKey()).getWeight();
                        minDest = city.getKey();
                    }
                }
            }
        }
        return minDest; // -1 for no ans, positive int for node_id
    }

    /**
     * TSP_B is the upgraded algorithm for tsp from our python project, copied as is to java for better results.
     * Finds the shortest path that visits all the nodes in the list
     * :param node_lst: A list of nodes id's
     * algorithm name - "double ganger":
     * always compare between two sides of the road ^^
     * phases:
     * first of all - add first node from the node_lst to the ans list
     * loop over len(remaining nodes) times and:
     * 1. use dijkstra on the last node of the ans_list
     * 2. use transpose_dijkstra on the first node of the ans_list
     * 3. compare between outputs of 1 and 2, choose the shortest path to one of the remaining nodes
     * 4. add the chosen path to ans_list
     * 5. if we came into dead end (no ans to both dijkstras) - break and return (None, float('inf'))
     * 6. return to point (1.)
     * note: if there is more than 3 diff strongly components in the graph and they r connected within one direction of edges - there is no ans absolutely
     * @param cities
     * @return - A list of the nodes id's in the path, and the overall distance
     */
    public List<NodeData> TSP_B(List<NodeData> cities){
        // init
        int first_node = cities.get(0).getKey();
        LinkedList<Integer> remaining_list = new LinkedList<>();
        // copy cities to inner list
        for (NodeData n : cities){
            remaining_list.addLast(n.getKey());
        }
        LinkedList<NodeData> ans_list = new LinkedList<>();

        ans_list.add(this.currGraph.getNode(remaining_list.getFirst())); // add first item to answer list
        remaining_list.removeFirst(); // remove from remaining since we added item to the ans_list
        double total_dist = 0;
        int flag = -1;
        LinkedList<Integer> exception_node = new LinkedList<>();
        int leng = remaining_list.size();

        List<NodeData> forward_list;
        double forward_dist;
        List<NodeData> backward_list;
        double backward_dist;

        Dijkstra dij = null;
        Dijkstra transDij = null;
        // iterate over all the remaining list objects, once ea node
        while (!remaining_list.isEmpty()){
            // phase 1,2
            if (flag == 1){
                dij = new Dijkstra(this.currGraph, ans_list.getLast());
                dij.mapPathDijkstra(ans_list.getLast());
            }
            else if (flag == 0){
                transDij = new Dijkstra(this.currGraph, ans_list.getFirst());
                transDij.mapPathDijkstraTranspose(ans_list.getFirst());
            }
            else {
                dij = new Dijkstra(this.currGraph, ans_list.getLast());
                dij.mapPathDijkstra(ans_list.getLast());

                transDij = new Dijkstra(this.currGraph, ans_list.getFirst());
                transDij.mapPathDijkstraTranspose(ans_list.getFirst());
            }

            forward_list = dij.nearest_neighbour(remaining_list, exception_node);
            backward_list = transDij.nearest_neighbour(remaining_list, exception_node);

            if (forward_list == null){
                forward_dist = Double.POSITIVE_INFINITY;
            }
            else {
                forward_dist = shortestPathDist(dij.src.getKey(), forward_list.get(forward_list.size()-1).getKey());
            }
            if (backward_list == null){
                backward_dist = Double.POSITIVE_INFINITY;
            }
            else {
                backward_dist = shortestPathDistTranspose(transDij.src.getKey(), backward_list.get(backward_list.size()-1).getKey());

            }
            // phase 3

            if (forward_dist > backward_dist){
                if (backward_list == null){
                    break;
                }
                // phase 4
                Collections.reverse(backward_list);
                remaining_list.removeAll(List.of(backward_list.get(0).getKey()));
                backward_list.remove(backward_list.size()-1);
                backward_list.addAll(ans_list);
                ans_list = (LinkedList<NodeData>) backward_list;
                total_dist += backward_dist;
                // process to avoid from double dijkstra for every iteration - implemantation issue
                if (flag == 1){
                    exception_node = new LinkedList<>();
                    exception_node.add(ans_list.getFirst().getKey());
                }
                else {
                    exception_node.addLast(ans_list.getFirst().getKey());
                }
                flag = 0;
            }
            else {
                if (forward_list == null){
                    break;
                }
                // phase 4
                remaining_list.removeAll(List.of(forward_list.get(forward_list.size()-1).getKey()));
                forward_list.remove(0);
                ans_list.addAll(forward_list);
                total_dist += forward_dist;

                // process to avoid from double dijkstra for every iteration - implemantation issue
                if (flag == 0){
                    exception_node = new LinkedList<>();
                    exception_node.add(ans_list.getLast().getKey());
                }
                else {
                    exception_node.addLast(ans_list.getLast().getKey());
                }
                flag = 1;
            }
        }

        if (remaining_list.size() > 0){
            // point 5. -> no ans :/
            return null;
        }
        else {
            return ans_list;
        }
    }

    /**
     * if failed to save, the program will "explode"
     * @param file - the file name (may include a relative path).
     * @return boolean, succession of saving the graph data in the file
     */
    @Override
    public boolean save(String file) {
        try{
            Saver.save(file, this.currGraph);
            return true;
        }
        catch (IOException err){
            err.printStackTrace();
            return false;
        }
    }

    /**
     * if failed to load, will return false and program keep working (wont "explode")
     * @param file - file name of JSON file
     * @return boolean, loaded/not
     */
    @Override
    public boolean load(String file) {
        try {
            DirectedWeightedGraph dwg = new Dwg();
            Loader l = new Loader(dwg);
            dwg = l.loadGraph(file);
            this.currGraph = dwg;
            this.isConnected = -1;
            this.mc = 0;
            return true;
        } catch (Exception e){ // dont print shit trace for gui
            return false;
        }// dont bomb the program

    }
}
