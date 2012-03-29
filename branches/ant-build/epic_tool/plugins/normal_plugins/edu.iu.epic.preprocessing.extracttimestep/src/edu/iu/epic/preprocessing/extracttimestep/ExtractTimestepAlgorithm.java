package edu.iu.epic.preprocessing.extracttimestep;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.LinkedHashMap;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class ExtractTimestepAlgorithm implements Algorithm {
	private File entireNwbFile;
	protected int timestep;
	protected String timestepColumnNamePrefix;
	private Data parentData;

    public ExtractTimestepAlgorithm(File nwbFile, int timestep, String timestepColumnNamePrefix, Data parentData) {
		this.entireNwbFile = nwbFile;
		this.timestep = timestep;
		this.timestepColumnNamePrefix = timestepColumnNamePrefix;
		this.parentData = parentData;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try {
			NWBFileParser parser = new NWBFileParser(entireNwbFile);
			File timestepNwbFile = NWBFileUtilities.createTemporaryNWBFile();
			
			parser.parse(new NWBFileWriter(timestepNwbFile) {
				@Override
				public void setNodeSchema(LinkedHashMap<String, String> schema) {
					super.setNodeSchema(
							Maps.newLinkedHashMap(
									Maps.filterKeys( // TODO Will this maintain order?
										schema,
										new Predicate<String>() {
											public boolean apply(String attributeName) {												
												boolean isATimestepColumn =
													attributeName.startsWith(
															timestepColumnNamePrefix);

												boolean isTheTimestepColumn =
													attributeName.matches(
															timestepColumnNamePrefix +
															"0*" +
															timestep);
												
												return isTheTimestepColumn || !isATimestepColumn;
											}								
										})));
				}
			});
			
			return new Data[]{ datafyTimestepNwbFile(timestepNwbFile, timestep, parentData) };
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e); // TODO
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e); // TODO
		}
    }
	
	protected static String makeTimestepColumnName(String timestepColumnNamePrefix, int timestep) {
		return String.format("%s%d", timestepColumnNamePrefix, timestep);
	}
	
	private static Data datafyTimestepNwbFile(File nwbFile, int timestep, Data parentData) {
		Data nwbData = new BasicData(nwbFile, NWBFileProperty.NWB_MIME_TYPE);
		
		Dictionary<String, Object> metadata = nwbData.getMetadata();
		metadata.put(DataProperty.LABEL, String.format("Timestep %d", timestep));
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		metadata.put(DataProperty.PARENT, parentData);
		
		return nwbData;
	}
}