package correctness;

import api.NodeData;
import graphAlgo.Dijkstra;
import impGraph.Dwg;
import impGraph.DwgMagic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraTest {

    DwgMagic m = new DwgMagic(new Dwg());
    @Test
    void mapPathDijkstra() {
        m.load("json_graphs\\G1.json");
        double ans1 = 1.8015954015822042;
        double ans2 = 4.8330358613553095;
        Dijkstra dij1 = new Dijkstra(m.getGraph(), m.getGraph().getNode(1));
        dij1.mapPathDijkstra(m.getGraph().getNode(1));
        assertEquals(dij1.src.getKey(), 1);
        assertEquals(dij1.shortestToSpecificNode(m.getGraph().getNode(2)), ans1);
        assertEquals(dij1.shortestToSpecificNode(m.getGraph().getNode(7)), ans2);

        Dijkstra dij2 = new Dijkstra(m.getGraph(), m.getGraph().getNode(8));
        dij2.mapPathDijkstra(m.getGraph().getNode(8));
        assertEquals(dij2.src.getKey(), 8);
        double ans3 = 9.925289024973141;
        double ans4 = 5.942366995958979;
        assertEquals(dij2.shortestToSpecificNode(m.getGraph().getNode(15)), ans3);
        assertEquals(dij2.shortestToSpecificNode(m.getGraph().getNode(4)), ans4);


        m.load("json_graphs\\G3.json");
        Dijkstra dij3 = new Dijkstra(m.getGraph(), m.getGraph().getNode(8));
        dij3.mapPathDijkstra(m.getGraph().getNode(8));
        assertEquals(dij3.src.getKey(), 8);
        double ans5 = 0.0; // check if start node distance is zeroed
        double ans6 = 10.484263699093287;
        assertEquals(dij3.shortestToSpecificNode(m.getGraph().getNode(8)), ans5);
        assertEquals(dij3.shortestToSpecificNode(m.getGraph().getNode(41)), ans6);

//
    }

    @Test
    void shortestToSpecificNode() {
        m.load("json_graphs\\G1.json");
        double ans1 = 1.8015954015822042;
        double ans2 = 4.8330358613553095;
        Dijkstra dij1 = new Dijkstra(m.getGraph(), m.getGraph().getNode(1));
        dij1.mapPathDijkstra(m.getGraph().getNode(1));
        assertEquals(dij1.src.getKey(), 1);
        assertEquals(dij1.shortestToSpecificNode(m.getGraph().getNode(2)), ans1);
        assertEquals(dij1.shortestToSpecificNode(m.getGraph().getNode(7)), ans2);

        Dijkstra dij2 = new Dijkstra(m.getGraph(), m.getGraph().getNode(8));
        dij2.mapPathDijkstra(m.getGraph().getNode(8));
        assertEquals(dij2.src.getKey(), 8);
        double ans3 = 9.925289024973141;
        double ans4 = 5.942366995958979;
        assertEquals(dij2.shortestToSpecificNode(m.getGraph().getNode(15)), ans3);
        assertEquals(dij2.shortestToSpecificNode(m.getGraph().getNode(4)), ans4);


        m.load("json_graphs\\G3.json");
        Dijkstra dij3 = new Dijkstra(m.getGraph(), m.getGraph().getNode(8));
        dij3.mapPathDijkstra(m.getGraph().getNode(8));
        assertEquals(dij3.src.getKey(), 8);
        double ans5 = 0.0; // check if start node distance is zeroed
        double ans6 = 10.484263699093287;
        assertEquals(dij3.shortestToSpecificNode(m.getGraph().getNode(8)), ans5);
        assertEquals(dij3.shortestToSpecificNode(m.getGraph().getNode(41)), ans6);

    }

    @Test
    void longestPath() {
        m.load("json_graphs\\G1.json");
        Dijkstra dij1 = new Dijkstra(m.getGraph(), m.getGraph().getNode(1));
        dij1.mapPathDijkstra(m.getGraph().getNode(1));
        assertEquals(dij1.src.getKey(), 1);
        double ans1 = 10.52002088011531;
        assertEquals(dij1.longestPath(), ans1);

        Dijkstra dij2 = new Dijkstra(m.getGraph(), m.getGraph().getNode(8));
        dij2.mapPathDijkstra(m.getGraph().getNode(8));
        assertEquals(dij2.src.getKey(), 8);
        double ans2 = 9.925289024973141;
        assertEquals(dij2.longestPath(), ans2);

        m.load("json_graphs\\G3.json");
        Dijkstra dij3 = new Dijkstra(m.getGraph(), m.getGraph().getNode(8));
        dij3.mapPathDijkstra(m.getGraph().getNode(8));
        assertEquals(dij3.src.getKey(), 8);
        double ans3 = 17.24337418338013;
        assertEquals(dij3.longestPath(), ans3);

    }

    @Test
    void shortestPathList() {
        m.load("json_graphs\\G1.json");
        Dijkstra dij1 = new Dijkstra(m.getGraph(), m.getGraph().getNode(1));
        dij1.mapPathDijkstra(m.getGraph().getNode(1));
        List<NodeData> l1 = dij1.shortestPathList(m.getGraph().getNode(7));
        assertEquals(l1.get(0).getKey(), 1);
        assertEquals(l1.get(1).getKey(), 2);
        assertEquals(l1.get(2).getKey(), 6);
        assertEquals(l1.get(3).getKey(), 7);
        assertEquals(l1.size(), 4);

        Dijkstra dij2 = new Dijkstra(m.getGraph(), m.getGraph().getNode(8));
        dij2.mapPathDijkstra(m.getGraph().getNode(8));
        List<NodeData> l2 = dij2.shortestPathList(m.getGraph().getNode(13));
        assertEquals(l2.get(0).getKey(), 8);
        assertEquals(l2.get(1).getKey(), 9);
        assertEquals(l2.get(2).getKey(), 10);
        assertEquals(l2.get(3).getKey(), 11);
        assertEquals(l2.get(4).getKey(), 12);
        assertEquals(l2.get(5).getKey(), 13);
        assertEquals(l2.size(), 6);

        m.load("json_graphs\\G3.json");
        Dijkstra dij3 = new Dijkstra(m.getGraph(), m.getGraph().getNode(8));
        dij3.mapPathDijkstra(m.getGraph().getNode(8));
        List<NodeData> l3 = dij3.shortestPathList(m.getGraph().getNode(41));
        assertEquals(l3.get(0).getKey(), 8);
        assertEquals(l3.get(1).getKey(), 10);
        assertEquals(l3.get(2).getKey(), 11);
        assertEquals(l3.get(3).getKey(), 13);
        assertEquals(l3.get(4).getKey(), 14);
        assertEquals(l3.get(5).getKey(), 15);
        assertEquals(l3.get(6).getKey(), 39);
        assertEquals(l3.get(7).getKey(), 40);
        assertEquals(l3.get(8).getKey(), 41);
        assertEquals(l3.size(), 9);

    }
}