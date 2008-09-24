package edu.iu.nwb.composite.isiloadandclean.loading;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

//TODO: Ideally this would be separated out so this bundle wouldn't have Eclipse dependencies.
public class SWTISILoader implements SpecialISILoader {

	private static File currentDir;
	private List returnList;

	public Data[] getISIDataFromUser() {

		this.returnList = new ArrayList();
		Data[] returnDM;

		final IWorkbenchWindow[] windows = PlatformUI.getWorkbench()
				.getWorkbenchWindows();
		if (windows.length == 0) {
			return null;
		}

		Display display = PlatformUI.getWorkbench().getDisplay();
		DataUpdater dataUpdater = new DataUpdater(windows[0]);

		if (Thread.currentThread() != display.getThread()) {
			display.syncExec(dataUpdater);
		} else {
			dataUpdater.run();
		}

		if (!dataUpdater.returnList.isEmpty()) {
			int size = dataUpdater.returnList.size();
			returnDM = new Data[size];
			for (int index = 0; index < size; index++) {
				returnDM[index] = (Data) dataUpdater.returnList.get(index);
			}
			return returnDM;
		} else {
			return null;
		}

	}

	public static String getFileExtension(File theFile) {
		String fileName = theFile.getName();
		String extension;
		if (fileName.lastIndexOf(".") != -1)
			extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			extension = "";
		return extension;
	}

	final class DataUpdater implements Runnable {
		boolean loadFileSuccess = false;
		IWorkbenchWindow window;
		ArrayList returnList = new ArrayList();

		DataUpdater(IWorkbenchWindow window) {
			this.window = window;
		}

		public void run() {

			FileDialog dialog = new FileDialog(window.getShell(), SWT.OPEN);
			if (currentDir == null) {
				currentDir = new File(System.getProperty("osgi.install.area")
						.replace("file:", "")
						+ "sampledata");

				if (!currentDir.exists()) {
					currentDir = new File(System.getProperty(
							"osgi.install.area").replace("file:", "")
							+ "sampledata"
							+ File.separator + "anything");
				}
			}
			dialog.setFilterPath(currentDir.getPath());
			dialog.setText("Select a File");
			//doesn't work for some reason
			//dialog.setFilterExtensions(new String[] { "isi" });
			String fileName = dialog.open();
			
			if (fileName == null) {
				return;
			}

			BasicData fileData = new BasicData(fileName, "file-ext:isi");
			fileData.getMetadata().put(DataProperty.LABEL, fileName);

			returnList.add(fileData);
		}
	}
}
