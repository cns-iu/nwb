package edu.iu.epic.modelbuilder.gui;

import java.awt.Dimension;

import javax.swing.JInternalFrame;

public class WorkBenchInternalFrame extends JInternalFrame {
    private static final int xOffset = 30, yOffset = 30;

    public WorkBenchInternalFrame() {
        super("Model", 
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

        setSize(new Dimension(1000, 500));
        setPreferredSize(new Dimension(1000, 500));

        pack();
        
        //Set the window's location.
        setLocation(xOffset, yOffset);
        
    }
}
