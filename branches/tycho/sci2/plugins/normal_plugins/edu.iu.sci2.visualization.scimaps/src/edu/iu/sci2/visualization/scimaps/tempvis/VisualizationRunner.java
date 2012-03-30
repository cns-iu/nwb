package edu.iu.sci2.visualization.scimaps.tempvis;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import org.freehep.util.export.ExportDialog;

public class VisualizationRunner {
	public static final String PAUSE_RESUME_BUTTON_COMMAND = "pause/resume";
	public static final String ENABLE_DISABLE_SCALING_CHECK_BOX_COMMAND = "enable/disable scaling";

	private RenderableVisualization visualization;

	private VisualizationPanel visualizationPanel;
	private JScrollPane containerPanel;
	private JFrame frame;

	public VisualizationRunner(
			RenderableVisualization visualization) {
		this.visualization = visualization;
		
		this.visualizationPanel =
			new VisualizationPanel(visualization);
		this.containerPanel = new JScrollPane(
			this.visualizationPanel,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
                	getContainerPanel(), "Export view as ...", getVisualizationPanel(), "export");
            }
        });
        file.add(exportItem);

        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	getFrame().dispose();
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

	protected JFrame getFrame() {
		return this.frame;
	}
	
	protected JScrollPane getContainerPanel() {
		return this.containerPanel;
	}
	
	protected VisualizationPanel getVisualizationPanel() {
		return this.visualizationPanel;
	}

	private Component createContentPane() {
		return this.containerPanel;
	}
}

class VisualizationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private RenderableVisualization visualization;


	public VisualizationPanel(
			RenderableVisualization visualization) {
		this.visualization = visualization;
		
		setPreferredSize(visualization.getDimension());
	}

	@Override
	public void paintComponent(Graphics graphics) {
		if (graphics == null) {
			return;
		}

//		VectorGraphics vectorGraphics = VectorGraphics.create(graphics);
//		Dimension currentSize = getSize();
//		vectorGraphics.setColor(Color.white);
//		vectorGraphics.fillRect(0, 0, currentSize.width, currentSize.height);
//		vectorGraphics.setColor(Color.black);
//
//		GraphicsState graphicsState = this.visualization.preRender(
//			vectorGraphics, /*this.messages,*/ currentSize);
		
		Dimension currentSize = getSize();
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, currentSize.width, currentSize.height);
		graphics.setColor(Color.black);

		GraphicsState graphicsState = this.visualization.preRender((Graphics2D) graphics, currentSize);

		this.visualization.render(graphicsState, currentSize);
	}
}
