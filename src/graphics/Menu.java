package graphics;

import api.*;
import javax.swing.*;
import java.awt.event.*;


/**
 * inherite from JMenuBar -> will hold all features from the menu bar of the window
 * will sync and execute drawGraph obj which connected to window as well
 * implements ActionListener to be able to rule over actions like button clicks on items
 * GUIDE:
 * categories:
 * 1- Menu:
 *     1.1 exit -> terminate whole program, replace the red X at the right up corner of the window
 */
public class Menu extends JMenuBar implements ActionListener {
    // vars
    Window w;
    DirectedWeightedGraphAlgorithms algoGraph;
    JMenu menu;
    // items for jmenu
    JMenuItem exitGUI;
    // for drawing
    DrawGraph drawer;
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
        this.menu = new JMenu("Menu");

        // init exit feature
        this.exitGUI = new JMenuItem("exit");
        this.exitGUI.addActionListener(this);
        this.menu.add(this.exitGUI);

        this.add(this.menu);
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
    }
}

