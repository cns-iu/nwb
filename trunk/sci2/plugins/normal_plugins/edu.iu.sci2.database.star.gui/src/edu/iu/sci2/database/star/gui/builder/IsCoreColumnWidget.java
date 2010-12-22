package edu.iu.sci2.database.star.gui.builder;

import org.cishell.utility.swt.GUIBuilderUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class IsCoreColumnWidget extends Composite {
	public static final String LABEL_TEXT = "Is Core Column?";
	public static final String YES_BUTTON_TEXT = "Include this column in the core entity table?";
	public static final String NO_BUTTON_TEXT =
		"Create a separate leaf table based on this column?";

	private boolean isCoreColumn = true;
	private Button yesButton;
	private Button noButton;

	public IsCoreColumnWidget(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(createLayout());
		this.yesButton = createYesButton(this);
		this.noButton = createNoButton(this);
	}

	public boolean isCoreColumn() {
		return this.isCoreColumn;
	}

	public Button getYesButton() {
		return this.yesButton;
	}

	public Button getNoButton() {
		return this.noButton;
	}

	public void setIsCoreColumn(boolean isCoreColumn) {
		this.isCoreColumn = isCoreColumn;

		if (isCoreColumn) {
			this.yesButton.setSelection(true);
			this.noButton.setSelection(false);
		} else {
			this.yesButton.setSelection(false);
			this.noButton.setSelection(true);
		}
	}

	private static Button createYesButton(final IsCoreColumnWidget parent) {
		final Button yesButton = new Button(parent, SWT.RADIO);
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
		GridLayout layout = new GridLayout(1, false);
		GUIBuilderUtilities.clearMargins(layout);

		return layout;
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