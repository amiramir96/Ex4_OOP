package graphAlgo;

import api.*;

import java.util.*;
import java.util.List;

/**
 * use details:
 * this is obj represent dijkstra algorithm
 * please use "mapPathDijkstra" function before u use any other kind of function
 * IMPLEMENTS Runnable means Dijkstra can be used to Thread object !
 * (can be used for Center func which asks for alot of dijkstra parallel on dif src node)
 * LB4
 *  * initialize visited,prev,dist maps as list of HashMaps for being able to handle huge sizes of graph (milions of items) and wont get into heap out of memory
 *  * for more details look at DWG object notes and the project readme: https://github.com/amiramir96/Ex2-OOP#readme
 */
public class Dijkstra{
    public NodeData src;
    DirectedWeightedGraph currGraph;
    // uses maps instead of arrays since the keys of the nodes not guranteed to be ordered one by one
    ArrayList<HashMap<Integer, Boolean>> visitMap;
    ArrayList<HashMap<Integer, Integer>> prevMap;
    ArrayList<HashMap<Integer, Double>> distMap;
    public double longestPath;

    /**
     * @param g - relevant graph
     * @param src - starting src node
     */
    public Dijkstra(DirectedWeightedGraph g, NodeData src) {
        this.src = src;
        this.currGraph = g;
        this.prevMap = new ArrayList<>();
        this.distMap = new ArrayList<>();
        this.visitMap = new ArrayList<>();
        for (int i=0; i<1000; i++){
            this.prevMap.add(new HashMap<>());
            this.distMap.add(new HashMap<>());
            this.visitMap.add(new HashMap<>());
        }
    }

    /**
     * given Directed Wieghted graph that isConnected with |V| nodes and |E| edges
     * dijkstra algo via: https://www.youtube.com/watch?v=pSqmAO-m7Lk || https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
     * using regular priority queue(binomal min heap)
     * running time O(|E|log|V| + |V|log|V|)
     * Use Nodes weight field to save the distance from src, resetting in the end of the algo run
     * @param src - the given node which we want to know all shortest path to every other node
     */
    public void mapPathDijkstra(NodeData src){
        // initialize priority queue, visit - boolean, distance - double arrays
        Iterator <NodeData> itNode = this.currGraph.nodeIter();
        int key;
        while (itNode.hasNext()){
            key = itNode.next().getKey();
            this.prevMap.get(KeyTransform(key)).put(key, -1);
            this.distMap.get(KeyTransform(key)).put(key, Double.POSITIVE_INFINITY);
            this.visitMap.get(KeyTransform(key)).put(key, false);
        }
        // credit stack overflow https://stackoverflow.com/questions/2555284/java-priority-queue-with-a-custom-anonymous-comparator
        // compare through id ("serial number")
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(2 * distMap.size(), (o1, o2) -> { // credit to yuval bobnovsky to guide us to fix bug via using epsilon param
            if (Math.abs(distMap.get(KeyTransform(o1)).get(o1) - distMap.get(KeyTransform(o2)).get(o2)) < 1e-32){
                return 0;
            }
            else {
                return distMap.get(KeyTransform(o1)).get(o1) - distMap.get(KeyTransform(o2)).get(o2) > 0 ? +1 : -1;
            }
        });

        // init the src node to be distance 0 and add it to our priority queue
        this.distMap.get(KeyTransform(src.getKey())).replace(src.getKey(), 0.0);
        minHeap.add(src.getKey());

        // init vars
        int node_id;
        double newDist;
        EdgeData tempE;
        // "dijkstra" algorithm
        while(!minHeap.isEmpty()){
            // out loop, run over the heap till its empty (will be empty only after visited at all the nodes)
            node_id = minHeap.poll(); // given Node "x"
            this.visitMap.get(KeyTransform(node_id)).replace(node_id, true);
            // inner loop - move to all the neighbors of "x" via iterating all over the given node OUT edges
            Iterator<EdgeData> itEdge = this.currGraph.edgeIterOut(node_id);
            while (itEdge.hasNext()){
                tempE = itEdge.next(); // Edge from "x" to "y"
                int dest_id = tempE.getDest();
                if (this.visitMap.get(KeyTransform(dest_id)).get(dest_id)){continue;} // if visited x, isnt relevant anymore

                newDist = this.distMap.get(KeyTransform(node_id)).get(node_id) + tempE.getWeight(); // path from src to "x" + path from "x" to "y"
                if (newDist < this.distMap.get(KeyTransform(dest_id)).get(tempE.getDest())){ // switch only for better path
                    this.prevMap.get(KeyTransform(dest_id)).replace(tempE.getDest(), node_id);
                    this.distMap.get(KeyTransform(dest_id)).replace(tempE.getDest(), newDist);
                    minHeap.add(dest_id);
                }
            }
        }
    }

    private int KeyTransform(int id) {return id%1000;}


    /**
     *
     * @param dest - node
     * @return shortest via "mapPathDijkstra" functions updated maps
     */
    public double shortestToSpecificNode(NodeData dest){
        return this.distMap.get(KeyTransform(dest.getKey())).get(dest.getKey());
    }

    /**
     * for use of Center from dwgMagic
     * returns longest path from the while distMap
     */
    public double longestPath(){
        this.longestPath = Double.MIN_VALUE;
        for (HashMap<Integer, Double> element : this.distMap){
            for (double val : element.values()){
                if (val > this.longestPath){
                    this.longestPath = val;
                }
            }
        }
        return this.longestPath;
    }

    /**
     * after main dijsktra func used (mapPathDijkstra) - ogenizing path from src to dest via parentsMap
     * @param dest - last node in path
     * @return - shortestPath represented as List
     */
    public List<NodeData> shortestPathList(NodeData dest){
        // get the prevMap from the pathDijkstra algo
        LinkedList<NodeData> outputPath = new LinkedList<>(); // output list
        if (this.prevMap.get(KeyTransform(dest.getKey())).get(dest.getKey()) == -1) { return outputPath; } // -1 == not exist, both nodes is not connected
        outputPath.addFirst(dest); // dest node is the last in the list
        NodeData tempN;
        for (tempN=dest; this.prevMap.get(KeyTransform(tempN.getKey())).get(tempN.getKey()) != -1 && tempN != null; tempN = this.currGraph.getNode(prevMap.get(KeyTransform(tempN.getKey())).get(tempN.getKey()))){ // reverse run from dest node to parents
            outputPath.addFirst(this.currGraph.getNode(prevMap.get(KeyTransform(tempN.getKey())).get(tempN.getKey()))); // add first so we keep on order
        }
//        outputPath.removeFirst(); // remove the parent of src which is null
        return outputPath;
    }
}