package edu.iu.sci2.visualization.scimaps.fields;

import java.util.Map;
import java.util.Set;

import oim.vivo.scimapcore.journal.Journal;
import oim.vivo.scimapcore.mapping.DetailedScienceMappingResult;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.journals.JournalsMapAlgorithm;
import edu.iu.sci2.visualization.scimaps.parameters.ScalingStrategy;
import edu.iu.sci2.visualization.scimaps.rendering.AbstractRenderablePageManager;
import edu.iu.sci2.visualization.scimaps.rendering.Layout;
import edu.iu.sci2.visualization.scimaps.tempvis.VisualizationRunner;

public class FieldsMapAlgorithm implements Algorithm {
	private final Data inData;
	private final Table table;
	private final String nodeIDColumnName;
	private final String nodeLabelColumnName;
	private final String nodeValueColumnName;
	private final String dataDisplayName;
	private final LogService logger;
	private final ScalingStrategy scalingStrategy;
	private final Layout layout;
	private final boolean showWindow;

	public FieldsMapAlgorithm(Data[] data, LogService logger, String nodeIDColumnName,
			String nodeLabelColumnName, String nodeValueColumnName, String dataDisplayName,
			ScalingStrategy scalingStrategy, Layout layout, boolean showWindow) {
		this.inData = data[0];
		this.table = (Table) data[0].getData();

		this.logger = logger;
		
		this.nodeIDColumnName = nodeIDColumnName;
		this.nodeLabelColumnName = nodeLabelColumnName;
		this.nodeValueColumnName = nodeValueColumnName;
		this.dataDisplayName = dataDisplayName;
		this.scalingStrategy = scalingStrategy;
		this.layout = layout;
		this.showWindow = showWindow;
	}

	@Override
	public Data[] execute() {
		TableReader tableReader = new TableReader(this.table, this.nodeValueColumnName,
				this.nodeLabelColumnName, this.nodeIDColumnName, this.logger);

		FieldsAnalyzer fieldsAnalyzer = new FieldsAnalyzer(
				tableReader.getUcsdAreaTotals(),
				tableReader.getUcsdAreaLabels(),
				tableReader.getUnclassifiedLabelCounts());

		MapOfScience map = createMapOfScience(nodeValueColumnName, fieldsAnalyzer);
		
		float scalingFactor = scalingStrategy.scalingFactorFor(
				map.getIdWeightMapping().values(), layout.getAreaForLegendMax());
		
		AbstractRenderablePageManager manager = layout.createPageManager(map, scalingFactor,
				dataDisplayName);

		if (this.showWindow) {
			VisualizationRunner visualizationRunner = new VisualizationRunner(manager);
			visualizationRunner.setUp();
			visualizationRunner.run();
		}

		return JournalsMapAlgorithm.datafy(map, manager, null, this.inData, this.logger);
	}

	private static MapOfScience createMapOfScience(String nodeValueColumnName,
			FieldsAnalyzer fieldsAnalyzer) {
		Map<Integer, Float> mappedResult = fieldsAnalyzer.getFound();
		Map<String, Float> unmappedResult = fieldsAnalyzer.getUnfound();

		Set<Journal> mappedJournals = fieldsAnalyzer.getMappedFields();
		Set<Journal> unmappedJournals = fieldsAnalyzer.getUnmappedFields();

		DetailedScienceMappingResult mappingResult = new FieldsDetailedScienceMapping(
				mappedResult, unmappedResult, mappedJournals, unmappedJournals);

		MapOfScience map = new MapOfScience(nodeValueColumnName, mappingResult);

		return map;
	}
}