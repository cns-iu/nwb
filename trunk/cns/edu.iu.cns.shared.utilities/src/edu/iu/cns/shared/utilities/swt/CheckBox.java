package edu.iu.cns.shared.utilities.swt;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class CheckBox {
	private Button button;
	private boolean isSelected = false;

	public CheckBox(Composite parent, int style) {
		this.button = new Button(parent, style);
		this.button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent selectionEvent) {
				CheckBox.this.isSelected = !CheckBox.this.isSelected;
			}

			public void widgetSelected(SelectionEvent selectionEvent) {
				CheckBox.this.isSelected = !CheckBox.this.isSelected;
			}
		});
	}

	public Button getButton() {
		return button;
	}

	public boolean isSelected() {
		return this.isSelected;
	}

	public void toggle() {
		this.isSelected = !this.isSelected;
		this.button.setSelection(this.isSelected);
	}
}