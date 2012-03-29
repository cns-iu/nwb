package edu.iu.epic.modelbuilder.gui;

import java.net.URL;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import edu.iu.epic.modelbuilder.gui.parametertable.ParameterTable;

public abstract class ModelBuilderGUIAlgorithmFactory implements AlgorithmFactory {
	
	private URL deleteButtonMousePressedImagePath, deleteButtonNormalStateImagePath;
	
	private static final String DELETE_BUTTON_MOUSE_PRESSED_IMAGE_FILE_PATH
									= "delete_button_pressed_state.png";
	private static final String DELETE_BUTTON_NORMAL_STATE_IMAGE_FILE_PATH 
									= "delete_button_normal_state.png";

	/*
	 * Fetch the Latitude & Longitude values from the text files in to appropriate Maps.
	 * This will be done only once.
	 * */
	protected void activate(ComponentContext ctxt) {
    	BundleContext bContext = ctxt.getBundleContext();
    	
    	this.deleteButtonMousePressedImagePath = bContext.getBundle().getResource(
    												DELETE_BUTTON_MOUSE_PRESSED_IMAGE_FILE_PATH);
    	ParameterTable.setDeleteButtonMousePressedImageFile(deleteButtonMousePressedImagePath);
    	
    	this.deleteButtonNormalStateImagePath = bContext.getBundle().getResource(
    												DELETE_BUTTON_NORMAL_STATE_IMAGE_FILE_PATH);
    	ParameterTable.setDeleteButtonNormalStateImageFile(deleteButtonNormalStateImagePath);
    	
    }
	
	public abstract Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context);
}