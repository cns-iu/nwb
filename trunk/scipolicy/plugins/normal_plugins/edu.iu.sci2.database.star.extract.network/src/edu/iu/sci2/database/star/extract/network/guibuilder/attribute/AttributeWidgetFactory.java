package edu.iu.sci2.database.star.extract.network.guibuilder.attribute;

import java.util.Collection;
import java.util.Map;

import org.cishell.utility.datastructure.datamodel.exception.UniqueNameException;
import org.cishell.utility.datastructure.datamodel.field.validation.FieldValidator;
import org.cishell.utility.swt.ExpandableComponentWidget;
import org.cishell.utility.swt.GridContainer;
import org.cishell.utility.swt.ScrolledComponentFactory;
import org.cishell.utility.swt.WidgetConstructionException;
import org.cishell.utility.swt.model.SWTModel;

import edu.iu.sci2.database.star.extract.network.guibuilder.DisplayErrorMessagesValidationAction;
import edu.iu.sci2.database.star.extract.network.guibuilder.GUIBuilder.DisableFinishedButtonAction;

public class AttributeWidgetFactory implements ScrolledComponentFactory<AttributeWidgetContainer> {
	private SWTModel model;
	private String aggregateFunctionGroupName;
	private String baseCoreEntityColumnGroupName;
	private Collection<String> coreEntityColumnLabels;
	private Map<String, String> coreEntityColumnsByLabels;
	private String resultColumnLabelGroupName;
	private FieldValidator<String> attributeNameValidator;
	private Collection<FieldValidator<String>> otherValidators;
	private DisableFinishedButtonAction disableFinishedButtonAction;
	private DisplayErrorMessagesValidationAction displayErrorMessagesValidationAction;

	public AttributeWidgetFactory(
			SWTModel model,
			String aggregateFunctionGroupName,
			String baseCoreEntityColumnGroupName,
			Collection<String> coreEntityColumnLabels,
			Map<String, String> coreEntityColumnsByLabels,
			String resultColumnLabelGroupName,
			FieldValidator<String> attributeNameValidator,
			Collection<FieldValidator<String>> otherValidators,
			DisableFinishedButtonAction disableFinishedButtonAction,
			DisplayErrorMessagesValidationAction displayErrorMessagesValidationAction) {
		this.model = model;
		this.aggregateFunctionGroupName = aggregateFunctionGroupName;
		this.baseCoreEntityColumnGroupName = baseCoreEntityColumnGroupName;
		this.coreEntityColumnLabels = coreEntityColumnLabels;
		this.coreEntityColumnsByLabels = coreEntityColumnsByLabels;
		this.resultColumnLabelGroupName = resultColumnLabelGroupName;
		this.attributeNameValidator = attributeNameValidator;
		this.otherValidators = otherValidators;
		this.disableFinishedButtonAction = disableFinishedButtonAction;
		this.displayErrorMessagesValidationAction = displayErrorMessagesValidationAction;
	}

	public AttributeWidgetContainer constructWidget(
			ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
			GridContainer scrolledAreaGrid,
			int style,
			Map<String, Object> arguments,
			int index,
			int uniqueIndex) throws WidgetConstructionException {
		try {
			return new AttributeWidgetContainer(
				model,
				this.aggregateFunctionGroupName,
				this.baseCoreEntityColumnGroupName,
				this.coreEntityColumnLabels,
				this.coreEntityColumnsByLabels,
				this.resultColumnLabelGroupName,
				componentWidget,
				index,
				uniqueIndex,
				scrolledAreaGrid,
				style,
				this.attributeNameValidator,
				this.otherValidators,
				this.disableFinishedButtonAction,
				this.displayErrorMessagesValidationAction);
		} catch (UniqueNameException e) {
			throw new WidgetConstructionException(e.getMessage(), e);
		}
	}

	public void reindexComponent(AttributeWidgetContainer component, int newIndex) {
		component.reindex(newIndex);
	}
}