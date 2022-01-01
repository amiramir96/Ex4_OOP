package correctness;

import FileWorkout.LoadGraph;
import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import impGraph.DwgMagic;
import impGraph.Node;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class nodeIterTest {


    DirectedWeightedGraph g1;
    {
        try {
            g1 = LoadGraph.loadGraph("json_graphs\\G1.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    DwgMagic dm = new DwgMagic(g1);

    @Test
    void hasNext() {
        Iterator<NodeData> it = dm.getGraph().nodeIter();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
    }

    @Test
    void next() {
        Iterator<NodeData> it = dm.getGraph().nodeIter();
        assertTrue(it.next() instanceof NodeData);
        it.next();
        assertTrue(it.next() instanceof NodeData);
    }

    @Test
    void remove() {
        int node_size = g1.nodeSize();
        Iterator<NodeData> it = g1.nodeIter();
        it.next();
        it.remove();
        assertEquals(node_size-1, g1.nodeSize());
        it.next();
        it.remove();
        assertEquals(node_size-2, g1.nodeSize());
    }

    @Test
    void forEachRemaining() {
        try {
            g1 = LoadGraph.loadGraph("json_graphs\\G1.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Iterator<NodeData> it = g1.nodeIter();
        AtomicInteger c= new AtomicInteger();
        int node_size = g1.nodeSize();
        it.forEachRemaining((EdgeData) -> c.getAndIncrement());
        assertEquals(node_size, c.get());
    }
}