package edu.iu.sci2.database.star.gui.builder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class IsCoreColumnWidget extends Composite {
	public static final String LABEL_TEXT = "Is Core Column?";
	public static final String YES_BUTTON_TEXT = "Yes";
	public static final String NO_BUTTON_TEXT = "No";

	private boolean isCoreColumn = true;
	private Label label;
	private Button yesButton;
	private Button noButton;

	public IsCoreColumnWidget(Composite parent) {
		super(parent, SWT.NONE);
		setBackground(Utilities.backgroundColor(getDisplay()));
		setLayout(createLayout());
		this.label = createLabel(this);
		this.yesButton = createYesButton(this);
		this.noButton = createNoButton(this);
	}

	public boolean isCoreColumn() {
		return this.isCoreColumn;
	}

	public Label getLabel() {
		return this.label;
	}

	public Button getYesButton() {
		return this.yesButton;
	}

	public Button getNoButton() {
		return this.noButton;
	}

	private static Label createLabel(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setBackground(Utilities.backgroundColor(parent.getDisplay()));
		label.setLayoutData(createLabelLayoutData());
		label.setText(LABEL_TEXT);

		return label;
	}

	private static Button createYesButton(final IsCoreColumnWidget parent) {
		final Button yesButton = new Button(parent, SWT.RADIO);
		yesButton.setBackground(Utilities.backgroundColor(parent.getDisplay()));
		yesButton.setLayoutData(createYesButtonLayoutData());
		yesButton.setText(YES_BUTTON_TEXT);
		yesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				selected(event);
			}

			public void widgetSelected(SelectionEvent event) {
				selected(event);
			}

			private void selected(SelectionEvent event) {
				if (!parent.isCoreColumn) {
					parent.getNoButton().setSelection(false);
					parent.isCoreColumn = true;
				}
			}
		});
		yesButton.setSelection(true);

		return yesButton;
	}

	private static Button createNoButton(final IsCoreColumnWidget parent) {
		final Button noButton = new Button(parent, SWT.RADIO);
		noButton.setBackground(Utilities.backgroundColor(parent.getDisplay()));
		noButton.setLayoutData(createNoButtonLayoutData());
		noButton.setText(NO_BUTTON_TEXT);
		noButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				selected(event);
			}

			public void widgetSelected(SelectionEvent event) {
				selected(event);
			}

			private void selected(SelectionEvent event) {
				if (parent.isCoreColumn) {
					parent.getYesButton().setSelection(false);
					parent.isCoreColumn = false;
				}
			}
		});

		return noButton;
	}

	private static GridLayout createLayout() {
		GridLayout layout = new GridLayout(3, false);
		Utilities.clearMargins(layout);

		return layout;
	}

	private static GridData createLabelLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, false);

		return layoutData;
	}

	private static GridData createYesButtonLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, false);

		return layoutData;
	}

	private static GridData createNoButtonLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, false);

		return layoutData;
	}
}