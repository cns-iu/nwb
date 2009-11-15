package edu.iu.epic.visualization.linegraph.utilities;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.jdesktop.swingworker.SwingWorker;

import stencil.adapters.java2D.Adapter;
import stencil.adapters.java2D.Panel;

public class StencilRunner {
	public static final String REPLAY_BUTTON_LABEL = "Replay";
	public static final String EXPORT_BUTTON_LABEL = "Export";
	public static final String FILE_SAVE_DIALOG_TEXT =
		"Export Line Graph Rendering As...";
	
	private boolean isShown = false;
	
	private JFrame frame;
	private JSplitPane splitPane;
	private Panel stencilPanel;
	private StencilData stencilData;
	
	public StencilRunner(StencilData stencilData) throws Exception {
		
		this.stencilData = stencilData;
		
		createSplitPane();
	}

//	public static StencilRunner createStencilRunner()
//			throws StencilRunnerCreationException {
//		try {
//			return createStencilRunner(FileUtilities.readEntireTextFile(
//				stencilProgramFile));
//		} catch (IOException ioException) {
//			String exceptionMessage =
//				"A problem occurred while trying to read the file " +
//				"\"" + stencilProgramFile.getAbsolutePath() + "\".";
//			
//			throw new StencilRunnerCreationException(
//				exceptionMessage, ioException);
//		} catch (Exception exception) {
//			String exceptionMessage = exception.getMessage();
//			
//			throw new StencilRunnerCreationException(
//				exceptionMessage, exception);
//		}
//	}
	
//	public static StencilRunner createStencilRunner(
//			String stencilProgramString)
//			throws Exception {
//		return new StencilRunner(stencilProgramString);
//	}
	
	private void addNewStencilPanel(StencilData stencilData)  {
		//dispose of old panel

//				if (StencilRunner.this.stencilPanel != null) {
//					StencilRunner.this.stencilPanel.dispose();
//					StencilRunner.this.splitPane.setRightComponent(null);
//					StencilRunner.this.stencilPanel = null;
//				}
				
				//create and add new panel
				
				Adapter displayAdapter = Adapter.INSTANCE;
				String stencilScript = StencilRunner.this.stencilData.getStencilScript();
				try {
					
					Panel oldStencilPanel = this.stencilPanel;
					
					StencilRunner.this.stencilPanel = displayAdapter.compile(stencilScript);
					StencilRunner.this.splitPane.setRightComponent(stencilPanel);
					StencilRunner.this.stencilPanel.preRun();
					
					if (oldStencilPanel != null) {oldStencilPanel.dispose();}
					
					StencilRunner.this.splitPane.repaint();
					Thread.yield();
					
					StencilRunner.this.splitPane.repaint();
				} catch (Exception e) {
					//TODO: Don't suck
				}
	}
	
	public void playFromBeginning() throws Exception {
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//			try {
//				StencilRunner.this.reallyDoDrawing();
//				System.out.println("Done with reallyDoDrawing call");
//			} catch (Exception e) {
//				//TODO: Do this right
//				throw new RuntimeException(e);
//			}
//		}
//		}
//		);
		(new TupleFeeder()).execute();
		//reallyDoDrawing();
	}
	
	public void playWithNewData(StencilData stencilData) throws Exception {
		this.stencilData = stencilData;
		playFromBeginning();
	}
	
	public void show() {
		createFrame("Stencil");
		this.isShown = true;
	}
	
	public boolean isShown() {
		return this.isShown;
	}
	
	/* WARNING: Disposing this frame will also dispose the stencil panel
	 * inside of it!
	 */
	private JFrame createFrame(String frameTitle) {
		if (this.frame == null) {
		this.frame = new JFrame(frameTitle);
		Container contentPane = frame.getContentPane();
		contentPane.add(this.splitPane);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setSize(800, 600);
		frame.setBackground(Color.BLACK);
		//this.stencilPanel.setSize(800, 600);
		//this.stencilPanel.preRun(); MIGHT CAUSE PROBLEMS IF DOESN'T EXIST AT THIS POINT
		frame.setVisible(true);
		}
		return frame;
		
	}
	
//	private void reset() throws Exception {
//		for (TupleStream stream : this.tupleStreams) {
//			stream.reset();
//		}
//		
//		if (this.stencilPanel != null) {
//			this.stencilPanel.dispose();
//		}
//		
//		this.stencilPanel = createStencilPanel();
//		this.splitPane.setRightComponent(this.stencilPanel);
//	}
	
	private void createSplitPane() throws Exception {
		JPanel userControlPanel = createUserControlPanel();
		//this.stencilPanel = createStencilPanel();
		this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		this.splitPane.setLeftComponent(userControlPanel);
		//addNewStencilPanel(this.stencilData);
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
		return userControlPanel;
	}
	
	private Panel createStencilPanel() throws Exception {
		Adapter displayAdapter = Adapter.INSTANCE;
		
		return displayAdapter.compile(this.stencilData.getStencilScript());
	}
	
	private JButton createReplayButton() {
		JButton replayButton = new JButton(REPLAY_BUTTON_LABEL);

		replayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
			System.out.println( "Are we in eventDispatch?" + javax.swing.SwingUtilities.isEventDispatchThread());
			try {
				StencilRunner.this.playFromBeginning();
			} catch (Exception e) {
				//TODO: CRIME!
			}
			}
		});

		return replayButton;
	}
	
	private JButton createExportButton() {
		JButton exportButton = new JButton(EXPORT_BUTTON_LABEL);
		
		// TODO: Action listener stuff.
		
		return exportButton;
	}
	
	
	private void reallyDoDrawing() {
//		if (! this.isShown()) {
//		show();
//	}
		
		
		
	addNewStencilPanel(stencilData);
	try{
//		this.stencilPanel.repaint();
//		this.splitPane.revalidate();
//	Thread.sleep(500);
		this.stencilPanel.repaint();
		this.splitPane.revalidate();
		System.err.println("After validation");
	} catch (Exception e) {
		
	}
	List<TupleStream> streams = stencilData.createStreams();
	/* TODO: Provide the option of playing the streams in parallel vs.
	 * playing them one after another?
	 */
	/*for (TupleStream stream : this.tupleStreams) {
		
	}*/
	// We're assuming all of our streams are of the same size.
	// TODO: Change this?
	if (streams.size() != 0) {
		try {
			long streamSize = streams.get(0).streamSize();
			System.out.println( "Tuple pushing in eventDispatch?" + javax.swing.SwingUtilities.isEventDispatchThread());
			for (long ii = 0; ii < streamSize; ii++) {
				for (TupleStream stream : streams) {
					//Thread.sleep(1);
					StencilRunner.this.stencilPanel.processTuple(stream.nextTuple());
					//System.out.println(ii);
				}
			}
		} catch (Exception processTupleFailedException) {
			// TODO
			processTupleFailedException.printStackTrace();
		}
	}
	}
	private class TupleFeeder extends SwingWorker<Object, Object> {
		public String doInBackground() {
			StencilRunner.this.reallyDoDrawing();
			return null;
		}
		
		protected void done() {
			//DO SOME STUFF?
		}
	}
}