package edu.iu.epic.spemshell.runner;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.collections.CollectionUtils;

import edu.iu.epic.spemshell.runner.parsing.generated.ModelFileLexer;
import edu.iu.epic.spemshell.runner.parsing.generated.ModelFileParser;

public class ModelFileReader {
	private ModelFileParser parser;

	public ModelFileReader(String modelFilePath) throws RecognitionException, IOException {
		ModelFileLexer lex = new ModelFileLexer(new ANTLRFileStream(modelFilePath));
       	CommonTokenStream tokens = new CommonTokenStream(lex);
       	parser = new ModelFileParser(tokens);
    	
        parser.modelFile();
	}
	
	public Map<String, String> getParameterBindings() {
		return parser.getParameterBindings();
	}
	
	public Set<String> getReferencedParameters() {
		return parser.getReferencedParameters();
	}
	
	@SuppressWarnings("unchecked") // TODO
	public Collection<String> findUnboundReferencedParameters() {
		return CollectionUtils.subtract(
				getReferencedParameters(),
				getParameterBindings().keySet());
	}

	public Collection<String> getInfectionCompartments() {
		return parser.getInfectionCompartments();
	}
}
