package graph_test.correctness;

import Graph.impGraph.Point2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point3DTest {

    Point2D p1 = new Point2D(0,0);
    Point2D p2 = new Point2D(3,4);


    @Test
    void x() {
        assertEquals(0,p1.x());
        assertEquals(3,p2.x());
    }

    @Test
    void y() {
        assertEquals(0,p1.y());
        assertEquals(4,p2.y());
    }

    @Test
    void distance() {
        assertEquals(5,p1.distance(p2));
        assertEquals(5,p2.distance(p1));
        assertEquals(0,p1.distance(p1));
    }
}