package edu.iu.sci2.database.star.gui.builder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import edu.iu.cns.shared.utilities.swt.CheckBox;

public class NonCoreColumnPropertiesWidget extends ExpandableComposite {
	public static final String MERGE_IDENTICAL_VALUES_LABEL =
		"Should the values in this column be merged if identical?";
	public static final String MULTI_VALUED_FIELD_INPUT_FIELD_LABEL =
		"Does this column contain multi-valued fields?";
	public static final String SEPARATOR_INPUT_FIELD_LABEL =
		"What character(s) separate(s) the values?";

	public static final int SEPARATOR_INPUT_FIELD_WIDTH = 10;
	public static final String DEFAULT_SEPARATOR = ",";

	private Composite propertiesArea;
	private Label mergeIdenticalValuesLabel;
	private CheckBox mergeIdenticalValuesInputField;
	private Label multiValuedFieldLabel;
	private CheckBox multiValuedFieldInputField;
	private Label separatorLabel;
	private Text separatorInputField;

	public NonCoreColumnPropertiesWidget(final Composite parent) {
		super(parent, SWT.NONE, ExpandableComposite.COMPACT | ExpandableComposite.NO_TITLE);
		this.propertiesArea = createPropertiesArea(this);
		this.mergeIdenticalValuesLabel = createMergeIdenticalValuesLabel(this.propertiesArea);
		this.mergeIdenticalValuesInputField =
			createMergeIdenticalValuesInputField(this.propertiesArea);
		this.multiValuedFieldLabel = createMultiValuedFieldLabel(this.propertiesArea);
		this.multiValuedFieldInputField = createMultiValuedFieldInputField(this.propertiesArea);
		this.separatorLabel = createSeparatorLabel(this.propertiesArea);
		this.separatorInputField = createSeparatorInputField(this.propertiesArea);

		setClient(this.propertiesArea);
		addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent event) {
				parent.pack(true);
			}
		});

		this.multiValuedFieldInputField.toggle();
		this.multiValuedFieldInputField.getButton().addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				selected(event);
			}

			public void widgetSelected(SelectionEvent event) {
				selected(event);
			}

			private void selected(SelectionEvent event) {
				boolean isSelected =
					NonCoreColumnPropertiesWidget.this.multiValuedFieldInputField.isSelected();
				NonCoreColumnPropertiesWidget.this.separatorLabel.setEnabled(isSelected);
				NonCoreColumnPropertiesWidget.this.separatorInputField.setEnabled(isSelected);
			}
		});
	}

	public Label getMergeIdenticalValuesLabel() {
		return this.mergeIdenticalValuesLabel;
	}

	public CheckBox getMergeIdenticalValuesInputField() {
		return this.mergeIdenticalValuesInputField;
	}

	public Label getMultiValuedFieldLabel() {
		return this.multiValuedFieldLabel;
	}

	public CheckBox getMultiValuedFieldInputField() {
		return this.multiValuedFieldInputField;
	}

	public Label getSeparatorLabel() {
		return this.separatorLabel;
	}

	public Text getSeparatorInputField() {
		return this.separatorInputField;
	}

	public void setMergeIdenticalValues(boolean mergeIdenticalValues) {
		if (this.mergeIdenticalValuesInputField.isSelected() != mergeIdenticalValues) {
			this.mergeIdenticalValuesInputField.toggle();
		}
	}

	public void setIsMultiValued(boolean isMultiValued) {
		if (this.multiValuedFieldInputField.isSelected() != isMultiValued) {
			this.multiValuedFieldInputField.toggle();
		}
	}

	public void setSeparator(String separator) {
		this.separatorInputField.setText(separator);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		this.propertiesArea.setEnabled(isEnabled() && enabled);
		this.mergeIdenticalValuesLabel.setEnabled(isEnabled() && enabled);
		this.mergeIdenticalValuesInputField.getButton().setEnabled(isEnabled() && enabled);
		this.multiValuedFieldLabel.setEnabled(isEnabled() && enabled);
		this.multiValuedFieldInputField.getButton().setEnabled(isEnabled() && enabled);
		this.separatorLabel.setEnabled(
			isEnabled() && this.multiValuedFieldInputField.isSelected());
		this.separatorInputField.setEnabled(
			isEnabled() && this.multiValuedFieldInputField.isSelected());
	}

	private static Composite createPropertiesArea(Composite parent) {
		Composite propertiesArea = new Composite(parent, SWT.NONE);
		propertiesArea.setLayout(createPropertiesAreaLayout());

		return propertiesArea;
	}

	private static Label createMergeIdenticalValuesLabel(Composite parent) {
		Label mergeIdenticalValuesLabel = new Label(parent, SWT.NONE);
		mergeIdenticalValuesLabel.setLayoutData(createMergeIdenticalValuesLabelLayoutData());
		mergeIdenticalValuesLabel.setText(MERGE_IDENTICAL_VALUES_LABEL);

		return mergeIdenticalValuesLabel;
	}

	private static CheckBox createMergeIdenticalValuesInputField(Composite parent) {
		CheckBox mergeIdenticalValuesInputField = new CheckBox(parent, SWT.CHECK);
		mergeIdenticalValuesInputField.getButton().setLayoutData(
			createMergeIdenticalValuesLayoutData());

		return mergeIdenticalValuesInputField;
	}

	private static Label createMultiValuedFieldLabel(Composite parent) {
		Label multiValuedFieldLabel = new Label(parent, SWT.NONE);
		multiValuedFieldLabel.setLayoutData(createMultiValuedFieldLabelLayoutData());
		multiValuedFieldLabel.setText(MULTI_VALUED_FIELD_INPUT_FIELD_LABEL);

		return multiValuedFieldLabel;
	}

	private static CheckBox createMultiValuedFieldInputField(Composite parent) {
		CheckBox multiValuedFieldInputField = new CheckBox(parent, SWT.CHECK);
		multiValuedFieldInputField.getButton().setLayoutData(
			createMultiValuedFieldInputFieldLayoutData());

		return multiValuedFieldInputField;
	}

	private static Label createSeparatorLabel(Composite parent) {
		Label separatorLabel = new Label(parent, SWT.NONE);
		separatorLabel.setLayoutData(createSeparatorLabelLayoutData());
		separatorLabel.setText(SEPARATOR_INPUT_FIELD_LABEL);

		return separatorLabel;
	}

	private static Text createSeparatorInputField(Composite parent) {
		Text separatorInputField = new Text(parent, SWT.BORDER);
		separatorInputField.setLayoutData(createSeparatorInputFieldLayoutData());
		separatorInputField.setText(DEFAULT_SEPARATOR);

		return separatorInputField;
	}

	private static GridLayout createPropertiesAreaLayout() {
		GridLayout layout = new GridLayout(2, false);
		layout.marginTop = 0;
		layout.marginLeft = 25;

		return layout;
	}

	private static GridData createMergeIdenticalValuesLabelLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, true);

		return layoutData;
	}

	private static GridData createMergeIdenticalValuesLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, true);

		return layoutData;
	}

	private static GridData createMultiValuedFieldLabelLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, true);

		return layoutData;
	}

	private static GridData createMultiValuedFieldInputFieldLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, true);

		return layoutData;
	}

	private static GridData createSeparatorLabelLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, true);

		return layoutData;
	}

	private static GridData createSeparatorInputFieldLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, true);
		layoutData.widthHint = SEPARATOR_INPUT_FIELD_WIDTH;

		return layoutData;
	}
}