package edu.iu.sci2.database.star.extract.common.guibuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.cishell.utility.datastructure.ObjectContainer;
import org.cishell.utility.datastructure.datamodel.DataModel;
import org.cishell.utility.datastructure.datamodel.exception.UniqueNameException;
import org.cishell.utility.datastructure.datamodel.field.validation.BasicFieldValidator;
import org.cishell.utility.datastructure.datamodel.field.validation.EmptyTextFieldValidationRule;
import org.cishell.utility.datastructure.datamodel.field.validation.FieldValidationAction;
import org.cishell.utility.datastructure.datamodel.field.validation.FieldValidationRule;
import org.cishell.utility.datastructure.datamodel.field.validation.FieldValidator;
import org.cishell.utility.datastructure.datamodel.field.validation.OneOccurrenceOfValueValidationRule;
import org.cishell.utility.datastructure.datamodel.field.validation.UniqueValueValidationRule;
import org.cishell.utility.swt.GUIBuilderUtilities;
import org.cishell.utility.swt.GUICanceledException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Function;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import edu.iu.sci2.database.star.extract.common.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.common.aggregate.AggregateFunction;
import edu.iu.sci2.database.star.extract.common.field.validation.ColumnWorksWithAggregateFunctionValidationRule;
import edu.iu.sci2.database.star.extract.common.guibuilder.attribute.AttributeListWidget;
import edu.iu.sci2.database.star.extract.common.guibuilder.attribute.AttributeWidgetProperties;

public abstract class GUIBuilder {
	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 850;

	public static final int INSTRUCTIONS_WIDTH = 400;

	public static final int LEAF_SELECTOR_LABEL_WIDTH = 280;
	public static final int LEAF_SELECTOR_WIDTH = 78;

	public static final int AGGREGATE_LIST_HEIGHT = 300;

	public static final int DEFAULT_AGGREGATE_WIDGET_COUNT = 0;

	public static final String INSTRUCTIONS_GROUP_TEXT = "";
	public static final String HEADER_GROUP_TEXT = "";
	public static final String NODE_ATTRIBUTES_GROUP_TEXT = "Node Attributes";
	public static final String EDGE_ATTRIBUTES_GROUP_TEXT = "Edge Attributes";
	public static final String TABLE_ATTRIBUTES_GROUP_TEXT = "Attributes";

	public static final String FINISHED_BUTTON_TEXT = "Run the Extraction!";

	// TODO enum?
	public static final String NODE_TYPE = "Node";
	public static final String EDGE_TYPE = "Edge";
	public static final String TABLE_TYPE = "Table";

	public static final String HEADER_GROUP_NAME = "header";

	public static final String ATTRIBUTE_FUNCTION_GROUP1_NAME = "attributeFunction1";
	public static final String CORE_ENTITY_COLUMN_GROUP1_NAME = "coreEntityColumn1";
	public static final String ATTRIBUTE_NAME_GROUP1_NAME = "attributeName1";

	public static final String ATTRIBUTE_FUNCTION_GROUP2_NAME = "attributeFunction2";
	public static final String CORE_ENTITY_COLUMN_GROUP2_NAME = "coreEntityColumn2";
	public static final String ATTRIBUTE_NAME_GROUP2_NAME = "attributeName2";

	public static final String LEAF_SELECTOR_FIELD_VALIDATOR_BASE_NAME = "Leaf Entity ";
	public static final String NODE_AGGREGATE_FUNCTION_FIELD_VALIDATOR_BASE_NAME =
		"Node Aggregate Function";
	public static final String EDGE_AGGREGATE_FUNCTION_FIELD_VALIDATOR_BASE_NAME =
		"Edge Aggregate Function";
	public static final String NODE_AGGREGATED_COLUMN_FIELD_VALIDATOR_BASE_NAME =
		"Node Aggregated Column";
	public static final String EDGE_AGGREGATED_COLUMN_FIELD_VALIDATOR_BASE_NAME =
		"Edge Aggregated Column";
	public static final String NODE_ATTRIBUTE_FIELD_VALIDATOR_BASE_NAME = "Node Attribute ";
	public static final String EDGE_ATTRIBUTE_FIELD_VALIDATOR_BASE_NAME = "Edge Attribute ";
	public static final String TABLE_ATTRIBUTE_FIELD_VALIDATOR_BASE_NAME = "Table Attribute ";

	protected StarDatabaseDescriptor databaseDescriptor;

	protected Button finishedButton;

	protected FieldValidator<String> leafSelectorFieldValidator =
		new BasicFieldValidator<String>(LEAF_SELECTOR_FIELD_VALIDATOR_BASE_NAME);

	protected FieldValidator<String> aggregateFunctionValidator1 =
		new BasicFieldValidator<String>(NODE_AGGREGATE_FUNCTION_FIELD_VALIDATOR_BASE_NAME);
	protected FieldValidator<String> aggregateFunctionValidator2 =
		new BasicFieldValidator<String>(EDGE_AGGREGATE_FUNCTION_FIELD_VALIDATOR_BASE_NAME);

	protected FieldValidator<String> aggregatedColumnValidator1 =
		new BasicFieldValidator<String>(NODE_AGGREGATED_COLUMN_FIELD_VALIDATOR_BASE_NAME);
	protected FieldValidator<String> aggregatedColumnValidator2 =
		new BasicFieldValidator<String>(EDGE_AGGREGATED_COLUMN_FIELD_VALIDATOR_BASE_NAME);

	protected FieldValidator<String> attributeNameValidator1 =
		new BasicFieldValidator<String>(NODE_ATTRIBUTE_FIELD_VALIDATOR_BASE_NAME);
	protected FieldValidator<String> attributeNameValidator2 =
		new BasicFieldValidator<String>(EDGE_ATTRIBUTE_FIELD_VALIDATOR_BASE_NAME);

	@SuppressWarnings("unchecked")
	protected final Collection<FieldValidator<String>> allValidators =
		Collections.unmodifiableSet(Sets.newHashSet(
			this.leafSelectorFieldValidator,
			this.aggregateFunctionValidator1,
			this.aggregateFunctionValidator2,
			this.aggregatedColumnValidator1,
			this.aggregatedColumnValidator2,
			this.attributeNameValidator2,
			this.attributeNameValidator1));

	protected DisableFinishedButtonAction disableFinishedButtonAction =
		new DisableFinishedButtonAction();

	public GUIBuilder(StarDatabaseDescriptor databaseDescriptor) {
		this.databaseDescriptor = databaseDescriptor;

		String countDistinctAggregateFunctionSQLName =
			AggregateFunction.COUNT_DISTINCT.getSQLName();

		/* Setup the validation rule that ensures that the two leaf selection widgets have
		 * unique values.
		 */

		FieldValidationRule<String> uniqueLeafSelectorValueValidationRule =
			new UniqueValueValidationRule<String>(LEAF_SELECTOR_FIELD_VALIDATOR_BASE_NAME);
		this.leafSelectorFieldValidator.addValidationRule(uniqueLeafSelectorValueValidationRule);

		/* Setup the validation rule that ensures that only one of the node aggregate function
		 * widgets contains the value COUNT DISTINCT.
		 */

		FieldValidationRule<String> aggregateFunction_CountDistinct_SingletonValidationRule1 =
			new OneOccurrenceOfValueValidationRule<String>(
				attributeFieldValidator1BaseName(), countDistinctAggregateFunctionSQLName);
		this.aggregateFunctionValidator1.addValidationRule(
			aggregateFunction_CountDistinct_SingletonValidationRule1);

		/* Setup the validation rule that ensures that only one of the edge aggregate function
		 * widgets contains the value COUNT DISTINCT.
		 */

		FieldValidationRule<String> aggregateFunction_CountDistinct_SingletonValidationRule2 =
			new OneOccurrenceOfValueValidationRule<String>(
				attributeFieldValidator2BaseName(), countDistinctAggregateFunctionSQLName);
		this.aggregateFunctionValidator2.addValidationRule(
			aggregateFunction_CountDistinct_SingletonValidationRule2);

		/* Setup the validation rules that ensure that aggregated columns are compatible with
		 * their respective, chosen aggregate functions.
		 */
		ColumnWorksWithAggregateFunctionValidationRule
			columnWorksWithAggregateFunctionValidationRule1 =
				new ColumnWorksWithAggregateFunctionValidationRule(
					this.databaseDescriptor.getAllColumnDescriptorsBySQLName(),
					ATTRIBUTE_FUNCTION_GROUP1_NAME,
					CORE_ENTITY_COLUMN_GROUP1_NAME);
		this.aggregateFunctionValidator1.addValidationRule(
			columnWorksWithAggregateFunctionValidationRule1);
		this.aggregatedColumnValidator1.addValidationRule(
			columnWorksWithAggregateFunctionValidationRule1);

		ColumnWorksWithAggregateFunctionValidationRule
			columnWorksWithAggregateFunctionValidationRule2 =
				new ColumnWorksWithAggregateFunctionValidationRule(
					this.databaseDescriptor.getAllColumnDescriptorsBySQLName(),
					ATTRIBUTE_FUNCTION_GROUP2_NAME,
					CORE_ENTITY_COLUMN_GROUP2_NAME);
		this.aggregateFunctionValidator2.addValidationRule(
			columnWorksWithAggregateFunctionValidationRule2);
		this.aggregatedColumnValidator2.addValidationRule(
			columnWorksWithAggregateFunctionValidationRule2);

		// Setup the validation rule that ensures that all attribute name widgets are NOT empty.

		FieldValidationRule<String> emptyAttributeNameValidationRule =
			new EmptyTextFieldValidationRule();
		this.attributeNameValidator1.addValidationRule(emptyAttributeNameValidationRule);
		this.attributeNameValidator2.addValidationRule(emptyAttributeNameValidationRule);

		/* Setup the validation rule that ensures that all node attribute name widgets have
		 * unique values.
		 */

		FieldValidationRule<String> uniqueAttributeNameValidationRule1 =
			new UniqueValueValidationRule<String>(attributeFieldValidator1BaseName());
		this.attributeNameValidator1.addValidationRule(uniqueAttributeNameValidationRule1);

		/* Setup the validation rule that ensures that all edge attribute name widgets have
		 * unique values.
		 */

		FieldValidationRule<String> uniqueAttributeNameValidationRule2 =
			new UniqueValueValidationRule<String>(attributeFieldValidator2BaseName());
		this.attributeNameValidator2.addValidationRule(uniqueAttributeNameValidationRule2);
	}

	public abstract String attributeFieldValidator1BaseName();
	public abstract String attributeFieldValidator2BaseName();

	public abstract DataModel createGUI(
			String windowTitle,
			int windowWidth,
			int windowHeight,
			StarDatabaseDescriptor databaseDescriptor)
			throws GUICanceledException, UniqueNameException;

	public static void runGUI(Display display, Shell shell, int windowHeight) {
		GUIBuilderUtilities.openShell(shell, windowHeight, true);
    	GUIBuilderUtilities.swtLoop(display, shell);
	}

	protected static Composite createInstructionsArea(Composite parent) {
		Composite instructionsArea = new Composite(parent, SWT.NONE);
		instructionsArea.setLayoutData(createInstructionsAreaLayoutData());
		instructionsArea.setLayout(createInstructionsAreaLayout());
		instructionsArea.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		return instructionsArea;
	}

	private static GridData createInstructionsAreaLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
		layoutData.horizontalSpan = 2;
		layoutData.widthHint = INSTRUCTIONS_WIDTH;

		return layoutData;
	}

	private static GridLayout createInstructionsAreaLayout() {
		GridLayout layout = new GridLayout(2, false);

		return layout;
	}

	protected static Group createHeaderGroup(Composite parent) {
		Group headerGroup = new Group(parent, SWT.NONE);
		headerGroup.setLayoutData(createHeaderGroupLayoutData());
		headerGroup.setLayout(createHeaderGroupLayout());
		headerGroup.setText(HEADER_GROUP_TEXT);

		return headerGroup;
	}

	private static GridData createHeaderGroupLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
		layoutData.horizontalSpan = 2;
		layoutData.widthHint = INSTRUCTIONS_WIDTH;

		return layoutData;
	}

	private static GridLayout createHeaderGroupLayout() {
		GridLayout layout = new GridLayout(2, false);

		return layout;
	}

	protected static Group createAggregatesGroup(Composite parent, String groupText) {
		Group nodeAggregatesGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
		nodeAggregatesGroup.setLayoutData(createAggregatesGroupLayoutData());
		nodeAggregatesGroup.setLayout(createAggregatesGroupLayout());
		nodeAggregatesGroup.setText(groupText);

		return nodeAggregatesGroup;
	}

	private static GridData createAggregatesGroupLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		layoutData.horizontalSpan = 2;

		return layoutData;
	}

	private static GridLayout createAggregatesGroupLayout() {
		GridLayout layout = new GridLayout(1, false);

		return layout;
	}

	protected static Group createFooterGroup(Composite parent) {
		Group footerGroup = new Group(parent, SWT.NONE);
		footerGroup.setLayoutData(createFooterGroupLayoutData());
		footerGroup.setLayout(createFooterGroupLayout());
		footerGroup.setText(HEADER_GROUP_TEXT);

		return footerGroup;
	}

	private static GridData createFooterGroupLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
		layoutData.horizontalSpan = 2;

		return layoutData;
	}

	private static GridLayout createFooterGroupLayout() {
		GridLayout layout = new GridLayout(2, false);

		return layout;
	}

	protected StyledText createInstructionsLabel(Composite parent, int height) {
		StyledText instructionsLabel = new StyledText(
			parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.LEFT | SWT.READ_ONLY | SWT.WRAP);
		instructionsLabel.setBackground(
			parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		instructionsLabel.setLayoutData(createInstructionsLabelLayoutData(height));
		instructionsLabel.getCaret().setVisible(false);

		return instructionsLabel;
	}

	private static GridData createInstructionsLabelLayoutData(int height) {
		GridData layoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
		layoutData.horizontalSpan = 2;
		layoutData.heightHint = height;

		return layoutData;
	}

	protected static Map<String, String> createAggregateOptions(
			StarDatabaseDescriptor databaseDescriptor) {
		Map<String, String> aggregateOptions = new LinkedHashMap<String, String>();
		BiMap<String, String> leafTableNameOptions = HashBiMap.create(
			Maps.newLinkedHashMap(databaseDescriptor.createTableNameOptionsWithoutCore()));
		Map<String, String> annotatedLeafTableNameOptions =
			Maps.transformValues(leafTableNameOptions.inverse(), new Function<String, String>() {
				public String apply(String value) {
					return String.format("%s (Leaf Table)", value);
				}
			});
		aggregateOptions.putAll(HashBiMap.create(annotatedLeafTableNameOptions).inverse());
		aggregateOptions.putAll(
			databaseDescriptor.getCoreTableDescriptor().getColumnNamesByLabels());

		return aggregateOptions;
	}

	protected AttributeListWidget createAggregateWidget(
			AttributeWidgetProperties properties, String type, Composite parent) {
		AttributeListWidget aggregateList = new AttributeListWidget(properties, type, parent);
		aggregateList.setLayoutData(createAggregateListLayoutData());

		return aggregateList;
	}

	protected static GridData createAggregateListLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		layoutData.heightHint = AGGREGATE_LIST_HEIGHT;

		return layoutData;
	}

	protected static Button createFinishedButton(
			final Composite parent,
			int horizontalSpan,
			final ObjectContainer<Boolean> userFinished) {
		Button finishedButton = new Button(parent, SWT.PUSH);
		finishedButton.setLayoutData(createFinishedButtonLayoutData(horizontalSpan));
		finishedButton.setText(FINISHED_BUTTON_TEXT);
		finishedButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				parent.getShell().close();
				userFinished.object = true; // TODO Pass to GUI a "CompletionEvent" or something
			}
		});

		return finishedButton;
	}

	private static GridData createFinishedButtonLayoutData(int horizontalSpan) {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		layoutData.horizontalSpan = horizontalSpan;

		return layoutData;
	}

	public class DisableFinishedButtonAction implements FieldValidationAction {
		public synchronized void doesValidate() {
			GUIBuilder.this.finishedButton.setEnabled(true);
		}

		public synchronized void doesNotValidate(Collection<String> reasons) {
			GUIBuilder.this.finishedButton.setEnabled(false);
		}
	}
}