package edu.iu.sci2.database.star.extract.table;

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
import edu.iu.sci2.database.star.extract.common.guibuilder.GUIBuilder;
import edu.iu.sci2.database.star.extract.table.guibuilder.TableGUIBuilder;
import edu.iu.sci2.database.star.extract.table.query.TableQueryConstructor;

public class ExtractTableAlgorithmFactory extends ExtractionAlgorithmFactory {
	public static final int WINDOW_WIDTH = 1500;
	public static final int WINDOW_HEIGHT = 600;
	public static final String WINDOW_TITLE = "Extract Table";

	private BundleContext bundleContext;
	private LogService logger;

	protected void activate(ComponentContext componentContext) {
		this.bundleContext = componentContext.getBundleContext();
		this.logger = (LogService) componentContext.locateService("LOG");
	}

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	Data parentData = data[0];
    	StarDatabaseMetadata databaseMetadata = getMetadata(parentData);
    	DataModel model = getModelFromUser(databaseMetadata);
    	TableQueryConstructor queryConstructor = new TableQueryConstructor(
    		TableGUIBuilder.LEAF_FIELD_NAME,
    		GUIBuilder.HEADER_GROUP_NAME,
    		GUIBuilder.ATTRIBUTE_FUNCTION_GROUP1_NAME,
    		GUIBuilder.CORE_ENTITY_COLUMN_GROUP1_NAME,
    		GUIBuilder.ATTRIBUTE_NAME_GROUP1_NAME,
    		model,
    		databaseMetadata);
    	AlgorithmFactory tableQueryRunner = getTableQueryRunner(this.bundleContext);

        return new ExtractTableAlgorithm(
        	ciShellContext, parentData, queryConstructor, tableQueryRunner, this.logger);
    }

    public int minimumLeafTableCount() {
    	return 1;
    }

    public String extractionType() {
    	return TABLE_EXTRACTION_TYPE;
    }

    private static DataModel getModelFromUser(StarDatabaseMetadata metadata)
    		throws AlgorithmCreationCanceledException {
    	try {
    		StarDatabaseDescriptor databaseDescriptor = new StarDatabaseDescriptor(metadata);

    		return new TableGUIBuilder(databaseDescriptor).createGUI(
				WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, databaseDescriptor);
    	} catch (GUICanceledException e) {
    		throw new AlgorithmCreationCanceledException(e.getMessage(), e);
    	} catch (UniqueNameException e) {
    		throw new AlgorithmCreationCanceledException(e.getMessage(), e);
    	}
    }
}