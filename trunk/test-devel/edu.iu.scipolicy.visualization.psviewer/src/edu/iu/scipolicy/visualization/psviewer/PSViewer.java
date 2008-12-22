package edu.iu.scipolicy.visualization.psviewer;

import java.util.Dictionary;
import java.util.Enumeration;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JFrame;
 
import org.freehep.postscript.Processor;
import org.freehep.postscript.PSInputFile;
import org.freehep.postscript.PSPanel;

//

public class PSViewer implements Algorithm {
	// Constants.
	private static final String EXAMPLE_POST_SCRIPT_RESOURCE_FILE_NAME = "example.ps";
	
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    public PSViewer(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    // Copied from FreeHEP, and then modified.
	// (svn://svn.freehep.org/svn/freehep/trunk/tools/freehep-psviewer/src/site/resources/examples/EmbeddedPSViewer.java)
    public Data[] execute() throws AlgorithmExecutionException {
    	System.out.println("Attempting to find resources.........");
    	// For now, load the resource "example.ps" that is included with this project.
    	String postScriptFileName = getAbsoluteResourceFileName(EXAMPLE_POST_SCRIPT_RESOURCE_FILE_NAME);
    	// Create a new Processor and the display that it will render to (and link them up).
   		Processor postScriptProcessor = createPostScriptProcessorAndDisplay();
   		
   		try {
   			postScriptProcessor.setData(new PSInputFile(postScriptFileName));
   		}
   		catch (FileNotFoundException e) {
   			throw new AlgorithmExecutionException(e);
   		}
   		catch (IOException e) {
   			throw new AlgorithmExecutionException(e);
   		}
  
   		// Process
   		try {
   			postScriptProcessor.process();
   		}
   		catch (IOException e) {
   			throw new AlgorithmExecutionException(e);
   		}
    	
    	return null;
    }
    
    private String getAbsoluteResourceFileName(String searchFileName) throws AlgorithmExecutionException {
    	String absoluteResourceFileName = null;
    	
    	try {
    		// We need this class' class loader to find resources.
    		Class thisClass = this.getClass();
        	ClassLoader thisClassLoader = thisClass.getClassLoader();
        	
    		// Try to get the first resource found with the provided search file name.
    		Enumeration foundResources = thisClassLoader.getResources(searchFileName);
    		
    		// Make sure the resource file we want to use is found.  (It should never NOT be!)
    		if (!foundResources.hasMoreElements()) {
    			throw new AlgorithmExecutionException("Unable to find the file \'" +
    					searchFileName + "\'.");
    		}
    		
    		// At this point, it's safe to get the file name from foundResources.
    		absoluteResourceFileName = foundResources.nextElement().toString();
    	}
    	catch (IOException e) {
    		throw new AlgorithmExecutionException(e);
    	}
    	
    	return absoluteResourceFileName;
    }
    
    private Processor createPostScriptProcessorAndDisplay() {
    	// Create the panels.
   		JPanel containerPanel = new JPanel();
   		containerPanel.setLayout(new GridLayout(1, 1));
   		PSPanel postScriptViewerPanel = new PSPanel();
   		containerPanel.add(postScriptViewerPanel);
    		
   		// Show the panel.
   		JFrame postScriptViewerFrame = new JFrame("PSViewer (SciPolicy)");
   		postScriptViewerFrame.getContentPane().add(containerPanel);
   		postScriptViewerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   		postScriptViewerFrame.setSize(800, 600);
   		postScriptViewerFrame.setVisible(true);
    
   		// Create the PostScript processor and associate to panels and input files.
   		return new Processor(postScriptViewerPanel);
    }
}