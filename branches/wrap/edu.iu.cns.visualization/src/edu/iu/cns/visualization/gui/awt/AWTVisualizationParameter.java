package edu.iu.cns.visualization.gui.awt;

import edu.iu.cns.visualization.parameter.VisualizationParameter;

public class AWTVisualizationParameter<V> extends VisualizationParameter<V> {
	public AWTVisualizationParameter(
			String name,
			String label,
			String description,
			V defaultValue) {
		super(name, label, description, defaultValue);
	}

	public void dispose() {
//		if (this.inputComponent != null) {
//			Component guiComponent = this.inputComponent.getGUIComponent();
//			guiComponent.setEnabled(false);
//			guiComponent.setVisible(false);
//			parent.remove(guiComponent);
//			this.inputComponent = null;
//		}
	}
}