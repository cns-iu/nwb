package edu.iu.epic.visualization.linegraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Schema;
import prefuse.data.Table;

import com.google.common.collect.Sets;

public class LineGraphAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String TITLE_ID = "title";
	public static final String ACTIVE_ALGORITHM_ID = "activeLineGraph";
	public static final String TIME_STEP_ID = "timeStepColumn";
	public static final String BASE_LINE_ID = "line_";

	public static final Collection<String> DEFAULT_COLUMNS_TO_IGNORE =
		Collections.unmodifiableSet(Sets.newHashSet("time"));

	public static final String NEW_LINE_GRAPH_WINDOW_VALUE = "<<New Line Graph Window>>";

	private static AtomicInteger uniqueIDCounter = new AtomicInteger(1);
	private static final Map<String, LineGraphAlgorithm> activeAlgorithmsByTitle =
		Collections.synchronizedMap(new LinkedHashMap<String, LineGraphAlgorithm>());
	private static final Collection<String> activeLineGraphAlgorithmTitles = new Stack<String>();

	private LogService logger;

	protected void activate(ComponentContext componentContext) {
		this.logger = (LogService) componentContext.locateService("LOG");
	}

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	Data inData = data[0];
    	Table inputTable = (Table) inData.getData();
    	
    	String title = ((String) parameters.get(TITLE_ID)).trim();
    	String activeAlgorithmTitle =  (String) parameters.get(ACTIVE_ALGORITHM_ID);
    	String timeStepColumnName = (String) parameters.get(TIME_STEP_ID);
    	Collection<String> lineColumnNames = extractLineColumnNames(parameters, inputTable);

    	if (shouldGraphDataInExistingLineGraph(activeAlgorithmTitle)) {
    		if (chosenLineGraphStillExists(activeAlgorithmTitle)) {
    			LineGraphAlgorithm existingAlgorithm =
    				LineGraphAlgorithmFactory.activeAlgorithmsByTitle.get(activeAlgorithmTitle);

    			try {
    				existingAlgorithm.addDataToGraph(
    					title, inputTable, timeStepColumnName, lineColumnNames);
    			} catch (AlgorithmExecutionException e) {
    				throw new AlgorithmCreationFailedException(e.getMessage(), e);
    			}

    			return new Algorithm() {
    				public Data[] execute() throws AlgorithmExecutionException {
    					/* (There is no need to do any operation in this algorithm.
    					 * The important stuff was added to the other algorithm.)
    					 */
    					
    					return new Data[0];
    				}
    			};
    		} else {
    			String format = "The Line Graph \"%s\" is no longer active.  "
		    				+ "(It was closed in between when you chose this algorithm and " 
		    				+ "when you finished entering your input parameters.)  A new " 
		    				+ "window will be created for you with just this data.";
    			String exceptionMessage = String.format(format, activeAlgorithmTitle);
    			this.logger.log(LogService.LOG_WARNING, exceptionMessage);
    		}
    	}

    	if (title == null) {
    		title = "" + LineGraphAlgorithmFactory.uniqueIDCounter.getAndIncrement();
    	}

    	/* Above, we checked if the user chose to graph their data in an already-existing
    	 * Line Graph window.  There are three possibilities for this:
    	 * 1) The user chose an active Line Graph window, and it still exists when this method
    	 * was called.
    	 * 2) The user chose an active Line Graph window, but it no longer exists (as of calling
    	 * this method).
    	 * 3) The user chose to graph their data in a new Line Graph window.
    	 * Cases 2) and 3) mean we need to create a window, but case 2) means we should print a
    	 * log message first.
    	 * The following algorithm construction code is NOT in an else so case 2) above falls
    	 * through to it.
    	 */
       	LineGraphAlgorithm algorithm = new LineGraphAlgorithm(
       		inputTable, title, timeStepColumnName, lineColumnNames, new ActiveAlgorithmHook() {
       			public void nowActive(LineGraphAlgorithm algorithm) {
       				
       				LineGraphAlgorithmFactory.activeAlgorithmsByTitle.put(
       					algorithm.getTitle(), algorithm);
       				LineGraphAlgorithmFactory.activeLineGraphAlgorithmTitles.add(
       					algorithm.getTitle());
       			}

       			public void nowInactive(LineGraphAlgorithm algorithm) {
       				
       				LineGraphAlgorithmFactory.activeAlgorithmsByTitle.remove(algorithm.getTitle());
       				LineGraphAlgorithmFactory.activeLineGraphAlgorithmTitles.remove(
       					algorithm.getTitle());
       			}
       		});

       	return algorithm;
    }
    
    public ObjectClassDefinition mutateParameters(
    		Data[] data, ObjectClassDefinition oldParameters) {
    	Table inputTable = (Table) data[0].getData();
    
    	/*
    	 * In the input table, 
    	 * we must choose one numeric or date column to be the 'time step' column,
    	 * and we must decide which of the other numeric columns should be graphed as lines.
    	 * All other columns will be ignored.
    	 */
    	
    	BasicObjectClassDefinition newParameters =
    		MutateParameterUtilities.createNewParameters(oldParameters);
    	
    	AttributeDefinition[] oldAttributeDefinitions =
			oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);

    	String[] validNumericColumnsInTable =
			TableUtilities.getValidNumberColumnNamesInTable(inputTable);

    	for (String numberColumn : validNumericColumnsInTable) {
    		String id = BASE_LINE_ID + numberColumn;
    		String name = numberColumn;
    		String description = "Graph column " + numberColumn + "?";
    		int type = AttributeDefinition.BOOLEAN;
    		String defaultValue = decideDefaultCheckBoxValue(name);
    		AttributeDefinition attributeDefinition =
    			new BasicAttributeDefinition(id, name, description, type, defaultValue);

			newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED, attributeDefinition);
    	}

    	for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
    		String oldAttributeDefinitionID = oldAttributeDefinition.getID();
    		AttributeDefinition newAttributeDefinition = oldAttributeDefinition;

    		if (TITLE_ID.equals(oldAttributeDefinitionID)) {
    			String suggestedTitle =
    				"" + LineGraphAlgorithmFactory.uniqueIDCounter.getAndIncrement();
    			newAttributeDefinition = new BasicAttributeDefinition(
    					oldAttributeDefinition.getID(),
    					oldAttributeDefinition.getName(),
    					oldAttributeDefinition.getDescription(),
    					oldAttributeDefinition.getType(),
    					suggestedTitle) {
    				@Override
    				public String validate(String value) {
    					String preValidation = super.validate(value);

    					if (preValidation != null) {
    						return preValidation;
    					} else {
    						if (LineGraphAlgorithmFactory.activeAlgorithmsByTitle.containsKey(
    								value)) {
    							String format =
    								"%s is already the title of a Line Graph visualization.  " 
    								+ "Please choose a unique title.";
    							String errorMessage = String.format(format, value);

    							return errorMessage;
    						} else {
    							return null;
    						}
    					}
    				}
    			};
    		} else if (ACTIVE_ALGORITHM_ID.equals(oldAttributeDefinitionID)) {
    			if (LineGraphAlgorithmFactory.activeAlgorithmsByTitle.size() > 0) {
    				List<String> optionList = new ArrayList<String>();
    				optionList.addAll(LineGraphAlgorithmFactory.activeLineGraphAlgorithmTitles);
    				
    				/*
    				 * To make sure that most recently opened line graph gui is shown first in the 
    				 * dropdown box.
    				 * */
    				Collections.reverse(optionList);
    				
    				/*
    				 * Always add the "New Line Graph Window" as the last option.
    				 * */
    				optionList.add(NEW_LINE_GRAPH_WINDOW_VALUE);
    				String[] options = optionList.toArray(new String[0]);
    				newAttributeDefinition = new BasicAttributeDefinition(
    					oldAttributeDefinition.getID(),
    					oldAttributeDefinition.getName(),
    					oldAttributeDefinition.getDescription(),
    					oldAttributeDefinition.getType(),
    					options,
    					options);
    			} else {
					continue;
    			}
    		} else if (TIME_STEP_ID.equals(oldAttributeDefinitionID)) {
    			newAttributeDefinition = MutateParameterUtilities.formDateAttributeDefinition(
					oldAttributeDefinition, inputTable);
    		}

    		newParameters.addAttributeDefinition(
    			ObjectClassDefinition.REQUIRED, newAttributeDefinition);
    	}

    	return newParameters;
    }

    private static String decideDefaultCheckBoxValue(String name) {
    	if (DEFAULT_COLUMNS_TO_IGNORE.contains(name.toLowerCase())) {
    		return Boolean.FALSE.toString();
    	} else {
    		return Boolean.TRUE.toString();
    	}
    }

	private Collection<String> extractLineColumnNames(
			Dictionary<String, Object> parameters, Table table) {
		Collection<String> lineColumnNames = new ArrayList<String>();
		Schema schema = table.getSchema();

		for (int ii = 0; ii < schema.getColumnCount(); ii++) {
			String columnName = schema.getColumnName(ii);
			String keyName = BASE_LINE_ID + columnName;
			Boolean value = (Boolean) parameters.get(keyName);
			
			if ((value != null) && value.booleanValue()) {
				lineColumnNames.add(columnName);
			}
		}
		
		return lineColumnNames;
	}

	private boolean shouldGraphDataInExistingLineGraph(String activeAlgorithmTitle) {
		return
			!StringUtilities.isNull_Empty_OrWhitespace(activeAlgorithmTitle) 
			&& !NEW_LINE_GRAPH_WINDOW_VALUE.equals(activeAlgorithmTitle);
	}

	private boolean chosenLineGraphStillExists(String activeAlgorithmTitle) {
		return LineGraphAlgorithmFactory.activeAlgorithmsByTitle.containsKey(activeAlgorithmTitle);
	}
}