package edu.iu.sci2.database.star.gui.builder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ColumnPropertiesWidget extends Composite {
	private IsCoreColumnWidget isCoreColumn;
//	private IsCoreColumnPropertiesWidget isCoreColumnProperties;
	private IsNotCoreColumnPropertiesWidget isNotCoreColumnProperties;

	public ColumnPropertiesWidget(Composite parent, ColumnListWidget columnList) {
		super(parent, SWT.NONE);
		setBackground(Utilities.backgroundColor(getDisplay()));
		setLayout(createLayout());
		this.isCoreColumn = createIsCoreColumn(this);
//		this.isCoreColumnProperties = createIsCoreColumnProperties(this, this.isCoreColumn);
		this.isNotCoreColumnProperties = createIsNotCoreColumnProperties(this, this.isCoreColumn);
	}

	public IsCoreColumnWidget getIsCoreColumn() {
			return this.isCoreColumn;
	}

	public IsNotCoreColumnPropertiesWidget getIsNotCoreColumnProperties() {
		return this.isNotCoreColumnProperties;
	}

	private static IsCoreColumnWidget createIsCoreColumn(Composite parent) {
		IsCoreColumnWidget isCoreColumn = new IsCoreColumnWidget(parent);
		isCoreColumn.setLayoutData(createIsCoreColumnLayoutData());

		return isCoreColumn;
	}

	private static IsNotCoreColumnPropertiesWidget createIsNotCoreColumnProperties(
			final ColumnPropertiesWidget parent, IsCoreColumnWidget isCoreColumn) {
		final IsNotCoreColumnPropertiesWidget isNotCoreColumnProperties =
			new IsNotCoreColumnPropertiesWidget(parent);
		final GridData layoutData = createIsNotCoreColumnPropertiesLayoutData();
		isNotCoreColumnProperties.setLayoutData(layoutData);

		Button yesButton = isCoreColumn.getYesButton();
		Button noButton = isCoreColumn.getNoButton();
		yesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				selected(event);
			}

			public void widgetSelected(SelectionEvent event) {
				selected(event);
			}

			private void selected(SelectionEvent event) {
				isNotCoreColumnProperties.setExpanded(false);
//				layoutData.exclude = true;
//				Shell shell = parent.getShell();
//				shell.layout(true);
//				shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
		noButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				selected(event);
			}

			public void widgetSelected(SelectionEvent event) {
				selected(event);
			}

			private void selected(SelectionEvent event) {
				isNotCoreColumnProperties.setExpanded(true);
//				layoutData.exclude = false;
//				Shell shell = parent.getShell();
//				shell.layout(true);
//				shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});

		return isNotCoreColumnProperties;
	}

	private static GridLayout createLayout() {
		GridLayout layout = new GridLayout(1, false);
//		Utilities.clearMargins(layout);

		return layout;
	}

	private static GridData createIsCoreColumnLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, false);

		return layoutData;
	}

	private static GridData createIsNotCoreColumnPropertiesLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, true);
//		layoutData.exclude = true;

		return layoutData;
	}
}
