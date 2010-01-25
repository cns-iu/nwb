package edu.iu.scipolicy.database.isi.load;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.cns.database.loader.framework.utilities.DatabaseModel;
import edu.iu.cns.database.loader.framework.utilities.DerbyDatabaseCreator;
import edu.iu.nwb.shared.isiutil.ISITableReaderHelper;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.nwb.shared.isiutil.exception.ISILoadingException;
import edu.iu.nwb.shared.isiutil.exception.ReadISIFileException;
import edu.iu.scipolicy.database.isi.load.utilities.ISITablePreprocessor;
import edu.iu.scipolicy.database.isi.load.utilities.parser.ISITableModelParser;

public class ISIDatabaseLoaderAlgorithm implements Algorithm {
	public static final boolean SHOULD_NORMALIZE_AUTHOR_NAMES = true;
	public static final boolean SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS = true;
	public static final boolean SHOULD_FILL_FILE_METADATA = true;
	public static final boolean SHOULD_CLEAN_CITED_REFERENCES = false;

    private Data inData;
    private LogService logger;
    private DatabaseService databaseProvider;
    
    public ISIDatabaseLoaderAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
        this.inData = data[0];

        this.logger = (LogService)ciShellContext.getService(LogService.class.getName());
        this.databaseProvider =
        	(DatabaseService)ciShellContext.getService(DatabaseService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
	    	// Convert input ISI data to an ISI table.

    		Table isiTable = convertISIToTable(this.inData, this.logger);

    		// Preprocess the ISI table: generate unique IDs for rows without them.

    		ISITablePreprocessor.generateMissingUniqueIDs(isiTable);

	    	// Preprocess the ISI table: remove duplicate Documents (on the row level).

    		Collection<Integer> rows =
    			ISITablePreprocessor.removeRowsWithDuplicateDocuments(isiTable);

    		// Convert the ISI table to an ISI database.

    		Database database = convertTableToDatabase(isiTable, rows);

	    	// Annotate ISI database as output data with metadata and return it.

    	    return annotateOutputData(database, this.inData);
    	} catch (ISILoadingException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }
    
    private Table convertISIToTable(Data isiData, LogService logger)
    		throws AlgorithmExecutionException {
    	File inISIFile = (File)isiData.getData();

    	try {
    		return ISITableReaderHelper.readISIFile(
    			inISIFile,
    			logger,
    			SHOULD_NORMALIZE_AUTHOR_NAMES,
    			SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS,
    			SHOULD_FILL_FILE_METADATA,
    			SHOULD_CLEAN_CITED_REFERENCES);
    	} catch (ReadISIFileException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }

    private Database convertTableToDatabase(Table table, Collection<Integer> rows)
    		throws ISILoadingException {
    	try {
	    	// Create an in-memory ISI model based off of the table.

    		DatabaseModel model = new ISITableModelParser().parseModel(table, rows);

	    	// Use the ISI model to create an ISI database.

	    	return DerbyDatabaseCreator.createFromModel(this.databaseProvider, model, "ISI");
    	} catch (DatabaseCreationException e) {
    		throw new ISILoadingException(e.getMessage(), e);
    	} catch (SQLException e) {
    		throw new ISILoadingException(e.getMessage(), e);
    	}
    }

    private Data[] annotateOutputData(Database isiDatabase, Data parentData) {
    	Data data = new BasicData(isiDatabase, ISI.ISI_DATABASE_MIME_TYPE);
    	Dictionary<String, Object> parentMetadata = parentData.getMetadata();
    	Dictionary<String, Object> metadata = data.getMetadata();
    	metadata.put(
    		DataProperty.LABEL, "ISI Database From " + parentMetadata.get(DataProperty.LABEL));
    	metadata.put(DataProperty.TYPE, DataProperty.DATABASE_TYPE);
    	metadata.put(DataProperty.PARENT, parentData);

    	//return null;
    	return new Data[] { data };
    }
    
    /*private Data[] wrapAsOutputData(Object outputObject, Data parentData) {
    	Data outData = new BasicData(outputObject, outputObject.getClass().getName());
    	Dictionary metadata = outData.getMetadata();
    	metadata.put(DataProperty.PARENT, parentData);
    	metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);

    	return new Data[] { outData };
    }*/

    /*public static final String BASE_PATH =
    	"C:\\Documents and Settings\\pataphil\\Desktop\\ISI_CSV";
    public static final String BASE_RESULT_FILE_NAME = "result_";
    public static final String BASE_RESULT_FILE_EXTENSION = ".csv";

	public static void main(String[] args) {
		try {
			File basePath = new File(BASE_PATH);
			File[] files = basePath.listFiles();
			List<Table> tables = new ArrayList<Table>();
			Map<Integer, BufferedWriter> resultFiles = new HashMap<Integer, BufferedWriter>();

			for (File file : files) {
				Data[] data = TestUtilities.createTestTableData(file);
				tables.add((Table)data[0].getData());
			}
			// TODO: Map between original file and table.

			for (Table table : tables) {
				Column citedReferencesColumn =
					table.getColumn(ISITag.CITED_REFERENCES.getColumnName());

				for (int ii = 0; ii < citedReferencesColumn.getRowCount(); ii++) {
					String citedReferencesString = citedReferencesColumn.getString(ii);
					String citedReferences[] = citedReferencesString.split("\\|");

					for (String citedReference : citedReferences) {
						String[] tokens = citedReference.split(",");
						int tokenCount = tokens.length;
						BufferedWriter writer = resultFiles.get(tokenCount);

						if (writer == null) {
							writer = new BufferedWriter(new FileWriter(
								BASE_PATH +
									"\\" +
									BASE_RESULT_FILE_NAME +
									tokenCount +
									BASE_RESULT_FILE_EXTENSION,
								true));
							writer.write("Cited References\n");
							resultFiles.put(tokenCount, writer);
						}

						writer.write("\"" + citedReference + "\"" + "\n");
					}
				}
			}

			for (BufferedWriter writer : resultFiles.values()) {
				writer.close();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}*/
}