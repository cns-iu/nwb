package edu.iu.nwb.converter.postscript.gs.util;

import java.io.File;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;

import edu.iu.nwb.converter.postscript.gs.util.fileformat.FileFormat;

// TODO Move to CIShell?
public class BasicHandlerAlgorithm implements Algorithm {	
	private final Data candidateFileDatum;
	private final FileFormat targetFileFormat;
	
	public BasicHandlerAlgorithm(Data datum, FileFormat targetFileFormat) {
		this.candidateFileDatum = datum;
		this.targetFileFormat = targetFileFormat;
	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		File file = handle(candidateFileDatum, targetFileFormat);
		
		return new Data[]{ targetFileFormat.wrapAsHandledData(file, candidateFileDatum) };
	}

	private static File handle(Data candidateFileDatum, FileFormat targetFileFormat)
			throws AlgorithmExecutionException {
		Object candidateFileObject = candidateFileDatum.getData();
		failIfNotFile(candidateFileObject);
		File file = (File) candidateFileObject;
		
		String format = candidateFileDatum.getFormat();
		failIfFormatsDiffer(targetFileFormat.getMimeType(), format);
		
		return file;
	}

	private static void failIfNotFile(Object candidateFileObject)
			throws AlgorithmExecutionException {
		if (!(candidateFileObject instanceof File)) {
			throw new AlgorithmExecutionException(
					String.format(
							"Datum \"%s\" is not an instance of %s.",
							candidateFileObject,
							File.class.getName()));
		}
	}
	
	private static void failIfFormatsDiffer(String mimeType, String dataFormat)
			throws AlgorithmExecutionException {
		if (!mimeType.equals(dataFormat)) {
			throw new AlgorithmExecutionException(
					String.format(
							"Expected format \"%s\" but the candidateFileDatum is \"%s\".",
							mimeType,
							dataFormat));
		}
	}
}
