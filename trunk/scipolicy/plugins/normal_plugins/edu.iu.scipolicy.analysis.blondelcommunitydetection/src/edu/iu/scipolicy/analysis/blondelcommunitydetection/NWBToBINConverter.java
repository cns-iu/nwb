package edu.iu.scipolicy.analysis.blondelcommunitydetection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class NWBToBINConverter extends NWBFileParserAdapter {
	private static final String NON_POSITIVE_WEIGHT_HALT_REASON =
		"Non-positive weights are not allowed.  To use this algorithm, " +
		"preprocess your network further.";
	
	private String haltParsingReason = "";
	private boolean shouldHaltParsing = false;
	
	private RandomAccessFile outputBINFile;
	
	public NWBToBINConverter(File outputFile) {
		try {
			this.outputBINFile = new RandomAccessFile(outputFile, "rw");
		}
		catch (FileNotFoundException fileNotFoundException) {
			throw new RuntimeException(fileNotFoundException);
		}
		
		try {
			this.createBINFile();
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}
	
	public void addDirectedEdge(int sourceNode,
								int targetNode,
								Map attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	public void addUndirectedEdge(int sourceNode,
								  int targetNode,
								  Map attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	public void finishedParsing() {
	}
	
	public boolean haltParsingNow() {
		return this.shouldHaltParsing;
	}
	
	private void addEdge(int sourceNodeID, int targetNodeID, Map attributes) {
		NodeLOL sourceNode = NodeLOL.findNodeByOriginalID(sourceNodeID);
		NodeLOL targetNode = NodeLOL.findNodeByOriginalID(targetNodeID);
		
		try {
			this.writeEdgeAndWeightForNode(sourceNode, targetNode);
			this.writeEdgeAndWeightForNode(targetNode, sourceNode);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}
	
	private void writeEdgeAndWeightForNode(
			NodeLOL sourceNode, NodeLOL targetNode) throws IOException {
		// Write the target node id.
		this.outputBINFile.seek(sourceNode.getWorkingEdgeOffsetInFile());
		this.writeLittleEndianInt(targetNode.getNewID());
		System.err.println("writing target node id " + targetNode.getNewID() + " at " + sourceNode.getWorkingEdgeOffsetInFile());
		sourceNode.incrementWorkingEdgeOffsetInFile();
		
		// Write the target node weight.
		this.outputBINFile.seek(sourceNode.getWorkingWeightOffsetInFile());
		this.writeLittleEndianInt(targetNode.getWeight());
		System.err.println("writing target node weight " + targetNode.getWeight() + " at " + sourceNode.getWorkingWeightOffsetInFile());
		sourceNode.incrementWorkingWeightOffsetInFile();
	}
	
	private void createBINFile() throws IOException {
		long headerSize = this.writeHeader();
		this.writeEmptyBody(headerSize);
	}
	
	private long writeHeader() throws IOException {
		long headerSize = 0;
		
		int nodeCount = NodeLOL.nodes.size();
		headerSize += this.writeLittleEndianInt(nodeCount);
		
		for (int ii = 0; ii < nodeCount; ii++) {
			NodeLOL node = (NodeLOL)NodeLOL.nodes.get(ii);
			headerSize +=
				this.writeLittleEndianInt(node.getEdgeCountForOutput());
		}
		
		return headerSize;
	}
	
	private void writeEmptyBody(long headerSize) throws IOException {
		long numBytesWritten = headerSize;
		
		int nodeCount = NodeLOL.nodes.size();
		
		// Write the fake edges.
		for (int ii = 0; ii < nodeCount; ii++) {
			NodeLOL node = (NodeLOL)NodeLOL.nodes.get(ii);
			node.setStartingEdgeOffsetInFile(numBytesWritten);
			System.err.println("node " + ii + " starting edge offset: " + numBytesWritten);
			for (int jj = 0; jj < node.getActualEdgeCount(); jj++) {
				numBytesWritten += this.writeLittleEndianInt(0);
			}
		}
		
		// Write the fake weights.
		for (int ii = 0; ii < nodeCount; ii++) {
			NodeLOL node = (NodeLOL)NodeLOL.nodes.get(ii);
			node.setStartingWeightOffsetInFile(numBytesWritten);
			System.err.println("node " + ii + " starting weight offset: " + numBytesWritten);
			for (int jj = 0; jj < node.getActualEdgeCount(); jj++) {
				numBytesWritten += this.writeLittleEndianInt(0);
			}
		}
	}
	
	private int writeLittleEndianInt(int bigEndianInt) throws IOException {
		this.outputBINFile.writeByte((byte)(0xff & bigEndianInt));
		this.outputBINFile.writeByte((byte)(0xff & (bigEndianInt >> 8)));
		this.outputBINFile.writeByte((byte)(0xff & (bigEndianInt >> 16)));
		this.outputBINFile.writeByte((byte)(0xff & (bigEndianInt >> 24)));
		
		return 4;
	}
}