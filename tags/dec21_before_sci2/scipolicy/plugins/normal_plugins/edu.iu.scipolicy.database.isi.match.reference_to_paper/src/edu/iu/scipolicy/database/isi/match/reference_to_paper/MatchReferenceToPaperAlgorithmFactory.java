package edu.iu.scipolicy.database.isi.match.reference_to_paper;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class MatchReferenceToPaperAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
        return new MatchReferenceToPaperAlgorithm(data, parameters, ciShellContext);
    }
}