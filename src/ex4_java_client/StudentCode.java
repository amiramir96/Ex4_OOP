package ex4_java_client; /**
 * @author AchiyaZigi
 * A trivial example for starting the server and running all needed commands
 */
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import FileWorkout.LoadGraph;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import director.Agent;
import director.GameData;
import director.Pokemon;
import graphics.Window;
import impGraph.Dwg;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class StudentCode {


    public static void main(String[] args) {
        GameData currGD; // important object - hold all the game details.
        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }

        currGD = new GameData(client);
        LoadGraph q = new LoadGraph(new Dwg());
        q.createGameFirstTime(currGD);
//-------------------------------------------------------------------------
        int agent_amount = currGD.getAgents_size();
        int[] start_points = new int[agent_amount];
        int center = currGD.getCurr_algo().center().getKey();
//        currGD.self_update(false, false);
        List<Pokemon> p = currGD.getPokemons();
        for (int i=0; i < agent_amount; i++){
            if (i < p.size()){
                start_points[i] = p.get(i).getSrc();
            }
            else {
                start_points[i] = center;
            }

//            String temp = "{\"id\":"+start_points[i]+"}";
//            System.out.println(start_points[i] +" "+ temp);
            client.addAgent("{\"id\":"+start_points[i]+"}");
        }
//        currGD.self_update(true, true);
//        for (Pokemon pok : p){
//            System.out.println(pok);
//        }
//        for (Agent ag : currGD.getAgents()){
//            System.out.println(ag);
//        }
        /** ADD HERE THE FIRST PROCESS DECISION THAT SET EA AGENT AT SOME POSITION*/
//-------------------------------------------------------------------------
        currGD.self_update(true, true);
        Window windo = new Window(currGD.getCurr_algo(), currGD);







        String graphStr = client.getGraph();
        System.out.println(graphStr);
        LoadGraph.setGraph(graphStr);

        client.addAgent("{\"id\":0}");
        String agentsStr = client.getAgents();
        LoadGraph.getAgents(agentsStr);
        System.out.println(agentsStr);
//        String pokemonsStr = client.getPokemons();
//        LoadGraph.getPokemons(pokemonsStr);
//        System.out.println(pokemonsStr);
        System.out.println("\n");
//        GameData gd = new GameData(client);
//        LoadGraph.updateGameData(gd, true, true);
//        gd.setCurr_client(client);
//        System.out.println(client.getInfo());
//        System.out.println(gd.getAgents_size()+" "+gd.getAgents());
        System.out.println("\n");
        String isRunningStr = client.isRunning();
        System.out.println(isRunningStr);

        client.start();


        System.out.println(client.getAgents());
        System.out.println(client.timeToEnd());
        Scanner keyboard = new Scanner(System.in);
        System.out.println("enter the next dest: ");
        int next = keyboard.nextInt();
        client.chooseNextEdge("{\"agent_id\":0, \"next_node_id\": " + next + "}");

        while (client.isRunning().equals("true")) {
            try{
                Thread.sleep(10);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            client.move();
            currGD.self_update(true, true);
        }

        // set here "join" to all threads.

    }

}