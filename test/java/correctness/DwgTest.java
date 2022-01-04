package correctness;

import FileWorkout.Loader;
import api.EdgeData;
import api.NodeData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import impGraph.Dwg;
import impGraph.Node;
import impGraph.Point3D;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class DwgTest {

    /**
     * function I "borrowed" from Ex2
     * @param json_file
     * @return
     */
    private Dwg getGraph(String json_file) {
        File jsonGraphFile = new File(json_file); // get file
        Dwg ans = null;

        try {
            JsonElement graphElement = JsonParser.parseReader(new FileReader(jsonGraphFile)); // shall read, so have to handle exception
            JsonObject graphObject = graphElement.getAsJsonObject(); // convert to json object, then we can work on fields inside
            // jsonToNode return list of nodes, jsonToEdge return list of edges, both is the req to cons Dwg(directed weighted graph)
            Dwg d = new Dwg();
            Loader g = new Loader(d);
            ans = (Dwg) g.loadGraph(json_file);
        }
        catch (FileNotFoundException err){ // throw exception..
            err.printStackTrace();
        }

        return ans;
    }

    Dwg dwg = getGraph("json_graphs\\G1.json"); //initialize graph

    /*
    excerpt from G1.json:
    {
      "src": 0,
      "w": 1.3118716362419698,
      "dest": 16
    },
    {
      "src": 0,
      "w": 1.232037506070033,
      "dest": 1
    },
    {
      "src": 1,
      "w": 1.8635670623870366,
      "dest": 0
    }
    {
      "pos": "35.19589389346247,32.10152879327731,0.0",
      "id": 0
    },
    {
      "pos": "35.20319591121872,32.10318254621849,0.0",
      "id": 1
    },
    {
      "pos": "35.20752617756255,32.1025646605042,0.0",
      "id": 2
    }
     */

    @Test
    void getNode() {
        assertEquals(35.19589389346247, dwg.getNode(0).getLocation().x());
        assertEquals(1, dwg.getNode(1).getKey());
    }

    @Test
    void getEdge() {
        assertEquals(1.3118716362419698, dwg.getEdge(0,16).getWeight());
        assertEquals(1, dwg.getEdge(1,0).getSrc());
    }

    @Test
    void addNode() {
        int num_of_nodes= dwg.nodeSize();
        Point3D p1 = new Point3D(0,0);
        Node n1 = new Node(p1,17);
        dwg.addNode(n1);
        assertEquals(num_of_nodes+1, dwg.nodeSize());
        assertEquals(0, dwg.getNode(17).getLocation().x());
    }

    @Test
    void connect() {
        int num_of_edges = dwg.edgeSize();
        dwg.connect(0,17,0.4);
        assertEquals(num_of_edges+1, dwg.edgeSize());
    }

    @Test
    void nodeIter() {
        int num_of_nodes= dwg.nodeSize();
        System.out.println(num_of_nodes);
        Iterator<NodeData> it = dwg.nodeIter();
        int c=0;
        //check if loops through all nodes
        while(it.hasNext()){
            c++;
            it.next();
        }
        assertEquals(num_of_nodes, c);
    }

    @Test
    void edgeIter() {
        int num_of_edges= dwg.edgeSize();
        Iterator<EdgeData> it = dwg.edgeIter();
        int c=0;
        //check if loops through all nodes
        while(it.hasNext()){
            c++;
            it.next();
        }
        assertEquals(num_of_edges, c);
    }

    @Test
    void testEdgeIter() {
        dwg.connect(17,1,0.4);
        Iterator<EdgeData> it = dwg.edgeIterOut(17);
        int c =0;
        // only 1 edge from 17
        while(it.hasNext()){
            c++;
            it.next();
        }
        assertEquals(1, c);
    }

    @Test
    void removeNode() {
        Dwg dwg = getGraph("json_graphs\\G1.json"); //initialize graph
        Point3D p1 = new Point3D(0,0);
        Node n1 = new Node(p1,17);
        dwg.addNode(n1);
        dwg.connect(17,1,0.4);

        int num_of_edges = dwg.edgeSize();
        dwg.removeNode(17);
        assertEquals(17, dwg.nodeSize());
        assertEquals(num_of_edges-1, dwg.edgeSize());
    }

    @Test
    void removeEdge() {
        int num_of_edges = dwg.edgeSize();
        dwg.removeEdge(0,16);
        assertEquals(num_of_edges-1, dwg.edgeSize());
    }

    @Test
    void nodeSize() {
        assertEquals(17, dwg.nodeSize());
        Point3D p1 = new Point3D(0,0);
        Node n1 = new Node(p1,17);
        dwg.addNode(n1);
        assertEquals(18, dwg.nodeSize());
    }

    @Test
    void edgeSize() {
        int num_of_edges = dwg.edgeSize();
        Point3D p1 = new Point3D(0,0);
        Node n1 = new Node(p1,17);
        dwg.addNode(n1);
        dwg.connect(17, 1, 12);
        assertEquals(num_of_edges+1, dwg.edgeSize());
        dwg.removeEdge(17, 1);
        assertEquals(num_of_edges, dwg.edgeSize());

    }

    @Test
    void getMC() {
        Dwg dwg = getGraph("json_graphs\\G1.json"); //initialize graph
        //how many insertions when building graph?
        int insertions = dwg.edgeSize()+ dwg.nodeSize();
        assertEquals(insertions, dwg.getMC());
        Point3D p1 = new Point3D(0,0);
        Node n1 = new Node(p1,17);
        dwg.addNode(n1);
        assertEquals(insertions+1, dwg.getMC());
        dwg.connect(17,1,0.4);
        assertEquals(insertions+2, dwg.getMC());
        dwg.removeEdge(17, 1);
        assertEquals(insertions+3, dwg.getMC());
    }
}