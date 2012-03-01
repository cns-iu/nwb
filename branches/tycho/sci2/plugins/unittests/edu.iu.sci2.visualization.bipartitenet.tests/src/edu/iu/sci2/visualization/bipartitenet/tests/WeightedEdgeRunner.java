package edu.iu.sci2.visualization.bipartitenet.tests;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.ps.EPSDocumentGraphics2D;

import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.sci2.visualization.bipartitenet.PageDirector;
import edu.iu.sci2.visualization.bipartitenet.PageDirector.Layout;
import edu.iu.sci2.visualization.bipartitenet.component.CanvasContainer;
import edu.iu.sci2.visualization.bipartitenet.model.BipartiteGraphDataModel;
import edu.iu.sci2.visualization.bipartitenet.model.NWBDataImporter;

public class WeightedEdgeRunner {

	/**
	 * @param args
	 * @throws ParsingException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParsingException {
//		NWBDataImporter importer = new NWBDataImporter("bipartitetype", "Who", "Desirability");
//		BipartiteGraphDataModel model = importer.constructModelFromFile(BasicRunner.class.getResourceAsStream("test-network.nwb"));
		
		NWBDataImporter importer = new NWBDataImporter("bipartitetype", "Who", "totaldesirability", "linkdesirability");
		BipartiteGraphDataModel model = importer.constructModelFromFile(WeightedEdgeRunner.class.getResourceAsStream("node-and-edge-weighted.nwb"));
		
		PageDirector.Layout layout = PageDirector.Layout.WEB;
		
		renderOnScreen(model, layout);
		renderToPNG(model, layout);
		renderToEps(model, layout);
	}

	private static void renderToPNG(BipartiteGraphDataModel model, Layout layout) throws IOException {
		BufferedImage img = new BufferedImage(layout.getWidth(), layout.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.setPaint(Color.white);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.setPaint(Color.black);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		PageDirector r = new PageDirector(layout, model, "Who", "Who", "What", "What");
		r.paint(g);
		ImageIO.write(img, "PNG", new File("BLAH.png"));
	}
	
	private static void renderOnScreen(BipartiteGraphDataModel model, Layout layout) {
		JFrame f = new JFrame("Application Review");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CanvasContainer cc = new CanvasContainer();
		PageDirector r = new PageDirector(layout, model, "Who", "Who", "What", "What");
		cc.add(r);
		f.getContentPane().add(cc);
		cc.setPreferredSize(new Dimension(layout.getWidth(), layout.getHeight()));
		f.pack();
		f.setVisible(true);
	}

	private static void renderToEps(BipartiteGraphDataModel model, Layout layout) throws IOException {
		
 		OutputStream out = new FileOutputStream("BLAH.eps");
		EPSDocumentGraphics2D g2d = new EPSDocumentGraphics2D(false);
		g2d.setGraphicContext(new GraphicContext());
		g2d.setupDocument(out, layout.getWidth(), layout.getHeight());
		PageDirector r = new PageDirector(layout, model, "Who", "Who", "What", "What");
		r.paint(g2d);
		g2d.finish();
		out.close();
	}
}
