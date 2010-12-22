package edu.iu.sci2.database.star.extract.common.guibuilder.attribute;

import java.util.Map;

import org.cishell.utility.datastructure.datamodel.exception.UniqueNameException;
import org.cishell.utility.swt.ExpandableComponentWidget;
import org.cishell.utility.swt.GridContainer;
import org.cishell.utility.swt.ScrolledComponentFactory;
import org.cishell.utility.swt.WidgetConstructionException;

public class AttributeWidgetFactory
		implements ScrolledComponentFactory<AttributeWidgetContainer> {
	private AttributeWidgetProperties properties;

	public AttributeWidgetFactory(AttributeWidgetProperties properties) {
		this.properties = properties;
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
				this.properties, componentWidget, index, uniqueIndex, scrolledAreaGrid, style);
		} catch (UniqueNameException e) {
			throw new WidgetConstructionException(e.getMessage(), e);
		}
	}

	public void reindexComponent(AttributeWidgetContainer component, int newIndex) {
		component.reindex(newIndex);
	}
}