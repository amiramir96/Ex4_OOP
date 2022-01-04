package director;

import Graph.api.DirectedWeightedGraph;
import Graph.api.EdgeData;
import Graph.api.NodeData;
import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class Saver {

    /**
     * save graph as json string in given target file
     * @param target_file
     * @param g DirectedWeightedGraph
     * @throws IOException
     */
    public static void save(String target_file, DirectedWeightedGraph g) throws IOException {
        Iterator<EdgeData> edge_it = g.edgeIter();
        Iterator<NodeData> node_it = g.nodeIter();
        // combine edges
        JsonArray edges = new JsonArray();
        while (edge_it.hasNext()){
            JsonObject edge = EdgeToObject(edge_it.next());
            edges.add(edge);
        }
        // combine nodes
        JsonArray nodes = new JsonArray();
        while(node_it.hasNext()){
            JsonObject node = NodeToObject(node_it.next());
            nodes.add(node);
        }
        // graph json
        JsonObject graph_json = new JsonObject();
        graph_json.add("Edges", edges);
        graph_json.add("Nodes", nodes);
        // write to file
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.writeString(Paths.get(target_file), gson.toJson(graph_json));

    }

    /**
     * create json object from given edge
     * @param edge
     * @return json object
     */
    public static JsonObject EdgeToObject(EdgeData edge){
        JsonObject obj = new JsonObject();
        obj.addProperty("src",edge.getSrc());
        obj.addProperty("w",edge.getWeight());
        obj.addProperty("dest",edge.getDest());
        return obj;
    }

    public static JsonObject NodeToObject(NodeData node){
        double node_x= node.getLocation().x();
        double node_y= node.getLocation().y();
//        double node_z= node.getLocation().z();
        JsonObject obj = new JsonObject();
        obj.addProperty("pos", node_x+","+node_y);
        obj.addProperty("id", node.getKey());
        return obj;
    }
}
