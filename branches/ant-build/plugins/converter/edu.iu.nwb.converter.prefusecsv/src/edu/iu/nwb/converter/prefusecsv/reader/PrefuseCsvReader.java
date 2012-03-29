package edu.iu.nwb.converter.prefusecsv.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Table;
import prefuse.data.io.CSVTableReader;
import prefuse.data.io.DataIOException;
import edu.iu.nwb.converter.prefusecsv.preprocessing.CSVFilePreprocessor;

/**
 * @author Russell Duhon
 */
public class PrefuseCsvReader implements Algorithm {
    private File inCSVFile;
    
    
    public static class Factory implements AlgorithmFactory {
        public Algorithm createAlgorithm(
        		Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
            return new PrefuseCsvReader(data);
        }
    }
    
    public PrefuseCsvReader(Data[] data) {
        this.inCSVFile = (File) data[0].getData();
    }
    
    public PrefuseCsvReader(File inFile) {
        this.inCSVFile = inFile;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		File tmpCSVFile = CSVFilePreprocessor.execute(inCSVFile);
    		CSVTableReader tableReader = new CSVTableReader();
    		tableReader.setHasHeader(true);
			Table table = tableReader.readTable(new FileInputStream(tmpCSVFile));
			
    		return createOutData(tmpCSVFile, table);
    	} catch (DataIOException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	} catch (SecurityException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	} catch (IOException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
		}
    }

	private Data[] createOutData(File file, Table table) {
		Data[] dm = new Data[] {new BasicData(table, Table.class.getName())};
		dm[0].getMetadata().put(DataProperty.LABEL, "Prefuse Table: " + file);
		dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		return dm;
	}
}