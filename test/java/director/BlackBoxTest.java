package director;

import Graph.api.DirectedWeightedGraph;
import Graph.impGraph.Point2D;
import game_client.Client;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BlackBoxTest {

    Agent a0 = new Agent(0, 0, 0, -1, 3, new Point2D(1,1));
    Agent a1 = new Agent(1, 0, 5, -1, 3, new Point2D(1,1));
    GameData gd = new GameData(new Client());
    @Test
    void runAlgorithm() {
        try {
            DirectedWeightedGraph g = Loader.loadGraph("json_graphs\\G1.json");
            gd.setCurr_graph(g);
            ArrayList<Executer> execList = new ArrayList<>();
            Executer e0 = new Executer(0, gd);
            Executer e1 = new Executer(1, gd);
            e0.setExecAgent(a0); e1.setExecAgent(a1);
            execList.add(e0); execList.add(e1);
            BlackBox bb = new BlackBox(gd, execList);
            // bdikat shfiut!
            assertTrue(execList.get(0).next_stations.isEmpty());
            assertTrue(execList.get(1).next_stations.isEmpty());
            // engage from 3 pokemons, the 2 most valued, p2 value 15 to agent 0, p1 value 20 to agent 1
            ArrayList<Pokemon> pokemons = new ArrayList<>();
            pokemons.add(new Pokemon(14, 1, new Point2D(9, 9), 5, 6)); // p0
            pokemons.add(new Pokemon(20, 1, new Point2D(10, 10), 7, 8)); // p1
            pokemons.add(new Pokemon(15, 1, new Point2D(2, 2), 1, 2)); // p2;
            Collections.sort(pokemons);
            this.gd.setPokemons(pokemons);
            ArrayList<Agent> agents = new ArrayList<>();
            agents.add(a0); agents.add(a1);
            this.gd.setAgents(agents);
            bb.runAlgorithm();
            assertEquals(e0.getNext_stations().getFirst(), 1);
            assertEquals(e0.getNext_stations().getLast(), 2);
            assertEquals(e1.getNext_stations().getLast(), 8);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}