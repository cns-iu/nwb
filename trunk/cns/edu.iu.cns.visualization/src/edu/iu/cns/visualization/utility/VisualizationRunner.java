package edu.iu.cns.visualization.utility;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.util.export.ExportDialog;

import edu.iu.cns.visualization.RenderableVisualization;

public class VisualizationRunner {
	public static final String PAUSE_RESUME_BUTTON_COMMAND = "pause/resume";
	public static final String ENABLE_DISABLE_SCALING_CHECK_BOX_COMMAND = "enable/disable scaling";

	private RenderableVisualization visualization;
	private VisualizationMessages messages = null;
	private VisualizationPanel visualizationPanel;
	private JScrollPane containerPanel;
	private JFrame frame;

	public VisualizationRunner(
			RenderableVisualization visualization,
			Dimension initialSize) {
		this.visualization = visualization;
		this.visualizationPanel =
			new VisualizationPanel(visualization, initialSize, messages);
		this.containerPanel = new JScrollPane(
			this.visualizationPanel,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	}

	public void setUp() {
		tearDown();

		this.frame = new JFrame(this.visualization.title());
		this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.frame.getContentPane().add(createContentPane());

		JMenuBar menuBar = new JMenuBar();
		this.frame.setJMenuBar(menuBar);

		JMenu file = new JMenu("File");
        menuBar.add(file);

        JMenuItem exportItem = new JMenuItem("Export...");
        exportItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ExportDialog export = new ExportDialog();
                export.showExportDialog(
                	containerPanel, "Export view as ...", containerPanel, "export");
            }
        });
        file.add(exportItem);

        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	VisualizationRunner.this.frame.dispose();
            }
        });
        file.add(quitItem);
	}

	public void run() {
		this.frame.pack();
		this.frame.setVisible(true);
	}

	private void tearDown() {
		if (this.frame != null) {
			this.frame.dispose();
			this.frame = null;
		}
	}

	private Component createContentPane() {
//		return new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.panel, createControlsPane());
		return this.containerPanel;
	}

	private Component createControlsPane() {
		JPanel panel = new JPanel();

//		JButton pauseButton = new JButton("Pause/Resume");
//		pauseButton.setVerticalTextPosition(AbstractButton.CENTER);
//		pauseButton.setHorizontalTextPosition(AbstractButton.LEADING);
//		pauseButton.setActionCommand(PAUSE_RESUME_BUTTON_COMMAND);
//		containerPanel.add(pauseButton);
		// TODO: Setup the actual action listener.

		return panel;
	}

//	public static void main(String[] arguments) {
//		VisualizationRunner runner = new VisualizationRunner(
//			new TestVisualization(),
//			new PageOrientation(
//				PageOrientationType.PORTRAIT,
//				1.0,
//				new BasicLayout(true, new DateTime(), new DateTime(), 1.0, 1.0, 1.0)),
//			new Dimension(800, 600));
//		runner.setUp();
//		runner.run();
//	}
}

class VisualizationPanel extends JPanel {
	private RenderableVisualization visualization;
	private Dimension size;
	private VisualizationMessages messages;

	public VisualizationPanel(
			RenderableVisualization visualization,
			Dimension initialSize,
			VisualizationMessages messages) {
		this.visualization = visualization;
		this.size = initialSize;
		this.messages = messages;
		setPreferredSize(initialSize);
	}

	public void paintComponent(Graphics graphics) {
		if (graphics == null) {
			return;
		}

		VectorGraphics vectorGraphics = VectorGraphics.create(graphics);
		Dimension currentSize = getSize();
		vectorGraphics.setColor(Color.white);
		vectorGraphics.fillRect(0, 0, currentSize.width, currentSize.height);
		vectorGraphics.setColor(Color.black);

		GraphicsState graphicsState = this.visualization.preRender(
			vectorGraphics, this.messages, currentSize);

//		vectorGraphics.translate(0.0, currentSize.height);

		this.visualization.renderBody(vectorGraphics, graphicsState, this.messages, currentSize);
	}
}

class TestVisualization implements RenderableVisualization {
	public String title() {
		return "Test Visualization";
	}

	public GraphicsState preRender(
			VectorGraphics graphics,
			VisualizationMessages messages,
			Dimension size) {
		return new GraphicsState(graphics);
	}

	public void renderBody(
			VectorGraphics graphics,
			GraphicsState graphicsState,
			VisualizationMessages messages,
			Dimension size) {
		graphics.drawLine(150, 150, 450, 450);
		graphics.drawLine(150, 450, 450, 150);
		graphics.drawOval(300, 300, 300, 300);
	}
}
