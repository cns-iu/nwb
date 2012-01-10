package edu.iu.nwb.converter.postscriptpdf.handler;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

public class PDFHandlerAlgorithm implements Algorithm {
	public static final String PDF_MIME_TYPE = "file:application/pdf";
	
	private Data datum;
	
	
	public static class Factory implements AlgorithmFactory {
		@Override
		public Algorithm createAlgorithm(
				Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
			Data datum = data[0];
			
			return new PDFHandlerAlgorithm(datum);
		}		
	}
	public PDFHandlerAlgorithm(Data datum) {
		this.datum = datum;
	}


	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		Object candidatePdfFile = datum.getData();
		String format = datum.getFormat();
		
		if (!(candidatePdfFile instanceof File)) {
			throw new AlgorithmExecutionException(
					String.format(
							"Datum \"%s\" is not an instance of %s.",
							candidatePdfFile,
							File.class.getName()));
		}
		
		if (!PDF_MIME_TYPE.equals(format)) {
			throw new AlgorithmExecutionException(
					String.format(
							"Expected format \"%s\" but the datum is \"%s\".",
							PDF_MIME_TYPE,
							format));
		}
		
		return new Data[] { new BasicData(candidatePdfFile, PDF_MIME_TYPE) };
	}
}
