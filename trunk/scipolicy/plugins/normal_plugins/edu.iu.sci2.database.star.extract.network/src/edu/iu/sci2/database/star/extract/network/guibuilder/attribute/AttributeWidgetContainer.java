package edu.iu.sci2.database.star.extract.network.guibuilder.attribute;

import java.util.Collection;
import java.util.Map;

import org.cishell.utilities.StringUtilities;
import org.cishell.utility.swt.ExpandableComponentWidget;
import org.cishell.utility.swt.GridContainer;
import org.cishell.utility.swt.model.GUIModel;
import org.cishell.utility.swt.model.GUIModelField;
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

import edu.iu.sci2.database.star.extract.network.aggregate.AggregateFunction;

public class AttributeWidgetContainer {
	public static final String DELETE_BUTTON_TEXT = "Delete";

	private GUIModel model;
	private int index;
	private ExpandableComponentWidget<AttributeWidgetContainer> componentWidget;

	private GUIModelField<String, Combo, DropDownDataSynchronizer> aggregateFunction;
	private GUIModelField<String, Combo, DropDownDataSynchronizer> coreEntityColumn;
	private GUIModelField<String, Text, TextDataSynchronizer> resultColumnLabelName;

	private boolean userEnteredCustomResultColumnLabelName = false;

	public AttributeWidgetContainer(
			GUIModel model,
			String aggregateFunctionGroupName,
			String coreEntityColumnGroupName,
			Collection<String> coreEntityColumnLabels,
			Map<String, String> coreEntityColumnsByLabels,
			String resultColumnLabelGroupName,
			ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
			int index,
			int uniqueIndex,
			GridContainer grid,
			int style) {
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
		this.resultColumnLabelName = createResultColumnLabelName(
			this.model,
			resultColumnLabelGroupName,
			"" + uniqueIndex,
			componentWidget,
			uniqueIndex,
			grid);
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
		this.resultColumnLabelName.getWidget().addKeyListener(new KeyListener() {
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
		return this.resultColumnLabelName.getValue();
	}

	public void reindex(int newIndex) {
		this.index = newIndex;
	}

	public void dispose() {
		this.componentWidget.removeComponent(AttributeWidgetContainer.this.index);
		this.model.removeField(this.aggregateFunction);
		this.model.removeField(this.coreEntityColumn);
		this.model.removeField(this.resultColumnLabelName);
	}

	private void suggestName() {
		String aggregateFunction = suggestName_FixValue(this.aggregateFunction.getValue());
		String coreEntityColumn = suggestName_FixValue(this.coreEntityColumn.getValue());
		String value = String.format(
			"%s_%s", aggregateFunction, coreEntityColumn);
		this.resultColumnLabelName.setValue(value);
	}

	private String suggestName_FixValue(String originalValue) {
		String[] tokenized =
			StringUtilities.tokenizeByWhitespace(originalValue);
		String reimplodedWithUnderscores = StringUtilities.implodeStringArray(tokenized, "_");

		return reimplodedWithUnderscores.toUpperCase();
	}

	private static GUIModelField<String, Combo, DropDownDataSynchronizer> createAggregateFunction(
			GUIModel model,
			String groupName,
			String aggregateFunctionName,
			ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
			GridContainer grid) {
		GUIModelField<String, Combo, DropDownDataSynchronizer> aggregateFunction =
			model.addDropDown(
				groupName,
				aggregateFunctionName,
				0,
				AggregateFunction.FUNCTIONS_BY_HUMAN_READABLE_NAMES.keySet(),
				AggregateFunction.SQL_NAMES_BY_HUMAN_READABLE_NAMES,
//				MapUtilities.mirror(AggregateFunction.FUNCTIONS_BY_HUMAN_READABLE_NAMES.keySet()),
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

	private static GUIModelField<String, Combo, DropDownDataSynchronizer> createCoreEntityColumn(
			GUIModel model,
			String groupName,
			String coreEntityColumnName,
			Collection<String> coreEntityColumnLabels,
			Map<String, String> coreEntityColumnsByLabels,
			ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
			GridContainer grid) {
		GUIModelField<String, Combo, DropDownDataSynchronizer> coreEntityColumn =
			model.addDropDown(
				groupName,
				coreEntityColumnName,
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

	private static GUIModelField<String, Text, TextDataSynchronizer> createResultColumnLabelName(
			GUIModel model,
			String groupName,
			String resultColumnLabelName,
			ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
			int uniqueIndex,
			GridContainer grid) {
		GUIModelField<String, Text, TextDataSynchronizer> resultColumnLabelNameField =
			model.addText(
				groupName,
				resultColumnLabelName,
				"RESULT" + uniqueIndex,
				false,
				grid.getActualParent(),
				SWT.BORDER);
		resultColumnLabelNameField.getWidget().setLayoutData(
			createResultColumnLabelNameLayoutData());
		grid.addComponent(resultColumnLabelNameField.getWidget());

		return resultColumnLabelNameField;
	}

	private static GridData createResultColumnLabelNameLayoutData() {
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
}