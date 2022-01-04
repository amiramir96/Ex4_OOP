package Graph.impGraph;

import Graph.api.NodeData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

public class nodeIter implements Iterator<NodeData> {

    Dwg currGraph;
    int originModeCounter;
    Iterator<NodeData> nodeIterator; // regular iterator DO NOT REMOVE ANY ITEM FROM!
    NodeData tempN;

    // constructor for specific idx of the list (nodemaps) an iterator
    nodeIter(Dwg g, HashMap<Integer, NodeData> node_list){
        this.currGraph = g;
        this.originModeCounter = g.getMC();
        // below is the main reason why we use this class and not "unnamed iterator"
        if (!node_list.isEmpty()){
            this.nodeIterator = node_list.values().iterator();
        }
        else {
            this.nodeIterator = new HashMap<Integer, NodeData>().values().iterator(); //will iterate over 0 items but not null!
        }
    }

    @Override
    public boolean hasNext() {
        if (tempN != null){
        }
        return nodeIterator.hasNext();
    }

    @Override
    public NodeData next() {
        this.tempN = nodeIterator.next();
        return this.tempN;
    }

    @Override
    public void remove() {
        nodeIterator.remove();;
        this.currGraph.removeNodeInOut(tempN.getKey());
        this.originModeCounter = this.currGraph.getMC();

    }

    // https://stackoverflow.com/questions/42465871/whats-the-point-of-having-both-iterator-foreachremaining-and-iterable-foreach/42466144
    // comment #1, helped to understand how its worked
    @Override
    public void forEachRemaining(Consumer action) {
        while(nodeIterator.hasNext()){
            action.accept(next());
        }
    }
}