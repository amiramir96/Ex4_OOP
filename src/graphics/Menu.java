package graphics;

import api.*;
import impGraph.Dwg;
import impGraph.DwgMagic;
import impGraph.Node;
import impGraph.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * inherite from JMenuBar -> will hold all features from the menu bar of the window
 * will sync and execute drawGraph obj which connected to window as well
 * implements ActionListener to be able to rule over actions like button clicks on items
 * GUIDE:
 * categories:
 * 1- File:
 *  1.1- loadGraph (load file from pc)
 *  1.2- saveGraph (save graph json file at directory)
 * 2- Algo_Command:
 *  2.1- isConnected: draw all nodes: green = true, red = false
 *  2.2- center: draw center node with bright yellow (will alert if graph has no center)
 *  2.3- shortestPath: draw all nodes of the path as Pink, all edges as Red => if there is no path, wont do anything
 *  2.4- shortestPathDist: draw Path: green=node, pink=edge, popup msg with distance of the path => if no path, wont do anything
 *  2.5- tsp: Green-nodes part of the TSP path, pink=edges, write every station that ea node have to visit,
 *       if there is no solution - draw all cities node with Red
 * 3- Graph_Management:
 *  3.1- add Node- have to get input of node_id,x_cord,y_cord or will popup msg to try again
 *  3.2- add edge- input via node_id1,node_id2,weight (problem will pop up msg)
 *  3.3- del node- input node_id and remove it.
 *  3.4- del edge- input node_id1,node_id2 and remove it
 *  3.5- create new dwg- creates new empty directed weighted garph
 */
public class Menu extends JMenuBar implements ActionListener {
    // vars
    Window w;
    DirectedWeightedGraphAlgorithms algoGraph;
    JMenu menu, runAlgo, graphToolBar; // categories
    // items for ea category
    JMenuItem exitGUI, loadGraph, saveGraph;
    JMenuItem isConnected, center, shortestPathDist, shortestPath, tsp;
    JMenuItem addNode, delNode, addEdge, delEdge, newDwg;
    JFileChooser fileChooser;
    // for drawing
    DrawGraph drawer;
    JTextField userTextIn;
    JButton submitBut;
    // actions
    ActionEvent funcEvent;

    /**
     * constructor
     * @param g - algo graph
     * @param d - draw graph obj
     * @param w - curr window we works on
     */
    public Menu(DirectedWeightedGraphAlgorithms g, DrawGraph d, Window w) {
        this.w = w;
        // init main objects
        this.drawer = d;
        this.algoGraph = g;
        this.menu = new JMenu("File");
        this.fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
        // init menu items ("buttons")
        this.exitGUI = new JMenuItem("exit");
        this.loadGraph = new JMenuItem("load graph");
        this.saveGraph = new JMenuItem("save graph");
        this.exitGUI.addActionListener(this);
        this.loadGraph.addActionListener(this);
        this.saveGraph.addActionListener(this); // add them to the bar
        this.menu.add(this.exitGUI);
        this.menu.add(this.loadGraph);
        this.menu.add(this.saveGraph);


        // init one more tool options for the menu
        this.runAlgo = new JMenu("Algo_Command");

        this.isConnected = new JMenuItem("isConnected");
        this.center = new JMenuItem("center");
        this.shortestPath = new JMenuItem("shortestPath");
        this.shortestPathDist = new JMenuItem("shortestPathDist");
        this.tsp = new JMenuItem("tsp");

        this.isConnected.addActionListener(this);
        this.center.addActionListener(this);
        this.shortestPath.addActionListener(this);
        this.shortestPathDist.addActionListener(this);
        this.tsp.addActionListener(this);

        // add options to "RunAlgorithm" menu
        this.runAlgo.add(this.isConnected);
        this.runAlgo.add(this.center);
        this.runAlgo.add(this.shortestPath);
        this.runAlgo.add(this.shortestPathDist);
        this.runAlgo.add(this.tsp);

        this.graphToolBar = new JMenu("Graph_Management");

        this.addNode = new JMenuItem("add node");
        this.delNode = new JMenuItem("delete node");
        this.addEdge = new JMenuItem("add edge");
        this.delEdge = new JMenuItem("delete edge");
        JSeparator nothing = new JSeparator(); // create lovely "--------" between options ^^
        this.newDwg = new JMenuItem("create empty graph");

        // as requried
        this.addNode.addActionListener(this);
        this.delNode.addActionListener(this);
        this.addEdge.addActionListener(this);
        this.delEdge.addActionListener(this);
        this.newDwg.addActionListener(this);

        this.graphToolBar.add(this.addNode);
        this.graphToolBar.add(this.delNode);
        this.graphToolBar.add(this.addEdge);
        this.graphToolBar.add(this.delEdge);
        this.graphToolBar.add(nothing);
        this.graphToolBar.add(this.newDwg);

        this.add(this.menu);
        this.add(this.runAlgo);
        this.add(this.graphToolBar);
    }

    /**
     * main function of class - react for click on one of the items
     * @param menuClickedEvent - Event
     */
    @Override
    public void actionPerformed(ActionEvent menuClickedEvent) {
        this.funcEvent = menuClickedEvent; // just var, for more comfort usage
        if (menuClickedEvent.getSource() == this.exitGUI){ // shut down game
            this.drawer.exitFlag = true;
        }
        else if (menuClickedEvent.getSource() == this.loadGraph) { // load graph
            this.fileChooser.showOpenDialog(null);
            if (this.fileChooser.getSelectedFile() == null) {
                return; // if nothing choosed just exit this proccess
            }
            else {
                // we can load item!
                this.algoGraph.load(this.fileChooser.getSelectedFile().getPath());
                this.drawer.updateDrawer(this.algoGraph);
                this.drawer.setColors(this.drawer.defNode, this.drawer.defEdge);
                getTopLevelAncestor().repaint();
            }
        }
        else if (menuClickedEvent.getSource() == this.saveGraph) { // save graph
            this.fileChooser.showSaveDialog(null);
            if (this.fileChooser.getSelectedFile() == null) {
                return; // nothing has been choosed
            }
            else {
                this.algoGraph.save(this.fileChooser.getSelectedFile().getPath());
            }
        }
        else if (menuClickedEvent.getSource() == this.isConnected) { // is connected
            drawer.setFlagTsp(false); // not tsp process
            this.drawer.setFlagAllSameColor(true); // we shall draw all nodes with green/red as boolean retuns from the isConnected()
            if (this.algoGraph.isConnected()) { // define colors etc as bullet 2.1 description
                this.drawer.setColors(Color.GREEN, drawer.defEdge);
            }
            else {
                this.drawer.setColors(Color.RED, drawer.defEdge);
            }
            getTopLevelAncestor().repaint();
        }
        else if (menuClickedEvent.getSource() == this.shortestPath || menuClickedEvent.getSource() == this.tsp || menuClickedEvent.getSource() == this.shortestPathDist) { // shortest path/dist and tsp
            //they all kinda same case (have to get input from user)
            // init
            JFrame n = new JFrame();
            JLabel nLabel = new JLabel();
            // check which func have been selected
            if (menuClickedEvent.getSource() == this.shortestPath){
                n.setTitle("input for shortestPath");
                nLabel.setText("<html> please set pair integers <br>via pattern 'int,int'</html>");
            }
            else if(menuClickedEvent.getSource() == this.shortestPathDist){
                n.setTitle("input for shortestPathDist");
                nLabel.setText("<html> please set pair integers <br>via pattern 'int,int'</html>");
            }
            else if(menuClickedEvent.getSource() == this.tsp){
                n.setTitle("input for tsp");
                nLabel.setText("<html> please set integers sequence <br>via pattern 'int,int,...,int'</html>");
            }
            // dispose - wont close other JFrame that opens (for ex- window)
            n.setDefaultCloseOperation(n.DISPOSE_ON_CLOSE);
            n.setLayout(new FlowLayout());
            n.setLocationRelativeTo(null);
            this.submitBut = new JButton("Submit"); // create submit button for input bar
            // get input this way
            this.submitBut.addActionListener(event -> {
                JLabel tempLabel = new JLabel();
                try { // for wrong input wont bomb the program, will popup msg to user
                    if (event.getSource() == submitBut){ // react only after submit button has been clicked
                        // init input from user
                        String s = userTextIn.getText();
                        String[] str = s.split(",");
                        ArrayList<Integer> nodes = new ArrayList<>();
                        for (String n1 : str){
                            nodes.add(Integer.parseInt(n1));
                        }
                        if (funcEvent.getSource() == shortestPath){ // funcEvent is shortestPath
                            if (nodes.size() > 2){ // throw user out to catch block for invalidInput
                                invalidInput();
                            }
                            // init params as bullet 2.3
                            drawer.specialNodes = algoGraph.shortestPath(nodes.get(0), nodes.get(1));
                            drawer.specialEdges = currPathEdges(drawer.specialNodes);
                            drawer.setFlagTsp(false);
                            drawer.setFlagAllSameColor(false);
                            drawer.setColors(Color.PINK, Color.RED);
                        }
                        else if (funcEvent.getSource() == shortestPathDist){ // funcEvent is shortestPathDist
                            if (nodes.size() > 2){ // throw user out to catch block for invalidInput
                                invalidInput();
                            }
                            // init data for being able to draw it
                            drawer.specialNodes = algoGraph.shortestPath(nodes.get(0), nodes.get(1));
                            drawer.specialEdges = currPathEdges(drawer.specialNodes);
                            double distToPrint = algoGraph.shortestPathDist(nodes.get(0), nodes.get(1));
                            drawer.setFlagTsp(false);
                            drawer.setFlagAllSameColor(false);
                            drawer.setColors(Color.PINK, Color.GREEN);
                            // init pop up msg to user with the data of the distance path
                            tempLabel.setText("<html> shortestPathDist between: "+nodes.get(0)+" to "+nodes.get(1)+"<br>"+distToPrint +"</html>");
                            JFrame j = new JFrame();
                            j.setBounds(200,200, 300, 100);
                            j.setTitle("shortestPathDist");
                            j.setLocationRelativeTo(null);
                            JButton okBut = new JButton();
                            okBut.setPreferredSize(new Dimension(60,40));
                            okBut.setText("OK");
                            okBut.addActionListener(eq -> {
                                if (eq.getSource() == okBut){
                                    j.setVisible(false);
                                    j.dispose();
                                }
                            });
                            JPanel tq = new JPanel(); // the pop up msg obj
                            tq.add(tempLabel);
                            tq.add(okBut);
                            tq.setLocation(300,50);
                            j.add(tq);
                            j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            j.setVisible(true);
                        }
                        else { //funcEvent is tsp!
                            ArrayList<NodeData> tspInput = new ArrayList<>(); // will hold list of cities
                            for (Integer i : nodes){
                                tspInput.add(algoGraph.getGraph().getNode(i));
                            }
                            // init drawer params to be ready to paint as tsp output
                            // bullet 2.5
                            drawer.specialNodes = algoGraph.tsp(tspInput);

                            if (drawer.specialNodes == null || drawer.specialNodes.size() == 0){ // no answer for tsp
                                drawer.specialNodes = tspInput;
                                drawer.setColors(Color.RED, drawer.defNode);
                                drawer.setFlagAllSameColor(false);
                            }
                            else {// there is answer for tsp
                                drawer.specialEdges = currPathEdges(drawer.specialNodes);
                                drawer.makeTspString(); // init drawer tsp string map to support accurate String print for ea node (station issue)
                                drawer.removeDuplicate(); // remove nodes that showen more than once along the path
                                drawer.setFlagTsp(true);
                                drawer.setFlagAllSameColor(false);
                                drawer.setColors(Color.GREEN, Color.PINK);
                            }
                        }
                        getTopLevelAncestor().repaint();
                        n.setVisible(false);
                        n.dispose();
                        }
                    }
                    catch (Exception e) { // for invalid input
                        n.setVisible(false);
                        n.dispose();
                        // popup msg for user, with text that order the user how to set a valid input
                        tempLabel.setText("<html> for shortestPath and shortestPathDist: please enter pair of nodes via 'int,int' node keys <br>" +
                                "for tsp: please enter sequence of nodes via 'int,int,....,int' <br> "+
                                "or there is existing path for ur query <br>" +
                                "please ENSURE that u enter input without space </html>");
                        JFrame j = new JFrame(); // as we did above
                        j.setBounds(200,200, 550, 150);
                        j.setTitle("ERROR");
                        j.setLocationRelativeTo(null);
                        JButton okBut = new JButton();
                        okBut.setPreferredSize(new Dimension(60,40));
                        okBut.setText("OK");
                        okBut.addActionListener(eq -> {
                            if (eq.getSource() == okBut){
                                j.setVisible(false);
                                j.dispose();
                            }
                        });
                        JPanel tq = new JPanel(); // hold obj for the error msg that gonna show up on the screen
                        tq.add(tempLabel);
                        tq.add(okBut);
                        tq.setLocation(300,50);
                        j.add(tq);
                        j.setVisible(true);
                        j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        return;
                    }
                });
            // here is the end of the code of the first msg bar - which asks for user input
            this.userTextIn = new JTextField(16);
            this.userTextIn.setPreferredSize(new Dimension(250, 50));
            n.add(nLabel);
            n.add(this.submitBut);
            n.add(this.userTextIn);
            n.pack();
            n.setVisible(true);
        }
        else if (menuClickedEvent.getSource() == this.center) { // center
            // init as stated at 2.2 bullet
            drawer.setFlagTsp(false);
            this.drawer.specialNodes = new LinkedList<>();
            NodeData center = this.algoGraph.center();
            if (center != null){
                this.drawer.specialNodes.add(center);
                this.drawer.setColors(Color.YELLOW, drawer.defEdge);
                this.drawer.setFlagAllSameColor(false);
            }
            else {
                // if there is no center, will pop up msg that the graph is not connected
                JLabel tempLabel = new JLabel();
                tempLabel.setText("graph is not connected so there is no center node in the graph");
                JFrame j = new JFrame();
                j.setBounds(200,200, 450, 100);
                // nothing new
                j.setTitle("center msg");
                j.setLocationRelativeTo(null);
                JButton okBut = new JButton();
                okBut.setPreferredSize(new Dimension(60,40));
                okBut.setText("OK");
                okBut.addActionListener(eq -> {
                    if (eq.getSource() == okBut){
                        j.setVisible(false);
                        j.dispose();
                    }
                });
                // same as above
                JPanel tq = new JPanel();
                tq.add(tempLabel);
                tq.add(okBut);
                tq.setLocation(300,50);
                j.add(tq);
                j.setVisible(true);
                j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                return;
            }
            getTopLevelAncestor().repaint();
        }
        else if (menuClickedEvent.getSource() == this.newDwg){ // will create new dwg and clear the screen
            //bullet 3.5
            DirectedWeightedGraph a = new Dwg();
            this.algoGraph.init(a);
            this.drawer.updateDrawer(new DwgMagic(a));
            getTopLevelAncestor().repaint();
        }
        else if (menuClickedEvent.getSource() == this.addNode || menuClickedEvent.getSource() == this.addEdge){
            // bullet 3.1 && 3.2
            JFrame addCom = new JFrame();
            JLabel addText = new JLabel();
            // which of the options have benn clicked?
            if (menuClickedEvent.getSource() == this.addNode){
                addCom.setTitle("add node");
                addText.setText("<html> please set node_id,x_cord,y,cord <br>via pattern 'int,float,float'");
            }
            else {
                addCom.setTitle("add edge");
                addText.setText("<html> please set node_id_src,node_id_dest,edge_weight <br>via pattern 'int,int,float'");
            }
            // create msg bar to the user that will ask for the needed data input
            this.submitBut = new JButton("Submit");
            addCom.setDefaultCloseOperation(addCom.DISPOSE_ON_CLOSE);
            addCom.setLayout(new FlowLayout());
            addCom.setLocationRelativeTo(null);
            this.submitBut.addActionListener(ac -> {
                try {
                    if (ac.getSource() == submitBut){
                        String s = userTextIn.getText();
                        String[] str = s.split(",");
                        drawer.setColors(drawer.defNode, drawer.defEdge);
                        if (menuClickedEvent.getSource() == addNode){ // add node bullet 3.1
                            // control over invalid inputs
                            if (str.length > 3 || algoGraph.getGraph().getNode(Integer.parseInt(str[0])) != null){
                                invalidInput();
                            }
                            algoGraph.getGraph().addNode(new Node(new Point3D(Double.parseDouble(str[1]), Double.parseDouble(str[2])), Integer.parseInt(str[0])));
                            algoGraph.init(algoGraph.getGraph());
                            drawer.updateDrawer(algoGraph);
                        }
                        else if (menuClickedEvent.getSource() == addEdge){ // add edge bullet .2
                            if (str.length > 3){ // control over invalid inputs
                                invalidInput();
                            }
                            algoGraph.getGraph().connect(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Double.parseDouble(str[2]));
                        }
                        getTopLevelAncestor().repaint();
                        addCom.setVisible(false);
                        addCom.dispose();
                    }
                }
                catch (Exception ex){
                    // for invalid input - pop up msg for user
                    // msg will guide the user for accurate input
                    addCom.setVisible(false);
                    addCom.dispose();
                    addText.setText("<html> for addNode: please enter node_id and pair of x,y cord via 'int,float,float' pattern <br>" +
                            "for addEdge: please enter pair of node_id and weight of edge via 'int,int,float' pattern<br> "+
                            "please ENSURE to enter input without space && node_id is UNIQUE! </html>");
                    JFrame j = new JFrame(); // same as above
                    j.setBounds(200,200, 550, 150);
                    j.setTitle("ERROR");
                    j.setLocationRelativeTo(null);
                    JButton okBut = new JButton();
                    okBut.setPreferredSize(new Dimension(60,40));
                    okBut.setText("OK");
                    okBut.addActionListener(eq -> {
                        if (eq.getSource() == okBut){
                            j.setVisible(false);
                            j.dispose();
                        }
                    });
                    JPanel tq = new JPanel();
                    tq.add(addText);
                    tq.add(okBut);
                    tq.setLocation(300,50);
                    j.add(tq);
                    j.setVisible(true);
                    j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    return;
                }
            });
            // end of add component msg bar code
            this.userTextIn = new JTextField(16);
            this.userTextIn.setPreferredSize(new Dimension(250, 50));
            addCom.add(addText);
            addCom.add(this.submitBut);
            addCom.add(this.userTextIn);
            addCom.pack();
            addCom.setVisible(true);
        }
        else if (menuClickedEvent.getSource() == this.delEdge || menuClickedEvent.getSource() == this.delNode){
            // delete phase
            JFrame delCom = new JFrame();
            JLabel delText = new JLabel();
            // same as add node/edge
            // init text
            if (menuClickedEvent.getSource() == this.delNode){
                delCom.setTitle("delete node");
                delText.setText("<html> please input integer node_id <br>");
            }
            else { // e.getsource equal to this.delEdge
                delCom.setTitle("delete edge");
                delText.setText("<html>input pair of integer node_id for source-dest of edge<br> via pattern 'int,int'");
            }
            // init msg bar for input from user, same as above
            this.submitBut = new JButton("Submit");
            delCom.setDefaultCloseOperation(delCom.DISPOSE_ON_CLOSE);
            delCom.setLayout(new FlowLayout());
            delCom.setLocationRelativeTo(null);
            this.submitBut.addActionListener(delAc -> {
                try { // for invalid input, jump to catch block
                    if (delAc.getSource() == submitBut){ // act only if submit button pressed
                        String s = userTextIn.getText();
                        String[] str = s.split(",");
                        drawer.setColors(drawer.defNode, drawer.defEdge);
                        if (menuClickedEvent.getSource() == delNode){ //delete node as bullet 3.3
                            // control over invalid input
                            if (str.length > 1){
                                invalidInput();
                            }
                            // init graph and drawer for input user
                            algoGraph.getGraph().removeNode(Integer.parseInt(str[0]));
                            drawer.specialNodes = new ArrayList<>();
                            drawer.specialEdges = new HashMap<>();
                            algoGraph.init(algoGraph.getGraph());
                            drawer.updateDrawer(algoGraph);
                        }
                        else if (menuClickedEvent.getSource() == delEdge){ // delete edge
                            if (str.length > 2){ // control over invalid input
                                invalidInput();
                            }
                            //sync graph,drawer for this action
                            drawer.specialNodes = new ArrayList<>();
                            drawer.specialEdges = new HashMap<>();
                            algoGraph.getGraph().removeEdge(Integer.parseInt(str[0]), Integer.parseInt(str[1]));
                        }
                        getTopLevelAncestor().repaint();
                        delCom.setVisible(false);
                        delCom.dispose();
                    }
                }
                catch (Exception ex){
                    // will pop up msg for invalid input
                    // will guide the user how to input a valid string
                    delCom.setVisible(false);
                    delCom.dispose();
                    delText.setText("<html> for delNode: please enter integer node_id via 'int' pattern <br>" +
                            "for delEdge: please enter pair of node_id as src->dest edge via 'int,int' pattern<br> "+
                            "please ENSURE to enter input without space </html>");
                    JFrame j = new JFrame(); // create the error little frame as above
                    j.setBounds(200,200, 550, 150);
                    j.setTitle("ERROR");
                    j.setLocationRelativeTo(null);
                    JButton okBut = new JButton();
                    okBut.setPreferredSize(new Dimension(60,40));
                    okBut.setText("OK");
                    okBut.addActionListener(eq -> {
                        if (eq.getSource() == okBut){
                            j.setVisible(false);
                            j.dispose();
                        }
                    });
                    JPanel tq = new JPanel();
                    tq.add(delText);
                    tq.add(okBut);
                    tq.setLocation(300,50);
                    j.add(tq);
                    j.setVisible(true);
                    j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    return;
                }


            });
            // handle end of actions for delete component little frame and text
            this.userTextIn = new JTextField(16);
            this.userTextIn.setPreferredSize(new Dimension(250, 50));
            delCom.add(delText);
            delCom.add(this.submitBut);
            delCom.add(this.userTextIn);
            delCom.pack();
            delCom.setVisible(true);

        }
    }

    /**
     * create exception cuz invalid input
     */

    private void invalidInput() { drawer.specialNodes.get(-1);}

    /**
     * adjust list of Nodes which represent a path to hashmap with String keys "node_id_src,node_id_dest"
     * and value of dest node
     * @param nodes - list of nodes which represent path
     * @return hashmap of this nodes that adjust to the usage of drawer.specialEdges map
     */
    private HashMap<String, Integer> currPathEdges(List<NodeData> nodes) {
        int i=0;
        HashMap<String, Integer> edges = new HashMap<>();
        while (i+1 < nodes.size()){
            edges.put(""+nodes.get(i).getKey()+","+nodes.get(i+1).getKey(), nodes.get(i+1).getKey());
            i++;
        }
        return edges;
    }
}

