package edu.iu.sci2.database.isi.merge.people.identical;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

public class MergeIdenticalPeopleFactory implements AlgorithmFactory {
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		LogService logger = (LogService) ciShellContext
				.getService(LogService.class.getName());
		return new MergeIdenticalPeople(data, ciShellContext, logger);
    }
}