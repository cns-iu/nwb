package edu.iu.epic.modelbuilder.gui.utility;

import javax.swing.JInternalFrame;

public class WorkBenchInternalFrame extends JInternalFrame {

    public WorkBenchInternalFrame() {
        super("Model", 
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable
        pack();
        //Set the window's location.
    }
}
