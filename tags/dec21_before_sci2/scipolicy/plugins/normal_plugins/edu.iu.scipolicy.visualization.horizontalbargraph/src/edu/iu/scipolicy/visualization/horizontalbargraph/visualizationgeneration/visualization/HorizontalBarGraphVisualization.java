package edu.iu.scipolicy.visualization.horizontalbargraph.visualizationgeneration.visualization;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;

import org.cishell.utilities.FileUtilities;
import org.freehep.graphics2d.VectorGraphics;

import edu.iu.cns.visualization.ExportableVisualization;
import edu.iu.cns.visualization.RenderableVisualization;
import edu.iu.cns.visualization.exception.VisualizationExportException;
import edu.iu.cns.visualization.utility.GraphicsState;
import edu.iu.cns.visualization.utility.VisualizationMessages;
import edu.iu.scipolicy.visualization.horizontalbargraph.HeaderAndFooterPositioningData;
import edu.iu.scipolicy.visualization.horizontalbargraph.Metadata;
import edu.iu.scipolicy.visualization.horizontalbargraph.PageOrientation;
import edu.iu.scipolicy.visualization.horizontalbargraph.bar.Bar;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BasicLayout;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BoundingBox;

public class HorizontalBarGraphVisualization
		implements ExportableVisualization, RenderableVisualization {
	private Metadata metadata;
	private Collection<Bar> bars;
	private PageOrientation pageOrientation;
	private BasicLayout layout;
	private String postScript;

	public HorizontalBarGraphVisualization(
			Metadata metadata,
			Collection<Bar> bars,
			PageOrientation pageOrientation,
			BasicLayout layout,
			String postScript) {
		this.metadata = metadata;
		this.bars = bars;
		this.pageOrientation = pageOrientation;
		this.layout = layout;
		this.postScript = postScript;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public Collection<Bar> getBars() {
		return this.bars;
	}

	public String getPostScript() {
		return this.postScript;
	}

	public String title() {
		return "Horizontal Bar Graph";
	}

	public String[] supportedExportFormats() {
		return new String[] { "ps", "eps" };
	}

	public File export(String format, String fileName)
			throws VisualizationExportException {
		try {
			File file =
				FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(fileName, format);
			export(format, file);

			return file;
		} catch (IOException e) {
			String exceptionMessage = "Error creating temporary PostScript file.";

			throw new VisualizationExportException(exceptionMessage, e);
		}
	}

	public void export(String format, File file) throws VisualizationExportException {
		try {		
			FileWriter temporaryPostScriptFileWriter = new FileWriter(file);
			
			temporaryPostScriptFileWriter.write(getPostScript());
			temporaryPostScriptFileWriter.flush();
			temporaryPostScriptFileWriter.close();
		}
		catch (IOException postScriptFileWritingException) {
			String exceptionMessage = "Error writing PostScript out to temporary file";
			
			throw new VisualizationExportException(
				exceptionMessage, postScriptFileWritingException);
		}
	}

	public void renderBody(
			VectorGraphics graphics,
			GraphicsState graphicsState,
			VisualizationMessages messages,
			Dimension size) {
//		graphics.drawLine(0, 0, size.width, size.height);
//		graphics.drawLine(0, size.height, size.width, 0);
		BoundingBox boundingBox = this.layout.calculateBoundingBox(this.bars);

		/*graphics.rotate((pageOrientation.getRotation() * Math.PI / 180.0));

		graphicsState.saveState();

		VisualizationUtilities.postScript_FindFont(graphics, "Garamond", 8);
		VisualizationUtilities.postScript_SetGray(graphics, 0.3f);

		renderAnalysisTime(pageOrientation, boundingBox, graphics, graphicsState);

		graphicsState.restoreState();

		graphics.translate(
			pageOrientation.getCenteringTranslateX(this.bars),
			pageOrientation.getCenteringTranslateY(this.bars));*/
//		double windowWidthScale = size.getWidth() / boundingBox.getWidth();
//		double windowHeightScale = size.getHeight() / boundingBox.getHeight();
//		graphics.scale(windowWidthScale, windowHeightScale);
		double scale = this.pageOrientation.getScale();
		graphics.translate(0, size.height / scale);
		graphics.scale(scale, scale);
//		graphics.translate(
//			pageOrientation.getCenteringTranslateX(this.bars),
//			pageOrientation.getCenteringTranslateY(this.bars));

		renderBars(graphics, size);
	}

	private void renderBars(VectorGraphics graphics, Dimension size) {
		for (Bar bar : this.bars) {
			graphics.fillRect(
				bar.getX(), bar.getY(), bar.getWidth(), -bar.getHeight());
			
		}
	}

	private void renderAnalysisTime(
			PageOrientation pageOrientation,
			BoundingBox boundingBox,
			VectorGraphics graphics,
			GraphicsState graphicsState) {
		double x = HeaderAndFooterPositioningData.X_POSITION;
		double y = pageOrientation.getYTranslateForHeader(
			boundingBox, HeaderAndFooterPositioningData.DATE_TIME_Y);
		String analysisTimeText =
			"Date and time of analysis: " +
			DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(new Date());

//		graphics.scale(1.0, -1.0);
		graphics.drawString(analysisTimeText, x, y);
//		graphics.scale(1.0, -1.0);
	}
}