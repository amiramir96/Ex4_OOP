package director;

import Graph.api.*;
import Graph.impGraph.DwgMagic;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import Graph.impGraph.Dwg;
import Graph.impGraph.Node;
import Graph.impGraph.Point2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * this class holds all the methods that requires loading/parsing data from strings/files in json format
 */
public class Loader {

    private static final double EPS = 0.000001; // eps for cals
    DirectedWeightedGraph currGraph;
    double mark; // little field that helps to control over prev and curr pokemons on the game

    public Loader(DirectedWeightedGraph g){ // simple constructor
        this.currGraph = g;
        this.mark = -1000000;
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
            newGraph.addNode(new Node(new Point2D(Double.parseDouble(pointsCor[0]), Double.parseDouble(pointsCor[1])), tempId));
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
            g.addNode(new Node(new Point2D(p[0], p[1]), id));
        }

        // create real edges and add them to the graph
        JSONArray jsonEdges = x.getJSONArray("Edges");
        JSONObject edge;
        int src, dest;
        double weight;
        // parse strings to edges
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
    public static ArrayList<Agent> getAgents(String strJsonFile, List<Agent> list){
        // init vars and json obj
        JSONObject x = new JSONObject(strJsonFile);
        JSONArray agents = x.getJSONArray("Agents");
        JSONObject ab, a;
        ArrayList<Agent> output = new ArrayList<>();
        // vars for the Agent obecjts
        int id;
        double value;
        int src;
        int dest;
        double speed;
        String[] q;
        GeoLocation pos;
        if (list == null){ // null -> first time we use this method -> shall CONSTRUCT the agent objects
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
                pos = new Point2D(Double.parseDouble(q[0]), Double.parseDouble(q[1]));
                // set it into the list
                output.add(new Agent(id, value, src, dest, speed, pos));
            }
            return output;
        }
        else { // gameData agent list is not null -> shall only UPDATE the data of ea agents ingame
            Agent agen;
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
                pos = new Point2D(Double.parseDouble(q[0]), Double.parseDouble(q[1]));
                agen = list.get(i);
                agen.setPos(pos);
                agen.setSpeed(speed);
                agen.setValue(value);
                agen.setSrc(src);
                agen.setDest(dest);
            }
            return (ArrayList<Agent>) list;
        }

    }

    /**
     * THIS METHOD CONSTRUCT THE POKEMONS OBJ (will be used for the start of the game)
     * build from the string file pokemon objects and set all of them into list
     * @param strJsonFile - represent json format of string of Pokemons
     * @return - list of pokemons
     */
    public ArrayList<Pokemon> getPokemons(String strJsonFile){
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
        EdgeData tempE; // will decide which edge that pok stands on

        // loop over all json objects that represent pokemons
        for (int i=0; i<pokemons.length(); i++){
            // save their data and create pokemon
            pb = (JSONObject) pokemons.get(i);
            p = (JSONObject) pb.get("Pokemon");
            val = p.getDouble("value");
            type = p.getInt("type");
            q = p.getString("pos").split(",");
            point = new Point2D(Double.parseDouble(q[0]), Double.parseDouble(q[1]));
            tempE = checkMyEdge(point, type);
            pok = new Pokemon(val, type, point, tempE.getSrc(), tempE.getDest());

            // set it into the list
            output.add(pok);
        }
        return output;
    }

    /**
     * using the triangle inquality
     * check which edge of the currGraph, the point stands on
     * @param point - geo location (x,y)
     * @return edge the point is on
     */
    private EdgeData checkMyEdge(GeoLocation point, int type) {
        Iterator<EdgeData> it = this.currGraph.edgeIter();
        EdgeData tempE=null;
        GeoLocation srcP, destP;

        // iterate over all graph_nodes and decide the edge of the given point
        while (it.hasNext()){
            tempE = it.next();
            srcP = this.currGraph.getNode(tempE.getSrc()).getLocation();
            destP = this.currGraph.getNode(tempE.getDest()).getLocation();
            double temp = Math.abs(srcP.distance(destP) - (srcP.distance(point) + destP.distance(point)));

            if (temp < EPS){
                // just some logic term to work with the Type +-1 (instructors definition)
                if (type < 0){ // src > dest
                    if (tempE.getSrc() > tempE.getDest()){
                        return tempE;
                    }
                    else {
                        return this.currGraph.getEdge(tempE.getDest(), tempE.getSrc());
                    }
                }
                else { // type == -1 dest > src
                    if (tempE.getSrc() > tempE.getDest()){
                        return this.currGraph.getEdge(tempE.getDest(), tempE.getSrc());
                    }
                    else {
                        return tempE;
                    }
                }
            }
        }
        return tempE;
    }

    /**
     * this method responsible to create "game space" for the game data objects
     * which means to initialize all gameData fields (beside agents which will do only while "update game" funtions)
     * @param ptr - gameData obj
     */
    public void createGameFirstTime(GameData ptr){

        // simple JSON format workout
        JSONObject y = new JSONObject(ptr.getCurr_client().getInfo());
        JSONObject x = y.getJSONObject("GameServer");
        ptr.setPokemons_size(x.getInt("pokemons"));
        ptr.setCurr_graph(setGraph(ptr.getCurr_client().getGraph())); // init graph
        ptr.setCurr_algo(new DwgMagic(ptr.getCurr_graph())); // init graph algo
        this.currGraph = ptr.getCurr_graph();

        // set pokemon list
        ptr.setPokemons(getPokemons(ptr.getCurr_client().getPokemons()));
        Collections.sort(ptr.getPokemons());

        // init all gameData fields
        ptr.setMoves(x.getInt("moves"));
        ptr.setGrade(x.getInt("grade"));
        ptr.setGame_level(x.getInt("game_level"));
        ptr.setMax_user_level(x.getInt("max_user_level"));
        ptr.setId(x.getInt("id"));
        ptr.setGraph_directory(x.getString("graph"));
        ptr.setAgents_size(x.getInt("agents"));
        ptr.setTimeLeft(ptr.getCurr_client().timeToEnd());

        // set loader for the gamedata (one obj for whole game, easier to mange)
        Loader load = new Loader(ptr.getCurr_graph());
        ptr.setLoad(load);
    }


    /**
     * update details of currect game
     * allow to the consumers of this method to decide if they would like to pass from updating pokemons/agents
     * @param ptr - pointer to gamedata object to update
     */
    public void updateGameData(GameData ptr, boolean flagAgent, boolean flagPok){
        // Json format workout
        JSONObject y = new JSONObject(ptr.getCurr_client().getInfo());
        JSONObject x = y.getJSONObject("GameServer");
        ptr.setPokemons_size(x.getInt("pokemons"));

        if (flagAgent){ // agents
            ptr.setAgents(getAgents(ptr.getCurr_client().getAgents(), ptr.getAgents()));
        }
        if (flagPok){ // pokemons
            UpdatePokemons(ptr, ptr.getCurr_client().getPokemons());
        }

        // update vars
        ptr.setMoves(x.getInt("moves"));
        ptr.setGrade(x.getInt("grade"));
        ptr.setGame_level(x.getInt("game_level"));
        ptr.setMax_user_level(x.getInt("max_user_level"));
        ptr.setId(x.getInt("id"));
        ptr.setGraph_directory(x.getString("graph"));
        ptr.setAgents_size(x.getInt("agents"));
        ptr.setTimeLeft(ptr.getCurr_client().timeToEnd());
    }

    /**
     * inner method
     * responsible to replace for GAMEDATA obj the list of pokemons to be the most relevant via:
     *      1. adding new pokemons
     *      2. remove pokemons that not existing in game anymore (got captured)
     * @param ptr -gamedata obj
     * @param strJsonFile - string of pokemons.json
     */
    private void UpdatePokemons(GameData ptr, String strJsonFile) {
        this.mark++;
        // init vars and json
        JSONObject x = new JSONObject(strJsonFile);
        JSONArray pokemons = x.getJSONArray("Pokemons");
        // vars of a pokemon
        JSONObject pb, p;
        double val;
        int type;
        String[] q;
        GeoLocation point;
        Pokemon pok;
        EdgeData tempE; // will decide which edge that pok stands on
        List<Pokemon> list = ptr.getPokemons();
        boolean exist = false;

        // loop over all json objects that represent pokemons
        for (int i=0; i<pokemons.length(); i++){
            // save their data and create pokemon
            pb = (JSONObject) pokemons.get(i);
            p = (JSONObject) pb.get("Pokemon");
            val = p.getDouble("value");
            type = p.getInt("type");
            q = p.getString("pos").split(",");
            point = new Point2D(Double.parseDouble(q[0]), Double.parseDouble(q[1]));

            for (Pokemon poki : list){
                // check if existing at the pokemon list
                // then update only mark field
                if (poki.getPos().x() == point.x() && poki.getPos().y() == point.y() && poki.getValue() == val && poki.getType() == type){
                    poki.setMark(this.mark);
                    exist = true;
                    break;
                }
            }
            if (!exist){ // no exists == create new object
                tempE = checkMyEdge(point, type);
                list.add(new Pokemon(val, type, point, tempE.getSrc(), tempE.getDest(), this.mark));
            }
            exist = false;
        }
        // remove all pokemons with diff mark field (means they r not existing in the server in the present)
        list.removeIf(poki -> poki.getMark() != this.mark); // thanks to intellij <3

        List<Pokemon> tempP = ptr.getPokemons();

        for (Pokemon piii : tempP){
            if (piii.isEngaged()){
                ptr.setEngaged(piii.getSrc(), piii.getDest());
            }
        }

        ptr.setPokemons_size(list.size());
    }
}
