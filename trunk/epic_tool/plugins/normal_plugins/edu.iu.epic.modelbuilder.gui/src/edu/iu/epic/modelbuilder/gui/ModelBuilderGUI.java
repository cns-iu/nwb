package edu.iu.epic.modelbuilder.gui;

import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiKeyMap;

import edu.iu.epic.modelbuilder.gui.compartment.CompartmentMoveEventHandler;
import edu.iu.epic.modelbuilder.gui.compartment.PCompartment;
import edu.iu.epic.modelbuilder.gui.parametertable.ParameterTable;
import edu.iu.epic.modelbuilder.gui.transition.ComplexTransition;
import edu.iu.epic.modelbuilder.gui.transition.ComplexTransitionInfectionInformation;
import edu.iu.epic.modelbuilder.gui.transition.SimpleTransition;
import edu.iu.epic.modelbuilder.gui.utility.CompartmentIDToLabelMap;
import edu.iu.epic.modelbuilder.gui.utility.GlobalConstants;
import edu.iu.epic.modelbuilder.gui.utility.IDGenerator;
import edu.iu.epic.modelbuilder.gui.utility.NotificationArea;
import edu.iu.epic.modelbuilder.gui.utility.ObjectSelectionEventHandler;
import edu.iu.epic.modelbuilder.gui.utility.PiccoloUtilities;
import edu.iu.epic.modelbuilder.gui.utility.WorkBenchInternalFrame;
import edu.iu.epic.modeling.compartment.model.Compartment;
import edu.iu.epic.modeling.compartment.model.InfectionTransition;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.RatioTransition;
import edu.iu.epic.modeling.compartment.model.Transition;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.PRoot;
import edu.umd.cs.piccolox.pswing.PSwing;
import edu.umd.cs.piccolox.pswing.PSwingCanvas;
import edu.umd.cs.piccolox.swing.PScrollPane;

public class ModelBuilderGUI implements ActionListener {

	
	

	private static final double ADD_NEW_COMPARTMENT_Y_INCREMENT = 10.0;
	private static final double ADD_NEW_COMPARTMENT_X_INCREMENT = 5.0;

	private static final long serialVersionUID = 8679069222688097887L;
	
	private JDesktopPane desktopPane;
	private JFrame parentJFrame;
	private WorkBenchInternalFrame workbench;
	
	private boolean isModelBuildingComplete;

    private PLayer canvasLayer, compartmentLayer, transitionLayer, 
    					  helperComponentsLayer, temporaryComponentsLayer;
    
    private PSwingCanvas mainWorkbenchCanvas;
    
    private Model inMemoryModel;
    private IDGenerator pObjectIDGenerator;

	private NotificationArea[] notificationAreas;

	private Point2D.Double newCompartmentSeriesPosition;
	private Point2D.Double addNewCompartmentPosition;
	private int addNewCompartmentCount;
	
	public ModelBuilderGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		
        parentJFrame = new JFrame("Model Builder");

        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int modelBuilderScreenOffset = 50;
		int modelBuilderWidth = (int) screenSize.getWidth() - modelBuilderScreenOffset;
		int modelBuilderHeight = (int) screenSize.getHeight() - modelBuilderScreenOffset * 2;
		
		parentJFrame.setSize(modelBuilderWidth, 
        					 modelBuilderHeight);
        
        parentJFrame.setPreferredSize(new Dimension(modelBuilderWidth, 
        											modelBuilderHeight));
        
        parentJFrame.setLocation(0, modelBuilderScreenOffset);
        
        
    }

	public void start(Model inputInMemoryModel) {

        //Set up the GUI.
        desktopPane = new JDesktopPane(); //a specialized layered pane
        createModelBuilderWorkbench(); //create first "window"
        
       
        
        parentJFrame.setContentPane(desktopPane);
        parentJFrame.setJMenuBar(createMenuBar());

        //Make dragging a little faster but perhaps uglier.
        desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        
		if (inputInMemoryModel == null) {
			inputInMemoryModel = new Model();
		}
		
		initialize(inputInMemoryModel);
        
        parentJFrame.addWindowListener(new WindowListener() {

			public void windowActivated(WindowEvent e) { }
	
			public void windowClosed(WindowEvent e) { 
				
			}
	
			public void windowClosing(WindowEvent e) { 
				quitModelBuilderWithoutSaving();
			}
	
			public void windowDeactivated(WindowEvent e) { }
	
			public void windowDeiconified(WindowEvent e) { }
	
			public void windowIconified(WindowEvent e) { }
	
			public void windowOpened(WindowEvent e) { }
	    	  
      });
        
      //Display the window.
      parentJFrame.pack();
      parentJFrame.setVisible(true);
      
      try {
    	  workbench.setSelected(true);
	  } catch (PropertyVetoException e1) {
		  System.out.println("workbench focus set issues");
	  }
      
	}

    private void initialize(Model inputInMemoryModel) {
        
        mainWorkbenchCanvas = new PSwingCanvas();
        
        PRoot root = mainWorkbenchCanvas.getRoot();
        
        canvasLayer = mainWorkbenchCanvas.getLayer();
        
        compartmentLayer = new PLayer();
		transitionLayer = new PLayer();
		helperComponentsLayer = new PLayer();
		temporaryComponentsLayer = new PLayer();
		
		root.addChild(temporaryComponentsLayer);
		root.addChild(transitionLayer);
		root.addChild(helperComponentsLayer);
		root.addChild(compartmentLayer);
		
		compartmentLayer.addAttribute(GlobalConstants.LAYER_ID_ATTRIBUTE_NAME, 
									  GlobalConstants.COMPARTMENT_LAYER_ID_ATTRIBUTE_VALUE);
		
		transitionLayer.addAttribute(GlobalConstants.LAYER_ID_ATTRIBUTE_NAME, 
									 GlobalConstants.TRANSITION_LAYER_ID_ATTRIBUTE_VALUE);
		
		helperComponentsLayer
				.addAttribute(GlobalConstants.LAYER_ID_ATTRIBUTE_NAME, 
							  GlobalConstants.HELPER_COMPONENTS_LAYER_ID_ATTRIBUTE_VALUE);
		
		temporaryComponentsLayer
				.addAttribute(GlobalConstants.LAYER_ID_ATTRIBUTE_NAME, 
							  GlobalConstants.TEMPORARY_COMPONENTS_LAYER_ID_ATTRIBUTE_VALUE);
		
		
		mainWorkbenchCanvas.getCamera().addLayer(0, temporaryComponentsLayer);
		mainWorkbenchCanvas.getCamera().addLayer(1, transitionLayer);
		mainWorkbenchCanvas.getCamera().addLayer(2, helperComponentsLayer);
		mainWorkbenchCanvas.getCamera().addLayer(3, compartmentLayer);
		
		PScrollPane mainWorkbenchCanvasScrollPane = new PScrollPane(mainWorkbenchCanvas);
		workbench.add(mainWorkbenchCanvasScrollPane);
		
		/*
		 * For initializing parameters that help build the model. 
		 * It side-effects,
		 * 		1. isModelBuildingComplete
		 * 		2. inMemoryModel
		 * 		3. pObjectIDGenerator
		 * 		4. CompartmentIDToLabelMap
		 * 		5. NotificationAreas
		 * */
		initializeModelHelpers();

        JButton addNewCompartmentButton = new JButton("Add New Compartment");
        PSwing buttonWrapper = new PSwing(addNewCompartmentButton);
        buttonWrapper.translate(10, 10);
        addNewCompartmentButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent actionEvent) {
				addNewCompartmentCount++;
		    	Shape compartmentShape = new Rectangle2D.Double(
		    			getNewCompartmentPosition().getX(), 
		    			getNewCompartmentPosition().getY(),
		    			GlobalConstants.COMPARTMENT_DIMENSIONS.getWidth(), 
		    			GlobalConstants.COMPARTMENT_DIMENSIONS.getHeight());
		    	
				PNode compartment = 
					new PCompartment(
							compartmentShape, 
							pObjectIDGenerator.getCompartmentCounter(), 
							inMemoryModel,
							notificationAreas[1]);
	        	compartmentLayer.addChild(compartment);
			}
        	
        });
        
        helperComponentsLayer.addChild(buttonWrapper);
        
		/*
         * Used to import parameter definitions, compartments & transitions from the input 
         * in-memory model into the Model Builder & add them into current in-memory model 
         * of the model builder.
         * It side-effects,
         * 		1. In-Memory model
         * 		2. Piccolo based GUI
         * */
        importModelElementsFromInputInMemoryModel(inputInMemoryModel);
		
		
		/*
		 * Used to manage input event listeners for the workbench. It takes care of adding 
		 * & removing appropriate event listeners to & from the workbench.
		 * */
		manageWorkbenchInputEventListeners();
		
        workbench.validate();
        desktopPane.validate();
        
    }

	/**
	 * 
	 */
	private void initializeModelHelpers() {
		isModelBuildingComplete = false;
		inMemoryModel = new Model();
		pObjectIDGenerator = new IDGenerator();
		CompartmentIDToLabelMap.resetCompartmentIDToLabelMap();
		
		notificationAreas = new NotificationArea[2];
		
		/*
		 * For reporting all the unbound parameters.
		 * */
		notificationAreas[0] = new NotificationArea(2, ", ");
		
		/*
		 * For reporting all the inconsistencies.
		 * */
		notificationAreas[1] = new NotificationArea(10);
		
		newCompartmentSeriesPosition = new Point2D.Double(5.0, 30.0);
		addNewCompartmentPosition = 
			new Point2D.Double(newCompartmentSeriesPosition.getX(),
							   newCompartmentSeriesPosition.getY());
		addNewCompartmentCount = 0;
	}

	/**
	 * @param inputInMemoryModel
	 */
	private void importModelElementsFromInputInMemoryModel(
			Model inputInMemoryModel) {
		/*
         * Used to import parameter definitions from the input in-memory model 
         * into the Model Builder & add them into current in-memory model of the 
         * model builder.
         * It side-effects,
         * 		1. In-Memory model
         * 		2. Piccolo based GUI
         * */
        importParameterDefinitionsFromInputInMemoryModel(inputInMemoryModel);

        /*
         * Used to import compartments from the input in-memory model into the Model 
         * Builder & add them into current in-memory model of the model builder.
         * It side-effects,
         * 		1. In-Memory model
         * 		2. Piccolo based GUI
         * */
		importCompartmentsFromInputInMemoryModel(inputInMemoryModel);
		
		
        /*
         * Used to import simple & complex transitions from the input in-memory model 
         * into the Model Builder & add them into current in-memory model of the model builder.
         * "Simple Transition" is represented as "Ratio Transition" & "Complex Transition" is
         * represented as "Infection Transition" in the in-memory model. 
         * It side-effects,
         * 		1. In-Memory model
         * 		2. Piccolo based GUI
         * */
		importTransitionsFromInputInMemoryModel(inputInMemoryModel);
	}

	/**
	 * @param inputInMemoryModel
	 */
	private void importParameterDefinitionsFromInputInMemoryModel(
			Model inputInMemoryModel) {
		PNode parameterTable;
		ParameterTable parameterTableWorkbench = new ParameterTable(pObjectIDGenerator,
        															inputInMemoryModel,
        															inMemoryModel,
        															notificationAreas,
        															helperComponentsLayer);
        
        parameterTable = parameterTableWorkbench.getParameterTableWorkbench();
        
        parameterTable.addAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME,
        							GlobalConstants.PARAMETER_TABLE_TYPE_ATTRIBUTE_VALUE);
        
        
        
        helperComponentsLayer.addChild(parameterTable);
	}

	/**
	 * 
	 */
	private void manageWorkbenchInputEventListeners() {
		// Create a selection event handler
        ObjectSelectionEventHandler selectionEventHandler = 
        		createSelectionEventHandlerForGUIObjects();
		
		mainWorkbenchCanvas.getRoot()
				.getDefaultInputManager()
						.setKeyboardFocus(selectionEventHandler);

		
		compartmentLayer.addInputEventListener(
				new CompartmentMoveEventHandler(compartmentLayer, 
												temporaryComponentsLayer, 
												transitionLayer,
												pObjectIDGenerator,
												inMemoryModel,
												notificationAreas,
												mainWorkbenchCanvas));

        mainWorkbenchCanvas.removeInputEventListener(mainWorkbenchCanvas.getPanEventHandler());
        mainWorkbenchCanvas.removeInputEventListener(mainWorkbenchCanvas.getZoomEventHandler());
        
        
        workbench.addComponentListener(new ComponentListener() {

			public void componentHidden(ComponentEvent e) { }

			public void componentMoved(ComponentEvent e) { }

			public void componentResized(ComponentEvent e) { 
				Dimension currentJInternalFrameSize = ((JInternalFrame) e.getSource()).getSize();
				ParameterTable.refreshParameterWorkbenchLocation(currentJInternalFrameSize, 
																 helperComponentsLayer);	
			}
			public void componentShown(ComponentEvent e) { 
				System.out.println(" @#$%%^&*( " + ((JInternalFrame) e.getSource()).getSize());
			}
        });
	}

	/**
	 * @return
	 */
	private ObjectSelectionEventHandler createSelectionEventHandlerForGUIObjects() {
		List<PLayer> selectionEventHandledLayers = new ArrayList<PLayer>();
        selectionEventHandledLayers.add(compartmentLayer);
        selectionEventHandledLayers.add(transitionLayer);
        
        ObjectSelectionEventHandler selectionEventHandler = 
        	new ObjectSelectionEventHandler(canvasLayer, 
        									selectionEventHandledLayers);
		
		mainWorkbenchCanvas.addInputEventListener(selectionEventHandler);
		return selectionEventHandler;
	}

	/**
	 * @param inputInMemoryModel
	 */
	private void importTransitionsFromInputInMemoryModel(
			Model inputInMemoryModel) {
		MultiKeyMap complexTransitionParentsToInfectionInformation = new MultiKeyMap();
		
		for (Transition inMemoryTransition : inputInMemoryModel.getTransitions()) { 
			
			String sourceCompartmentName = inMemoryTransition
												.getSource().getName();

			String targetCompartmentName = inMemoryTransition
												.getTarget().getName();
			
			String transitionRatio = inMemoryTransition.getRatio();
			
			if (inMemoryTransition instanceof RatioTransition) {
				

				
				/*
				 * We are comparing here against the id of the compartment bcoz it is
				 * guaranteed that just after creation of the piccolo compartments they 
				 * will have the same id & label.
				 * TODO: later i shouold refactor it so that i can access the label of 
				 * the compartment duirectly instead of getting it through the editablLabel.
				 * */
				PNode sourceCompartment = 
					PiccoloUtilities
						.getChildComponentBasedOnGivenAttribute(
								compartmentLayer, 
								GlobalConstants.NODE_ID_ATTRIBUTE_NAME, 
								sourceCompartmentName);
				
				PNode targetCompartment = 
					PiccoloUtilities
						.getChildComponentBasedOnGivenAttribute(
								compartmentLayer, 
								GlobalConstants.NODE_ID_ATTRIBUTE_NAME, 
								targetCompartmentName);
				
				if (!PiccoloUtilities.isTransitionDuplicate(
						sourceCompartment, 
						targetCompartment,
		        		transitionLayer)) {
					
				
	        	PNode simpleTransition = 
	        		new SimpleTransition(sourceCompartment, 
	        							 targetCompartment,
	        							 pObjectIDGenerator,
	        							 transitionRatio,
	        							 inMemoryModel,
	        							 notificationAreas);
	        
	        	transitionLayer.addChild(simpleTransition);
				} else {
					System.out.println("it was a duplicate");
				}
				
			} else if (inMemoryTransition instanceof InfectionTransition) {
				
				String infectorCompartmentName = ((InfectionTransition) inMemoryTransition)
														.getInfector().getName();
				
				List<ComplexTransitionInfectionInformation> infectionsInformation = 
						(List<ComplexTransitionInfectionInformation>) 
								complexTransitionParentsToInfectionInformation	
										.get(sourceCompartmentName, targetCompartmentName);
				
				if (infectionsInformation == null) {
					infectionsInformation = new ArrayList<ComplexTransitionInfectionInformation>();
					
					infectionsInformation.add(
							new ComplexTransitionInfectionInformation(
										infectorCompartmentName,
										transitionRatio));
					
					complexTransitionParentsToInfectionInformation.put(
							sourceCompartmentName,
							targetCompartmentName,
							infectionsInformation
							);
					
				} else {
					
					infectionsInformation.add(
							new ComplexTransitionInfectionInformation(
										infectorCompartmentName,
										transitionRatio));
				
				}
			}
		}
		
		MapIterator inMemoryComplexTransitionIterator = 
				complexTransitionParentsToInfectionInformation.mapIterator(); 
		
		while (inMemoryComplexTransitionIterator.hasNext()) {
			inMemoryComplexTransitionIterator.next();
			
			MultiKey infectionParents = (MultiKey) inMemoryComplexTransitionIterator.getKey();
			
			/*
			 * We are comparing here against the id of the compartment bcoz it is
			 * guaranteed that just after creation of the piccolo compartments they 
			 * will have the same id & label.
			 * TODO: later i shouold refactor it so that i can access the label of 
			 * the compartment duirectly instead of getting it through the editablLabel.
			 * */
			PNode sourceCompartment = 
				PiccoloUtilities
					.getChildComponentBasedOnGivenAttribute(
							compartmentLayer, 
							GlobalConstants.NODE_ID_ATTRIBUTE_NAME, 
							infectionParents.getKey(0).toString());
			
			PNode targetCompartment = 
				PiccoloUtilities
					.getChildComponentBasedOnGivenAttribute(
							compartmentLayer, 
							GlobalConstants.NODE_ID_ATTRIBUTE_NAME, 
							infectionParents.getKey(1).toString());
			
			if (!PiccoloUtilities.isTransitionDuplicate(
					sourceCompartment, 
					targetCompartment,
	        		transitionLayer)) { 
				
	        	PNode complexTransition = 
	            	new ComplexTransition(sourceCompartment, 
	            						  targetCompartment,
	            						  (List<ComplexTransitionInfectionInformation>) 
	      								  inMemoryComplexTransitionIterator.getValue(),
				  						  pObjectIDGenerator,
										  inMemoryModel,
										  notificationAreas,
										  mainWorkbenchCanvas);
	        
	        	transitionLayer.addChild(complexTransition);
	        	
			} else {
				System.out.println("comlpex it was a duplicate");
			}
			
		}
	}

	/**
	 * @param inputInMemoryModel
	 */
	private void importCompartmentsFromInputInMemoryModel(
			Model inputInMemoryModel) {
		for (Compartment inMemoryCompartment : inputInMemoryModel.getCompartments()) {
			Shape compartmentShape = new Rectangle2D.Double(
					inMemoryCompartment.getPosition().getX(), 
					inMemoryCompartment.getPosition().getY(),
        			GlobalConstants.COMPARTMENT_DIMENSIONS.getWidth(), 
        			GlobalConstants.COMPARTMENT_DIMENSIONS.getHeight());
        	
        	
        	PNode compartment = 
        		new PCompartment(
        				compartmentShape,
        				inMemoryCompartment.getName(),
						inMemoryModel,
						notificationAreas[1]);
        	compartmentLayer.addChild(compartment);
		}
	}

    

	private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //Set up the lone menu.
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);

        //Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("New Model");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("new");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menu.addSeparator();
        
        menuItem = new JMenuItem("Quit without Saving");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("quit_no_save");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menu.addSeparator();
        
        //Set up the second menu item.
        menuItem = new JMenuItem("Save & Quit");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("save_quit");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }
	
  //React to menu selections.
    public void actionPerformed(ActionEvent e) {
        if ("new".equals(e.getActionCommand())) { 
        	newModelBuilder();
        } else if ("save_quit".equals(e.getActionCommand())) { 
        	quitModelBuilderAfterSaving();
        } else if ("quit_no_save".equals(e.getActionCommand())) { 
            quitModelBuilderWithoutSaving();
        }
    }

    private void newModelBuilder() {
    	compartmentLayer.removeAllChildren();
    	transitionLayer.removeAllChildren();
    	
    	/*
    	 * To delete only the parameter table we get that element and remove it from 
    	 * the helperComponenetsLayer.
    	 * */
    	PNode parameterTable = 
    		PiccoloUtilities.getChildComponentBasedOnGivenAttribute(
    					helperComponentsLayer, 
    					GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
    					GlobalConstants.PARAMETER_TABLE_TYPE_ATTRIBUTE_VALUE);
    	
    	helperComponentsLayer.removeChild(parameterTable);
    	
    	initializeModelHelpers();
    	
    	importModelElementsFromInputInMemoryModel(new Model());
    	
		ParameterTable.refreshParameterWorkbenchLocation(workbench.getSize(), 
				 helperComponentsLayer);
    	
	}

	private void quitModelBuilderAfterSaving() {
    	isModelBuildingComplete = true;
    	
        parentJFrame.dispose();
        synchronized (this) {
        this.notify();
        }
	}
    
    //Quit the application.
    private void quitModelBuilderWithoutSaving() {
    	
    	String[] question = {
    			"All the changes you made to the model will not be saved. ",
    			"Do you want to still quit without saving? If no then select ", 
				"\"Cancel\" button else select \"Quit without Saving\" " 
				+ "button."
    	};
    	
    	String[] options = {"Quit without Saving", "Cancel"};
        int quitOptionSelected = JOptionPane.showOptionDialog(workbench,
                        question,
                        "Quit without Saving",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);

        if (quitOptionSelected == JOptionPane.YES_OPTION) {
        	System.out.println("selected quit without saving");
        	inMemoryModel = null;
        	isModelBuildingComplete = true;
            parentJFrame.dispose();
            synchronized (this) {
            this.notify();
            }
        } 
        
        /*
         * We should not do anything i.e. continue with the model builder in case of user 
         * selecting "Cancel" or if user just closes the dialog box.
         * */

    }

	//Create a new internal frame.
    protected void createModelBuilderWorkbench() {
        workbench = new WorkBenchInternalFrame();
        workbench.setVisible(true); 
        
        desktopPane.add(workbench);
        
        try {
        	workbench.setResizable(false);
        	workbench.setMaximizable(false);
        	workbench.setClosable(false);
        	workbench.setMaximum(true);
            workbench.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
    }


	/**
	 * @return the isModelBuildingComplete
	 */
	public boolean isModelBuildingComplete() {
		return isModelBuildingComplete;
	}

	/**
	 * @return the inMemoryModel
	 */
	public Model getInMemoryModel() {
		return inMemoryModel;
	}

	public PSwingCanvas getMainWorkbenchCanvas() {
		return mainWorkbenchCanvas;
	}

	/**
	 * @return the addNewCompartmentPosition
	 */
	public Point2D.Double getNewCompartmentPosition() {
		
		/*
		 * Reset the position of the new series of compartments when it reaches a certain
		 * threshold, in this case it is 21. 
		 * */
		if (addNewCompartmentCount <= 21) {
			addNewCompartmentPosition.x += ADD_NEW_COMPARTMENT_X_INCREMENT;
			addNewCompartmentPosition.y += ADD_NEW_COMPARTMENT_Y_INCREMENT;
		} else {
			addNewCompartmentCount = 0;
			newCompartmentSeriesPosition.x += 21.0 + GlobalConstants.COMPARTMENT_DIMENSIONS.getWidth(); 
			addNewCompartmentPosition.x = newCompartmentSeriesPosition.x;
			addNewCompartmentPosition.y = newCompartmentSeriesPosition.y 
										  + ADD_NEW_COMPARTMENT_Y_INCREMENT;
		}
		return addNewCompartmentPosition;
	}

}
