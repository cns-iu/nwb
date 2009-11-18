package edu.iu.epic.visualization.linegraph.core;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import edu.iu.epic.visualization.linegraph.utilities.StencilData;
import edu.iu.epic.visualization.linegraph.utilities.StencilException;

import stencil.explore.PropertyManager;

/*
 * StencilRunner creates the Stencil GUI
 *  and wires up its controls to the StencilController
 */
public class StencilRunner {
	public static final String REPLAY_BUTTON_LABEL = "Replay";
	public static final String EXPORT_BUTTON_LABEL = "Export";
	public static final String FILE_SAVE_DIALOG_TEXT = "Export Line Graph Rendering As...";
	
	private JFrame frame;
	private JSplitPane splitPane;
	private StencilController controller;

	public StencilRunner(StencilData stencilData) throws StencilException {

		PropertyManager.loadProperties(new String[0],
				PropertyManager.stencilConfig);

		createStencilGUI("Stencil");
		
		this.controller = new StencilController(splitPane, stencilData);
	}

	

	public void play() throws StencilException {
		controller.playFromStart();
	}

	public void show() {
		this.frame.setVisible(true);
	}

	public void hide() {
		this.frame.setVisible(false);
	}

	public boolean isShown() {
		return this.frame.isVisible();
	}

	private JFrame createStencilGUI(String frameTitle) throws StencilException {
		if (this.frame == null) {

			this.frame = new JFrame(frameTitle);

			JSplitPane splitPane = createSplitPane();
			this.splitPane = splitPane;

			Container contentPane = frame.getContentPane();
			contentPane.add(splitPane);

			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.pack();
			frame.setSize(800, 600);
			frame.setBackground(Color.BLACK);
		}
		return frame;

	}

	private JSplitPane createSplitPane() throws StencilException {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		JPanel userControlPanel = createUserControlPanel();
		splitPane.setLeftComponent(userControlPanel);

		JPanel fillerPanel = createFillerPanel();
		splitPane.setRightComponent(fillerPanel);

		return splitPane;
	}

	private JPanel createUserControlPanel() {
		JPanel userControlPanel = new JPanel();
		BoxLayout layout = new BoxLayout(userControlPanel, BoxLayout.Y_AXIS);
		userControlPanel.setLayout(layout);

		JButton replayButton = createReplayButton();
		// TODO: Set layout data or something for the replayButton?
		userControlPanel.add(replayButton);

		JButton exportButton = createExportButton();
		// TODO: Set layout data or something for the exportButton?
		userControlPanel.add(exportButton);
		
		//TODO: Change these to wire up to any data, not just sample data
		JCheckBox open = createVisibilityCheckbox("Open");
		userControlPanel.add(open);
		JCheckBox high = createVisibilityCheckbox("High");
		userControlPanel.add(high);
		JCheckBox low = createVisibilityCheckbox("Low");
		userControlPanel.add(low);
		JCheckBox close = createVisibilityCheckbox("Close");
		userControlPanel.add(close);
		
		return userControlPanel;
	}
	
	private JCheckBox createVisibilityCheckbox(final String lineName) {
		JCheckBox checkBox = new JCheckBox(lineName);
		checkBox.setSelected(true);
		checkBox.addItemListener(new ItemListener() {
			public void itemStateChanged (ItemEvent event) {
					try {
					boolean visible = true;
					if (event.getStateChange() == ItemEvent.DESELECTED) {
						visible = false;
					}
					
					StencilRunner.this.controller.setLineVisible(lineName, visible);
			
					} catch (StencilException e) {
						//TODO: improve
						throw new RuntimeException(e);
					}
					}});
		
		return checkBox;
	}

	/*
	 * This panel is shown briefly while the first stencil is loading
	 */
	private JPanel createFillerPanel() {
		JPanel fillerPanel = new JPanel();
		fillerPanel.setBackground(Color.WHITE);
		JLabel loadingLabel = new JLabel("Loading...");
		fillerPanel.add(loadingLabel);
		return fillerPanel;
	}

	private JButton createReplayButton() {
		JButton replayButton = new JButton(REPLAY_BUTTON_LABEL);

		replayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					StencilRunner.this.play();
				} catch (Exception e) {
					// TODO: still needs work
					throw new RuntimeException(e);
				}
			}
		});

		return replayButton;
	}


	
	private JButton createExportButton() {
		JButton exportButton = new JButton(EXPORT_BUTTON_LABEL);

		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
					StencilRunner.this.controller.export();					
			}
		});
		return exportButton;
	}
}