package edu.iu.nwb.converter.prefusecsv.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Table;
import prefuse.data.io.CSVTableReader;
import prefuse.data.io.DataIOException;

/**
 * @author Russell Duhon
 */
public class PrefuseCsvReader implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public PrefuseCsvReader(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	File file = (File) data[0].getData();

    	try{
    		CSVTableReader tableReader = new CSVTableReader();
    		tableReader.setHasHeader(true);
			Table table= tableReader.readTable(new FileInputStream(file));
    		Data[] dm = new Data[] {new BasicData(table, Table.class.getName())};
    		dm[0].getMetadata().put(DataProperty.LABEL, "Prefuse Table: " + file);
            dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
    		return dm;
    	} catch (DataIOException dioe){
    		throw new AlgorithmExecutionException("DataIOException", dioe);
    	} catch (SecurityException exception){
    		throw new AlgorithmExecutionException("SecurityException", exception);
    	} catch (FileNotFoundException e){
    		throw new AlgorithmExecutionException("FileNotFoundException", e);
    	}
    }
}