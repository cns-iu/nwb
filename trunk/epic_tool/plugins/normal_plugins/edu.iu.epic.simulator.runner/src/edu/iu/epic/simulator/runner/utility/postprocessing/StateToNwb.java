package edu.iu.epic.simulator.runner.utility.postprocessing;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;
import edu.iu.nwb.util.nwbfile.ParsingException;

/** TODO Will this behave if the input node IDs are not sequential? */
public class StateToNwb {
	public static final String HEADER_MARKER = "# time";
	
	private File nodeLevelStateFile;
	private File rawNwbFile;
	private List<String> compartments;
	
	public StateToNwb(File nodeLevelStateFile, File nwbFile, List<String> compartments) {
		this.nodeLevelStateFile = nodeLevelStateFile;
		this.rawNwbFile = nwbFile;
		this.compartments = compartments;
	}
	
	public File convert() throws IOException, ParsingException {
		final Map<String, Map<Integer, String>> nodeToTimestepToState =
			readNodeToTimestepToState(nodeLevelStateFile, compartments);
		
		final SortedSet<Integer> timesteps = Sets.newTreeSet();
		for (Map<Integer, String> timestepToState : nodeToTimestepToState.values()) {
			timesteps.addAll(timestepToState.keySet());
		}
		
		/* How many digits do we need to hold the largest timestep?
		 * We will zero-pad the timesteps in all timestep attribute names up to that length
		 * so those attributes sort better throughout the tool.
		 */
		final int digits = String.valueOf(timesteps.last()).length();
		
		File seriesNwbFile = NWBFileUtilities.createTemporaryNWBFile();
		new NWBFileParser(rawNwbFile).parse(new NWBFileWriter(seriesNwbFile) {
			@Override
			public void setNodeSchema(LinkedHashMap<String, String> schema) {
				for (int timestep : timesteps) {
					// TODO Name collision?
					schema.put(makeTimeKey(timestep, digits), NWBFileProperty.TYPE_STRING);
				}
				
				super.setNodeSchema(schema);
			}
			
			@Override
			// TODO Is this necessary after removing testing code?
			public void setNodeCount(int numberOfNodes) {
				super.setNodeCount(-1);
			}
			
			@Override
			public void addNode(int id, String label, Map<String, Object> attributes) {				
				String nodeId = String.valueOf(id);
				
				if (nodeToTimestepToState.containsKey(nodeId)) {
					Set<Entry<Integer, String>> timestepToState =
						nodeToTimestepToState.get(nodeId).entrySet();
					
					for (Entry<Integer, String> timestepAndState : timestepToState) {
						int timestep = timestepAndState.getKey();
						String state = timestepAndState.getValue();
						
						attributes.put(makeTimeKey(timestep, digits), state);
					}
				}
				
				super.addNode(id, label, attributes);
			}
		});
		
		return seriesNwbFile;
	}

	public static String makeTimeKey(int timestep, int length) {
		String timestepString = zeroPad(timestep, length);
		return String.format("compartment_at_time_%s", timestepString);
	}

	private static String zeroPad(int timestep, int length) {
		String zeroPadPattern = "%0" + length + "d";
		return String.format(zeroPadPattern, timestep);
	}

	private static Map<String, Map<Integer, String>> readNodeToTimestepToState(
			File nodeLevelStateFile, List<String>compartments) throws IOException {		
		return Files.readLines(
				nodeLevelStateFile,
				Charset.forName("UTF-8"),
				new StateFileLineProcessor(compartments));
	}
	
	private static class StateFileLineProcessor implements LineProcessor<Map<String, Map<Integer, String>>> {
		public static final String COMMENT_MARKER = DatToCsv.DAT_FILE_COMMENT_MARKER;
		private List<String> compartments;
		private List<String> nodeIds;
		private Map<String, Map<Integer, String>> nodeToTimestepToState;

		public StateFileLineProcessor(List<String> compartments) {
			this.compartments = compartments;
			
			this.nodeIds = Lists.newArrayList();
			this.nodeToTimestepToState = Maps.newHashMap();
		}

		public boolean processLine(String line) {
			// Read special header comment
			if (line.startsWith(HEADER_MARKER)) {
				String lineAfterTimeColumn = line.substring(HEADER_MARKER.length());				
				for (String token : lineAfterTimeColumn.trim().split(" ")) {
					if (token != null && token.trim().length() > 0) {
						nodeIds.add(token);
					}
				}				
				
				return true;
			} else if (line.startsWith(COMMENT_MARKER)) {
				// Skip other comments
				return true;
			}
			
			
			
			int timestep = readTimestepFromLine(line);
			List<String> nodeStateIds = readNodeStateIdsFromLine(line);
			
			for (int column = 0; column < nodeStateIds.size(); column++) {
				String nodeStateId = nodeStateIds.get(column);
				String nodeState = compartments.get(Integer.valueOf(nodeStateId));
				
				String nodeId = nodeIds.get(column);
				
				if (!nodeToTimestepToState.containsKey(nodeId)) {
					nodeToTimestepToState.put(nodeId, new HashMap<Integer, String>());
				}				
				nodeToTimestepToState.get(nodeId).put(timestep, nodeState);
			}
			
			return true;
		}

		private int readTimestepFromLine(String line) {
			return Integer.valueOf(line.trim().split(" ")[0].trim());
		}

		private List<String> readNodeStateIdsFromLine(String line) {
			String[] tokens = line.trim().split(" ");

			List<String> nodeStateIds = Lists.newArrayList();
			boolean onFirstToken = true;
			for (String rawToken : tokens) {
				String rawTokenTrimmed = rawToken.trim();
				
				if (rawTokenTrimmed.length() > 0 && !onFirstToken) {
					nodeStateIds.add(rawTokenTrimmed);
				}
				
				onFirstToken = false;
			}
			
			return nodeStateIds;
		}

		public Map<String, Map<Integer, String>> getResult() {
			return nodeToTimestepToState;
		}		
	}
}
