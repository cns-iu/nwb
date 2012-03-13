package edu.iu.sci2.visualization.bipartitenet.algorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Dictionary;

import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.ps.PSDocumentGraphics2D;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.sci2.visualization.bipartitenet.PageDirector;
import edu.iu.sci2.visualization.bipartitenet.PageDirector.Layout;
import edu.iu.sci2.visualization.bipartitenet.component.Paintable;
import edu.iu.sci2.visualization.bipartitenet.model.BipartiteGraphDataModel;
import edu.iu.sci2.visualization.bipartitenet.model.NWBDataImporter;

public class BipartiteNetAlgorithm implements Algorithm {

	private final NWBDataImporter importer;
	private final File nwbFile;
	private final Data parentData;
	private final String leftSideType;
	private final String rightSideType;
	private final String leftSideTitle;
	private final String rightSideTitle;
	private final Layout layout;

	public BipartiteNetAlgorithm(Data parentData, File nwbFile, Layout layout, String nodeWeightColumn, String edgeWeightColumn,
			String leftSideType, String leftSideTitle, String rightSideType, String rightSideTitle, LogService log) {
		this.parentData = parentData;
		this.layout = layout;
		this.leftSideType = leftSideType;
		this.leftSideTitle = leftSideTitle;
		this.rightSideType = rightSideType;
		this.rightSideTitle = rightSideTitle;
		this.importer = new NWBDataImporter("bipartitetype",
				leftSideType, nodeWeightColumn, edgeWeightColumn, log);
		this.nwbFile = nwbFile;
	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		FileInputStream nwbStream = null;
		try {
			nwbStream = new FileInputStream(nwbFile);
			BipartiteGraphDataModel model = importer.constructModelFromFile(nwbStream);
			if (!model.hasAnyNodes()) {
				throw new AlgorithmExecutionException("Input graph has no nodes, can't make a meaningful graph.  Stopping.");
			}
			PageDirector pageDirector = new PageDirector(layout, model, leftSideType, leftSideTitle, rightSideType, rightSideTitle);
			
			Data psData = drawToPSFile(pageDirector);
			
			return new Data[] { psData }; 
		} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException("Internal error: data file disappeared?", e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException(".nwb graph file parsing problem", e);
		} finally {
			if (nwbStream != null) {
				try {
					nwbStream.close();
				} catch (IOException e) {
					throw new AlgorithmExecutionException("Couldn't close NWB file", e);
				}
			}
		}
	}

	private Data drawToPSFile(Paintable paintable) throws IOException {
		File outFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory("BipartiteGraph", "ps");
		OutputStream out = new FileOutputStream(outFile);
		PSDocumentGraphics2D g2d = new PSDocumentGraphics2D(false);
		g2d.setGraphicContext(new GraphicContext());
//		g2d.setCustomTextHandler(new NativeTextHandler(g2d, null));
		
		g2d.setupDocument(out, layout.getWidth(), layout.getHeight());
		g2d.setClip(0, 0, layout.getWidth(), layout.getHeight());
		paintable.paint(g2d);
		g2d.finish();
		out.close();
		
		Data outData = new BasicData(outFile, "file:text/ps");
		Dictionary<String, Object> metadata = (Dictionary<String, Object>)outData.getMetadata();
		metadata.put(DataProperty.LABEL, "Bipartite Network Graph PS");
		metadata.put(DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
		metadata.put(DataProperty.PARENT, parentData);
		
		return outData;
	}

//	private Data drawToPNGFile(Paintable paintable) throws IOException {
//		BufferedImage img = new BufferedImage(layout.getWidth(), layout.getHeight(), BufferedImage.TYPE_INT_RGB);
//		Graphics2D g = img.createGraphics();
//		g.setPaint(Color.white);
//		g.fillRect(0, 0, img.getWidth(), img.getHeight());
//		g.setPaint(Color.black);
//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		paintable.paint(g);
//		File outFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory("BipartiteGraph", "png");
//		ImageIO.write(img, "PNG", outFile);
//		
//		Data outData = new BasicData(outFile, "file:image/png");
//		Dictionary<String, Object> metadata = (Dictionary<String, Object>) outData.getMetadata();
//		metadata.put(DataProperty.LABEL, "Bipartite Network Graph PNG");
//		metadata.put(DataProperty.TYPE, DataProperty.RASTER_IMAGE_TYPE);
//		metadata.put(DataProperty.PARENT, parentData);
//
//		return outData;
//	}

}
