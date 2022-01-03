package graphics;

import api.*;
import director.Agent;
import director.GameData;
import director.Pokemon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * inherit from JPanel and impliments Mouse interfaces
 * responsible on drawing the graph as its ordered (via colors, stroke, etc)
 */
public class DrawGraph extends JPanel  implements MouseListener, MouseMotionListener, MouseWheelListener, Runnable{
    Window window;
    GameData gd;
    // init params
    final double constWidth = 750*0.8;
    final double constHeight = 750*0.1;
    private double widthArrow;
    private double heightArrow;
    private double widthPoint;
    private double heightPoint;
    DirectedWeightedGraphAlgorithms algoGraph;
    DirectedWeightedGraph currGraph;
    double[] min_max_cord; // idx: 0-minX, 1-minY, 2-maxX, 3-maxY
    double zoomInOut;

    // stroke and fonts
    final Stroke edgeStroke = new BasicStroke((float)1.5);
    final Stroke nodeStroke = new BasicStroke((float)5.0);
    final Font amirFont = new Font("a", Font.BOLD, 16);

    // for representing functions output (via colors)
    boolean flagAllSameColor;
    boolean flagTsp;
    final Color defEdge, defNode; // default colors
    Color colorE, colorN; // special colors for events
    HashMap<String, Integer> specialEdges; // which will be drawed with none default color
    List<NodeData> specialNodes; // which will be drawed with none default color
    HashMap<Integer, String> tspString; // helps to management drawing of TSP items
    //save mouse points, help to make picture accurate to client presses
    private Point2D mousePoint;
    private Point2D mousePrevPos;
    private Point2D mouseNextPos;

    boolean exitFlag;

    DrawGraph(DirectedWeightedGraphAlgorithms al, Window wind){
        this.window = wind;
        // initialize the drawer parameters and vars
        this.algoGraph = al;
        this.currGraph = al.getGraph();
        this.zoomInOut = 1; // can be chaned later
        this.widthArrow = 10.0;
        this.heightArrow = 5.0;
        this.widthPoint = 5.0;
        this.heightPoint = 5.0;
        this.mousePoint = new Point(0,0);
        this.mousePrevPos = new Point(0,0);
        this.mouseNextPos = new Point(0,0);
        // responsible to keep all items in the bounds of the 750*750 picture
        this.min_max_cord = new double[4]; // idx: 0-minX, 1-minY, 2-maxX, 3-maxY
        if (this.currGraph != null && this.currGraph.nodeSize() >0){
            updateMinMax();
        }

        // init colors block
        this.defNode = Color.BLACK; // black
        this.defEdge = new Color(0,100,200); // regular blue
        this.specialNodes = new LinkedList<>();
        this.specialEdges = new HashMap<>();
        this.flagAllSameColor = false;
        this.flagTsp = false;

        // as needed..
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);

        this.exitFlag = false;
    }

    /**
     * if the graph or its algos parameters have been changed, drawer obj shall be updated
     * @param newAlgo - for example DwgMagic
     */
    void updateDrawer(DirectedWeightedGraphAlgorithms newAlgo){
        // init params
        this.algoGraph = newAlgo;
        this.currGraph = newAlgo.getGraph();
        this.zoomInOut = 1;
        this.widthArrow = 4.0;
        this.heightArrow = 4.0;
        this.widthPoint = 5.0;
        this.heightPoint = 5.0;
        mousePoint = new Point(0,0);
        mousePrevPos = new Point(0,0);
        mouseNextPos = new Point(0,0);
        // init flag for coloring (zeroing)
        this.flagAllSameColor = false;
        this.flagTsp = false;
        // back to default
        if (newAlgo.getGraph() != null && newAlgo.getGraph().nodeSize() > 0){
            updateMinMax();
        }
        this.exitFlag = false;
    }

    /**
     * update special colors for node and edges
     * @param nodeNewColor - color
     * @param edgeNewColor - color
     */
    void setColors(Color nodeNewColor, Color edgeNewColor){
        this.colorN = nodeNewColor;
        this.colorE = edgeNewColor;
    }

    /**
     * update flage "allSameColor" - decide if to draw all color in the same special color
     * @param b - boolean
     */
    void setFlagAllSameColor(boolean b){
        this.flagAllSameColor = b;
    }

    /**
     * update flage "tsp" - decide if to draw special nodes as tsp strategy or regulary
     * @param b - boolean
     */
    public void setFlagTsp(boolean b) { this.flagTsp = b; }
    /**
     * credit to Shai Aharon teacher
     * this function ensure that the program will draw everything on a back image
     * after the prog is done to draw the curr image, its replace between cur and new images
     * @param g - graphics
     */
    public void paint(Graphics g) {
        // create new image
        Image bufferImage = createImage(750, 750);
        Graphics bufferGraphics = bufferImage.getGraphics();

        // draw at new image
        paintComponents(bufferGraphics); // paint graph
        paintGameItems(bufferGraphics); // paint agents + pokemons

        // "Switch" the old image for the new one
        g.drawImage(bufferImage, 0, 0, this);
    }

    private void paintGameItems(Graphics g){
        Graphics2D graphic = (Graphics2D) g;
        double[] cord;
        File f;
        graphic.setColor(new Color(255, 0, 0));
        Agent agent;
        Pokemon pok;
        for (int i=0; i<this.gd.getAgents_size(); i++){
            agent = gd.getAgents().get(i);
            cord = linearTransform(agent.getPos()); // linear transfer regular cord to width/height cord
//            System.out.println(cord[0] + " "+ cord[1]);
            // draw agent
//            try{
//                f = new File("agents_image/ash.png");
//                Image img = ImageIO.read(f);
//                graphic.drawImage(img, (int)cord[0], (int)cord[1], (int)(50*zoomInOut), (int)(50*zoomInOut), this);
//            }
//            catch (IOException e) {
                graphic.draw(new Ellipse2D.Double(cord[0], cord[1], this.widthPoint*zoomInOut*3, this.heightPoint*zoomInOut*3));
//            }
            graphic.drawString(""+agent.getId(), (int)cord[0], (int)cord[1]);
        }
        graphic.setColor(new Color(110, 80, 0));
        for (int j=0; j<this.gd.getPokemons_size(); j++){
            pok = this.gd.getPokemons().get(j);
            cord = linearTransform(pok.getPos());
            // draw pokemon
            try {
                f = new File("pokmeons_image/"+pok.getId()+".png");
                Image img = ImageIO.read(f);
                //+this.widthPoint*zoomInOut), +this.heightPoint*zoomInOut)
                graphic.drawImage(img, (int)cord[0], (int)(cord[1]-this.heightPoint*zoomInOut*3), (int)(50*zoomInOut), (int)(50*zoomInOut), this);
            }
            catch(Exception e) {
                graphic.draw(new Ellipse2D.Double(cord[0], cord[1], this.widthPoint * zoomInOut, this.heightPoint * zoomInOut));
            }
            graphic.drawString(""+pok.getValue(), (int)cord[0], (int)cord[1]);
        }
        graphic.setColor(this.defNode);
    }

    /**
     * executing the draw process via neccessary params (colors etc..)
     * split all nodes and edges to two groups: specials, regulars
     *      specials shall be drawen within the var choosen color
     *      regulars shall be drawen within default colors ("final colors", cant be changed)
     * draw phases:
     *  1- draw regular edges
     *  2- draw regular nodes
     *  3- draw special edges
     *  4- draw special nodes
     *  in this way, the user will always see in front the data that they looked for in priority
     *
     * this function draws on the bufferImage that paints creates (paint function will switch between the buffers)
     * @param g - graphics of the curr drawer
     */
    public void paintComponents(Graphics g){
        Graphics2D graphic = (Graphics2D) g;

        // stage 1: paint regular edge
        // init params
        graphic.setStroke(this.edgeStroke);
        double[] cordSrc, cordDest;
        EdgeData tempE;
        List<EdgeData> edgeDataList = new ArrayList<>(); // for special edges - tranform from "menu languege" to "drawer languege"
        Iterator<EdgeData> itEdge = this.currGraph.edgeIter();
        while (itEdge.hasNext()){ // draw all edges which is not "special" with default color which is blue
            // init edge
            tempE = itEdge.next();
            cordSrc = linearTransform(this.currGraph.getNode(tempE.getSrc()).getLocation());
            cordDest = linearTransform(this.currGraph.getNode(tempE.getDest()).getLocation());
            // init color
            if (this.flagAllSameColor || this.specialEdges.containsKey(""+tempE.getSrc()+","+tempE.getDest()) || this.specialEdges.containsKey(""+tempE.getDest()+","+tempE.getSrc())){
                if (this.flagAllSameColor){ // true = paint all edges with the special color
                    graphic.setColor(this.colorE);
                    drawArrow(graphic, cordSrc[0], cordSrc[1], cordDest[0], cordDest[1]); // draw arrow (edge)
                }
                edgeDataList.add(tempE); // add to special edge to print (transform from "menu languege" to "drawer languege"

            }
            else{ // default color
                graphic.setColor(this.defEdge);
                drawArrow(graphic, cordSrc[0], cordSrc[1], cordDest[0], cordDest[1]); // draw arrow (edge)
            }
        }


        // stage 2: paint regular nodes
        graphic.setStroke(this.nodeStroke);
        Iterator<NodeData> itNode = this.currGraph.nodeIter();
        double[] cord;
        NodeData tempN;
        graphic.setFont(amirFont);
        if (this.currGraph != null && this.currGraph.nodeSize() <= 3){// when there is only few nodes - picture shall be defined abit diff
            this.min_max_cord[0] -= 10;
            this.min_max_cord[1] -= 10;
            this.min_max_cord[2] += 10;
            this.min_max_cord[3] += 10;
        }

        while (itNode.hasNext()) {
            // init node
            tempN = itNode.next();
            cord = linearTransform(tempN.getLocation()); // linear transfer regular cord to width/height cord
            // init color
            if (this.flagAllSameColor || this.specialNodes.contains(tempN)){
                if (this.flagAllSameColor){ // true = all nodes shall be painted with special color
                    graphic.setColor(this.colorN);
                    // draw curr node
                    graphic.draw(new Ellipse2D.Double(cord[0] - this.widthPoint*zoomInOut/2, cord[1]-this.heightPoint*zoomInOut/2, this.widthPoint*zoomInOut, this.heightPoint*zoomInOut));
                    graphic.drawString(""+tempN.getKey(), (int)cord[0], (int)cord[1]);
                }
            }
            else {
                graphic.setColor(this.defNode);
                // draw curr node
                graphic.draw(new Ellipse2D.Double(cord[0], cord[1], this.widthPoint*zoomInOut, this.heightPoint*zoomInOut));
                graphic.drawString(""+tempN.getKey(), (int)cord[0], (int)cord[1]);
            }

        }

        // stage 3: paint special edges
        // note - if flagAllSameColor is true, the edgeDataList is irrelevant
        graphic.setStroke(this.edgeStroke);
        itEdge = edgeDataList.iterator();
        while(itEdge.hasNext() && !this.flagAllSameColor){
            tempE = itEdge.next();
            cordSrc = linearTransform(this.currGraph.getNode(tempE.getSrc()).getLocation());
            cordDest = linearTransform(this.currGraph.getNode(tempE.getDest()).getLocation());
            graphic.setColor(this.colorE);
            drawArrow(graphic, cordSrc[0], cordSrc[1], cordDest[0], cordDest[1]); // draw arrow (edge)
        }

        // stage 4: paint all special nodes
        graphic.setStroke(this.nodeStroke);
        itNode = this.specialNodes.iterator();
        while(itNode.hasNext()){
            tempN = itNode.next();
            cord = linearTransform(tempN.getLocation()); // linear transfer regular cord to width/height cord
            graphic.setColor(this.colorN);
            // draw curr node
            graphic.draw(new Ellipse2D.Double(cord[0], cord[1], this.widthPoint*zoomInOut, this.heightPoint*zoomInOut));
            if (this.flagTsp){ // true = we in TSP mode! shall print in addition the station String!
                graphic.drawString(""+tempN.getKey()+" station: "+this.tspString.get(tempN.getKey()), (int)cord[0], (int)cord[1]);
            }
            else{ // just print the node_id
                graphic.drawString(""+tempN.getKey(), (int)cord[0], (int)cord[1]);
            }
        }
    }

    /**
     * remove duplicate nodes which contains in the specialNodes list
     * helpful for tsp handling which is hard to print without duplicates
     */
    void removeDuplicate() {
        int size = this.specialNodes.size();
        NodeData n;
        // iterate over the list
        for (int i=0; i < size; i++){
            n = this.specialNodes.get(0);
            // remove all pointers to node n
            while (this.specialNodes.contains(n)){
                this.specialNodes.remove(n);
            }
            // add node n back to the list
            this.specialNodes.add(n);
        }
    }

    /**
     *  loop over special nodes list
     *  creates HashMap with keys - node_id, values - string which shall be printed with the curr node in the end of TSP process
     */
    void makeTspString() {
        this.tspString = new HashMap<>();
        int stationCounter=1; // represent the id station which the travel will get to within the i "move"
        for (NodeData element : this.specialNodes){
            if (!this.tspString.containsKey(element.getKey())){ // first time we handling with the element node
                this.tspString.put(element.getKey(), ""+stationCounter);
            }
            else { // not the first time
                this.tspString.replace(element.getKey(), this.tspString.get(element.getKey()) +","+stationCounter);
            }
            stationCounter++;
        }
    }

    /**
     * draw arrow between src node cords to dest node cords
     * credit for algebric formulas to https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java
     * in general the func do:
     * calculate from src,dest points a 3 points that represent a arrowHead which shall be drawen as polygon
     * (one of the points is destNode cords which is the headArrowPeak , and the other two is the base triangular nodes)
     * @param g - graphics obj
     * @param xSrc - x cord of src node
     * @param ySrc - y cord of src node
     * @param xDest - x cord of dest node
     * @param yDest - y cord of dest node
     */
    private void drawArrow(Graphics g, double xSrc, double ySrc, double xDest, double yDest) {
        // init vars via formulas
        double xHeadArrow1, xHeadArrow2, yHeadArrow1, yHeadArrow2; // which combine to 2 extra points that create triangular for the arrow head
        double delX = (xDest - xSrc), delY = (yDest - ySrc); // cal delta x,y
        double distBetNodes = Math.sqrt(delX*delX + delY*delY); // cal distance src->dest nodes
        double sinVal = delY / distBetNodes , cosVal = delX / distBetNodes; // const math via algebra, basic rules of sin/cos

        yHeadArrow1 = (distBetNodes - this.widthArrow)*sinVal + this.heightArrow*cosVal + ySrc; // via formula
        xHeadArrow1 = (distBetNodes - this.widthArrow)*cosVal - this.heightArrow*sinVal + xSrc; // via formula

        yHeadArrow2 = (distBetNodes - this.widthArrow)*sinVal + -1*(this.heightArrow)*cosVal + ySrc; // via formula
        xHeadArrow2 = (distBetNodes - this.widthArrow)*cosVal - -1*(this.heightArrow)*sinVal + xSrc; // via formula

        // arrays for x,y cordinates to draw the polygon
        int[] xpoints = {(int)(xDest), (int) (xHeadArrow1), (int) (xHeadArrow2)};
        int[] ypoints = {(int)(yDest), (int) (yHeadArrow1), (int) (yHeadArrow2)};
        // draw arrow line
        g.drawLine((int)(xSrc), (int)(ySrc), (int)(xDest), (int)(yDest));
        // draw arrow head
        g.drawPolygon(xpoints, ypoints, 3);
    }

    /**
     * save an array of min/max x,y cords in array size 4
     * idx: 0-minX, 1-minY, 2-maxX, 3-maxY
     */
    private void updateMinMax() {
        Iterator<NodeData> it = this.currGraph.nodeIter();
        NodeData tempN;
        double minX = 0, minY = 0, maxX = 0, maxY = 0;
        if (it.hasNext()) { // save first node for indicate
            tempN = it.next();
            minX = tempN.getLocation().x();
            maxX = tempN.getLocation().x();
            minY = tempN.getLocation().y();
            maxY = tempN.getLocation().y();
        }
        while (it.hasNext()) { // for every node from now on, check min/max params
            tempN = it.next();
            if (tempN.getLocation().x() < minX) {
                minX = tempN.getLocation().x();
            }
            else if (tempN.getLocation().x() > maxX) {
                maxX = tempN.getLocation().x();
            }
            if (tempN.getLocation().y() < minY) {
                minY = tempN.getLocation().y();
            }
            else if (tempN.getLocation().y() > maxY) {
                maxY = tempN.getLocation().y();
            }
        }
        this.min_max_cord[0] = minX;
        this.min_max_cord[1] = minY;
        this.min_max_cord[2] = maxX;
        this.min_max_cord[3] = maxY;
    }

    /**
     * transform point of GEOLOCATION type to x,y cordinates that match the picture
     * @param point - var obj of point
     * @return - x,y cordinates that match the picture
     */
    double[] linearTransform(GeoLocation point){ // credit to Daniel Rosenberg, student of our class, for the formula
        double delCurrX = this.min_max_cord[2] - point.x(), delPicX = this.min_max_cord[2] - this.min_max_cord[0];
        double delCurrY = this.min_max_cord[3] - point.y(), delPicY = this.min_max_cord[3] - this.min_max_cord[1];
        double x = (delCurrX / delPicX * this.constWidth + this.constHeight + mousePoint.getX()) * zoomInOut;
        double y = (delCurrY / delPicY * this.constWidth + this.constHeight + mousePoint.getY()) * zoomInOut;
        return new double[]{x,y};
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseNextPos = e.getPoint(); // update mouse next pos cordinates
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePrevPos = (Point2D)mousePoint.clone(); // update mouse prev pos cordinates
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * this func ensure "smooth" dragging over the map
     * @param e - mouse event
     */
    @Override
    public void mouseDragged(MouseEvent e) { // credit to Daniel Rosenberg, class student, for formula
        double xMPrev = mousePrevPos.getX(), yMPrev = mousePrevPos.getY();
        double xMNext = mouseNextPos.getX(), yMNext = mouseNextPos.getY();
        double zoomedX = xMPrev + (e.getX() - xMNext)/this.zoomInOut, zoomedY = yMPrev + (e.getY() - yMNext)/this.zoomInOut;
        mousePoint.setLocation(zoomedX , zoomedY);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) { // https://www.javacodex.com/Swing/MouseWheelListener for more data
        // ensure that the picture wont minimize to size that will be too close to zero zoom
        // this way of implementation avoids from stuck picture cuz of too much zoom usage
        double temp = ((double)-e.getWheelRotation()) / 7;
        if (this.zoomInOut + temp > 0.05){
            this.zoomInOut = this.zoomInOut + temp;
            repaint();
        }
    }

    @Override
    public void run() {
        while(!this.exitFlag){
            repaint();
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (exitFlag) {
            this.window.closeWindow();
        }
    }
}
