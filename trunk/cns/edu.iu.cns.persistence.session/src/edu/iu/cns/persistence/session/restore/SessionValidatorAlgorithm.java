package edu.iu.cns.persistence.session.restore;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

public class SessionValidatorAlgorithm implements Algorithm, ProgressTrackable {
	private String fileName;
	private LogService logger;
	private DataManagerService dataManager;
	private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;

	public SessionValidatorAlgorithm(
			String fileName,
			LogService logger,
			DataManagerService dataManager) {
		this.fileName = fileName;
		this.logger = logger;
		this.dataManager = dataManager;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		return new SessionImporter(this.fileName, this.logger, this.dataManager).importSession();
	}

	public ProgressMonitor getProgressMonitor() {
		return this.progressMonitor;
	}

	public void setProgressMonitor(ProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
	}

//	private boolean isFileMimeType(String mimeType) {
//		return (
//			mimeType.startsWith(Utilities.FILE_EXTENSION) || mimeType.startsWith(Utilities.FILE));
//	}

//	void addDataToDataManager(Data[] data) {
//		for (Data datum : data) {
//			this.dataManager.addData(datum);
//		}
//	}
}