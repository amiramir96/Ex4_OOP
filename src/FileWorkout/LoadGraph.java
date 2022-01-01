package FileWorkout;

import api.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import impGraph.Dwg;
import impGraph.Node;
import impGraph.Point3D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class LoadGraph {

    JsonObject currGraphObject;
    DirectedWeightedGraph newGraph;
    public LoadGraph(){

    }
    /**
     * return DEG from name
     * @param file_direct - directory to file
     * @return DirectedWeightedGraph
     * @throws FileNotFoundException - not directory found
     */
    public static DirectedWeightedGraph loadGraph (String file_direct) throws FileNotFoundException {
        JsonObject currGraphObject;
        DirectedWeightedGraph newGraph;
        newGraph = new Dwg();
        File jsonGraphFile = new File(file_direct); // get file
        JsonElement graphElement = JsonParser.parseReader(new FileReader(jsonGraphFile)); // shall read, so have to handle exception
        currGraphObject = graphElement.getAsJsonObject();
        JsonArray arrOfEdges = currGraphObject.get("Edges").getAsJsonArray(); // get all the edge objects
        JsonArray arrOfNodes = currGraphObject.get("Nodes").getAsJsonArray(); // get all the edge objects

        // declare our var that we gonna work with inside the loop
        int tempId;
        String[] pointsCor;  // x = idx 0, y = idx 1, z = idx 3
        JsonObject nodeJsonObj; // temporary var
        for (JsonElement node : arrOfNodes){
            nodeJsonObj = node.getAsJsonObject(); // convert from element to object, so we would be able to pull the data as strings names
            // use split to discriminate between each cordinate inside the string
            // idx 0 = x, idx 1 = y, idx 2 = z
            pointsCor = nodeJsonObj.get("pos").getAsString().split(",");
            // construct the Node and add it to the output list
            tempId = nodeJsonObj.get("id").getAsInt();
            newGraph.addNode(new Node(new Point3D(Double.parseDouble(pointsCor[0]), Double.parseDouble(pointsCor[1]),
                    Double.parseDouble(pointsCor[2])), tempId));
        }
        JsonObject edgeJsonObj; // temporary var
        for (JsonElement edge : arrOfEdges){
            edgeJsonObj = edge.getAsJsonObject(); // convert from element to object, so we would be able to pull the data as strings names
            // construct the Node and add it to the output list
            newGraph.connect(edgeJsonObj.get("src").getAsInt(), edgeJsonObj.get("dest").getAsInt(), edgeJsonObj.get("w").getAsDouble());
        }
        return newGraph;
    }

}
