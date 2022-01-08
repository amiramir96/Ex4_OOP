package director;

import Graph.api.DirectedWeightedGraph;
import Graph.api.EdgeData;
import Graph.api.NodeData;
import java.util.LinkedList;
import java.util.List;

/**
 * this class is responsible on specific agent - marked as agent_id
 * to communicate with the server about his next mission (send nextEdge commands)
 */
public class Executer implements Runnable{
    GameData gd;
    int agent_id;
    LinkedList<Integer> next_stations; // orddered list of stations, ea int represent node_id, first item is always the next stop
    Agent x; // ez field to work with -> just a temporary pointer
    double timeToEndAll; // seconds to end all the tasks in the stations list
    DirectedWeightedGraph currGraph;
    private int currDest;

    /** Constructor */
    public Executer(int id, GameData g){
        this.agent_id = id;
        this.gd = g;
        this.currGraph = g.getCurr_graph();
        this.timeToEndAll = 0; // seconds
        this.next_stations = new LinkedList<>();
    }

    /**
     * @return time to end the next task (curr pos of agent -> first item in linked list
     * return 0 if:
     *  ->there is no task that the agent moving to currectly
     *
     */
    double timeToEndTask(){
        this.x = gd.getAgents().get(agent_id);
        // no curr task ongoing
        if (this.next_stations == null || this.next_stations.isEmpty()){
            return 0;
        }
        if (this.next_stations.getFirst() == currDest){
            this.next_stations.removeFirst();
            return 0;
        }
        if (this.x.getDest() != -1){
            return 0;
        }
        else {
            // there is task ongoing -> return time to move from src->dest of the edge
            int src = this.x.getSrc();
            int dest = this.next_stations.getFirst();
            EdgeData tempE = this.currGraph.getEdge(src, dest);
            if (tempE == null){
                return 0;
            }
            else {
                return tempE.getWeight()/this.x.getSpeed();
            }
        }
    }

    /**
     * loop over the whole next_stations list and calculate time to get to its end
     * @param speed - updated speed of the agent
     * @return total time to END ALL TASKS
     */
    public double selfUpdateTimeToEndAll(double speed){
        if (this.next_stations == null || this.next_stations.isEmpty()){
            // zero for no tasks
            this.timeToEndAll = 0;
            return 0;
        }
        double temp = 0;
        int src, dest;

//        x = this.gd.getAgents().get(agent_id);
        EdgeData tempE =  this.currGraph.getEdge(x.getSrc(), this.next_stations.getFirst());
        if (tempE != null){
            temp += tempE.getWeight();
        }
        for (int i=0; i < this.next_stations.size()-1; i++){
            // loop over all the tasks and updated the time for that
            src = this.next_stations.get(i);
            dest = this.next_stations.get(i+1);
            temp += this.currGraph.getEdge(src, dest).getWeight();
        }
        // return total
        this.timeToEndAll = temp/speed;
        return this.timeToEndAll;
    }

    /**
     * @return if agent of the execeuter is ready to get a new Task (moving to catch pokemon)
     */
    public boolean isReady(){
        if (this.x.getSrc() == this.currDest && this.next_stations.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * while thread is on:
     * 1. calculate time of the next task
     * 2. cases:
     * case 2:
     *      agent is in "idle" mode
     *      -> engaged him to his next stop :)
     *  case 2:
     *      agent is still meanwhile moving to some node
     *      -> do nothing
     * this thread is work super fast since its only if command and one linear function to run in background
     */
    @Override
    public void run() {
        double t;
        t = timeToEndTask();
        if (t != 0) {
//            this.x = this.gd.getAgents().get(agent_id);
            this.currDest = this.next_stations.getFirst();
            this.gd.getCurr_client().chooseNextEdge("{\"agent_id\":" + agent_id + ", \"next_node_id\": " + this.next_stations.getFirst() + "}");
            this.getNext_stations().removeFirst();
        }

    }

    //------------------------------------------------------------------------------------------------------------------
    /** functions that responsible for adding "stops"-> nodes that the agent have to move to-> to the next_statins list*/

    public void addStop(int node_id){
        if (!this.next_stations.isEmpty() && this.next_stations.getLast() == node_id){
            return;
        }
        else {
            this.next_stations.addLast(node_id);
        }
    }

//    public void addManyStops(LinkedList<Integer> nodes){
//        this.next_stations.addAll(nodes);
//    }

    /**
     * add list of nodes to next_stations
     * @param nodes - list of nodes to add to the next_stations list
     */
    public void addManyStops(List<NodeData> nodes){
        for (NodeData node : nodes){
            if (this.next_stations.isEmpty() || this.next_stations.getLast() != node.getKey()){
                this.next_stations.addLast(node.getKey());
            }
        }
    }
    // -----------------------------------------------------------------------------------------------
    /** ------------------------ SETTER AND GETTERS ---------------------------------------------*/

    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public LinkedList<Integer> getNext_stations() {
        return next_stations;
    }

    public void setNext_stations(LinkedList<Integer> next_stations) {
        this.next_stations = next_stations;
    }

    public GameData getGd() {
        return gd;
    }

    public void setGd(GameData gd) {
        this.gd = gd;
    }


    public double getTimeToEndAll() {
        return timeToEndAll;
    }

    public void setTimeToEndAll(double timeToEndAll) {
        this.timeToEndAll = timeToEndAll;
    }
    public Agent getExecAgent() {
        return x;
    }

    public void setExecAgent(Agent x) {
        this.currDest = x.getSrc();
        this.x = x;
    }
}
