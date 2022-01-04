package Graph.impGraph;

import Graph.api.GeoLocation;
import Graph.api.NodeData;

/**
 * implement as writen in the interface.
 */
public class Node implements NodeData {
    private final Point2D cord; // point on plane
    private final int _id; // serial num
    private int tag; // for user decision
    private String metaData; // same as above
    private double weight; // same as above

    /**
     * const
     * @param cord - point
     * @param id - serial num
     */
    public Node(Point2D cord, int id) {
        this.cord = new Point2D(cord);
        this._id = id;
        this.tag = 0;
        this.metaData = "";
        this.weight = Double.POSITIVE_INFINITY;
    }

    /**
     * deepCopy
     * @param n - node
     */
    public Node(NodeData n){
        this.cord = new Point2D(n.getLocation());
        this._id = n.getKey();
        this.tag = n.getTag();
        this.metaData = n.getInfo();
        this.weight = n.getWeight();
    }

    /**
     * via interface
     * @return _id
     */
    @Override
    public int getKey() {
        return this._id;
    }

    /**
     * via interface
     * @return point
     */
    @Override
    public GeoLocation getLocation() {
        return this.cord;
    }

    /**
     * via interface
     * @param p - new location  (position) of this node.
     */
    @Override
    public void setLocation(GeoLocation p) {
        this.cord._x = p.x();
        this.cord._y = p.y();
    }

    /**
     * via interface
     * @return metaData
     */
    @Override
    public String getInfo() {
        return this.metaData;
    }

    /**
     * via interface
     * @param s - new metaData
     */
    @Override
    public void setInfo(String s) {
        this.metaData = "" + s;
    }

    /**
     * via interface
     * @return - tag
     */
    @Override
    public int getTag() {
        return this.tag;
    }

    /**
     * via interface
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    // irrelevant for this exercise
    @Override
    public double getWeight() {
        return this.weight;
    }
    // irrelevant for this exercise
    @Override
    public void setWeight(double w) {this.weight = w;}

}
