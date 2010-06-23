package edu.iu.sci2.database.star.gui;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;

import edu.iu.sci2.database.star.gui.builder.LoadStarDatabaseGUIBuilder;

public class StarDatabaseGUIAlgorithm implements Algorithm {
	public static final String WINDOW_TITLE = "Star Database Loader";
	public static final int WINDOW_WIDTH = 400;
	public static final int WINDOW_HEIGHT = 600;

    private Data[] data;
    private Dictionary<String, Object> parameters;
    private CIShellContext ciShellContext;
    
    public StarDatabaseGUIAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
        this.data = data;
        this.parameters = parameters;
        this.ciShellContext = ciShellContext;
    }

    public Data[] execute() throws AlgorithmExecutionException {
//    	LoadStarDatabaseGUIBuilder.createAndDispatchGUI(WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT);

        return null;
    }
}