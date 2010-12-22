package edu.iu.sci2.database.nsf.load.converter;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;

public class NSFDatabaseToAnyDatabaseConverterAlgorithm implements Algorithm {
	private Data inputData;
	private Database nsfDatabase;

	public NSFDatabaseToAnyDatabaseConverterAlgorithm(
			Data[] data,
			Dictionary parameters,
			CIShellContext ciShellContext) {
		this.inputData = data[0];
		this.nsfDatabase = (Database) this.inputData.getData();
	}

	public Data[] execute() {
		Data outputData = new BasicData(this.nsfDatabase, Database.GENERIC_DB_MIME_TYPE);
		Dictionary outputMetadata = outputData.getMetadata();
		outputMetadata.put(DataProperty.LABEL, "as a generic database");
		outputMetadata.put(DataProperty.PARENT, this.inputData);
		outputMetadata.put(DataProperty.TYPE, DataProperty.DATABASE_TYPE);

		return new Data[] { outputData };
	}
}