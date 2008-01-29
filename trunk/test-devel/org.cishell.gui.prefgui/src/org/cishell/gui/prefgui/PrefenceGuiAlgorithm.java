package org.cishell.gui.prefgui;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
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
    
    private LogService log;
    
    public PrefenceGuiAlgorithm(Data[] data, Dictionary parameters, CIShellContext context,
    		PrefAdmin prefAdmin, LogService log) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.prefAdmin = prefAdmin;
        
        this.log = log;
    }

    public Data[] execute() {
    	
    	Shell parentShell = getParentShell();
    	
    	PreferenceManager prefManager = new PreferenceManager();
    	
    	PrefPage[] globalPrefPages = prefAdmin.getGlobalPrefPages();
    	addPrefPages(prefManager, globalPrefPages);
    	
      	PrefPage[] localPrefPages = prefAdmin.getLocalPrefPages();
    	addPrefPages(prefManager, localPrefPages);
 
		PreferenceGUIRunnable prefGUIRunnable = new PreferenceGUIRunnable(parentShell, prefManager);
		Thread preferenceGUIThread = new Thread(prefGUIRunnable);
    
		parentShell.getDisplay().asyncExec(preferenceGUIThread);
    	
    	return null;
    }
    
    private Shell getParentShell() {
    	IWorkbench workbench = PlatformUI.getWorkbench();
    	IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
    	
    	//TODO: possibly a better, less seamingly arbitrary way to do this
    	IWorkbenchWindow window = windows[0];
    	Shell parentShell = window.getShell();
    	
    	return parentShell;
    }
    
    private void addPrefPages(PreferenceManager prefManager, PrefPage[] prefPages) {
		for (int ii = 0; ii < prefPages.length; ii++) {
			PrefPage prefPage = prefPages[ii];
			
			PreferenceOCD prefOCD = prefPage.getPrefOCD();
			Configuration prefConf = prefPage.getPrefConf();
			
			CIShellPreferenceStore prefStore = new CIShellPreferenceStore(this.log, prefOCD, prefConf);
			CIShellPreferencePage guiPrefPage = new CIShellPreferencePage(this.log, prefOCD, prefConf.getProperties(), prefStore);
			
			prefManager.addToRoot(new PreferenceNode(prefConf.getPid(), guiPrefPage));
		}
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