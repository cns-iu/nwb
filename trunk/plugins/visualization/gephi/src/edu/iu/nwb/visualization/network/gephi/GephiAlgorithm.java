package edu.iu.nwb.visualization.network.gephi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileCopyingException;
import org.cishell.utilities.FileUtilities;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.log.LogService;

public class GephiAlgorithm implements Algorithm {
	/* An extension that we hope is associated with the Gephi program on
	 * the running machine.
	 */
	public static final String GEPHI_EXT = ".gephi";
	/* An extension for GraphML that Gephi recognizes. */
	public static final String GRAPHML_EXT = ".graphml";
	
	private final Data[] data;
	private Program gephi;
	private final LogService logger;


	public GephiAlgorithm(Data[] data,
			Dictionary<String, Object> parameters,
			CIShellContext ciShellContext) {
		this.data = data;
		this.logger = (LogService) ciShellContext.getService(LogService.class
				.getName());
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				gephi = Program.findProgram(GEPHI_EXT);
			}
		});

		if (gephi == null) {
			String message = "Error: No program was associated with Gephi's "
					+ GEPHI_EXT
					+ " file extension.  You need to have this file type associated "
					+ "with Gephi for this algorithm to work.\n";
			throw new AlgorithmCreationFailedException(message);
		}
	}

	public Data[] execute() throws AlgorithmExecutionException {

		List<File> files = fixFileExtForGephi();

		for (File file : files) {
						
			/*
			 *  BUG Launching this way has an issue where if CIShell has a console
			 *  open, Gephi will reuse this console.  There does not appear to
			 *  be a way to set parameters like "--console new" for gephi.
			 *  
			 *  Because it reuses this console, if you close CIShell console, it will
			 *  close Gephi without warning.  
			 */
			gephi.execute(file.getPath());

		}

		return new Data[0];
	}

	private List<File> fixFileExtForGephi() throws AlgorithmExecutionException {
		/*
		 * BUG The way CIShell currently treats graphml files is not compatible
		 * with how Gephi expects them to look.  CIShell adds the file extension
		 * ".xml" to the files, but Gephi needs ".graphml".  I copy the files
		 * into a new temp file with the extension Gephi expects.
		 */
		List<File> files = new ArrayList<File>(data.length);

		for (Data datum : data) {
			File graphmlFile;

			// Set up temp file with proper extension
			try {
				graphmlFile = File.createTempFile("Graphml-for-gephi",
						GRAPHML_EXT);
			} catch (IOException e) {
				String error = "A temp file could not be created to create the "
						+ "graphml file for Gephi.\n" + e.getMessage();
				throw new AlgorithmExecutionException(error);
			}

			File datumFile = (File) datum.getData();
			
			// Copy this file to the temp file
			try {
				FileUtilities.copyFile(datumFile, graphmlFile);
			} catch (FileCopyingException e) {
				String error = "An error occured while copying "
						+ datumFile.getName()
						+ " to a temporary file.  It will be ignored and "
						+ "Gephi will not run if it was the only file to be opened.\n"
						+ e.getMessage();
				this.logger.log(LogService.LOG_ERROR, error);
				continue;
			}

			files.add(graphmlFile);
		}
		return files;
	}
}