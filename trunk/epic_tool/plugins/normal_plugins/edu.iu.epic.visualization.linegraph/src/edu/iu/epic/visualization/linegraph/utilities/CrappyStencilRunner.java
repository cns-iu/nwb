package edu.iu.epic.visualization.linegraph.utilities;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.cishell.utilities.FileUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import prefuse.data.Table;
import prefuse.util.collections.IntIterator;
import stencil.adapters.java2D.Adapter;
import stencil.adapters.java2D.Panel;
import stencil.streams.Tuple;
import stencil.util.BasicTuple;

// Not generic.  (Specific to LineGraphAlgorithm.)
public class CrappyStencilRunner {
	// TODO: Change these to the real deal when the time is right.
	public static final String STENCIL_STREAM_NAME = "Stocks";
	public static final String STENCIL_X_AXIS_NAME = "Date";
	public static final String STENCIL_Y_AXIS_NAME = "Open";
	
	public static final String REPLAY_BUTTON_LABEL = "Replay";
	public static final String EXPORT_BUTTON_LABEL = "Export";
	public static final String FILE_SAVE_DIALOG_TEXT =
		"Export Line Graph Rendering As...";
	public static final String PNG_FILE_EXTENSION = "PNG";
	public static final String EPS_FILE_EXTENSION = "EPS";
	public static final String[] FILE_SAVE_DIALOG_FILTER_EXTENSIONS = {
		EPS_FILE_EXTENSION,
		PNG_FILE_EXTENSION
	};

	private String xAxisName;
	private String yAxisName;
	private Table inputTable;
	private String stencilProgramString;
	private Display display;
	private Shell shell;
	private Composite stencilContainer;
	private Frame stencilFrame;
	private Panel stencilPanel;
	
	private CrappyStencilRunner(
			File stencilProgramFile,
			String xAxisName,
			String yAxisName,
			Table inputTable)
			throws IOException, Exception {
		this(FileUtilities.readEntireTextFile(
			stencilProgramFile), xAxisName, yAxisName, inputTable);
	}
	
	private CrappyStencilRunner(
			String stencilProgramString,
			String xAxisName,
			String yAxisName,
			Table inputTable)
			throws Exception {
		this.stencilProgramString = stencilProgramString;
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
		this.inputTable = inputTable;
		this.display = new Display();
		createShell();
	}
	
	public static CrappyStencilRunner createStencilRunner(
			File stencilProgramFile,
			String xAxisName,
			String yAxisName,
			Table inputTable)
			throws StencilRunnerCreationException {
		try {
			return new CrappyStencilRunner(
				stencilProgramFile, xAxisName, yAxisName, inputTable);
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
	
	public Display getDisplay() {
		return this.display;
	}
	
	public Shell getShell() {
		return this.shell;
	}
	
	public void runStencil() {
    	try {
    		resetStencilStuff();
			
    		for (IntIterator rows = this.inputTable.rows(); rows.hasNext(); ) {
				int rowIndex = rows.nextInt();

				stencilPanel.processTuple(
					createTuple(this.inputTable, rowIndex));
			}
    	} catch (Exception stencilException) {
    		// TODO: This sucks.  It'd be really nice if this exception could
    		// be caught much earlier on, as opposed to potentially any time
    		// the user triggers a replay.
    		// TODO: Do something sensible.
    	}
    }
	
	private void createShell() {
		this.shell = new Shell(this.display);
		this.shell.setLayout(new RowLayout());
		
		Composite shellContents = createContentsComposite(this.shell);
		Composite userControlPanel = createUserControlPanel(shellContents);
		this.stencilContainer = createStencilContainer(shellContents);
		resetStencilStuff();
		
		this.shell.pack();
	}
	
	private void resetStencilStuff() {
		try {
			Adapter displayAdapter = Adapter.INSTANCE;
			// TODO: I *really* hate this.
			if (this.stencilFrame != null) {
				this.stencilPanel.dispose();
				this.stencilFrame.dispose();
			}

			this.stencilFrame = SWT_AWT.new_Frame(this.stencilContainer);
			this.stencilPanel = displayAdapter.compile(stencilProgramString);

			this.stencilFrame.add(this.stencilPanel);
			this.stencilFrame.setSize(800, 600);
			this.stencilFrame.pack();
		} catch (Exception exception) {
			System.err.println("Some better message");
		}
	}
	
	// TODO: Better error handling of null/invalid values in the two columns?
    private Tuple createTuple(Table table, int rowIndex) {
    	prefuse.data.Tuple row = table.getTuple(rowIndex);

    	return new BasicTuple(
    		STENCIL_STREAM_NAME,
    		Arrays.asList(
    			new String[] { STENCIL_X_AXIS_NAME, STENCIL_Y_AXIS_NAME }),
    		Arrays.asList(new String[] {
    			row.get(this.xAxisName).toString(),
    			row.get(this.yAxisName).toString() }));
    }
	
	private Composite createContentsComposite(Shell shell) {
    	Composite contents = new Composite(shell, SWT.NONE);
    	GridLayout layout = new GridLayout(2, false);
    	contents.setLayout(layout);
    	
    	return contents;
    }
	
	private Composite createUserControlPanel(Composite shellContents) {
    	Composite buttonPanel = new Composite(shellContents, SWT.BORDER);
    	RowLayout layout = new RowLayout(SWT.VERTICAL);
    	buttonPanel.setLayout(layout);
    	
    	Button replayButton = createReplayButton(buttonPanel);
    	RowData replayButtonLayoutData = new RowData();
    	replayButton.setLayoutData(replayButtonLayoutData);
    	
    	Button exportButton = createExportButton(buttonPanel);
    	RowData exportButtonLayoutData = new RowData();
    	exportButton.setLayoutData(exportButtonLayoutData);
    	
    	Composite checkBoxContainer = createCheckBoxContainer(shellContents);
    	
    	return buttonPanel;
    }
	
	private Composite createStencilContainer(Composite shellContents) {
		Composite stencilContainer =
			new Composite(shellContents, SWT.BORDER | SWT.EMBEDDED);
		RowLayout layout = new RowLayout();
		stencilContainer.setLayout(layout);
		
		return stencilContainer;
	}
	
	private Button createReplayButton(Composite userControlContainer) {
    	Button replayButton = new Button(userControlContainer, SWT.PUSH);
    	replayButton.setText(REPLAY_BUTTON_LABEL);
    	
    	replayButton.addSelectionListener(new SelectionAdapter() {
    		public void widgetSelected(SelectionEvent selectionEvent) {
    			CrappyStencilRunner.this.runStencil();
    		}
    	});
    	
    	return replayButton;
    }
	
	private Button createExportButton(final Composite userControlContainer) {
		Button exportButton = new Button(userControlContainer, SWT.PUSH);
		exportButton.setText(EXPORT_BUTTON_LABEL);
		
		exportButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent selectionEvent) {
				FileDialog fileDialog =
					new FileDialog(userControlContainer.getShell(), SWT.SAVE);
				fileDialog.setText(FILE_SAVE_DIALOG_TEXT);
				// TODO: fileDialog.setFilterPath(some thing);
				fileDialog.setFilterExtensions(
					FILE_SAVE_DIALOG_FILTER_EXTENSIONS);
				String newFilePath = fileDialog.open();
				
				if (newFilePath != null) {
					String fileExtension = FILE_SAVE_DIALOG_FILTER_EXTENSIONS[
						fileDialog.getFilterIndex()];
					
					// TODO: this.stencilPanel.export(something...);
					try {
						System.err.println("Trying to export");
						CrappyStencilRunner.this.stencilPanel.export(
							newFilePath, fileExtension, null);
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}
		});
		
		return exportButton;
	}

	private Composite createCheckBoxContainer(Composite userControlContainer) {
		Composite checkBoxContainer =
			new Composite(userControlContainer, SWT.BORDER);
		GridLayout layout = new GridLayout(2, false);
		checkBoxContainer.setLayout(layout);
		
		// TODO: Create checkboxes here.
		
		return checkBoxContainer;
	}
}