package correctness;

import impGraph.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point3DTest {

    Point3D p1 = new Point3D(0,0,0);
    Point3D p2 = new Point3D(3,4,0);


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
    void z() {
        assertEquals(0,p1.z());
        assertEquals(0,p2.z());
    }

    @Test
    void distance() {
        assertEquals(5,p1.distance(p2));
        assertEquals(5,p2.distance(p1));
        assertEquals(0,p1.distance(p1));
    }
}