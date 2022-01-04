package graph_test.correctness;

import Graph.impGraph.Edge;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {

    Edge e1= new Edge(1,0.2,2);
    Edge e2= new Edge(2,0.5,3);
    Edge e3 = new Edge(e1);


    @Test
    void getSrc() {
        assertEquals(1, e1.getSrc());
        assertEquals(2, e2.getSrc());
    }

    @Test
    void getDest() {
        assertEquals(2, e1.getDest());
        assertEquals(3, e2.getDest());
    }

    @Test
    void getWeight() {
        assertEquals(0.2, e1.getWeight());
        assertEquals(0.5, e2.getWeight());
    }

    @Test
    void getInfo() {
        assertEquals("", e1.getInfo());
        assertEquals("", e2.getInfo());
    }

    @Test
    void setInfo() {
        e1.setInfo("info 1");
        assertEquals("info 1", e1.getInfo());
        String s = "info 2";
        e2.setInfo(s);
        s="";
        assertEquals("info 2", e2.getInfo());
    }

    @Test
    void getTag() {
        assertEquals(0, e1.getTag());
        assertEquals(0, e3.getTag());
    }

    @Test
    void setTag() {
        e1.setTag(4);
        e3.setTag(2);
        assertEquals(2, e3.getTag());
        assertEquals(4, e1.getTag());
    }
}