package edu.iu.nwb.visualization.hyperbolictree;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import javax.swing.JFrame;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;


public class HyperbolicTreeAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    private static HTFileNode root      = null; // the root of the demo tree
    private static HyperTree  hypertree = null; // the hypertree builded

    
    public HyperbolicTreeAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
        String myregex = "[a-zA-Z]:";
        String rootParam = (String)parameters.get("rootDirectory");
        // compensate for windows users possibly entering "C:", "d:", etc... 
    	if (rootParam.matches(myregex)) {
    		rootParam = rootParam+"\\";
    	}
    
    	File rootFile = new File(rootParam);
        
        try {
            System.out.println("Starting the hyperbolic tree from " +
                                rootFile.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (! rootFile.exists()) {
            System.out.println("Can't start hypertree : " + rootFile.getName() +
                               " does not exist.");
            return null;
        }


        root = new HTFileNode(rootFile);
        if (root == null) {
            System.err.println("Error : can't start hypertree from " +
                                rootFile.getAbsolutePath());
            return null;
        }

        hypertree = new HyperTree(root);
        SwingHTView view = hypertree.getView();
	//HTNode firstchild = (HTNode) root.children().next();
	//view.translateToOrigin(firstchild);
        JFrame viewFrame = new JFrame(root.getName());
        viewFrame.setContentPane(view);
        viewFrame.setSize(500, 500);
        //viewFrame.pack();
        viewFrame.setVisible(true);
        
        return null;    	
    	
    }
}