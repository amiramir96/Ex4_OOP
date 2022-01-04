package graph_test.running_time;

import director.Loader;
import Graph.api.DirectedWeightedGraph;
import Graph.api.NodeData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import Graph.impGraph.Dwg;
import Graph.impGraph.DwgMagic;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HundredNodesTest {



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

    DirectedWeightedGraph d = getGraph("json_graphs\\G100.json"); //initialize graph
    DwgMagic hundred = new DwgMagic(d);
    int size = 100;

    @Test
    void load() {
        hundred.load("json_graphs\\G100.json");
        assertEquals(hundred.getGraph().nodeSize(), size);
    }
    @Test
    void init() {
        this.hundred.init(this.hundred.getGraph());
        assertEquals(hundred.getGraph().nodeSize(), size);
    }

    @Test
    void getGraph() {
        d = this.hundred.getGraph();
        assertEquals(hundred.getGraph().nodeSize(), size);
    }

    @Test
    void copy() {
        this.d = this.hundred.copy();
        assertEquals(hundred.getGraph().nodeSize(), this.size);
    }

    @Test
    void isConnected() {
        this.hundred.isConnected();
        assertEquals(hundred.getGraph().nodeSize(), this.size);
    }

    @Test
    void shortestPathDist() {
        this.hundred.shortestPathDist(1,70);
        assertEquals(hundred.getGraph().nodeSize(), this.size);
    }

    @Test
    void shortestPath() {
        this.hundred.shortestPathDist(1,70);
        assertEquals(hundred.getGraph().nodeSize(), this.size);
    }

    @Test
    void center() {
        this.hundred.center();
        assertEquals(hundred.getGraph().nodeSize(), this.size);
    }

    @Test
    void tsp() {
        LinkedList<NodeData> l1 = new LinkedList<>();
        l1.add(this.d.getNode(0));
        l1.add(this.d.getNode(11));
        l1.add(this.d.getNode(70));
        l1.add(this.d.getNode(55));
        l1.add(this.d.getNode(44));
        l1.add(this.d.getNode(33));
        l1.add(this.d.getNode(34));
        l1.add(this.d.getNode(88));
        l1.add(this.d.getNode(93));
        l1.add(this.d.getNode(31));
        l1.add(this.d.getNode(41));
        l1.add(this.d.getNode(43));
        l1.add(this.d.getNode(72));
        l1.add(this.d.getNode(89));
        l1.add(this.d.getNode(91));
        l1.add(this.d.getNode(97));
        l1.add(this.d.getNode(2));
        l1.add(this.d.getNode(14));
        l1.add(this.d.getNode(18));
        l1.add(this.d.getNode(22));
        l1.add(this.d.getNode(65));

        this.hundred.tsp(l1);
        assertEquals(hundred.getGraph().nodeSize(), this.size);
    }
}