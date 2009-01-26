package edu.iu.scipolicy.converter.nsf.db_to_prefuse;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

/*
 * TODO: This algorithm probably shouldn't be called db to prefuse,
 *  since there can (and will) be more than one type of extraction 
 *  from an nsf db to prefuse table.
 */
public class DbToPrefuseAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new DbToPrefuseAlgorithm(data, parameters, context);
    }
}