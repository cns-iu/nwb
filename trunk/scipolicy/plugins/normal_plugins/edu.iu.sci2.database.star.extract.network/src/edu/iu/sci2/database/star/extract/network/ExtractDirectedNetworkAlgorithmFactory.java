package edu.iu.sci2.database.star.extract.network;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationCanceledException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utilities.swt.GUICanceledException;
import org.cishell.utilities.swt.model.GUIModel;
import org.cishell.utilities.swt.model.GUIModelField;
import org.cishell.utilities.swt.model.GUIModelGroup;
import org.cishell.utilities.swt.model.datasynchronizer.ModelDataSynchronizer;
import org.eclipse.swt.widgets.Widget;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.extract.network.guibuilder.DirectedNetworkGUIBuilder;
import edu.iu.sci2.database.star.extract.network.guibuilder.GUIBuilder;
import edu.iu.sci2.database.star.extract.network.query.CoreToLeafDirectedNetworkQueryConstructor;
import edu.iu.sci2.database.star.extract.network.query.LeafToCoreDirectedNetworkQueryConstructor;
import edu.iu.sci2.database.star.extract.network.query.LeafToLeafDirectedNetworkQueryConstructor;
import edu.iu.sci2.database.star.extract.network.query.QueryConstructor;

public class ExtractDirectedNetworkAlgorithmFactory extends ExtractionAlgorithmFactory {
	public static final String WINDOW_TITLE = "Extract Bipartite Network";

	private BundleContext bundleContext;
	private LogService logger;

	protected void activate(ComponentContext componentContext) {
		this.bundleContext = componentContext.getBundleContext();
		this.logger = (LogService)componentContext.locateService("LOG");
	}

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	Data parentData = data[0];
    	StarDatabaseMetadata databaseMetadata = getMetadata(parentData);
    	verifyLeafTables(databaseMetadata, this.logger);
    	GUIModel model = getModelFromUser(databaseMetadata);
    	QueryConstructor queryConstructor = decideQueryConstructor(databaseMetadata, model);
    	AlgorithmFactory networkQueryRunner = getNetworkQueryRunner(this.bundleContext);

        return new ExtractNetworkAlgorithm(
        	ciShellContext, parentData, queryConstructor, networkQueryRunner, this.logger);
    }

    private static GUIModel getModelFromUser(StarDatabaseMetadata metadata) {
    	try {
    		return new DirectedNetworkGUIBuilder().createGUI(
    			WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, new StarDatabaseDescriptor(metadata));
    	} catch (GUICanceledException e) {
    		throw new AlgorithmCreationCanceledException(e.getMessage(), e);
    	}
    }

    private static QueryConstructor decideQueryConstructor(
    		StarDatabaseMetadata metadata, GUIModel model) {
	    // TODO: Specify generic types?
    	GUIModelGroup headerGroup = model.getGroup(GUIBuilder.HEADER_GROUP_NAME);
    	GUIModelField<?, ? extends Widget, ? extends ModelDataSynchronizer<?>> entity1 =
    		headerGroup.getField(DirectedNetworkGUIBuilder.SOURCE_LEAF_FIELD_NAME);
    	GUIModelField<?, ? extends Widget, ? extends ModelDataSynchronizer<?>> entity2 =
    		headerGroup.getField(DirectedNetworkGUIBuilder.TARGET_LEAF_FIELD_NAME);
    	String coreEntityTableName = metadata.getCoreEntityTableName();
    	String entity1Value = (String) entity1.getValue();
    	String entity2Value = (String) entity2.getValue();

    	// Leaf -> Core
    	if (coreEntityTableName.equals(entity2Value)) {
    		return new LeafToCoreDirectedNetworkQueryConstructor(
    			entity1Value,
    			GUIBuilder.HEADER_GROUP_NAME,
    			GUIBuilder.NODE_ATTRIBUTE_FUNCTION_GROUP_NAME,
    			GUIBuilder.NODE_CORE_ENTITY_COLUMN_GROUP_NAME,
    			GUIBuilder.NODE_RESULT_NAME_GROUP_NAME,
    			GUIBuilder.EDGE_ATTRIBUTE_FUNCTION_GROUP_NAME,
    			GUIBuilder.EDGE_CORE_ENTITY_COLUMN_GROUP_NAME,
    			GUIBuilder.EDGE_RESULT_NAME_GROUP_NAME,
    			model,
    			metadata);
    	// Core -> Leaf
    	} else if (coreEntityTableName.equals(entity1Value)) {
    		return new CoreToLeafDirectedNetworkQueryConstructor(
    			entity2Value,
    			GUIBuilder.HEADER_GROUP_NAME,
    			GUIBuilder.NODE_ATTRIBUTE_FUNCTION_GROUP_NAME,
    			GUIBuilder.NODE_CORE_ENTITY_COLUMN_GROUP_NAME,
    			GUIBuilder.NODE_RESULT_NAME_GROUP_NAME,
    			GUIBuilder.EDGE_ATTRIBUTE_FUNCTION_GROUP_NAME,
    			GUIBuilder.EDGE_CORE_ENTITY_COLUMN_GROUP_NAME,
    			GUIBuilder.EDGE_RESULT_NAME_GROUP_NAME,
    			model,
    			metadata);
    	// Leaf1 -> Leaf2
    	} else {
    		return new LeafToLeafDirectedNetworkQueryConstructor(
    			entity1Value,
    			entity2Value,
    			GUIBuilder.HEADER_GROUP_NAME,
    			GUIBuilder.NODE_ATTRIBUTE_FUNCTION_GROUP_NAME,
    			GUIBuilder.NODE_CORE_ENTITY_COLUMN_GROUP_NAME,
    			GUIBuilder.NODE_RESULT_NAME_GROUP_NAME,
    			GUIBuilder.EDGE_ATTRIBUTE_FUNCTION_GROUP_NAME,
    			GUIBuilder.EDGE_CORE_ENTITY_COLUMN_GROUP_NAME,
    			GUIBuilder.EDGE_RESULT_NAME_GROUP_NAME,
    			model,
    			metadata);
    	}
    }
}
