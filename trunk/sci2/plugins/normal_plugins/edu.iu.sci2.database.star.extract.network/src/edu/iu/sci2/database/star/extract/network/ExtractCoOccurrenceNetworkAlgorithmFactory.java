package edu.iu.sci2.database.star.extract.network;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationCanceledException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utility.datastructure.datamodel.DataModel;
import org.cishell.utility.datastructure.datamodel.exception.UniqueNameException;
import org.cishell.utility.swt.GUICanceledException;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.extract.common.ExtractionAlgorithmFactory;
import edu.iu.sci2.database.star.extract.common.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.common.aggregate.Aggregate;
import edu.iu.sci2.database.star.extract.common.guibuilder.GUIBuilder;
import edu.iu.sci2.database.star.extract.network.guibuilder.CoOccurrenceNetworkGUIBuilder;
import edu.iu.sci2.database.star.extract.network.query.CoOccurrenceNetworkQueryConstructor;
import edu.iu.sci2.database.star.extract.network.query.NetworkQueryConstructor;

public class ExtractCoOccurrenceNetworkAlgorithmFactory extends ExtractionAlgorithmFactory {
	public static final String WINDOW_TITLE = "Extract Co-Occurrence Network";

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
    	StarDatabaseDescriptor databaseDescriptor = new StarDatabaseDescriptor(databaseMetadata);
    	verifyLeafTables(databaseMetadata, this.logger);
    	DataModel model = getModelFromUser(databaseDescriptor);
    	NetworkQueryConstructor<? extends Aggregate, ? extends Aggregate> queryConstructor =
    		new CoOccurrenceNetworkQueryConstructor(
    			CoOccurrenceNetworkGUIBuilder.LEAF_FIELD_NAME,
    			GUIBuilder.HEADER_GROUP_NAME,
    			GUIBuilder.ATTRIBUTE_FUNCTION_GROUP1_NAME,
    			GUIBuilder.CORE_ENTITY_COLUMN_GROUP1_NAME,
    			GUIBuilder.ATTRIBUTE_NAME_GROUP1_NAME,
    			GUIBuilder.ATTRIBUTE_FUNCTION_GROUP2_NAME,
    			GUIBuilder.CORE_ENTITY_COLUMN_GROUP2_NAME,
    			GUIBuilder.ATTRIBUTE_NAME_GROUP2_NAME,
    			model,
    			databaseDescriptor);
    	AlgorithmFactory networkQueryRunner = getNetworkQueryRunner(this.bundleContext);

        return new ExtractNetworkAlgorithm(
        	ciShellContext, parentData, queryConstructor, networkQueryRunner, this.logger);
    }

    public int minimumLeafTableCount() {
    	return 1;
    }

    public String extractionType() {
    	return NETWORK_EXTRACTION_TYPE;
    }

    private static DataModel getModelFromUser(StarDatabaseDescriptor databaseDescriptor)
    		throws AlgorithmCreationCanceledException {
    	try {
    		return new CoOccurrenceNetworkGUIBuilder(databaseDescriptor).createGUI(
				WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, databaseDescriptor);
    	} catch (GUICanceledException e) {
    		throw new AlgorithmCreationCanceledException(e.getMessage(), e);
    	} catch (UniqueNameException e) {
    		throw new AlgorithmCreationCanceledException(e.getMessage(), e);
    	}
    }
}