package edu.iu.iv.modeling.tarl;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.iv.modeling.tarl.author.AuthorManager;
import edu.iu.iv.modeling.tarl.input.InputReader;
import edu.iu.iv.modeling.tarl.input.MainParameters;
import edu.iu.iv.modeling.tarl.input.impl.DefaultInputReader;
import edu.iu.iv.modeling.tarl.input.impl.DefaultMainParameters;
import edu.iu.iv.modeling.tarl.main.TarlHelper;
import edu.iu.iv.modeling.tarl.main.impl.DefaultTarlHelper;
import edu.iu.iv.modeling.tarl.output.GraphGenerator;
import edu.iu.iv.modeling.tarl.output.impl.JUNGGraphGenerator;
import edu.iu.iv.modeling.tarl.publication.PublicationManager;
import edu.uci.ics.jung.graph.Graph;

public class TarlAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    private File inputScriptFile;
    private File agingFunctionFile;
    private TarlHelper tarlHelper;
    private InputReader inputReader;
    private MainParameters mainParameters;
    
    public TarlAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
        inputScriptFile = new File((String)parameters.get("inputScriptFile"));
		agingFunctionFile = new File((String)parameters.get("agingFunctionFile"));

		println("Input Script File: " + inputScriptFile);
		println("Aging Function File: " + agingFunctionFile);
		println("Generating graphs...");

		try {
			initialize(inputScriptFile);
			tarlHelper.runModel();
		} catch (TarlException e) {
			throw new AlgorithmExecutionException(e);
		}

		AuthorManager am = tarlHelper.getTarlExecuter().getAuthorManager();
		PublicationManager pm = tarlHelper.getTarlExecuter()
				.getPublicationManager();
		GraphGenerator gg = new JUNGGraphGenerator(pm, am);

		Data[] d = new Data[3];
		d[0] =(Data) addModel(gg.generateCoAuthorGraph(), "TARL Co-author graph");
		d[1] = (Data) addModel(gg.generateCitationGraph(), "TARL Paper-citation graph");
		d[2] = (Data) addModel(gg.generateAuthorPublicationGraph(),
				"TARL Author-Paper graph");

		tarlHelper.cleanUpSystem();
		println("done.");
       
        return d;
    }
    
    private void println(String string) {
        LogService log = (LogService) context.getService(LogService.class.getName());
        log.log(LogService.LOG_INFO, string);
    }
    private Data addModel(Object model, String desc) {
        BasicData dm = new BasicData(model,Graph.class.getName());
        Dictionary map = dm.getMetadata();
        map.put(DataProperty.LABEL,desc);
        map.put(DataProperty.TYPE,DataProperty.NETWORK_TYPE);
        return dm;
    }

    private void initialize(File scriptFile) throws TarlException {
        inputReader = new DefaultInputReader();
        inputReader.initialize(scriptFile);
        mainParameters = new DefaultMainParameters();
        mainParameters = inputReader.getModelParameters();
        this.tarlHelper = new DefaultTarlHelper();
        this.tarlHelper.initializeModel(mainParameters.getHelperParameters(),
                agingFunctionFile);
    }
}