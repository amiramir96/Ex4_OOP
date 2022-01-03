package correctness;

import FileWorkout.LoadGraph;
import api.DirectedWeightedGraph;
import api.NodeData;
import graphAlgo.BFS;
import impGraph.Node;
import impGraph.Point3D;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class BFSTest {

    @Test
    void mainProcessIsConnected() {
        DirectedWeightedGraph g1 = null;
        try {
            g1 = LoadGraph.loadGraph("json_graphs\\GShfiut.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BFS dfs = new BFS(g1);
        NodeData n1 = g1.nodeIter().next();
        assertTrue(dfs.mainProcessIsConnected(n1));
        // disconnect the graph
        Point3D p1 = new Point3D(0,0);
        Node n2 = new Node(p1,20);
        g1.addNode(n2);
        assertFalse(dfs.mainProcessIsConnected(n1));
    }
}