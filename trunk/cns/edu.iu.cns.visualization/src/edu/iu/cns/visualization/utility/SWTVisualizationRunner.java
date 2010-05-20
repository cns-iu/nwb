package edu.iu.cns.visualization.utility;
//package edu.iu.scipolicy.visualization.horizontalbargraph.visualizationgeneration.utility;
//
//import java.awt.Graphics2D;
//
//import org.eclipse.draw2d.Figure;
//import org.eclipse.draw2d.IFigure;
//import org.eclipse.draw2d.LightweightSystem;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Shell;
//
//import edu.iu.scipolicy.visualization.horizontalbargraph.visualizationgeneration.visualization.RenderableVisualization;
//
//public class SWTVisualizationRunner {
//	private RenderableVisualization visualization;
//	private Shell shell;
//	private Display display;
//
//	public SWTVisualizationRunner(RenderableVisualization visualization) {
//		this.visualization = visualization;
//	}
//
//	public void setUp() {
//		tearDown();
//
//		this.shell = new Shell();
//		this.shell.setSize(800, 600);
//		this.shell.setText("Test");
//
//		this.display = Display.getDefault();
//	}
//
//	public void run() {
//		final Graphics2DRenderer renderer = new Graphics2DRenderer();
//		this.shell.open();
//
//		IFigure figure = new Figure() {
//			protected void paintClientArea(org.eclipse.draw2d.Graphics graphics) {
//				renderer.prepareRendering(graphics);
//				Graphics2D graphics2D = renderer.getGraphics2D();
//			}
//		};
//
//		LightweightSystem drawSystem = new LightweightSystem(this.shell);
//		drawSystem.setContents(figure);
//
//		while (!this.shell.isDisposed()) {
//			if (!this.display.readAndDispatch()) {
//				this.display.sleep();
//			}
//		}
//
//		tearDown();
//	}
//
//	private void tearDown() {
//	}
//
//	public static void main(String[] arguments) {
//		SWTVisualizationRunner runner = new SWTVisualizationRunner(null);
//		runner.setUp();
//		runner.run();
//	}
//}