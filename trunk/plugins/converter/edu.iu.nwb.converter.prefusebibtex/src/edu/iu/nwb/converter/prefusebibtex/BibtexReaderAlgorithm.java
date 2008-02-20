package edu.iu.nwb.converter.prefusebibtex;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

public class BibtexReaderAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService log;
    
    public BibtexReaderAlgorithm(Data[] data, Dictionary parameters, CIShellContext context, LogService log) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.log = log;
    }

    public Data[] execute() {
    	File bibtexFile = (File) data[0].getData();
    	String bibtexFilePath = bibtexFile.getAbsolutePath();
    	bibtex.Main.main(new String[] {"-expandStringDefinitions", bibtexFilePath});
		return null;	
    }
}