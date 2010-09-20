package edu.iu.sci2.database.star.extract.common.guibuilder.attribute;

import java.util.Collection;
import java.util.Map;

import org.cishell.utility.datastructure.datamodel.field.validation.FieldValidator;
import org.cishell.utility.swt.model.SWTModel;

import edu.iu.sci2.database.star.extract.common.guibuilder.DisplayErrorMessagesValidationAction;
import edu.iu.sci2.database.star.extract.common.guibuilder.GUIBuilder.DisableFinishedButtonAction;

public class AttributeWidgetProperties {
	public SWTModel model;
	public String aggregateFunctionGroupName;
	public String columnGroupName;
	public Collection<String> coreEntityColumnLabels;
	public Map<String, String> coreEntityColumnsByLabels;
	public String attributeNameGroupName;
	public Collection<FieldValidator<String>> aggregateFunctionValidators;
	public Collection<FieldValidator<String>> aggregateNameValidators;
	public Collection<FieldValidator<String>> allValidators;
	public DisableFinishedButtonAction disableFinishedButtonAction;
	public DisplayErrorMessagesValidationAction displayErrorMessagesValidationAction;
	public boolean allowCountDistinct;

	public AttributeWidgetProperties(
			SWTModel model,
			String aggregateFunctionGroupName,
			String columnGroupName,
			Collection<String> coreEntityColumnLabels,
			Map<String, String> coreEntityColumnsByLabels,
			String attributeNameGroupName,
			Collection<FieldValidator<String>> aggregateFunctionValidators,
			Collection<FieldValidator<String>> aggregateColumnValidators,
			Collection<FieldValidator<String>> aggregateNameValidators,
			Collection<FieldValidator<String>> allValidators,
			DisableFinishedButtonAction disableFinishedButtonAction,
			DisplayErrorMessagesValidationAction displayErrorMessagesValidationAction,
			boolean allowCountDistinct) {
		this.model = model;
		this.aggregateFunctionGroupName = aggregateFunctionGroupName;
		this.columnGroupName = columnGroupName;
		this.coreEntityColumnLabels = coreEntityColumnLabels;
		this.coreEntityColumnsByLabels = coreEntityColumnsByLabels;
		this.attributeNameGroupName = attributeNameGroupName;
		this.aggregateFunctionValidators = aggregateFunctionValidators;
		this.aggregateNameValidators = aggregateNameValidators;
		this.allValidators = allValidators;
		this.disableFinishedButtonAction = disableFinishedButtonAction;
		this.displayErrorMessagesValidationAction = displayErrorMessagesValidationAction;
		this.allowCountDistinct = allowCountDistinct;
	}
}