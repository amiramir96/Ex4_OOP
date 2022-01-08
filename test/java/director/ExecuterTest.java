package director;

import Graph.api.DirectedWeightedGraph;
import Graph.api.NodeData;
import game_client.Client;
import Graph.impGraph.Point2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ExecuterTest {

    Agent a0 = new Agent(0, 0, 0, -1, 3, new Point2D(1,1));
    ArrayList<Agent> agents;
    GameData gd = new GameData(new Client());
    @Test
    void timeToEndTask() {
        try {
            DirectedWeightedGraph g = Loader.loadGraph("json_graphs\\G1.json");
            gd.setCurr_graph(g);
            Executer e1 = new Executer(0, gd);
            agents = new ArrayList<>();
            agents.add(a0);
            gd.setAgents(agents);
            // case 1: no tasks
            assertEquals(e1.timeToEndTask(), 0.0);
            LinkedList<Integer> li = new LinkedList<>();
            // case 2: there is task and agent in IDLE state
            li.add(1);
            e1.setNext_stations(li);
            assertEquals(e1.timeToEndTask(), 0.41067916869001103);
            // case 3: there is task but agent is already engaged
            a0.setDest(1);
            assertEquals(e1.timeToEndTask(), 0.0);
        }
        catch(Exception e) {
                return;
            }
    }

    @Test
    void selfUpdateTimeToEndAll() {
        try {
            DirectedWeightedGraph g = Loader.loadGraph("json_graphs\\G1.json");
            gd.setCurr_graph(g);
            Executer e1 = new Executer(0, gd);
            agents = new ArrayList<>();
            agents.add(a0);
            gd.setAgents(agents);
            // case 1: empty list
            assertEquals(e1.selfUpdateTimeToEndAll(a0.getSpeed()), 0.0);
            // case 2: moving for two nodes
            LinkedList<Integer> li = new LinkedList<>();
            li.add(2);
            a0.setDest(1);
            e1.setNext_stations(li);
            assertEquals(e1.selfUpdateTimeToEndAll(a0.getSpeed()), 1.0112109692174125);
        }
        catch(Exception e) {
            return;
        }
    }

    @Test
    void addStop() {
        try {
            DirectedWeightedGraph g = Loader.loadGraph("json_graphs\\G1.json");
            gd.setCurr_graph(g);
            Executer e1 = new Executer(0, gd);
            agents = new ArrayList<>();
            agents.add(a0);
            gd.setAgents(agents);
            // case 1: task to only one node
            e1.addStop(1);
            assertEquals(e1.getNext_stations().getFirst(), 1);
            // case 2: moving for two nodes
            e1.addStop(2);
            assertEquals(e1.getNext_stations().getLast(), 2);
            // case 3: wont add same dest (last item of the next_stations list)
            e1.addStop(2);
            assertEquals(e1.getNext_stations().size(), 2);
        }
        catch(Exception e) {
            return;
        }
    }

    @Test
    void testAddManyStops() {
        try {
            DirectedWeightedGraph g = Loader.loadGraph("json_graphs\\G1.json");
            gd.setCurr_graph(g);
            Executer e1 = new Executer(0, gd);
            agents = new ArrayList<>();
            agents.add(a0);
            gd.setAgents(agents);
            //
            assertEquals(e1.selfUpdateTimeToEndAll(a0.getSpeed()), 0.0);
            LinkedList<NodeData> li = new LinkedList<>();
            li.add(gd.getCurr_graph().getNode(1)); li.add(gd.getCurr_graph().getNode(2));
            e1.addManyStops(li);
            assertEquals(e1.getNext_stations().getLast(), 2);
            assertEquals(e1.getNext_stations().size(), 2);

            li.removeFirst();
            li.add(g.getNode(3)); li.add(g.getNode(4));
            e1.getNext_stations().removeFirst();
            e1.addManyStops(li);
            assertEquals(e1.getNext_stations().size(), 3);
            assertEquals(e1.getNext_stations().get(1), 3);
        }
        catch(Exception e) {
            return;
        }
    }
}