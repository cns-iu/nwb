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
	private NonCoreColumnPropertiesWidget coreNonColumnProperties;

	public ColumnPropertiesWidget(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(createLayout());
		this.isCoreColumn = createIsCoreColumn(this);
		this.coreNonColumnProperties = createNonCoreColumnProperties(this, this.isCoreColumn);
	}

	public IsCoreColumnWidget getIsCoreColumn() {
			return this.isCoreColumn;
	}

	public NonCoreColumnPropertiesWidget getNonCoreColumnProperties() {
		return this.coreNonColumnProperties;
	}

	private static IsCoreColumnWidget createIsCoreColumn(Composite parent) {
		IsCoreColumnWidget isCoreColumn = new IsCoreColumnWidget(parent);
		isCoreColumn.setLayoutData(createIsCoreColumnLayoutData());

		return isCoreColumn;
	}

	private static NonCoreColumnPropertiesWidget createNonCoreColumnProperties(
			final ColumnPropertiesWidget parent, IsCoreColumnWidget isCoreColumn) {
		final NonCoreColumnPropertiesWidget nonCoreColumnProperties =
			new NonCoreColumnPropertiesWidget(parent);
		final GridData layoutData = createCoreColumnPropertiesLayoutData();
		nonCoreColumnProperties.setLayoutData(layoutData);

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
				if (!GUIBuilder.GRAY_OUT_NON_CORE_COLUMN_CONTROLS) {
					nonCoreColumnProperties.setExpanded(false);
				} else {
					nonCoreColumnProperties.setEnabled(false);
				}
				
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
				if (!GUIBuilder.GRAY_OUT_NON_CORE_COLUMN_CONTROLS) {
					nonCoreColumnProperties.setExpanded(true);
				} else {
					nonCoreColumnProperties.setEnabled(true);
				}
			}
		});

		if (GUIBuilder.GRAY_OUT_NON_CORE_COLUMN_CONTROLS) {
			nonCoreColumnProperties.setEnabled(false);
		}

		return nonCoreColumnProperties;
	}

	private static GridLayout createLayout() {
		GridLayout layout = new GridLayout(1, false);

		return layout;
	}

	private static GridData createIsCoreColumnLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, false);

		return layoutData;
	}

	private static GridData createCoreColumnPropertiesLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, false, true);

		return layoutData;
	}
}
