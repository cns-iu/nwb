package edu.iu.sci2.database.star.gui.builder;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;

public class Utilities {
	public static void clearMargins(GridLayout layout) {
		layout.marginTop = layout.marginBottom = layout.marginHeight = 0;
		layout.marginLeft = layout.marginRight = layout.marginWidth = 0;
	}

	public static void clearSpacing(GridLayout layout) {
		layout.horizontalSpacing = layout.verticalSpacing = 0;
	}
}