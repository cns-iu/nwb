package edu.iu.sci2.database.star.gui.builder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import edu.iu.sci2.database.star.gui.ColumnDescriptor;

public class ColumnHeaderWidget extends Composite {
	private String name;
	private Label label;
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
		this.name = columnDescriptor.getName();
		setBackground(Utilities.backgroundColor(getDisplay()));
		setLayout(createLayout());
		this.label = createLabel(this, columnDescriptor);
		this.inputField = createInputField(this, columnTypeOptions, defaultOptionIndex);
	}

	public String getColumnName() {
		return this.name;
	}

	public Label getLabel() {
		return this.label;
	}

	public Combo getInputField() {
		return this.inputField;
	}

	public String getType() {
		return this.inputField.getItem(this.inputField.getSelectionIndex());
	}

	private static GridLayout createLayout() {
		GridLayout layout = new GridLayout(2, false);

		return layout;
	}

	private static Label createLabel(Composite parent, ColumnDescriptor columnDescriptor) {
		Label label = new Label(parent, SWT.NONE);
		label.setBackground(Utilities.backgroundColor(parent.getDisplay()));
		label.setLayoutData(createLabelLayoutData());
		label.setText("Column " + columnDescriptor.getName());

		return label;
	}

	private static Combo createInputField(
			Composite parent, String[] columnTypeOptions, int defaultOptionIndex) {
		Combo inputField = new Combo(parent, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		inputField.setLayoutData(createInputFieldLayoutData());
		inputField.setItems(columnTypeOptions);
		inputField.select(defaultOptionIndex);

		return inputField;
	}

	private static GridData createLabelLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, true, true);

		return layoutData;
	}

	private static GridData createInputFieldLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, true);

		return layoutData;
	}
}