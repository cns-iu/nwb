package org.cishell.gui.prefgui;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.gui.prefgui.preferencepages.BlankPreferencePage;
import org.cishell.gui.prefgui.preferencepages.CIShellPreferencePage;
import org.cishell.service.prefadmin.PrefAdmin;
import org.cishell.service.prefadmin.PrefPage;
import org.cishell.service.prefadmin.PreferenceOCD;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.cm.Configuration;
import org.osgi.service.log.LogService;

public class PrefenceGuiAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    PrefAdmin prefAdmin;
    
    LogService log;
    
    public PrefenceGuiAlgorithm(Data[] data, Dictionary parameters, CIShellContext context,
    		PrefAdmin prefAdmin, LogService log) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        
        this.prefAdmin = prefAdmin;    
        this.log = log;
    }

    public Data[] execute() {
    	PreferenceManager prefManager = new PreferenceManager();
    	
    	addGlobalPreferences(prefManager);
    	addLocalPreferences(prefManager);
    	addParamPreferences(prefManager);
    	
    	Shell parentShell = getParentShell();
		PreferenceGUIRunnable prefGUIRunnable = new PreferenceGUIRunnable(parentShell, prefManager);
		Thread preferenceGUIThread = new Thread(prefGUIRunnable);
    
		//We must tell SWT to run the preference dialog, instead of running it directly ourselves
		parentShell.getDisplay().asyncExec(preferenceGUIThread);
    	
    	return null;
    }
    
    private void addGlobalPreferences(PreferenceManager prefManager) {
    	PrefPage[] globalPrefPages = prefAdmin.getGlobalPrefPages();
    	
    	BlankPreferencePage globalPrefPageRoot = new BlankPreferencePage(1, "General Preferences", "Contains non-algorithm preferences");
    	PreferenceNode rootNode = new PreferenceNode("General Preferences Root", globalPrefPageRoot);
    	prefManager.addToRoot(rootNode);
    	
    	addPrefPages(globalPrefPages, rootNode);
    }
    
    private void addLocalPreferences(PreferenceManager prefManager) {
    	PrefPage[] localPrefPages = prefAdmin.getLocalPrefPages();
    	
    	BlankPreferencePage localPrefPageRoot = new BlankPreferencePage(1,
    			"Algorithm Preferences", "Contains preferences that modify how particular algorithms work.");
    	PreferenceNode rootNode = new PreferenceNode("Algorithm Preferences Root", localPrefPageRoot);
    	prefManager.addToRoot(rootNode);
    	
    	addPrefPages(localPrefPages, rootNode);
    }
 
    private void addParamPreferences(PreferenceManager prefManager) {
    	PrefPage[] paramPrefPages = prefAdmin.getParamPrefPages();
    	
    	BlankPreferencePage paramPrefPageRoot = new BlankPreferencePage(1, "Algorithm Parameter Preferences",
    			"Contains preferences that allow you to to modify the default values for the fields that appear when an algorithm runs.");
    	PreferenceNode rootNode = new PreferenceNode("General Preferences Root", paramPrefPageRoot);
    	prefManager.addToRoot(rootNode);
    	
    	addPrefPages(paramPrefPages, rootNode);
    }
    
    private Shell getParentShell() {
    	IWorkbench workbench = PlatformUI.getWorkbench();
    	IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
    	
    	//possibly a better, less seamingly arbitrary way to do this
    	IWorkbenchWindow window = windows[0];
    	Shell parentShell = window.getShell();
    	
    	return parentShell;
    }
    
    private void addPrefPages(PrefPage[] prefPages, PreferenceNode rootNode) {
    	for (int ii = 0; ii < prefPages.length; ii++) {
    		PreferenceNode prefNode = makePreferenceNode(prefPages[ii]);
    		rootNode.add(prefNode);
    	}
    }
    
    private PreferenceNode makePreferenceNode(PrefPage prefPage) {
		PreferenceOCD prefOCD = prefPage.getPrefOCD();
		Configuration prefConf = prefPage.getPrefConf();
		
		CIShellPreferenceStore prefStore = new CIShellPreferenceStore(this.log, prefOCD, prefConf);
		CIShellPreferencePage guiPrefPage = new CIShellPreferencePage(this.log,
				prefOCD, prefStore);
		return new PreferenceNode(prefConf.getPid(), guiPrefPage);
    }
    
    private class PreferenceGUIRunnable implements Runnable {

    	private Shell parentShell;
    	private PreferenceManager prefManager;
    	
    	public PreferenceGUIRunnable(Shell parentShell, PreferenceManager prefManager) {
    		this.parentShell = parentShell;
    		this.prefManager = prefManager;
    	}
    	
		public void run() {
			PreferenceDialog prefDialog = new PreferenceDialog(parentShell, prefManager);
			prefDialog.open();
		}
    }
}