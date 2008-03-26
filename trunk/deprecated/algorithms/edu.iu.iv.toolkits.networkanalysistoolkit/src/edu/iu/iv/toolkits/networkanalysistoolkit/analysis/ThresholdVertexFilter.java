package edu.iu.iv.toolkits.networkanalysistoolkit.analysis;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.filters.GeneralVertexAcceptFilter;

/**
 * @author Shashikant
 */
public class ThresholdVertexFilter extends GeneralVertexAcceptFilter {

    private int min, max;

    private boolean directed;

    public ThresholdVertexFilter(int min, int max) {
        this.min = min;
        this.max = max;
        this.directed = false;
    }

    private int idmin, idmax, odmin, odmax;

    public ThresholdVertexFilter(int idmin, int idmax, int odmin, int odmax) {
        this.idmin = idmin;
        this.idmax = idmax;
        this.odmin = odmin;
        this.odmax = odmax;
        this.directed = true;
    }

    public boolean acceptVertex(Vertex vertex) {
        boolean accept = false ;
        if (this.directed) {
            if (vertex.inDegree() > idmin && vertex.inDegree() < idmax && 
                    vertex.outDegree() > odmin && vertex.outDegree() < odmax)
                accept = true ;
            else
                accept = false ;
        }
        else {
            if (vertex.degree() > min && vertex.degree() < max)
                accept = true ;
            else
                accept = false ;
        }
        return accept ;
    }

    public String getName() {
        return "General Vertex Threshold filter";
    }
}
