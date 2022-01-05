package graphics;

import Graph.api.*;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * inherit from JPanel and impliments Mouse interfaces
 * responsible on drawing the graph as its ordered (via colors, stroke, etc)
 */
public class DrawGraph extends JPanel  implements MouseListener, MouseMotionListener, MouseWheelListener, Runnable{
    Window window;
    GameData gd;
    // init params
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
    final Color defEdge, defNode; // default colors
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

        // as needed..
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.exitFlag = false;


    }

    /**
     * credit to Shai Aharon teacher
     * this function ensure that the program will draw everything on a back image
     * after the prog is done to draw the curr image, its replace between cur and new images
     * @param g - graphics
     */
    public void paint(Graphics g) {
        // create new image
        Image bufferImage = createImage(this.getWidth(), this.getHeight());
        Graphics bufferGraphics = bufferImage.getGraphics();

        // draw at new image
        paintComponents(bufferGraphics); // paint graph
        if (!exitFlag){ // if exit we r done here :)
            synchronized (this.gd.getCurr_client()){
                paintGameItems(bufferGraphics); // paint agents + pokemons + game info
            }
        }

        g.drawImage(bufferImage, 0, 0, this);
    }

    /**
     * paint components of the game that can be changed meanwhile running time:
     * 1- pokemons <- set to ea pokemon a picture of pokemon between 100 diff pokemons
     * 2- agents <- represented as RED circle
     * 3- game info <- in the left corner of the window
     * @param g - graphics obj
     */
    private void paintGameItems(Graphics g){
        Graphics2D graphic = (Graphics2D) g;

        // draw game info
        int moves = this.gd.getMoves();
        int time = Integer.parseInt(this.gd.getTimeLeft());
        graphic.drawString("moves: " + moves,5,15);
        graphic.drawString("grade: " + gd.getGrade(),5,29);
        graphic.drawString("time left: " + TimeUnit.MILLISECONDS.toSeconds(time),5,43);

        // draw pokemons
        double[] cord;
        File f;
        Agent agent;
        Pokemon pok;
        graphic.setColor(new Color(110, 80, 0));
        for (int j=0; j<this.gd.getPokemons_size(); j++){
            pok = this.gd.getPokemons().get(j);
            cord = linearTransform(pok.getPos());
            // draw pokemon
            try {
                f = new File("pokmeons_image/"+pok.getId()+".png");
                Image img = ImageIO.read(f);
                graphic.drawImage(img, (int)cord[0], (int)(cord[1]-this.heightPoint*zoomInOut*3), (int)(50*zoomInOut), (int)(50*zoomInOut), this);
            }
            catch(Exception e) {
                // if pokemon picture didnt opened for some reason - print pokemons as BROWN circle
                graphic.draw(new Ellipse2D.Double(cord[0], cord[1], this.widthPoint * zoomInOut, this.heightPoint * zoomInOut));
            }
            // string of the pokemon - 'value Edge: (src, dest)'
            graphic.drawString(""+pok.getValue()+" Edge: ("+pok.getSrc()+","+pok.getDest()+")", (int)cord[0], (int)cord[1]);
        }

        // draw Agents
        graphic.setColor(new Color(255, 0, 0));
        for (int i=0; i<this.gd.getAgents_size(); i++){

            agent = gd.getAgents().get(i);
            cord = linearTransform(agent.getPos()); // linear transfer regular cord to width/height cord
            // draw as red circle and write agent_id above
            graphic.draw(new Ellipse2D.Double(cord[0], cord[1], this.widthPoint*zoomInOut*3, this.heightPoint*zoomInOut*3));
            graphic.drawString(""+agent.getId(), (int)cord[0], (int)cord[1]);
        }
        graphic.setColor(this.defNode);
    }

    /**
     * executing the draw process via neccessary params (colors etc..)
     * draw phases:
     *  1- draw all edges
     *  2- draw all nodes
     *
     * this function draws on the bufferImage that paints creates (paint function will switch between the buffers)
     * @param g - graphics of the curr drawer
     */
    public void paintComponents(Graphics g){
        Graphics2D graphic = (Graphics2D) g;

        // stage 1: paint all edges
        // init params
        graphic.setStroke(this.edgeStroke);
        double[] cordSrc, cordDest;
        EdgeData tempE;
        List<EdgeData> edgeDataList = new ArrayList<>(); // for special edges - tranform from "menu languege" to "drawer languege"
        Iterator<EdgeData> itEdge = this.currGraph.edgeIter();
        // draw edges
        while (itEdge.hasNext()){ // draw all edges which is not "special" with default color which is blue
            // init edge
            tempE = itEdge.next();
            cordSrc = linearTransform(this.currGraph.getNode(tempE.getSrc()).getLocation());
            cordDest = linearTransform(this.currGraph.getNode(tempE.getDest()).getLocation());
            // init color
            graphic.setColor(this.defEdge);
            drawArrow(graphic, cordSrc[0], cordSrc[1], cordDest[0], cordDest[1]); // draw arrow (edge)
        }


        // stage 2: paint all nodes
        // init vars
        graphic.setStroke(this.nodeStroke);
        Iterator<NodeData> itNode = this.currGraph.nodeIter();
        double[] cord;
        NodeData tempN;
        graphic.setFont(amirFont);
        if (this.currGraph != null && this.currGraph.nodeSize() <= 3){// when there is only few nodes - picture shall be defined abit diff
            this.min_max_cord[0] -= 10*(1/zoomInOut);
            this.min_max_cord[1] -= 10*(1/zoomInOut);
            this.min_max_cord[2] += 10*(1/zoomInOut);
            this.min_max_cord[3] += 10*(1/zoomInOut);
        }

        // draw all nodes
        while (itNode.hasNext()) {
            // init node
            tempN = itNode.next();
            cord = linearTransform(tempN.getLocation()); // linear transfer regular cord to width/height cord
            // init color
            graphic.setColor(this.defNode);
            // draw curr node
            graphic.draw(new Ellipse2D.Double(cord[0], cord[1], this.widthPoint*zoomInOut, this.heightPoint*zoomInOut));
            graphic.drawString(""+tempN.getKey(), (int)cord[0], (int)cord[1]);
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
//      min_max_cord; // idx: 0-minX, 1-minY, 2-maxX, 3-maxY

        double delCurrX = this.min_max_cord[2] - point.x(), delPicX = this.min_max_cord[2] - this.min_max_cord[0];
        double delCurrY = this.min_max_cord[3] - point.y(), delPicY = this.min_max_cord[3] - this.min_max_cord[1];

        double x = ((delCurrX / delPicX)*this.getWidth()*0.85 + this.getWidth()*0.05 + mousePoint.getX()) * zoomInOut;
        double y = ((delCurrY / delPicY)*this.getHeight()*0.85 + this.getHeight()*0.05 + mousePoint.getY()) * zoomInOut;

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
                Thread.sleep(33);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (exitFlag) {
            this.window.closeWindow();
        }
    }

}
