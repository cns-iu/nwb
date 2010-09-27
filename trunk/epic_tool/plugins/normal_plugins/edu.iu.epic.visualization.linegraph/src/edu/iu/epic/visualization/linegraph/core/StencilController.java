package edu.iu.epic.visualization.linegraph.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.cishell.utilities.color.ColorRegistry;
import edu.iu.epic.visualization.linegraph.utilities.StencilData;
import edu.iu.epic.visualization.linegraph.utilities.StencilException;


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

	public void playFromStart() throws StencilException {
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

					// Start the new panel.

					StencilController.this.currentPanel.start();
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
			
			JPanel wrapperPanel = new JPanel();
			wrapperPanel.setBackground(Color.BLUE);
			JComponent stencilPanel = this.currentPanel.getComponent();
			wrapperPanel.add(stencilPanel);
			// TODO: Does this do anything?
//			wrapperPanel.add(new JButton("Meep"));
		
			this.parent.setRightComponent(this.currentPanel.getComponent());
			
			if (oldPanel != null) {
				oldPanel.dispose();
			}
		} catch (Exception exception) {
			throw new StencilException(exception);
		}
	}

	private StencilRun createNewPanel(String stencilScript, Collection<StencilData> stencilData)
			throws StencilException {
		StencilRun panel = new StencilRun(this.parent, stencilScript, stencilData);
		
		return panel;
	}
}
