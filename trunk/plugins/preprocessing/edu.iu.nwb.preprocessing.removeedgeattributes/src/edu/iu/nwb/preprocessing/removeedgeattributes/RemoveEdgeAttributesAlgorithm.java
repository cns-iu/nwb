package edu.iu.nwb.preprocessing.removeedgeattributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;

/* This code assumes the input file's edge table will not contain duplicate
 * column names, even though the NWB file format specification does
 * not seem to specifically disallow having both "foo*int" and "foo*string".
 * 
 * This assumption is borne out by that the nwbutils package considers
 * the node and edge schemas to implement Map.  It will also simplify the
 * user interface.
 * 
 * No guarantees are made for when the input file contains such duplicates.
 */

public class RemoveEdgeAttributesAlgorithm implements Algorithm {
    public static final String OUT_DATA_LABEL =
    	"With select edge attributes removed";
	private Data[] data;
	private Dictionary parameters;
    protected static LogService logger;
    
    
	public RemoveEdgeAttributesAlgorithm(Data[] data,
											Dictionary parameters,
											CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        
        RemoveEdgeAttributesAlgorithm.logger =
        		(LogService) context.getService(LogService.class.getName());
    }

	
	public Data[] execute() throws AlgorithmExecutionException {    	
    	try {
			File inputNWBFile = (File) data[0].getData();
			// TODO: Use NWBFileUtilities blah blah.
			File outputNWBFile = File.createTempFile("nwb-", ".nwb");
			
			NWBEdgeAttributeReader attributeReader =
									new NWBEdgeAttributeReader(inputNWBFile);
			List removableKeys = attributeReader.getRemovableAttributeKeys();
			
			List selectedKeys = getSelectedKeys(removableKeys, parameters);
			
			NWBFileParser parser = new NWBFileParser(inputNWBFile);
			EdgeAttributeFilteringWriter filteringWriter =
				new EdgeAttributeFilteringWriter(outputNWBFile, selectedKeys);
			parser.parse(filteringWriter);
			
			if ( selectedKeys.isEmpty() ) {
				logger.log(LogService.LOG_INFO, "No edge attributes removed.");
			}
			else {
				logger.log(LogService.LOG_INFO,
					"Removed edge attributes named: "
					+ createCommaSeparatedString(selectedKeys)
					+ ".");
			}
			
			return createOutData(outputNWBFile);
    	}
    	catch(IOException e) {
    		throw new AlgorithmExecutionException(e);
    	} catch (ParsingException e) {
    		throw new AlgorithmExecutionException(e);
		}
    }
	
	private List getSelectedKeys(List removableKeys, Dictionary parameters)
			throws AlgorithmExecutionException {
		List selectedKeys = new ArrayList();
		
		for (Iterator keyIt = removableKeys.iterator(); keyIt.hasNext();) {
			String key = (String) keyIt.next();
			Object value = parameters.get(key);
			
			if (value == null) {
				throw new AlgorithmExecutionException(
					"Attribute key has no corresponding AD parameter.");
			}
			else {
				boolean selected = ((Boolean) value).booleanValue();
				
				if (selected) {
					selectedKeys.add(key);
				}
			}
		}
		
		return selectedKeys;
	}


	private String createCommaSeparatedString(List list) {
		String s = "";
		
		Iterator listIt = list.iterator();		
		if ( listIt.hasNext() ) {
			Object first = listIt.next();
			s += first;
			
			while ( listIt.hasNext() ) {
				Object o = listIt.next();
				s += ", ";
				s += o;
			}
		}
		
		return s;
	}

	private Data[] createOutData(File outputNWBFile) {
		Data outData = new BasicData(outputNWBFile,
									 NWBFileProperty.NWB_MIME_TYPE);
		outData.getMetadata().put(DataProperty.LABEL, OUT_DATA_LABEL);
		outData.getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		outData.getMetadata().put(DataProperty.PARENT, data[0]);
		
		return new Data[] { outData };
	}
}