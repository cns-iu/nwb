package edu.iu.sci2.database.star.gui.builder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class UserFinishedWidget extends Composite {

	public static final String FINISHED_BUTTON_TEXT = "I'm Finished!";

	private Label instructionsLabel;
	private Button finishedButton;

	public UserFinishedWidget(Composite parent) {
		super(parent, SWT.BORDER);
		setLayout(createLayout());
		this.finishedButton = createFinishedButton(this);
	}

	public Label getInstructionsLabel() {
		return this.instructionsLabel;
	}

	public Button getFinishedButton() {
		return this.finishedButton;
	}

	private static GridLayout createLayout() {
		GridLayout layout = new GridLayout(1, false);

		return layout;
	}

	private static Button createFinishedButton(Composite parent) {
		Button finishedButton = new Button(parent, SWT.BORDER | SWT.PUSH);
		finishedButton.setLayoutData(createFinishedButtonLayoutData());
		finishedButton.setText(FINISHED_BUTTON_TEXT);

		return finishedButton;
	}

	private static GridData createFinishedButtonLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);

		return layoutData;
	}
}