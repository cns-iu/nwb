package edu.iu.nwb.preprocessing.removeegraphattributes.nodes;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.nwb.preprocessing.removeegraphattributes.RemoveGraphAttributesAlgorithm;
import edu.iu.nwb.preprocessing.removeegraphattributes.RemoveGraphAttributesAlgorithmFactory;
import edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO.AttributeFilteringNWBWriter;
import edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO.RemovableAttributeReader;

public class RemoveNodeAttributesAlgorithmFactory
		extends RemoveGraphAttributesAlgorithmFactory {
	private LogService logger;
	public LogService getLogger() {
		return logger;
	}
	
	public Algorithm createAlgorithm(
			Data[] data, Dictionary parameters, CIShellContext context) {
		this.logger =
			(LogService) context.getService(LogService.class.getName());
		
		return new RemoveNodeAttributesAlgorithm(data, parameters, context);
	}
	
	public RemovableAttributeReader getAttributeReader(File inNWBFile)
			throws AlgorithmExecutionException {
		return new NWBRemovableNodeAttributeReader(inNWBFile);
	}
	
	
	private class RemoveNodeAttributesAlgorithm
			extends RemoveGraphAttributesAlgorithm {
		public RemoveNodeAttributesAlgorithm(
				Data[] data, Dictionary parameters, CIShellContext context) {
			super(data, parameters, context);
		}
		

		public RemovableAttributeReader createAttributeReader(
				File inputNWBFile) throws AlgorithmExecutionException {
			return new NWBRemovableNodeAttributeReader(inputNWBFile);
		}

		public AttributeFilteringNWBWriter createAttributeFilteringFileWriter(
				File outputNWBFile, Collection keysToRemove) throws IOException {
			return new NodeAttributeFilteringWriter(outputNWBFile, keysToRemove);
		}

		public String createOutDataLabel(Collection keysToRemove) {
			int numberOfRemovedAttributes = keysToRemove.size();
			if (numberOfRemovedAttributes == 0) {
				return "With no node attributes removed";
			} else {
				return ("With "
						+ numberOfRemovedAttributes
						+ " select node attributes removed.");
			}
		}
	}
}