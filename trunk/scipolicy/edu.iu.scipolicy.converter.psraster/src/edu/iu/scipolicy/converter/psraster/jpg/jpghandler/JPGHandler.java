package edu.iu.scipolicy.converter.psraster.jpg.jpghandler;

import java.awt.Image;
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
import org.osgi.service.log.LogService;

import edu.iu.scipolicy.converter.psraster.psrasterproperties.PSRasterProperties;

public class JPGHandler implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    public JPGHandler(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    // Convert the in-data from mime type file:text/jpg to file-ext:jpg. 
    public Data[] execute() throws AlgorithmExecutionException {
    	File jpgFile = (File) this.data[0].getData();
    	String jpgFileName = jpgFile.getName();
    		
    	// "Validate" the file by making sure it exists.
        if (!jpgFile.exists()) {
        	throw new AlgorithmExecutionException("Unable to find the file \'" +
        		jpgFileName + "\' file for validation.");
        }

        Data jpgFileData =
        	new BasicData(jpgFile, PSRasterProperties.JPG_FILE_TYPE);
        	
        return new Data[] { jpgFileData };
    }
}