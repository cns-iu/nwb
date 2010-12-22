package edu.iu.sci2.database.star.gui.builder;

import org.cishell.utility.swt.GUIBuilderUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public class ColumnWidget extends Composite {
	public static final int ERROR_MESSAGE_LAYOUT_DATA_MAXIMUM_WIDTH = 125;

	private ColumnDescriptor columnDescriptor;
//	private Text errorMessageField;
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
		setLayout(createLayout());
		this.columnDescriptor = columnDescriptor;
		this.header = createHeader(this, columnDescriptor, columnTypeOptions, defaultOptionIndex);
//		this.errorMessageField = createErrorMessageField(this);
		this.properties = createProperties(this);
	}

	public ColumnDescriptor getColumnDescriptor() {
		return this.columnDescriptor;
	}

//	public Text getErrorMessageField() {
//		return this.errorMessageField;
//	}

	public ColumnHeaderWidget getHeader() {
		return this.header;
	}

	public ColumnPropertiesWidget getProperties() {
		return this.properties;
	}

	private static GridLayout createLayout() {
		GridLayout layout = new GridLayout(1, false);
		GUIBuilderUtilities.clearMargins(layout);
		GUIBuilderUtilities.clearSpacing(layout);

		return layout;
	}

//	private static Text createErrorMessageField(Composite parent) {
//		Text errorMessageField = //new Text(parent, SWT.READ_ONLY);
//			new Text(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY);
//		errorMessageField.setForeground(new Color(parent.getDisplay(), 255, 32, 32));
//		errorMessageField.setLayoutData(createErrorMessageLabelLayoutData());
//		errorMessageField.setText("This is filler text FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF\nFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFUUUUUUUUUUUU UUUUUUUUUUUUU UUUUUUUUUUUU UUUUUUUUUUU UUUUUUUUUUUUUUUUUUUU UUUUUUUUUUUUUUUU UUUUUUUUUUUU UUUUUUUUUUUUUUUUU UUUUUUUUUUUUUUUUUUU UUUUUUU UUUUUUUUUUUUUUUUUUUUUUUUUUU.");
//
//		return null;
//	}

	private static ColumnHeaderWidget createHeader(
			Composite parent,
			ColumnDescriptor columnDescriptor,
			String[] columnTypeOptions,
			int defaultOptionIndex) {
		ColumnHeaderWidget header = new ColumnHeaderWidget(
			parent, columnDescriptor, columnTypeOptions, defaultOptionIndex);
		header.setLayoutData(createColumnHeaderLayoutData());

		return header;
	}

	private static ColumnPropertiesWidget createProperties(Composite parent) {
		ColumnPropertiesWidget properties = new ColumnPropertiesWidget(parent);
		properties.setLayoutData(createPropertiesLayoutData());

		return properties;
	}

//	private static GridData createErrorMessageLabelLayoutData() {
//		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
//		layoutData.verticalSpan = 2;
//		layoutData.widthHint = ERROR_MESSAGE_LAYOUT_DATA_MAXIMUM_WIDTH;
//
//		return layoutData;
//	}

	private static GridData createColumnHeaderLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, false, true);

		return layoutData;
	}

	private static GridData createPropertiesLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, false, true);

		return layoutData;
	}
}