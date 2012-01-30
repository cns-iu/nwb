package edu.iu.sci2.visualization.scimaps.fields;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;
import java.io.File;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import oim.vivo.scimapcore.journal.Journal;
import oim.vivo.scimapcore.mapping.DetailedScienceMappingResult;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

import com.google.common.collect.ImmutableSortedMap;

import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;
import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.journals.JournalsMapAlgorithm;
import edu.iu.sci2.visualization.scimaps.rendering.print2008.MapOfScienceDocumentRenderer;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;
import edu.iu.sci2.visualization.scimaps.tempvis.VisualizationRunner;

public class FieldsMapAlgorithm implements Algorithm {
	public static final String OUT_FIELD_COLUMN_NAME = "Field";
	public static final String OUT_TOTAL_COLUMN_NAME = "Total";
	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";
	public static final String CSV_MIME_TYPE = "file:text/csv";

	private Data inData;
	private Table table;
	private String inDataLabel;
	private String nodeIDColumnName;
	private String nodeLabelColumnName;
	private String nodeValueColumnName;
	private String dataDisplayName;
	private LogService logger;
	private float scalingFactor;

	public FieldsMapAlgorithm(Data[] data, LogService logger,
			String nodeIDColumnName, String nodeLabelColumnName,
			String nodeValueColumnName, String dataDisplayName,
			float scalingFactor) {
		this.inData = data[0];
		this.table = (Table) data[0].getData();

		this.inDataLabel = (String) data[0].getMetadata().get(
				DataProperty.LABEL);

		this.logger = logger;
		this.nodeIDColumnName = nodeIDColumnName;
		this.nodeLabelColumnName = nodeLabelColumnName;
		this.nodeValueColumnName = nodeValueColumnName;
		this.dataDisplayName = dataDisplayName;
		this.scalingFactor = scalingFactor;
	}

	public Data[] execute() {
		TableReader tableReader = new TableReader(table, nodeValueColumnName,
				nodeLabelColumnName, nodeIDColumnName, logger);

		FieldsAnalyzer fieldsAnalyzer = new FieldsAnalyzer(
				tableReader.getUcsdAreaTotals(),
				tableReader.getUcsdAreaLabels(),
				tableReader.getUnclassifiedLabelCounts());

		Map<Integer, Float> found = fieldsAnalyzer.getFound();

		createVisualization(fieldsAnalyzer);

		return datafy(found, inData);
	}

	private static Data[] datafy(Map<Integer, Float> found, Data parentData) {
		Table foundTable = makeFieldTotalTable(ImmutableSortedMap.copyOf(found));
		Data foundData = JournalsMapAlgorithm.datafy(foundTable,
				"Totals per field", parentData);

		return new Data[] { foundData };
	}

	private static Table makeFieldTotalTable(Map<Integer, Float> fieldToTotal) {
		Table table = new Table();
		table.addColumn(OUT_FIELD_COLUMN_NAME, int.class);
		table.addColumn(OUT_TOTAL_COLUMN_NAME, double.class);

		for (Entry<Integer, Float> entry : fieldToTotal.entrySet()) {
			int row = table.addRow();
			table.setInt(row, OUT_FIELD_COLUMN_NAME, entry.getKey());
			table.setDouble(row, OUT_TOTAL_COLUMN_NAME, entry.getValue()
					.doubleValue());
		}

		return table;
	}

	private RenderableVisualization createVisualization(
			FieldsAnalyzer fieldsAnalyzer) {

		Map<Integer, Float> mappedResult = fieldsAnalyzer.getFound();
		Map<String, Float> unmappedResult = fieldsAnalyzer.getUnfound();

		Set<Journal> mappedJournals = fieldsAnalyzer.getMappedFields();
		Set<Journal> unmappedJournals = fieldsAnalyzer.getUnmappedFields();

		DetailedScienceMappingResult mappingResult = new FieldsDetailedScienceMapping(
				mappedResult, unmappedResult, mappedJournals, unmappedJournals);

		String circleSizeMeaning = nodeValueColumnName;

		MapOfScience map = new MapOfScience(mappingResult);

		double listFontSize = fieldsAnalyzer.calculateListFontSize(MapOfScience
				.getCategories());

		RenderableVisualization visualization = new MapOfScienceDocumentRenderer(
				dataDisplayName, inDataLabel, map, listFontSize,
				"via 554 Fields", "records",
				"Records which could not be located on the map of science:",
				new Date(), scalingFactor, circleSizeMeaning, new Dimension(
						(int) inch(8.5f), (int) inch(11f)));

		VisualizationRunner visualizationRunner = new VisualizationRunner(
				visualization);
		visualizationRunner.setUp();
		visualizationRunner.run();
		return visualization;
	}

}