package edu.iu.nwb.toolkit.networkanalysis.algorithms;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.nwb.toolkit.networkanalysis.analysis.NetworkProperties;



public class ToolkitAlgorithm implements Algorithm{
	Data[] data;
    Dictionary parameters;
    CIShellContext ciContext;
    LogService logger;
	
	public ToolkitAlgorithm(Data[] dm, Dictionary parameters, CIShellContext cContext){
		this.data = dm;
		this.parameters = parameters;
		this.ciContext = cContext;
		logger = (LogService)ciContext.getService(LogService.class.getName());
	}
	
	public Data[] execute() {
		// TODO Auto-generated method stub
/*		
		System.out.println("I'm just seeing if I work");
		NetworkAnalysisToolkitGUI natgui = new NetworkAnalysisToolkitGUI();
		System.out.println(data[0].getFormat());
		Network net = new Network((edu.uci.ics.jung.graph.Graph)data[0].getData());*/
		prefuse.data.Graph netGraph = (prefuse.data.Graph)data[0].getData();
		NetworkProperties np = new NetworkProperties(netGraph);
		System.out.println(np.isConnected());
		/*System.out.println(net.getNumEdges());
		natgui.setNetwork(net);
		natgui.setLocation(200, 200);
		natgui.setVisible(true);*/
		return null;
	}

}
