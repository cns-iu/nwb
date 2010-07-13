package edu.iu.sci2.database.star.extract.network.guibuilder.attribute;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.cishell.utilities.swt.ExpandableComponentWidget;
import org.cishell.utilities.swt.model.GUIModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class AttributeListWidget extends ExpandableComponentWidget<AttributeWidgetContainer> {
	public static final String ADD_ATTRIBUTE_BUTTON_TEXT_FORMAT = "Add Another %s Attribute Field";

	public static final String AGGREGATE_FUNCTION_LABEL_TEXT = "Summarize By:";
	public static final String CORE_ENTITY_COLUMN_LABEL_TEXT = "Column to Summarize:";
	public static final String RESULT_COLUMN_LABEL_TEXT = "Attribute Name:";

	public AttributeListWidget(
			GUIModel model,
			String aggregateFunctionBaseName,
			String coreEntityColumnBaseName,
			Collection<String> coreEntityColumns,
			String resultColumnLabelBaseName,
			String type,
			Composite parent) {
		super(
			parent,
			new AttributeWidgetFactory(
				model,
				aggregateFunctionBaseName,
				coreEntityColumnBaseName,
				coreEntityColumns,
				resultColumnLabelBaseName));
		createAddAttributeButton(getHeaderArea(), type);
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Collection<String> createColumnLabelTexts() {
		List<String> columnLabelTexts = Arrays.asList(
			AGGREGATE_FUNCTION_LABEL_TEXT,
			CORE_ENTITY_COLUMN_LABEL_TEXT,
			RESULT_COLUMN_LABEL_TEXT,
			"");

		return columnLabelTexts;
	}

	@Override
	protected Composite createFooterArea() {
		return null;
	}

	private Button createAddAttributeButton(Composite parent, String type) {
		Button addAttributeButton = new Button(parent, SWT.PUSH);
		addAttributeButton.setLayoutData(createAddAttributeButtonLayoutData());
		addAttributeButton.setText(String.format(ADD_ATTRIBUTE_BUTTON_TEXT_FORMAT, type));
		addAttributeButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				selected(event);
			}

			public void widgetSelected(SelectionEvent event) {
				selected(event);
			}

			private void selected(SelectionEvent event) {
				AttributeListWidget.this.addComponent(SWT.NONE, null);
			}
		});

		return addAttributeButton;
	}

	private static GridData createAddAttributeButtonLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, true);

		return layoutData;
	}
}