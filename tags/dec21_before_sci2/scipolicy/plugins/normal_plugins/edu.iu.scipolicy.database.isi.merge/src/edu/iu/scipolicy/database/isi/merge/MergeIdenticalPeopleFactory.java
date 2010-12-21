package edu.iu.scipolicy.database.isi.merge;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class MergeIdenticalPeopleFactory implements AlgorithmFactory {
    @SuppressWarnings("unchecked") // Raw Dictionary
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new MergeIdenticalPeople(data, context);
    }
}