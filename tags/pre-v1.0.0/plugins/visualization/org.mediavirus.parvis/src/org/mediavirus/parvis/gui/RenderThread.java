/*

 Copyright (c) 2001, 2002, 2003 Flo Ledermann <flo@subnet.at>

 This file is part of parvis - a parallel coordiante based data visualisation
 tool written in java. You find parvis and additional information on its
 website at http://www.mediavirus.org/parvis.

 parvis is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 parvis is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with parvis (in the file LICENSE.txt); if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */

package org.mediavirus.parvis.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import org.mediavirus.parvis.model.Brush;

/**
 * 
 * @author Flo Ledermann flo@subnet.at
 * @version 0.2
 */
class RenderThread extends Thread {

	/* flags to control rendering */
	/** whether we are in quality rendering mode */
	boolean quality = false;

	/** whether we should do progressive rendering */
	boolean progressive = false;

	/* flags to indicate thread state */
	/** the thread is currently doing some work */
	boolean isWorking = false;

	/** the thread is waiting but there is work to do */
	boolean doWork = false;

	/** the last rendering loop has been interrupted */
	boolean wasInterrupted = false;

	/** the 2nd pass of progressive rendering has been interrupted */
	boolean progressiveInterrupted = false;

	/** thread is in second pass of progressive rendering */
	boolean secondPass = false;

	/** the quality settings of the thread have changed */
	boolean qualitychanged = false;

	/** start and stop axis of the region to be rendered */
	int startAxis, stopAxis;

	/** start and stop axis of the quality rendering in progressive mode */
	int progressiveStartAxis, progressiveStopAxis;

	/** the last region that was rendered successfully */
	int lastStart = 0;

	int lastStop = 0;

	/** ids of the brushed records */
	// int ids[] = null;
	/** the result image */
	BufferedImage renderedImage = null;

	/** the stroke setting for painting records */
	Stroke stroke = new BasicStroke();

	/** the color setting for painting records */
	Color color = Color.GRAY;

	/** our parents ui delegate */
	BasicParallelDisplayUI ui = null;

	/** our parent compontent */
	ParallelDisplay comp = null;

	Brush brush = null;

	/**
	 * Creates a new RenderThread for the given ui delegate.
	 * 
	 * @param ui
	 *            The BasicParallelDisplayUI this thread should render.
	 */
	RenderThread(BasicParallelDisplayUI ui) {
		this.ui = ui;
		this.setPriority(Thread.MIN_PRIORITY);
	}

	/**
	 * Helper function to set the component to get the data.
	 * 
	 * @param comp
	 *            The ParallelDisplay component this thread renders.
	 */
	void setCurrentComponent(ParallelDisplay comp) {
		this.comp = comp;
	}

	/**
	 * Toggles brush mode for this thread. If brush mode is true, this thread
	 * renders the overlay image with brushed records.
	 * 
	 * @param brushMode
	 *            A boolean indicating whether this thread renders the brush
	 *            display.
	 */
	void setBrush(Brush brush) {
		this.brush = brush;
	}

	/**
	 * Sets the render quality and progressive mode settings of this thread.
	 * 
	 * @param quality
	 *            A boolean indicating whether this thread should render
	 *            optimized for quality (true) or speed (false).
	 * @param progressive
	 *            A boolean indicating whether this thread should render in
	 *            progressive mode. If set to true, the image is rendered in two
	 *            passes: the first pass optimized for speed (preview), the
	 *            socond pass optimized for quality. If set to false, the
	 *            quality parameter determines rendering quality.
	 */
	void setQuality(boolean quality, boolean progressive) {
		this.progressive = progressive;
		if (progressive)
			this.quality = false;
		else {
			if (this.quality != quality) {
				qualitychanged = true;
				this.quality = quality;
			}
		}
	}

	/**
	 * Sets the region that this thread should render. This method invalidates
	 * the given region and tells the renderer to include it in the next
	 * rendering loop. If there is another invalidated region, the region
	 * rendered in the next pass is expanded to include all areas that are now
	 * invalidated.
	 * 
	 * @param startAxis
	 *            The start axis of the region to be rendered.
	 * @param stopAxis
	 *            The end axis of the region to be rendered.
	 */
	synchronized void setRegion(int startAxis, int stopAxis) {
		if (wasInterrupted || ((isWorking || doWork) && !secondPass)
				|| (isWorking && secondPass && doWork)) {
			// old render area still invalid -> expand
			if (startAxis < this.startAxis)
				this.startAxis = startAxis;
			if (stopAxis > this.stopAxis)
				this.stopAxis = stopAxis;
			if (startAxis < this.progressiveStartAxis)
				this.progressiveStartAxis = startAxis;
			if (stopAxis > this.progressiveStopAxis)
				this.progressiveStopAxis = stopAxis;
		} else if (progressiveInterrupted || (isWorking || doWork)) {
			this.startAxis = startAxis;
			this.stopAxis = stopAxis;
			if (startAxis < this.progressiveStartAxis)
				this.progressiveStartAxis = startAxis;
			if (stopAxis > this.progressiveStopAxis)
				this.progressiveStopAxis = stopAxis;
		} else {
			this.startAxis = startAxis;
			this.stopAxis = stopAxis;
			this.progressiveStartAxis = startAxis;
			this.progressiveStopAxis = stopAxis;
		}
		// this.ids = ids.clone();
		// System.out.println("RenderThread: setting repaint axes: " +
		// this.startAxis + ", " + this.stopAxis);
	}

	/**
	 * Sets the style how records are painted.
	 * 
	 * @param stroke
	 *            A Stroke object containing information about line style, width
	 *            etc.
	 * @param color
	 *            The Color to be used to paint the records.
	 */
	void setStyle(Stroke stroke, Color color) {
		this.stroke = stroke;
		this.color = color;
	}

	/**
	 * Indicates whether this thread is doing some work right now.
	 * 
	 * @return true if this thread is currently rendering.
	 */
	synchronized boolean isWorking() {
		return isWorking;
	}

	/**
	 * Returns the last rendered image this thread has finished rendering. Note
	 * that the image contains only those pixels between the last startAxis and
	 * stopAxis. All other pixels are transparent.
	 * 
	 * @return A BufferedImage with the rendered area.
	 */
	public BufferedImage getRenderedImage() {
		return renderedImage;
	}

	/**
	 * Returns the start axis of the rendered image.
	 * 
	 * @return The start axis of the rendered image.
	 */
	public int getRenderedRegionStart() {
		return lastStart;
	}

	/**
	 * Returns the stop axis of the rendered image.
	 * 
	 * @return The stop axis of the rendered image.
	 */
	public int getRenderedRegionStop() {
		return lastStop;
	}

	/**
	 * The main method of this thread. Runs an infinite loop, waiting for
	 * rendering data and rendering the data in one or two passes (in
	 * progressive mode). In low quality mode and the first pass of progressive
	 * mode, the thread ignores interruptions received during rendering. In
	 * second pass progressive mode rendering can be interrupted after each
	 * record, for example if new data is available or some area has been
	 * invalidated.
	 * 
	 * Do not interfere with the thread directly, use render() to signalize that
	 * there is something new to render.
	 */
	public void run() {
		while (true) {
			synchronized (this) {
				isWorking = false;
				// wait for next rendering to be queued
				do {
					try {
						if (!doWork) {
							System.out.println("RenderThread: waiting...");
							this.wait();
						}
						// System.out.println("RenderThread: exit waiting.");
					} catch (InterruptedException iex) {
						System.out
								.println("RenderThread: interruptedExcpetion.");
						// rendering has been cancelled
					}
				} while (this.interrupted());

				isWorking = true;
				doWork = false;
				renderedImage = null;
				qualitychanged = false;
			}

			// System.out.println("RenderThread: run loop start...");

			boolean progress = true;

			while (comp != null && progress) {
				String modestr;

				if (progressive && quality) {
					secondPass = true;

					// System.out.println("RenderThread: starting progressive
					// paint...");
//					modestr = "[quality]";
					modestr = "";
					
					// 2nd pass: lower priority, keep response time low
					this.yield();
				} else {
					secondPass = false;

					// System.out.println("RenderThread: starting paint...");
					modestr = "[preview]";
				}

				// this is the main rendering routine
				comp.fireProgressEvent(new ProgressEvent(comp,
						ProgressEvent.PROGRESS_START, 0.0f, "rendering "
								+ modestr));

				BufferedImage img = new BufferedImage(comp.getWidth(), comp
						.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
				Graphics2D g2 = (Graphics2D) img.getGraphics();

				// render all records
				int i = 0;
				float brushVal = 0.0f;
				Color bgcol = comp.getBackground();
				color = comp.getColorPreference("recordColor");
				if (brush != null) {
					color = brush.getColor();
					bgcol = new Color((int) (0.1 * color.getRed() + 0.9 * bgcol
							.getRed()),
							(int) (0.1 * color.getGreen() + 0.9 * bgcol
									.getGreen()),
							(int) (0.1 * color.getBlue() + 0.9 * bgcol
									.getBlue()));
				}

				setupRendering(g2, quality, stroke, color);

				for (; i < comp.getNumRecords(); i++) {
					if (i % 300 == 0) {
						comp.fireProgressEvent(new ProgressEvent(comp,
								ProgressEvent.PROGRESS_UPDATE, ((float) i)
										/ comp.getNumRecords(), "rendering "
										+ modestr));
					}
					if ((brush == null)
							|| (brushVal = brush.getBrushValue(i)) > 0.0f) {
						// select records in brushmode, render all in normal
						// mode
						// skip soft edges
						if (!quality && (brush != null) && brushVal < 0.8)
							continue;
						if ((brush != null)) {

							// New Addition
							color = brush.getColorValue(i);
							g2.setColor(color);
							
							// Generates stroke for a given line
							stroke = brush.getStrokeValue(i);
							g2.setStroke(stroke);
						}

						if (secondPass) {
							ui.drawRecord(g2, comp, i, progressiveStartAxis,
									progressiveStopAxis);
						} else {
							ui.drawRecord(g2, comp, i, startAxis, stopAxis);
						}

						if (qualitychanged || secondPass) {
							// 2nd pass: lower priority, keep response time low
							this.yield();
							if (this.interrupted()) {
								progressiveInterrupted = true;
								// System.out.println("### breaking!");
								break;
							}
						}
					}
				}
				if (i == comp.getNumRecords()) {
					// finished all records
					wasInterrupted = false;

					renderedImage = img;
					if (secondPass) {
						lastStart = progressiveStartAxis;
						lastStop = progressiveStopAxis;
						progressiveInterrupted = false;
					} else {
						lastStart = startAxis;
						lastStop = stopAxis;
					}

					comp.fireProgressEvent(new ProgressEvent(comp,
							ProgressEvent.PROGRESS_FINISH, 1.0f, "rendering "
									+ modestr));
					comp.repaint();

					// System.out.println("RenderThread: paint finished...");

					if (progressive) {
						if (quality) {
							quality = false;
							progress = false;
						} else {
							quality = true;
						}
					} else {
						progress = false;
					}
				} else {
					// we have been interrupted
					// System.out.println("RenderThread: paint interrupted...");
					progress = false;
					if (secondPass) {
						// 2nd pass progressive -> throw away
						wasInterrupted = false;
						quality = false;
					}
				}
				secondPass = false;

			}
		}
	}

	/**
	 * Starts the rendering. The region to be rendered must be set with
	 * setRegion(). If there is a high quality rendering in progress it is
	 * interrupted.
	 */
	synchronized void render() {
		// System.out.println(this.getName() + ".render() called");
		if (this.isWorking) {
			this.interrupt();
		}
		this.doWork = true;
		this.notify();
	}

	/**
	 * Throws away the result image and initializes the thread for new
	 * rendering.
	 */
	void reset() {
		// throw away image
		renderedImage = null;
	}

	/**
	 * Helper function to set up the graphics object for rendering.
	 */
	private void setupRendering(Graphics2D g2, boolean quality, Stroke stroke,
			Color color) {
		RenderingHints qualityHints = new RenderingHints(null);

		if (quality) {
			qualityHints.put(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			qualityHints.put(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);

			AlphaComposite ac = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.7f);
			g2.setComposite(ac);

			g2.setStroke(stroke);
			g2.setColor(color);
		} else {
			qualityHints.put(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
			qualityHints.put(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_SPEED);

			g2.setStroke(new BasicStroke());
			// strip out alpha
			g2.setColor(new Color(color.getRed(), color.getGreen(), color
					.getBlue()));
		}

		g2.setRenderingHints(qualityHints);

	}

}
