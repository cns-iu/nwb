package edu.iu.sci2.database.star.extract.network.guibuilder.attribute;

import java.util.Collection;
import java.util.Map;

import org.cishell.utility.datastructure.datamodel.exception.UniqueNameException;
import org.cishell.utility.swt.ExpandableComponentWidget;
import org.cishell.utility.swt.GridContainer;
import org.cishell.utility.swt.ScrolledComponentFactory;
import org.cishell.utility.swt.WidgetConstructionException;
import org.cishell.utility.swt.model.SWTModel;

import edu.iu.sci2.database.star.extract.network.guibuilder.GUIBuilder.DisableFinishedButtonAction;

public class AttributeWidgetFactory implements ScrolledComponentFactory<AttributeWidgetContainer> {
	private SWTModel model;
	private String aggregateFunctionGroupName;
	private String baseCoreEntityColumnGroupName;
	private Collection<String> coreEntityColumnLabels;
	private Map<String, String> coreEntityColumnsByLabels;
	private String resultColumnLabelGroupName;
	private DisableFinishedButtonAction disableFinishedButtonAction;

	public AttributeWidgetFactory(
			SWTModel model,
			String aggregateFunctionGroupName,
			String baseCoreEntityColumnGroupName,
			Collection<String> coreEntityColumnLabels,
			Map<String, String> coreEntityColumnsByLabels,
			String resultColumnLabelGroupName,
			DisableFinishedButtonAction disableFinishedButtonAction) {
		this.model = model;
		this.aggregateFunctionGroupName = aggregateFunctionGroupName;
		this.baseCoreEntityColumnGroupName = baseCoreEntityColumnGroupName;
		this.coreEntityColumnLabels = coreEntityColumnLabels;
		this.coreEntityColumnsByLabels = coreEntityColumnsByLabels;
		this.resultColumnLabelGroupName = resultColumnLabelGroupName;
		this.disableFinishedButtonAction = disableFinishedButtonAction;
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
				disableFinishedButtonAction);
		} catch (UniqueNameException e) {
			throw new WidgetConstructionException(e.getMessage(), e);
		}
	}

	public void reindexComponent(AttributeWidgetContainer component, int newIndex) {
		component.reindex(newIndex);
	}
}