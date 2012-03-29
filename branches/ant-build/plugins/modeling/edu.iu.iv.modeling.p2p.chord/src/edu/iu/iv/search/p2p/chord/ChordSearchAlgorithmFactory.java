package edu.iu.iv.search.p2p.chord;

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

public class ChordSearchAlgorithmFactory implements AlgorithmFactory, DataValidator {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new ChordSearchAlgorithm(data, parameters, context);
    }
    
    public String validate(Data[] data) {
        String supports = "Not a CHORD Graph";
        
        if (data[0].getData() instanceof Graph) {
            Graph g = (Graph) data[0].getData();
            if (g.containsUserDatumKey("0")) {
                Vertex v = (Vertex) g.getUserDatum("0");
                if (v.containsUserDatumKey("id"))
                                supports = "";
            }
        }
        return supports;
    }
}
