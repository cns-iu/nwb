package edu.iu.cns.persistence.session.restore;

import java.io.File;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.app.service.fileloader.FileLoadException;
import org.cishell.app.service.fileloader.FileLoaderService;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.cns.persistence.session.common.Utilities;

public class ImportSessionAlgorithm implements Algorithm {
	public static final String SESSION_FILE_EXTENSION_FILTER =
		String.format("*%s", Utilities.DEFAULT_SESSION_FILE_EXTENSION);
	public static final String[] FILTER_EXTENSIONS =
		new String[] { SESSION_FILE_EXTENSION_FILTER };

	private LogService logger;
	private DataManagerService dataManager;
	private FileLoaderService fileLoader;

    public ImportSessionAlgorithm(
    		LogService logger, DataManagerService dataManager,FileLoaderService fileLoader) {
    	this.logger = logger;
    	this.dataManager = dataManager;
    	this.fileLoader = fileLoader;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		File[] selectedFiles = this.fileLoader.getFilesToLoadFromUser(true, FILTER_EXTENSIONS);

    		if (selectedFiles.length > 0) {
    			File sessionFile = selectedFiles[0];

    			return new SessionImporter(
    				sessionFile.getAbsolutePath(), this.logger, this.dataManager).importSession();
    		} else {
    			throw new AlgorithmCanceledException();
    		}
    	} catch (FileLoadException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }
}