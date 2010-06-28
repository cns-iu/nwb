package edu.iu.sci2.database.star.gui.builder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class BottomWidgetThingy extends Composite {
	public static final String INSTRUCTIONS_LABEL_TEXT =
		"These are instructions for what to do in this GUI.  This is currently filler text.  " +
		"What about that Lorum Ipsum thingy?";
	public static final String FINISHED_BUTTON_TEXT = "I'm Finished!";

	private Label instructionsLabel;
	private Button finishedButton;

	public BottomWidgetThingy(Composite parent) {
		super(parent, SWT.BORDER);
		setLayout(createLayout());
		this.instructionsLabel = createInstructionsLabel(this);
		this.finishedButton = createFinishedButton(this);
	}

	private static GridLayout createLayout() {
		GridLayout layout = new GridLayout(1, true);

		return layout;
	}

	private static Label createInstructionsLabel(Composite parent) {
		Label instructionsLabel = new Label(parent, SWT.BORDER | SWT.LEFT | SWT.WRAP);
		instructionsLabel.setLayoutData(createInstructionsLabelLayoutData());
		instructionsLabel.setText(INSTRUCTIONS_LABEL_TEXT);

		return instructionsLabel;
	}

	private static Button createFinishedButton(Composite parent) {
		Button finishedButton = new Button(parent, SWT.BORDER | SWT.PUSH);
		finishedButton.setLayoutData(createFinishedButtonLayoutData());
		finishedButton.setText(FINISHED_BUTTON_TEXT);

		return finishedButton;
	}

	private static GridData createInstructionsLabelLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);

		return layoutData;
	}

	private static GridData createFinishedButtonLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);

		return layoutData;
	}
}