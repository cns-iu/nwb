package edu.iu.epic.visualization.linegraph.core;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import edu.iu.epic.visualization.linegraph.ActiveAlgorithmHook;
import edu.iu.epic.visualization.linegraph.LineGraphAlgorithm;
import edu.iu.epic.visualization.linegraph.stencil.hack.PropertiesSource;
import edu.iu.epic.visualization.linegraph.stencil.hack.PropertyManager2;
import edu.iu.epic.visualization.linegraph.utilities.StencilData;
import edu.iu.epic.visualization.linegraph.utilities.StencilException;

/*
 * This class creates the Stencil GUI
 *  and wires up its controls to the StencilController.
 */
public class StencilGUI {
	public static final String REPLAY_BUTTON_LABEL = "Replay";
	public static final String EXPORT_BUTTON_LABEL = "Export";
	public static final String FILE_SAVE_DIALOG_TEXT = "Export Line Graph Rendering As...";
	
	private JFrame frame;
	private JPanel userControlPanel;
	private JSplitPane splitPane;
	private StencilController controller;
	private Map<String, StencilData> stencilDataByTitle = new HashMap<String, StencilData>();

	public StencilGUI(
			PropertiesSource stencilConfiguration,
			String stencilScript,
			StencilData initialData,
			String guiTitle,
			final LineGraphAlgorithm hostAlgorithm,
			final ActiveAlgorithmHook activeAlgorithmHook) throws StencilException {

		PropertyManager2.loadProperties(stencilConfiguration);

		createStencilGUI(guiTitle, new Dimension(800, 600));
		this.stencilDataByTitle.put(hostAlgorithm.getTitle(), initialData);

		// TODO: Hack.  Refactor this and addStencilDataToGraph to be/use the same code?
		for (String lineColumnName : initialData.getLineColumnNames()) {
			JCheckBox lineCheckBox = createVisibilityCheckbox(lineColumnName);
			this.userControlPanel.add(lineCheckBox);
		}

		/* This sets it up so the algorithm that this StencilGUI is attached to unregisters itself
		 * with the factory when it's been closed.
		 */
		this.frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				activeAlgorithmHook.nowInactive(hostAlgorithm);
			}
		});
		
		this.controller = new StencilController(splitPane, stencilScript, initialData);
	}

	public void run() throws StencilException {
		this.controller.playFromStart();
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

	public void addStencilDataToGraph(String title, StencilData stencilDatum) {
		this.stencilDataByTitle.put(title, stencilDatum);

		// Create and add check boxes for the new data being graphed.
		for (String lineColumnName : stencilDatum.getLineColumnNames()) {
			JCheckBox lineCheckBox = createVisibilityCheckbox(lineColumnName);
			this.userControlPanel.add(lineCheckBox);
		}

		this.controller.addStencilDataToGraph(stencilDatum);
	}

	private JFrame createStencilGUI(String frameTitle, Dimension frameSize)
			throws StencilException {
		if (this.frame == null) {
			this.frame = new JFrame(frameTitle + " Line Graph");

			JSplitPane splitPane = createSplitPane();
			this.splitPane = splitPane;

			Container contentPane = frame.getContentPane();
			contentPane.add(splitPane);

			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.pack();
			frame.setSize(frameSize);
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

	/** This side-effects userControlPanel. */
	private JPanel createUserControlPanel() {
		// TODO: I wish there were a way to do this better.
		this.userControlPanel = new JPanel();
		BoxLayout layout = new BoxLayout(userControlPanel, BoxLayout.Y_AXIS);
		userControlPanel.setLayout(layout);

		JButton replayButton = createReplayButton();
		// TODO: Set layout data or something for the replayButton?
		userControlPanel.add(replayButton);

		JButton exportButton = createExportButton();
		// TODO: Set layout data or something for the exportButton?
		userControlPanel.add(exportButton);

		return userControlPanel;
	}
	
	private JCheckBox createVisibilityCheckbox(final String lineName) {
		JCheckBox checkBox = new JCheckBox(lineName);
		checkBox.setSelected(true);
		checkBox.addItemListener(new ItemListener() {
			public void itemStateChanged (ItemEvent event) {
				try {
					boolean visible = true;
					if (event.getStateChange() == ItemEvent.SELECTED) {
						visible = true;
					} else if (event.getStateChange() == ItemEvent.DESELECTED) {
						visible = false;
					}
					
					StencilGUI.this.controller.setLineVisible(lineName, visible);
			
					} catch (StencilException stencilException) {
						// TODO: Improve this exception handling.
						throw new RuntimeException(stencilException);
					}
				}
			});
		
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
					StencilGUI.this.run();
				} catch (Exception exception) {
					// TODO: Improve this exception handling.
					throw new RuntimeException(exception);
				}
			}
		});

		return replayButton;
	}

	private JButton createExportButton() {
		JButton exportButton = new JButton(EXPORT_BUTTON_LABEL);

		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
					StencilGUI.this.controller.export();					
			}
		});

		return exportButton;
	}
}