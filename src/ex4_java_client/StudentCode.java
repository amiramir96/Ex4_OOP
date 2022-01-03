package ex4_java_client; /**
 * @author AchiyaZigi
 * A trivial example for starting the server and running all needed commands
 */
import java.io.IOException;
import java.util.Scanner;

import FileWorkout.LoadGraph;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class StudentCode {
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String graphStr = client.getGraph();
        System.out.println(graphStr);
        LoadGraph l = new LoadGraph();
        LoadGraph.setGraph(graphStr);
//        JSONObject x = new JSONObject(graphStr);
//        JSONArray y = x.getJSONArray("Nodes");
//        String[] q;
//        Double[] p = new Double[2];
//        for (int i=0; i<y.length();i++){
//            JSONObject node = y.getJSONObject(i);
//
//            q = node.getString("pos").split(",");
//            p[0] = Double.parseDouble(q[0]);
//            p[1] = Double.parseDouble(q[1]);
//            int dest = node.getInt("id");
//            System.out.println(p[0]+", "+p[1] + ", "+dest);
//
//        }
        client.addAgent("{\"id\":0}");
        String agentsStr = client.getAgents();
        LoadGraph.getAgents(agentsStr);
        System.out.println(agentsStr);
        String pokemonsStr = client.getPokemons();
        LoadGraph.getPokemons(pokemonsStr);
        System.out.println(pokemonsStr);
        String isRunningStr = client.isRunning();
        System.out.println(isRunningStr);

        client.start();

        while (client.isRunning().equals("true")) {
            client.move();
            System.out.println(client.getAgents());
            System.out.println(client.timeToEnd());
            Scanner keyboard = new Scanner(System.in);
            System.out.println("enter the next dest: ");
            int next = keyboard.nextInt();
            client.chooseNextEdge("{\"agent_id\":0, \"next_node_id\": " + next + "}");

        }
    }

}