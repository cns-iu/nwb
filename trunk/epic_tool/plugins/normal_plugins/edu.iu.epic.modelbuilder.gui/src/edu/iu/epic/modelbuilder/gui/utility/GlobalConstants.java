package edu.iu.epic.modelbuilder.gui.utility;

import java.awt.Dimension;

public final class GlobalConstants {

	//Prevents instantiation
	private GlobalConstants() { }
	
	public static final double COMPARTMENT_LABEL_X_OFFSET = 5.0;
	public static final double COMPARTMENT_LABEL_Y_OFFSET = 5.0;
	
	public static final Dimension SIMPLE_TRANSITION_BUTTON_DIMENSIONS = new Dimension(15, 15);
	public static final Dimension COMPLEX_TRANSITION_BUTTON_DIMENSIONS = new Dimension(15, 15);
	public static final Dimension INFECTOR_ADDER_DIMENSIONS = new Dimension(9, 9);
	public static final Dimension INFECTOR_DELETER_DIMENSIONS = new Dimension(9, 9);
	public static final Dimension COMPARTMENT_DIMENSIONS = new Dimension(125, 50);
	
	public static final String NODE_ID_ATTRIBUTE_NAME = "ID";
	public static final String NODE_TYPE_ATTRIBUTE_NAME = "TYPE";
	public static final String NODE_COLOR_ATTRIBUTE_NAME = "COLOR";
	public static final String LAYER_ID_ATTRIBUTE_NAME = "LAYER_ID";

	public static final String TRANSITION_INVOLVED_COMPARTMENTS_ATTRIBUTE_NAME = "COMPARTMENTS";
	public static final String TRANSITION_INVOLVED_INFECTORS_ATTRIBUTE_NAME = "INFECTORS";
	public static final String COMPARTMENT_TRANSITIONS_ATTRIBUTE_NAME = "TRANSITIONS";
	public static final String INFECTOR_PARENTS_ATTRIBUTE_NAME = "INFECTOR_PARENTS";
	
	public static final String TEMPORARY_COMPONENTS_LAYER_ID_ATTRIBUTE_VALUE = "TEMPORARY_COMPONENTS";
	public static final String INFECTOR_COMPONENTS_LAYER_ID_ATTRIBUTE_VALUE = "INFECTOR_COMPONENTS";
	public static final String HELPER_COMPONENTS_LAYER_ID_ATTRIBUTE_VALUE = "HELPER_COMPONENTS";
	public static final String TRANSITION_LAYER_ID_ATTRIBUTE_VALUE = "TRANSITION";
	public static final String COMPARTMENT_LAYER_ID_ATTRIBUTE_VALUE = "COMPARTMENT";
	public static final String COMPARTMENT_TYPE_ATTRIBUTE_VALUE = "CORE_COMPARTMENT";
	public static final String COMPARTMENT_LABEL_TYPE_ATTRIBUTE_VALUE = "COMPARTMENT_LABEL";
	public static final String PARAMETER_ATTRIBUTE_VALUE = "PARAMETER";
	public static final String PARAMETER_TABLE_TYPE_ATTRIBUTE_VALUE = "PARAMETER_TABLE";
	public static final String SIMPLE_TRANSITION_TYPE_ATTRIBUTE_VALUE = "SIMPLE_TRANSITION";
	public static final String COMPLEX_TRANSITION_TYPE_ATTRIBUTE_VALUE = "COMPLEX_TRANSITION";
	public static final String CONNECTING_EDGE_TYPE_ATTRIBUTE_VALUE = "CONNECTING_EDGE";
	public static final String TRANSITION_ARROW_TYPE_ATTRIBUTE_VALUE = "ARROW";
	public static final String SIMPLE_TRANSITION_RATIO_ATTRIBUTE_NAME = "SIMPLE_TRANSITION_LABEL";
	public static final String COMPLEX_TRANSITION_RATIO_ATTRIBUTE_NAME = "COMPLEX_TRANSITION_LABEL";
	public static final String INFECTOR_ADDER_TYPE_ATTRIBUTE_VALUE = "INFECTOR_ADDER";
	public static final String INFECTOR_DELETER_TYPE_ATTRIBUTE_VALUE = "INFECTOR_DELETER";
	public static final String INFECTOR_COMBO_BOX_TYPE_ATTRIBUTE_VALUE = "INFECTOR_COMBO_BOX";
	public static final String INFECTOR_INFORMATION_PANEL_TYPE_ATTRIBUTE_VALUE = "INFECTOR_INFORMATION_PANEL";
	
	public static final String SIMPLE_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE = "SIMPLE_TRANSITION_HANDLE";
	public static final String COMPLEX_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE = "COMPLEX_TRANSITION_HANDLE";
	public static final String SIMPLE_TRANSITION_RATIO_DEFAULT_VALUE = "rate";
	public static final String COMPLEX_TRANSITION_RATIO_DEFAULT_VALUE = "c_rate";
	
	public static final String NEW_PARAMETER_DEFAULT_VALUE = "1.0";

}