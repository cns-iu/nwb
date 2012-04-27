package edu.iu.cns.visualization.gui.awt;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class AWTVisualizationParametersContainer extends Container {
	private static final long serialVersionUID = 7824315588866861754L;

	private JPanel controlPanel;

	public AWTVisualizationParametersContainer() {
		this.controlPanel = createControlPanel();

		add(this.controlPanel);
		setVisible(true);
	}

	private static JPanel createControlPanel() {
		JPanel controlPanel = new JPanel();
		JButton runButton = createRunButton();

		controlPanel.add(runButton);

		return controlPanel;
	}

	private static JButton createRunButton() {
		JButton runButton = new JButton("Run Visualization");

		return runButton;
	}

	public static void main(String[] arguments) {
		final JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(createControlPanel());
		frame.pack();
		frame.setVisible(true);
	}
}