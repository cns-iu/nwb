package edu.iu.epic.modelbuilder.gui;

import java.util.Dictionary;

import javax.swing.SwingUtilities;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.epic.modeling.compartment.model.Model;

/**
 * This algorithm aggregates/collapses the input table based off on values in a 
 * "Aggregated On" column provided by the user. The types of aggregation performed
 * on each column can be selected by the user from a drop-down box. 
 * Currently "Sum", "Difference", "Average", "Min", "Max" aggregations are available 
 * for numerical column types.
 * Non-numerical column types are treated as String and hence a user can select 
 * appropriate text delimiters for each. 
 * 
 * @author cdtank
 *
 */

public class ModelBuilderGUIAlgorithm implements Algorithm {
	
	private Data[] data;
	private LogService logger;
	
    public static final String AGGREGATE_ON_COLUMN = "aggregateoncolumn";
    
    public ModelBuilderGUIAlgorithm(Data[] data, CIShellContext context) {
    	
        this.data = data;
		this.logger = (LogService) context.getService(LogService.class.getName());
	}

    public Data[] execute() throws AlgorithmExecutionException {

    	boolean isCreateNewModelAlgorithmSelected = false;
    	final Model originalModel;
    	if (data.length > 0) {
    		originalModel = (Model) this.data[0].getData();
    	} else {
    		isCreateNewModelAlgorithmSelected = true;
    		originalModel = new Model();
    	}

    	
    	final ModelBuilderGUI modelBuilderInstance = new ModelBuilderGUI();
    	synchronized(modelBuilderInstance) {
    		
    		SwingUtilities.invokeLater(new Runnable() {
    			public void run() {
    				try {
    					modelBuilderInstance.start(originalModel);
    				} catch (Exception e) {
    					/*
    					 * Wrap all Exceptions as RuntimeExceptions, and rethrow
    					 * the inner exception on the other side.
    					 */
    					throw new RuntimeException(e);
    				}
    			}
    		});
    		
    		try {
    			while (!modelBuilderInstance.isModelBuildingComplete()) {
    				modelBuilderInstance.wait();
    			}
    			
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	
    	
    	
    	
    	Model builtModel = modelBuilderInstance.getInMemoryModel();
    	/*
    	 * Only populate the data manager if we actually have a model.
    	 * */
    	if (builtModel != null) {
    		
    		/*
    		 * After getting the output model make it available to the user.
    		 * */
    		Data output = new BasicData(builtModel, Model.class.getName());
    		Dictionary metadata = output.getMetadata();
    		
    		
    		/*
    		 * Put the newly created model as a child in the data manager only if its parent 
    		 * exists. In case of Create New Model Algorithm there will be no parent to begin 
    		 * with. 
    		 * */
    		if (!isCreateNewModelAlgorithmSelected) {
    			metadata.put(DataProperty.LABEL, "Modified in-memory model.");
    			metadata.put(DataProperty.PARENT, this.data[0]);
    		} else {
    			metadata.put(DataProperty.LABEL, "New EpiC model.");
    		}
    		metadata.put(DataProperty.TYPE, DataProperty.MODIFIED);
    		
    		return new Data[]{output};
    	} else {
    		/*
    		 * Since the model is explicitly set as null when the user quits without saving
    		 * we can return an empty array Data object. This will not put anything on the 
    		 * data manger & is considered as better coding practice.
    		 * */
    		return new Data[]{};
    	}
    	
    }
    
    
/*	private ModelBuilderGUI showModelBuilder()
			throws AlgorithmExecutionException {
		final ModelBuilderGUI modelBuilderInstance = null;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					ModelBuilderGUI modelBuilderInstance = new ModelBuilderGUI();
				} catch (Exception e) {
					
					 * Wrap all Exceptions as RuntimeExceptions, and rethrow
					 * the inner exception on the other side.
					 
					throw new RuntimeException(e);
				}
			}
		});
		
		return modelBuilderInstance;
	}*/

}