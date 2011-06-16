package edu.iu.epic.visualization.linegraph.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.cishell.utilities.color.ColorRegistry;

import edu.iu.epic.visualization.linegraph.controller.StencilRun.RUN_TYPE;
import edu.iu.epic.visualization.linegraph.model.StencilData;
import edu.iu.epic.visualization.linegraph.model.TimestepBounds;
import edu.iu.epic.visualization.linegraph.utilities.StencilException;
import edu.iu.epic.visualization.linegraph.view.StencilGUI;
import edu.iu.epic.visualization.linegraph.zoom.NaiveSubsetAlgorithm;
import edu.iu.epic.visualization.linegraph.zoom.ZoomAlgorithm;


/*
 * This class is the main interface used to interact with the Stencil. 
 * It maintains state that lasts longer than a single run of a Stencil
 * (e.g. which lines are visible or not visible).
 */
public class StencilController {

	private JSplitPane parent;
	private StencilRun currentPanel;
	private String stencilScript;
	private Collection<StencilData> stencilData = new ArrayList<StencilData>();
	
	private Map<String, Boolean> lineVisibilityStates =
		Collections.synchronizedMap(new HashMap<String, Boolean>());

	
	/* Assigned color based on ColorSchema */
	private ColorRegistry<String> colorRegistry;
	
	public StencilController(
			JSplitPane parent,
			String stencilScript, 
			StencilData initialData,
			ColorRegistry<String> colorRegistry) {
		this.parent = parent;
		this.stencilScript = stencilScript;
		this.stencilData.add(initialData);
		this.colorRegistry = colorRegistry;
	}

	public void addStencilDataToGraph(StencilData stencilDatum) {
		this.stencilData.add(stencilDatum);
	}

	public void playStencil(final RUN_TYPE runType, 
							final TimestepBounds timestepBounds, 
							final JSlider graphSubsetSetterSlider, 
							final JFrame stencilGUIFrame) throws StencilException {
		final StencilException[] stencilExceptionThrown = new StencilException[1];

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					// Replace the old panel with a new one.
					
					StencilRun newPanel = createNewPanel(
						StencilController.this.stencilScript, StencilController.this.stencilData);
					swapInNewPanel(newPanel);
					StencilController.this.currentPanel = newPanel;

					/* Assigned colors to lines before the run starts. */
					assignColorsToLines();
					
					/*
	 				 * Tell the new panel which lines should be visible
	 				 * (lines visible/invisible carries over between stencil runs).
	 				 */
					Set<String> lineNames = lineVisibilityStates.keySet();
					for (String lineName : lineNames) {
						Boolean lineVisibility = lineVisibilityStates.get(lineName);
						StencilController.this.currentPanel.setLineVisible(
							lineName, lineVisibility);
					}
					
					/*
					 * Depending upon the run type it will perform pre-processing before
					 * actually starting the line graph animation. 
					 * */
					if (runType == StencilRun.RUN_TYPE.COMPLETE) {
						
						if (graphSubsetSetterSlider != null) {
							graphSubsetSetterSlider.setValue(graphSubsetSetterSlider.getMaximum());	
						}
						
						// Start the new panel.
						StencilController.this.currentPanel.start(null);	

					} else {
						TimestepBounds calculatedTimestepsBounds;

						/*
						 * When timestep bounds are not provided, that means we need to use
						 * an algorithm to find the subset bounds using an algorithm. 
						 * */
						if (timestepBounds == null) {
							
							ZoomAlgorithm zoomAlgorithm = new NaiveSubsetAlgorithm();

							List<Integer> upperTimeStepBounds = new ArrayList<Integer>();
							for (StencilData currentStencilData 
									: StencilController.this.stencilData) {
								Integer timestepUpperBoundFromAllStreamSources = currentStencilData
										.getTimestepUpperBoundFromAllStreamSources(zoomAlgorithm);
								upperTimeStepBounds.add(
										timestepUpperBoundFromAllStreamSources);
							}
							
							calculatedTimestepsBounds = new TimestepBounds(
									null, 
									Collections.max(upperTimeStepBounds));
							
							graphSubsetSetterSlider.setValue(Collections.max(upperTimeStepBounds));
							
						} else {
							calculatedTimestepsBounds = timestepBounds;
						}
						
						StencilController.this.currentPanel.start(calculatedTimestepsBounds);

						/*
						 * Here in lies an ugly hack to make sure that stencil renders the line 
						 * graph properly. We resize the entire frame manually by one pixel & 
						 * then back. I hope the god of programmers will not smite me with it's 
						 * fury!
						 * */
						Dimension size = stencilGUIFrame.getSize();
						int temporaryResizeByPixels = 3;
						stencilGUIFrame.setSize(size.width + temporaryResizeByPixels, 
												size.height);
						stencilGUIFrame.setSize(size.width, size.height);
						
					}
					
				} catch (StencilException stencilException) {
					stencilExceptionThrown[0] = stencilException;
				}
			}
		});
		
		if (stencilExceptionThrown[0] != null) {
			throw stencilExceptionThrown[0];
		}
	}
	
	/* Assign color for each lines */
	private void assignColorsToLines() throws StencilException {
		
		for (StencilData data : this.stencilData) {
			for (String lineName : data.getLineColumnNames()) {
				Color color = colorRegistry.getColorOf(lineName);
				StencilController.this.currentPanel.setLineColor(lineName, color);
			}
		}
	}

	public void setLineVisible(String lineName, boolean visible) throws StencilException {
		// TODO: Put in runnable?
		// Make the line visible/invisible on the current panel.
		this.currentPanel.setLineVisible(lineName, visible);
		// And remember its visibility/invisibility for future panels.
		this.lineVisibilityStates.put(lineName, visible);
	}

	public void export() {
		this.currentPanel.export();
	}

	private void swapInNewPanel(StencilRun newPanel) throws StencilException {
		try {
			StencilRun oldPanel = this.currentPanel;
	
			this.currentPanel = newPanel;
			
			JComponent stencilPanel = this.currentPanel.getComponent();
			
			stencilPanel.setVisible(true);
			
			stencilPanel.setMinimumSize(StencilGUI.STENCIL_GRAPH_SIZE);
			
			this.parent.setTopComponent(stencilPanel);
			
			if (oldPanel != null) {
				oldPanel.dispose();
			}
		} catch (Exception exception) {
			throw new StencilException(exception);
		}
	}

	private StencilRun createNewPanel(String stencilScript, Collection<StencilData> stencilData)
			throws StencilException {
		return new StencilRun(this.parent, stencilScript, stencilData);
	}

	public List<Integer> getTimestepsFromAllStencilDatums() {
		
		Set<Integer> timesteps = new HashSet<Integer>();
		
		for (StencilData currentStencilData : StencilController.this.stencilData) {
			timesteps.addAll(currentStencilData.getTimestepsFromAllSources());
		}
		
		List<Integer> unifiedTimestepList = new ArrayList<Integer>(timesteps);
		
		Collections.sort(unifiedTimestepList);
		
		return unifiedTimestepList;
	}
}
