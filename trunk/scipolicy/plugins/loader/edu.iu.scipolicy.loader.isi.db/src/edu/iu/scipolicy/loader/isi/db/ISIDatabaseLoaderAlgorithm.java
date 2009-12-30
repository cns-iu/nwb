package edu.iu.scipolicy.loader.isi.db;

import java.io.File;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseService;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.nwb.shared.isiutil.ISITableReaderHelper;
import edu.iu.nwb.shared.isiutil.exception.ISILoadingException;
import edu.iu.nwb.shared.isiutil.exception.ReadISIFileException;
import edu.iu.scipolicy.loader.isi.db.model.ISIModel;
import edu.iu.scipolicy.loader.isi.db.utilities.ISIDatabaseCreator;
import edu.iu.scipolicy.loader.isi.db.utilities.ISITablePreprocessor;
import edu.iu.scipolicy.loader.isi.db.utilities.exception.ModelCreationException;
import edu.iu.scipolicy.loader.isi.db.utilities.parser.ISITableModelParser;

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

	    	// Preprocess the ISI table to remove duplicate Documents (on the row level).

    		Collection<Integer> rows = ISITablePreprocessor.removeRowsWithDuplicateDocuments(isiTable);

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
    	/*
    	 * TODO: If you want to do template style commenting, describe what's going on throughout
    	 *  the method.
    	 */ 
   		// TODO: (What happens after you read the input ISI data?)
    	// Read the input ISI data.

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

    		ISIModel model = new ISITableModelParser().parseModel(table, rows);

	    	// Use the ISI model to create an ISI database.

	    	return ISIDatabaseCreator.createFromModel(this.databaseProvider, model);
    	} catch (ModelCreationException e) {
    		throw new ISILoadingException(e.getMessage(), e);
    	}
    }

    private Data[] annotateOutputData(Database isiDatabase, Data parentData) {
    	return null;
    }
    
    private Data[] wrapAsOutputData(Object outputObject, Data parentData) {
    	Data outData = new BasicData(outputObject, outputObject.getClass().getName());
    	Dictionary metadata = outData.getMetadata();
    	metadata.put(DataProperty.PARENT, parentData);
    	metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);

    	return new Data[] { outData };
    }

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