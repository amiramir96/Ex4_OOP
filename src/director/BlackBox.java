package director;

/**
 * this is the ALGORITHM of the game
 * hold method and in the end decides how and to which agent to engage each pokemon
 */
public class BlackBox {
    GameData currGD;

    public BlackBox(GameData gd){
        this.currGD = gd;
    }
}
