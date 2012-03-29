package edu.iu.sci2.database.star.extract.network;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationCanceledException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utility.datastructure.datamodel.exception.UniqueNameException;
import org.cishell.utility.datastructure.datamodel.field.DataModelField;
import org.cishell.utility.datastructure.datamodel.group.DataModelGroup;
import org.cishell.utility.swt.GUICanceledException;
import org.cishell.utility.swt.model.SWTModel;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.extract.common.ExtractionAlgorithmFactory;
import edu.iu.sci2.database.star.extract.common.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.common.aggregate.Aggregate;
import edu.iu.sci2.database.star.extract.common.guibuilder.GUIBuilder;
import edu.iu.sci2.database.star.extract.network.guibuilder.TwoLeafTableNetworkGUIBuilder;
import edu.iu.sci2.database.star.extract.network.query.CoreToLeafBipartiteNetworkQueryConstructor;
import edu.iu.sci2.database.star.extract.network.query.LeafToCoreBipartiteNetworkQueryConstructor;
import edu.iu.sci2.database.star.extract.network.query.LeafToLeafDirectedNetworkQueryConstructor;
import edu.iu.sci2.database.star.extract.network.query.NetworkQueryConstructor;

// TODO: Rename this at some point to reflect bipartite terminology.
public abstract class ExtractTwoLeafTableNetworkAlgorithmFactory
		extends ExtractionAlgorithmFactory {
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
    	SWTModel model = getModelFromUser(databaseMetadata);
    	NetworkQueryConstructor<? extends Aggregate, ? extends Aggregate> queryConstructor =
    		decideQueryConstructor(databaseDescriptor, model);
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

    public abstract String instructionsLabelText();
	public abstract String tutorialURL();
	public abstract String tutorialDisplayURL();
	public abstract int instructionsLabelHeight();
	public abstract String leafField1Label();
	public abstract String leafField1Name();
	public abstract String leafField2Label();
	public abstract String leafField2Name();
	public abstract boolean includeCoreTableInLeafSelectors();
	public abstract int defaultAggregateWidgetCount();
	public abstract boolean allowCountDistinctFor1();
	public abstract boolean allowCountDistinctFor2();

	public abstract String windowTitle();

    public SWTModel getModelFromUser(StarDatabaseMetadata metadata)
    		throws AlgorithmCreationCanceledException {
    	try {
    		StarDatabaseDescriptor databaseDescriptor = new StarDatabaseDescriptor(metadata);

    		return new TwoLeafTableNetworkGUIBuilder(
    			databaseDescriptor,
    			instructionsLabelText(),
    			tutorialURL(),
    			tutorialDisplayURL(),
    			instructionsLabelHeight(),
    			leafField1Label(),
    			leafField1Name(),
    			leafField2Label(),
    			leafField2Name(),
    			includeCoreTableInLeafSelectors(),
    			defaultAggregateWidgetCount(),
    			allowCountDistinctFor1(),
    			allowCountDistinctFor2()).createGUI(
    			windowTitle(), WINDOW_WIDTH, WINDOW_HEIGHT, databaseDescriptor);
    	} catch (GUICanceledException e) {
    		throw new AlgorithmCreationCanceledException(e.getMessage(), e);
    	} catch (UniqueNameException e) {
    		throw new AlgorithmCreationCanceledException(e.getMessage(), e);
    	}
    }

    public NetworkQueryConstructor<
    		? extends Aggregate, ? extends Aggregate> decideQueryConstructor(
    			StarDatabaseDescriptor databaseDescriptor, SWTModel model) {
    	DataModelGroup headerGroup = model.getGroup(GUIBuilder.HEADER_GROUP_NAME);
    	DataModelField<?> entity1 =
    		headerGroup.getField(leafField1Name());
    	DataModelField<?> entity2 =
    		headerGroup.getField(leafField2Name());
    	String coreEntityTableName = databaseDescriptor.getMetadata().getCoreEntityTableName();
    	String entity1Value = (String) entity1.getValue();
    	String entity2Value = (String) entity2.getValue();

    	// Leaf -> Core
    	if (coreEntityTableName.equals(entity2Value)) {
    		return new LeafToCoreBipartiteNetworkQueryConstructor(
    			entity1Value,
    			GUIBuilder.HEADER_GROUP_NAME,
    			GUIBuilder.ATTRIBUTE_FUNCTION_GROUP1_NAME,
    			GUIBuilder.CORE_ENTITY_COLUMN_GROUP1_NAME,
    			GUIBuilder.ATTRIBUTE_NAME_GROUP1_NAME,
    			GUIBuilder.ATTRIBUTE_FUNCTION_GROUP2_NAME,
    			GUIBuilder.CORE_ENTITY_COLUMN_GROUP2_NAME,
    			GUIBuilder.ATTRIBUTE_NAME_GROUP2_NAME,
    			model,
    			databaseDescriptor);
    	// Core -> Leaf
    	} else if (coreEntityTableName.equals(entity1Value)) {
    		return new CoreToLeafBipartiteNetworkQueryConstructor(
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
    	// Leaf1 -> Leaf2
    	} else {
    		return new LeafToLeafDirectedNetworkQueryConstructor(
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
}
