package edu.iu.nwb.converter.postscript.gs.png;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.nwb.converter.postscript.gs.util.BasicHandlerAlgorithm;
import edu.iu.nwb.converter.postscript.gs.util.fileformat.BasicFileFormat;

public class PNGHandlerAlgorithmFactory implements AlgorithmFactory {
	public static final BasicFileFormat PNG_FILE_FORMAT =
			BasicFileFormat.definedBy(
					"PNG", "file:image/png", "png", DataProperty.RASTER_IMAGE_TYPE);
		
	@Override
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters,	CIShellContext ciContext) {
		return new BasicHandlerAlgorithm(data[0], PNG_FILE_FORMAT);
	}
}
