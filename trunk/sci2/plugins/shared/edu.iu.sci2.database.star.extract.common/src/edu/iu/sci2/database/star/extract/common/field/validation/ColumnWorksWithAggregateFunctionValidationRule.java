package edu.iu.sci2.database.star.extract.common.field.validation;

import java.util.Collection;
import java.util.Map;

import org.cishell.utility.datastructure.datamodel.DataModel;
import org.cishell.utility.datastructure.datamodel.exception.ModelValidationException;
import org.cishell.utility.datastructure.datamodel.field.DataModelField;
import org.cishell.utility.datastructure.datamodel.field.validation.FieldValidationRule;
import org.cishell.utility.datastructure.datamodel.group.DataModelGroup;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.extract.common.aggregate.AggregateFunction;

public class ColumnWorksWithAggregateFunctionValidationRule
		implements FieldValidationRule<String> {
	private Map<String, ColumnDescriptor> leafTableDescriptorsBySQLName;
	private String aggregateFunctionGroupName;
	private String aggregatedColumnGroupName;

	public ColumnWorksWithAggregateFunctionValidationRule(
			Map<String, ColumnDescriptor> leafTableDescriptorsBySQLName,
			String aggregateFunctionGroupName,
			String aggregatedColumnGroupName) {
		this.leafTableDescriptorsBySQLName = leafTableDescriptorsBySQLName;
		this.aggregateFunctionGroupName = aggregateFunctionGroupName;
		this.aggregatedColumnGroupName = aggregatedColumnGroupName;
	}

	@SuppressWarnings("unchecked")	// Cast from DataModelField<?> to DataModelField<String>
	public void validateField(DataModelField<String> field, DataModel model)
			throws ModelValidationException {
		/* Check what type of field this is (either the aggregate function field or the aggregated
		 * column field).
		 * (We validate both sets of fields to easily avoid duplicate error messages for the same
		 * problem.)
		 * TODO: Actually make this so duplicate errors are not produced.  This is a bigger
		 * problem, though.
		 */

		String fieldName = field.getName();
		DataModelGroup aggregateFunctionGroup = model.getGroup(this.aggregateFunctionGroupName);

		if (aggregateFunctionGroup.getFieldNames().contains(fieldName)) {
			/* field is an aggregate function field, which means we need to retrieve its
			 * corresponding aggregated column field.
			 */

			DataModelGroup aggregatedColumnGroup = model.getGroup(this.aggregatedColumnGroupName);
			DataModelField<String> aggregatedColumnField =
				(DataModelField<String>) aggregatedColumnGroup.getField(fieldName);
			internalValidateField(aggregatedColumnField, field);
		} else {
			/* field is an aggregated column field, which means we need to retrieve its
			 * corresponding aggregate function field.
			 */

			DataModelField<String> aggregateFunctionField =
				(DataModelField<String>) aggregateFunctionGroup.getField(fieldName);
			internalValidateField(field, aggregateFunctionField);
		}
	}

	private void internalValidateField(
			DataModelField<String> columnField, DataModelField<String> aggregateFunctionField)
			throws ModelValidationException {
		/* TODO: This is hack.  It's possible for columnField or aggregateFunctionField to be
		 * null, but only initially.  This is because they're both validated as they're created,
		 * so naturally one of them would have to be null at first (depending on which one was
		 * created first).
		 */
		if ((columnField == null) || (aggregateFunctionField == null)) {
			return;
		}

		String columnFieldValue = columnField.getValue();
		ColumnDescriptor columnChosenInField =
			this.leafTableDescriptorsBySQLName.get(columnFieldValue);
		String aggregateFunctionName = (String) aggregateFunctionField.getValue();
		AggregateFunction aggregateFunction =
			AggregateFunction.FUNCTIONS_BY_SQL_NAMES.get(aggregateFunctionName);

		if (!aggregateFunction.compatibleTypes().contains(columnChosenInField.getType())) {
			String format =
				"The column '%s' (of type '%s') " +
				"is not compatible with the method of summmary '%s'";
			String exceptionMessage = String.format(
				format,
				columnChosenInField.getName(),
				columnChosenInField.getType().getHumanReadableName(),
				aggregateFunction.getHumanReadableName());
			throw new ModelValidationException(exceptionMessage);
		}
	}

	public void fieldUpdated(DataModelField<String> field) {
	}

	public void fieldsUpdated(Collection<DataModelField<String>> fields) {
		for (DataModelField<String> field : fields) {
			fieldUpdated(field);
		}
	}

	public void fieldDisposed(DataModelField<String> field) {
	}
}