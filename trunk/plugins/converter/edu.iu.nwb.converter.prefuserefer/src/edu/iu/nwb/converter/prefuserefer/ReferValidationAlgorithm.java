package edu.iu.nwb.converter.prefuserefer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

public class ReferValidationAlgorithm implements Algorithm {
	public static final String REFER_MIME_TYPE = "file:text/referbib";
	private String inReferFileName;
	private LogService log;
    private ReferUtil util;	
	
    
    public ReferValidationAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext context) {
    	this.inReferFileName = (String) data[0].getData();
        
        this.log = (LogService) context.getService(LogService.class.getName());        
        this.util = new ReferUtil(log);
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
		File inReferFile = new File(inReferFileName);

		try {
			if (isValid(inReferFile)) {
				try {
					return createOutData(inReferFile);
				} catch (SecurityException e) {
					throw new AlgorithmExecutionException(e.getMessage(), e);
				}
			} else {
				throw new AlgorithmExecutionException(
						"Unable to validate " + inReferFileName);
			}
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
    }


	private Data[] createOutData(File inData) {
		Data[] dm = new Data[] {new BasicData(inData, REFER_MIME_TYPE)};
		dm[0].getMetadata().put(DataProperty.LABEL,
				"EndNote reference file: " + inReferFileName);
		dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		return dm;
	}
    
    /* Not fool-proof. No smoking gun for validating the file without basically
     * reading the whole thing.  If the file has 0 or more empty lines followed
     * by a line starting with %, it's good.  Else, it's not good.
     */
    private boolean isValid(File referFile)
    		throws AlgorithmExecutionException, IOException {
    	BufferedReader fileReader = util.makeReader(referFile);

		while (true) {
			String line = fileReader.readLine();
			if (util.isEndOfFile(line)) {
				return false;
			} else if (util.isBlank(line)) {	
				continue;
			} else if (util.startsWithFieldMarker(line)) {
				return true;
			} else {
				return false;
			}
		}
    }
}