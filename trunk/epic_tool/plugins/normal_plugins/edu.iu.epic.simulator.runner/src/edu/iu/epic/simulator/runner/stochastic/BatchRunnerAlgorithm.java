package edu.iu.epic.simulator.runner.stochastic;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.framework.BundleContext;

import com.google.common.collect.Lists;

public abstract class BatchRunnerAlgorithm implements Algorithm {
	public static final int THREAD_POOL_SIZE = 4;
	
	private Data[] data;
	private Dictionary<String, Object> batchParameters;
	private CIShellContext ciContext;
	private BundleContext bundleContext;

	
	public BatchRunnerAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			CIShellContext ciContext,
			BundleContext bundleContext) {
		this.data = data;
		this.batchParameters = parameters;
		this.ciContext = ciContext;
		this.bundleContext = bundleContext;
	}

	public abstract Algorithm getSingleRunnerAlgorithm(
			Data[] singleData,
			Dictionary<String, Object> singleParameters,
			CIShellContext singleCIContext,
			BundleContext singleBundleContext);
	
	public Data[] execute() throws AlgorithmExecutionException {
		/* Note that the batch runner seed parameter ID is the same as the single runner seed
		 * parameter ID because the batch runner steals all of the single runner's
		 * AttributeDefinitions in its Factory's mutateParameters method.
		 */
		int batchSeed =
			(Integer) batchParameters.get(BatchRunnerAlgorithmFactory.SEED_PARAMETER_ID);
		// For generating the sequence of single-run seeds.
		Random batchRandom = new Random(batchSeed);
		
		
		int numberOfRuns =
			(Integer) batchParameters.get(
					BatchRunnerAlgorithmFactory.NUMBER_OF_RUNS_PARAMETER_ID);
		

		// Create the single run tasks.
		Collection<Callable<Data[]>> runTasks = new HashSet<Callable<Data[]>>();
		for (int runIndex = 0; runIndex < numberOfRuns; runIndex++) {			
			int singleSeed = batchRandom.nextInt();

			Dictionary<String, Object> singleParameters = createShallowCopy(batchParameters);
			singleParameters.put(
					BatchRunnerAlgorithmFactory.SEED_PARAMETER_ID, singleSeed);
			
			Algorithm singleAlgorithm =
				getSingleRunnerAlgorithm(data, singleParameters, ciContext, bundleContext);
			
			Callable<Data[]> runTask = new SingleRunTask(singleAlgorithm);
			
			runTasks.add(runTask);
		}

		// Invoke all run tasks and get out the result data.
		ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		try {
			Collection<Future<Data[]>> futureOutputData = pool.invokeAll(runTasks);
			
			List<Data> outputData = Lists.newArrayList();
			int runIndex = 0;
			for (Future<Data[]> futureOutputDatum : futureOutputData) {
				try {
					Data[] singleOutputDataArray = futureOutputDatum.get();
					for (Data singleOutputData : singleOutputDataArray) {
						addRunNumberToLabel(singleOutputData, runIndex);
						outputData.add(singleOutputData);
					}
					
					runIndex++;
				} catch (ExecutionException e) {
					String message =
						"Error getting simulation results for some run: " + e.getMessage();
					throw new AlgorithmExecutionException(message, e);
				}
			}

			// TODO Eventually, summarize the output.
			return outputData.toArray(new Data[]{});
		} catch (InterruptedException e) {
			String message = "Error: Interrupted while running simulation: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		}
	}

	private void addRunNumberToLabel(Data coreData, int runIndex) {
		coreData.getMetadata().put(
				DataProperty.LABEL,
				createSingleOutputDataLabel(coreData, runIndex));
	}	
	private String createSingleOutputDataLabel(Data coreOutData, int runIndex) {
		String originalLabel = (String) coreOutData.getMetadata().get(DataProperty.LABEL);
		
		int userFriendlyRunIndex = runIndex + 1;
		return originalLabel + ": Run " + userFriendlyRunIndex;
	}
	
	// Key and value are not cloned.
	private static <K, V> Dictionary<K, V> createShallowCopy(
			Dictionary<K, V> originalDictionary) {
		Dictionary<K, V> newDictionary = new Hashtable<K, V>();
		
		for (Enumeration<K> keys = originalDictionary.keys(); keys.hasMoreElements();) {
			K key = keys.nextElement();
			V value = originalDictionary.get(key);
			
			newDictionary.put(key, value);
		}
		
		return newDictionary;
	}
	
	
	private class SingleRunTask implements Callable<Data[]> {
		private Algorithm singleAlgorithm;

		public SingleRunTask(Algorithm singleAlgorithm) {
			this.singleAlgorithm = singleAlgorithm;
		}
		
		public Data[] call() throws Exception {
			try {
				return singleAlgorithm.execute();
			} catch (AlgorithmExecutionException e) {
				String message = "Error on one of the runs: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			}
		}
	}
}
