package edu.iu.sci2.visualization.bipartitenet.algorithm;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.freehep.graphicsio.ps.PSGraphics2D;
import org.freehep.util.UserProperties;

import com.google.common.annotations.VisibleForTesting;

import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.sci2.visualization.bipartitenet.LogStream;
import edu.iu.sci2.visualization.bipartitenet.PageDirector;
import edu.iu.sci2.visualization.bipartitenet.PageDirector.Layout;
import edu.iu.sci2.visualization.bipartitenet.component.Paintable;
import edu.iu.sci2.visualization.bipartitenet.model.BipartiteGraphDataModel;
import edu.iu.sci2.visualization.bipartitenet.model.NWBDataImporter;
import edu.iu.sci2.visualization.bipartitenet.model.NodeType;

public class BipartiteNetAlgorithm implements Algorithm {

	private final NWBDataImporter importer;
	private final File nwbFile;
	private final Data parentData;
	private final Layout layout;
	private final String subtitle;
	private final NodeType leftType;
	private final NodeType rightType;

	public BipartiteNetAlgorithm(Data parentData, File nwbFile, Layout layout,
			String subtitle, String edgeWeightColumn, NodeType leftType,
			NodeType rightType) {
		this.parentData = parentData;
		this.layout = layout;
		this.subtitle = subtitle;
		this.leftType = leftType;
		this.rightType = rightType;
		this.importer = NWBDataImporter.create("bipartitetype", leftType.getName(),
				leftType.getWeightColumn(), edgeWeightColumn, leftType.getOrdering(),
				rightType.getOrdering());
		this.nwbFile = nwbFile;
	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		BufferedInputStream nwbStream = null;
		try {
			nwbStream = new BufferedInputStream(new FileInputStream(nwbFile));
			BipartiteGraphDataModel model = importer.constructModelFromFile(nwbStream);
			if (!model.hasAnyNodes()) {
				throw new AlgorithmExecutionException("Input graph has no nodes, can't make a meaningful graph.  Stopping.");
			}
			
			if (( ! layout.hasTitle())
					&& ( ! subtitle.isEmpty())) {
				LogStream.WARNING.send("A subtitle was requested, but it won't be rendered " +
						"because the chosen layout does not render titles or subtitles.");
			}
			
			PageDirector pageDirector = new PageDirector(layout, subtitle, model, leftType, rightType);
			
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
		
		UserProperties p = new UserProperties();
		// p.setProperty(PSGraphics2D.PAGE_SIZE,PageConstants.INTERNATIONAL);
		p.setProperty(PSGraphics2D.EMBED_FONTS, false);
		p.setProperty(PSGraphics2D.TEXT_AS_SHAPES, false);
		PSGraphics2D g = new PSGraphics2D(outFile,
				new Dimension(layout.getWidth(), layout.getHeight()));
		g.setProperties(p);
		g.startExport();
		g.setClip(0, 0, layout.getWidth(), layout.getHeight());
		paintable.paint(g);
		g.endExport(); // dispose, write trailer, close stream
		
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

	@VisibleForTesting
	Layout getLayout() {
		return this.layout;
	}
}
