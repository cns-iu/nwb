package edu.iu.iv.toolkits.networkanalysistoolkit;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import edu.uci.ics.jung.graph.Graph;
import edu.iu.iv.toolkits.networkanalysistoolkit.analysis.Network;
import edu.iu.iv.toolkits.networkanalysistoolkit.gui.NetworkAnalysisToolkitGUI;

public class NetworkAnalysisToolkitAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public NetworkAnalysisToolkitAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	NetworkAnalysisToolkitGUI natgui = new NetworkAnalysisToolkitGUI();
        Network network = new Network((Graph)data[0].getData());
        natgui.setNetwork(network);
        natgui.setLocation(200, 200);
        natgui.setVisible(true);
        return null;
    }
}