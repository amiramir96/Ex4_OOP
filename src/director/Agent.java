package director;

import Graph.api.GeoLocation;
import Graph.impGraph.Point2D;

/**
 * This class represents
 * Agent, someone that aim to be "Master Pokemon"!
 * @author boaz.benmoshe
 */
public class Agent {
    // fields of agents - pretty basic
    int id; // serial

    // the below fields is always updating!
    double value; // sum val of pokemons agent catched
    int src;
    int dest;
    double speed;
    GeoLocation pos;

    /**
     * @param ID - serial
     * @param v - sum value of pokemons agent catched
     * @param s - src node
     * @param d - dest node (where agent is moving right now, -1 if no moving)
     * @param sp - speed
     * @param p - position (cordinates on map)
     */
    public Agent(int ID, double v, int s, int d, double sp, GeoLocation p){
        this.id = ID;
        this.value = v;
        this.src = s;
        this.dest = d;
        this.speed = sp;
        this.pos = p;
    }

    public Agent(int ID, double v, int s, int d, double sp, double x, double y){
        this.id = ID;
        this.value = v;
        this.src = s;
        this.dest = d;
        this.speed = sp;
        this.pos = new Point2D(x, y);
    }
    public Agent(){
        this.src = 0;
        this.dest = -1;
        this.speed = 1;
    }

    /** ---------------------------------- GETTERS AND SETTERS----------------------------------------*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public GeoLocation getPos() {
        return pos;
    }

    public void setPos(GeoLocation pos) {
        this.pos = pos;
    }

    @Override
    public String toString(){
        return "Agent id: "+this.getId()+" src_node: "+this.getSrc()+" dest_node: "+this.getDest()+" speed: "+this.getSpeed()+" pos: "+this.getPos();
    }
}
