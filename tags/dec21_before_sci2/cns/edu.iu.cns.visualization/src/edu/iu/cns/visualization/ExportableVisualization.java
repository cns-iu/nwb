package edu.iu.cns.visualization;

import java.io.File;

import edu.iu.cns.visualization.exception.VisualizationExportException;

public interface ExportableVisualization extends Visualization {
	public String[] supportedExportFormats();
	public File export(String format, String filePath) throws VisualizationExportException;
	public void export(String format, File file) throws VisualizationExportException;
}