package edu.iu.sci2.database.star.gui.builder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import edu.iu.sci2.database.star.gui.ColumnDescriptor;

public class ColumnWidget extends Composite {
	public static final int ERROR_MESSAGE_LAYOUT_DATA_MINIMUM_WIDTH = 50;

	private ColumnDescriptor columnDescriptor;
	private Label errorMessageLabel;
	private ColumnHeaderWidget header;
	private ColumnPropertiesWidget properties;

	public ColumnWidget(
			ColumnListWidget columnList,
			ColumnDescriptor columnDescriptor,
			String[] columnTypeOptions) {
		this(columnList, columnDescriptor, columnTypeOptions, 0);
	}

	public ColumnWidget(
			ColumnListWidget columnList,
			ColumnDescriptor columnDescriptor,
			String[] columnTypeOptions,
			int defaultOptionIndex) {
		super(columnList.getColumnArea(), SWT.NONE);
		setBackground(Utilities.backgroundColor(getDisplay()));
//		setBackground(new Color(getDisplay(), 12, 65, 255));
		setLayout(createLayout());
		this.columnDescriptor = columnDescriptor;
		this.header = createHeader(this, columnDescriptor, columnTypeOptions, defaultOptionIndex);
		this.errorMessageLabel = createErrorMessageLabel(this);
		this.properties = createProperties(this, columnList);
	}

	public ColumnDescriptor getColumnDescriptor() {
		return this.columnDescriptor;
	}

	public Label getErrorMessageLabel() {
		return this.errorMessageLabel;
	}

	public ColumnHeaderWidget getHeader() {
		return this.header;
	}

	public ColumnPropertiesWidget getProperties() {
		return this.properties;
	}

	private static GridLayout createLayout() {
		GridLayout layout = new GridLayout(2, false);
		Utilities.clearMargins(layout);
		Utilities.clearSpacing(layout);

		return layout;
	}

	private static Label createErrorMessageLabel(Composite parent) {
			Label errorMessageField = new Label(parent, SWT.LEFT | SWT.WRAP);
			errorMessageField.setBackground(Utilities.backgroundColor(parent.getDisplay()));
			errorMessageField.setForeground(new Color(parent.getDisplay(), 255, 32, 32));
			errorMessageField.setLayoutData(createErrorMessageLabelLayoutData());
			errorMessageField.setText("This is filler text.");

			return errorMessageField;
	}

	private static ColumnHeaderWidget createHeader(
			Composite parent,
			ColumnDescriptor columnDescriptor,
			String[] columnTypeOptions,
			int defaultOptionIndex) {
		ColumnHeaderWidget header =
			new ColumnHeaderWidget(parent, columnDescriptor, columnTypeOptions, defaultOptionIndex);
		header.setLayoutData(createColumnHeaderLayoutData());

		return header;
	}

	private static ColumnPropertiesWidget createProperties(
			Composite parent, ColumnListWidget columnList) {
		ColumnPropertiesWidget properties = new ColumnPropertiesWidget(parent, columnList);
		properties.setLayoutData(createPropertiesLayoutData());

		return properties;
	}

	private static GridData createErrorMessageLabelLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		layoutData.verticalSpan = 2;
//		layoutData.widthHint = ERROR_MESSAGE_LAYOUT_DATA_MINIMUM_WIDTH;

		return layoutData;
	}

	private static GridData createColumnHeaderLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, false, true);

		return layoutData;
	}

	private static GridData createPropertiesLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, false, true);

		return layoutData;
	}
}