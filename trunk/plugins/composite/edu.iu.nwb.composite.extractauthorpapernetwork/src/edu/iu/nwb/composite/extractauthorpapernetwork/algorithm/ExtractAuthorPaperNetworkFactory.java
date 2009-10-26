package edu.iu.nwb.composite.extractauthorpapernetwork.algorithm;

import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.composite.extractauthorpapernetwork.metadata.SupportedFileTypes;

public class ExtractAuthorPaperNetworkFactory implements AlgorithmFactory, ParameterMutator,SupportedFileTypes {
	private MetaTypeInformation originalProvider;
	private String pid;

	
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition parameters) {

		ObjectClassDefinition oldDefinition = originalProvider.getObjectClassDefinition(this.pid, null);

		BasicObjectClassDefinition definition;
		try {
			definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), oldDefinition.getIcon(16));
		} catch (IOException e) {
			definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), null);
		}

		String[] supportedFormats = SupportedFileTypes.supportedFormats;

		definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
				new BasicAttributeDefinition("fileFormat", "File Format", "The file format of the original data.", AttributeDefinition.STRING, supportedFormats, supportedFormats));



		return definition;	
	}

	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		return new ExtractAuthorPaperNetwork(data, parameters, context);
	}
}