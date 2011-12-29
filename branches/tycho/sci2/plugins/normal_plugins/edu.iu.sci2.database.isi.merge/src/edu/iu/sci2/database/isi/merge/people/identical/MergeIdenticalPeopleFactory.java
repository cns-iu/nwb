package edu.iu.sci2.database.isi.merge.people.identical;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class MergeIdenticalPeopleFactory implements AlgorithmFactory {
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
        return new MergeIdenticalPeople(data, context);
    }
}