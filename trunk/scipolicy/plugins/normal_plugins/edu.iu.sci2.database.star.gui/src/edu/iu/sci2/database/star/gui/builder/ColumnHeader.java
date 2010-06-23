package edu.iu.sci2.database.star.gui.builder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ColumnHeader {
	private Label lineBreak;
	private Label label;
	private Combo inputField;
	
	public ColumnHeader(Composite parent, String columnName, String[] columnTypeOptions) {
		this(parent, columnName, columnTypeOptions, 0);
	}

	public ColumnHeader(
			Composite parent,
			String columnName,
			String[] columnTypeOptions,
			int defaultOptionIndex) {
		this.lineBreak = createLineBreak(parent);
		this.label = createLabel(parent, columnName);
		this.inputField = createInputField(parent, columnTypeOptions, defaultOptionIndex);
	}

	public Label getLineBreak() {
		return this.lineBreak;
	}

	public Label getLabel() {
		return this.label;
	}

	public Combo getInputField() {
		return this.inputField;
	}

	private static Label createLineBreak(Composite parent) {
		Label lineBreak = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		lineBreak.setLayoutData(createLineBreakLayoutData());

		return lineBreak;
	}

	private static Label createLabel(Composite parent, String columnName) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(createLabelLayoutData());
		label.setText("Column " + columnName);

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

	private static GridData createLineBreakLayoutData() {
		GridData layoutData = new GridData();
//		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalSpan = 2;
		layoutData.horizontalAlignment = SWT.FILL;

		return layoutData;
	}

	private static GridData createLabelLayoutData() {
		GridData layoutData = new GridData();

		return layoutData;
	}

	private static GridData createInputFieldLayoutData() {
		GridData layoutData = new GridData();

		return layoutData;
	}
}