package edu.iu.sci2.converter.psraster.jpg.jpgreader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import javax.imageio.ImageIO;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.sci2.converter.psraster.psrasterproperties.PSRasterProperties;


public class JPGReaderAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    public JPGReaderAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    // The in-data should be a file and have the mime type of file:text/jpg.
    // Read the in-data file as a jpg image file and output the resulting in-memory
    // image object.
    public Data[] execute() throws AlgorithmExecutionException {
    	// Get the input data as a (jpg) File.
    	File jpgFile = (File)data[0].getData();
    	
    	// The jpg file will actually be loaded in as a BufferedImage and wrapped/returned if valid.
    	BufferedImage loadedImage = null;
    	
    	// Attempt to load the jpg file.
    	try {
    		loadedImage = ImageIO.read(jpgFile);
    	}
    	catch (IOException e) {
    		throw new AlgorithmExecutionException(e);
    	}
    	
    	// If we got this far, the jpg was successfully loaded, so wrap it in a Data
    	// object, etc.
    	Data loadedImageData =
    		new BasicData(loadedImage, PSRasterProperties.IMAGE_OBJECT_MIME_TYPE);
    	Dictionary loadedImageMetadata = loadedImageData.getMetadata();
    	
    	loadedImageMetadata.put(DataProperty.LABEL, "Image: " + jpgFile.getAbsolutePath());
    	loadedImageMetadata.put(DataProperty.TYPE, DataProperty.RASTER_IMAGE_TYPE);
    	
    	return new Data[] { loadedImageData };
    }
}