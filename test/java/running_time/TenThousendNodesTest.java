package running_time;

import FileWorkout.Loader;
import api.DirectedWeightedGraph;
import api.NodeData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import impGraph.Dwg;
import impGraph.DwgMagic;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TenThousendNodesTest {
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

    DirectedWeightedGraph d = getGraph("json_graphs\\G10K.json"); //initialize graph
    DwgMagic thousend = new DwgMagic(d);
    int size = 10000;

    @Test
    void load() {
        thousend.load("json_graphs\\G10K.json");
        assertEquals(thousend.getGraph().nodeSize(), size);
    }
    @Test
    void init() {
        this.thousend.init(this.thousend.getGraph());
        assertEquals(thousend.getGraph().nodeSize(), size);
    }

    @Test
    void getGraph() {
        d = this.thousend.getGraph();
        assertEquals(thousend.getGraph().nodeSize(), size);
    }

    @Test
    void copy() {
        this.d = this.thousend.copy();
        assertEquals(thousend.getGraph().nodeSize(), this.size);
    }

    @Test
    void isConnected() {
        this.thousend.isConnected();
        assertEquals(thousend.getGraph().nodeSize(), this.size);
    }

    @Test
    void shortestPathDist() {
        this.thousend.shortestPathDist(1,505);
        assertEquals(thousend.getGraph().nodeSize(), this.size);
    }

    @Test
    void shortestPath() {
        this.thousend.shortestPathDist(8,730);
        assertEquals(thousend.getGraph().nodeSize(), this.size);
    }

    @Test
    void center() {
        this.thousend.center();
        assertEquals(thousend.getGraph().nodeSize(), this.size);
        assertEquals(thousend.getGraph().nodeSize(), this.size);
    }

    @Test
    void tsp() {
        LinkedList<NodeData> l1 = new LinkedList<>();
        l1.add(this.d.getNode(111));
        l1.add(this.d.getNode(7023));
        l1.add(this.d.getNode(5845));
        l1.add(this.d.getNode(2244));
        l1.add(this.d.getNode(3213));
        l1.add(this.d.getNode(9761));
        l1.add(this.d.getNode(8192));
        l1.add(this.d.getNode(1345));
        l1.add(this.d.getNode(9752));
        l1.add(this.d.getNode(23));
        l1.add(this.d.getNode(1211));
        l1.add(this.d.getNode(703));
        l1.add(this.d.getNode(585));
        l1.add(this.d.getNode(2944));
        l1.add(this.d.getNode(3273));
        l1.add(this.d.getNode(9771));
        l1.add(this.d.getNode(8132));
        l1.add(this.d.getNode(1145));
        l1.add(this.d.getNode(9352));
        l1.add(this.d.getNode(233));
        this.thousend.tsp(l1);
        assertEquals(thousend.getGraph().nodeSize(), this.size);
    }
}