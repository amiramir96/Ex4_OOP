package director;

import api.GeoLocation;
import impGraph.Point3D;

public class Pokemon implements Comparable{
    double value;
    int type;
    GeoLocation pos;
    int id; // decides which type of pokemon it is :)
    int src, dest; // which edge the pokemon stands on
    boolean engaged; // engaged to an agent already
    double mark;

    public Pokemon(double v, int t, GeoLocation p, int src, int dest){
        this.value = v;
        this.type = t;
        this.pos = p;
        this.id = transVal_to_ImageNum();
        this.src = src;
        this.dest = dest;
        this.engaged = false;
    }

    public Pokemon(double v, int t, GeoLocation p, int src, int dest, double m){
        this.value = v;
        this.type = t;
        this.pos = p;
        this.id = transVal_to_ImageNum();
        this.src = src;
        this.dest = dest;
        this.engaged = false;
        this.mark = m;
    }

    public Pokemon(double v, int t, double x, double y, int src, int dest){
        this.value = v;
        this.type = t;
        this.pos = new Point3D(x, y);
        this.id = transVal_to_ImageNum();
        this.src = src;
        this.dest = dest;
        this.engaged = false;
    }

    public Pokemon(){
        this.value = 0;
    }


    private int transVal_to_ImageNum() {
        return ((int)this.value)%100;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        this.id = transVal_to_ImageNum();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GeoLocation getPos() {
        return pos;
    }

    public void setPos(GeoLocation pos) {
        this.pos = pos;
    }

    @Override
    public int compareTo(Object o) {
        if (this.getValue() - ((Pokemon)o).getValue() < 0){
            return 1;
        }
        else {
            return -1;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isEngaged() {
        return engaged;
    }

    public void setEngaged(boolean engaged) {
        this.engaged = engaged;
    }

    public String toString(){
        return "Poki id: "+this.getId()+" value: "+this.getValue()+" on edge from: "+this.getSrc()+" to: "+this.getDest() +" engaged: ";
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }
}
