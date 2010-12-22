package edu.iu.sci2.help.documentation;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

//the important part of this plugin. 
//A dialog box which links to various online documentation for Scipolicy.
public class DocumentationDialog extends Dialog {
	private static final String MAIN_DOC_PAGE_TEXT = "<A>Main Sci^2 Tool Website</A>";
	private static final String MAIN_DOC_PAGE_URL = "http://sci.slis.indiana.edu/";

	private static final String ALL_ALGORITHMS_DOC_TEXT = "<A>Algorithms Documentation</A>";
	private static final String ALL_ALGORITHMS_DOC_URL = "https://nwb.slis.indiana.edu/community/?n=Algorithms.HomePage";

	private static final String ALL_SUPPORTED_FORMATS_DOC_TEXT = "<A>Supported Formats Documentation</A>";
	private static final String ALL_SUPPORTED_FORMATS_DOC_URL = "https://nwb.slis.indiana.edu/community/?n=DataFormats.HomePage";
	
	private static final String TUTORIALS_DOC_TEXT = "<A>Tutorials on NWB Community Wiki</A>";
	private static final String TUTORIALS_DOC_URL = "https://nwb.slis.indiana.edu/community/?n=Tutorials.HomePage";
	
	Shell shell = null;

	public DocumentationDialog(Shell parent) {
		super(parent);
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		createControls(composite);
		return composite;
	}

	private void createControls(Composite composite) {
		shell = composite.getShell();
		shell.setText("Documentation Links");
		Group group = new Group(composite, SWT.None);
		group.setText("");
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(1, true));
		   	
		try {
			//main page link
			URL mainDocPageURL = new URL(MAIN_DOC_PAGE_URL);
			WebsiteLink mainDocPageLink = new WebsiteLink(group, SWT.BORDER, MAIN_DOC_PAGE_TEXT, mainDocPageURL);
			mainDocPageLink.setSize(180, 40);
			mainDocPageLink.setLayoutData(new GridData(GridData.FILL_VERTICAL));
			
			//all algorithms docs link
			WebsiteLink allAlgorithmsDocLink = new WebsiteLink(group, SWT.BORDER, ALL_ALGORITHMS_DOC_TEXT, new URL(
					ALL_ALGORITHMS_DOC_URL));
			allAlgorithmsDocLink.setSize(180, 40);
			allAlgorithmsDocLink.setLayoutData(new GridData(GridData.FILL_VERTICAL));
			
			//all supported formats doc link
			WebsiteLink allSupportedFormatsDocLink = new WebsiteLink(group, SWT.BORDER,
					ALL_SUPPORTED_FORMATS_DOC_TEXT, new URL(ALL_SUPPORTED_FORMATS_DOC_URL));
			allSupportedFormatsDocLink.setSize(180, 40);
			allSupportedFormatsDocLink.setLayoutData(new GridData(GridData.FILL_VERTICAL));
			
			//tutorials doc link
			WebsiteLink tutorialsDocLink = new WebsiteLink(group, SWT.BORDER,
					TUTORIALS_DOC_TEXT, new URL(TUTORIALS_DOC_URL));
			tutorialsDocLink.setSize(180, 40);
			tutorialsDocLink.setLayoutData(new GridData(GridData.FILL_VERTICAL));
			
			//explanation of how browser opens these links
			String labelText = "Documentation should open in your default web browser." +
				" It may open in its own tab or in a separate window, depending on your" + 
				" browser settings.";
			Label browserBehaviourExplanation = new Label(group, SWT.WRAP);
			browserBehaviourExplanation.setText(labelText);
			browserBehaviourExplanation.setSize(180, 120);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new Error(e.getMessage(), e);
		}

		group.pack();
		composite.pack();
	}

	protected void createButtonsForButtonBar(Composite parent) {
		// you can create your own button or can call super method to create default ok and cancel button
		super.createButtonsForButtonBar(parent);
	}

}

