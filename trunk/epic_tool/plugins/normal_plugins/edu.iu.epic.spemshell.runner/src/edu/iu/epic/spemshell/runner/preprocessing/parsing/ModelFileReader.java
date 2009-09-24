package edu.iu.epic.spemshell.runner.preprocessing.parsing;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.collections.CollectionUtils;

import edu.iu.epic.spemshell.runner.preprocessing.parsing.generated.ModelFileLexer;
import edu.iu.epic.spemshell.runner.preprocessing.parsing.generated.ModelFileParser;

public class ModelFileReader {
	private ModelFileParser parser;

	public ModelFileReader(String modelFilePath) throws RecognitionException, IOException {
		ModelFileLexer lex = new ModelFileLexer(new ANTLRFileStream(modelFilePath));
       	CommonTokenStream tokens = new CommonTokenStream(lex);
       	this.parser = new ModelFileParser(tokens);
    	
        this.parser.modelFile();
	}
	
	public Map<String, String> getParameterBindings() {
		return this.parser.getParameterBindings();
	}
	
	public Set<String> getReferencedParameters() {
		return this.parser.getReferencedParameters();
	}
	
	@SuppressWarnings("unchecked") // TODO
	public Collection<String> findUnboundReferencedParameters() {
		return CollectionUtils.subtract(
				getReferencedParameters(),
				getParameterBindings().keySet());
	}
	
	public String getSusceptibleCompartmentID() {
		return this.parser.getSusceptibleCompartmentID();
	}

	public Set<String> getInfectionCompartments() {
		return this.parser.getInfectionCompartments();
	}
	
	public Set<String> getLatentCompartments() {
		return this.parser.getLatentCompartments();
	}
	
	public Set<String> getRecoveredCompartments() {
		return this.parser.getRecoveredCompartments();
	}
}
