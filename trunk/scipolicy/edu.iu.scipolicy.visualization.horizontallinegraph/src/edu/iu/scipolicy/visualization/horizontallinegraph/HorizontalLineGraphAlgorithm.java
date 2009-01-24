package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import edu.iu.scipolicy.utilities.FileUtilities;

public class HorizontalLineGraphAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    public static void main(String[] args) {
    	String[] columnNames =
    		new String[] { "label", "start date", "end date", "amount" };
    	
    	TableImpl table = new TableImpl(columnNames);
    	
    	// Add some rows.
    	Object[][] rowObjects = new Object[][]
    	{
    		new Object[] { "Micah", new Date(1985, 6, 3), new Date(2009, 6, 4), new Float(1000.0f) },
    		new Object[] { "Patrick", new Date(1984, 2, 15), new Date(1985, 2, 15), new Float(100.0f) },
    		new Object[] { "Elisha", new Date(1985, 10, 19), new Date(1994, 9, 20), new Float(500.0f) }
    	};
    	
    	for (int ii = 0; ii < rowObjects.length; ii++)
    		table.addRow(new TableRowImpl(columnNames, rowObjects[ii]));
    	
    	Data dataItem = new BasicData(table, "file:text/table");
    	Data[] data = new Data[] { dataItem };
    	HorizontalLineGraphAlgorithm hlga = new HorizontalLineGraphAlgorithm(data, null, null);
    	
    	try {
    		hlga.execute();
    	}
    	catch (AlgorithmExecutionException e) {
    		System.err.println("AlgorithmExecutionException: " + e.getMessage());
    	}
    	catch (Exception e) {
    		System.err.println("Exception (" + e.getClass().getName() + "): " + e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    public HorizontalLineGraphAlgorithm(Data[] data,
    									Dictionary parameters,
    									CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	// Unpack the in-data table.
    	Table inTable = (Table)this.data[0].getData();
    	
    	// "Get" the input parameters.
    	Date startDate = new Date(1984, 0, 1);
        Date endDate = new Date(2009, 0, 21);
        int minNumberOfDaysForBar = 15;
    	
        // Create the PostScript... creator.
    	HorizontalLineGraphPostScriptCreator horizontalLineGraphPostScriptCreator =
    		new HorizontalLineGraphPostScriptCreator();
    	
    	// Create the PostScript.
    	String horizontalLineGraphPostScriptToOutput = null;
    	
    	try {
    		horizontalLineGraphPostScriptToOutput =
    			horizontalLineGraphPostScriptCreator.createPostScript
    				(inTable, startDate, endDate, minNumberOfDaysForBar);
    	}
    	catch (PostScriptCreationException e) {
    		throw new AlgorithmExecutionException(e);
    	}
    	
    	// System.err.println("PostScript:");
    	System.err.println(horizontalLineGraphPostScriptToOutput);
    	
    	// Create a temporary file to write this PostScript out to.
    	File temporaryPostScriptFile = null;
    	
    	try {
    		temporaryPostScriptFile =
    			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory
    				("TEMP-POSTSCRIPT", "eps");
    	}
    	catch (IOException e) {
    		throw new AlgorithmExecutionException
    			("Error creating temporary file to write PostScript out to", e);
    	}
    	
    	// Write the contents of the PostScript to this temporary file now.
		try {
			FileWriter temporaryPostScriptFileWriter =
				new FileWriter(temporaryPostScriptFile);
			
			temporaryPostScriptFileWriter.write
				(horizontalLineGraphPostScriptToOutput);
		}
		catch (IOException e) {
			throw new AlgorithmExecutionException
				("Error writing PostScript out to temporary file", e);
		}
    	
        return null;
    }
}