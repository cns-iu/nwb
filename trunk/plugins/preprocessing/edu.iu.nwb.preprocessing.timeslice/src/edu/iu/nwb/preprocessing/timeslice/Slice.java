package edu.iu.nwb.preprocessing.timeslice;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.osgi.service.log.LogService;

import prefuse.data.DataTypeException;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.util.collections.IntIterator;

/**
 * @author rduhon & modified by @author Chintan Tank
 *
 */
public class Slice implements Algorithm {
	public static final int MONTHS_PER_QUARTER = 3;
	public static final int YEARS_PER_DECADE = 10;
	public static final int YEARS_PER_CENTURY = 100;
	private Data[] data;
	private Dictionary<String, Object> parameters;
	private LogService logger;
	private static final String DEFAULT_CUSTOM_TIME = "yyyy";


	private static Map<String, DateTimeFieldType> alignmentMap = 
		new HashMap<String, DateTimeFieldType>();
	private static Map<DateTimeFieldType, DateTimeFieldType> dependencyMap = 
		new HashMap<DateTimeFieldType, DateTimeFieldType>();
	private static Map<String, Integer> weekDayMap = new HashMap<String, Integer>();
	private static Map<DateTimeFieldType, Period> rollBackMap = 
		new HashMap<DateTimeFieldType, Period>();
	private static Map<String, Period> periodMap = new HashMap<String, Period>();
	private static Map<String, Period> formatTokenToPeriod = new LinkedHashMap<String, Period>();
	private static Set<String> periodPatternTokens = new HashSet<String>();
	private Period smallestInputPeriodComponent = Period.millis(1);
	private DateTimeFormatter format;
	static {
		
		/* What field to zero out for a particular alignment
		 * 
		 * The following can't be aligned:
		 * fortnights, quarters, decades, milliseconds
		 */
		alignmentMap.put("seconds", DateTimeFieldType.millisOfSecond());
		alignmentMap.put("minutes", DateTimeFieldType.secondOfMinute());
		alignmentMap.put("hours", DateTimeFieldType.minuteOfHour());
		alignmentMap.put("days", DateTimeFieldType.millisOfDay());
		alignmentMap.put("weeks", DateTimeFieldType.dayOfWeek());
		alignmentMap.put("months", DateTimeFieldType.dayOfMonth());
		alignmentMap.put("years", DateTimeFieldType.dayOfYear());
		alignmentMap.put("centuries", DateTimeFieldType.yearOfCentury());
		//alignmentMap.put("eras", DateTimeFieldType.centuryOfEra());

		//to recursively determine what needs to be "zero'd" if a particular field is zero'd
		dependencyMap.put(DateTimeFieldType.secondOfMinute(), DateTimeFieldType.millisOfSecond());
		dependencyMap.put(DateTimeFieldType.minuteOfHour(), DateTimeFieldType.secondOfMinute());
		dependencyMap.put(DateTimeFieldType.dayOfWeek(), DateTimeFieldType.millisOfDay());
		dependencyMap.put(DateTimeFieldType.dayOfMonth(), DateTimeFieldType.millisOfDay());
		dependencyMap.put(DateTimeFieldType.dayOfYear(), DateTimeFieldType.millisOfDay());
		dependencyMap.put(DateTimeFieldType.yearOfCentury(), DateTimeFieldType.dayOfYear());
		//dependencyMap.put(DateTimeFieldType.centuryOfEra(), DateTimeFieldType.yearOfCentury());

		//what constant represents what day of the week
		weekDayMap.put("saturday", new Integer(DateTimeConstants.SATURDAY));
		weekDayMap.put("sunday", new Integer(DateTimeConstants.SUNDAY));
		weekDayMap.put("monday", new Integer(DateTimeConstants.MONDAY));

		//how far should a date be rolled back if the field is zero'd to more than the current value
		rollBackMap.put(DateTimeFieldType.dayOfWeek(), Period.weeks(1));

		//how long a period is for each interval chosen
		periodMap.put("milliseconds", Period.millis(1));
		periodMap.put("seconds", Period.seconds(1));
		periodMap.put("minutes", Period.minutes(1));
		periodMap.put("hours", Period.hours(1));
		periodMap.put("days", Period.days(1));
		periodMap.put("weeks", Period.weeks(1));
		periodMap.put("months", Period.months(1));
		periodMap.put("quarters", Period.months(MONTHS_PER_QUARTER));
		periodMap.put("years", Period.years(1));
		periodMap.put("decades", Period.years(YEARS_PER_DECADE));
		periodMap.put("centuries", Period.years(YEARS_PER_CENTURY));
		//periodMap.put("eras", Period);
		
		/*
		 * Any new period type must be added in-order.
		 * */
		formatTokenToPeriod.put("S", periodMap.get("milliseconds"));
		formatTokenToPeriod.put("s", periodMap.get("seconds"));
		formatTokenToPeriod.put("m", periodMap.get("minutes"));
		formatTokenToPeriod.put("k", periodMap.get("hours"));
		formatTokenToPeriod.put("H", periodMap.get("hours"));
		formatTokenToPeriod.put("K", periodMap.get("hours"));
		formatTokenToPeriod.put("h", periodMap.get("hours"));
		formatTokenToPeriod.put("d", periodMap.get("days"));
		formatTokenToPeriod.put("D", periodMap.get("days"));
		formatTokenToPeriod.put("E", periodMap.get("days"));
		formatTokenToPeriod.put("e", periodMap.get("days"));
		formatTokenToPeriod.put("w", periodMap.get("weeks"));
		formatTokenToPeriod.put("M", periodMap.get("months"));
		formatTokenToPeriod.put("x", periodMap.get("years"));
		formatTokenToPeriod.put("Y", periodMap.get("years"));
		formatTokenToPeriod.put("y", periodMap.get("years"));
		formatTokenToPeriod.put("C", periodMap.get("centuries"));
		
		/*
		 * Set consisting of all possible format tokens supported by the plugin.
		 * */
		periodPatternTokens.addAll(formatTokenToPeriod.keySet());

	}

	public Slice(Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.logger = (LogService) context.getService(LogService.class.getName());

	}
	
	public static void main(String[] ss) throws ParseException {
		DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yy");
		DateTime date1 = format.parseDateTime("01/12/4");
		DateTime date2 = format.parseDateTime("01/01/-3");
		
		
		Years years = Years.yearsBetween(date2, date1);
		System.out.println(years.getYears());
	}

	/* (non-Javadoc)
	 * @see org.cishell.framework.algorithm.Algorithm#execute()
	 */
	public Data[] execute() throws AlgorithmExecutionException {

		/*
		 * iterate over rows. For each row, store the corresponding LocalDateTime and the index 
		 * for the row in a SortedMap get the min and max keys
		 * if align is true, move min so it is aligned with the appropriate interval
		 * iterate in periods of slice into from min to max
		 * if cumulative, keep adding to every slice; otherwise, only the latest
		 * */

		Table table = (Table) data[0].getData();
		
		String formatString = (String) parameters.get("format");
		String dateColumn = (String) parameters.get("column");
		String interval = (String) parameters.get("interval");
		boolean align = (Boolean) parameters.get("align");
		boolean cumulative = (Boolean) parameters.get("cumulative");
		String weekStarts = (String) parameters.get("weekstarts");
		
		smallestInputPeriodComponent = initializeSmallestInputPeriodComponent(formatString);
		
		format = DateTimeFormat.forPattern(formatString);

		LocalDateTime[] customYearRange = initializeCustomPeriodRange();
		
		int periodMultiplier = (Integer) parameters.get("periodmultiplier");
		
		if (periodMultiplier <= 0) {
			periodMultiplier = 1;
		}
		
		Period periodOriginal = periodMap.get(interval);

		Period period = multiplyPeriod(periodOriginal, periodMultiplier);

		SortedMap<LocalDateTime, Set<Integer>> byDateTime = createDateTimeMap(table, dateColumn);

		/*
		 * this is where tables will be put as created, and new rows will be added through the 
		 * TableGroup interface
		 * */
		TableGroup tables = createTableGroup(cumulative);
		LocalDateTime[] recordsExtractionBounds = initializeRecordsExtractionBounds(
				interval, align, weekStarts, period, customYearRange, byDateTime);
		List<List<LocalDateTime>> epochRange = accumulateTables(
				table, period, recordsExtractionBounds, byDateTime, tables, cumulative);

		return dataChildrenOf(
				data[0], 
				tables.getTables(), 
				epochRange.get(0).toArray(new LocalDateTime[]{}), 
				epochRange.get(1).toArray(new LocalDateTime[]{}),
				period);
	}

	/**
	 * Used to initialize smallest period component. The order of period is set by
	 * periodPatternTokens - which is a linked hash map containing this information. 
	 * @param formatString
	 */
	private Period initializeSmallestInputPeriodComponent(String formatString) {
		
		/*
		 * If the format entered doesnot comply with JODA Time library format then
		 * at the minimum resolve to using millisecond.
		 * */
		Period inputPeriodComponent = Period.millis(1);
		
		/*
		 * Collect all the unique format tokens in the input.
		 * */
		Set<String> inputFormatTokens = new HashSet<String>(Arrays.asList(formatString.split("")));
		
		/*
		 * Retain only those tokens that are legal - this helps in removing literals like
		 * /, - etc.
		 * */
		inputFormatTokens.retainAll(periodPatternTokens);
		
		/*
		 * Walk down the linked hash map containing legal format tokens - smallest to 
		 * largest. Once matching token found assign its Periodic value.
		 * */
		for (Iterator<String> formatTokenIterator = formatTokenToPeriod.keySet().iterator(); 
				formatTokenIterator.hasNext();) {
			String currentFormatToken = formatTokenIterator.next();
			
			if (inputFormatTokens.contains(currentFormatToken)) {
				inputPeriodComponent = formatTokenToPeriod.get(currentFormatToken);
				break;
			}
		}
		return inputPeriodComponent;
	}

	/**
	 * Initializes the Custom period range provided by the user.
	 * @param customYearRange
	 * @return 
	 */
	private LocalDateTime[] initializeCustomPeriodRange() {

		LocalDateTime[] customPeriodRange = {null, null};
		
		String fromTimeString = (String) parameters.get("fromtime");
		if (!DEFAULT_CUSTOM_TIME.equalsIgnoreCase(fromTimeString)) {
			try {
				customPeriodRange[0] = new LocalDateTime(format.parseDateTime(fromTimeString));
			} catch (IllegalArgumentException e) {
				logger.log(LogService.LOG_WARNING, 
						"Problem parsing " + fromTimeString + ". \"From time\" value " 
						+ "format should look like " + format.print(new DateTime()) 
						+ ". Using default \"From time\" value.", e);
			}
		}

		String toTimeString = (String) parameters.get("totime");
		if (!DEFAULT_CUSTOM_TIME.equalsIgnoreCase(toTimeString)) {
			try {
				customPeriodRange[1] = new LocalDateTime(format.parseDateTime(toTimeString));
			} catch (IllegalArgumentException e) {
				logger.log(LogService.LOG_WARNING, 
						"Problem parsing " + toTimeString + ". \"To time\" value " 
						+ "format should look like " + format.print(new DateTime()) 
						+ ". Using default \"To time\" value.", e);
			}
		}
		return customPeriodRange;
	}

	/**
	 * Multiplies the original period as per the period multiplier provided by the user.
	 * @param interval is a string indicating which interval is selected
	 * @return
	 */

	private Period multiplyPeriod(Period periodOriginal, int periodMultiplier) {
		Period period = periodOriginal;

		for (int ii = 1; ii < periodMultiplier; ii++) {
			period = period.plus(periodOriginal);
		}
		return period;
	}

	/**
	 * Puts new tables in a {@link TableGroup}, adds rows to the TableGroup, and returns 
	 * a list of start times.
	 * 
	 * @param table the original data table
	 * @param interval a string indicating which interval is selected
	 * @param align if the interval needs to be aligned to calendar intervals
	 * @param weekStarts a string indicating the day of the week each week starts on
	 * @param period how long each slice of time is
	 * @param customYearRange array of "from" & "to" year provided by the user. 
	 * @param byDateTime a map from times into sets of row ids
	 * @param tables the TableGroup to accumulate into
	 * @param cumulative 
	 * @return an Object containing list of start & end times
	 * @throws AlgorithmExecutionException 
	 */

	private List<List<LocalDateTime>> accumulateTables(
			Table table,
			Period period,
			LocalDateTime[] recordsExtractionBounds,
			SortedMap<LocalDateTime, Set<Integer>> byDateTime,
			TableGroup tables, 
			boolean cumulative) 
				throws AlgorithmExecutionException {

		Schema schema = table.getSchema();

		LocalDateTime current = new LocalDateTime(recordsExtractionBounds[1]);
		
		LocalDateTime currentForLabel;

		List<LocalDateTime> ends = new ArrayList<LocalDateTime>();
		List<LocalDateTime> starts = new ArrayList<LocalDateTime>();
		
		while (current.compareTo(recordsExtractionBounds[0]) > 0) {


			LocalDateTime currentMinus = new LocalDateTime(current.minus(period));
			tables.addTable(schema.instantiate());

			/* 
			 * All the records having DateTime greater than or equal to "currentMinus" & strictly
			 * less than "current" objects are extracted in a separate table using "subMap" method. 
			 */

			if (currentMinus.compareTo(recordsExtractionBounds[0]) < 0) {
				currentMinus = recordsExtractionBounds[0];
			}

			
			Collection<Set<Integer>> rowSets = byDateTime.subMap(currentMinus, current).values();
			addRowSets(tables, rowSets, table);
			currentForLabel =  current.minus(smallestInputPeriodComponent);
			ends.add(currentForLabel);
			
			/*
			 * If Cumulative option is selected we would want the label for the start time 
			 * to be the same for all the time slices.
			 * */
			if (cumulative) {
				starts.add(recordsExtractionBounds[0]);
			} else {
				starts.add(currentMinus);
			}
			current = currentMinus;
		}

		List<List<LocalDateTime>> returnRange = new ArrayList<List<LocalDateTime>>();
		returnRange.add(starts);
		returnRange.add(ends);

		return returnRange;
	}

	/**
	 * Used to initialize the bounds for records extractions from the tables.
	 * @param interval
	 * @param align
	 * @param weekStarts
	 * @param period
	 * @param customYearRange
	 * @param byDateTime
	 * @return recordsExtractionBounds
	 * @throws AlgorithmExecutionException
	 */
	
	private LocalDateTime[] initializeRecordsExtractionBounds(
			String interval,
			boolean align, 
			String weekStarts, 
			Period period,
			LocalDateTime[] customYearRange, 
			SortedMap<LocalDateTime, Set<Integer>> byDateTime)
				throws AlgorithmExecutionException {
		
		LocalDateTime[] recordsExtractionBounds = {null, null};
		
		/*
		 * To handle the inappropriate input of "From time" value being greater than "To time". 
		 * Only legal case where "From time" value can be greater than "To time" is when no 
		 * "To time" value provided. This is also handled. 
		 * */
		
		if (customYearRange[1] != null 
				&& customYearRange[0] != null 
				&&  customYearRange[0].compareTo(customYearRange[1]) > 0) { 
			recordsExtractionBounds[0] = realMin(byDateTime, interval, align, weekStarts);
			recordsExtractionBounds[1] = realMax(byDateTime);	
			throw new AlgorithmExecutionException(
					"\"From year\" value cannot be more than \"To year\" value.");
		} else {
			
			/*
			 * If a legal "From year" value is provided by the user. 
			 * */
			if (customYearRange[0] != null) {
				recordsExtractionBounds[0] = customYearRange[0];
			} else {
				recordsExtractionBounds[0] = realMin(byDateTime, interval, align, weekStarts);
			}
			
			/*
			 * If a legal "To year" value is provided by the user. 
			 * */
			if (customYearRange[1] != null) {
				recordsExtractionBounds[1] = customYearRange[1];
			} else {
				recordsExtractionBounds[1] = realMax(byDateTime);
			}
		}

		/*
		 * In order to make the Maximum bound to be inclusive we add the smallest possible
		 * period component provided by the user as the DateTime Format String.
		 * */
		recordsExtractionBounds[1] = recordsExtractionBounds[1].plus(smallestInputPeriodComponent);
		
		return recordsExtractionBounds;
	}

	/**
	 * Returns an array of data as children of the parent containing the tables in the array.
	 * This will include empty tables.
	 * @param parent the data item to be the parent of the new data items
	 * @param tables the tables to become data items
	 * @param starts a list of start times, for the descriptive label
	 * @param ends a list of end times, for the descriptive label
	 * @param period how long each period was
	 * @return an array of data items
	 */
	private Data[] dataChildrenOf(Data parent, 
								  Table[] tables, 
								  LocalDateTime[] starts, 
								  LocalDateTime[] ends, 
								  Period period) {
		/* 
		 * Predefined array size will only works if the out array size is equal to the 
		 * tables' size. If the filtering is applied to the result, make sure the returned
		 * output array size must not contain NULL value.
		 */
		Data[] output = new Data[tables.length];

		for (int ii = 0; ii < output.length; ii++) {
			Data data = new BasicData(tables[ii], Table.class.getName());
			data = updateProperties(data,
									parent,
									starts[ii],
									ends[ii],
									tables[ii].getRowCount(),
									period);
			output[ii] = data;
		}
		return output;
	}

	/**
	 * Sets a data item like the first with the appropriate metadata set. Currently 
	 * returns the original item.
	 * 
	 * @param data a data item to set metadata on
	 * @param parent the parent to set
	 * @param start a start time to use for the label
	 * @param end a end time to use for the label
	 * @param rowCount how many rows are in the data item, for the label
	 * @param period 
	 * @return the original data item with updated metadata
	 */
	private Data updateProperties(Data data, 
								  Data parent, 
								  LocalDateTime start, 
								  LocalDateTime end, 
								  int rowCount, Period period) {

		Dictionary<String, Object> metadata = data.getMetadata();
		metadata.put(DataProperty.LABEL, 
						"slice from beginning of " + format.print(start) 
						+ " to end of " + format.print(end) + " (" + rowCount + " records)");
		metadata.put(DataProperty.PARENT, parent);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);

		return data;
	}

	/**
	 * Returns the appropriate {@link TableGroup}.
	 * 
	 * @param cumulative should the results be cumulative?
	 * @return the appropriate TableGroup
	 */
	private TableGroup createTableGroup(boolean cumulative) {
		if (cumulative) {
			return new MultiTableGroup();
		} else {
			return new SingleTableGroup();
		}
	}

	/**
	 * Adds sets of rows, indicated by sets of ids, to the {@link TableGroup}.
	 * 
	 * @param tables the TableGroup to add rows to
	 * @param rowSets a collection of sets of ids from the original table
	 * @param table the original table to copy rows from
	 */
	private void addRowSets(TableGroup tables, Collection<Set<Integer>> rowSets, Table table) {
		Iterator<Set<Integer>> idSets = rowSets.iterator();
		while (idSets.hasNext()) {
			Set<Integer> idSet = idSets.next();
			addRows(tables, idSet, table);
		}
	}

	/**
	 * Adds a set of rows, indicated by ids, to the {@link TableGroup}.
	 * 
	 * @param tables the TableGroup to add rows to
	 * @param idSet a set of ids from the original table
	 * @param table the original table to copy rows from
	 */
	private void addRows(TableGroup tables, Set<Integer> idSet, Table table) {
		Iterator<Integer> ids = idSet.iterator();
		while (ids.hasNext()) {
			int id = ids.next();
			tables.addTupleToAll(table.getTuple(id));
		}
	}

	/**
	 * Returns the real maximum value to use, which is the successor to the maximum value 
	 * in the data set.
	 * 
	 * @param byDateTime the map from dates to sets of rows
	 * @return the maximum date/time to use
	 */
	private LocalDateTime realMax(SortedMap<LocalDateTime, Set<Integer>> byDateTime) {
		LocalDateTime max = byDateTime.lastKey();
		return max;
	}

	/**
	 * Returns the real minimum value to use, which may require alignment with the calendar.
	 * 
	 * @param byDateTime the map from dates to sets of rows
	 * @param interval a string indicating which interval should be used
	 * @param align if the result should be aligned with the calendar
	 * @param weekStarts a string indicating which day is the first day of a week
	 * @return
	 */
	private LocalDateTime realMin(
			SortedMap<LocalDateTime, Set<Integer>> byDateTime, 
			String interval, 
			boolean align, 
			String weekStarts) {
		LocalDateTime min = byDateTime.firstKey();
		if (align) {
			min = truncate(min, interval, weekStarts);
		}
		return min;
	}

	/**
	 * Takes the first time in the data and truncates it to align with the calendar.
	 * Sensitive to differing week starts, but not all intervals can be truncated.
	 * The current exceptions are: millisecond, fortnight, decade
	 * 
	 * @param min the minimum time in the data
	 * @param interval a string representing the desired interval
	 * @param weekStarts a string representing the day a week starts on
	 * @return the truncated minimum date/time
	 */
	private LocalDateTime truncate(LocalDateTime min, String interval, String weekStarts) {
		if (!alignmentMap.containsKey(interval)) {
			logger.log(LogService.LOG_WARNING, 
					interval + " cannot be aligned with the calendar; " 
					+ "proceeding without alignment.");
			return min;
		}

		DateTimeFieldType fieldType = alignmentMap.get(interval);
		min = rollBackMainField(min, fieldType, weekStarts);
		//zeroes the fields at finer resolution than the main field to be rolled back
		min = zeroOut(min, fieldType);
		return min;
	}

	/**
	 * Return a date time where the field indicated by the {@link DateTimeFieldType} 
	 * is rolled back to align with the calendar.
	 * 
	 * @param min the current minimum date/time.
	 * @param fieldType the field to roll back
	 * @param weekStarts a string indicating the day a week starts on
	 * @return a date time with a field rolled back to align with the calendar
	 */
	private LocalDateTime rollBackMainField(LocalDateTime min,
			DateTimeFieldType fieldType, String weekStarts) {
		int firstValue = getFirstValue(fieldType, weekStarts);
		if (firstValue > min.get(fieldType)) {
			Period rollBack = rollBackMap.get(fieldType);
			min = min.minus(rollBack);
		}

		return min.withField(fieldType, firstValue);
	}

	/**
	 * Zero's out fields with finer resolution than the main field.
	 * For instance, if minutes are rolled back to align with the calendar, 
	 * seconds and milliseconds need to be zero'd out.
	 * 
	 * @param min the current minimum date/time
	 * @param fieldType the main fieldType
	 * @return a date time with all fields of finer resolution than the main field zero'd out.
	 */
	private LocalDateTime zeroOut(LocalDateTime min, DateTimeFieldType fieldType) {
		while (dependencyMap.containsKey(fieldType)) {
			fieldType = dependencyMap.get(fieldType);
			
			/*
			 * maybe not 0 in the future, instead check for the right 'zero' using getFirstValue, 
			 * or maybe just use rollBackMainField. But for now, only dayOfWeek has that issue, 
			 * and it is not a dependency of any of the things that can be rolled back
			 */
			min = min.withField(fieldType, 0); 
		}
		return min;
	}

	/**
	 * For a given {@link DateTimeFieldType}, returns the appropriate first value.
	 * 
	 * @param fieldType the field type in question
	 * @param weekStarts a string indicating the day a week starts on
	 * @return the integer value for the appropriate first value of the field type
	 */
	private int getFirstValue(DateTimeFieldType fieldType, String weekStarts) {

		if (DateTimeFieldType.dayOfWeek().equals(fieldType)) {
			return weekDayMap.get(weekStarts).intValue();
		}
		return 1;
	}

	/**
	 * Accumulates ids of rows into a {@link SortedMap} from date/times to sets of ids for 
	 * rows occurring in each date/time.
	 * 
	 * @param table the table to accumulate rows from
	 * @param dateColumn the column in the table with date/times for the row
	 * @return a sorted map from date/times to sets of ids
	 * @throws AlgorithmExecutionException
	 */
	private SortedMap<LocalDateTime, Set<Integer>> 
	createDateTimeMap(Table table, String dateColumn) throws AlgorithmExecutionException {
		
		SortedMap<LocalDateTime, Set<Integer>> byDateTime = 
			new TreeMap<LocalDateTime, Set<Integer>>();
		
		IntIterator ids = table.rows();
		while (ids.hasNext()) {
			int id = ids.nextInt();
			LocalDateTime dateTime;
			String dateTimeString = null;
			try {
				/*
				 * toString method was applied on the object instead of directly using getString
				 * method because prefuse tries to be smart about data types of the column & in 
				 * the process sometimes ends corrupting data. This mostly happens when the Column 
				 * format is set to Date by prefuse.
				 * To get around this we get the table element as an object and then convert to 
				 * string.
				 * */
				dateTimeString = table.get(id, dateColumn).toString();
				dateTime = new LocalDateTime(format.parseDateTime(dateTimeString));
			} catch (IllegalArgumentException e) {
				
				/*
				 * When the column type is "Date" prefuse converts the original element into java.
				 * sql.date format. This causes its format to not match (probably) with the 
				 * original format string input by the user. 
				 * To take care of this we try to create DateTime object of this element using 
				 * this modified date format. We know for a fact that prefuse converts date element 
				 * into a format acceptable by java.util.date. 
				 * */
				try {
				dateTime = new LocalDateTime(dateTimeString);
				} catch (DataTypeException dateTypeException) {
					throw new AlgorithmExecutionException("Problem parsing " 
							+ dateTimeString, dateTypeException);
				} catch (IllegalArgumentException illegalArgumentException) {

					/*
					 * There will be cases when the format input by the user is faulty. To cover
					 * this case we catch the IllegalArgumentException again over here.
					 * */
					throw new AlgorithmExecutionException("Problem parsing " + dateTimeString 
							+ ". Date time values for that format should look like " 
							+ format.print(new DateTime()) + ".", illegalArgumentException);
				}
			}
			if (!byDateTime.containsKey(dateTime)) {
				byDateTime.put(dateTime, new HashSet<Integer>());
			}
			byDateTime.get(dateTime).add(new Integer(id));
		}
		return byDateTime;
	}
}