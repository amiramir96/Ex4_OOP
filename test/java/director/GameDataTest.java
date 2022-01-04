package director;

import api.DirectedWeightedGraph;
import ex4_java_client.Client;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameDataTest {
    GameData gd = new GameData(new Client());
    @Test
    void getFreePokemons() {
        try {
            DirectedWeightedGraph g = Loader.loadGraph("json_graphs\\G1.json");
            gd.setCurr_graph(g);
            ArrayList<Pokemon> Lp = new ArrayList<>();
            Pokemon p1 = new Pokemon(1, -1, g.getNode(0).getLocation(), 0, 1);
            Pokemon p2 = new Pokemon(2, 1, g.getNode(1).getLocation(), 1, 0);
            Pokemon p3 = new Pokemon(3, -1, g.getNode(2).getLocation(), 2, 3);
            Pokemon p4 = new Pokemon(4, 1, g.getNode(3).getLocation(), 4, 3);
            Pokemon p5 = new Pokemon(5, -1, g.getNode(5).getLocation(), 4, 5);
            Lp.add(p3);
            Lp.add(p2);
            Lp.add(p1);
            gd.setPokemons(Lp);
            List<Pokemon> temp = gd.getFreePokemons();
            // naive case, nothing changed
            assertEquals(temp.get(0).getValue(), 3);

            // cases with engaged pokemons
            gd.getPokemons().get(0).setEngaged(true);
            temp = gd.getFreePokemons();
            assertEquals(temp.get(0).getValue(), 2);
            gd.getPokemons().add(0, p4);
            p5.setEngaged(true);
            gd.getPokemons().add(0, p5);
            temp = gd.getFreePokemons();
            assertEquals(temp.get(0).getValue(), 4);
        }
        catch (Exception e){
            return;
        }
    }
}