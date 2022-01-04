package correctness;

import director.Loader;
import api.DirectedWeightedGraph;
import api.NodeData;
import impGraph.Dwg;
import impGraph.DwgMagic;
import impGraph.Node;
import impGraph.Point2D;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DwgMagicTest {

    DirectedWeightedGraph g1, g2;
    {
        try {
            g1 = Loader.loadGraph("json_graphs\\G1.json");
            g2 = Loader.loadGraph("json_graphs\\G2.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    DwgMagic dm = new DwgMagic(g1);


    @Test
    void init() {
        assertEquals(g1.nodeSize(), dm.getGraph().nodeSize());
        dm.init(g2);
        assertEquals(g2.nodeSize(), dm.getGraph().nodeSize());
        dm.init(g1);
    }

    @Test
    void getGraph() {
        DirectedWeightedGraph g3 = dm.getGraph();
        assertEquals(g1.edgeSize(), g3.edgeSize());
        assertEquals(g1, g3);
    }

    @Test
    void copy() {
        DirectedWeightedGraph g3 = dm.copy();
        g3.removeNode(0);
        //independence
        assertEquals(g3.nodeSize()+1, dm.getGraph().nodeSize());
    }

    @Test
    void isConnected() {
        assertTrue(dm.isConnected());
        Point2D p1 = new Point2D(0,0);
        Node n1 = new Node(p1,20);
        // add new node
        dm.getGraph().addNode(n1);
        assertFalse(dm.isConnected());
    }

    @Test
    void shortestPathDist() {
        try {
            g1 = Loader.loadGraph("json_graphs\\G1.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dm.init(g1);
        Point2D p1 = new Point2D(0,0);
        Node n1 = new Node(p1,20);
        Point2D p2 = new Point2D(3,4);
        Node n2 = new Node(p2,21);
        //add nodes
        dm.getGraph().addNode(n1);
        dm.getGraph().addNode(n2);
        //create path
        dm.getGraph().connect(1, 20, 2.2);
        dm.getGraph().connect(20, 21, 1.9);
        assertEquals(4.1, dm.shortestPathDist(1, 21));
    }

    @Test
    void shortestPath() {
        try {
            g1 = Loader.loadGraph("json_graphs\\G1.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dm.init(g1);
        Point2D p1 = new Point2D(0,0);
        Node n1 = new Node(p1,20);
        Point2D p2 = new Point2D(3,4);
        Node n2 = new Node(p2,21);
        //add nodes
        dm.getGraph().addNode(n1);
        dm.getGraph().addNode(n2);
        //create path
        dm.getGraph().connect(1, 20, 2.2);
        dm.getGraph().connect(20, 21, 1.9);
        LinkedList<NodeData> path = (LinkedList<NodeData>) dm.shortestPath(1,21);
        assertTrue(path.contains(n1));
        assertTrue(path.contains(n2));
    }

    @Test
    void center() {
        // I built this graph so that it's center is 0
        try {
            g1 = Loader.loadGraph("json_graphs\\GShfiut.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dm.init(g1);
        assertEquals(0, dm.center().getKey());
    }


    @Test
    void tsp() {
        DwgMagic m = new DwgMagic(new Dwg());
        DirectedWeightedGraph g;
        m.load("json_graphs\\G1.json");
        g = m.getGraph();
        List<NodeData> l1 = new LinkedList<>();
        l1.add(g.getNode(9));
        l1.add(g.getNode(2));
        l1.add(g.getNode(7));
        l1 = m.tsp(l1);
        assertEquals(l1.get(0).getKey(), 9);
        assertEquals(l1.get(1).getKey(), 8);
        assertEquals(l1.get(2).getKey(), 7);
        assertEquals(l1.get(3).getKey(), 6);
        assertEquals(l1.get(4).getKey(), 2);
        assertEquals(l1.size(), 5);

        m.load("json_graphs\\G3.json");
        g = m.getGraph();
        l1 = new LinkedList<>();
        // 21,24,14,3,10,7
        l1.add(g.getNode(21));
        l1.add(g.getNode(24));
        l1.add(g.getNode(14));
        l1.add(g.getNode(3));
        l1.add(g.getNode(10));
        l1.add(g.getNode(7));
        l1 = m.tsp(l1);
        assertEquals(l1.get(0).getKey(), 21);
        assertEquals(l1.get(1).getKey(), 32);
        assertEquals(l1.get(2).getKey(), 31);
        assertEquals(l1.get(3).getKey(), 24);
        assertEquals(l1.get(4).getKey(), 31);
        assertEquals(l1.get(5).getKey(), 30);
        assertEquals(l1.get(6).getKey(), 29);
        assertEquals(l1.get(7).getKey(), 14);
        assertEquals(l1.get(8).getKey(), 13);
        assertEquals(l1.get(9).getKey(), 3);
        assertEquals(l1.get(10).getKey(), 12);
        assertEquals(l1.get(11).getKey(), 10);
        assertEquals(l1.get(12).getKey(), 7);
        assertEquals(l1.size(), 13);


    }

    @Test
    void TSP_B() {
        DwgMagic m = new DwgMagic(new Dwg());
        DirectedWeightedGraph g;
        m.load("json_graphs\\G1.json");
        g = m.getGraph();
        List<NodeData> l1 = new LinkedList<>();
        l1.add(g.getNode(9));
        l1.add(g.getNode(2));
        l1.add(g.getNode(7));
        l1 = m.TSP_B(l1);
        assertEquals(l1.get(0).getKey(), 2);
        assertEquals(l1.get(1).getKey(), 6);
        assertEquals(l1.get(2).getKey(), 7);
        assertEquals(l1.get(3).getKey(), 8);
        assertEquals(l1.get(4).getKey(), 9);

        assertEquals(l1.size(), 5);

        m.load("json_graphs\\G3.json");
        g = m.getGraph();
        LinkedList<NodeData> l2 = new LinkedList<>();
        l2.add(g.getNode(0));
        l2.addLast(g.getNode(2));
        l2.addLast(g.getNode(4));
        l2.addLast(g.getNode(6));
        l2.addLast(g.getNode(8));
        List<NodeData>l3 = m.TSP_B(l2);
        double total = 0;
        for (int i=0; i < l3.size()-1; i++){
            total += m.shortestPathDist(l3.get(i).getKey(), l3.get(i+1).getKey());
        }
        assertEquals(total, 7.503729445849754);


        l1 = new LinkedList<>();
        // 21,24,14,3,10,7
        l1.add(g.getNode(21));
        l1.add(g.getNode(24));
        l1.add(g.getNode(14));
        l1.add(g.getNode(3));
        l1.add(g.getNode(10));
        l1.add(g.getNode(7));
        l1 = m.TSP_B(l1);
        assertEquals(l1.get(0).getKey(), 7);
        assertEquals(l1.get(1).getKey(), 10);
        assertEquals(l1.get(2).getKey(), 12);
        assertEquals(l1.get(3).getKey(), 3);
        assertEquals(l1.get(4).getKey(), 13);
        assertEquals(l1.get(5).getKey(), 14);
        assertEquals(l1.get(6).getKey(), 29);
        assertEquals(l1.get(7).getKey(), 30);
        assertEquals(l1.get(8).getKey(), 31);
        assertEquals(l1.get(9).getKey(), 24);
        assertEquals(l1.get(10).getKey(), 31);
        assertEquals(l1.get(11).getKey(), 32);
        assertEquals(l1.get(12).getKey(), 21);
        assertEquals(l1.size(), 13);


    }

    @Test
    void save() {
        try {
            g1 = Loader.loadGraph("json_graphs\\GShfiut.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dm.init(g1);
        int num_of_nodes = g1.nodeSize();
        Point2D p1 = new Point2D(0,0);
        Node n1 = new Node(p1,20);
        dm.getGraph().addNode(n1);// add new node
        dm.save("json_graphs\\saved_graph.json");
        //reload graph
        try {
            g1 = Loader.loadGraph("json_graphs\\saved_graph.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dm.init(g1);
        assertEquals(num_of_nodes+1, dm.getGraph().nodeSize());
    }
//
    @Test
    void load() {
        // same as save
    }
}