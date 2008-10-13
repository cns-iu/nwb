package edu.iu.nwb.converter.edgelist.nwbwritable;

public class Node implements NWBLineWritable {

	private int id;
	private String label;
	
	public Node(int id, String label) {
		this.id = id;
		if (label == null) throw new IllegalArgumentException("Label was null");
		this.label = label;
	}
	
	public String getNWBLine() {
		
		String printedLabel;
		if (label.startsWith("\"") && label.endsWith("\"")) {
			printedLabel = label;
		} else {
			printedLabel = "\"" + label + "\"";
		}
		
		return "" + id + " " + printedLabel + "\n";
	}

}
