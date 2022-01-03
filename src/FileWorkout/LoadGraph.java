package FileWorkout;

import api.*;
import director.Agent;
import director.Pokemon;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
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
import java.util.ArrayList;

public class LoadGraph {

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
            newGraph.addNode(new Node(new Point3D(Double.parseDouble(pointsCor[0]), Double.parseDouble(pointsCor[1])), tempId));
        }
        JsonObject edgeJsonObj; // temporary var
        for (JsonElement edge : arrOfEdges){
            edgeJsonObj = edge.getAsJsonObject(); // convert from element to object, so we would be able to pull the data as strings names
            // construct the Node and add it to the output list
            newGraph.connect(edgeJsonObj.get("src").getAsInt(), edgeJsonObj.get("dest").getAsInt(), edgeJsonObj.get("w").getAsDouble());
        }
        return newGraph;
    }

    /**
     * @param strJsonFile - string that got extracted in JSON format
     * @return new graph via the parameters of the strJsonFile
     */
    public static DirectedWeightedGraph setGraph (String strJsonFile){
        // init empty graph
        Dwg g = new Dwg();
        // xfer string to json object
        JSONObject x = new JSONObject(strJsonFile);
        JSONArray jsonNodes = x.getJSONArray("Nodes");
        String[] q;
        int id;
        Double[] p = new Double[2];
        JSONObject node;

        // parse string of nodes to real nodes and add them to the graph
        for (int i=0; i<jsonNodes.length();i++){
            node = jsonNodes.getJSONObject(i);

            q = node.getString("pos").split(",");
            p[0] = Double.parseDouble(q[0]);
            p[1] = Double.parseDouble(q[1]);
            id = node.getInt("id");
//            System.out.println(p[0]+", "+p[1] + ", "+dest);
            g.addNode(new Node(new Point3D(p[0], p[1]), id));
        }

        // create real edges and add them to the graph
        JSONArray jsonEdges = x.getJSONArray("Edges");
        JSONObject edge;
        int src, dest;
        double weight;
        for (int j=0; j < jsonEdges.length(); j++){
            edge = jsonEdges.getJSONObject(j);
            src = edge.getInt("src");
            weight = edge.getDouble("w");
            dest = edge.getInt("dest");
            g.connect(src, dest, weight);
        }

        return g;
    }

    /**
     * build from the string file agent objects and set all of them into list
     * @param strJsonFile - represent json format of string of Agents
     * @return list of agents
     */
    public static ArrayList<Agent> getAgents(String strJsonFile){
        // init vars and json obj
        JSONObject x = new JSONObject(strJsonFile);
        JSONArray agents = x.getJSONArray("Agents");
        JSONObject ab, a;
        ArrayList<Agent> output = new ArrayList<>();
        int id;
        double value;
        int src;
        int dest;
        double speed;
        String[] q;
        GeoLocation pos;

        for (int i=0; i < agents.length(); i++){
            // loop over all json objects that represent agents
            ab = (JSONObject) agents.get(i);
            a = (JSONObject) ab.get("Agent");
            id = a.getInt("id");
            value = a.getDouble("value");
            src = a.getInt("src");
            dest = a.getInt("dest");
            speed = a.getDouble("speed");
            q = a.getString("pos").split(",");
            pos = new Point3D(Double.parseDouble(q[0]), Double.parseDouble(q[1]));

            // set it into the list
            output.add(new Agent(id, value, src, dest, speed, pos));
        }
        return output;
    }

    /**
     * build from the string file pokemon objects and set all of them into list
     * @param strJsonFile - represent json format of string of Pokemons
     * @return - list of pokemons
     */
    public static ArrayList<Pokemon> getPokemons(String strJsonFile){
        // init vars and json
        JSONObject x = new JSONObject(strJsonFile);
        JSONArray pokemons = x.getJSONArray("Pokemons");
        JSONObject pb, p;
        ArrayList<Pokemon> output = new ArrayList<>();
        double val;
        int type;
        String[] q;
        GeoLocation point;
        Pokemon pok;

        // loop over all json objects that represent pokemons
        for (int i=0; i<pokemons.length(); i++){
            // save their data and create pokemon
            pb = (JSONObject) pokemons.get(i);
            p = (JSONObject) pb.get("Pokemon");
            val = p.getDouble("value");
            type = p.getInt("type");
            q = p.getString("pos").split(",");
            point = new Point3D(Double.parseDouble(q[0]), Double.parseDouble(q[1]));
            pok = new Pokemon(val, type, point);

            // set it into the list
            output.add(pok);
        }
        return output;
    }
}
