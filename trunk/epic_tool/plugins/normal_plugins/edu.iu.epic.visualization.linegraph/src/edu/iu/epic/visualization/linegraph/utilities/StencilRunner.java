package edu.iu.epic.visualization.linegraph.utilities;

import java.awt.Color;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jdesktop.swingworker.SwingWorker;

import stencil.adapters.java2D.Adapter;
import stencil.adapters.java2D.Panel;
import stencil.explore.PropertyManager;

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
		return userControlPanel;
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

	private JButton createExportButton() {
		JButton exportButton = new JButton(EXPORT_BUTTON_LABEL);

		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					System.out.println("Exporting button works!");
					
					//TODO: Have Joseph Cottam make an enumeration?
					String[] supportedFormats = new String[]{ "PNG", "EPS" };
					
					String selectedFormat = (String) JOptionPane.showInputDialog(null,
				            "Choose an export format", "Image Format",
				            JOptionPane.INFORMATION_MESSAGE, null,
				            supportedFormats, supportedFormats[0]);
					
					if (selectedFormat == null) {
						//user canceled export
						return;
					}
					//TODO: Is mixing swing and SWT like this a problem?
					 JFileChooser c = new JFileChooser();
				      // Demonstrate "Save" dialog:
				      int rVal = c.showSaveDialog(StencilRunner.this.frame);
				      if (rVal == JFileChooser.APPROVE_OPTION) {
				        String fullFileName = 
				        	c.getCurrentDirectory().toString() + "/" +
				        	c.getSelectedFile().getName();
				        
				        Object exportInfo = null;
				        if (selectedFormat.equals("PNG")) {
				        	 int dotsPerInch = 32;
				        	 exportInfo = dotsPerInch;
				        } else if (selectedFormat.equals("EPS")) {
				        	Rectangle dimensions = new Rectangle(400,1600);
				        	exportInfo = dimensions;
				        }
				        StencilRunner.this.stencilPanel.export(fullFileName, selectedFormat, exportInfo);
				    	 
				      }
				      if (rVal == JFileChooser.CANCEL_OPTION) {
				        System.out.println("You pressed cancel");
				        
				      }
//				     File currentDir = new File(System.getProperty("user.home") + File.separator 
//				            		+ "anything");
//				        
//				     dialog.setFilterPath(currentDir.getPath());
//				        
//
//				      String suggestedFileName = "linegraph";
//				      dialog.setFileName(suggestedFileName + "." + selectedFormat);
//				     
//				      String selectedFileName = dialog.open();
//				      
//				      if (selectedFileName == null) {
//				    	  //user cancelled export
//				    	  return;
//				      }
//				     
//				      StencilRunner.this.stencilPanel.export(
//				    		  selectedFileName, selectedFormat, null);
//					
					
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