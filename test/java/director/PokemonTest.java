package director;

import Graph.api.DirectedWeightedGraph;
import game_client.Client;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {

    /**
     * check if we able to sort list of pokemons and its will be sorted via Value field
     */
    @Test
    void compareTo() {
        GameData gd = new GameData(new Client());
        try {
            DirectedWeightedGraph g = Loader.loadGraph("json_graphs\\G1.json");
            gd.setCurr_graph(g);
            ArrayList<Pokemon> Lp = new ArrayList<>();
            Pokemon p1 = new Pokemon(1, -1, g.getNode(0).getLocation(), 0, 1);
            Pokemon p2 = new Pokemon(2, 1, g.getNode(1).getLocation(), 1, 0);
            Pokemon p3 = new Pokemon(3, -1, g.getNode(2).getLocation(), 2, 3);
            Pokemon p4 = new Pokemon(4, 1, g.getNode(3).getLocation(), 4, 3);
            Pokemon p5 = new Pokemon(5, -1, g.getNode(5).getLocation(), 4, 5);
            Lp.add(p1);
            Lp.add(p2);
            Lp.add(p3);
            Lp.add(p4);
            Lp.add(p5);
            gd.setPokemons(Lp);
            List<Pokemon> temp = gd.getPokemons();
            Collections.sort(temp);
            assertEquals(temp.get(0).getValue(), 5);
            assertEquals(temp.get(1).getValue(), 4);
            assertEquals(temp.get(2).getValue(), 3);
            assertEquals(temp.get(3).getValue(), 2);
            assertEquals(temp.get(4).getValue(), 1);

        }
        catch (Exception e){

        }
    }
}