package edu.iu.nwb.converter.postscript.gs.pdf;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.nwb.converter.postscript.gs.util.BasicHandlerAlgorithm;
import edu.iu.nwb.converter.postscript.gs.util.fileformat.BasicFileFormat;

public class PDFHandlerAlgorithmFactory implements AlgorithmFactory {
	public static final BasicFileFormat PDF_FILE_FORMAT =
			BasicFileFormat.definedBy(
					"PDF", "file:application/pdf", "pdf", DataProperty.VECTOR_IMAGE_TYPE);
	
	@Override
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters,	CIShellContext ciContext) {
		return new BasicHandlerAlgorithm(data[0], PDF_FILE_FORMAT);
	}	
}
