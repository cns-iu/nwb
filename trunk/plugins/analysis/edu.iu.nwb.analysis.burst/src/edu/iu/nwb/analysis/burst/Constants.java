package edu.iu.nwb.analysis.burst;

import edu.iu.nwb.analysis.burst.batcher.BatchFactory;

public final class Constants {
	
	private Constants() {
	}
	
	/* Input columns' name */
	public static final String GAMMA_COLUMN = "gamma";
	public static final String RATIO_COLUMN = "ratio";
	public static final String BURSTING_STATES_COLUMN = "states";
	public static final String DATE_COLUMN = "date";
	public static final String DATE_FORMAT_COLUMN = "format";
	public static final String BATCH_BY_COLUMN = "batchBy";
	public static final String BATCH_BY_UNITS_COLUMN = "batchByUnits";
	public static final String TEXT_COLUMN = "text";
	public static final String TEXT_SEPARATOR_COLUMN = "separator";
	public static final String DOCUMENT_COLUMN = "document";
	public static final String IGNORE_EMPTY_COLUMN = "ignore";
	
	/* Output columns' name */
	public static final String WORD_COLUMN = "Word";
	public static final String LEVEL_COLUMN = "Level";
	public static final String WEIGHT_COLUMN = "Weight";
	public static final String LENGTH_COLUMN = "Length";
	public static final String START_COLUMN = "Start";
	public static final String END_COLUMN = "End";
	
	/* The available options for BATCHED_BY_COLUMN */
	public static final String[] BATCHED_BY_OPTIONS = BatchFactory.userFriendlyNames();

}
