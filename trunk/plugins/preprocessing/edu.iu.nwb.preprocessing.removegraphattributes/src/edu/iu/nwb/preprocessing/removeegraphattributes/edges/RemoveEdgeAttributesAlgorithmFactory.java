package edu.iu.nwb.preprocessing.removeegraphattributes.edges;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.nwb.preprocessing.removeegraphattributes.RemoveGraphAttributesAlgorithm;
import edu.iu.nwb.preprocessing.removeegraphattributes.RemoveGraphAttributesAlgorithmFactory;
import edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO.AttributeFilteringNWBWriter;
import edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO.NWBRemovableAttributeReader;

public class RemoveEdgeAttributesAlgorithmFactory
		extends RemoveGraphAttributesAlgorithmFactory {
	private LogService logger;
	public LogService getLogger() {
		return logger;
	}
	
	
	public Algorithm createAlgorithm(
			Data[] data, Dictionary parameters, CIShellContext context) {
		this.logger =
			(LogService) context.getService(LogService.class.getName());
		
		return new RemoveEdgeAttributesAlgorithm(data, parameters, context);
	}
	
	
	public NWBRemovableAttributeReader createAttributeReader(File inNWBFile) {
		return new NWBRemovableEdgeAttributeReader(inNWBFile);
	}	
	
	
	private class RemoveEdgeAttributesAlgorithm extends RemoveGraphAttributesAlgorithm {
		public RemoveEdgeAttributesAlgorithm(
				Data[] data, Dictionary parameters, CIShellContext context) {
			super(data, parameters, context);
		}
		

		public NWBRemovableAttributeReader createAttributeReader(File inputNWBFile) {
			return new NWBRemovableEdgeAttributeReader(inputNWBFile);
		}

		public AttributeFilteringNWBWriter createAttributeFilteringFileWriter(
				File outputNWBFile,
				Collection keysToRemove)
					throws IOException {
			return new EdgeAttributeFilteringWriter(
					outputNWBFile, keysToRemove);
		}		

		public String createOutDataLabel(Collection keysToRemove) {
			int numberOfRemovedAttributes = keysToRemove.size();
			if (numberOfRemovedAttributes == 0) {
				return "With no edge attributes removed";
			} else {
				return ("With "
						+ numberOfRemovedAttributes
						+ " select edge attributes removed.");
			}
		}
	}
}