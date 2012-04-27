package edu.iu.cns.visualization.gui.awt;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import org.freehep.util.export.ExportDialog;

import edu.iu.cns.visualization.Visualization;
import edu.iu.cns.visualization.generator.VisualizationGenerator;

public class AWTVisualizationRunner {
	public static final String PAUSE_RESUME_BUTTON_COMMAND = "pause/resume";
	public static final String ENABLE_DISABLE_SCALING_CHECK_BOX_COMMAND = "enable/disable scaling";

	private VisualizationGenerator<?> visualizationGenerator;
	private Visualization visualization;
	private AWTVisualizationDisplayPanel visualizationDisplayPanel;
	private AWTVisualizationControlPanel visualizationControlPanel;

	private JSplitPane visualizationContainerPanel;
	private AWTVisualizationParametersContainer parametersContainer;

	private JSplitPane containerPanel;
	private JFrame frame;

	public AWTVisualizationRunner(
			VisualizationGenerator<?> visualizationGenerator,
			Dimension initialSize) {
		this.visualizationGenerator = visualizationGenerator;
		this.visualization = this.visualizationGenerator.generateVisualization();
		this.visualizationDisplayPanel = new AWTVisualizationDisplayPanel(initialSize);
		this.visualizationControlPanel = new AWTVisualizationControlPanel();

		this.visualizationContainerPanel = new JSplitPane(
			JSplitPane.VERTICAL_SPLIT,
			this.visualizationDisplayPanel,
			this.visualizationControlPanel);
		this.parametersContainer = new AWTVisualizationParametersContainer();
		final JFrame frame = new JFrame(visualization.title());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(this.parametersContainer);
		frame.pack();
		frame.setVisible(true);

		this.containerPanel = new JSplitPane(
			JSplitPane.HORIZONTAL_SPLIT,
			this.parametersContainer,
			this.visualizationContainerPanel);
		this.frame =
			createFrame(this.visualization, this.visualizationDisplayPanel, this.containerPanel);
//		this.containerPanel.setLeftComponent(this.parametersContainer);
//		this.containerPanel.setRightComponent(this.visualizationContainerPanel);
	}

	private static JFrame createFrame(
			Visualization visualization,
			final AWTVisualizationDisplayPanel visualizationPanel,
			Container contents) {
		final JFrame frame = new JFrame(visualization.title());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(contents);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu file = new JMenu("File");
        menuBar.add(file);

        JMenuItem exportItem = new JMenuItem("Export...");
        exportItem.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
                ExportDialog export = new ExportDialog();
                export.showExportDialog(
                	visualizationPanel, "Export view as ...", visualizationPanel, "export");
            }
        });

        file.add(exportItem);

        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
            	frame.dispose();
            }
        });

        file.add(quitItem);

        return frame;
	}

	public void run() {
		this.frame.pack();
		this.frame.setVisible(true);
	}
}
