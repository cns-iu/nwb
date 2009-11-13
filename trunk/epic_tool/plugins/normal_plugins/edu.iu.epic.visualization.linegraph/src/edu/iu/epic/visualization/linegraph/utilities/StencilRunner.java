package edu.iu.epic.visualization.linegraph.utilities;

import java.awt.Color;
import java.awt.Container;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.cishell.utilities.FileUtilities;

import prefuse.data.Table;
import stencil.adapters.java2D.Adapter;
import stencil.adapters.java2D.Panel;
import stencil.streams.Tuple;

public class StencilRunner {
	public static final String REPLAY_BUTTON_LABEL = "Replay";
	public static final String EXPORT_BUTTON_LABEL = "Export";
	public static final String FILE_SAVE_DIALOG_TEXT =
		"Export Line Graph Rendering As...";
	
	private String stencilProgramString;
	private JSplitPane splitPane;
	private Panel stencilPanel;
	
	private StencilRunner(
			File stencilProgramFile,
			String xAxisName,
			String yAxisName,
			Table inputTable)
			throws IOException, Exception {
		this(FileUtilities.readEntireTextFile(stencilProgramFile));
	}
	
	private StencilRunner(String stencilProgramString) throws Exception {
		this.stencilProgramString = stencilProgramString;
		createSplitPane();
	}

	public static StencilRunner createStencilRunner(File stencilProgramFile)
			throws StencilRunnerCreationException {
		try {
			return createStencilRunner(FileUtilities.readEntireTextFile(
				stencilProgramFile));
		} catch (IOException ioException) {
			String exceptionMessage =
				"A problem occurred while trying to read the file " +
				"\"" + stencilProgramFile.getAbsolutePath() + "\".";
			
			throw new StencilRunnerCreationException(
				exceptionMessage, ioException);
		} catch (Exception exception) {
			String exceptionMessage = exception.getMessage();
			
			throw new StencilRunnerCreationException(
				exceptionMessage, exception);
		}
	}
	
	public static StencilRunner createStencilRunner(
			String stencilProgramString)
			throws Exception {
		return new StencilRunner(stencilProgramString);
	}
	
	/* WARNING: Disposing this frame will also dispose the stencil panel
	 * inside of it!
	 */
	public JFrame createFrame() {
		JFrame frame = new JFrame();
		Container contentPane = frame.getContentPane();
		contentPane.add(this.splitPane);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setSize(800, 600);
		frame.setBackground(Color.BLACK);
		this.stencilPanel.setSize(800, 600);
		this.stencilPanel.preRun();
		frame.setVisible(true);
		
		return frame;
	}
	
	/* TODO: Maybe just throw a custom exception?  Really, why does
	 * processTuple *have* to throw an exception? :(
	 */
	public boolean processStencilTuple(Tuple tuple) {
		try {
			this.stencilPanel.processTuple(tuple);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void createSplitPane() throws Exception {
		JPanel userControlPanel = createUserControlPanel();
		this.stencilPanel = createStencilPanel();
		this.splitPane = new JSplitPane(
			JSplitPane.HORIZONTAL_SPLIT, userControlPanel, this.stencilPanel);
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
	
	private Panel createStencilPanel() throws Exception {
		Adapter displayAdapter = Adapter.INSTANCE;
		
		return displayAdapter.compile(this.stencilProgramString);
	}
	
	private JButton createReplayButton() {
		System.err.println("Replay button being created");
		JButton replayButton = new JButton(REPLAY_BUTTON_LABEL);
		
		// TODO: Action listener stuff.
		
		return replayButton;
	}
	
	private JButton createExportButton() {
		JButton exportButton = new JButton(EXPORT_BUTTON_LABEL);
		
		// TODO: Action listener stuff.
		
		return exportButton;
	}
}