package edu.iu.sci2.database.star.extract.common.guibuilder.attribute;

import java.util.Collection;
import java.util.Map;

import org.cishell.utilities.StringUtilities;
import org.cishell.utility.datastructure.datamodel.exception.UniqueNameException;
import org.cishell.utility.datastructure.datamodel.field.validation.FieldValidator;
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
import edu.iu.sci2.database.star.extract.common.guibuilder.DisplayErrorMessagesValidationAction;
import edu.iu.sci2.database.star.extract.common.guibuilder.GUIBuilder.DisableFinishedButtonAction;

public class AttributeWidgetContainer {
	public static final String DELETE_BUTTON_TEXT = "Delete";

	private SWTModel model;
	private int index;
	private ExpandableComponentWidget<AttributeWidgetContainer> componentWidget;

	private SWTModelField<String, Combo, DropDownDataSynchronizer<String>> aggregateFunction;
	private SWTModelField<String, Combo, DropDownDataSynchronizer<String>> coreEntityColumn;
	private SWTModelField<String, Text, TextDataSynchronizer> attributeNameField;

	private boolean userEnteredCustomResultColumnLabelName = false;

	public AttributeWidgetContainer(
			SWTModel model,
			String aggregateFunctionGroupName,
			String coreEntityColumnGroupName,
			Collection<String> coreEntityColumnLabels,
			Map<String, String> coreEntityColumnsByLabels,
			String resultColumnLabelGroupName,
			ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
			int index,
			int uniqueIndex,
			GridContainer grid,
			int style,
			FieldValidator<String> attributeNameValidator,
			Collection<FieldValidator<String>> otherValidators,
			DisableFinishedButtonAction disableFinishedButtonAction,
			DisplayErrorMessagesValidationAction displayErrorMessagesValidationAction)
			throws UniqueNameException {
		this.model = model;
		this.index = index;
		this.componentWidget = componentWidget;

		this.aggregateFunction = createAggregateFunction(
			this.model, aggregateFunctionGroupName, "" + uniqueIndex, componentWidget, grid);
		this.coreEntityColumn = createCoreEntityColumn(
			this.model,
			coreEntityColumnGroupName,
			"" + uniqueIndex,
			coreEntityColumnLabels,
			coreEntityColumnsByLabels,
			componentWidget,
			grid);
		this.attributeNameField = createAttributeNameField(
			this.model,
			resultColumnLabelGroupName,
			"" + uniqueIndex,
			componentWidget,
			uniqueIndex,
			grid,
			attributeNameValidator,
			otherValidators,
			disableFinishedButtonAction,
			displayErrorMessagesValidationAction);
		createDeleteButton(this.componentWidget, grid);

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
				SWTModel model,
				String groupName,
				String aggregateFunctionName,
				ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
				GridContainer grid) throws UniqueNameException {
		SWTModelField<String, Combo, DropDownDataSynchronizer<String>> aggregateFunction =
			model.addDropDown(
				aggregateFunctionName,
				"",
				groupName,
				0,
				AggregateFunction.FUNCTIONS_BY_HUMAN_READABLE_NAMES.keySet(),
				AggregateFunction.SQL_NAMES_BY_HUMAN_READABLE_NAMES,
				grid.getActualParent(),
				SWT.BORDER | SWT.READ_ONLY);
		aggregateFunction.getWidget().setLayoutData(createAggregateFunctionLayoutData());
		grid.addComponent(aggregateFunction.getWidget());

		return aggregateFunction;
	}

	private static GridData createAggregateFunctionLayoutData() {
		GridData layoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false);

		return layoutData;
	}

	private static SWTModelField<
			String, Combo, DropDownDataSynchronizer<String>> createCoreEntityColumn(
				SWTModel model,
				String groupName,
				String coreEntityColumnName,
				Collection<String> coreEntityColumnLabels,
				Map<String, String> coreEntityColumnsByLabels,
				ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
				GridContainer grid) throws UniqueNameException {
		SWTModelField<String, Combo, DropDownDataSynchronizer<String>> coreEntityColumn =
			model.addDropDown(
				coreEntityColumnName,
				"",
				groupName,
				0,
				coreEntityColumnLabels,
				coreEntityColumnsByLabels,
				grid.getActualParent(),
				SWT.BORDER | SWT.READ_ONLY);
		coreEntityColumn.getWidget().setLayoutData(createCoreEntityColumnLayoutData());
		grid.addComponent(coreEntityColumn.getWidget());

		return coreEntityColumn;
	}

	private static GridData createCoreEntityColumnLayoutData() {
		GridData layoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false);

		return layoutData;
	}

	private SWTModelField<
			String, Text, TextDataSynchronizer> createAttributeNameField(
				SWTModel model,
				String groupName,
				String resultColumnLabelName,
				ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
				int uniqueIndex,
				GridContainer grid,
				FieldValidator<String> attributeNameValidator,
				Collection<FieldValidator<String>> otherValidators,
				DisableFinishedButtonAction disableFinishedButtonAction,
				DisplayErrorMessagesValidationAction displayErrorMessagesValidationAction)
			throws UniqueNameException {
		SWTModelField<String, Text, TextDataSynchronizer> attributeNameField =
			model.addText(
				resultColumnLabelName,
				"",
				groupName,
				"RESULT" + uniqueIndex,
				false,
				grid.getActualParent(),
				SWT.BORDER);
		attributeNameField.getWidget().setLayoutData(createAttributeNameFieldLayoutData());
		grid.addComponent(attributeNameField.getWidget());
		attributeNameField.addValidator(attributeNameValidator);
		attributeNameField.addOtherValidators(otherValidators);
		attributeNameField.addValidationAction(disableFinishedButtonAction);
		attributeNameField.addValidationAction(displayErrorMessagesValidationAction);

		return attributeNameField;
	}

	private static GridData createAttributeNameFieldLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);

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

//	private static class AttributeNameValidator<ValueType> implements FieldValidationRule<ValueType> {
//		private String attributeNameGroup;
//
//		public AttributeNameValidator(String attributeNameGroup) {
//			this.attributeNameGroup = attributeNameGroup;
//		}
//
//		public void validateField(DataModelField<ValueType> field, DataModel model)
//				throws ModelValidationException {
//			DataModelGroup attributeNameGroup = model.getGroup(this.attributeNameGroup);
//			Map<Object, Integer> attributeNamesToCounts = new HashMap<Object, Integer>();
//
//			for (DataModelField<?> fieldInGroup : attributeNameGroup.getFields()) {
//				Object attributeName = fieldInGroup.getValue();
//
//				if (attributeNamesToCounts.containsKey(attributeName)) {
//					attributeNamesToCounts.put(
//						attributeName, attributeNamesToCounts.get(attributeName) + 1);
//				} else {
//					attributeNamesToCounts.put(attributeName, 1);
//				}
//			}
//
//			int attributeNameCount = attributeNamesToCounts.get(field.getValue());
//
//			if (attributeNameCount > 1) {
//				String exceptionMessage = String.format(
//					"You have %d attributes named '%s'.  All attributes must have unique names.",
//					attributeNameCount,
//					field.getName());
//				throw new ModelValidationException(exceptionMessage);
//			} else {
//			}
//		}
//
//		public void fieldDisposed(DataModelField<ValueType> field) {
//		}
//	}
}