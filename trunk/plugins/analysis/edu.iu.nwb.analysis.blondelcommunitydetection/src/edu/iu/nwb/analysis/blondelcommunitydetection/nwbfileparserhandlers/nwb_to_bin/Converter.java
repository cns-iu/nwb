package edu.iu.nwb.analysis.blondelcommunitydetection.nwbfileparserhandlers.nwb_to_bin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Map;

import edu.iu.nwb.analysis.blondelcommunitydetection.NetworkInfo;
import edu.iu.nwb.analysis.blondelcommunitydetection.Node;
import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class Converter extends NWBFileParserAdapter {
	private static final String WINDOWS_95_PLATFORM = "Windows 95";
	private static final String WINDOWS_98_PLATFORM = "Windows 98";
	private static final String WINDOWS_NT_PLATFORM = "Windows NT";
	private static final String WINDOWS_VISTA_PLATFORM = "Windows Vista";
	private static final String WINDOWS_XP_PLATFORM = "Windows XP";
	// TODO: Verify this.
	private static final String WINDOWS_7_PLATFORM = "Windows 7";
	
	@SuppressWarnings("unused")
	private static final String[] WINDOWS_PLATFORMS = new String[] {
		WINDOWS_95_PLATFORM,
		WINDOWS_98_PLATFORM,
		WINDOWS_NT_PLATFORM,
		WINDOWS_VISTA_PLATFORM,
		WINDOWS_XP_PLATFORM,
		WINDOWS_7_PLATFORM
	};

	public static final int WEIGHT_RESOLUTION = (int) 10e6;

	private boolean shouldHaltParsing = false;
	private NetworkInfo networkInfo;
	private RandomAccessFile outputBINFile;
	private String weightAttribute;
	private boolean isWeighted;
	
	public Converter(
			NetworkInfo networkInfo, File outputFile, String weightAttribute, boolean isWeighted) {
		this.networkInfo = networkInfo;
		this.weightAttribute = weightAttribute;
		this.isWeighted = isWeighted;
		
		try {
			this.outputBINFile = new RandomAccessFile(outputFile, "rw");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		try {
			this.createBINFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	@SuppressWarnings("unchecked")
	public void addUndirectedEdge(int sourceNode, int targetNode, Map attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	public void finishedParsing() {
		try {
			this.outputBINFile.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean haltParsingNow() {
		return this.shouldHaltParsing;
	}
	
	@SuppressWarnings("unchecked")
	private void addEdge(int sourceNodeID, int targetNodeID, Map attributes) {
		int weight = fetchNormalizedWeight(attributes);
		
		if (weight < 0.0) {
			this.shouldHaltParsing = true;
		} else {
			Node sourceNode = this.networkInfo.findNodeByOriginalID(sourceNodeID);
			Node targetNode = this.networkInfo.findNodeByOriginalID(targetNodeID);
			
			try {
				this.writeEdgeAndWeightForNode(sourceNode, targetNode, weight);
				this.writeEdgeAndWeightForNode(targetNode, sourceNode, weight);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@SuppressWarnings("unchecked") // Raw Map
	private int fetchNormalizedWeight(Map attributes) {
		double weight;
		
		if (this.isWeighted) {
			weight = ((Number)attributes.get(this.weightAttribute)).doubleValue();
		} else {
			weight = 1.0;
		}

		double normalizedWeight = weight / this.networkInfo.getMaximumWeight();
		int fixedWeight = (int)(normalizedWeight * WEIGHT_RESOLUTION);

		return fixedWeight;
	}
	
	private void writeEdgeAndWeightForNode(
			Node sourceNode, Node targetNode, int weight) throws IOException {
		// Write the target node id.
		this.outputBINFile.seek(sourceNode.getWorkingEdgeOffsetInFile());
		this.writeProperInt(targetNode.getNewID());
		sourceNode.incrementWorkingEdgeOffsetInFile();
		
		// Write the target node weight.
		this.outputBINFile.seek(sourceNode.getWorkingWeightOffsetInFile());
		this.writeProperInt(weight);
		sourceNode.incrementWorkingWeightOffsetInFile();
	}
	
	private void createBINFile() throws IOException {
		long headerSize = this.writeHeader();
		this.writeEmptyBody(headerSize);
	}
	
	private long writeHeader() throws IOException {
		long headerSize = 0;
		
		ArrayList<Node> nodes = this.networkInfo.getNodes();
		int nodeCount = nodes.size();
		headerSize += this.writeProperInt(nodeCount);
		
		for (int ii = 0; ii < nodeCount; ii++) {
			Node node = (Node)nodes.get(ii);
			headerSize += this.writeProperInt(node.getEdgeCountForOutput());
		}
		
		return headerSize;
	}
	
	private void writeEmptyBody(long headerSize) throws IOException {
		long numBytesWritten = headerSize;
		
		ArrayList<Node> nodes = this.networkInfo.getNodes();
		int nodeCount = nodes.size();
		
		// Write the fake edges.
		for (int ii = 0; ii < nodeCount; ii++) {
			Node node = (Node)nodes.get(ii);
			node.setStartingEdgeOffsetInFile(numBytesWritten);
			
			for (int jj = 0; jj < node.getActualEdgeCount(); jj++) {
				numBytesWritten += this.writeProperInt(0);
			}
		}
		
		// Write the fake weights.
		for (int ii = 0; ii < nodeCount; ii++) {
			Node node = (Node)nodes.get(ii);
			node.setStartingWeightOffsetInFile(numBytesWritten);
			
			for (int jj = 0; jj < node.getActualEdgeCount(); jj++) {
				numBytesWritten += this.writeProperInt(0);
			}
		}
	}
	
	private int writeProperInt(int bigEndianInt) throws IOException {
		byte firstByte = (byte)(0xff & bigEndianInt);
		byte secondByte = (byte)(0xff & (bigEndianInt >> 8));
		byte thirdByte = (byte)(0xff & (bigEndianInt >> 16));
		byte fourthByte = (byte)(0xff & (bigEndianInt >> 24));
		
		if (platformUsesLittleEndian()) {
			this.outputBINFile.writeByte(firstByte);
			this.outputBINFile.writeByte(secondByte);
			this.outputBINFile.writeByte(thirdByte);
			this.outputBINFile.writeByte(fourthByte);
		} else {
			this.outputBINFile.writeByte(fourthByte);
			this.outputBINFile.writeByte(thirdByte);
			this.outputBINFile.writeByte(secondByte);
			this.outputBINFile.writeByte(firstByte);
		}
		
		return 4;
	}
	
	private boolean platformUsesLittleEndian() {
		if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
			return true;
		} else {
			return false;
		}
	}
}