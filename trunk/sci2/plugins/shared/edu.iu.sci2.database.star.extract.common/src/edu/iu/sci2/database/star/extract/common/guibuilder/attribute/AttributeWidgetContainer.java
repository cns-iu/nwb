package edu.iu.sci2.database.star.extract.common.guibuilder.attribute;

import java.util.Map;

import org.cishell.utilities.StringUtilities;
import org.cishell.utility.datastructure.datamodel.exception.UniqueNameException;
import org.cishell.utility.swt.ExpandableComponentWidget;
import org.cishell.utility.swt.GridContainer;
import org.cishell.utility.swt.model.SWTModel;
import org.cishell.utility.swt.model.SWTModelField;
import org.cishell.utility.swt.model.datasynchronizer.DropDownDataSynchronizer;
import org.cishell.utility.swt.model.datasynchronizer.TextDataSynchronizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

import edu.iu.sci2.database.star.extract.common.aggregate.AggregateFunction;

public class AttributeWidgetContainer {
	public static final int AGGREGATE_FUNCTION_COLUMN_WIDTH = 45;
	public static final int CORE_ENTITY_COLUMN_WIDTH = 80;
	public static final int ATTRIBUTE_NAME_COLUMN_WIDTH = 110;

	public static final String DELETE_BUTTON_TEXT = "Delete";

	private SWTModel model;
	private int index;
	private ExpandableComponentWidget<AttributeWidgetContainer> componentWidget;

	private SWTModelField<String, Combo, DropDownDataSynchronizer<String>> aggregateFunction;
	private SWTModelField<String, Combo, DropDownDataSynchronizer<String>> coreEntityColumn;
	private SWTModelField<String, Text, TextDataSynchronizer> attributeNameField;

	private boolean userEnteredCustomResultColumnLabelName = false;

	public AttributeWidgetContainer(
			AttributeWidgetProperties properties,
			ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
			int index,
			int uniqueIndex,
			GridContainer scrolledAreaGrid,
			int style)
			throws UniqueNameException {
		this.model = properties.model;
		this.componentWidget = componentWidget;
		this.index = index;

		this.aggregateFunction = createAggregateFunction(
			properties, componentWidget, "" + uniqueIndex, scrolledAreaGrid);
		this.coreEntityColumn = createCoreEntityColumn(
			properties,
			componentWidget,
			"" + uniqueIndex,
			scrolledAreaGrid);
		this.attributeNameField = createAttributeNameField(
			properties, componentWidget, "" + uniqueIndex, uniqueIndex, scrolledAreaGrid);
		createDeleteButton(this.componentWidget, scrolledAreaGrid);

		SelectionListener suggestedResultColumnNameSelectionListener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				selected(event);
			}

			public void widgetSelected(SelectionEvent event) {
				selected(event);
			}

			private void selected(SelectionEvent event) {
				if (!AttributeWidgetContainer.this.userEnteredCustomResultColumnLabelName) {
					AttributeWidgetContainer.this.suggestName();
				}
			}
		};
		this.aggregateFunction.getWidget().addSelectionListener(
			suggestedResultColumnNameSelectionListener);
		this.coreEntityColumn.getWidget().addSelectionListener(
			suggestedResultColumnNameSelectionListener);

		suggestName();
		this.attributeNameField.getWidget().addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent event) {
				key(event);
			}

			public void keyReleased(KeyEvent event) {
				key(event);
			}

			public void key(KeyEvent event) {
				AttributeWidgetContainer.this.userEnteredCustomResultColumnLabelName = true;
			}
		});
	}

	public String getAggregateFunction() {
		return this.aggregateFunction.getValue();
	}

	public String getCoreEntityColumn() {
		return this.coreEntityColumn.getValue();
	}

	public String getResultColumnLabelName() {
		return this.attributeNameField.getValue();
	}

	public void reindex(int newIndex) {
		this.index = newIndex;
	}

	public void dispose() {
		this.componentWidget.removeComponent(AttributeWidgetContainer.this.index);
		this.aggregateFunction.dispose();
		this.coreEntityColumn.dispose();
		this.attributeNameField.dispose();
	}

	private void suggestName() {
		String aggregateFunction = suggestName_FixValue(this.aggregateFunction.getValue());
		String coreEntityColumn = suggestName_FixValue(this.coreEntityColumn.getValue());
		String value = String.format(
			"%s_%s", aggregateFunction, coreEntityColumn);
		this.attributeNameField.setValue(value);
	}

	private String suggestName_FixValue(String originalValue) {
		String[] tokenized =
			StringUtilities.tokenizeByWhitespace(originalValue);
		String reimplodedWithUnderscores = StringUtilities.implodeStringArray(tokenized, "_");

		return reimplodedWithUnderscores.toUpperCase();
	}

	private static SWTModelField<
			String, Combo, DropDownDataSynchronizer<String>> createAggregateFunction(
				AttributeWidgetProperties properties,
				ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
				String aggregateFunctionFieldName,
				GridContainer scrolledAreaGrid)
				throws UniqueNameException {
		Map<String, AggregateFunction> aggregateFunctionsByHumanReadableNames;
		Map<String, String> sqlNamesByHumanReadableNames;

		// TODO: Hacky.
		if (properties.allowCountDistinct) {
			aggregateFunctionsByHumanReadableNames =
				AggregateFunction.FUNCTIONS_BY_HUMAN_READABLE_NAMES;
			sqlNamesByHumanReadableNames = AggregateFunction.SQL_NAMES_BY_HUMAN_READABLE_NAMES;
		} else {
			aggregateFunctionsByHumanReadableNames =
				AggregateFunction.FUNCTIONS_WITHOUT_COUNT_DISTINCT_BY_HUMAN_READABLE_NAMES;
			sqlNamesByHumanReadableNames =
				AggregateFunction.SQL_NAMES_WITHOUT_COUNT_DISTINCT_BY_HUMAN_READABLE_NAMES;
		}

		SWTModelField<String, Combo, DropDownDataSynchronizer<String>> aggregateFunction =
			properties.model.addDropDown(
				aggregateFunctionFieldName,
				"",
				properties.aggregateFunctionGroupName,
				0,
				aggregateFunctionsByHumanReadableNames.keySet(),
				sqlNamesByHumanReadableNames,
//				AggregateFunction.FUNCTIONS_BY_HUMAN_READABLE_NAMES.keySet(),
//				AggregateFunction.SQL_NAMES_BY_HUMAN_READABLE_NAMES,
				scrolledAreaGrid.getActualParent(),
				SWT.BORDER | SWT.READ_ONLY);
		aggregateFunction.getWidget().setLayoutData(createAggregateFunctionLayoutData());
		scrolledAreaGrid.addComponent(aggregateFunction.getWidget());
		aggregateFunction.addValidators(properties.aggregateFunctionValidators);
		aggregateFunction.addOtherValidators(properties.allValidators);
		aggregateFunction.addValidationAction(properties.disableFinishedButtonAction);
		aggregateFunction.addValidationAction(properties.displayErrorMessagesValidationAction);

		return aggregateFunction;
	}

	private static GridData createAggregateFunctionLayoutData() {
		GridData layoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		layoutData.widthHint = AGGREGATE_FUNCTION_COLUMN_WIDTH;

		return layoutData;
	}

	private static SWTModelField<
			String, Combo, DropDownDataSynchronizer<String>> createCoreEntityColumn(
				AttributeWidgetProperties properties,
				ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
				String coreEntityColumnFieldName,
				GridContainer scrolledAreaGrid) throws UniqueNameException {
		SWTModelField<String, Combo, DropDownDataSynchronizer<String>> coreEntityColumn =
			properties.model.addDropDown(
				coreEntityColumnFieldName,
				"",
				properties.columnGroupName,
				0,
				properties.coreEntityColumnLabels,
				properties.coreEntityColumnsByLabels,
				scrolledAreaGrid.getActualParent(),
				SWT.BORDER | SWT.READ_ONLY);
		coreEntityColumn.getWidget().setLayoutData(createCoreEntityColumnLayoutData());
		scrolledAreaGrid.addComponent(coreEntityColumn.getWidget());

		return coreEntityColumn;
	}

	private static GridData createCoreEntityColumnLayoutData() {
		GridData layoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		layoutData.widthHint = CORE_ENTITY_COLUMN_WIDTH;

		return layoutData;
	}

	private SWTModelField<
			String, Text, TextDataSynchronizer> createAttributeNameField(
				AttributeWidgetProperties properties,
				ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
				String attributeFieldName,
				int uniqueIndex,
				GridContainer scrolledAreaGrid)
			throws UniqueNameException {
		SWTModelField<String, Text, TextDataSynchronizer> attributeNameField =
			model.addText(
				attributeFieldName,
				"",
				properties.attributeNameGroupName,
				"RESULT" + uniqueIndex,
				false,
				scrolledAreaGrid.getActualParent(),
				SWT.BORDER);
		attributeNameField.getWidget().setLayoutData(createAttributeNameFieldLayoutData());
		scrolledAreaGrid.addComponent(attributeNameField.getWidget());
		attributeNameField.addValidators(properties.aggregateNameValidators);
		attributeNameField.addOtherValidators(properties.allValidators);
		attributeNameField.addValidationAction(properties.disableFinishedButtonAction);
		attributeNameField.addValidationAction(properties.displayErrorMessagesValidationAction);

		return attributeNameField;
	}

	private static GridData createAttributeNameFieldLayoutData() {
		GridData layoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		layoutData.widthHint = ATTRIBUTE_NAME_COLUMN_WIDTH;

		return layoutData;
	}

	private Button createDeleteButton(
			final ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
			GridContainer grid) {
		Button deleteButton = new Button(grid.getActualParent(), SWT.PUSH);
		deleteButton.setLayoutData(createDeleteButtonLayoutData());
		deleteButton.setText(DELETE_BUTTON_TEXT);
		deleteButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				dispose();
			}

			public void widgetSelected(SelectionEvent event) {
				dispose();
			}
		});
		grid.addComponent(deleteButton);

		return deleteButton;
	}

	private static GridData createDeleteButtonLayoutData() {
		GridData layoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false);

		return layoutData;
	}
}