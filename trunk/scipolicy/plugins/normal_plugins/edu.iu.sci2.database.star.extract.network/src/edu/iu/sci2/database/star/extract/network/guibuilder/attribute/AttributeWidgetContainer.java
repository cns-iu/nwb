package edu.iu.sci2.database.star.extract.network.guibuilder.attribute;

import java.util.Collection;

import org.cishell.utilities.MapUtilities;
import org.cishell.utilities.swt.ExpandableComponentWidget;
import org.cishell.utilities.swt.GridContainer;
import org.cishell.utilities.swt.model.GUIModel;
import org.cishell.utilities.swt.model.GUIModelField;
import org.cishell.utilities.swt.model.datasynchronizer.DropDownDataSynchronizer;
import org.cishell.utilities.swt.model.datasynchronizer.TextDataSynchronizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

import edu.iu.sci2.database.star.extract.network.attribute.AttributeFunction;

public class AttributeWidgetContainer {
	public static final String DELETE_BUTTON_TEXT = "Delete";

	private GUIModel model;
	private int index;

	private GUIModelField<String, Combo, DropDownDataSynchronizer> aggregateFunction;
	private GUIModelField<String, Combo, DropDownDataSynchronizer> coreEntityColumn;
	private GUIModelField<String, Text, TextDataSynchronizer> resultColumnLabelName;

	public AttributeWidgetContainer(
			GUIModel model,
			String aggregateFunctionName,
			String coreEntityColumnName,
			Collection<String> coreEntityColumns,
			String resultColumnLabelName,
			ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
			int index,
			int uniqueIndex,
			GridContainer grid,
			int style) {
		this.model = model;
		this.index = index;

//		setLayout(createLayout());
		this.aggregateFunction =
			createAggregateFunction(this.model, aggregateFunctionName, componentWidget, grid);
		this.coreEntityColumn = createCoreEntityColumn(
			this.model, coreEntityColumnName, coreEntityColumns, componentWidget, grid);
		this.resultColumnLabelName = createResultColumnLabelName(
			this.model, resultColumnLabelName, componentWidget, uniqueIndex, grid);
		createDeleteButton(componentWidget, grid);
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

//	private static GridLayout createLayout() {
//		GridLayout layout = new GridLayout(4, false);
//
//		return layout;
//	}

	private static GUIModelField<String, Combo, DropDownDataSynchronizer> createAggregateFunction(
			GUIModel model,
			String aggregateFunctionName,
			ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
			GridContainer grid) {
		GUIModelField<String, Combo, DropDownDataSynchronizer> aggregateFunction =
			model.addSingleSelectionDropDown(
				aggregateFunctionName,
				0,
				AttributeFunction.AGGREGATE_FUNCTION_NAMES,
				MapUtilities.mirror(AttributeFunction.AGGREGATE_FUNCTION_NAMES),
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
			String coreEntityColumnName,
			Collection<String> coreEntityColumns,
			ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
			GridContainer grid) {
		GUIModelField<String, Combo, DropDownDataSynchronizer> coreEntityColumn =
			model.addSingleSelectionDropDown(
				coreEntityColumnName,
				0,
				coreEntityColumns,
				MapUtilities.mirror(coreEntityColumns),
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
			String resultColumnLabelName,
			ExpandableComponentWidget<AttributeWidgetContainer> componentWidget,
			int uniqueIndex,
			GridContainer grid) {
		GUIModelField<String, Text, TextDataSynchronizer> resultColumnLabelNameField =
			model.addUnstyledText(
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
				selected(event);
			}

			public void widgetSelected(SelectionEvent event) {
				selected(event);
			}

			private void selected(SelectionEvent event) {
				componentWidget.removeComponent(AttributeWidgetContainer.this.index);
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