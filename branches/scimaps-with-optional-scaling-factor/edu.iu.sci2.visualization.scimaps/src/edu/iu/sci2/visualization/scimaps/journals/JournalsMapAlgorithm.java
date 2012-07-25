package edu.iu.sci2.visualization.scimaps.journals;

import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Dictionary;
import java.util.List;
import java.util.Set;

import oim.vivo.scimapcore.journal.Journal;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.DataFactory;
import org.freehep.graphicsio.AbstractVectorGraphicsIO;
import org.freehep.graphicsio.ps.PSGraphics2D;
import org.freehep.util.UserProperties;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.parameters.ScalingStrategy;
import edu.iu.sci2.visualization.scimaps.rendering.AbstractRenderablePageManager;
import edu.iu.sci2.visualization.scimaps.rendering.Layout;
import edu.iu.sci2.visualization.scimaps.tempvis.AbstractPageManager;
import edu.iu.sci2.visualization.scimaps.tempvis.AbstractPageManager.PageManagerRenderingException;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.VisualizationRunner;

public class JournalsMapAlgorithm implements Algorithm {
	// TODO Pick a path and remove these options
	public static final boolean RESOLVE_JOURNALS_TO_CANONICAL_NAME = false; // TODO Keep canonical.tsv in binary build only when true
	public static final boolean INCLUDE_DISTRIBUTION_TABLE_OUTPUT_DATA = false;
	
	public static final String OUT_FREQUENCY_COLUMN_NAME = "Frequency";
	public static final String OUT_JOURNAL_COLUMN_NAME = "Journal name";
	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";
	public static final String CSV_MIME_TYPE = "file:text/csv";

	private final Data parentData;
	private final Table table;
	private final String journalColumnName;
	private final ScalingStrategy scalingStrategy;
	private final String dataDisplayName;
	private final Layout layout;
	private final boolean showWindow;
	private final LogService logger;

	public JournalsMapAlgorithm(Data[] data, String journalColumnName,
			ScalingStrategy scalingStrategy, String dataDisplayName, Layout layout,
			boolean showWindow, LogService logger) {
		this.parentData = data[0];
		this.table = (Table) data[0].getData();
		this.journalColumnName = journalColumnName;
		this.scalingStrategy = scalingStrategy;
		this.dataDisplayName = dataDisplayName;
		this.layout = layout;
		this.showWindow = showWindow;
		this.logger = logger;
	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		JournalDataset dataset = JournalDataset.fromTable(table, journalColumnName, logger);
		
		logger.log(LogService.LOG_INFO, String.format(
				"Loaded %d occurrences of %d distinct journals.",
				dataset.size(),
				dataset.distinctJournals().size()));
		
		if (dataset.isEmpty()) {
			throw new AlgorithmExecutionException("No journals could be found in the data.");
		}

		MapOfScience mapOfScience = new MapOfScience("Fractional record count",
				dataset.copyAsIdentifierCountMap());
		
		if (mapOfScience.getMappedResults().isEmpty()) {
			throw new AlgorithmExecutionException(
					"No journals could be mapped to the Map of Science.");
		}
		
		float scalingFactor = scalingStrategy.scalingFactorFor(
				mapOfScience.getIdWeightMapping().values(), layout.getAreaForLegendMax());
		
		AbstractRenderablePageManager manager =
				layout.createPageManager(mapOfScience, scalingFactor, dataDisplayName);
		
		if (this.showWindow) {
			VisualizationRunner visualizationRunner = new VisualizationRunner(manager);
			visualizationRunner.setUp();
			visualizationRunner.run();
		}

		return datafy(mapOfScience, manager, dataset, this.parentData, this.logger);
	}

	public static Data[] datafy(MapOfScience mapOfScience, AbstractPageManager pageManager,
			JournalDataset journalOccurrences, Data parentData, LogService logger) {
		List<Data> outData = Lists.newArrayList();
		
		Set<Journal> foundJournals = mapOfScience.getMappedJournals();
		Table foundTable = makeJournalFrequencyTable(ImmutableSet.copyOf(foundJournals));
		outData.add(datafy(foundTable, "Journals located", parentData));
		
		Set<Journal> unfoundJournals = mapOfScience.getUnmappedJournals();
		Table unfoundTable = makeJournalFrequencyTable(ImmutableSet.copyOf(unfoundJournals));
		outData.add(datafy(unfoundTable, "Journals not located", parentData));
		
		if (journalOccurrences != null && INCLUDE_DISTRIBUTION_TABLE_OUTPUT_DATA) {
			outData.add(DataFactory.withClassNameAsFormat(
					mapOfScience.createDisciplineAnalysis(journalOccurrences).copyAsTable(),
					DataProperty.TABLE_TYPE,
					parentData,
					"Journal occurrences per discipline"));
			
			outData.add(DataFactory.withClassNameAsFormat(
					mapOfScience.createSubdisciplineAnalysis(journalOccurrences).copyAsTable(),
					DataProperty.TABLE_TYPE,
					parentData,
					"Journal occurrences per subdiscipline"));
		}
		
		try {
			Data visualizationData = createData(pageManager, parentData, logger);
			outData.add(visualizationData);
		} catch (DataCreationException e) {
			logger.log(LogService.LOG_ERROR, e.getMessage());
		}

		return outData.toArray(new Data[]{});
	}

	private static Data createData(AbstractPageManager pageManager, Data parentData,
			LogService logger) throws DataCreationException {
		try {
			File outFile = File.createTempFile("MapOfScience_Visualization_", ".ps");
			OutputStream out = new FileOutputStream(outFile);
			writePostscript(pageManager, out, logger);
			out.close();
			File hackOutFile = hackPageSizeForFreehepAndAdobe(outFile, pageManager);
			Data outData = new BasicData(hackOutFile, "file:text/ps");
			Dictionary<String, Object> metadata = outData.getMetadata();
			metadata.put(DataProperty.LABEL, "Map of Science Visualization");
			metadata.put(DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
			metadata.put(DataProperty.PARENT, parentData);

			return outData;
		} catch (IOException e) {
			throw new DataCreationException(
					"There was a problem creating the postscript file", e);
		}
	}
	
	/**
	 * HACK The file output by freehep 2.1.1 and converted to PDF with Adobe
	 * Distiller X does not respect the page size setting added to each page by
	 * freehep. It requires an extra "setpagedevice" to be added, but there is
	 * no way to do this with freehep. This hack method solves that issue.
	 * 
	 * This method should be replaced as soon as another solution is available.
	 * 
	 * TODO HACK FIXME XXX
	 * 
	 * @throws IOException
	 *             If there is a problem creating the temp file or reading from
	 *             the {@code psFile}.
	 */
	private static File hackPageSizeForFreehepAndAdobe(File psFile,
			AbstractPageManager pageManager) throws IOException {
		File outFile = File
				.createTempFile("MapOfScience_Visualization_", ".ps");
		BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
		BufferedReader reader = new BufferedReader(new FileReader(psFile));
		
		final String newline = System.getProperty("line.separator");
		final String MAGIC_STRING_TO_APPEND_AFTER = "%%EndComments";
		final String MAGIC_WORDS = "%%BeginFeature: *PageSize Default\n<< /PageSize [ "
				+ pageManager.pageDimensions().getWidth()
				+ " "
				+ pageManager.pageDimensions().getHeight()
				+ " ] >> setpagedevice\n%%EndFeature";
		String line;
		while ((line = reader.readLine()) != null) {
			out.write(line + newline);
			if (line.contains(MAGIC_STRING_TO_APPEND_AFTER)) {
				out.write(MAGIC_WORDS + newline);
			}
		}

		reader.close();
		out.close();
		return outFile;

	}

	private static void writePostscript(AbstractPageManager pageManager,
			OutputStream out, LogService logger) throws IOException {
		
		UserProperties psProperties = new UserProperties();
		psProperties.setProperty(PSGraphics2D.EMBED_FONTS, false);
		psProperties.setProperty(AbstractVectorGraphicsIO.TEXT_AS_SHAPES, false);
		psProperties.setProperty(PSGraphics2D.FIT_TO_PAGE, false);
		psProperties.setProperty(PSGraphics2D.PAGE_SIZE, PSGraphics2D.CUSTOM_PAGE_SIZE);
		psProperties.setProperty(PSGraphics2D.CUSTOM_PAGE_SIZE, pageManager.pageDimensions());
		psProperties.setProperty(PSGraphics2D.PAGE_MARGINS, new Insets(0, 0, 0, 0));
		
		PSGraphics2D psGraphic = new PSGraphics2D(out, pageManager.pageDimensions());
		psGraphic.setProperties(psProperties);
		psGraphic.setMultiPage(true);
		psGraphic.startExport();
		for (int pageNumber = 0; pageNumber < pageManager.numberOfPages(); pageNumber++) {
			try {
				psGraphic.openPage(pageManager.pageDimensions(), "Page " + (pageNumber + 1));
				psGraphic.setClip(0, 0, (int) pageManager.pageDimensions().getWidth(), (int) pageManager.pageDimensions().getHeight());
				pageManager.render(pageNumber, new GraphicsState(psGraphic));
				psGraphic.closePage();
			} catch (PageManagerRenderingException e) {
				logger.log(LogService.LOG_ERROR, e.getMessage(), e);
			}
		}
		
		psGraphic.endExport();
	}

	public static class DataCreationException extends Exception {
		private static final long serialVersionUID = 3544771808601245002L;

		public DataCreationException(String message) {
			super(message);
		}

		public DataCreationException(String string, Exception e) {
			super(string, e);
		}
	}

	private static Data datafy(Table table, String label, Data parentData) {
		Data tableData = new BasicData(table, table.getClass().getName());
		tableData.getMetadata().put(DataProperty.LABEL, label);
		tableData.getMetadata().put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		tableData.getMetadata().put(DataProperty.PARENT, parentData);

		return tableData;
	}

	public static Table makeJournalFrequencyTable(Set<Journal> journals) {
		Table table = new Table();
		table.addColumn(OUT_JOURNAL_COLUMN_NAME, String.class);
		table.addColumn(OUT_FREQUENCY_COLUMN_NAME, int.class);

		for (Journal journal : journals) {
			int row = table.addRow();
			table.setString(row, OUT_JOURNAL_COLUMN_NAME,
					journal.getJournalName());
			table.setInt(row, OUT_FREQUENCY_COLUMN_NAME,
					(int) journal.getJournalHitCount());
		}

		return table;
	}

}