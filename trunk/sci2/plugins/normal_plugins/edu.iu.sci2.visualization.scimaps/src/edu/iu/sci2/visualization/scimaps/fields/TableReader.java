package edu.iu.sci2.visualization.scimaps.fields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cishell.utilities.NumberUtilities;
import org.cishell.utilities.NumberUtilities.EmptyCollectionException;
import org.cishell.utilities.NumberUtilities.NotANumberException;
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
	
	public TableReader(Table table, String nodeValueColumnName, String nodeLabelColumnName, String nodeIDColumnName, LogService logger) {
		int noValueCount = 0;
		int badUCSDAreaCount = 0;
		
		for (@SuppressWarnings("unchecked") // Raw Iterator from table.tuples()
		Iterator<Tuple> rows = table.tuples(); rows.hasNext();) {
			Tuple row = rows.next();
			
			int value = 0;
			if (FieldsMapAlgorithmFactory.NO_VALUE_COLUMN_TOKEN.equals(nodeValueColumnName)) {
				value = 1;				
			} else {
				try {
					value = NumberUtilities.interpretObjectAsDouble(row.get(nodeValueColumnName)).intValue();
				} catch (EmptyCollectionException e) {
					value = 0;
					noValueCount++;
				} catch (NotANumberException e) {
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
				
				// SOMEDAY use Set for ids
				// SOMEDAY use StringBuilder not appending
				
				for (int ucsdArea : ids) {					
					if ((label == null) || (label.trim().length() == 0)) {
						label = "Area " + String.valueOf(ucsdArea);
					}
					
					String oldLabel = "";
					if (this.ucsdAreaLabels.containsKey(ucsdArea)) {
						oldLabel = this.ucsdAreaLabels.get(ucsdArea).trim() + "; ";
					}
					if (!oldLabel.contains(label)) { // TODO Hack
						this.ucsdAreaLabels.put(ucsdArea, oldLabel + label);
					}
					
					if (MIN_UCSD_AREA <= ucsdArea && ucsdArea <= MAX_UCSD_AREA) {
						float oldValue = 0.0f;
						if (this.ucsdAreaTotals.containsKey(ucsdArea)) {
							oldValue = this.ucsdAreaTotals.get(ucsdArea);
						}
						
						this.ucsdAreaTotals.put(ucsdArea, oldValue + normalizedValue);
	
						this.goodRecordCount++;
					} else {
						badUCSDAreaCount++;
					}
				}
			} catch (EmptyCollectionException e) {
				handlingException(label, value);
			} catch (NotANumberException e) {
				handlingException(label, value);
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
	
	private void handlingException(String label, int value) {
		if ((label == null) || (label.trim().length() == 0)) {
			label = "Unidentified Area";
		}
		
		float oldValue = 0;
		if (this.ucsdAreaTotals.containsKey(label)) {
			oldValue = this.unclassifiedLabelCounts.get(label);
		}
		
		this.unclassifiedLabelCounts.put(label, oldValue + value);
		
		this.unclassifiedRecordCount++;
	}
	
	
	public int getGoodRecordCount() {
		return this.goodRecordCount;
	}

	public int getUnclassifiedRecordCount() {
		return this.unclassifiedRecordCount;
	}

	public Map<Integer, Float> getUcsdAreaTotals() {
		return this.ucsdAreaTotals;
	}

	public Map<Integer, String> getUcsdAreaLabels() {
		return this.ucsdAreaLabels;
	}

	public Map<String, Float> getUnclassifiedLabelCounts() {
		return this.unclassifiedLabelCounts;
	}
}
