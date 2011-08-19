package edu.iu.epic.visualization.linegraph.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.swingworker.SwingWorker;

import stencil.adapters.java2D.Adapter;
import stencil.adapters.java2D.Panel;
import stencil.tuple.SourcedTuple;
import stencil.tuple.instances.ArrayTuple;
import edu.iu.epic.visualization.linegraph.model.StencilData;
import edu.iu.epic.visualization.linegraph.model.TimestepBounds;
import edu.iu.epic.visualization.linegraph.model.tuple.TupleStream;
import edu.iu.epic.visualization.linegraph.utilities.DoubleStartException;
import edu.iu.epic.visualization.linegraph.utilities.ExtensionFileFilter;
import edu.iu.epic.visualization.linegraph.utilities.StencilException;

/*
 * This class performs a single run of a Stencil. 
 */
public class StencilRun {
	
	public static enum RUN_TYPE {
		COMPLETE, SUBSET
	};
	
	public static final Dimension ESP_DIMENSION = new Dimension(400, 1600);
	public static final int DOTS_PER_INCH = 64;
	private Panel panel;
	private boolean hasStarted = false;
	
	private Collection<StencilData> stencilData;
	private JSplitPane parent;

	public StencilRun(JSplitPane parent, String stencilScript, Collection<StencilData> stencilData)
			throws StencilException {
		this.parent = parent;
		this.stencilData = stencilData;

		try {
			
			Adapter displayAdapter = Adapter.ADAPTER;
			Panel stencilPanel = displayAdapter.compile(stencilScript);
			stencilPanel.preRun();
			this.panel = stencilPanel;
			
		} catch (Exception exception) {
			// TODO Handle this exception better?
			exception.printStackTrace();
			throw new StencilException(exception.getMessage(), exception);
		}
	}

	public void start(TimestepBounds timestepBounds) {
		if (this.hasStarted) {
			throw new DoubleStartException("StencilRun can only be started once.");
		}
		(new TupleFeeder(this.stencilData, timestepBounds)).execute();
		this.hasStarted = true;
	}

	public void dispose() { 
		this.panel.signalStop();
	}
	
	/*
	 * (Let's try not to expose the full 'Panel' class. 
	 * Interaction should go through this class instead.)
	 */
	public JComponent getComponent() {
		return this.panel;
	}
	
	private static final String DEFAULT_EXPORT_FILENAME = "graph";
	
	public void export() {
		try {
			/*
			 * TODO: EPS support seems to not be working. Disabling format choosing until
			 *  it's fixed.
			 */
			/* String[] supportedFormats = new String[]{ "PNG", "EPS" };
			
			String selectedFormat = (String) JOptionPane.showInputDialog(null,
		            "Choose an export format", "Image Format",
		            JOptionPane.INFORMATION_MESSAGE, null,
		            supportedFormats, supportedFormats[0]);
			
			if (selectedFormat == null) {
				//user canceled export
				return;
			} */
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
	        		fileChooser.getCurrentDirectory().toString() + "/"
	        		+ fileChooser.getSelectedFile().getName();

				Object exportInfo = null;

				// TODO: Disabled until EPS works and dotsPerInch is handled right.
				if (selectedFormat.equals("PNG")) {
					exportInfo = DOTS_PER_INCH; 
				} else if (selectedFormat.equals("EPS")) {
					Rectangle dimensions = new Rectangle(ESP_DIMENSION);
					exportInfo = dimensions;
				}

				this.panel.export(fullFileName, selectedFormat, exportInfo);
			}

			// TODO: This isn't necessary?
			if (approvedOrCancelled == JFileChooser.CANCEL_OPTION) {
				return;
			}
		} catch (Exception exception) {
			// TODO: Perhaps a better way to do this?
			throw new RuntimeException(exception);
		}
	}

	public void setLineVisible(String lineName, boolean visible) throws StencilException {
		try {
			SourcedTuple visibilityTuple = new SourcedTuple.Wrapper(
				"Visibility",
				ArrayTuple.from(lineName, String.valueOf(visible)));
			
			this.panel.processTuple(visibilityTuple);
			
		} catch (Exception exception) {
			// TODO Handle this exception better.
			throw new StencilException(exception);
		}
	}
	
	/*
	 * Set color for the line that match the given lineName.
	 * Throw StencilException when failed
	 */
	public void setLineColor(String lineName, Color color) throws StencilException {
		try {
			SourcedTuple colorTuple = new SourcedTuple.Wrapper(
					"LineColor",
					ArrayTuple.from(lineName, color));
			this.panel.processTuple(colorTuple);
		} catch (Exception exception) {
			// TODO Handle this exception better.
			throw new StencilException(exception);
		}
	}

	/*
	 * TODO: The first half-second of drawing looks odd because the graph is so
	 * small. We might want to do something about it eventually.
	 */
	private void feedTuplesToStencilPanel(Collection<StencilData> stencilData, 
										  TimestepBounds timestepBounds)
			throws StencilException {

		List<TupleStream> streams = new ArrayList<TupleStream>();

		for (StencilData stencilDatum : stencilData) {
			streams.addAll(stencilDatum.createStreams(timestepBounds));
		}

		/*
		 * TODO: Provide the option of playing the streams in parallel vs.
		 * playing them one after another?
		 */
		/*
		 * for (TupleStream stream : this.tupleStreams) {
		 * 
		 * }
		 */
		/*
		 * TODO: Currently if a user clicks 'replay', the old stencil will
		 * continue to draw. We should stop feeding a stencil tuples if it is no
		 * longer active.
		 */

		if (streams.size() != 0) {
			try {
				long streamSize = determineMaximumStreamSize(streams);
				
				for (long ii = 0; ii < streamSize; ii++) {
					for (TupleStream stream : streams) {
						if (!stream.hasNext()) {
							continue;
						}

						SourcedTuple t = stream.nextTuple();
						this.panel.processTuple(t);
					}
				}
			} catch (Exception processTupleFailedException) {
				// TODO: Handle this exception better.
				processTupleFailedException.printStackTrace();
			}
		}
	}

	private static long determineMaximumStreamSize(List<TupleStream> streams) {
		long maximumStreamSizeSoFar = streams.get(0).streamSize();

		for (TupleStream stream : streams) {
			maximumStreamSizeSoFar = Math.max(stream.streamSize(), maximumStreamSizeSoFar);
		}
		return maximumStreamSizeSoFar;
	}

	private class TupleFeeder extends SwingWorker<Object, Object> {
		private Collection<StencilData> stencilData;
		private TimestepBounds timestepBounds;

		public TupleFeeder(Collection<StencilData> stencilData, 
						   TimestepBounds timestepBounds) {
			this.stencilData = stencilData;
			this.timestepBounds = timestepBounds;
		}

		public String doInBackground() {
			try {
				StencilRun.this.feedTuplesToStencilPanel(this.stencilData, this.timestepBounds);

				// TODO: Should this return null or empty string?
				return null;
			} catch (StencilException stencilException) {
				// TODO Handle this exception better.
				throw new RuntimeException(stencilException);
			}
		}

		protected void done() {
		}
	}
}
