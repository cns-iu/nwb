package edu.iu.cns.visualization.gui.awt;

import java.awt.Component;
import java.awt.Container;

import edu.iu.cns.visualization.gui.InputComponent;
import edu.iu.cns.visualization.parameter.VisualizationParameter;

public class AWTVisualizationParameter<V> extends VisualizationParameter<V> {
	private InputComponent<V, Component> inputComponent;

	public AWTVisualizationParameter(
			String name,
			String label,
			String description,
			V defaultValue,
			InputComponent<V, Component> inputComponent) {
		super(name, label, description, defaultValue);
		this.inputComponent = inputComponent;
	}

	public void dispose(Container parent) {
//		if (this.inputComponent != null) {
//			Component guiComponent = this.inputComponent.getGUIComponent();
//			guiComponent.setEnabled(false);
//			guiComponent.setVisible(false);
//			parent.remove(guiComponent);
//			this.inputComponent = null;
//		}
	}
}