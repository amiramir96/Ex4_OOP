package director;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
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
        if (this.x.getDest() != -1){
            return 0;
        }
        else {
            if (this.next_stations == null || this.next_stations.isEmpty()){
                return 0;
            }

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

        x = this.gd.getAgents().get(agent_id);
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
            this.x = this.gd.getAgents().get(agent_id);
            this.gd.getCurr_client().chooseNextEdge("{\"agent_id\":" + agent_id + ", \"next_node_id\": " + this.getNext_stations().getFirst() + "}");
            this.getNext_stations().removeFirst();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    /** functions that responsible for adding "stops"-> nodes that the agent have to move to-> to the next_statins list*/

    public void addStop(int node){
        if (!this.next_stations.isEmpty() && this.next_stations.getLast() == node){
            return;
        }
        else {
            this.next_stations.addLast(node);
        }
    }

    public void addManyStops(LinkedList<Integer> nodes){
        this.next_stations.addAll(nodes);
    }
    public void addManyStops(List<NodeData> nodes){
        for (NodeData node : nodes){
            if (!this.next_stations.isEmpty() && this.next_stations.getLast() == node.getKey()){
                continue;
            }
            else {
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
}
