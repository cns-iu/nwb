package edu.iu.sci2.medline.converter.medline_to_medlinetable_converter;

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

import prefuse.data.Table;

import com.google.common.base.Preconditions;

import edu.iu.sci2.medline.common.MedlineTable;
import edu.iu.sci2.medline.converter.medline_to_medlinetable_converter.MedlineFileReader.MedlineFileReaderException;

/**
 * A converter that will convert from a MEDLINE®/PubMed® File to a
 * {@link MedlineTable}.
 * 
 */
public class Converter implements Algorithm {
	public static class Factory implements AlgorithmFactory {

		@Override
		public Algorithm createAlgorithm(Data[] data,
				Dictionary<String, Object> parameters,
				CIShellContext ciShellContext) {
			Preconditions
					.checkArgument(parameters.isEmpty(),
							"The CIShell 1.0 Spec guarantees that parameters will be null for a converter.");
			return new Converter(data[0], ciShellContext);
		}

	}

	private Data datum;
	private LogService logger;

	public Converter(Data datum, CIShellContext ciShellContext) {
		Preconditions.checkNotNull(datum,
				"The datum from the data[] must not be null");
		this.datum = datum;
		this.logger = (LogService) ciShellContext.getService(LogService.class.getName());

	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			// According the the CIShell 1.0 Spec this should be a string that
			// is the pathname, but it must be a file due to existing code.
			File file = (File) this.datum.getData();
			Table table = MedlineFileReader.read(file, this.logger);
			MedlineTable medlineTable = new MedlineTable(table);
			return new Data[] { DataFactory.withClassNameAsFormat(medlineTable,
					DataProperty.TABLE_TYPE, this.datum,
					"A medline table from " + file) };
		} catch (ClassCastException e) {
			throw new AlgorithmExecutionException(
					"The pathname could not be retrieved from the data.", e);
		} catch (MedlineFileReaderException e) {
			throw new AlgorithmExecutionException(
					"The file could not be converted to a table.", e);
		}
	}
}
