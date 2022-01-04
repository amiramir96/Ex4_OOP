package Graph.impGraph;

import Graph.api.EdgeData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * implements our own iterator class for iterEdge(node_id)
 * why? ez and ordered way to avoid from iteration on null (in place of iterating on hashmap size 0)
 */

public class edgeIter implements Iterator<EdgeData> {

    Dwg currGraph;
    int originModeCounter;
    Iterator<EdgeData> edgeIterator; // regular iterator implements remove
    EdgeData tempE;

    // constructor for specific node iterator (iterate over all edges that going from that node)
    //the iterator will serve as sub-iterator of the mergedIterator class
    edgeIter(Dwg g, int node_id){
        this.currGraph = g;
        this.originModeCounter = g.getMC();
        // below is the main reason why we use this class and not "unnamed iterator"
        if (this.currGraph.edgeOutMap.get(node_id%1000).containsKey(node_id)){
            edgeIterator = this.currGraph.edgeOutMap.get(node_id%1000).get(node_id).values().iterator();
        }
        else {
            edgeIterator = new HashMap<Integer, EdgeData>().values().iterator(); //will iterate over 0 items but not null!
        }
    }

    @Override
    public boolean hasNext() {
        return edgeIterator.hasNext();
    }

    @Override
    public EdgeData next() {
        tempE = edgeIterator.next();
        return tempE;
    }

    @Override
    public void remove() {
        edgeIterator.remove();
        this.currGraph.removeEdgeIn(tempE.getSrc(), tempE.getDest());
        this.originModeCounter = this.currGraph.getMC();

    }

    // https://stackoverflow.com/questions/42465871/whats-the-point-of-having-both-iterator-foreachremaining-and-iterable-foreach/42466144
    // comment #1, helped to understand how its worked
    @Override
    public void forEachRemaining(Consumer<? super EdgeData> action) {
        while(edgeIterator.hasNext()){
            action.accept(next());
        }
    }
}
