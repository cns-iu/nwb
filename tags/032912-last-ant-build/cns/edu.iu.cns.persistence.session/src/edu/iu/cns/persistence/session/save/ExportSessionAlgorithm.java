package edu.iu.cns.persistence.session.save;

import java.io.File;
import java.io.IOException;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.app.service.filesaver.FileSaveException;
import org.cishell.app.service.filesaver.FileSaverService;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.service.conversion.DataConversionService;
import org.osgi.service.log.LogService;

import edu.iu.cns.persistence.session.common.Utilities;

public class ExportSessionAlgorithm implements Algorithm {
	public static final String ANY_FILE_EXTENSION = "file-ext:*";

	private LogService logger;
	private DataManagerService dataManager;
	private DataConversionService dataConverter;
	private FileSaverService fileSaver;

	public ExportSessionAlgorithm(
			LogService logger,
			DataManagerService dataManager,
			DataConversionService dataConverter,
			FileSaverService fileSaver) {
		this.logger = logger;
		this.dataManager = dataManager;
		this.dataConverter = dataConverter;
		this.fileSaver = fileSaver;
	}

	// 0
	public Data[] execute() throws AlgorithmExecutionException {
		File targetSessionFile = getTargetSessionFileFromUser();

		SessionDataGetter sessionDataGetter = new SessionDataGetter(this.dataManager);
		Data[] allData = sessionDataGetter.readData();

		if (allData.length == 0) {
			String logMessage = "There is no data to save to a session. Ignoring operation.";
			logger.log(LogService.LOG_WARNING, logMessage);

			return null;
		}

		SessionDataConverter sessionDataConverter =
			new SessionDataConverter(this.logger, this.dataConverter, allData);
		new SessionWriter(
			sessionDataConverter.getInputToOutputData(),
			sessionDataConverter.getOutputDataToIndex(),
			sessionDataConverter.getOriginalToConvertedData(),
			targetSessionFile);

		return null;
	}

	private File getTargetSessionFileFromUser() throws AlgorithmExecutionException {
		try {
			File targetSessionFile =
				fileSaver.promptForTargetFile(Utilities.FULL_DEFAULT_SESSION_FILE_NAME);

			if (targetSessionFile != null) {
				try {
					if (!targetSessionFile.exists()) {
						targetSessionFile.createNewFile();
					}
				} catch (IOException e) {
					throw new AlgorithmExecutionException(e.getMessage(), e);
				}

				return targetSessionFile;
			} else {
				throw new AlgorithmCanceledException();
			}
		} catch (FileSaveException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}
}