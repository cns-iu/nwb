package edu.iu.sci2.medline.validator;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.DataFactory;
import org.osgi.service.log.LogService;

import com.google.common.base.Preconditions;

/**
 * An 'empty' validator that will convert a {@code file-ext:txt} file into a
 * {@code file:text/medline} file. It is empty in the sense that it will not
 * actually validate the file, it just changes the format.
 * 
 */
public class Validator implements Algorithm {

	public static final String MEDLINE_MIME_TYPE = "file:text/medline";

	public static class Factory implements AlgorithmFactory {
		@Override
		public Algorithm createAlgorithm(Data[] data,
				Dictionary<String, Object> parameters,
				CIShellContext ciShellContext) {

			Preconditions
					.checkArgument(parameters.isEmpty(),
							"The CIShell 1.0 Spec guarantees that parameters will be null for a validator.");
			Preconditions.checkNotNull(data, "The data[] must not be null.");
			return new Validator(data[0],
					(LogService) ciShellContext.getService(LogService.class
							.getName()));
		}

	}

	private Data datum;
	private LogService logger;

	public Validator(Data datum, LogService logger) {
		Preconditions.checkNotNull(datum,
				"The datum from the data[] must not be null");
		Preconditions.checkNotNull(logger, "The logger must not be null");

		this.datum = datum;
		this.logger = logger;
	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			String pathname = (String) this.datum.getData();
			File file = new File(pathname);
			if (!file.canRead()) {
				this.logger.log(LogService.LOG_ERROR, "The file at '"
						+ pathname + "' cannot be read.");
				return null;
			}

			// This is what the output should be according to the CIShell 1.0
			// Spec.
			// Data outData = new BasicData(this.datum.getData(),
			// MEDLINE_MIME_TYPE);
			// outData.getMetadata().put(DataProperty.LABEL, "Medline file:" +
			// pathname);
			// return new Data[] { outData };
			return new Data[] { DataFactory.forFile(file, MEDLINE_MIME_TYPE,
					DataProperty.TEXT_TYPE, this.datum,
					"Medline file: " + file.getName()) };
		} catch (ClassCastException e) {
			this.logger.log(LogService.LOG_ERROR,
					"The pathname could not be converted to a string.");
			return null;
		}
	}

}
