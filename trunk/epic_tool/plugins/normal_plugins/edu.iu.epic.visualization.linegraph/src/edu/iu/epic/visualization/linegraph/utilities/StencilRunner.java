package edu.iu.epic.visualization.linegraph.utilities;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.swingworker.SwingWorker;

import stencil.adapters.java2D.Adapter;
import stencil.adapters.java2D.Panel;
import stencil.explore.PropertyManager;
import stencil.streams.Tuple;
import stencil.util.BasicTuple;

public class StencilRunner {
	public static final String REPLAY_BUTTON_LABEL = "Replay";
	public static final String EXPORT_BUTTON_LABEL = "Export";
	public static final String FILE_SAVE_DIALOG_TEXT = "Export Line Graph Rendering As...";
	
	private JFrame frame;
	private JSplitPane splitPane;
	private Panel stencilPanel;
	private StencilData loadedStencilData;

	public StencilRunner(StencilData stencilData) throws StencilException {

		PropertyManager.loadProperties(new String[0],
				PropertyManager.stencilConfig);
		
		this.loadedStencilData = stencilData;

		createFrame("Stencil");
	}

	private void swapInNewStencilPanel(Panel newStencilPanel)
			throws StencilException {

		try {
			Panel oldStencilPanel = this.stencilPanel;

			this.stencilPanel = newStencilPanel;

			this.splitPane.setRightComponent(newStencilPanel);

			if (oldStencilPanel != null) {
				oldStencilPanel.dispose();
			}
		} catch (Exception e) {
			throw new StencilException(e);
		}
	}

	private Panel createNewStencilPanel(StencilData stencilData) 
		throws StencilException {
		try {
			String stencilScript = stencilData.getStencilScript();

			Adapter displayAdapter = Adapter.INSTANCE;
			Panel stencilPanel = displayAdapter.compile(stencilScript);

			stencilPanel.preRun();

			return stencilPanel;
		} catch (Exception e) {
			throw new StencilException(e);
		}
	}

	public void playWithLoadedData() {
		(new TupleFeeder(this.loadedStencilData)).execute();
	}

	public void playWithNewData(StencilData stencilData) {
		(new TupleFeeder(stencilData)).execute();
		this.loadedStencilData = stencilData;
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

	private JFrame createFrame(String frameTitle) throws StencilException {
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
		JCheckBox checkBox = new JCheckBox();
		checkBox.setSelected(true);
		checkBox.addItemListener(new ItemListener() {
			public void itemStateChanged (ItemEvent e) {
				try {
					
					boolean visible = true;
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						visible = false;
					}
					
					//TODO: Probably need to do this differently
					if (StencilRunner.this.stencilPanel != null) {
						Tuple visibilityTuple = new BasicTuple("Visibility", new String[] {"Line", "Visible"}, 
								new String[] {lineName, String.valueOf(visible)});
						StencilRunner.this.stencilPanel.processTuple(visibilityTuple);
					}
						
				} catch (Exception exception) {
					// TODO: still needs work
					throw new RuntimeException(exception);
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
					StencilRunner.this.playWithLoadedData();
				} catch (Exception e) {
					// TODO: still needs work
					throw new RuntimeException(e);
				}
			}
		});

		return replayButton;
	}

	
	private static final String DEFAULT_EXPORT_FILENAME = "graph";
	
	private JButton createExportButton() {
		JButton exportButton = new JButton(EXPORT_BUTTON_LABEL);

		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					
					//TODO: EPS support seems to not be working. Disabling format choosing until it's fixed
//					String[] supportedFormats = new String[]{ "PNG", "EPS" };
//					
//					String selectedFormat = (String) JOptionPane.showInputDialog(null,
//				            "Choose an export format", "Image Format",
//				            JOptionPane.INFORMATION_MESSAGE, null,
//				            supportedFormats, supportedFormats[0]);
//					
//					if (selectedFormat == null) {
//						//user canceled export
//						return;
//					}
					
					String selectedFormat = "PNG";
					
					 JFileChooser fileChooser = new JFileChooser();
					 fileChooser.setSelectedFile(
							 new File(DEFAULT_EXPORT_FILENAME + "." + selectedFormat.toLowerCase()));
					 FileFilter pngOnly = new ExtensionFileFilter(
							 "Portable Network Graphics (PNG)", new String[]{ "png" });
					 fileChooser.setFileFilter(pngOnly);
					 
					 int approvedOrCancelled = fileChooser.showSaveDialog(StencilRunner.this.frame);
				      
				      if (approvedOrCancelled == JFileChooser.APPROVE_OPTION) {
				        String fullFileName = 
				        	fileChooser.getCurrentDirectory().toString() + "/" +
				        	fileChooser.getSelectedFile().getName();
				        
				        Object exportInfo = null;
				        
				        //TODO: Disabled until EPS works and dotsPerInch is handled right
				        if (selectedFormat.equals("PNG")) {
				        	 int dotsPerInch = 64;
				        	 exportInfo = dotsPerInch; //TODO: this seems to be ignored for now
				        }
//				        	
//				        } else if (selectedFormat.equals("EPS")) {
//				        	Rectangle dimensions = new Rectangle(400,1600);
//				        	exportInfo = dimensions;
//				        }
				        StencilRunner.this.stencilPanel.export(fullFileName, selectedFormat, exportInfo);
				    	 
				      }
				      if (approvedOrCancelled == JFileChooser.CANCEL_OPTION) {
				    	  return;
				      }
					
				} catch (Exception e) {
					// TODO: Perhaps a better way to do this
					throw new RuntimeException(e);
				}
			}
		});

		return exportButton;
	}

	/*
	 * TODO: The first half-second of drawing looks odd because the graph is so
	 * small. We might want to do something about it eventually.
	 */

	private void feedTupleStreamsToStencilPanel(StencilData stencilData) 
		throws StencilException {

		Panel stencilPanel = createNewStencilPanel(stencilData);
		swapInNewStencilPanel(stencilPanel);
		
		List<TupleStream> streams = stencilData.createStreams();
		/*
		 * TODO: Provide the option of playing the streams in parallel vs.
		 * playing them one after another?
		 */
		/*
		 * for (TupleStream stream : this.tupleStreams) {
		 * 
		 * }
		 */
		// We're assuming all of our streams are of the same size.
		// TODO: Change this?
		/*
		 * TODO: Currently if a user clicks 'replay', 
		 * the old stencil will continue to draw.
		 * We should stop feeding a stencil tuples if it is no longer active.
		 */
		
		if (streams.size() != 0) {
			try {
				long streamSize = streams.get(0).streamSize();
				for (long ii = 0; ii < streamSize; ii++) {
					for (TupleStream stream : streams) {
						// Thread.sleep(1);
						stencilPanel.processTuple(stream
								.nextTuple());
					}
				}
			} catch (Exception processTupleFailedException) {
				// TODO:
				processTupleFailedException.printStackTrace();
			}
		}
	}

	private class TupleFeeder extends SwingWorker<Object, Object> {
	
		private StencilData stencilData;
		
		public TupleFeeder(StencilData stencilData) {
			this.stencilData = stencilData;
		}
		
		public String doInBackground() {
			try {
				StencilRunner.this.feedTupleStreamsToStencilPanel(this.stencilData);
				return null;
			} catch (StencilException e) {
				// TODO: needs work
				throw new RuntimeException(e);
			}
		}

		protected void done() {
			// DO SOME STUFF?
		}
	}
	
	class SaveL implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	     
	    }
	  }
}