package edu.iu.sci2.database.star.load.parameter;

import java.io.IOException;

import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.cishell.utilities.mutateParameter.MetaAttributeDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.sci2.database.star.common.parameter.ParameterDescriptors;
import edu.iu.sci2.database.star.common.utility.CSVReaderUtilities;

public class ParameterFactory {
	public static ObjectClassDefinition createParameters(
			Data data, ObjectClassDefinition oldParameters) {
    	try {
			String[] header = CSVReaderUtilities.getHeader(data);
			BasicObjectClassDefinition newParameters =
				createParametersFromHeader(header, oldParameters);
			newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED, createCoreEntityNameAttribute());

			return newParameters;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
    }

	public static BasicObjectClassDefinition createParametersFromHeader(
			String[] header, ObjectClassDefinition oldParameters) {
		BasicObjectClassDefinition newParameters =
			MutateParameterUtilities.createNewParameters(oldParameters);

		for (String columnName : header) {
			MetaAttributeDefinition[] attributes = createParametersForColumn(columnName);

			for (MetaAttributeDefinition attribute : attributes) {
				newParameters.addAttributeDefinition(
					attribute.getType(), attribute.getAttributeDefinition());
			}
		}

		return newParameters;
	}

	public static MetaAttributeDefinition[] createParametersForColumn(String columnName) {
		AttributeDefinition typeAttribute = createTypeAttribute(columnName);
		AttributeDefinition multiValuedAttribute = createMultiValuedAttribute(columnName);
		AttributeDefinition mergeIdenticalAttribute = createMergeIdenticalAttribute(columnName);
		AttributeDefinition separatorAttribute = createSeparatorAttribute(columnName);

		return new MetaAttributeDefinition[] {
			new MetaAttributeDefinition(ObjectClassDefinition.REQUIRED, typeAttribute),
			new MetaAttributeDefinition(ObjectClassDefinition.REQUIRED, multiValuedAttribute),
			new MetaAttributeDefinition(
				ObjectClassDefinition.REQUIRED, mergeIdenticalAttribute),
			new MetaAttributeDefinition(ObjectClassDefinition.OPTIONAL, separatorAttribute),
		};
	}

	public static AttributeDefinition createTypeAttribute(String columnName) {
		return new BasicAttributeDefinition(
			ParameterDescriptors.Type.id(columnName),
			ParameterDescriptors.Type.name(columnName),
			ParameterDescriptors.Type.description(columnName),
			ParameterDescriptors.Type.valueType(),
			ParameterDescriptors.Type.optionLabels(),
			ParameterDescriptors.Type.optionValues());
	}

	public static AttributeDefinition createMultiValuedAttribute(String columnName) {
		return new BasicAttributeDefinition(
			ParameterDescriptors.SeparateEntity.id(columnName),
			ParameterDescriptors.SeparateEntity.name(columnName),
			ParameterDescriptors.SeparateEntity.description(columnName),
			ParameterDescriptors.SeparateEntity.valueType(),
			ParameterDescriptors.SeparateEntity.defaultValue());
	}

	public static AttributeDefinition createMergeIdenticalAttribute(String columnName) {
		return new BasicAttributeDefinition(
			ParameterDescriptors.MergeIdentical.id(columnName),
			ParameterDescriptors.MergeIdentical.name(columnName),
			ParameterDescriptors.MergeIdentical.description(columnName),
			ParameterDescriptors.MergeIdentical.valueType(),
			ParameterDescriptors.MergeIdentical.defaultValue());
	}

	public static AttributeDefinition createSeparatorAttribute(String columnName) {
		return new BasicAttributeDefinition(
			ParameterDescriptors.Separator.id(columnName),
			ParameterDescriptors.Separator.name(columnName),
			ParameterDescriptors.Separator.description(columnName),
			ParameterDescriptors.Separator.valueType(),
			ParameterDescriptors.Separator.defaultValue());
	}

	public static AttributeDefinition createCoreEntityNameAttribute() {
		BasicAttributeDefinition parameter = new BasicAttributeDefinition(
			ParameterDescriptors.CoreEntityName.CORE_ENTITY_NAME_ID,
			ParameterDescriptors.CoreEntityName.CORE_ENTITY_NAME_NAME,
			ParameterDescriptors.CoreEntityName.CORE_ENTITY_NAME_DESCRIPTION,
			ParameterDescriptors.CoreEntityName.CORE_ENTITY_NAME_TYPE,
			ParameterDescriptors.CoreEntityName.DEFAULT_CORE_ENTITY_NAME_VALUE);

		return parameter;
	}
}