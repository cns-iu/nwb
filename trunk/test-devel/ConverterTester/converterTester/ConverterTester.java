package converterTester;

import java.io.File;

public interface ConverterTester {

	public File convert(File initialGraph, AlgorithmFactory[] converter);
	 public void compare(File initialGraph, File convertedGraph);
}
