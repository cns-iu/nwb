package edu.iu.epic.modelbuilder.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.exception.InvalidParameterExpressionException;
import edu.iu.epic.modeling.compartment.model.exception.InvalidParameterNameException;
import edu.iu.epic.modeling.compartment.model.exception.ParameterAlreadyDefinedException;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolox.pswing.PSwing;

public class ParameterTable {
	
	private PNode parameterTableWorkbench;
	private static URL deleteButtonMousePressedImage, deleteButtonNormalStateImage;
	private Model inMemoryModel;
	private NotificationArea[] notificationAreas;
	protected static final int parameterNameColumnIndex = 0;
	protected static final int parameterValueColumnIndex = 1;
	protected static final int deleteButtonColumnIndex = 2; 
	
	private static enum ParameterDefinitionAdditionMessageType {
		NO_ERRORS, PARAMETER_NAME_ERROR, PARAMETER_VALUE_ERROR;
	}


	public ParameterTable(final IDGenerator pObjectIDGenerator,
						  Model inputInMemoryModel,
						  Model inMemoryModel, 
						  NotificationArea[] notificationAreas,
						  PLayer helperComponentsLayer) { 

		final String deleteButtonText = "";
		parameterTableWorkbench = new PNode();
        this.inMemoryModel = inMemoryModel;
        this.notificationAreas = notificationAreas;
        
        Vector<String> columns = new Vector<String>();
        columns.add(GlobalConstants.NODE_ID_ATTRIBUTE_NAME);
        columns.add("Value");
        columns.add("");

        Vector<Vector<String>> rows = new Vector<Vector<String>>();
        Vector<String> row; new Vector<String>();

        
        
        for (Entry<String, String> parameterDefinition 
        		: inputInMemoryModel.getParameterDefinitions().entrySet()) {
        	row = new Vector<String>();
        	row.add(parameterDefinition.getKey());
        	row.add(parameterDefinition.getValue());
            row.add(deleteButtonText);
            rows.add(row);
        }

        final DefaultTableModel parameterDefaultTableModel = 
        		generateParameterDefaultTableModel(pObjectIDGenerator, columns, rows); 
        	
        JTable parameterTable = initializeParameterTable(parameterDefaultTableModel);
        
        /*
         * To disable re-ordering of the columns when table header was dragged.
         * */
        parameterTable.getTableHeader().setReorderingAllowed(false);
        
        
        Action tableCellChangedAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                TableCellListener tableCellListener = (TableCellListener) e.getSource();

                
                switch(tableCellListener.getColumn()) {
                /*
                 * Do not process the events originated from Delete button column
                 * */
                case deleteButtonColumnIndex:
                	break;
                
                /*
                 * Change in the cell content can belong to either parameterName or 
                 * parameterValue. 
                 * */	
                case parameterNameColumnIndex:
                    /*
                     * Only proceed with renaming of the parameter definition if there is 
                     * actually any change in the name of the parameter.
                     * */
                    String oldParameterName = (String) tableCellListener.getOldValue();
					String newParameterName = (String) tableCellListener.getNewValue();
					String parameterValue = (String) parameterDefaultTableModel
												.getValueAt(tableCellListener.getRow(), 
														    parameterValueColumnIndex);
					if (!oldParameterName.equalsIgnoreCase(newParameterName)) {
						Vector<String> approvedParameterDefinition = 
								renameParameterDefinition(pObjectIDGenerator,
														  newParameterName, 
														  parameterValue,
														  oldParameterName,
														  parameterValue);
						
						parameterDefaultTableModel.setValueAt(
									approvedParameterDefinition.get(parameterNameColumnIndex),
									tableCellListener.getRow(), 
								    parameterNameColumnIndex);
						
						parameterDefaultTableModel.setValueAt(
								approvedParameterDefinition.get(parameterValueColumnIndex),
								tableCellListener.getRow(), 
							    parameterValueColumnIndex);
                    }
					break;
                	
                case parameterValueColumnIndex:
                    /*
                     * Only proceed with renaming of the parameter definition if there is 
                     * actually any change in the value of the parameter.
                     * */
                    String oldParameterValue = (String) tableCellListener.getOldValue();
					String newParameterValue = (String) tableCellListener.getNewValue();
					String parameterName = (String) parameterDefaultTableModel
												.getValueAt(tableCellListener.getRow(), 
														    parameterNameColumnIndex);
					if (!oldParameterValue.equalsIgnoreCase(newParameterValue)) {
						Vector<String> approvedParameterDefinition = 
								renameParameterDefinition(pObjectIDGenerator,
														  parameterName,
														  newParameterValue,
														  parameterName,
														  oldParameterValue);
						
						parameterDefaultTableModel.setValueAt(
								approvedParameterDefinition.get(parameterNameColumnIndex),
								tableCellListener.getRow(), 
							    parameterNameColumnIndex);
					
						parameterDefaultTableModel.setValueAt(
								approvedParameterDefinition.get(parameterValueColumnIndex),
								tableCellListener.getRow(), 
							    parameterValueColumnIndex);
						
                    } 
					break;
                	
                default:
					break;
                	
                }
            }
        };

        new TableCellListener(parameterTable, tableCellChangedAction);
        
        
        /*
         * Create button column & do something more OOey with it.. current solution not 
         * satisfactory. what about notficication? mostly tablebutton cell should not 
         * know what to do once button is pressed that should be left to the calling class.
         * */
        new ParameterTableParameterDeleter(deleteButtonNormalStateImage, 
										   deleteButtonMousePressedImage, 
										   parameterDefaultTableModel,
										   parameterTable,
										   notificationAreas,
										   inMemoryModel);
        
        JScrollPane parameterTableScrollPane = new JScrollPane(parameterTable);
        parameterTableScrollPane.setPreferredSize(new Dimension(285, 300));
        
        JPanel parameterTablePanel = initializeParameterTablePanel(deleteButtonText,
        														   pObjectIDGenerator,
        														   parameterDefaultTableModel,
        														   parameterTableScrollPane);
        
        JLabel notificationLabel = new JLabel("Inconsistencies Log");
        
        parameterTablePanel.add(notificationLabel);
        
        JScrollPane notificationAreaScrollPane = new JScrollPane(notificationAreas[1]);        

        parameterTablePanel.add(notificationAreaScrollPane);
        
        PNode parameterTableWrapper = new PSwing(parameterTablePanel); 
        
        
        
        parameterTableWorkbench.addChild(parameterTableWrapper);
	
	}
	
	private Vector<String> renameParameterDefinition(IDGenerator pObjectIDGenerator,
										   String newParameterName,
										   String newParameterValue, 
										   String oldParameterName, 
										   String oldParameterValue) {
		inMemoryModel.removeParameterDefinition(oldParameterName);
		return addParameterDefinition(pObjectIDGenerator,
							   newParameterName, 
							   newParameterValue, 
							   oldParameterName, 
							   oldParameterValue);
		
	}

	private JPanel initializeParameterTablePanel(
			final String deleteButtonText,
			final IDGenerator pObjectIDGenerator,
			final DefaultTableModel parameterDefaultTableModel,
			JScrollPane parameterTableScrollPane) {
		
		String addParameterButtonText = "Add Parameter";
		
		JPanel parameterTablePanel = new JPanel();
		parameterTablePanel.setLayout(new BoxLayout(parameterTablePanel, BoxLayout.Y_AXIS));
    	
		
		parameterTableScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        parameterTablePanel.add(parameterTableScrollPane);
        
        JLabel unboundParametersAreaLabel = new JLabel("Unbound Parameters");
        
        parameterTablePanel.add(unboundParametersAreaLabel);
        
        JScrollPane unboundParametersAreaScrollPane = new JScrollPane(notificationAreas[0]);        

        parameterTablePanel.add(unboundParametersAreaScrollPane);
        
		JButton addParametersButton = new JButton(addParameterButtonText); 
		addParametersButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		parameterTablePanel.add(addParametersButton);
		
		addParametersButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
		        Vector<String> newParameterRow = new Vector<String>();
		        
		        String newParameterName = pObjectIDGenerator.getParameterCounter();
		        String newParameterValue = GlobalConstants.NEW_PARAMETER_DEFAULT_VALUE;
		        
		        /*
		         * Values of newParameterName & newParameterValue is guaranteed to be legal.
		         * Hence we can pass them off again as the backup values.
		         * */
	        	Vector<String> parameterDefinition = 
	        		addParameterDefinition(pObjectIDGenerator,
	        							   newParameterName, 
	        							   newParameterValue,
	        							   newParameterName,
	        							   newParameterValue);
				
	        	newParameterRow.add(parameterDefinition.get(parameterNameColumnIndex));
	        	newParameterRow.add(parameterDefinition.get(parameterValueColumnIndex));
		        newParameterRow.add(deleteButtonText);
		        parameterDefaultTableModel.addRow(newParameterRow);
		        
			}
        	
        });
		
		return parameterTablePanel;
	}

	public static void refreshParameterWorkbenchLocation(
			Dimension currentJInternalFrameSize,
			PLayer helperComponentsLayer) {
    	
    	
    	PNode parameterTableWorkbench = 
    			PiccoloUtilities.getChildComponentBasedOnGivenAttribute(
    					helperComponentsLayer, 
    					GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME,
    					GlobalConstants.PARAMETER_TABLE_TYPE_ATTRIBUTE_VALUE);
    	
		PBounds parameterCurrentFullBounds = parameterTableWorkbench.getFullBoundsReference();
		
		parameterTableWorkbench
			.setOffset(currentJInternalFrameSize.getWidth() 
							- parameterCurrentFullBounds.getWidth() 
							- 25.0, 
						0.0);
	}
    
	private JTable initializeParameterTable(final DefaultTableModel parameterDefaultTableModel) {
		JTable parameterTable = new JTable(parameterDefaultTableModel);
        parameterTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        parameterTable.setRowHeight(30);
        
        
        TableColumn tableColumn = parameterTable
        								.getColumn(parameterTable
        												.getColumnName(
        														parameterNameColumnIndex));
        tableColumn.setPreferredWidth(150);
        
        tableColumn = parameterTable
        					.getColumn(parameterTable
        									.getColumnName(
        											parameterValueColumnIndex));
        tableColumn.setPreferredWidth(100);
        
        tableColumn = parameterTable
        					.getColumn(parameterTable
        									.getColumnName(
        											deleteButtonColumnIndex));
        tableColumn.setPreferredWidth(30);
        parameterTable.setDoubleBuffered(false);
		return parameterTable;
	}

	private DefaultTableModel generateParameterDefaultTableModel(
									IDGenerator pObjectIDGenerator,
									Vector<String> columns, 
									Vector<Vector<String>> rows) {
  
		DefaultTableModel parameterDefaultTableModel = new DefaultTableModel();

        parameterDefaultTableModel.addColumn(columns.get(parameterNameColumnIndex));
		parameterDefaultTableModel.addColumn(columns.get(parameterValueColumnIndex));
		parameterDefaultTableModel.addColumn(columns.get(deleteButtonColumnIndex));
        
        
        for (Vector<String> currentRowValues : rows) {	
        	
        	String parameterName = currentRowValues.get(parameterNameColumnIndex);
        	String parameterValue = currentRowValues.get(parameterValueColumnIndex);
        	
	        String backupParameterName = pObjectIDGenerator.getParameterCounter();
	        
	        String backupParameterValue = GlobalConstants.NEW_PARAMETER_DEFAULT_VALUE;

        	Vector<String> parameterDefinition = 
        		addParameterDefinition(pObjectIDGenerator,
        							   parameterName, 
        							   parameterValue, 
        							   backupParameterName, 
        							   backupParameterValue);
			
        	currentRowValues.set(parameterNameColumnIndex, 
        						 parameterDefinition.get(parameterNameColumnIndex));
        	
        	currentRowValues.set(parameterValueColumnIndex, 
								 parameterDefinition.get(parameterValueColumnIndex));
			
        	parameterDefaultTableModel.addRow(currentRowValues);
        }
		
		return parameterDefaultTableModel;
	}

	/**
	 * @param newParameterName
	 * @param newParameterValue
	 */
	private Vector<String> addParameterDefinition(IDGenerator pObjectIDGenerator,
												  String newParameterName,
												  String newParameterValue,
												  String oldParameterName,
												  String oldParameterValue) {
		Vector<String> parameterDefinition = new Vector<String>();
		//TODO: do something more constructive like display a message to the user.
		//also we should not stop user from saving illegal parameter name/value
		//but when they truy to save it out. we shud bust them (per new gleamviz 
		//modelbuilder bui)
		
		/*
		 * Round 1: Try using user entered values
		 * */
		ParameterDefinitionAdditionMessageType parameterAdditionResult = 
			attemptParameterDefinitionAddition(newParameterName, 
											   newParameterValue);
		
		if (parameterAdditionResult != ParameterDefinitionAdditionMessageType.NO_ERRORS) {
		
			/*
			 * Round 2: Try using legal old value & legal new value
			 * */
			
			/*
			 * parameter name was illegal. so switch to old parameter name but keep the
			 * new parameter value.
			 * */
			if (parameterAdditionResult == 
					ParameterDefinitionAdditionMessageType.PARAMETER_NAME_ERROR) {
				newParameterName = oldParameterName;
			} else {
				/*
				 * parameter value was illegal. so switch to old parameter value but keep the
				 * new parameter name.
				 * */
				newParameterValue = oldParameterValue;
			}
			
			parameterAdditionResult = 
				attemptParameterDefinitionAddition(newParameterName, 
												   newParameterValue);
			
			if (parameterAdditionResult != ParameterDefinitionAdditionMessageType.NO_ERRORS) {
				
				/*
				 * Round 3: Try using default values 
				 * */
				
				/*
				 * parameter name was illegal. so switch to default parameter name but keep 
				 * the old parameter value.
				 * */
				if (parameterAdditionResult == 
						ParameterDefinitionAdditionMessageType.PARAMETER_NAME_ERROR) {
					newParameterName = pObjectIDGenerator.getParameterCounter();
				} else {
					/*
					 * parameter value was illegal. so switch to default parameter value but 
					 * keep the old parameter name.
					 * */
					newParameterValue = GlobalConstants.NEW_PARAMETER_DEFAULT_VALUE;
				}

				parameterAdditionResult = 
					attemptParameterDefinitionAddition(newParameterName, 
													   newParameterValue);
				
				if (parameterAdditionResult != ParameterDefinitionAdditionMessageType.NO_ERRORS) { 
					
					/*
					 * 
					 * If there is still an error than god help us!
					 * */
					
					
				} else {
					parameterDefinition.add(newParameterName);
					parameterDefinition.add(newParameterValue);
				}
				
				
			} else {
				parameterDefinition.add(newParameterName);
				parameterDefinition.add(newParameterValue);
			}
			
		} else {
			parameterDefinition.add(newParameterName);
			parameterDefinition.add(newParameterValue);
		}
		
		
		
		System.out.println(inMemoryModel.getParameterDefinitions());
		
		return parameterDefinition;
	}

	/**
	 * @param parameterName
	 * @param parameterValue
	 * @param isParameterAdditionSuccessful
	 * @return
	 */
	private ParameterDefinitionAdditionMessageType 
					attemptParameterDefinitionAddition(String parameterName,
													   String parameterValue) {
		ParameterDefinitionAdditionMessageType 
			additionMessage = ParameterDefinitionAdditionMessageType.NO_ERRORS;
		try {
			inMemoryModel.setParameterDefinition(parameterName, parameterValue);
		} catch (InvalidParameterExpressionException e) {
			System.out.println("invalid parameter value " + e.getMessage());
			notificationAreas[1].addNotification("\"" + parameterValue 
											 + "\" is an invalid parameter expression.");
			additionMessage = ParameterDefinitionAdditionMessageType.PARAMETER_VALUE_ERROR;
			
		} catch (InvalidParameterNameException e) {
			System.out.println("invalid parameter name " + e.getMessage());
			notificationAreas[1].addNotification("\"" + parameterName 
											 + "\" is an invalid parameter name.");
			additionMessage = ParameterDefinitionAdditionMessageType.PARAMETER_NAME_ERROR;
		} catch (ParameterAlreadyDefinedException e) {
			System.out.println("duplicate parameter name " + e.getMessage() 
								+ " new parameter name geenrated");
			notificationAreas[1].addNotification("\"" + parameterName 
											 + "\" is already defined.");
			additionMessage = ParameterDefinitionAdditionMessageType.PARAMETER_NAME_ERROR;
		}
		
		if (additionMessage == ParameterDefinitionAdditionMessageType.NO_ERRORS) {
		System.out.println("remove notification >");
			try {
				notificationAreas[0].resetNotifications();
				System.out.println(inMemoryModel.listUnboundReferencedParameters());
				for (String parameter : inMemoryModel.listUnboundReferencedParameters()) {
					notificationAreas[0].addUniqueNotification(parameter);
				}	
			} catch (InvalidParameterExpressionException e) {
				notificationAreas[1].addNotification("Errors in testing of defined parameters.");
			}
		
		}
		return additionMessage;
	}
	
	public static void setDeleteButtonMousePressedImageFile(
			URL deleteButtonMousePressedImagePath) {
		deleteButtonMousePressedImage = deleteButtonMousePressedImagePath; 
		
	}

	public static void setDeleteButtonNormalStateImageFile(
			URL deleteButtonNormalStateImagePath) {
		deleteButtonNormalStateImage = deleteButtonNormalStateImagePath;
	}

	/**
	 * @return the parameterTableWorkbench
	 */
	public PNode getParameterTableWorkbench() {
		return parameterTableWorkbench;
	}
	
	
	

}
