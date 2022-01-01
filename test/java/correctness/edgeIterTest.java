package correctness;

import FileWorkout.LoadGraph;
import api.DirectedWeightedGraph;
import api.EdgeData;
import impGraph.DwgMagic;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class edgeIterTest {


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
        assertTrue(dm.getGraph().edgeIter().hasNext());
        assertTrue(dm.getGraph().edgeIterOut(0).hasNext());
    }

    @Test
    void next() {
        Iterator<EdgeData> it = dm.getGraph().edgeIter();
        assertTrue(it.next() instanceof EdgeData);
        it.next();
        assertTrue(it.next() instanceof EdgeData);
    }

    @Test
    void remove() {
        int edge_size = g1.edgeSize();
        Iterator<EdgeData> it = g1.edgeIter();
        it.next();
        it.remove();
        assertEquals(edge_size-1, g1.edgeSize());
        it.next();
        it.remove();
        assertEquals(edge_size-2, g1.edgeSize());
    }

    @Test
    void forEachRemaining() {
        try {
            g1 = LoadGraph.loadGraph("json_graphs\\G1.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Iterator<EdgeData> it = g1.edgeIter();
        AtomicInteger c= new AtomicInteger();
        int edge_size = g1.edgeSize();
        it.forEachRemaining((EdgeData) -> c.getAndIncrement());
        assertEquals(edge_size, c.get());
    }

    @Test
    void remove2() {
        int edge_size = g1.edgeSize();
        Iterator<EdgeData> it = g1.edgeIterOut(1);
        it.next();
        it.remove();
        assertEquals(edge_size-1, g1.edgeSize());
        it.next();
        it.remove();
        assertEquals(edge_size-2, g1.edgeSize());
    }

    @Test
    void raiseExcep(){
        Iterator<EdgeData> it1 = g1.edgeIter();
        Iterator<EdgeData> it2 = g1.edgeIterOut(1);
        g1.removeNode(3);
        try {
            if(it1.hasNext()){
                it1.next();
            }
            fail();
        }
        catch (Exception e){
            assertTrue(true);
        }
        try {
            if(it2.hasNext()){
                it2.next();
            }
            fail();
        }
        catch (Exception e){
            assertTrue(true);
        }
    }
}