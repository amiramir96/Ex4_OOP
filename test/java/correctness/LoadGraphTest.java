package correctness;

import FileWorkout.LoadGraph;
import api.DirectedWeightedGraph;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class LoadGraphTest {

    @Test
    void loadGraph() {
        RandomGraphGenerator.createRndGraph(50);
        DirectedWeightedGraph g= null;
        try {
            g = LoadGraph.loadGraph("random_graph.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert g != null;
        assertEquals(50, g.nodeSize());
    }

    @Test
    void jsonToEdge() {
        // submethod of loadGraph, tested there.
    }

    @Test
    void jsonToNode() {
        // submethod of loadGraph, tested there.
    }
}