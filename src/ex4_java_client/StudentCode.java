package ex4_java_client; /**
 * @author AchiyaZigi
 * A trivial example for starting the server and running all needed commands
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import FileWorkout.LoadGraph;
import api.NodeData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import director.Agent;
import director.Executer;
import director.GameData;
import director.Pokemon;
import graphAlgo.Dijkstra;
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

        String str = client.getAgents();
        System.out.println(str);

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

//        // bdikat shfiut
//        LinkedList<Integer> l1 = new LinkedList<>();
//        l1.addLast(2); l1.addLast(1); l1.addLast(0);
//        Executer ee = new Executer(0, l1, currGD);
//        Thread th0 = new Thread(ee);
        ArrayList<Thread> threads = new ArrayList<>();
        ArrayList<Executer> executers = new ArrayList<>();
        Executer exec;
        for (int i=0; i < agent_amount; i++){
            exec = new Executer(i, currGD);
            executers.add(exec);
            threads.add(new Thread(exec));
        }




        String graphStr = client.getGraph();
        System.out.println(graphStr);
        LoadGraph.setGraph(graphStr);

        client.addAgent("{\"id\":0}");
        String agentsStr = client.getAgents();
//        LoadGraph.getAgents(agentsStr);
        System.out.println(agentsStr);
        String pokemonsStr = client.getPokemons();
//        LoadGraph.getPokemons(pokemonsStr);
        System.out.println(pokemonsStr);
//        GameData gd = new GameData(client);
//        LoadGraph.updateGameData(gd, true, true);
//        gd.setCurr_client(client);
//        System.out.println(client.getInfo());
//        System.out.println(gd.getAgents_size()+" "+gd.getAgents());
        String isRunningStr = client.isRunning();
        System.out.println("client is running: " + isRunningStr);

        client.start();
//        for (Thread th : threads){
//            th.start();
//        }

//        System.out.println(client.getAgents());
//        System.out.println(client.timeToEnd());
//        Scanner keyboard = new Scanner(System.in);
//        System.out.println("enter the next dest: ");
//        int next = keyboard.nextInt();
//        client.chooseNextEdge("{\"agent_id\":0, \"next_node_id\": " + next + "}");
        double[] times = new double[executers.size()];
        double[] speeds = new double[executers.size()];
        double min_time;
        int min_idx;
        NodeData n;
        Dijkstra[] dijkstras = new Dijkstra[executers.size()];
        List<Pokemon> tempFreePokemons = new ArrayList<>();
        int iterates = 10;
        while (true) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (client.isRunning().equals("true")) {
                synchronized (client) {
//                    synchronized (currGD) {
                        if (iterates == 10) {
//                            System.out.println("synced for main and move");
//                        System.out.println("ordered move");
                            client.move();
                            iterates = 0;
//                    }
                            currGD.self_update(true, true);
                            //-----------------------------------------------------------------------------------
                            /** HERE GONNA BE the BRAIN Process that decides which agent engage which pokemon*/
                            for (Executer ex : executers) {
//                    System.out.println("im exec of agent: "+ex.getAgent_id());
                                ex.selfUpdateTimeToEndAll(currGD.getAgents().get(ex.getAgent_id()).getSpeed());
                            }
                            tempFreePokemons = currGD.getFreePokemons();
                            speeds = updateSpeeds(currGD.getAgents());
                            for (Pokemon poki : tempFreePokemons) {
//                    System.out.println(poki);
                                for (Agent agent : currGD.getAgents()) {
                                    if (executers.get(agent.getId()).getNext_stations() == null || executers.get(agent.getId()).getNext_stations().isEmpty()) {
                                        n = currGD.getCurr_graph().getNode(agent.getSrc());
                                    } else {
                                        n = currGD.getCurr_graph().getNode(executers.get(agent.getId()).getNext_stations().getLast());
                                    }
                                    dijkstras[agent.getId()] = new Dijkstra(currGD.getCurr_graph(), n);
                                    dijkstras[agent.getId()].mapPathDijkstra(n);
                                    times[agent.getId()] = executers.get(agent.getId()).getTimeToEndAll() + dijkstras[agent.getId()].shortestToSpecificNode(poki.getSrc()) / speeds[agent.getId()];
                                }
                                min_time = Double.MAX_VALUE;
                                min_idx = 0;
                                for (int i = 0; i < times.length; i++) {
                                    if (min_time > times[i]) {
                                        min_idx = i;
                                        min_time = times[i];
                                    }
                                }
                                executers.get(min_idx).addManyStops(dijkstras[min_idx].shortestPathList(poki.getSrc()));
                                executers.get(min_idx).addStop(poki.getDest());
                                times = new double[executers.size()];
                                poki.setEngaged(true);
                            }
                            for (Thread th : threads){
                                th.run();
//                                System.out.println("?");
                            }
                            for (Thread th : threads){
                                try{
                                    th.join();
//                                    System.out.println("??");
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
//                            System.out.println("???");
                        }
////                        System.out.println();
//                        //-----------------------------------------------------------------------------------
                        else {
//                            System.out.println("synced for main but without move ");
                            currGD.self_update(true, true);
                        }
//                    }
                }
            }
            else {
                break;
            }
            iterates++;
        }

        // set here "join" to all threads.

    }

    private static double[] updateSpeeds(List<Agent> agents) {
        double[] output = new double[agents.size()];
        for (Agent a : agents){
            output[a.getId()] = a.getSpeed();
        }
        return output;
    }

}