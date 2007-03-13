package org.mediavirus.parvis;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Dictionary;

import javax.swing.UIManager;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.mediavirus.parvis.gui.MainFrame;

public class ParallelCoordinatesAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public ParallelCoordinatesAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	File inFile = (File) data[0].getData();
    	
		UIManager.put("org.mediavirus.parvis.gui.ParallelDisplayUI",
		"org.mediavirus.parvis.gui.BasicParallelDisplayUI");

		MainFrame mainFrame = new MainFrame();
		try {
			mainFrame.readDataFile(inFile.toURL().toExternalForm());
			
			mainFrame.show();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
        return null;
    }
}