package edu.iu.sci2.database.star.extract.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.utilities.StringUtilities;
import org.cishell.utility.datastructure.datamodel.field.DataModelField;
import org.cishell.utility.datastructure.datamodel.group.DataModelGroup;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.extract.common.aggregate.Aggregate;

public class StarDatabaseExtractionUtilities {
	public static String fixQuerySectionWithCommaPrefix(String querySection) {
		if (StringUtilities.isNull_Empty_OrWhitespace(querySection)) {
			return "";
		} else {
			return ", " + querySection;
		}
	}
}