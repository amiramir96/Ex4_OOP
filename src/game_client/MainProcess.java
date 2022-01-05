package game_client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import director.Loader;
import Graph.api.NodeData;
import director.*;
import Graph.graphAlgo.Dijkstra;
import graphics.Window;
import Graph.impGraph.Dwg;


public class MainProcess {


    public static void main(String[] args) {
        GameData currGD; // important object - hold all the game details.

        // open client and connect him to server
        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // load gameData details
        currGD = new GameData(client);
        Loader load = new Loader(new Dwg());
        load.createGameFirstTime(currGD);

        //-------------------------------------------------------------------------
        /**
         * this block decides where to place each Agent at the start of the game!
         * logic:
         *      1. set ea agent closest to disjoint pokemons (start as good as possible with score)
         *      2. for every agent that DIDNT GOT a pokemon (not enough pokemon at start point for e.g) - set him at the center point
         */
        // init vars
        int agent_amount = currGD.getAgents_size();
        int[] start_points = new int[agent_amount];
        int center = currGD.getCurr_algo().center().getKey();

        /** REMINDER - Pokemon list is always SORTED via the most valued pokemon */
        List<Pokemon> p = currGD.getPokemons();
        // loop over all agents and let them pokemon
        for (int i=0; i < agent_amount; i++){
            if (i < p.size()){
                // logic -> 1.
                start_points[i] = p.get(i).getSrc();
            }
            else {
                // logic -> 2.
                start_points[i] = center;
            }
            // inform client to set the agent as we decided
            client.addAgent("{\"id\":"+start_points[i]+"}");
        }

        /** ADD HERE THE FIRST PROCESS DECISION THAT SET EA AGENT AT SOME POSITION*/

        //-------------------------------------------------------------------------
        currGD.self_update(true, true); // update serve - now WITH agents
        Window windo = new Window(currGD.getCurr_algo(), currGD); // start gui running :)

        // define for ea Agent an Executer class which is runnable
        // set thread for ea Executer
        // then, we have for ea Agent an Executer that responsible on his moving along the plain
        ArrayList<Thread> threads = new ArrayList<>();
        ArrayList<Executer> executers = new ArrayList<>();
        Executer exec;
        for (int i=0; i < agent_amount; i++){
            exec = new Executer(i, currGD);
            executers.add(exec);
            threads.add(new Thread(exec));
        }



        // shit -> to remove
        String graphStr = client.getGraph();
        System.out.println(graphStr);
        Loader.setGraph(graphStr);

        String agentsStr = client.getAgents();
        System.out.println(agentsStr);
        String pokemonsStr = client.getPokemons();
        System.out.println(pokemonsStr);

        // start client
        client.start();
        //        currGD.getCurr_client().login("316049311_212458244");


        // init vars
        double[] times = new double[executers.size()];
        double[] speeds = new double[executers.size()];
        double min_time;
        int min_idx;
        NodeData n;

        Dijkstra[] dijkstras = new Dijkstra[executers.size()]; // we gonna work with a Dijkstra obj for ea Agent
        List<Pokemon> tempFreePokemons = new ArrayList<>();

        Help_Funcs algo = new Help_Funcs(currGD);

        int iterates = 10; // idx to control moves VS gameData updates VS server.move borders
        while (true) {
            /**
             * this block is the MAIN PROCESS of the game:
             *  for each 10 iterates (approximated 100 miliseconds min border) -
             *      -> use move command
             *      -> use algorithm to split agents to pokemons
             *      -> engage all free pokemons via algorithm
             *  for every iterate:
             *      -> gameData will update himself
             */
            if (client.isRunning().equals("true")) { // false -> end of programme
                synchronized (client) {
                /** THIS BLOCK IS SYNCRONIZED with the Client!!!! */
                    if (iterates == 10) {
                            client.move(); // move command for agents via clients
                            iterates = 0;
                            currGD.self_update(true, true); // get the most updated game info


                        //-----------------------------------------------------------------------------------
                        // place for algorithm -> ori
                        /** HERE GONNA BE the BRAIN Process that decides which agent engage which pokemon*/


                        algo.hashCode();


                        for (Executer ex : executers) {
                            ex.selfUpdateTimeToEndAll(currGD.getAgents().get(ex.getAgent_id()).getSpeed());
                        }
                        tempFreePokemons = currGD.getFreePokemons();
                        speeds = updateSpeeds(currGD.getAgents());
                        for (Pokemon poki : tempFreePokemons) {
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

                        // END OF ALGORITHM
                        //-----------------------------------------------------------------------------------

                        // th.run == executer.run => executer update server about next destination of ea agent
                        for (Thread th : threads){
                            th.run();
                        }
                        // set here "join" to all threads.
                        for (Thread th : threads){
                            try{
                                th.join();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        // every iterate which we shouldnt use move command -> just update gameData info
                        currGD.self_update(true, true);
                    }
                }
                // control over "waiting" time
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                // client is down, game got to its end
                break;
            }
            iterates++;
        }
        // shut down programme ^^
        windo.closeWindow();
        if (client.isRunning().equals("true")){
            client.stop();
        }
    }

    /**
     * @param agents - list of Agents
     * @return arr of double, arr[agent_id] = agent_spped
     */
    private static double[] updateSpeeds(List<Agent> agents) {
        double[] output = new double[agents.size()];
        for (Agent a : agents){
            output[a.getId()] = a.getSpeed();
        }
        return output;
    }

}