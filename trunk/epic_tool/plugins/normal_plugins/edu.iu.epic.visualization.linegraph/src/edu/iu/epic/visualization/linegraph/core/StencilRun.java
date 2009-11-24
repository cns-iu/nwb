package edu.iu.epic.visualization.linegraph.core;

import java.awt.Rectangle;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.swingworker.SwingWorker;

import stencil.adapters.java2D.Adapter;
import stencil.adapters.java2D.Panel;
import stencil.streams.Tuple;
import stencil.util.BasicTuple;
import edu.iu.epic.visualization.linegraph.utilities.DoubleStartException;
import edu.iu.epic.visualization.linegraph.utilities.ExtensionFileFilter;
import edu.iu.epic.visualization.linegraph.utilities.StencilData;
import edu.iu.epic.visualization.linegraph.utilities.StencilException;
import edu.iu.epic.visualization.linegraph.utilities.TupleStream;

/*
 * This class performs a single run of a Stencil. 
 */
public class StencilRun {

	private Panel panel;
	private boolean hasStarted = false;
	
	private StencilData data;
	private JSplitPane parent;

	public StencilRun(JSplitPane parent, StencilData data)
			throws StencilException {
		this.parent = parent;
		this.data = data;

		try {
			String stencilScript = data.getStencilScript();

			Adapter displayAdapter = Adapter.INSTANCE;
			Panel stencilPanel = displayAdapter.compile(stencilScript);
			stencilPanel.preRun();

			this.panel = stencilPanel;
		} catch (Exception e) {
			throw new StencilException(e);
		}
	}

	public void start() {
		if (this.hasStarted) {
			throw new DoubleStartException("StencilRun can only be started once.");
		}
		
		(new TupleFeeder(this.data)).execute();
		this.hasStarted = true;
	}


	public void dispose() {

	}
	
	//(Let's try not to expose the full 'Panel' class. 
	//Interaction should go through this class instead.
	public JComponent getComponent() {
		return this.panel;
	}
	
	
	private static final String DEFAULT_EXPORT_FILENAME = "graph";
	
	public void export() {
		
		try {
			
			//TODO: EPS support seems to not be working. Disabling format choosing until it's fixed
//			String[] supportedFormats = new String[]{ "PNG", "EPS" };
//			
//			String selectedFormat = (String) JOptionPane.showInputDialog(null,
//		            "Choose an export format", "Image Format",
//		            JOptionPane.INFORMATION_MESSAGE, null,
//		            supportedFormats, supportedFormats[0]);
//			
//			if (selectedFormat == null) {
//				//user canceled export
//				return;
//			}
			
		String selectedFormat = "PNG";
		
		 JFileChooser fileChooser = new JFileChooser();
		 fileChooser.setSelectedFile(
				 new File(DEFAULT_EXPORT_FILENAME + "." + selectedFormat.toLowerCase()));
		 FileFilter pngOnly = new ExtensionFileFilter(
				 "Portable Network Graphics (PNG)", new String[]{ "png" });
		 fileChooser.setFileFilter(pngOnly);
		 
		 int approvedOrCancelled = fileChooser.showSaveDialog(this.parent);
	      
	      if (approvedOrCancelled == JFileChooser.APPROVE_OPTION) {
	        String fullFileName = 
	        	fileChooser.getCurrentDirectory().toString() + "/" +
	        	fileChooser.getSelectedFile().getName();
	        
	        Object exportInfo = null;
	        
	        //TODO: Disabled until EPS works and dotsPerInch is handled right
	        if (selectedFormat.equals("PNG")) {
	        	 int dotsPerInch = 64;
	        	 exportInfo = dotsPerInch; 
	        } else if (selectedFormat.equals("EPS")) {
	        	Rectangle dimensions = new Rectangle(400,1600);
	        	exportInfo = dimensions;
	        }
	        this.panel.export(fullFileName, selectedFormat, exportInfo);
	    	 
	      }
	      if (approvedOrCancelled == JFileChooser.CANCEL_OPTION) {
	    	  return;
	      }
		
	} catch (Exception e) {
		// TODO: Perhaps a better way to do this
		throw new RuntimeException(e);
	}
}

	public void setLineVisible(String lineName, boolean visible)
		throws StencilException {
		try {
			System.out.println(lineName);
			System.out.println(visible);
			Tuple visibilityTuple = new BasicTuple("Visibility", Arrays.asList(new String[] {"Line", "Visible"}), 
					Arrays.asList(new String[] {lineName, String.valueOf(visible)}));
			this.panel.processTuple(visibilityTuple);
		} catch (Exception e) {
			throw new StencilException(e);
		}
		}


	/*
	 * TODO: The first half-second of drawing looks odd because the graph is so
	 * small. We might want to do something about it eventually.
	 */
	private void feedTuplesToStencilPanel(StencilData stencilData)
			throws StencilException {

		List<TupleStream> streams = stencilData.createStreams();
		/*
		 * TODO: Provide the option of playing the streams in parallel vs.
		 * playing them one after another?
		 */
		/*
		 * for (TupleStream stream : this.tupleStreams) {
		 * 
		 * }
		 */
		// We're assuming all of our streams are of the same size.
		// TODO: Change this?
		/*
		 * TODO: Currently if a user clicks 'replay', the old stencil will
		 * continue to draw. We should stop feeding a stencil tuples if it is no
		 * longer active.
		 */

		if (streams.size() != 0) {
			try {
				long streamSize = streams.get(0).streamSize();
				for (long ii = 0; ii < streamSize; ii++) {
					for (TupleStream stream : streams) {
						//System.err.println("tuple");
						// Thread.sleep(1);
						this.panel.processTuple(stream.nextTuple());
					}
				}
			} catch (Exception processTupleFailedException) {
				// TODO:
				processTupleFailedException.printStackTrace();
			}
		}
	}

	private class TupleFeeder extends SwingWorker<Object, Object> {

		private StencilData stencilData;

		public TupleFeeder(StencilData stencilData) {
			this.stencilData = stencilData;
		}

		public String doInBackground() {
			try {
				StencilRun.this
						.feedTuplesToStencilPanel(this.stencilData);
				return null;
			} catch (StencilException e) {
				// TODO: needs work
				throw new RuntimeException(e);
			}
		}

		protected void done() {
		}
	}
}
