package edu.iu.sci2.help.documentation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
//A dialog box which links to various online documentation for Sci2.
public class DocumentationDialog extends Dialog {
	
	private static List<Hyperlink> links = new ArrayList<Hyperlink>();
	static {
		links.add(new Hyperlink("Main Sci2 Tool Website", "https://sci2.cns.iu.edu"));
		links.add(new Hyperlink("All Sci2 Documentation", "https://sci2.cns.iu.edu/user/documentation.php"));
		links.add(new Hyperlink("Sci2 Manual","http://sci2.wiki.cns.iu.edu"));
	}
	
	private Shell shell;

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
			for (Hyperlink link : links) {
				URL url = new URL(link.url);
				WebsiteLinkWidget widget = 
					new WebsiteLinkWidget(group, SWT.BORDER, link.text, url);
				widget.setSize(180, 40);
				widget.setLayoutData(new GridData(GridData.FILL_VERTICAL));
			}
			
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
		/*
		 *  you can create your own button 
		 *  or can call super method to create default ok and cancel button
		 */
		super.createButtonsForButtonBar(parent);
	}
	
	private static final class Hyperlink {
		public String text;
		public String url;
		
		public Hyperlink(String text, String url) {
			this.text = "<A>" + text + "</A>"; //text inside 'A' will become the hyperlink
			this.url = url;
		}
	}

}

