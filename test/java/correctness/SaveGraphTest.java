package correctness;

import FileWorkout.LoadGraph;
import FileWorkout.SaveGraph;
import api.DirectedWeightedGraph;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SaveGraphTest {

    @Test
    void save() throws IOException {
        DirectedWeightedGraph dwg = LoadGraph.loadGraph("json_graphs\\G1.json");
        SaveGraph.save("json_graphs\\saved_graph.json", dwg);
        DirectedWeightedGraph dwg2 = LoadGraph.loadGraph("json_graphs\\saved_graph.json");
        assertEquals(dwg.edgeSize(), dwg2. edgeSize());
        assertEquals(dwg.nodeSize(), dwg2.nodeSize());

    }

    @Test
    void edgeToObject() {
        // nested in save
    }

    @Test
    void nodeToObject() {
        // nested in save
    }

}