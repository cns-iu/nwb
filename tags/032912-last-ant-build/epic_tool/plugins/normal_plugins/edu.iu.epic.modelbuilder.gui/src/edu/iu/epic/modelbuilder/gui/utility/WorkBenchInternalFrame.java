package edu.iu.epic.modelbuilder.gui.utility;

import javax.swing.JInternalFrame;

@SuppressWarnings("serial")
public class WorkBenchInternalFrame extends JInternalFrame {

    public WorkBenchInternalFrame() {
        super("Model", 
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable
        
        pack();
    }
}
