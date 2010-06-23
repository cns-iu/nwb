package edu.iu.sci2.database.star.gui.builder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import edu.iu.cns.shared.utilities.swt.CheckBox;

public class ColumnProperties {
	private CheckBox isCoreColumn;

	public ColumnProperties(Composite parent) {
		this.isCoreColumn = createIsCoreColumn(parent);
	}

	public CheckBox getIsCoreColumn() {
		return this.isCoreColumn;
	}

	private static CheckBox createIsCoreColumn(Composite parent) {
		CheckBox isCoreColumn = new CheckBox(parent, SWT.CHECK);
		isCoreColumn.toggle();

		return isCoreColumn;
	}
}