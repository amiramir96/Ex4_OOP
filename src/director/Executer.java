package director;

import api.EdgeData;

import java.util.LinkedList;
import java.util.List;

/**
 * this class is responsible on specific agent - marked as agent_id
 * to communicate with the server about his next mission (send nextEdge commands)
 */
public class Executer implements Runnable{
    GameData gd;
    int agent_id;
    LinkedList<Integer> next_stations;
    Agent x;
    boolean flag;

    public Executer(int id, GameData g){
        this.agent_id = id;
        this.gd = g;
        this.flag = true;
    }
    public Executer(int id, LinkedList<Integer> stations, GameData g){
        this.agent_id = id;
        this.next_stations = stations;
        this.gd = g;
        this.flag = true;
    }

    /**
     * @return time to end the next task (curr pos of agent -> first item in linked list
     */
    double timeToEndTask(){
        this.x = gd.getAgents().get(agent_id);
        if (this.x.getDest() != -1){
            return 0;
        }
        else {
            if (this.next_stations == null || this.next_stations.isEmpty()){
                return 0;
            }
            int src = this.x.getSrc();
            int dest = this.next_stations.getFirst();
            EdgeData tempE = gd.getCurr_graph().getEdge(src, dest);
            if (tempE == null){
                return 0;
            }
            else {
                return tempE.getWeight()/this.x.getSpeed();
            }
        }
    }

    /**
     * while thread is on:
     * 1. calculate time of the next task
     * 2. cases:
     *  case 1:
     *      agent is still meanwhile moving to some node
     *      -> sleep 10 miliseconds and then check again.
     *  case 2:
     *      agent is in "idle" mode
     *      -> engaged him to his next stop :)
     * this thread will stop if and only if the game got to its end
     */
    @Override
    public void run() {
        double t;
        while (flag){
            t = timeToEndTask();
            if (t == 0){ //
                try { // unable to engage the agent to next node (or there is no next stops)
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                try { // engage the agent to his next stop
                    this.x = this.gd.getAgents().get(agent_id);
                    // "{\"agent_id\":" + id + ", \"next_node_id\": " + dest + "}"
                    this.gd.getCurr_client().chooseNextEdge("{\"agent_id\":" + agent_id + ", \"next_node_id\": " + this.getNext_stations().getFirst() + "}");
                    this.getNext_stations().removeFirst();
                    Thread.sleep((long) (t*1000));
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


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
    public void setRun(boolean f){
        this.flag = f;
    }
    public boolean getRunStatus(){
        return this.flag;
    }
    public void addStop(int node){
        this.next_stations.addFirst(node);
    }
}
