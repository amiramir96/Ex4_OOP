import api.DirectedWeightedGraph;
import api.NodeData;
import correctness.RandomGraphGenerator;
import impGraph.Dwg;
import impGraph.DwgMagic;
import impGraph.Node;
import impGraph.Point3D;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PerformanceTest {

    
    public static void main(String[] args) {
        int param;
        if (args.length > 0){
            param = Integer.parseInt(args[0]);
        }
        else{
            System.out.println("set number of nodes:");
            Scanner sc = new Scanner(System.in);
            param = sc.nextInt();
        }
        long start = System.nanoTime();
        DirectedWeightedGraph g = new Dwg();
        for (int i=0; i < param; i++){
            g.addNode(new Node(new Point3D(Math.random()*100, Math.random()*100), i));
        }
        for (int i=0; i < param; i++){
            for (int j=0; j < 20; j++){
                g.connect(i, (int)(Math.random()*(param)), Math.random()*50);
            }
        }
        DwgMagic dm = new DwgMagic(g);
        if (dm.getGraph().nodeSize() > 800000){
            Iterator<NodeData> n = dm.getGraph().nodeIter();
            ArrayList<NodeData> p = new ArrayList<>();
            for (int i=0; n.hasNext() && i<100000; i++){
                n.next();
                p.add(n.next());
            }
            NodeData ni = n.next();
            dm.getGraph().removeNode(ni.getKey());
            dm.getGraph().addNode(ni);
            int j=0;
            for (int i=dm.getGraph().edgeSize(); i<dm.getGraph().nodeSize()*20; i++){
                dm.getGraph().connect(p.get(j).getKey(),p.get(j+1).getKey(), Math.random()*1000);
                j++;
            }
        }
        long finish = System.nanoTime();
        System.out.println("create graph time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
        System.out.println("Number of nodes: "+ g.nodeSize());
        System.out.println("Number of edges: "+ g.edgeSize());
        int num_of_nodes = g.nodeSize();
        int num_of_edges = g.edgeSize();

        //is connected
        start = System.nanoTime();
        boolean connected = dm.isConnected();
        finish = System.nanoTime();
        System.out.println("isConnected time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
//        //center
        if (dm.getGraph().nodeSize() + dm.getGraph().edgeSize() < 550000){
            start = System.nanoTime();
            dm.center();
            finish = System.nanoTime();
            System.out.println("center time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
        }
        else if (!connected){
            start = System.nanoTime();
            dm.center();
            finish = System.nanoTime();
            System.out.println("center time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds" + " graph is not connected, easy case");
        }
        else {
            double approximateCenter = ((double)(num_of_nodes+num_of_edges)) / 210000;
            int approximateCenter210kObj = 5;
            approximateCenter = Math.pow(approximateCenter, 3) * approximateCenter210kObj;
            System.out.println("total graph objects is more than 500k, will take too long to end center proccess for connected graph");
            System.out.println("approximate time for center is: "+ approximateCenter/60 + " hours");
        }
        //shortest path
        start = System.nanoTime();
        dm.shortestPathDist(0, g.nodeSize()-1);
        finish = System.nanoTime();
        System.out.println("shortestPath time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
         //tsp
        LinkedList<NodeData> l1 = new LinkedList<>();
        if (dm.getGraph().nodeSize() > 20){
            // add 20 nodes to the list
            for (int i = 2; i < 22; i++) {
                l1.add(g.getNode(num_of_nodes/i));
            }
            start = System.nanoTime();
            dm.tsp(l1);
            finish = System.nanoTime();
            System.out.println("tsp for 20 cities time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
        }
        else {
            Iterator<NodeData> itN = dm.getGraph().nodeIter();
            while (itN.hasNext()){
                l1.add(itN.next());
            }
            start = System.nanoTime();
            dm.tsp(l1);
            finish = System.nanoTime();
            System.out.println("tsp for all the node graph as cities time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
        }
        // copy graph
        start = System.nanoTime();
        dm.copy();
        finish = System.nanoTime();
        System.out.println("copy time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
        // load graph
        if (dm.getGraph().nodeSize() + dm.getGraph().edgeSize() < 4000001){
            start = System.nanoTime();
            g = RandomGraphGenerator.createRndGraph(dm.getGraph().nodeSize());
            finish = System.nanoTime();
            System.out.println("load time from json file: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
        }
        else {
            System.out.println("cannot simulate more than 4 milion objects loading from json, Heap would get out of memory");
        }
        // save graph
        if (dm.getGraph().nodeSize() + dm.getGraph().edgeSize() < 4000001){
            start = System.nanoTime();
            dm.save("random_graph_simulation_of_save.json");
            finish = System.nanoTime();
            System.out.println("save time to json file: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
        }
        else {
            System.out.println("cannot simulate more than 4 milion objects saving to json, Heap would get out of memory");
        }
    }
}