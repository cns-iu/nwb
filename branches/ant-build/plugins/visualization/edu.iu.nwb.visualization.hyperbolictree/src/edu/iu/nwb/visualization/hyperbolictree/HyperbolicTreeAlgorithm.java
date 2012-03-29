package edu.iu.nwb.visualization.hyperbolictree;

import java.io.File;
import java.util.Dictionary;

import javax.swing.JFrame;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
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

    public Data[] execute() throws AlgorithmExecutionException {
        String myregex = "[a-zA-Z]:";
        String rootParam = (String)parameters.get("rootDirectory");
        // compensate for windows users possibly entering "C:", "d:", etc... 
    	if (rootParam.matches(myregex)) {
    		rootParam = rootParam+"\\";
    	}
    
    	File rootFile = new File(rootParam);

        if (! rootFile.exists()) {
        	throw new AlgorithmExecutionException("Can't start hypertree : " + rootFile.getName() +
                    " does not exist.");
        }


        root = new HTFileNode(rootFile);
        if (root == null) {
        	throw new AlgorithmExecutionException("Problem starting hypertree with " + rootFile.getAbsolutePath());
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