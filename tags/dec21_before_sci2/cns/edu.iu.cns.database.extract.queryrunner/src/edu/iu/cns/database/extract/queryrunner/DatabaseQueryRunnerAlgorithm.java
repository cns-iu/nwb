package edu.iu.cns.database.extract.queryrunner;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.log.LogService;

import edu.iu.cns.shared.utilities.swt.CheckBox;

public class DatabaseQueryRunnerAlgorithm implements Algorithm, ProgressTrackable {
	public static final int WINDOW_WIDTH = 400;
	public static final int WINDOW_HEIGHT = 600;
	public static final int CONTROL_WIDTH = WINDOW_WIDTH - 75;
	public static final int CONTROL_HEIGHT = 50;
	public static final String DEFAULT_NODE_ID_COLUMN = "ID";
	public static final String DEFAULT_SOURCE_ID_COLUMN = "SOURCE";
	public static final String DEFAULT_TARGET_ID_COLUMN = "TARGET";

	private Data[] data;
	private CIShellContext ciShellContext;
	private DataManagerService dataManager;
	private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;

	private AlgorithmFactory tableQueryRunner;
	private AlgorithmFactory networkQueryRunner;
	private LogService logger;

	// Sort of hackish.
	private Text errorMessageWidget;

    public DatabaseQueryRunnerAlgorithm(
    		Data[] data,
    		CIShellContext ciShellContext,
    		DataManagerService dataManager,
    		AlgorithmFactory tableQueryRunner,
    		AlgorithmFactory networkQueryRunner,
    		LogService logger) {
    	this.data = data;
    	this.ciShellContext = ciShellContext;
    	this.dataManager = dataManager;
    	this.tableQueryRunner = tableQueryRunner;
    	this.networkQueryRunner = networkQueryRunner;
    	this.logger = logger;
    }

    private Text getErrorMessageWidget() {
    	return this.errorMessageWidget;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	Display display = new Display();
    	Shell shell = new Shell(display);
    	shell.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    	shell.setLayout(new GridLayout(1, true));
    	// TODO: Tabulate this stuff.
    	// TODO: Add field descriptions/help text.
    	createTableQueryControls(shell);
    	createNetworkQueryControls(shell);
    	Group otherControlsContainer = createOtherControls(shell);
    	this.errorMessageWidget = createErrorMessageControl(otherControlsContainer);
    	shell.pack();
    	shell.open();

    	while (!shell.isDisposed()) {
    		if (!display.readAndDispatch()) {
    			display.sleep();
    		}
    	}

    	display.dispose();

        return null;
    }

    public ProgressMonitor getProgressMonitor() {
    	return this.progressMonitor;
    }

    public void setProgressMonitor(ProgressMonitor progressMonitor) {
    	this.progressMonitor = progressMonitor;
    }

    // Table Query Controls

    private void createTableQueryControls(Shell shell) {
    	Group group = new Group(shell, SWT.NONE);
    	group.setText("Table Query");
    	group.setLayout(new GridLayout(2, true));
    	Text tableQueryTextField = createTableQueryTextField(group);
    	createTableQueryButton(group, tableQueryTextField);
    }

    private Text createTableQueryTextField(Composite container) {
    	createLabel(container, "Table Query");
    	int style = SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;

    	return createTextField(container, style, "", true, true);
    }

    private Button createTableQueryButton(Composite container, final Text tableQueryTextField) {
    	Button tableQueryButton = new Button(container, SWT.CENTER);
    	tableQueryButton.setText("Run Table Query");
    	GridData layoutData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
    	layoutData.horizontalSpan = 2;
    	layoutData.widthHint = CONTROL_WIDTH;
    	layoutData.heightHint = CONTROL_HEIGHT;
    	tableQueryButton.setLayoutData(layoutData);
    	tableQueryButton.addSelectionListener(new SelectionAdapter() {
    		public void widgetSelected(SelectionEvent selectionEvent) {
    			try {
    				getErrorMessageWidget().setText("");

    				String tableQuery = tableQueryTextField.getText();
    				Data[] tableData = runTableQuery(tableQuery);
    				handleData(tableData);
    			} catch (Exception e) {
    				getErrorMessageWidget().setText(e.getMessage());
    				DatabaseQueryRunnerAlgorithm.this.logger.log(
    					LogService.LOG_ERROR,
    					"Error running table query: \"" + e.getMessage() + "\"",
    					e);
    			}
    		}
    	});

    	return tableQueryButton;
    }

    // Network Query Controls

    private void createNetworkQueryControls(Shell shell) {
    	Group group = new Group(shell, SWT.NONE);
    	group.setText("Network Query");
    	group.setLayout(new GridLayout(2, true));
    	Text nodeQueryTextField = createQueryTextField(group);
    	Text edgeQueryTextField = createEdgeQueryTextField(group);
    	Text nodeIDColumnTextField = createNodeIDColumnTextField(group);
    	Text sourceIDColumnTextField = createSourceIDColumnTextField(group);
    	Text targetIDColumnTextField = createTargetIDColumn(group);
    	CheckBox directedField = createDirectedField(group);
    	createNetworkQueryButton(
    		group,
    		nodeQueryTextField,
    		edgeQueryTextField,
    		nodeIDColumnTextField,
    		sourceIDColumnTextField,
    		targetIDColumnTextField,
    		directedField);
    }

    private Text createQueryTextField(Composite container) {
    	createLabel(container, "Node Query");
    	int style = SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;

    	return createTextField(container, style, "", true, true);
    }

    private Text createEdgeQueryTextField(Composite container) {
    	createLabel(container, "Edge Query");
    	int style = SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;

    	return createTextField(container, style, "", true, true);
    }

    private Text createNodeIDColumnTextField(Composite container) {
    	createLabel(container, "Node ID Column");
    	int style = SWT.SINGLE | SWT.BORDER;

    	return createTextField(container, style, DEFAULT_NODE_ID_COLUMN, true, false);
    }

    private Text createSourceIDColumnTextField(Composite container) {
    	createLabel(container, "Source ID Column");
    	int style = SWT.SINGLE | SWT.BORDER;

    	return createTextField(container, style, DEFAULT_SOURCE_ID_COLUMN, true, false);
    }

    private Text createTargetIDColumn(Composite container) {
    	createLabel(container, "Target ID Column");
    	int style = SWT.SINGLE | SWT.BORDER;

    	return createTextField(container, style, DEFAULT_TARGET_ID_COLUMN, true, false);
    }

    private CheckBox createDirectedField(Composite container) {
    	createLabel(container, "Directed?");

    	CheckBox directedFieldButton = new CheckBox(container, SWT.CHECK);
    	GridData layoutData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
    	directedFieldButton.getButton().setLayoutData(layoutData);

    	return directedFieldButton;
    }

    private Button createNetworkQueryButton(
    		Composite container,
    		final Text nodeQueryTextField,
    		final Text edgeQueryTextField,
    		final Text nodeIDColumnTextField,
    		final Text sourceIDColumnTextField,
    		final Text targetIDColumnTextField,
    		final CheckBox directedField) {
    	Button networkQueryButton = new Button(container, SWT.CENTER);
    	networkQueryButton.setText("Run Network Query");
    	GridData layoutData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
    	layoutData.horizontalSpan = 2;
    	layoutData.widthHint = CONTROL_WIDTH;
    	layoutData.heightHint = CONTROL_HEIGHT;
    	networkQueryButton.setLayoutData(layoutData);
    	networkQueryButton.addSelectionListener(new SelectionAdapter() {
    		public void widgetSelected(SelectionEvent selectionEvent) {
    			try {
    				getErrorMessageWidget().setText("");

    				String nodeQuery = nodeQueryTextField.getText();
    				String edgeQuery = edgeQueryTextField.getText();
    				String idColumn = nodeIDColumnTextField.getText();
    				String sourceColumn = sourceIDColumnTextField.getText();
    				String targetColumn = targetIDColumnTextField.getText();
    				boolean directed = directedField.isSelected();
    				Data[] networkData = runNetworkQuery(
    					nodeQuery, edgeQuery, idColumn, sourceColumn, targetColumn, directed);
    				handleData(networkData);
    			} catch (Exception e) {
    				getErrorMessageWidget().setText(e.getMessage());
    				DatabaseQueryRunnerAlgorithm.this.logger.log(
    					LogService.LOG_ERROR,
    					"Error running network query: \"" + e.getMessage() + "\"",
    					e);
    			}
    		}
    	});

    	return networkQueryButton;
    }

    // Other Controls

    private Group createOtherControls(Shell shell) {
    	Group group = new Group(shell, SWT.NONE);
    	group.setText("Other");
    	group.setLayout(new GridLayout(2, true));

    	return group;
    }

    private Text createErrorMessageControl(Composite container) {
    	int style = SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
    	Text errorMessageControl = createTextField(
    		container, style, "", true, true, 2);
    	errorMessageControl.setEditable(false);
    	errorMessageControl.setBackground(new Color(container.getDisplay(), 255, 255, 255));
    	errorMessageControl.setForeground(new Color(container.getDisplay(), 255, 64, 64));

    	return errorMessageControl;
    }

    // Generic Controls

    private void createLabel(Composite container, String text) {
    	Label label = new Label(container, SWT.CENTER);
    	GridData labelLayoutData = new GridData(SWT.RIGHT, SWT.CENTER, true, true);
    	label.setLayoutData(labelLayoutData);
    	label.setText(text);
    }

    private Text createTextField(
    		Composite container,
    		int style,
    		String defaultValue,
    		boolean shouldUseWidthHint,
    		boolean shouldUseHeightHint) {
    	return createTextField(
    		container, style, defaultValue, shouldUseWidthHint, shouldUseHeightHint, 1);
    }

    private Text createTextField(
    		Composite container,
    		int style,
    		String defaultValue,
    		boolean shouldUseWidthHint,
    		boolean shouldUseHeightHint,
    		int horizontalSpan) {
    	final Text textField = new Text(container, style);

    	if (defaultValue != null) {
    		textField.setText(defaultValue);
    	}

    	textField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent focusEvent) {
				textField.selectAll();
			}

			public void focusLost(FocusEvent focusevent) {
				textField.clearSelection();
			}
    	});
    	textField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {}

			/*
			 * For some reason, the CTRL+A behavior to select all text in a text field doesn't
			 *  exist by default.  This is taking care of that.
			 * Also, SWT doesn't seem to provide key code constants for regular letters, though it
			 *  does for special keys (CTRL, ALT, etc.).
			 */
			public void keyReleased(KeyEvent keyEvent) {
				if ((keyEvent.stateMask == SWT.CTRL) && (keyEvent.keyCode == 'a')) {
    				textField.selectAll();
    			}
			}
    	});
    	GridData textLayoutData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
    	textLayoutData.horizontalSpan = horizontalSpan;

    	if (shouldUseWidthHint) {
    		textLayoutData.widthHint = CONTROL_WIDTH;
    	}

    	if (shouldUseHeightHint) {
    		textLayoutData.heightHint = CONTROL_HEIGHT;
    	}

    	textField.setLayoutData(textLayoutData);

    	return textField;
    }

    // Other Methods

    private Data[] runTableQuery(String tableQuery) throws Exception {
    	Dictionary<String, Object> parameters = new Hashtable<String, Object>();
    	parameters.put("query", tableQuery);

    	return AlgorithmUtilities.executeAlgorithm(
    		this.tableQueryRunner,
    		this.progressMonitor,
    		this.data,
    		parameters,
    		this.ciShellContext);
    }

    private Data[] runNetworkQuery(
    		String nodeQuery,
    		String edgeQuery,
    		String idColumn,
    		String sourceColumn,
    		String targetColumn,
    		boolean directed) throws Exception {
    	Dictionary<String, Object> parameters = new Hashtable<String, Object>();
    	parameters.put("node_query", nodeQuery);
    	parameters.put("edge_query", edgeQuery);
    	parameters.put("id_column", idColumn);
    	parameters.put("source_column", sourceColumn);
    	parameters.put("target_column", targetColumn);
    	parameters.put("directed", directed);

    	return AlgorithmUtilities.executeAlgorithm(
    		this.networkQueryRunner,
    		this.progressMonitor,
    		this.data,
    		parameters,
    		this.ciShellContext);
    }

    private void handleData(Data[] data) {
    	if ((data == null) || (data.length == 0)) {
    		return;
    	}

    	for (Data singleData : data) {
    		this.dataManager.addData(singleData);
    	}
    }
}