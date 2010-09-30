package edu.iu.nwb.composite.extractauthorpapernetwork.algorithm;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.nwb.composite.extractauthorpapernetwork.metadata.AuthorPaperFormat;

public class ExtractAuthorPaperNetworkFactory implements AlgorithmFactory, ParameterMutator {
	public static final String FILE_FORMAT_ID = "fileFormat";

	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		String fileFormat = parameters.get(FILE_FORMAT_ID).toString();
		Data inData = data[0];
		Table table = (Table) inData.getData();
		LogService logger = (LogService) ciShellContext.getService(LogService.class.getName());

		return new ExtractAuthorPaperNetwork(table, fileFormat, inData, logger);
	}
	
	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {

		BasicObjectClassDefinition newParameters =
			MutateParameterUtilities.createNewParameters(oldParameters);

		String[] supportedFormatOptions =
			AuthorPaperFormat.FORMATS_SUPPORTED.toArray(new String[0]);

		newParameters.addAttributeDefinition(
			ObjectClassDefinition.REQUIRED,
			new BasicAttributeDefinition(
				FILE_FORMAT_ID,
				"File Format",
				"The file format of the original data.",
				AttributeDefinition.STRING,
				supportedFormatOptions, supportedFormatOptions));



		return newParameters;	
	}
}