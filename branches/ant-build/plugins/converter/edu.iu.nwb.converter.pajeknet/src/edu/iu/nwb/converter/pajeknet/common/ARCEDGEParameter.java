package edu.iu.nwb.converter.pajeknet.common;

public class ARCEDGEParameter {
	public static final String PARAMETER_WIDTH = "w";
	public static final String PARAMETER_COLOR = "c";
	public static final String PARAMETER_PATTERN = "p";
	public static final String PARAMETER_SIZE = "s";
	public static final String PARAMETER_ARROW_SHAPE = "a";
	public static final String PARAMETER_ARROW_POSITION = "ap";
	public static final String PARAMETER_LABEL = "l";
	public static final String PARAMETER_LABEL_POSITION = "lp";
	public static final String PARAMETER_LABEL_RADIUS = "lr";
	public static final String PARAMETER_LABEL_PHI = "lphi";
	public static final String PARAMETER_LABEL_COLOR = "lc";
	public static final String PARAMETER_LABEL_ANGLE = "la";
	public static final String PARAMETER_FONT_SIZE = "fos";
	public static final String PARAMETER_FONT = "font";
	public static final String PARAMETER_HOOK = "h";
	public static final String PARAMETER_ANGLE = "a";
	public static final String PARAMETER_VELOCITY = "k";

	public static final String[] ARCEDGE_NUMERIC_PARAMETER_LIST = {PARAMETER_WIDTH,
		PARAMETER_SIZE, PARAMETER_ARROW_POSITION,  PARAMETER_LABEL_POSITION,
		PARAMETER_LABEL_RADIUS, PARAMETER_LABEL_PHI, PARAMETER_LABEL_ANGLE,
		PARAMETER_FONT_SIZE, PARAMETER_HOOK,  PARAMETER_ANGLE,
		PARAMETER_VELOCITY, };
	public static final String[] ARCEDGE_STRING_PARAMETER_LIST = {PARAMETER_LABEL, PARAMETER_COLOR,
		PARAMETER_LABEL_COLOR, PARAMETER_FONT, PARAMETER_PATTERN, PARAMETER_ARROW_SHAPE};

	public static final String[] ARCEDGE_COLOR_LIST = {PARAMETER_COLOR,PARAMETER_LABEL_COLOR};
	}


