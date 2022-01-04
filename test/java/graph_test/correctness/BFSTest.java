package graph_test.correctness;

import director.Loader;
import Graph.api.DirectedWeightedGraph;
import Graph.api.NodeData;
import Graph.graphAlgo.BFS;
import Graph.impGraph.Node;
import Graph.impGraph.Point2D;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class BFSTest {

    @Test
    void mainProcessIsConnected() {
        DirectedWeightedGraph g1 = null;
        try {
            g1 = Loader.loadGraph("json_graphs\\GShfiut.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BFS dfs = new BFS(g1);
        NodeData n1 = g1.nodeIter().next();
        assertTrue(dfs.mainProcessIsConnected(n1));
        // disconnect the graph
        Point2D p1 = new Point2D(0,0);
        Node n2 = new Node(p1,20);
        g1.addNode(n2);
        assertFalse(dfs.mainProcessIsConnected(n1));
    }
}