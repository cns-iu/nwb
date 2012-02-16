package edu.iu.sci2.visualization.scimaps.fields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cishell.utilities.NumberUtilities;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;

public class TableReader {
	public static final int MIN_UCSD_AREA = 1;
	public static final int MAX_UCSD_AREA = 554;
	
	private int goodRecordCount = 0;
	private int unclassifiedRecordCount = 0;
	private Map<Integer, Float> ucsdAreaTotals = new HashMap<Integer, Float>();
	private Map<Integer, String> ucsdAreaLabels = new HashMap<Integer, String>();
	private Map<String, Float> unclassifiedLabelCounts = new HashMap<String, Float>();
	
	@SuppressWarnings("unchecked") // Raw Iterator from table.tuples()
	public TableReader(Table table, String nodeValueColumnName, String nodeLabelColumnName, String nodeIDColumnName, LogService logger) {
		int noValueCount = 0;
		int badUCSDAreaCount = 0;
		
		for (Iterator<Tuple> rows = table.tuples(); rows.hasNext();) {
			Tuple row = rows.next();
			
			int value = 0;
			if (FieldsMapAlgorithmFactory.NO_VALUE_COLUMN_TOKEN.equals(nodeValueColumnName)) {
				value = 1;				
			} else {
				try {
					value = NumberUtilities.interpretObjectAsDouble(row.get(nodeValueColumnName)).intValue();
				} catch (NumberFormatException e) {
					value = 0;
					noValueCount++;
				}
				
				if (value < 0) {
					noValueCount++;
					continue;
				}
			}
			
			String label =
				StringUtilities.interpretObjectAsString(row.get(nodeLabelColumnName));						
			
			try {
				Object idsObject = row.get(nodeIDColumnName);
				
				List<Integer> ids = new ArrayList<Integer>();
				if (idsObject instanceof String) { // TODO Or just try cast?
					String idsString = (String) idsObject;
					String[] idStrings = idsString.split("\\D");
					
					for (String idString : idStrings) {
						if (idString.trim().length() > 0) {
							ids.add(Integer.valueOf(idString.trim()));
						}
					}
				} else {
					int id =
						NumberUtilities.interpretObjectAsDouble(row.get(nodeIDColumnName)).intValue();
					ids.add(id);
				}
				
				float normalizedValue = value / ((float) ids.size());
				
				for (int ucsdArea : ids) {					
					if ((label == null) || (label.trim().length() == 0)) {
						label = "Area " + String.valueOf(ucsdArea);
					}
					
					String oldLabel = "";
					if (ucsdAreaLabels.containsKey(ucsdArea)) {
						oldLabel = ucsdAreaLabels.get(ucsdArea).trim() + "; ";
					}
					if (!oldLabel.contains(label)) { // TODO Hack
						ucsdAreaLabels.put(ucsdArea, oldLabel + label);
					}
					
					if (MIN_UCSD_AREA <= ucsdArea && ucsdArea <= MAX_UCSD_AREA) {
						float oldValue = 0.0f;
						if (ucsdAreaTotals.containsKey(ucsdArea)) {
							oldValue = ucsdAreaTotals.get(ucsdArea);
						}
						
						ucsdAreaTotals.put(ucsdArea, oldValue + normalizedValue);
	
						goodRecordCount++;
					} else {
						badUCSDAreaCount++;
					}
				}
			} catch (NumberFormatException e) {
				if ((label == null) || (label.trim().length() == 0)) {
					label = "Unidentified Area";
				}
				
				float oldValue = 0;
				if (ucsdAreaTotals.containsKey(label)) {
					oldValue = unclassifiedLabelCounts.get(label);
				}
				
				unclassifiedLabelCounts.put(label, oldValue + value);
				
				unclassifiedRecordCount++;
			}
		}
		
		if (badUCSDAreaCount > 0) {
			logger.log(
					LogService.LOG_WARNING,
					noValueCount + " records specified an invalid UCSD area and were skipped.");
		}
		
		if (noValueCount > 0) {
			logger.log(
					LogService.LOG_WARNING,
					noValueCount + " records specified no valid value and were treated as specifying zero.");
		}
	}
	
	
	public int getGoodRecordCount() {
		return goodRecordCount;
	}

	public int getUnclassifiedRecordCount() {
		return unclassifiedRecordCount;
	}

	public Map<Integer, Float> getUcsdAreaTotals() {
		return ucsdAreaTotals;
	}

	public Map<Integer, String> getUcsdAreaLabels() {
		return ucsdAreaLabels;
	}

	public Map<String, Float> getUnclassifiedLabelCounts() {
		return unclassifiedLabelCounts;
	}
}
