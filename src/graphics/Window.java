package graphics;

import api.DirectedWeightedGraphAlgorithms;
import director.GameData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * class which inherite from JFrame
 * within constructing it, the frame will be opened
 * the window represent the directedWeighted graph and hold all features of the algorithms funcs
 */

public class Window extends JFrame{

    // save vars
    DirectedWeightedGraphAlgorithms currAlgo;
    Menu menu;
    GameData currGameData;

    public Window(DirectedWeightedGraphAlgorithms algos, GameData gd){
        // init window
        this.currAlgo = algos;
        this.currGameData = gd;
        this.setTitle("Amir & Ori DWG GUI!"); // name
        this.setSize(750,750); // size
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close everything within exit

        // icon
        ImageIcon im = new ImageIcon("dwg.png");
        this.setIconImage(im.getImage());

        // drawer for the features
        DrawGraph drawer = new DrawGraph(this.currAlgo, this);
        drawer.gd = this.currGameData;
        // menubar which control on features
        menu = new Menu(this.currAlgo, drawer, this);

        this.add(drawer);
        this.setJMenuBar(menu);
        this.setVisible(true);

        new Thread(drawer).start();
    }

    public void closeWindow(){
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }
}
