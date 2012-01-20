package edu.iu.nwb.converter.postscript.gs.pdf;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

import edu.iu.nwb.converter.postscript.gs.ghostscript.GhostscriptConverterAlgorithm;
import edu.iu.nwb.converter.postscript.gs.ghostscript.GhostscriptDevice;
import edu.iu.nwb.converter.postscript.gs.ghostscript.GhostscriptFileFormat;

public class PostScriptToPDFAlgorithmFactory implements AlgorithmFactory {
	public static final GhostscriptFileFormat PDF_GHOSTSCRIPT_FILE_FORMAT =
			GhostscriptFileFormat.definedBy(
					PDFHandlerAlgorithmFactory.PDF_FILE_FORMAT,	GhostscriptDevice.pdfwrite);
	
	@Override
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters,	CIShellContext ciShellContext) {
		return new GhostscriptConverterAlgorithm(data[0], PDF_GHOSTSCRIPT_FILE_FORMAT);
	}
}