package edu.iu.cns.r.utility;

public class RProperties {
	public static final String R_EXECUTABLE_BASE_NAME = "R";
	public static final String R_SCRIPT_EXECUTABLE_BASE_NAME = "Rscript";
	public static final String R_GUI_EXECUTABLE_BASE_NAME = "Rgui";
	public static final String R_DATA_FILE_NAME = ".RData";

	public static final String R_EXPRESSION_COMMAND_LINE_SWITCH = "-e";
	public static final String R_FILE_COMMAND_LINE_SWITCH = "-f";
	public static final String R_SAVE_ENVIRONMENT_COMMAND_LINE_SWITCH = "--save";
	public static final String R_RESTORE_ENVIRONMENT_COMMAND_LINE_SWITCH = "--restore";
	public static final String R_SLAVE_COMMAND_LINE_SWITCH = "--slave";

	public static final String BASE_CLASS_PATH = "/edu/iu/cns/r/";
	public static final String MAKE_JAVA_FILE_NAME = "makejava";
	public static final String MAKE_JAVA_FILE_NAME_EXTENSION = ".r";
	public static final String MAKE_JAVA_PATH =
		BASE_CLASS_PATH + MAKE_JAVA_FILE_NAME + MAKE_JAVA_FILE_NAME_EXTENSION;

	public static final String R_OBJECTS_COMMAND = "objects()";
}