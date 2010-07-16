package edu.iu.sci2.database.star.extract.network.guibuilder.attribute;

import java.util.Collection;
import java.util.Map;

import org.cishell.utilities.swt.ExpandableComponentWidget;
import org.cishell.utilities.swt.GridContainer;
import org.cishell.utilities.swt.ScrolledComponentFactory;
import org.cishell.utilities.swt.model.GUIModel;

public class AttributeWidgetFactory implements ScrolledComponentFactory<AttributeWidgetContainer> {
	private GUIModel model;
	private String aggregateFunctionGroupName;
	private String baseCoreEntityColumnGroupName;
	private Collection<String> coreEntityColumnLabels;
	private Map<String, String> coreEntityColumnsByLabels;
	private String resultColumnLabelGroupName;

	public AttributeWidgetFactory(
			GUIModel model,
			String aggregateFunctionGroupName,
			String baseCoreEntityColumnGroupName,
			Collection<String> coreEntityColumnLabels,
			Map<String, String> coreEntityColumnsByLabels,
			String resultColumnLabelGroupName) {
		this.model = model;
		this.aggregateFunctionGroupName = aggregateFunctionGroupName;
		this.baseCoreEntityColumnGroupName = baseCoreEntityColumnGroupName;
		this.coreEntityColumnLabels = coreEntityColumnLabels;
		this.coreEntityColumnsByLabels = coreEntityColumnsByLabels;
		this.resultColumnLabelGroupName = resultColumnLabelGroupName;
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
			this.aggregateFunctionGroupName,
			this.baseCoreEntityColumnGroupName,
			this.coreEntityColumnLabels,
			this.coreEntityColumnsByLabels,
			this.resultColumnLabelGroupName,
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