package edu.iu.sci2.database.star.gui.builder;

import org.cishell.utility.swt.GUIBuilderUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CoreEntityNameWidget extends Composite {
	public static final int HEIGHT = 30;
	public static final String LABEL_TEXT = "Core Entity Name:";

	private Label label;
	private Text inputField;

	public CoreEntityNameWidget(Composite parent, String defaultValue) {
		super(parent, SWT.BORDER);
		setLayout(createLayout());
		this.label = createLabel(this);
		this.inputField = createInputField(this, defaultValue);
	}

	public Label getLabel() {
		return this.label;
	}

	public Text getInputField() {
		return this.inputField;
	}

	public String getCoreEntityName() {
		return this.inputField.getText();
	}

	public void setCoreEntityName(String coreEntityName) {
		this.inputField.setText(coreEntityName);
	}

	private static GridLayout createLayout() {
		GridLayout layout = new GridLayout(2, true);
		GUIBuilderUtilities.clearSpacing(layout);

		return layout;
	}

	private static Label createLabel(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(createLabelLayoutData());
		label.setText(LABEL_TEXT);

		return label;
	}

	private static Text createInputField(Composite parent, String defaultValue) {
		Text text = new Text(parent, SWT.BORDER);
		text.setLayoutData(createInputTextLayoutData());
		text.setText(defaultValue);

		return text;
	}

	private static GridData createLabelLayoutData() {
		GridData layoutData = new GridData();

		return layoutData;
	}

	private static GridData createInputTextLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, true);
		layoutData.heightHint = HEIGHT;

		return layoutData;
	}
}