package edu.iu.iv.search.p2p.can;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.DataValidator;
import org.cishell.framework.data.Data;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;

public class CanSearchAlgorithmFactory implements AlgorithmFactory, DataValidator {
    private MetaTypeProvider provider;

    protected void activate(ComponentContext ctxt) {
        //You may delete all references to metatype service if 
        //your algorithm does not require parameters and return
        //null in the createParameters() method
        MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
        provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());       
    }
    protected void deactivate(ComponentContext ctxt) {
        provider = null;
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new CanSearchAlgorithm(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return provider;
    }
    
    public String validate(Data[] data) {
        //make sure its a Can Network Graph
        String supports = "Not a CAN Graph";
        if (data[0].getData() instanceof Graph) {
            Graph g = (Graph) data[0].getData();
            if (g.containsUserDatumKey("0")) {
                Vertex v = (Vertex) g.getUserDatum("0");
                if (v.containsUserDatumKey("id"))
                    if (v.containsUserDatumKey("xmid"))
                        if (v.containsUserDatumKey("ymid"))
                            if (v.containsUserDatumKey("zmid"))
                                supports = "";
            }
        }
        
        return supports;
    }
}
