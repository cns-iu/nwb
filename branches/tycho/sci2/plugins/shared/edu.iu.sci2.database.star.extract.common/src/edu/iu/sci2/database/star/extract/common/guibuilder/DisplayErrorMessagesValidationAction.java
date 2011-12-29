package edu.iu.sci2.database.star.extract.common.guibuilder;

import java.util.Collection;

import org.cishell.utilities.StringUtilities;
import org.cishell.utility.datastructure.datamodel.field.validation.FieldValidationAction;
import org.cishell.utility.swt.SWTUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;

public class DisplayErrorMessagesValidationAction implements FieldValidationAction {
	private StyledText instructionsLabel;
	private String instructionsLabelText;
	private String tutorialURL;
	private String tutorialDisplayURL;

	public DisplayErrorMessagesValidationAction(
			StyledText instructionsLabel,
			String instructionsLabelText,
			String tutorialURL,
			String tutorialDisplayURL) {
		this.instructionsLabel = instructionsLabel;
		this.instructionsLabelText = instructionsLabelText;
		this.tutorialURL = tutorialURL;
		this.tutorialDisplayURL = tutorialDisplayURL;

		doesValidate();
	}

	public void doesValidate() {
		this.instructionsLabel.setText("");
		SWTUtilities.styledPrint(
			this.instructionsLabel,
			this.instructionsLabelText,
			this.instructionsLabel.getDisplay().getSystemColor(SWT.COLOR_BLACK),
			SWT.NORMAL);
		SWTUtilities.printURL(
			this.instructionsLabel.getParent(),
			this.instructionsLabel,
			this.tutorialURL,
			this.tutorialDisplayURL,
			this.instructionsLabel.getDisplay().getSystemColor(SWT.COLOR_BLUE),
			SWT.BOLD);
	}

	public void doesNotValidate(Collection<String> errorMessages) {
		this.instructionsLabel.setText("");
		String newLine = String.format("%n");
		String errorText = StringUtilities.implodeItems(errorMessages, newLine);
		SWTUtilities.styledPrint(
			this.instructionsLabel,
			String.format("The following errors were found:%n"),
			this.instructionsLabel.getDisplay().getSystemColor(SWT.COLOR_BLACK),
			SWT.NORMAL);
		SWTUtilities.styledPrint(
			this.instructionsLabel,
			errorText,
			this.instructionsLabel.getDisplay().getSystemColor(SWT.COLOR_RED),
			SWT.NORMAL);
	}
}