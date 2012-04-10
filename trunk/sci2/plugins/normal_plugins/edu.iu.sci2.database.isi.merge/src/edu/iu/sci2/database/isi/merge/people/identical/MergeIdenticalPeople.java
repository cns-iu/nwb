package edu.iu.sci2.database.isi.merge.people.identical;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.utilities.DataFactory;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.cns.database.merge.generic.analyze.mergetable.MergeTableAnalyzer;
import edu.iu.cns.database.merge.generic.analyze.mergetable.MergeTableAnalyzer.AnalysisException;
import edu.iu.cns.database.merge.generic.detect.redundant_author.MergeGroup;
import edu.iu.cns.database.merge.generic.detect.redundant_author.MergeGroup.DuplicateAuthorException;
import edu.iu.cns.database.merge.generic.detect.redundant_author.MergeGroupSettings;
import edu.iu.cns.database.merge.generic.prepare.marked.MergeMarker;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.KeyBasedGroupingStrategy;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.isi.merge.people.IsiPersonPriorities;
import edu.iu.sci2.database.scholarly.model.entity.Author;
import edu.iu.sci2.database.scholarly.model.entity.Person;
import edu.iu.sci2.database.scholarly.model.entity.Document;
import edu.iu.cns.database.load.framework.Schema;

public class MergeIdenticalPeople implements Algorithm, ProgressTrackable {
	public static final MergeGroupSettings MERGE_GROUP_SETTINGS;
	static {
		
		final String MERGE_GROUP_PK_IDENTIFIER = Schema.PRIMARY_KEY;
		final String MERGE_GROUP_IDENTIFIER = "Merge Group Identifier";
		
		final String AUTHORS_TABLE = "APP." + ISI.AUTHORS_TABLE_NAME;
		final String AUTHORS_DOCUMENT_FK = AUTHORS_TABLE + "." + Author.Field.DOCUMENT_ID.name();
		final String AUTHORS_PERSON_FK = AUTHORS_TABLE + "." + Author.Field.PERSON_ID.name();
		
		final String PERSON_TABLE = "APP." + ISI.PERSON_TABLE_NAME;
		final String PERSON_ID = PERSON_TABLE + "." + Person.Field.RAW_NAME.name();
		final String PERSON_PK = PERSON_TABLE + "." + Schema.PRIMARY_KEY;
		
		final String DOCUMENT_TABLE = "APP." + ISI.DOCUMENT_TABLE_NAME;
		final String DOCUMENT_ID = DOCUMENT_TABLE + "." + Document.Field.ISI_UNIQUE_ARTICLE_IDENTIFIER.name();
		final String DOCUMENT_PK = DOCUMENT_TABLE + "." + Schema.PRIMARY_KEY;
		
		MERGE_GROUP_SETTINGS = new MergeGroupSettings(
				MERGE_GROUP_PK_IDENTIFIER, MERGE_GROUP_IDENTIFIER,
				AUTHORS_TABLE, AUTHORS_DOCUMENT_FK, AUTHORS_PERSON_FK,
				PERSON_TABLE, PERSON_ID, PERSON_PK, DOCUMENT_TABLE,
				DOCUMENT_ID, DOCUMENT_PK);
	}
	
	private Data[] data;
	private CIShellContext context;
	private LogService logger;
	private ProgressMonitor monitor = ProgressMonitor.NULL_MONITOR;
	
	public MergeIdenticalPeople(Data[] data, CIShellContext context,
			LogService logger) {
		this.data = data;
		this.context = context;
		this.logger = logger;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		MergeMarker mergeMarker = new MergeMarker(
				new KeyBasedGroupingStrategy<String>(
						new IsiSimpleNameNormalized()),
				new IsiPersonPriorities());

		Database database = (Database) this.data[0].getData();

		Table mergeTable = mergeMarker.createMarkedMergingTable(MERGE_GROUP_SETTINGS.PERSON_TABLE,
				database, this.context);

		try {
			Collection<MergeGroup> mergeGroups = MergeGroup.createMergeGroups(
					mergeTable, database, MERGE_GROUP_SETTINGS);

			MergeGroup.checkForAuthorDuplication(mergeGroups);
		} catch (DuplicateAuthorException e) {
			this.logger.log(LogService.LOG_WARNING, e.getMessage());
		} catch (SQLException e) {
			throw new AlgorithmExecutionException(
					"A SQL Exception occured when trying to check ISI "
							+ "Merge Identical People for errors:\n"
							+ e.getMessage());
		}
		
		Database merged = MergeMarker.executeMerge(mergeTable, database, this.context, this.monitor);
		
		Data mergedData = DataFactory.likeParent(merged, this.data[0],
				"with identical people merged");
		
		Data mergedTable = DataFactory.withClassNameAsFormat(mergeTable,
				DataProperty.TABLE_TYPE, this.data[0], "Merge Table: based on "
						+ MERGE_GROUP_SETTINGS.PERSON_TABLE);
	
		List<Data> returnData = new ArrayList<Data>();
		returnData.add(mergedData);
		returnData.add(mergedTable);
		
		
		try {
			File mergeReportFile = File.createTempFile("Merge Report", ".txt");
			MergeTableAnalyzer.writeAnalysis(new FileOutputStream(mergeReportFile), mergeTable,
					Person.Field.RAW_NAME.name());

			Data mergeReportData = DataFactory.withClassNameAsFormat(
					mergeReportFile, DataProperty.TEXT_TYPE, this.data[0],
					"Text Log: A Merge Report for the mergeTable.");
			returnData.add(mergeReportData);
			
		} catch (AnalysisException e) {
			this.logger.log(LogService.LOG_ERROR,
					"Could not analyze the mergeTable:\n\t" + e.getMessage());
		} catch (IOException e) {
			this.logger.log(LogService.LOG_ERROR,
					"Could not create the merge report file for the mergeTable:\n\t"
							+ e.getMessage());
		}

		return returnData.toArray(new Data[0]);
	}


	public ProgressMonitor getProgressMonitor() {
		return this.monitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.monitor = monitor;
	}
}