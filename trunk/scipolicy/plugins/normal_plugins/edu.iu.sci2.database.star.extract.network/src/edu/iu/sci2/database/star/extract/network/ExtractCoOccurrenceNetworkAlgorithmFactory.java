package edu.iu.sci2.database.star.extract.network;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utilities.swt.model.GUIModel;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.extract.network.guibuilder.CoOccurrenceNetworkGUIBuilder;
import edu.iu.sci2.database.star.extract.network.guibuilder.GUIBuilder;
import edu.iu.sci2.database.star.extract.network.query.CoOccurrenceNetworkQueryConstructor;
import edu.iu.sci2.database.star.extract.network.query.QueryConstructor;

public class ExtractCoOccurrenceNetworkAlgorithmFactory extends ExtractionAlgorithmFactory {
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
    	QueryConstructor queryConstructor = new CoOccurrenceNetworkQueryConstructor(
    		CoOccurrenceNetworkGUIBuilder.LEAF_FIELD_NAME,
    		GUIBuilder.HEADER_GROUP_NAME,
    		GUIBuilder.NODE_ATTRIBUTE_FUNCTION_GROUP_NAME,
    		GUIBuilder.NODE_CORE_ENTITY_COLUMN_GROUP_NAME,
    		GUIBuilder.NODE_RESULT_NAME_GROUP_NAME,
    		GUIBuilder.EDGE_ATTRIBUTE_FUNCTION_GROUP_NAME,
    		GUIBuilder.EDGE_CORE_ENTITY_COLUMN_GROUP_NAME,
    		GUIBuilder.EDGE_RESULT_NAME_GROUP_NAME,
    		model,
    		databaseMetadata);
    	AlgorithmFactory networkQueryRunner = getNetworkQueryRunner(this.bundleContext);

        return new ExtractNetworkAlgorithm(
        	ciShellContext, parentData, queryConstructor, networkQueryRunner, this.logger);
    }

    private static GUIModel getModelFromUser(StarDatabaseMetadata metadata) {
    	return new CoOccurrenceNetworkGUIBuilder().createGUI(
    		WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, new StarDatabaseDescriptor(metadata));
    }
}