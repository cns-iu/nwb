package edu.iu.epic.visualization.linegraph.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalCheckBoxIcon;

import org.cishell.utilities.color.ColorRegistry;

import edu.iu.epic.visualization.linegraph.ActiveAlgorithmHook;
import edu.iu.epic.visualization.linegraph.LineGraphAlgorithm;
import edu.iu.epic.visualization.linegraph.controller.StencilController;
import edu.iu.epic.visualization.linegraph.controller.StencilRun;
import edu.iu.epic.visualization.linegraph.model.StencilData;
import edu.iu.epic.visualization.linegraph.model.TimestepBounds;
import edu.iu.epic.visualization.linegraph.stencil.hack.PropertiesSource;
import edu.iu.epic.visualization.linegraph.stencil.hack.PropertyManager2;
import edu.iu.epic.visualization.linegraph.utilities.StencilColorSchema;
import edu.iu.epic.visualization.linegraph.utilities.StencilException;

/*
 * This class creates the Stencil GUI
 *  and wires up its controls to the StencilController.
 */
public class StencilGUI {
	public static final String REPLAY_BUTTON_LABEL = "Replay";
	public static final String EXPORT_BUTTON_LABEL = "Export";
	public static final String ZOOM_BUTTON_LABEL_TEXT = "Show active segment";
	public static final String FILE_SAVE_DIALOG_TEXT = "Export Line Graph Rendering As...";
	public static final Dimension STENCIL_GUI_SIZE = new Dimension(800, 600);
	public static final Dimension STENCIL_GRAPH_SIZE = new Dimension(700, 500);
	public static final Dimension COLOR_ICON_SIZE = new Dimension(
			(int) (new MetalCheckBoxIcon().getIconHeight()),
			(int) (new MetalCheckBoxIcon().getIconHeight()));

	private JFrame frame;
	private JPanel userControlPanel;
	private JSlider graphSubsetSetterSlider;
	private JSplitPane mainSplitPanel;
	private StencilController controller;
	private ColorRegistry<String> colorRegistry;
	private Map<String, StencilData> stencilDataByTitle = new HashMap<String, StencilData>();

	public StencilGUI(PropertiesSource stencilConfiguration,
			String stencilScript, StencilData initialData, String guiTitle,
			final LineGraphAlgorithm hostAlgorithm,
			final ActiveAlgorithmHook activeAlgorithmHook)
			throws StencilException {

		this.colorRegistry = new ColorRegistry<String>(new StencilColorSchema());
		PropertyManager2.loadProperties(stencilConfiguration);

		createStencilGUI(guiTitle, STENCIL_GUI_SIZE);
		this.stencilDataByTitle.put(hostAlgorithm.getTitle(), initialData);

		initZoomStencilPanel(initialData.getSortedTimestepsFromAllSources());

		initCheckBoxes(initialData.getLineColumnNames());

		/*
		 * This sets it up so the algorithm that this StencilGUI is attached to
		 * unregisters itself with the factory when it's been closed.
		 */
		this.frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				activeAlgorithmHook.nowInactive(hostAlgorithm);
			}

		});

		this.controller = new StencilController(
				(JSplitPane) this.mainSplitPanel.getRightComponent(),
				stencilScript, initialData, colorRegistry);
	}

	public void run() throws StencilException {
		this.controller.playStencil(StencilRun.RUN_TYPE.COMPLETE, 
									null, 
									this.graphSubsetSetterSlider,
									this.frame);
	}
	
	/**
	 * This method will run zoomed part of the stencil graph. Since we 
	 * don't provide the time step bounds it will be calculated by whatever
	 * default zoom algorithm was selected. 
	 * @throws StencilException
	 */
	public void runShowingActiveSegment()
			throws StencilException {
		this.controller.playStencil(StencilRun.RUN_TYPE.SUBSET, 
								   null, 
								   this.graphSubsetSetterSlider,
								   this.frame);
	}

	/**
	 * This method will run sunset or the zoomed part of the stencil graph. 
	 * @param timestepBounds
	 * @throws StencilException
	 */
	public void runSubset(TimestepBounds timestepBounds)
			throws StencilException {
		this.controller.playStencil(StencilRun.RUN_TYPE.SUBSET, 
								   timestepBounds, 
								   this.graphSubsetSetterSlider,
								   this.frame);
	}

	public void show() {
		this.frame.setVisible(true);

		/*
		 * This to make sure that when a user chooses to run a simulation in
		 * already opened line graph window, which was minimised earlier, the
		 * line graph window being used is shown. So that the user knows that
		 * it's simulation is being run in that line graph window.
		 */
		if (this.frame.getState() == Frame.ICONIFIED) {
			this.frame.setState(Frame.NORMAL);
		}
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
		this.controller.addStencilDataToGraph(stencilDatum);

		/*
		 * Make sure that new stencil datum is already to the controller before 
		 * initializing zoom stencil panel. 
		 * */
		initZoomStencilPanel(this.controller.getTimestepsFromAllStencilDatums());
		initCheckBoxes(stencilDatum.getLineColumnNames());

	}

	private JFrame createStencilGUI(String frameTitle, Dimension frameSize)
			throws StencilException {
		if (this.frame == null) {
			this.frame = new JFrame(frameTitle + " Line Graph");

			Container contentPane = frame.getContentPane();

			this.mainSplitPanel = createUserControlAndStencilSplitPanel();

			contentPane.add(mainSplitPanel);

			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.pack();
			frame.setSize(frameSize);
			frame.setBackground(Color.BLACK);
		}

		return frame;

	}

	/**
	 * This method created the main split panel with user control panel (having replay, 
	 * export button etc) on the left & stencil canvas area (having the actual line graph
	 * and zoom control panel) on the right.
	 * @return
	 * @throws StencilException
	 */
	private JSplitPane createUserControlAndStencilSplitPanel() throws StencilException {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		JPanel userControlPanel = createUserControlPanel();
		splitPane.setLeftComponent(userControlPanel);

		/*
		 * At the time of init system doens have handle on the actual content for the right 
		 * panel, so we create filler panels instead which have a message "Loading..." in it.
		 * */
		JPanel fillerPanel = createFillerPanel();
		
		/*
		 * The right panel is further spit in top & bottom panels containing the stencil 
		 * line graph and stencil zoom control panel respectively.
		 * */
		JSplitPane stencilSplitPanel = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				fillerPanel, 
				fillerPanel);

		splitPane.setRightComponent(stencilSplitPanel);

		return splitPane;
	}

	/** This side-effects userControlPanel. */
	private JPanel createUserControlPanel() {
		// TODO: I wish there were a way to do this better.
		this.userControlPanel = new JPanel();
		BoxLayout layout = new BoxLayout(userControlPanel, BoxLayout.Y_AXIS);
		userControlPanel.setLayout(layout);
		userControlPanel.setBackground(Color.WHITE);

		JButton replayButton = createReplayButton();
		// TODO: Set layout data or something for the replayButton?
		userControlPanel.add(replayButton);

		JButton exportButton = createExportButton();
		// TODO: Set layout data or something for the exportButton?
		userControlPanel.add(exportButton);

		return userControlPanel;
	}

	/*
	 * Create check boxes for the given lineColumnNames
	 */
	private void initCheckBoxes(Collection<String> lineColumnNames) {
		for (String lineColumnName : lineColumnNames) {
			/* Create a panel for check box */
			JPanel checkBoxPanel = new JPanel();
			checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel,
					BoxLayout.X_AXIS));
			checkBoxPanel.setAlignmentX(0.0f);
			checkBoxPanel.setBackground(Color.WHITE);
			userControlPanel.add(checkBoxPanel);

			/* Create line check box */
			JCheckBox lineCheckBox = createVisibilityCheckbox(lineColumnName);
			checkBoxPanel.add(lineCheckBox);

			/* Create the label of the line check box */
			JLabel lineCheckBoxLabel = createLineCheckBoxLabel(lineColumnName);
			checkBoxPanel.add(lineCheckBoxLabel);
		}
	}

	/**
	 * This method will create a new panel that resides below the stencil line graph.
	 * It has slider & button to automatically zoom the current line graph so that only
	 * the most interesting section of the entire line graph is seen at a time.
	 * @param list
	 */
	private void initZoomStencilPanel(List<Integer> list) {

		Integer maxTimestep = Collections.max(list);
		Integer minTimestep = Collections.min(list);
		int midTimestep = (maxTimestep - minTimestep) / 2;
		
		/*
		 * Create a new slider instance and other elements belonging to this panel
		 * only if it doesn't exist yet. 
		 * */
		if (this.graphSubsetSetterSlider == null) {

			JPanel stencilZoomControlPanel = new JPanel();

			JLabel sliderLabel = new JLabel("Show");
			stencilZoomControlPanel.add(sliderLabel);
			
			sliderLabel = new JLabel("less");
			Font f = sliderLabel.getFont();
			sliderLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
			stencilZoomControlPanel.add(sliderLabel);

			this.graphSubsetSetterSlider = createZoomStencilSlider(minTimestep, maxTimestep);
			stencilZoomControlPanel.add(this.graphSubsetSetterSlider);
			
			sliderLabel = new JLabel("more");
			f = sliderLabel.getFont();
			sliderLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
			stencilZoomControlPanel.add(sliderLabel);

			sliderLabel = new JLabel("   ");
			stencilZoomControlPanel.add(sliderLabel);

			JButton autoZoomButton = createAutoZoomButton();
			stencilZoomControlPanel.add(autoZoomButton);

			((JSplitPane) this.mainSplitPanel.getRightComponent())
					.setBottomComponent(stencilZoomControlPanel);

		} else {

			/*
			 * Once the zoom panel is created we no longer mneed to recreate it, but we do 
			 * need to update the slider with new timestep values and subsequently new labels
			 * for the start, mid & end timestep. 
			 * */
			this.graphSubsetSetterSlider.setMinimum(minTimestep);

			this.graphSubsetSetterSlider.setMaximum(maxTimestep);

			this.graphSubsetSetterSlider.setValue(this.graphSubsetSetterSlider
					.getMaximum());

		}
	}

	/**
	 * This method side-effects the slider instance by setting up the labels to 
	 * be displayed below the start, mid & end timestep. 
	 * 
	 * @param maxTimestep
	 * @param minTimestep
	 * @param midTimestep
	 */
	private void initZoomSliderLabels(
			int maxTimestep,	 
			int minTimestep,
			int midTimestep) {
		
		Hashtable<Integer, JLabel> timestepToTickLabel = new Hashtable<Integer, JLabel>();

		timestepToTickLabel.put(minTimestep, new JLabel(Integer.toString(minTimestep)));
		timestepToTickLabel.put(minTimestep + midTimestep, 
								new JLabel(Integer.toString(minTimestep + midTimestep)));
		timestepToTickLabel.put(maxTimestep, new JLabel(Integer.toString(maxTimestep)));

		this.graphSubsetSetterSlider.setLabelTable(timestepToTickLabel);
		this.graphSubsetSetterSlider.setPaintLabels(true);
	}

	/*
	 * Create a CheckBox and its JLabel
	 */
	private JCheckBox createVisibilityCheckbox(final String lineName) {
		JCheckBox checkBox = new JCheckBox();
		checkBox.setBackground(Color.WHITE);
		checkBox.setSelected(true);
		checkBox.setName(lineName);
		checkBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				try {
					boolean visible = true;
					if (event.getStateChange() == ItemEvent.SELECTED) {
						visible = true;
					} else if (event.getStateChange() == ItemEvent.DESELECTED) {
						visible = false;
					}

					StencilGUI.this.controller
							.setLineVisible(lineName, visible);

				} catch (StencilException stencilException) {
					// TODO: Improve this exception handling.
					throw new RuntimeException(stencilException);
				}
			}
		});

		return checkBox;
	}

	/*
	 * Create line check box label that contains a color icon and the text label
	 */
	private JLabel createLineCheckBoxLabel(final String lineName) {

		/* Create color icon */
		Icon colorIcon = new Icon() {
			private Color color;
			private Dimension dimension;

			public Icon colorIcon(Dimension dimension, Color color) {
				this.dimension = dimension;
				this.color = color;

				return this;
			}

			public int getIconHeight() {
				return (int) this.dimension.getHeight();
			}

			public int getIconWidth() {
				return (int) this.dimension.getWidth();
			}

			public void paintIcon(Component component, Graphics graphics,
					int x, int y) {
				graphics.setColor(color);
				graphics.fillRect(x, y, getIconWidth(), getIconHeight());
			}
		}.colorIcon(COLOR_ICON_SIZE, colorRegistry.getColorOf(lineName));

		return new JLabel(lineName, colorIcon, JLabel.LEFT);
	}

	/*
	 * This panel is shown briefly while the first stencil is loading
	 */
	private JPanel createFillerPanel() {
		JPanel fillerPanel = new JPanel();
		fillerPanel.setBackground(Color.WHITE);
		JLabel loadingLabel = new JLabel("Loading...");
		fillerPanel.add(loadingLabel);

		fillerPanel.setVisible(true);
		fillerPanel.setMinimumSize(STENCIL_GRAPH_SIZE);
		fillerPanel.setSize(STENCIL_GRAPH_SIZE);
		fillerPanel.setPreferredSize(STENCIL_GRAPH_SIZE);

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

	private JSlider createZoomStencilSlider(int min, int max) {
		JSlider slider = new JSlider(min, max, max);

		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();

				/*
				 * We want to refresh the line graph only when a user has stopped moving 
				 * the slider else it's computer will explode. 
				 * */
				if (!source.getValueIsAdjusting()) {

					try {
						StencilGUI.this.runSubset(new TimestepBounds(null,
								source.getValue()));
						
						source.setToolTipText(String.valueOf(source
										.getValue()));

					} catch (Exception exception) {
						throw new RuntimeException(exception);
					}
				} 
			}
		});

		return slider;
	}

	private JButton createAutoZoomButton() {

		JButton button = new JButton(ZOOM_BUTTON_LABEL_TEXT);

		button.setToolTipText("Show only the segment where values " 
								+ "are still changing over time.");

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					StencilGUI.this.runShowingActiveSegment();
				} catch (Exception exception) {
					// TODO: Improve this exception handling.
					throw new RuntimeException(exception);
				}
			}
		});

		return button;
	}
}