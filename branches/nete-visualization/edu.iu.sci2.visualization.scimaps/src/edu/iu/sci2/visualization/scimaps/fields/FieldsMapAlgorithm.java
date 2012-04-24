package edu.iu.sci2.visualization.scimaps.fields;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;
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
import edu.iu.sci2.visualization.scimaps.rendering.print2012.Print2012;
import edu.iu.sci2.visualization.scimaps.rendering.web2012.Web2012;
import edu.iu.sci2.visualization.scimaps.tempvis.PageManager;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;
import edu.iu.sci2.visualization.scimaps.tempvis.VisualizationRunner;

public class FieldsMapAlgorithm implements Algorithm {
	private Data inData;
	private Table table;
	private String nodeIDColumnName;
	private String nodeLabelColumnName;
	private String nodeValueColumnName;
	private String dataDisplayName;
	private LogService logger;
	private float scalingFactor;
	private boolean showWindow;
	private boolean webVersion;

	public FieldsMapAlgorithm(Data[] data, LogService logger,
			String nodeIDColumnName, String nodeLabelColumnName,
			String nodeValueColumnName, String dataDisplayName,
			float scalingFactor, boolean webVersion, boolean showWindow) {
		this.inData = data[0];
		this.table = (Table) data[0].getData();

		this.logger = logger;
		
		this.nodeIDColumnName = nodeIDColumnName;
		this.nodeLabelColumnName = nodeLabelColumnName;
		this.nodeValueColumnName = nodeValueColumnName;
		this.dataDisplayName = dataDisplayName;
		this.scalingFactor = scalingFactor;
		this.showWindow = showWindow;
		this.webVersion = webVersion;
	}

	public Data[] execute() {
		TableReader tableReader = new TableReader(this.table,
				this.nodeValueColumnName, this.nodeLabelColumnName,
				this.nodeIDColumnName, this.logger);

		FieldsAnalyzer fieldsAnalyzer = new FieldsAnalyzer(
				tableReader.getUcsdAreaTotals(),
				tableReader.getUcsdAreaLabels(),
				tableReader.getUnclassifiedLabelCounts());

		MapOfScience map = createMapOfScience(fieldsAnalyzer);
		RenderableVisualization visualization = null;
		PageManager pageManager = null;
		
		if (this.webVersion) {
			Web2012 document = createWebDocument(map);
			visualization = document.getVisualization();
			pageManager = document.getPageManager();
		} else {
			Print2012 document = createPrintDocument(map);
			visualization = document.getVisualization();
			pageManager = document.getPageManager();
		}

		if (this.showWindow) {
			VisualizationRunner visualizationRunner = new VisualizationRunner(
					visualization);
			visualizationRunner.setUp();
			visualizationRunner.run();
		}

		return JournalsMapAlgorithm.datafy(map, pageManager, this.inData,
				this.logger);
	}

	private static MapOfScience createMapOfScience(FieldsAnalyzer fieldsAnalyzer) {
		Map<Integer, Float> mappedResult = fieldsAnalyzer.getFound();
		Map<String, Float> unmappedResult = fieldsAnalyzer.getUnfound();

		Set<Journal> mappedJournals = fieldsAnalyzer.getMappedFields();
		Set<Journal> unmappedJournals = fieldsAnalyzer.getUnmappedFields();

		DetailedScienceMappingResult mappingResult = new FieldsDetailedScienceMapping(
				mappedResult, unmappedResult, mappedJournals, unmappedJournals);

		MapOfScience map = new MapOfScience(mappingResult);

		return map;
	}

	private Print2012 createPrintDocument(MapOfScience map) {

		Dimension dimensions = new Dimension((int) inch(11.0f),
				(int) inch(8.5f));
		Print2012 document = new Print2012(map, this.dataDisplayName,
				dimensions, this.scalingFactor);

		return document;
	}

	private Web2012 createWebDocument(MapOfScience map) {

		Dimension dimensions = new Dimension(1280, 960);
		Web2012 document = new Web2012(map, dimensions, this.scalingFactor);

		return document;
	}
}