package correctness;

import FileWorkout.LoadGraph;
import api.DirectedWeightedGraph;
import impGraph.DwgMagic;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ThreadPoolTest {

    @Test
    void run() {
        DirectedWeightedGraph g1 = null;
        {
            try {
                g1 = LoadGraph.loadGraph("json_graphs\\G1.json");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        DwgMagic dm = new DwgMagic(g1);

        //run "center" which uses the threadpool "run" method"
        dm.center();
    }
}