package graphics;

import api.DirectedWeightedGraphAlgorithms;
import javax.swing.*;

/**
 * class which inherite from JFrame
 * within constructing it, the frame will be opened
 * the window represent the directedWeighted graph and hold all features of the algorithms funcs
 */

public class Window extends JFrame{

    // save vars
    DirectedWeightedGraphAlgorithms currAlgo;
    Menu menu;

    public Window(DirectedWeightedGraphAlgorithms algos){
        // init window
        this.currAlgo = algos;
        this.setTitle("Amir & Ori DWG GUI!"); // name
        this.setSize(750,750); // size
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close everything within exit

        // icon
        ImageIcon im = new ImageIcon("dwg.png");
        this.setIconImage(im.getImage());

        // drawer for the features
        DrawGraph drawer = new DrawGraph(this.currAlgo);
        // menubar which control on features
        menu = new Menu(this.currAlgo, drawer, this);

        this.add(drawer);
        this.setJMenuBar(menu);
        this.setVisible(true);
    }
}
