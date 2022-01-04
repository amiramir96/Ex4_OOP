package Graph.impGraph;

import Graph.api.EdgeData;

/**
 * implemented as writen in the interface EdgeData
 */
public class Edge implements EdgeData {
    private final int _src, _dest;
    private int tag; // via interface
    private final double _weight; // via interface
    private String metaData; // via interface

    /**
     * constructor
     * @param src - start node
     * @param weight - weight of edge
     * @param dest - destination node
     */
    public Edge(int src, double weight, int dest) {
        this._src = src;
        this._weight = weight;
        this._dest = dest;
        this.tag = 0;
        this.metaData = "";
    }

    /**
     * copy const (deep copy
     * @param e - edge
     */
    public Edge(EdgeData e){
        this._src = e.getSrc();
        this._weight = e.getWeight();
        this._dest = e.getDest();
        this.tag = e.getTag();
        this.metaData = e.getInfo();
    }

    /**
     * via interface
     * @return src
     */
    @Override
    public int getSrc() {
        return this._src;
    }

    /**
     * via interface
     * @return dest
     */
    @Override
    public int getDest() {
        return this._dest;
    }

    /**
     * via interface
     * @return weight
     */
    @Override
    public double getWeight() {
        return this._weight;
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
     * @param s - new string to update metaData
     */
    @Override
    public void setInfo(String s) {
        this.metaData = "" + s;
    }

    /**
     * via interface
     * @return tag
     */
    @Override
    public int getTag() { return this.tag; }

    /**
     * via interface
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }
}
