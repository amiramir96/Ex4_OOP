package director;

import api.GeoLocation;
import impGraph.Point3D;

import java.util.List;

/**
 * This class represents
 * Agent, someone that aim to be "Master Pokemon"!
 * @author boaz.benmoshe
 */
public class Agent {
    int id;
    double value;
    int src;
    int dest;
    double speed;
    GeoLocation pos;
    /**
     * constructor
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
        this.pos = new Point3D(x, y);
    }
    public Agent(){
        this.src = 0;
        this.dest = -1;
        this.speed = 1;
    }

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
