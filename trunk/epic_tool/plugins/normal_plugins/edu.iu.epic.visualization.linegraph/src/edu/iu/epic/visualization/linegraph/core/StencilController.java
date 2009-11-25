package edu.iu.epic.visualization.linegraph.core;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

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
	private StencilData data;

	private Map<String, Boolean> lineVisibilityStates = 
		new HashMap<String, Boolean>();
	public StencilController(JSplitPane parent, StencilData stencilData) {
		this.parent = parent;
		this.data = stencilData;
	}

	public void playFromStart() throws StencilException {
		final StencilException[] stencilExceptionThrown = new StencilException[1];

		//try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						// Replace the old panel with a new one.

						StencilRun newPanel = createNewPanel(StencilController.this.data);
						swapInNewPanel(newPanel);
						StencilController.this.currentPanel = newPanel;

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
		//}
//		} catch (InvocationTargetException invocationTargetException) {
//			throw new StencilException(
//				invocationTargetException.getMessage(), invocationTargetException);
//		} catch (InterruptedException interruptedException) {
//			throw new StencilException(
//				interruptedException.getMessage(), interruptedException);
//		}
	}
	
	

	public void setLineVisible(String lineName, boolean visible)
		throws StencilException {
		//TODO: put in runnable
		//make the line visible/invisible on the current panel
		this.currentPanel.setLineVisible(lineName, visible);
		//and remember its visibility/invisibility for future panels
		this.lineVisibilityStates.put(lineName, visible);
	}

	public void export() {
		this.currentPanel.export();
	}

	private void swapInNewPanel(StencilRun newPanel)
			throws StencilException {

		try {
			StencilRun oldPanel = this.currentPanel;
	
			this.currentPanel = newPanel;
			this.parent.setRightComponent(this.currentPanel.getComponent());
			
			if (oldPanel != null) {
				oldPanel.dispose();
			}
		} catch (Exception e) {
			throw new StencilException(e);
		}
	}

	private StencilRun createNewPanel(StencilData stencilData)
			throws StencilException {
		StencilRun panel = new StencilRun(this.parent, stencilData);
		return panel;
	}

	
}
