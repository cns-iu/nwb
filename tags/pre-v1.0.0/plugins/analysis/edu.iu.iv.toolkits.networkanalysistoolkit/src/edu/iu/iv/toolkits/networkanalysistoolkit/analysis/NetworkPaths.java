/*
 * Created on Sep 24, 2004
 */
package edu.iu.iv.toolkits.networkanalysistoolkit.analysis;

import java.util.Map;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;
import edu.uci.ics.jung.statistics.GraphStatistics;

/**
 * @author Shashikant
 */
public class NetworkPaths {

    private NetworkPaths() {
        // do nothing
    }
    public static NetworkPaths getNewInstance() {
        return new NetworkPaths() ;
    }

    public static double BAD_VALUE = -22.7 ;
    
    public double getCharacteristicPathLength(Network network) {

    	Map nodeToDistanceMap = GraphStatistics.averageDistances(network.getGraph()) ;
        if (nodeToDistanceMap == null)
            return  BAD_VALUE;
        else {
        	DoubleArrayList dal = new DoubleArrayList();
        	dal.addAllOf(nodeToDistanceMap.values());
        	
            return Descriptive.mean(dal);
        }
    }

    public int getDiameter(Network network) {
        return (int)GraphStatistics.diameter(network.getGraph());
    }
}