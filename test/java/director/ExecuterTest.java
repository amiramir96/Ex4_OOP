package director;

import Graph.api.DirectedWeightedGraph;
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
            LinkedList<Integer> li = new LinkedList<>();
            // case 2: task to only one node
            li.add(1);
            e1.setNext_stations(li);
            assertEquals(e1.selfUpdateTimeToEndAll(a0.getSpeed()), 0.41067916869001103);
            // case 3: moving for two nodes
            li.add(2);
            a0.setDest(1);
            assertEquals(e1.selfUpdateTimeToEndAll(a0.getSpeed()), 1.0112109692174125);
        }
        catch(Exception e) {
            return;
        }
    }

    @Test
    void addStop() {
    }

    @Test
    void addManyStops() {
    }

    @Test
    void testAddManyStops() {
    }
}