package graph_test.correctness;

import Graph.impGraph.Node;
import Graph.impGraph.Point2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    Point2D p1 = new Point2D(0,0);
    Point2D p2 = new Point2D(3,4);

    Node n1 = new Node(p1,1);
    Node n2 = new Node(p1,2);
    Node n3 = new Node(p2,3);
    Node n4 = new Node(p2,4);

    @Test
    void getKey() {
        assertEquals(1,n1.getKey());
        assertEquals(3,n3.getKey());
    }

    @Test
    void getLocation() {
        assertNotEquals(p1, n1.getLocation());// independence
        assertEquals(3, n4.getLocation().x());
    }

    @Test
    void setLocation() {
        n2.setLocation(p2);
        assertNotEquals(p2, n2.getLocation());// independence
        assertEquals(3, n2.getLocation().x());
        n2.setLocation(p1);
    }

    @Test
    void getInfo() {
        assertEquals("", n3.getInfo());
        n3.setInfo("info");
        assertEquals("info", n3.getInfo());
    }

    @Test
    void setInfo() {
        n2.setInfo("info2");
        assertEquals("info2", n2.getInfo());
        String in = "info";
        n1.setInfo(in);
        in="";
        assertEquals("info",n1.getInfo());
    }

    @Test
    void getTag() {
        assertEquals(0, n1.getTag());
        n1.setTag(5);
        assertEquals(5, n1.getTag());
    }

    @Test
    void setTag() {
        n1.setTag(3);
        assertEquals(3, n1.getTag());
        int a=0;
        n1.setTag(a);
        a=1;
        assertEquals(0, n1.getTag());
    }

    @Test
    void getWeight() {
        assertEquals(Double.POSITIVE_INFINITY, n4.getWeight());
        n4.setWeight(2.2);
        assertEquals(2.2, n4.getWeight());
    }

    @Test
    void setWeight() {
        n4.setWeight(2.0);
        assertEquals(2.0, n4.getWeight());
    }
}