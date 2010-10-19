package edu.iu.epic.modelbuilder.gui;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.Dictionary;

import javax.swing.SwingUtilities;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.epic.modelbuilder.gui.utility.GlobalConstants;
import edu.iu.epic.modeling.compartment.model.Compartment;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.exception.ModelModificationException;

/**
 * @author cdtank
 *
 */

public class ModelBuilderGUIAlgorithm implements Algorithm {	
	private Data[] data;
	
	public ModelBuilderGUIAlgorithm(Data[] data, CIShellContext context) {    	
        this.data = data;
	}

    public Data[] execute() throws AlgorithmExecutionException {

    	boolean isCreateNewModelAlgorithmSelected = false;
    	Model importedModel;
    	
    	if (data.length > 0) {
	    	try {
				importedModel = ((Model) this.data[0].getData()).createCopy();
			} catch (ModelModificationException e1) {
				importedModel = new Model();
				// TODO Exception here   Need to issue some kind of warning to the user.  "Load failed etc.."
			}
    	} else {
    		isCreateNewModelAlgorithmSelected = true;
    		importedModel = new Model();
    	}
    	
    	final Model originalModel = importedModel;
    	
    	final ModelBuilderGUI modelBuilderInstance = new ModelBuilderGUI();
    	synchronized (modelBuilderInstance) {
    		
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
    		for (Compartment compartment : builtModel.getCompartments()) {    			
				compartment.setPosition(
						normalizeCompartmentPositionForExporting(
								compartment.getPosition(),
								modelBuilderInstance.getParentFrameDimensionsAtDisposal()));
    		}
    		
    		/*
    		 * After getting the output model make it available to the user.
    		 * */
    		Data output = new BasicData(builtModel, Model.class.getName());
    		Dictionary<String, Object> metadata = output.getMetadata();
    		
    		
    		/*
    		 * Put the newly created model as a child in the data manager only if its parent 
    		 * exists. In case of Create New Model Algorithm there will be no parent to begin 
    		 * with. 
    		 * */
    		if (!isCreateNewModelAlgorithmSelected) {
    			metadata.put(DataProperty.LABEL, "Modified model");
    			metadata.put(DataProperty.PARENT, this.data[0]);
    		} else {
    			metadata.put(DataProperty.LABEL, "New EpiC model");
    		}
    		metadata.put(DataProperty.TYPE, DataProperty.MODEL_TYPE);
    		
    		builtModel = null; // TODO Check if necessary
    		
    		return new Data[]{output};
    	} else {
    		/*
    		 * Since the model is explicitly set as null when the user quits without saving
    		 * we can return an empty array Data object. This will not put anything on the 
    		 * data manager & is considered as better coding practice.
    		 * */
    		return new Data[]{};
    	}    	
    }

	private Point2D normalizeCompartmentPositionForExporting(Point2D position,
			Dimension dimension) {		
       	DecimalFormat roundedPositionFormatter = new DecimalFormat("#.###");
       	double xPosition = java.lang.Double.valueOf(
       			roundedPositionFormatter.format(
       					(position.getX() / dimension.getWidth()) 
       					* GlobalConstants.COMPARTMENT_POSITION_NORMALIZING_FACTOR));
       	
       	double yPosition = java.lang.Double.valueOf(
       			roundedPositionFormatter.format(
       					(position.getY() / dimension.getHeight())
       					* GlobalConstants.COMPARTMENT_POSITION_NORMALIZING_FACTOR));
		
		return new Point2D.Double(xPosition, yPosition);
	}

}