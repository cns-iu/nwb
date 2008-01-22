package org.cishell.gui.prefgui;

import org.eclipse.jface.preference.FieldEditorPreferencePage;

public class BlankPreferencePage extends FieldEditorPreferencePage {

	public BlankPreferencePage(int style, String title) {
		super(style);
		
		this.setTitle(title);
	}
	
	protected void createFieldEditors() {
	}

}
