package edu.iu.cns.tool.metadata_report;

public class AlgorithmMetadata {
	public String menuLabel;
	public String servicePID;
	public String containingPluginName;

	public AlgorithmMetadata(String menuLabel, String servicePID, String containingPluginName) {
		this.menuLabel = menuLabel;
		this.servicePID = servicePID;
		this.containingPluginName = containingPluginName;
	}
}