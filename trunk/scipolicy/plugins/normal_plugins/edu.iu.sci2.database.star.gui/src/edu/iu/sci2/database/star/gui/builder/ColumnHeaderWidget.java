package edu.iu.sci2.database.star.gui.builder;

import org.cishell.utilities.ArrayUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public class ColumnHeaderWidget extends Composite {
	public static final String DATA_TYPE_LABEL_TEXT =
		"What type of data does this column contain?";

	private String[] columnTypeOptions;
	private String name;
	private Label columnLabel;
	private Label dataTypeLabel;
	private Combo inputField;
	
	public ColumnHeaderWidget(
			Composite parent, ColumnDescriptor columnDescriptor, String[] columnTypeOptions) {
		this(parent, columnDescriptor, columnTypeOptions, 0);
	}

	public ColumnHeaderWidget(
			Composite parent,
			ColumnDescriptor columnDescriptor,
			String[] columnTypeOptions,
			int defaultOptionIndex) {
		super(parent, SWT.NONE);
		this.columnTypeOptions = columnTypeOptions;
		this.name = columnDescriptor.getName();
		setLayout(createLayout());
		this.columnLabel = createColumnLabel(this, columnDescriptor);
		this.dataTypeLabel = createDataTypeLabel(this);
		this.inputField = createInputField(this, this.columnTypeOptions, defaultOptionIndex);
	}

	public String getColumnName() {
		return this.name;
	}

	public Label getColumnLabel() {
		return this.columnLabel;
	}

	public Label getDataTypeLabel() {
		return this.dataTypeLabel;
	}

	public Combo getInputField() {
		return this.inputField;
	}

	public String getType() {
		return this.inputField.getItem(this.inputField.getSelectionIndex());
	}

	public void setType(String type) {
		this.inputField.select(ArrayUtilities.indexOf(this.columnTypeOptions, type));
	}

	private static GridLayout createLayout() {
		GridLayout layout = new GridLayout(2, false);

		return layout;
	}

	private static Label createColumnLabel(Composite parent, ColumnDescriptor columnDescriptor) {
		// TODO: Make this actually be bold. :-(
		Label columnLabel = new Label(parent, SWT.BOLD | SWT.BORDER | SWT.CENTER);
		columnLabel.setLayoutData(createColumnLabelLayoutData());
		columnLabel.setText("Column \"" + columnDescriptor.getName() + "\"");

		return columnLabel;
	}

	private static Label createDataTypeLabel(Composite parent) {
		Label dataTypeLabel = new Label(parent, SWT.NONE);
		dataTypeLabel.setLayoutData(createDataTypeLabelLayoutData());
		dataTypeLabel.setText(DATA_TYPE_LABEL_TEXT);

		return dataTypeLabel;
	}

	private static Combo createInputField(
			Composite parent, String[] columnTypeOptions, int defaultOptionIndex) {
		Combo inputField = new Combo(parent, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		inputField.setLayoutData(createInputFieldLayoutData());
		inputField.setItems(columnTypeOptions);
		inputField.select(defaultOptionIndex);

		return inputField;
	}

	private static GridData createColumnLabelLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, true);
		layoutData.horizontalSpan = 2;

		return layoutData;
	}

	private static GridData createDataTypeLabelLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, true, true);

		return layoutData;
	}

	private static GridData createInputFieldLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, true);

		return layoutData;
	}
}