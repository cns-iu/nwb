package edu.iu.nwb.preprocessing.removeegraphattributes;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.CollectionUtilities;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO.AttributeFilteringNWBWriter;
import edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO.RemovableAttributeReader;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.ParsingException;

/* This code assumes the input file's attribute schemata will not contain
 * duplicate keys, even though the NWB file format specification does
 * not seem to specifically disallow having both "foo*int" and "foo*string".
 * 
 * This assumption is borne out by that the nwbutils package considers
 * the node and edge schemas to implement Map.
 * It will also simplify the user interface.
 * 
 * No guarantees are made when the inNWBFile contains such duplicates.
 */
public abstract class RemoveGraphAttributesAlgorithm implements Algorithm {
	public static final String REMOVED_ATTRIBUTES_LIST_SEPARATOR = ", ";
	
	private File inNWBFile;
	private Data inData;
	private Dictionary parameters;  
	private LogService logger;
    
    
	public RemoveGraphAttributesAlgorithm(
			Data[] data, Dictionary parameters, CIShellContext context) {
		this.inNWBFile = (File) data[0].getData();
        this.inData = data[0];
        this.parameters = parameters;
        
        this.logger = (LogService) context.getService(LogService.class.getName());
    }
	
	public abstract RemovableAttributeReader createAttributeReader(
			File inputNWBFile) throws AlgorithmExecutionException;
	
	public abstract AttributeFilteringNWBWriter createAttributeFilteringFileWriter(
			File outputNWBFile, Collection keysToRemove) throws IOException;
	
	public abstract String createOutDataLabel(Collection keysToRemove);
	
	public Data[] execute() throws AlgorithmExecutionException {    	
    	try {
    		// Read the attribute keys on inNWBFile
    		RemovableAttributeReader attributeReader =
    			createAttributeReader(inNWBFile);
    		Collection removableAttributeKeys =
    			attributeReader.determineRemovableAttributeKeys();
    		
    		// Take from those only the ones that the user selected
    		Collection keysToRemove =
    			CollectionUtilities.grabSelectedValues(removableAttributeKeys,
    												   parameters);

    		/* Write outputNWBFile, which is just like inNWBFile
    		 * save for that those attributes are excluded
    		 */
    		File outputNWBFile = NWBFileUtilities.createTemporaryNWBFile();
    		AttributeFilteringNWBWriter filteringWriter =
    			createAttributeFilteringFileWriter(outputNWBFile, keysToRemove);
    		NWBFileUtilities.parseNWBFileWithHandler(inNWBFile, filteringWriter);

    		// Log the keys of removed attributes
    		logger.log(LogService.LOG_INFO,
    				   createRemovedAttributesReport(keysToRemove));
    		
    		String outDataLabel = createOutDataLabel(keysToRemove);
			return createOutData(outputNWBFile, outDataLabel, inData);
    	} catch (IOException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	} catch (ParsingException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
		}
    }
	
	public static String createRemovedAttributesReport(Collection keysToRemove) {
		if (keysToRemove.isEmpty()) {
			return "No attributes removed.";
		} else {
			String[] removedKeysArray =
				(String[]) keysToRemove.toArray(new String[0]);
			String removedKeysString =
				StringUtilities.implodeStringArray(removedKeysArray,
												   REMOVED_ATTRIBUTES_LIST_SEPARATOR);

			return ("Removed attributes named: " + removedKeysString + ".");
		}
	}
	
	private Data[] createOutData(File outNWBFile, String outDataLabel, Data inData) {
		Data outData = new BasicData(outNWBFile, NWBFileProperty.NWB_MIME_TYPE);
		outData.getMetadata().put(DataProperty.LABEL, outDataLabel);
		outData.getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		outData.getMetadata().put(DataProperty.PARENT, inData);
		
		return new Data[] { outData };
	}
}
