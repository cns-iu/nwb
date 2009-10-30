package edu.iu.scipolicy.help.documentation;

import java.net.URL;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

//Makes it easier to create a link which opens a website.
public class WebsiteLink {

	private Link link;
	//Remember that the link portion of the link text must be in the tag <A></A>, for example 'here is <A>the link</A>'
	public WebsiteLink(Composite parent, int style, String linkText, final URL websiteURL) {
		this.link = new Link(parent, style);

		link.setText(linkText);

		//if this link is clicked on, open the website in the users' default web browser.
		link.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				try {
					IWebBrowser browser = getWebBrowser();
					browser.openURL(websiteURL);
				} catch (PartInitException exception) {
					// TODO: Is this a terrible crime, or not? Not sure what the best practice for this situation is.
					exception.printStackTrace();
					throw new Error(exception);
				}
			}

		});
	}
	
	public void setSize(int x, int y) {
		link.setSize(x, y);
	}
	
	public void setLayoutData(Object layoutData) {
		link.setLayoutData(layoutData);
	}
	
	//for some reason, Eclipse won't let you subclass Link, so we have to do this hack.
	public Link getLink() {
		return this.link;
	}

	//Getting the browser every time like this is probably ok, but could be more efficient.
	protected IWebBrowser getWebBrowser() throws PartInitException {
		IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
		IWebBrowser browser = browserSupport.getExternalBrowser();
		return browser;
	}
}
