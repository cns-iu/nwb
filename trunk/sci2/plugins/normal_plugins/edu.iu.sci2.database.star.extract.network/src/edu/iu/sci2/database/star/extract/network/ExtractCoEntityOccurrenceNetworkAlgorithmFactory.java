package edu.iu.sci2.database.star.extract.network;

import org.cishell.utility.datastructure.datamodel.field.DataModelField;
import org.cishell.utility.datastructure.datamodel.group.DataModelGroup;
import org.cishell.utility.swt.model.SWTModel;

import edu.iu.sci2.database.star.extract.common.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.common.aggregate.Aggregate;
import edu.iu.sci2.database.star.extract.common.guibuilder.GUIBuilder;
import edu.iu.sci2.database.star.extract.network.query.CoEntityOccurrenceNetworkQueryConstructor;
import edu.iu.sci2.database.star.extract.network.query.NetworkQueryConstructor;

public class ExtractCoEntityOccurrenceNetworkAlgorithmFactory
		extends ExtractTwoLeafTableNetworkAlgorithmFactory {
	public static final String INSTRUCTIONS_LABEL_TEXT =
		"First, choose the leaf entity that you want to correspond to the nodes in your " +
		"resulting Co-Entity Occurrence network.\n" +
		"Then, choose the leaf entity on which you want your node-entities to co-occur.\n" +
		"Finally, setup any node and edge attributes you want on your resulting network.\n" +
		"For more information see the Sci2 tutorial at: ";
	public static final String TUTORIAL_URL =
		"https://nwb.slis.indiana.edu/community/" +
		"?n=Sci2Algorithm.GenericCSVExtractCoEntityOccurrenceNetwork";
	public static final String TUTORIAL_DISPLAY_URL = "Sci2 Tutorial";
	public static final int INSTRUCTIONS_LABEL_HEIGHT = 95;

	public static final String ENTITY_NODE_LEAF_FIELD_LABEL =
		"Choose the leaf entity that you want to correspond to the nodes in your network: ";
	public static final String VIA_ENTITY_LEAF_FIELD_LABEL =
		"Choose the leaf entity on which your node-entities should co-occur: ";

	public static final String ENTITY_NODE_LEAF_FIELD_NAME = "To Treat As The Nodes";
	public static final String VIA_ENTITY_LEAF_FIELD_NAME =
		"On Which Your Node-Entities Should Co-Occur";

	public static final String WINDOW_TITLE = "Extract Co-Entity Occurrence Network";

	@Override
	public String instructionsLabelText() {
		return INSTRUCTIONS_LABEL_TEXT;
	}

	@Override
	public String tutorialURL() {
		return TUTORIAL_URL;
	}

	@Override
	public String tutorialDisplayURL() {
		return TUTORIAL_DISPLAY_URL;
	}

	@Override
	public int instructionsLabelHeight() {
		return INSTRUCTIONS_LABEL_HEIGHT;
	}

	@Override
	public String leafField1Label() {
		return ENTITY_NODE_LEAF_FIELD_LABEL;
	}

	@Override
	public String leafField1Name() {
		return ENTITY_NODE_LEAF_FIELD_NAME;
	}

	@Override
	public String leafField2Label() {
		return VIA_ENTITY_LEAF_FIELD_LABEL;
	}

	@Override
    public String leafField2Name() {
		return VIA_ENTITY_LEAF_FIELD_NAME;
	}

	@Override
	public boolean includeCoreTableInLeafSelectors() {
		return false;
	}

	@Override
	public int defaultAggregateWidgetCount() {
		return 0;
	}

	@Override
	public String windowTitle() {
		return WINDOW_TITLE;
	}

	@Override
	public boolean allowCountDistinctFor1() {
		return true;
	}

	@Override
	public boolean allowCountDistinctFor2() {
		return false;
	}

	@Override
	public NetworkQueryConstructor<
			? extends Aggregate, ? extends Aggregate> decideQueryConstructor(
    			StarDatabaseDescriptor databaseDescriptor, SWTModel model) {
		DataModelGroup headerGroup = model.getGroup(GUIBuilder.HEADER_GROUP_NAME);
    	DataModelField<?> entity1 =
    		headerGroup.getField(leafField1Name());
    	DataModelField<?> entity2 =
    		headerGroup.getField(leafField2Name());
    	String entity1Value = (String) entity1.getValue();
    	String entity2Value = (String) entity2.getValue();

    	return new CoEntityOccurrenceNetworkQueryConstructor(
    		entity1Value,
    		entity2Value,
    		GUIBuilder.HEADER_GROUP_NAME,
			GUIBuilder.ATTRIBUTE_FUNCTION_GROUP1_NAME,
			GUIBuilder.CORE_ENTITY_COLUMN_GROUP1_NAME,
			GUIBuilder.ATTRIBUTE_NAME_GROUP1_NAME,
			GUIBuilder.ATTRIBUTE_FUNCTION_GROUP2_NAME,
			GUIBuilder.CORE_ENTITY_COLUMN_GROUP2_NAME,
			GUIBuilder.ATTRIBUTE_NAME_GROUP2_NAME,
			model,
			databaseDescriptor);
	}
}