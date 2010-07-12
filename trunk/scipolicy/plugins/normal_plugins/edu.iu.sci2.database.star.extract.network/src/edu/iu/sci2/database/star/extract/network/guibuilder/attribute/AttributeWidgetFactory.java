package edu.iu.sci2.database.star.extract.network.guibuilder.attribute;

import java.util.Collection;
import java.util.Map;

import org.cishell.utilities.swt.ExpandableComponentWidget;
import org.cishell.utilities.swt.GridContainer;
import org.cishell.utilities.swt.ScrolledComponentFactory;
import org.cishell.utilities.swt.model.GUIModel;

public class AttributeWidgetFactory implements ScrolledComponentFactory<AttributeWidgetContainer> {
	private GUIModel model;
	private String aggregateFunctionBaseName;
	private String baseCoreEntityColumnBaseName;
	private Collection<String> coreEntityColumns;
	private String resultColumnLabelBaseName;

	public AttributeWidgetFactory(
			GUIModel model,
			String aggregateFunctionBaseName,
			String baseCoreEntityColumnBaseName,
			Collection<String> coreEntityColumns,
			String resultColumnLabelBaseName) {
		this.model = model;
		this.aggregateFunctionBaseName = aggregateFunctionBaseName;
		this.baseCoreEntityColumnBaseName = baseCoreEntityColumnBaseName;
		this.coreEntityColumns = coreEntityColumns;
		this.resultColumnLabelBaseName = resultColumnLabelBaseName;
	}

	public AttributeWidgetContainer constructWidget(
			ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
			GridContainer scrolledAreaGrid,
			int style,
			Map<String, Object> arguments,
			int index,
			int uniqueIndex) {
		return new AttributeWidgetContainer(
			model,
			this.aggregateFunctionBaseName + uniqueIndex,
			this.baseCoreEntityColumnBaseName + uniqueIndex,
			this.coreEntityColumns,
			this.resultColumnLabelBaseName + uniqueIndex,
			componentWidget,
			index,
			uniqueIndex,
			scrolledAreaGrid,
			style);
	}

	public void reindexComponent(AttributeWidgetContainer component, int newIndex) {
		component.reindex(newIndex);
	}
}