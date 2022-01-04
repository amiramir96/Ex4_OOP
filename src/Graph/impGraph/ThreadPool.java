package Graph.impGraph;

import Graph.api.DirectedWeightedGraph;
import Graph.api.NodeData;
import Graph.graphAlgo.Dijkstra;
import java.util.List;

public class ThreadPool extends Thread{
    /**
     * this object purpose is to make (inherite from) Thread that help to the Center function of DwgMagic
     * for more data on the whole "picture" look at center function in DwgMagic class
     */
    List<NodeData> nodeList; // use to iterate over all the engaged nodes (from the dwgMagic center)
    int id; // serial num of the threads.. ezier for tests
    public int centerForNodeList; // the end line
    public double shortest; // var
    DirectedWeightedGraph currGraph; // relevant graph

    /**
     * constructor
     * @param dijList - list of nodes which threadpool shall use dijkstra on
     * @param serialnum - as named
     * @param g - graph
     */
    ThreadPool(List<NodeData> dijList, int serialnum, DirectedWeightedGraph g){
        this.nodeList = dijList;
        this.id = serialnum;
        this.shortest = Double.MAX_VALUE;
        this.currGraph = g;
    }

    /**
     * loop over all the node_list and  use dijkstra
     * takes shortest path between all the longests of the dijkstra of ea node
     */
    @Override
    public void run() {
        Dijkstra tempDij; // just var
        for (NodeData d : nodeList){
            // use dijkstra on curr node and save his longest value path if its the shortest till now (and the node_id)
            tempDij = new Dijkstra(this.currGraph, d);
            tempDij.mapPathDijkstra(d);
            if (tempDij.longestPath() < this.shortest){ // update shortest
                this.shortest = tempDij.longestPath;
                this.centerForNodeList = d.getKey();
            }
        }
    }
}
