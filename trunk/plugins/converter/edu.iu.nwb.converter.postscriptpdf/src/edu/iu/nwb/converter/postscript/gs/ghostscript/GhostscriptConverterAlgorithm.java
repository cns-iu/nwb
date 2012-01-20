package edu.iu.nwb.converter.postscript.gs.ghostscript;

import java.io.File;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.service.conversion.ConversionException;

public class GhostscriptConverterAlgorithm implements Algorithm {
	public static final String TOP_LEVEL_ERROR_MESSAGE =
			"This converter requires a Ghostscript installation.  " +
			"Potential problems: " +
			"(1) Ghostscript may not be installed.  " +
			"See [url]http://pages.cs.wisc.edu/~ghost/[/url].  " +
			"(2) Ghostscript may be installed but not included on this " +
			"system's \"path\" environment variable.  " +
			"Ensure that the Ghostscript interpreter executable is visible on the \"path\".  " +
			"For help setting \"path\" see [url]http://java.com/en/download/help/path.xml[/url].";

	private final Data inputFileData;
	private final GhostscriptFileFormat targetFormat;
		
	/**
	 * @param inputFileData	Data wrapping a File in a format Ghostscript can read.
	 * @param targetFormat	Desired output format.
	 */
	public GhostscriptConverterAlgorithm(Data inputFileData, GhostscriptFileFormat targetFormat) {
		this.inputFileData = inputFileData;
		this.targetFormat = targetFormat;
	}
	

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		File postScriptFile = (File) inputFileData.getData();

		try {
			File outputFile = targetFormat.convert(postScriptFile);
			
			return new Data[] { targetFormat.wrapAsLoadedData(outputFile, inputFileData) };
		} catch (GhostscriptException e) {
			throw new AlgorithmExecutionException(
					TOP_LEVEL_ERROR_MESSAGE, new ConversionException(e));
		}
	}
}
