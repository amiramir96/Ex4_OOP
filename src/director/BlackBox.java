package director;

import Graph.api.NodeData;
import Graph.graphAlgo.Dijkstra;

import java.util.ArrayList;
import java.util.List;

/**
 * this is the ALGORITHM of the game
 * decides how and to which agent to engage each pokemon
 * general principles:
 *      1. prioritize the most valued pokemon to be catched
 *      2. once pokemon shall be enaged - match it to the agent that has the optimal time to end task
 */
public class BlackBox {
    private ArrayList<Executer> executers;
    GameData currGD;
    double[] times;
    double[] speeds;
    Dijkstra[] dijkstras; // we gonna work with a Dijkstra obj for ea Agent
    List<Pokemon> tempFreePokemons;

    public BlackBox(GameData gd, ArrayList<Executer> execList){
        this.executers = execList;
        this.currGD = gd;
        this.dijkstras = new Dijkstra[execList.size()];
        this.times = new double[execList.size()];
        this.speeds = new double[execList.size()];
        this.tempFreePokemons = new ArrayList<>();
    }

    /**
     * the main algorithm which matches pokemons to agents
     * working via the principles:
     *      1. prioritize the most valued pokemon to be catched
     *      2. once pokemon shall be enaged - match it to the agent that has the optimal time to end task
     * ATTENTION! - every Executer represents an AGENT!!!
     */
    public void runAlgorithm (){
        double min_time;
        int min_idx;
        NodeData n;

        // update times of each executer to be the most updated to present
        for (Executer ex : this.executers) {
            ex.selfUpdateTimeToEndAll(this.currGD.getAgents().get(ex.getAgent_id()).getSpeed());
        }

        this.tempFreePokemons = this.currGD.getFreePokemons();
        this.speeds = updateSpeeds(this.currGD.getAgents()); // get the agents speed

        // loop over all the free pokemons
        for (Pokemon poki : this.tempFreePokemons) {
            // relevant if and only if there is free agents
            if (this.thereIsFreeAgents()) {

                // loop over all the agents and calculate their time to catch the current pokemon
                for (Agent agent : this.currGD.getAgents()) {
                    if (this.executers.get(agent.getId()).getNext_stations() == null || this.executers.get(agent.getId()).getNext_stations().isEmpty()) {
                        n = this.currGD.getCurr_graph().getNode(agent.getSrc());
                    } else {
                        n = this.currGD.getCurr_graph().getNode(this.executers.get(agent.getId()).getNext_stations().getLast());
                    }
                    this.dijkstras[agent.getId()] = new Dijkstra(this.currGD.getCurr_graph(), n);
                    this.dijkstras[agent.getId()].mapPathDijkstra(n);
                    // update time to end its task
                    this.times[agent.getId()] = this.executers.get(agent.getId()).getTimeToEndAll() + this.dijkstras[agent.getId()].shortestToSpecificNode(poki.getSrc()) / this.speeds[agent.getId()];
                }
                min_time = Double.MAX_VALUE;
                min_idx = 0;
                // get the fastest agent
                for (int i = 0; i < this.times.length; i++) {
                    if (min_time > this.times[i]) {
                        min_idx = i;
                        min_time = this.times[i];
                    }
                }
                // match between agent to pokemon IF AND ONLY IF the optimal agent is ready (free to start the task)
                // otherwise: DONT MATCH - shall wait for better opportunity
                if (this.executers.get(min_idx).isReady()){
                    this.executers.get(min_idx).addManyStops(this.dijkstras[min_idx].shortestPathList(poki.getSrc()));
                    this.executers.get(min_idx).addStop(poki.getDest());
                    this.times = new double[this.executers.size()];

                    this.currGD.setEngaged(poki.getSrc(), poki.getDest()); // mark all the pokemons on poki edge to be engage
                }
            }
            else {
                // no free agents, shall stop iterating over the pokemons list
                break;
            }
        }
    }

    /**
     * @return true if there is free agent to get new task
     */
    private boolean thereIsFreeAgents() {
        for (Executer exec : this.executers){
            if (exec.isReady()){
                return true;
            }
        }
        return false;
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
