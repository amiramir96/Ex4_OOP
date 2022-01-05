package director;

import java.util.LinkedList;

/**
 * this is the ALGORITHM of the game
 * hold method and in the end decides how and to which agent to engage each pokemon
 */
public class Help_Funcs {
    GameData currGD;

    public Help_Funcs(GameData gd){
        this.currGD = gd;
    }

    public static boolean is_edge_included(LinkedList<Integer> stops, int src, int dest){
        for (int i = 0; i < stops.size() - 1; i++) {
            if(src == stops.get(i) && dest == stops.get(i+1))
                return true;
        }
        return false;
    }
}
