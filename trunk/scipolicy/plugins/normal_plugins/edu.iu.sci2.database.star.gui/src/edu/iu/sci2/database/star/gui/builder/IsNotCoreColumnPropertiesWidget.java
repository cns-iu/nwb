package edu.iu.sci2.database.star.gui.builder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import edu.iu.cns.shared.utilities.swt.CheckBox;

public class IsNotCoreColumnPropertiesWidget extends ExpandableComposite {
	public static final String SEPARATOR_INPUT_FIELD_LABEL = "Specify Value Separator:";
	public static final String SINGLE_VALUES_INPUT_FIELD_LABEL =
		"Does this column contain only single values?";

	public static final String DEFAULT_SEPARATOR = ",";

	private Composite propertiesArea;
	private Label separatorLabel;
	private Text separatorInputField;
	private Label singleValuesLabel;
	private CheckBox singleValuesInputField;

	public IsNotCoreColumnPropertiesWidget(final Composite parent) {
		super(parent, SWT.NONE, ExpandableComposite.COMPACT | ExpandableComposite.NO_TITLE);
		setBackground(Utilities.backgroundColor(getDisplay()));
		this.propertiesArea = createPropertiesArea(this);
		this.separatorLabel = createSeparatorLabel(this.propertiesArea);
		this.separatorInputField = createSeparatorInputField(this.propertiesArea);
		this.singleValuesLabel = createSingleValuesLabel(this.propertiesArea);
		this.singleValuesInputField = createSingleValuesInputField(this.propertiesArea);

//		setExpanded(false);
		setClient(this.propertiesArea);
		addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent event) {
				parent.pack(true);
			}
		});
	}

	public Label getSeparatorLabel() {
		return this.separatorLabel;
	}

	public Text getSeparatorInputField() {
		return this.separatorInputField;
	}

	public Label getSingleValuesLabel() {
		return this.singleValuesLabel;
	}

	public CheckBox getSingleValuesInputField() {
		return this.singleValuesInputField;
	}

	private static Composite createPropertiesArea(Composite parent) {
		Composite propertiesArea = new Composite(parent, SWT.NONE);
		propertiesArea.setBackground(Utilities.backgroundColor(parent.getDisplay()));
		propertiesArea.setLayout(createPropertiesAreaLayout());

		return propertiesArea;
	}

	private static Label createSeparatorLabel(Composite parent) {
		Label separatorLabel = new Label(parent, SWT.NONE);
		separatorLabel.setBackground(Utilities.backgroundColor(parent.getDisplay()));
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

	private static Label createSingleValuesLabel(Composite parent) {
		Label singleValuesLabel = new Label(parent, SWT.NONE);
		singleValuesLabel.setBackground(Utilities.backgroundColor(parent.getDisplay()));
		singleValuesLabel.setLayoutData(createSingleValuesLabelLayoutData());
		singleValuesLabel.setText(SINGLE_VALUES_INPUT_FIELD_LABEL);

		return singleValuesLabel;
	}

	private static CheckBox createSingleValuesInputField(Composite parent) {
		CheckBox singleValuesInputField = new CheckBox(parent, SWT.CHECK);
		singleValuesInputField.getButton().setBackground(
			Utilities.backgroundColor(parent.getDisplay()));
		singleValuesInputField.getButton().setLayoutData(createSingleValuesInputFieldLayoutData());

		return singleValuesInputField;
	}

	private static GridLayout createPropertiesAreaLayout() {
		GridLayout layout = new GridLayout(2, false);
		layout.marginTop = 0;

		return layout;
	}

	private static GridData createSeparatorLabelLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, true);

		return layoutData;
	}

	private static GridData createSeparatorInputFieldLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, true);

		return layoutData;
	}

	private static GridData createSingleValuesLabelLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, true);

		return layoutData;
	}

	private static GridData createSingleValuesInputFieldLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, true);

		return layoutData;
	}
}