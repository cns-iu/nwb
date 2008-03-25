package edu.iu.nwb.visualization.drl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.DataValidator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicMetaTypeProvider;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.GetNWBFileMetadata;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;


public class VxOrdAlgorithmFactory implements AlgorithmFactory, DataValidator {
    private MetaTypeInformation provider;
    private BundleContext bContext;
    private String pid;

    protected void activate(ComponentContext ctxt) {
        MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
        provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());
		pid = (String) ctxt.getServiceReference().getProperty(org.osgi.framework.Constants.SERVICE_PID);
        bContext = ctxt.getBundleContext();
    }
    protected void deactivate(ComponentContext ctxt) {
        provider = null;
    }
    
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new VxOrdAlgorithm(data, parameters, context, bContext);
    }
    
	public String validate(Data[] data) {
		File nwbFile = (File) data[0].getData();
		GetNWBFileMetadata handler = new GetNWBFileMetadata();
		
		try {
			NWBFileParser parser = new NWBFileParser(nwbFile);
			parser.parse(handler);
		} catch (IOException e1) {
			return "Invalid nwb file";
		} catch (ParsingException e) {
			return "Invalid nwb file";
		}
		
		if (handler.getUndirectedEdgeSchema() != null) {
			return "";
		} else {
			return "DrL can only process undirected networks."; 
		}
	}
    
	//Adapted from edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaAlgorithmFactory
	public MetaTypeProvider createParameters(Data[] dm) {
		File nwbFile = (File) dm[0].getData();
		GetNWBFileMetadata handler = new GetNWBFileMetadata();
		
		try {
			NWBFileParser parser = new NWBFileParser(nwbFile);
			parser.parse(handler);
		} catch (IOException e1) {
			return provider;
		} catch (ParsingException e) {
			return provider;
		}
		
		String[] pids = provider.getPids();
		Arrays.sort(pids);
		if(Arrays.binarySearch(pids, this.pid) >= 0) {
			ObjectClassDefinition oldDefinition = provider.getObjectClassDefinition(this.pid, null);

			BasicObjectClassDefinition definition;
			try {
				definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), oldDefinition.getIcon(16));
			} catch (IOException e) {
				definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), null);
			}

			String[] edgeAttributesArray = createKeyArray(handler.getUndirectedEdgeSchema());

			AttributeDefinition[] definitions = oldDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL);

			for(int ii = 0; ii < definitions.length; ii++) {
				String id = definitions[ii].getID();
				if(id.equals("edgeWeight")) {
					definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
							new BasicAttributeDefinition(definitions[ii].getID(), 
									definitions[ii].getName(), 
									definitions[ii].getDescription(), 
									definitions[ii].getType(), 
									edgeAttributesArray, 
									edgeAttributesArray));
				}  else {
					definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, definitions[ii]);
				}
			}

			MetaTypeProvider provider = new BasicMetaTypeProvider(definition);
			return provider;
		} else {
			return null;
		}
	}
	
	private String[] createKeyArray(Map schema) {
		List goodkeys = new ArrayList();
		
		for (Iterator keys = schema.keySet().iterator(); keys.hasNext(); ) {
			String key = ""+keys.next();
			if (!schema.get(key).equals(NWBFileProperty.TYPE_STRING)) {
				goodkeys.add(key);
			}
		}
		
		Collections.reverse(goodkeys);
		
		return (String[]) goodkeys.toArray(new String[]{});
	}
}