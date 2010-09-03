package edu.iu.sci2.database.star.extract.common.guibuilder.attribute;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cishell.utility.datastructure.datamodel.field.validation.FieldValidator;
import org.cishell.utility.swt.ExpandableComponentWidget;
import org.cishell.utility.swt.WidgetConstructionException;
import org.cishell.utility.swt.model.SWTModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import edu.iu.sci2.database.star.extract.common.guibuilder.DisplayErrorMessagesValidationAction;
import edu.iu.sci2.database.star.extract.common.guibuilder.GUIBuilder.DisableFinishedButtonAction;

public class AttributeListWidget extends ExpandableComponentWidget<AttributeWidgetContainer> {
	public static final String ADD_ATTRIBUTE_BUTTON_TEXT_FORMAT = "Add Another %s Attribute";
	public static final String REMOVE_ALL_BUTTON_TEXT_FORMAT = "Remove All %s Fields";

	public static final String AGGREGATE_FUNCTION_LABEL_TEXT = "Summarize By:";
	public static final String CORE_ENTITY_COLUMN_LABEL_TEXT = "Column to Summarize:";
	public static final String RESULT_COLUMN_LABEL_TEXT = "Attribute Name:";

	public AttributeListWidget(
			SWTModel model,
			String aggregateFunctionGroupName,
			String coreEntityColumnGroupName,
			Collection<String> coreEntityColumnLabels,
			Map<String, String> coreEntityColumnsByLabels,
			String attributeNameGroupName,
			String type,
			Composite parent,
			FieldValidator<String> attributeNameValidator,
			Collection<FieldValidator<String>> otherValidators,
			DisableFinishedButtonAction disableFinishedButtonAction,
			DisplayErrorMessagesValidationAction displayErrorMessagesValidationAction) {
		super(
			parent,
			new AttributeWidgetFactory(
				model,
				aggregateFunctionGroupName,
				coreEntityColumnGroupName,
				coreEntityColumnLabels,
				coreEntityColumnsByLabels,
				attributeNameGroupName,
				attributeNameValidator,
				otherValidators,
				disableFinishedButtonAction,
				displayErrorMessagesValidationAction));
		Composite headerArea = getHeaderArea();
		createAddAttributeButton(headerArea, type);
		createRemoveAllAttributesButton(headerArea, type);
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
				try {
					AttributeListWidget.this.addComponent(SWT.NONE, null);
				} catch (WidgetConstructionException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		});

		return addAttributeButton;
	}

	private static GridData createAddAttributeButtonLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, true);

		return layoutData;
	}

	private Button createRemoveAllAttributesButton(Composite parent, String type) {
		Button removeAllAttributesButton = new Button(parent, SWT.PUSH);
		removeAllAttributesButton.setLayoutData(createRemoveAllAttributesButtonLayoutData());
		removeAllAttributesButton.setText(String.format(REMOVE_ALL_BUTTON_TEXT_FORMAT, type));
		removeAllAttributesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				selected(event);
			}

			public void widgetSelected(SelectionEvent event) {
				selected(event);
			}

			private void selected(SelectionEvent event) {
				/* We loop this way because dispose actually removes components from the Collection
				 * getComponents() returns!
				 */
				while (AttributeListWidget.this.getComponents().size() > 0) {
					AttributeListWidget.this.getComponents().get(0).dispose();
				}
			}
		});

		return removeAllAttributesButton;
	}

	private static GridData createRemoveAllAttributesButtonLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, true);

		return layoutData;
	}
}