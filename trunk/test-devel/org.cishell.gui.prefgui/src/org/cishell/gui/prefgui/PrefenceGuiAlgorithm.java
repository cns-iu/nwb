package org.cishell.gui.prefgui;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.cishell.service.prefadmin.PrefAdmin;
import org.cishell.service.prefadmin.PrefPage;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.cm.Configuration;
import org.osgi.service.metatype.ObjectClassDefinition;

public class PrefenceGuiAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    PrefAdmin prefAdmin;
    
    public PrefenceGuiAlgorithm(Data[] data, Dictionary parameters, CIShellContext context,
    		PrefAdmin prefAdmin) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.prefAdmin = prefAdmin;
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
			
			ObjectClassDefinition prefOCD = prefPage.getPrefOCD();
			Configuration prefConf = prefPage.getPrefConf();
			
			CIShellPreferenceStore prefStore = new CIShellPreferenceStore(prefOCD, prefConf);
			CIShellPreferencePage guiPrefPage = new CIShellPreferencePage(prefOCD, prefConf.getProperties(), prefStore);
			
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