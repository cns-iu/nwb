package edu.iu.sci2.database.star.extract.network;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utilities.swt.model.GUIModel;
import org.cishell.utilities.swt.model.GUIModelField;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.extract.network.guibuilder.DirectedNetworkGUIBuilder;
import edu.iu.sci2.database.star.extract.network.query.LeafToCoreDirectedNetworkQueryConstructor;
import edu.iu.sci2.database.star.extract.network.query.LeafToLeafDirectedNetworkQueryConstructor;
import edu.iu.sci2.database.star.extract.network.query.QueryConstructor;

public class ExtractDirectedNetworkAlgorithmFactory extends ExtractionAlgorithmFactory {
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
        	ciShellContext, parentData, model, queryConstructor, networkQueryRunner);
    }

    private static GUIModel getModelFromUser(StarDatabaseMetadata metadata) {
    	return new DirectedNetworkGUIBuilder().createGUI(
    		WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, new StarDatabaseDescriptor(metadata));
    }

    private static QueryConstructor decideQueryConstructor(
    		StarDatabaseMetadata metadata, GUIModel model) {
    	GUIModelField entity1 = model.getField(DirectedNetworkGUIBuilder.SOURCE_LEAF_FIELD_NAME);
    	GUIModelField entity2 = model.getField(DirectedNetworkGUIBuilder.TARGET_LEAF_FIELD_NAME);
    	String coreEntityTableName = metadata.getCoreEntityTableName();

    	if (coreEntityTableName.equals(entity1.getValue()) ||
    			coreEntityTableName.equals(entity2.getValue())) {
    		System.err.println("Leaf to core");
    		return new LeafToCoreDirectedNetworkQueryConstructor();
    	} else {
    		System.err.println("Leaf to leaf");
    		return new LeafToLeafDirectedNetworkQueryConstructor();
    	}
    }
}