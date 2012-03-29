package edu.iu.epic.help.documentation;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.log.LogService;

//opens a dialog box which links to various online documentation
//this class handles all the dirty work with Eclipse. The meat is in DocumentationDialog
public class Documentation implements Algorithm {
	private LogService log;

	public Documentation(Data[] data, Dictionary parameters, CIShellContext context) {
		this.log = (LogService) context.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try {
			
			// prepare to open documentation dialog box
			Shell parentShell = getParentShell();
			DocumentationRunnable documentationDialogRunnable = new DocumentationRunnable(parentShell);
			Thread documentationDialogThread = new Thread(documentationDialogRunnable);

			// tell Eclipse to open the documentation dialog box runnable
			// (We must tell SWT to run the dialog, instead of running it directly ourselves
			// since it is a gui element)
			parentShell.getDisplay().asyncExec(documentationDialogThread);
			// (we return immediately, instead of waiting for the dialog to close)
			
		} catch (Error e) {
			log(LogService.LOG_ERROR, "Documentation Algorithm failed due to unhandled error: " + e.getMessage(), e);
		}
		
		return new Data[0];
	}

	private Shell getParentShell() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();

		// possibly a better, less seamingly arbitrary way to do this
		IWorkbenchWindow window = windows[0];
		Shell parentShell = window.getShell();
		return parentShell;
	}

	private class DocumentationRunnable implements Runnable {

		private Shell parentShell;

		public DocumentationRunnable(Shell parentShell) {
			this.parentShell = parentShell;
		}

		public void run() {
			DocumentationDialog dialog = new DocumentationDialog(parentShell);
			dialog.open();
		}
	}
	

	private void log(int messageLevel, String message, Throwable error) {
		if (log != null) {
			this.log(messageLevel, message, error);
			
			error.printStackTrace();
		} else {	
			error.printStackTrace();
		}
	}
}