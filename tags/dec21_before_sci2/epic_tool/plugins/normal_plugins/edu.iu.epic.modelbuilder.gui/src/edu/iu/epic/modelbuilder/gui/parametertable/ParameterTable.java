package edu.iu.epic.modelbuilder.gui.parametertable;

import java.awt.Color;
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

import edu.iu.epic.modelbuilder.gui.utility.GlobalConstants;
import edu.iu.epic.modelbuilder.gui.utility.IDGenerator;
import edu.iu.epic.modelbuilder.gui.utility.NotificationArea;
import edu.iu.epic.modelbuilder.gui.utility.PiccoloUtilities;
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
	
	// TODO enum?
	protected static final int PARAMETER_NAME_COLUMN_INDEX = 0;
	protected static final int PARAMETER_VALUE_COLUMN_INDEX = 1;
	protected static final int DELETE_BUTTON_COLUMN_INDEX = 2; 
	
	private static enum ParameterDefinitionAdditionMessageType {
		NO_ERRORS, PARAMETER_NAME_ERROR, PARAMETER_VALUE_ERROR;
	}

	@SuppressWarnings("serial")
	public ParameterTable(boolean createOnlyGUI, 
						  final IDGenerator pObjectIDGenerator,
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
        		: this.inMemoryModel.getParameterDefinitions().entrySet()) {
        	
        	row = new Vector<String>();
        	row.add(parameterDefinition.getKey());
        	row.add(parameterDefinition.getValue());
            row.add(deleteButtonText);
            
            System.out.println(parameterDefinition.getKey() + " - " + parameterDefinition.getValue() + " -- " + row );
            
            rows.add(row);
        }

        final DefaultTableModel parameterDefaultTableModel = 
        		generateParameterDefaultTableModel(createOnlyGUI, pObjectIDGenerator, columns, rows); 
        	
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
                case DELETE_BUTTON_COLUMN_INDEX:
                	break;
                
                /*
                 * Change in the cell content can belong to either parameterName or 
                 * parameterValue. 
                 * */	
                case PARAMETER_NAME_COLUMN_INDEX:
                    /*
                     * Only proceed with renaming of the parameter definition if there is 
                     * actually any change in the name of the parameter.
                     * */
                    String oldParameterName = (String) tableCellListener.getOldValue();
					String newParameterName = (String) tableCellListener.getNewValue();
					String parameterValue = (String) parameterDefaultTableModel
												.getValueAt(tableCellListener.getRow(), 
														    PARAMETER_VALUE_COLUMN_INDEX);
					if (!oldParameterName.equalsIgnoreCase(newParameterName)) {
						Vector<String> approvedParameterDefinition = 
								renameParameterDefinition(pObjectIDGenerator,
														  newParameterName, 
														  parameterValue,
														  oldParameterName,
														  parameterValue);
						
						parameterDefaultTableModel.setValueAt(
									approvedParameterDefinition.get(PARAMETER_NAME_COLUMN_INDEX),
									tableCellListener.getRow(), 
								    PARAMETER_NAME_COLUMN_INDEX);
						
						parameterDefaultTableModel.setValueAt(
								approvedParameterDefinition.get(PARAMETER_VALUE_COLUMN_INDEX),
								tableCellListener.getRow(), 
							    PARAMETER_VALUE_COLUMN_INDEX);
                    }
					break;
                	
                case PARAMETER_VALUE_COLUMN_INDEX:
                    /*
                     * Only proceed with renaming of the parameter definition if there is 
                     * actually any change in the value of the parameter.
                     * */
                    String oldParameterValue = (String) tableCellListener.getOldValue();
					String newParameterValue = (String) tableCellListener.getNewValue();
					String parameterName = (String) parameterDefaultTableModel
												.getValueAt(tableCellListener.getRow(), 
														    PARAMETER_NAME_COLUMN_INDEX);
					if (!oldParameterValue.equalsIgnoreCase(newParameterValue)) {
						Vector<String> approvedParameterDefinition = 
								renameParameterDefinition(pObjectIDGenerator,
														  parameterName,
														  newParameterValue,
														  parameterName,
														  oldParameterValue);
						
						parameterDefaultTableModel.setValueAt(
								approvedParameterDefinition.get(PARAMETER_NAME_COLUMN_INDEX),
								tableCellListener.getRow(), 
							    PARAMETER_NAME_COLUMN_INDEX);
					
						parameterDefaultTableModel.setValueAt(
								approvedParameterDefinition.get(PARAMETER_VALUE_COLUMN_INDEX),
								tableCellListener.getRow(), 
							    PARAMETER_VALUE_COLUMN_INDEX);
						
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
        
        
        PNode legendTableWrapper = new PSwing(parameterTablePanel); 
        
        JLabel legendLabel = new JLabel("Legend");
        
        parameterTablePanel.add(legendLabel);
        
        String[] columnNames = {"Color", "Conveys"};

		Object[][] data = {
				{GlobalConstants.SIMPLE_TRANSITION_HANDLE_COLOR,
					"Click & drag to create a simple transition"},
				{GlobalConstants.COMPLEX_TRANSITION_HANDLE_COLOR,
					"Click & drag to create a complex transition"},
				{GlobalConstants.PRIMARY_COMPARTMENT_IDENTIFYING_COLOR,
					"Indicates a non-secondary compartment"},
				{GlobalConstants.SECONDARY_COMPARTMENT_IDENTIFYING_COLOR,
					"Indicates a secondary compartment"}
		};
		
		final JTable legendTable = new JTable(new DefaultTableModel(data, columnNames) {
			
	        /*
	         * JTable uses this method to determine the default renderer/
	         * editor for each cell.  If we didn't implement this method,
	         * then the last column would contain text ("true"/"false"),
	         * rather than a check box.
	         */
	        public Class getColumnClass(int c) {
	            return getValueAt(0, c).getClass();
	        }

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}	
			
		});
		
		legendTable.setPreferredScrollableViewportSize(new Dimension(285, 70));
//		legendTable.setFillsViewportHeight(true); New in Java 6

		legendTable.getColumnModel().getColumn(0).setMaxWidth(30);
		legendTable.setDefaultRenderer(Color.class, new ColorRenderer(true));
		
		/* TODO Fix?
		 * Tooltips currently hover at an incorrect position (way too far to the left).
		 * We'll settle for descriptive label text for now.
		 */
//		legendTable.addMouseMotionListener(new MouseMotionAdapter() {
//			public void mouseMoved(MouseEvent e) { 
//				Point p = e.getPoint(); 
//				int row = legendTable.rowAtPoint(p);
//				
//				legendTable.setToolTipText(
//						getCustomTooltipText((Color) legendTable.getValueAt(row, 0))); 
//			} 
//			
//		    private String getCustomTooltipText(Color newColor) {		    	
//		    	if (GlobalConstants.PRIMARY_COMPARTMENT_IDENTIFYING_COLOR == newColor) {
//		    		return "This color indicates the compartment is not Secondary."; 
//		    	} else if (GlobalConstants.SECONDARY_COMPARTMENT_IDENTIFYING_COLOR == newColor) {
//		    		return "This color indicates the compartment is Secondary.";
//		    	} else if (GlobalConstants.SIMPLE_TRANSITION_HANDLE_COLOR == newColor) {
//		    		return "Click & Drag to create a simple transition between the source & "
//		    					+ "destination compartments.";
//		    	} else if (GlobalConstants.COMPLEX_TRANSITION_HANDLE_COLOR == newColor) {
//		    		return "Click & Drag to create a complex transition between the source & "
//		    					+ "destination compartments involving an infector.";
//		    	} else {
//		    		return "Color not identified as part of the legend.";
//		    	}		    	
//		    }			
//		});		
		
        parameterTablePanel.add(legendTable);
        
        parameterTableWorkbench.addChild(legendTableWrapper);
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
				
	        	newParameterRow.add(parameterDefinition.get(PARAMETER_NAME_COLUMN_INDEX));
	        	newParameterRow.add(parameterDefinition.get(PARAMETER_VALUE_COLUMN_INDEX));
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
        														PARAMETER_NAME_COLUMN_INDEX));
        tableColumn.setPreferredWidth(150);
        
        tableColumn = parameterTable
        					.getColumn(parameterTable
        									.getColumnName(
        											PARAMETER_VALUE_COLUMN_INDEX));
        tableColumn.setPreferredWidth(100);
        
        tableColumn = parameterTable
        					.getColumn(parameterTable
        									.getColumnName(
        											DELETE_BUTTON_COLUMN_INDEX));
        tableColumn.setPreferredWidth(30);
        parameterTable.setDoubleBuffered(false);
		return parameterTable;
	}

	private DefaultTableModel generateParameterDefaultTableModel(
									boolean createOnlyGUI, 
									IDGenerator pObjectIDGenerator,
									Vector<String> columns, 
									Vector<Vector<String>> rows) {
  
		DefaultTableModel parameterDefaultTableModel = new DefaultTableModel();

        parameterDefaultTableModel.addColumn(columns.get(PARAMETER_NAME_COLUMN_INDEX));
		parameterDefaultTableModel.addColumn(columns.get(PARAMETER_VALUE_COLUMN_INDEX));
		parameterDefaultTableModel.addColumn(columns.get(DELETE_BUTTON_COLUMN_INDEX));
        
        
        for (Vector<String> currentRowValues : rows) {	
        	
        	String parameterName = currentRowValues.get(PARAMETER_NAME_COLUMN_INDEX);
        	String parameterValue = currentRowValues.get(PARAMETER_VALUE_COLUMN_INDEX);
        	
	        String backupParameterName = pObjectIDGenerator.getParameterCounter();
	        
	        String backupParameterValue = GlobalConstants.NEW_PARAMETER_DEFAULT_VALUE;

	        if (!createOnlyGUI) {
	        	Vector<String> parameterDefinition = 
	        		addParameterDefinition(pObjectIDGenerator,
	        							   parameterName, 
	        							   parameterValue, 
	        							   backupParameterName, 
	        							   backupParameterValue);
	        	
	        	parameterName = parameterDefinition.get(PARAMETER_NAME_COLUMN_INDEX);
	        	parameterValue = parameterDefinition.get(PARAMETER_VALUE_COLUMN_INDEX);
	        }
        	
        	
        	currentRowValues.set(PARAMETER_NAME_COLUMN_INDEX, 
        						 parameterName);
        	
        	currentRowValues.set(PARAMETER_VALUE_COLUMN_INDEX, 
        					     parameterValue);
			
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
		
		System.out.println(newParameterName  + " x "  + newParameterValue + " xx " + oldParameterName + " xxx " + oldParameterValue);
		
		/*
		 * Round 1: Try using user entered values
		 * */
		ParameterDefinitionAdditionMessageType parameterAdditionResult = 
			attemptParameterDefinitionAddition(newParameterName, 
											   newParameterValue);
		
		System.out.println("AFTER round 1 attemptParameterDefinitionAddition");
		
		if (parameterAdditionResult != ParameterDefinitionAdditionMessageType.NO_ERRORS) {
		
			/*
			 * Round 2: Try using legal old value & legal new value
			 * */
			
			/*
			 * parameter name was illegal. so switch to old parameter name but keep the
			 * new parameter value.
			 * */
			if (parameterAdditionResult 
					== ParameterDefinitionAdditionMessageType.PARAMETER_NAME_ERROR) {
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
			
			System.out.println("AFTER round 2 attemptParameterDefinitionAddition");
			
			if (parameterAdditionResult != ParameterDefinitionAdditionMessageType.NO_ERRORS) {
				
				/*
				 * Round 3: Try using default values 
				 * */
				
				/*
				 * parameter name was illegal. so switch to default parameter name but keep 
				 * the old parameter value.
				 * */
				if (parameterAdditionResult 
						== ParameterDefinitionAdditionMessageType.PARAMETER_NAME_ERROR) {
					
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
				
				System.out.println("AFTER round 3 attemptParameterDefinitionAddition");
				
				if (parameterAdditionResult != ParameterDefinitionAdditionMessageType.NO_ERRORS) { 
					System.out.println("inside no erro should happen " + parameterAdditionResult);
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
		
		System.out.println("param defn " + parameterDefinition);
		
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
			
			notificationAreas[1].addNotification("\"" + parameterValue 
											 + "\" is an invalid parameter expression.");
			
			additionMessage = ParameterDefinitionAdditionMessageType.PARAMETER_VALUE_ERROR;
			
		} catch (InvalidParameterNameException e) {
			
			notificationAreas[1].addNotification("\"" + parameterName 
											 + "\" is an invalid parameter name.");
			additionMessage = ParameterDefinitionAdditionMessageType.PARAMETER_NAME_ERROR;
		} catch (ParameterAlreadyDefinedException e) {
			
			notificationAreas[1].addNotification("\"" + parameterName 
											 + "\" is already defined.");
			additionMessage = ParameterDefinitionAdditionMessageType.PARAMETER_NAME_ERROR;
		}
		
		if (additionMessage == ParameterDefinitionAdditionMessageType.NO_ERRORS) {
			try {
				notificationAreas[0].addAllNotifications(
						inMemoryModel.listUnboundReferencedParameters());
			} catch (InvalidParameterExpressionException exception) {
				notificationAreas[1].addNotification("Errors in testing of undefined parameters.");
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
