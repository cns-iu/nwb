package edu.iu.sci2.database.star.extract.network;

import org.cishell.framework.algorithm.AlgorithmCreationCanceledException;
import org.cishell.utility.datastructure.datamodel.exception.UniqueNameException;
import org.cishell.utility.swt.GUICanceledException;
import org.cishell.utility.swt.model.SWTModel;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.extract.common.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.network.guibuilder.TwoLeafTableNetworkGUIBuilder;

public class ExtractBipartiteNetworkAlgorithmFactory
		extends ExtractTwoLeafTableNetworkAlgorithmFactory {
	public static final String INSTRUCTIONS_LABEL_TEXT =
		"Choose the entity tables that extract your bipartite network should be " +
		"extracted between.\n" +
		"Then, setup any node and edge attributes you want on your resulting network.\n" +
		"For more information see the Sci2 tutorial at: ";
	public static final String TUTORIAL_URL =
		"https://nwb.slis.indiana.edu/community/" +
		"?n=Sci2Algorithm.GenericCSVExtractBipartiteNetwork";
	public static final String TUTORIAL_DISPLAY_URL = "Sci2 Tutorial";
	public static final int INSTRUCTIONS_LABEL_HEIGHT = 70;

	public static final String SOURCE_LEAF_FIELD_LABEL =
		"Choose the Source for your bipartite network extraction: ";
	public static final String TARGET_LEAF_FIELD_LABEL =
		"Choose the Target for your bipartite network extraction: ";

	public static final String SOURCE_LEAF_FIELD_NAME = "To Treat As The Source Nodes";
	public static final String TARGET_LEAF_FIELD_NAME = "To Treat As The Target Nodes";

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
	public String sourceLeafFieldLabel() {
		return SOURCE_LEAF_FIELD_LABEL;
	}

	@Override
	public String sourceLeafFieldName() {
		return SOURCE_LEAF_FIELD_LABEL;
	}

	@Override
	public String targetLeafFieldLabel() {
		return TARGET_LEAF_FIELD_LABEL;
	}

	@Override
    public String targetLeafFieldName() {
		return TARGET_LEAF_FIELD_NAME;
	}
}