package director;

import api.GeoLocation;
import impGraph.Point2D;

/**
 * this class represents Pokemon
 * this class is COMPARABLE - can be used via java built in sort
 *      the sort is via the value of the pokemon, but reverse (if p1.val > p2.val then return -1 which is "reversed" value order)
 */
public class Pokemon implements Comparable{
    double value;
    int type;
    GeoLocation pos;
    int src, dest; // which edge the pokemon stands on
    boolean engaged; // engaged to an agent already
    private double mark; // inner field help to know if this pokemon is exist in the server game world
    private int id; // inner field shall be used only with graphics decides which type of pokemon it is :)

    /**
     * Constructors
     * @param v - value
     * @param t - type
     * @param p - point2D
     *          via the below two we know which edge the pokemon stands on
     * @param src - source node
     * @param dest - dest node
     */
    public Pokemon(double v, int t, GeoLocation p, int src, int dest){
        this.value = v;
        this.type = t;
        this.pos = p;
        this.src = src;
        this.dest = dest;
        this.engaged = false;
        this.id = transVal_to_ImageNum();
    }

    public Pokemon(double v, int t, GeoLocation p, int src, int dest, double m){
        this.value = v;
        this.type = t;
        this.pos = p;
        this.src = src;
        this.dest = dest;
        this.engaged = false;
        this.mark = m;
        this.id = transVal_to_ImageNum();
    }

    public Pokemon(double v, int t, double x, double y, int src, int dest){
        this.value = v;
        this.type = t;
        this.pos = new Point2D(x, y);
        this.src = src;
        this.dest = dest;
        this.engaged = false;
        this.id = transVal_to_ImageNum();
    }

    public Pokemon(){
        this.value = 0;
    }

    /**
     * the sort is via the value of the pokemon,
     *      but reverse (if p1.val > p2.val then return -1 which is "reversed" value order)
     * @param o - pokemon object
     * @return currPok_val is better: -1, any other option: 1
     */
    @Override
    public int compareTo(Object o) {
        if (this.getValue() - ((Pokemon)o).getValue() < 0){
            return 1;
        }
        else {
            return -1;
        }
    }

    /**
     * xfer the value of the pokemon to serial id number that represent the image name of the pokemon
     * @return id of the pokemon
     */
    private int transVal_to_ImageNum() {
        return ((int)this.value)%20;
    }

    /** ---------------------------------GETTERS AND SETTERS----------------------------------------*/

    public void setValue(double value) {
        this.value = value;
        this.id = transVal_to_ImageNum();
    }

    public double getValue() {
        return value;
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

    protected double getMark() {
        return mark;
    }

    protected void setMark(double mark) {
        this.mark = mark;
    }

    public String toString(){
        return "Poki id: "+this.getId()+" value: "+this.getValue()+" on edge from: "+this.getSrc()+" to: "+this.getDest() +" engaged: ";
    }

}
