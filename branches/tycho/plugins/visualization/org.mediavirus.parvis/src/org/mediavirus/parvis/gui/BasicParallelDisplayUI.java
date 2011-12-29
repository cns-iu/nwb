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

/*
 * Modified by @author Ketan
 * 
 */

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import org.mediavirus.parvis.model.Brush;

/**
 * The UI Delegate, responsible for rendering the ParallelDisplay component.
 * 
 * @author Flo Ledermann flo@subnet.at
 * @version 0.1
 */
public class BasicParallelDisplayUI extends ParallelDisplayUI implements
		MouseListener, MouseMotionListener {

	private static final String LABEL_NEWLINE_SEPARATOR = "<BR>";

	ParallelDisplay pd;

	int numDimensions;

	int numRecords;

	int stepx;

	int hoverAxis = -1;

	int hoverRecord = -1;

	float axisScale[];

	// int borderH = 20;

	// int borderV = 40;

	// make changes to borderH and borderV to shrink the display from the border
	int borderH = 30;

	int borderV = 45;

	int width = 0, height = 0;

	String metaText = null;

	int metaX = 0, metaY = 0;

	boolean dragAxis = false;

	String selAxisLabel = "";

	int dragX = 0;

	BufferedImage bufferImg = null;

	BufferedImage brushImg = null;

	boolean needsDeepRepaint = true;

	boolean renderQuality = false;

	int brushHoverStart = 0;

	int brushHoverEnd = 0;

	int brushHoverX = 0;

	boolean inBrush = false;

	Brush tempBrush = null;

	Brush dragBrush = null;

	Brush newBrush = null;

	int redStep = 0;

	int greenStep = 0;

	int blueStep = 0;

	HashMap recColorHash = new HashMap();

	HashMap recStrokeHash = new HashMap();

	int strokeCtr = 0;

	/**
	 * Default Constructor. Creates a new BasicParallelDisplayUI.
	 */
	public BasicParallelDisplayUI() {
	}

	/**
	 * Swing method. Returns a new instance.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new BasicParallelDisplayUI();
	}

	/**
	 * Installs this instance as UI delegate for the given component.
	 * 
	 * @param c
	 *            The component, a ParallelDisplay in our case.
	 */
	public void installUI(JComponent c) {
		ParallelDisplay pd = (ParallelDisplay) c;
		pd.addMouseListener(this);
		pd.addMouseMotionListener(this);

	}

	/**
	 * Uninstalls this instance from its component.
	 * 
	 * @param c
	 *            The component, a ParallelDisplay in our case.
	 */
	public void uninstallUI(JComponent c) {
		pd = (ParallelDisplay) c;

		pd.removeMouseListener(this);
		pd.removeMouseMotionListener(this);

		numDimensions = 0;
		numRecords = 0;
	}

	/** RenderThread instance to render the dataset. */
	RenderThread renderThread = null;

	/** RenderThread instance to render the brushed records. */
	RenderThread brushThread = null;

	/**
	 * Renders the component on the screen.
	 * 
	 * @param g
	 *            The graphics object to draw on.
	 * @param c
	 *            The Component, our ParallelDisplay.
	 */
	public void paint(Graphics g, JComponent c) {

		ParallelDisplay comp = (ParallelDisplay) c;

		// start our renderThread
		if (renderThread == null) {
			renderThread = new RenderThread(this);
			renderThread.setQuality(false, true);
			renderThread.setStyle(new BasicStroke(0.5f), comp
					.getColorPreference("recordColor"));
			renderThread.start();
		}

		if (brushThread == null) {
			brushThread = new RenderThread(this);
			brushThread.setQuality(false, true);
			brushThread.setStyle(new BasicStroke(1.5f), comp
					.getColorPreference("brushColor"));
			brushThread.start();
		}

		// set up the environment
		Graphics2D g2 = (Graphics2D) g;

		RenderingHints qualityHints = new RenderingHints(null);

		qualityHints.put(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		qualityHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g2.setRenderingHints(qualityHints);

		// workaround flag for model change, resize,...
		if (comp.deepRepaint) {
			// throw away buffered image -> complete repaint

			width = c.getWidth() - 2 * borderH;
			height = c.getHeight() - 2 * borderV;

			numDimensions = comp.getNumAxes();
			numRecords = comp.getNumRecords();

			stepx = width / (numDimensions - 1);

			System.out.println("Setting numDimensions to " + numDimensions);
			needsDeepRepaint = true;

			bufferImg = new BufferedImage(c.getWidth(), c.getHeight(),
					BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D ig = bufferImg.createGraphics();
			ig.setColor(c.getBackground());
			ig.fillRect(0, 0, c.getWidth(), c.getHeight());
			// brushImg = null;

			renderThread.reset();
			brushThread.reset();
			renderThread.setCurrentComponent(comp);
			brushThread.setCurrentComponent(comp);

			brushThread.setBrush(comp.getCurrentBrush());
			if (comp.getCurrentBrush() == null) {
				brushImg = null;
			} else {
				renderBrush();
			}
			renderAll();

			comp.deepRepaint = false;
		} else if (comp.brushChanged) {
			if (!tempBrushSwap) {
				brushImg = null;
				brushThread.reset();

				brushThread.setBrush(comp.getCurrentBrush());
				renderBrush();
			} else {
				// we just came from interactive brushing -> no need for
				// re-rendering
				tempBrushSwap = false;
			}

			comp.brushChanged = false;
		}

		g2.setColor(c.getBackground());
		g2.fillRect(0, 0, comp.getWidth(), comp.getHeight());
		g2.translate(borderH, borderV);

		// save rendered image in new buffer
		if (renderThread.getRenderedImage() != null) {
			// we cant do this becase the renderedImage is only a part of the
			// whole
			// bufferImg = (BufferedImage)renderThread.getRenderedImage();
			Graphics2D ig = bufferImg.createGraphics();
			ig.setColor(comp.getBackground());
			int startAxis = renderThread.getRenderedRegionStart();
			int stopAxis = renderThread.getRenderedRegionStop();

			// delete area that has been rendered
			ig.fillRect(startAxis * stepx, 0, (stopAxis - startAxis) * stepx,
					comp.getHeight());
			// and paint it new
			ig.drawImage(renderThread.getRenderedImage(), 0, 0, comp);
		}

		if (brushThread.getRenderedImage() != null) {
			brushImg = brushThread.getRenderedImage();
		}

		if ((comp.getCurrentBrush() == null) && (brushImg != null)
				&& (!inBrush)) {
			brushImg = null;
		}

		if ((comp.getCurrentBrush() == null) && (!inBrush)) {
			synchronized (bufferImg) {
				g2.drawImage(bufferImg, 0, 0, comp);
			}
		} else {
			Composite oldcomp = g2.getComposite();

			AlphaComposite ac = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.15f);
			g2.setComposite(ac);

			g2.drawImage(bufferImg, 0, 0, comp);

			g2.setComposite(oldcomp);
			if (brushImg != null) {
				g2.drawImage(brushImg, 0, 0, comp);
			}
		}

		// set up
		g2.setColor(comp.getForeground());
		g2.setStroke(new BasicStroke(1.0f));

		// draw all the dynamic parts on the screen:
		// axis labels
		for (int i = 0; i < numDimensions; i++) {
			float curx = i * stepx;

			// hovering over Axis
			if (i == hoverAxis) {
				g2.setStroke(new BasicStroke(1.5f));
				g2.draw(new Line2D.Float(curx, 0, curx, height));
				g2.setStroke(new BasicStroke(1.0f));

			} else {
				// To set the color of axis to darkgray instead of black
				g2.setColor(Color.DARK_GRAY);
				g2.draw(new Line2D.Float(curx, 0, curx, height));
				g2.setColor(Color.BLACK);
			}

			String label = comp.getAxisLabel(i);
			if (label != null) {

				// Color code the selected axis label in blue
				if (label.equals(getSelAxisLabel())) {
					Font oldfont = g2.getFont();
					Color oldColor = g2.getColor();

					g2.setFont(new Font("Goudy Handtooled BT", Font.BOLD, 12));
					g2.setColor(Color.BLUE);
					float curLabelX = curx - 10;
					float curLabelY = height + 30;
					drawMultiLineAxisLabel(g2, curLabelX, curLabelY, label);

					g2.setFont(oldfont);
					g2.setColor(oldColor);

				} else {

					g2.setColor(g2.getColor());
					float curLabelX = curx - 10;
					float curLabelY = height + 30;
					g2.setColor(g2.getColor());
					drawMultiLineAxisLabel(g2, curLabelX, curLabelY, label);

				}
			}

			// Code to assign x-y locn to axis vertical labels
			float axisX = curx + 2;
			float axisYL = borderV / 2 - 22;
			float axisYH = height + borderV / 2 - 5;
			if (comp.getBoolPreference("axis-labels")) {
				boolean isNominal = comp.getAxisIsNominal(i);
				if (isNominal) {
					String[] axisNominalLabels = comp.getAxisNominalLabels(i);
					// int labelHeight = (int) (axisYH - axisYL)
					// / axisNominalLabels.length;
					Font originalFont = g2.getFont();
					Font rotatedFont = originalFont;

					// Font rotatedFont = originalFont.deriveFont(0.8f);
					rotatedFont = rotatedFont.deriveFont(AffineTransform
							.getRotateInstance(-Math.PI * 90.0 / 180));

					// Change the size of the rotated text
					g2.setColor(Color.RED);
					g2.setFont(rotatedFont);

					// label markers
					float[] relMonthsVal = { -1, 0, 18, 36 };
					float[] relSite = { 0, 1, 2, 3, 4 };
					float[] deceased = { 1, 2 };
					float[] immuno = { 1, 2 };
					float[] genCat = { 0, 1, 2, 3, 4 };
					float[] cns = { 0, 1, 2, 3, 4 };
					float[] sex = { 1, 2 };
					float[] race = { 0, 1, 2, 3, 4, 5 };
					float[] bm7 = { 0, 5, 25 };
					float[] bm14 = { -1, 0, 5, 25 };
					float[] famInc = { 0, 1, 2, 3, 4, 5 };
					float[] edu = { 0, 1, 2, 3, 4 };
					int labelSpaceCtr = 0;
					for (int labelCount = 0; labelCount < axisNominalLabels.length; ++labelCount) {
						if (comp.getAxisLabel(i).equals("Relapse<BR>(Months)")) {
							// float yValue = getYValueAlongAxis(-1, i, comp);
							float yValue = getYValueAlongAxis(
									relMonthsVal[labelCount], i, comp);
							g2.setColor(Color.RED);
							g2.draw(new Line2D.Double(axisX - 6, yValue, axisX,
									yValue));

							// float labelY = axisYH - labelCount * labelHeight;
							// g2.drawString(axisNominalLabels[labelCount],
							// axisX - 10, labelY);
							if (labelSpaceCtr == 20) {
								labelSpaceCtr = 0;
							}
							// g2.drawString(axisNominalLabels[labelCount],
							// axisX
							// - 10, yValue);
							g2.drawString(axisNominalLabels[labelCount], axisX
									- 5 - labelSpaceCtr, yValue);
							labelSpaceCtr = labelSpaceCtr + 10;

						} else if (comp.getAxisLabel(i).equals(
								"Relapse<BR>Site")) {
							float yValue = getYValueAlongAxis(
									relSite[labelCount], i, comp);
							g2.setColor(Color.RED);
							g2.draw(new Line2D.Double(axisX - 6, yValue, axisX,
									yValue));
							g2.drawString(axisNominalLabels[labelCount], axisX
									- 5 - labelSpaceCtr, yValue);
						} else if (comp.getAxisLabel(i).equals("Deceased")) {
							float yValue = getYValueAlongAxis(
									deceased[labelCount], i, comp);
							g2.setColor(Color.RED);
							g2.draw(new Line2D.Double(axisX - 6, yValue, axisX,
									yValue));
							g2.drawString(axisNominalLabels[labelCount], axisX
									- 5 - labelSpaceCtr, yValue);
						} else if (comp.getAxisLabel(i).equals("Immuno")) {
							float yValue = getYValueAlongAxis(
									immuno[labelCount], i, comp);
							g2.setColor(Color.RED);
							g2.draw(new Line2D.Double(axisX - 6, yValue, axisX,
									yValue));
							g2.drawString(axisNominalLabels[labelCount], axisX
									- 5 - labelSpaceCtr, yValue);
						} else if (comp.getAxisLabel(i).equals(
								"Genetic<BR>Category")) {
							float yValue = getYValueAlongAxis(
									genCat[labelCount], i, comp);
							g2.setColor(Color.RED);
							g2.draw(new Line2D.Double(axisX - 6, yValue, axisX,
									yValue));
							g2.drawString(axisNominalLabels[labelCount], axisX
									- 5 - labelSpaceCtr, yValue);
						} else if (comp.getAxisLabel(i).equals("CNS")) {
							float yValue = getYValueAlongAxis(cns[labelCount],
									i, comp);
							g2.setColor(Color.RED);
							g2.draw(new Line2D.Double(axisX - 6, yValue, axisX,
									yValue));
							g2.drawString(axisNominalLabels[labelCount], axisX
									- 5 - labelSpaceCtr, yValue);
						} else if (comp.getAxisLabel(i).equals("Sex")) {
							float yValue = getYValueAlongAxis(sex[labelCount],
									i, comp);
							g2.setColor(Color.RED);
							g2.draw(new Line2D.Double(axisX - 6, yValue, axisX,
									yValue));
							g2.drawString(axisNominalLabels[labelCount], axisX
									- 5 - labelSpaceCtr, yValue);
						} else if (comp.getAxisLabel(i).equals("Race")) {
							float yValue = getYValueAlongAxis(race[labelCount],
									i, comp);
							g2.setColor(Color.RED);
							g2.draw(new Line2D.Double(axisX - 6, yValue, axisX,
									yValue));
							g2.drawString(axisNominalLabels[labelCount], axisX
									- 5 - labelSpaceCtr, yValue);
						} else if (comp.getAxisLabel(i).equals("BM7")) {
							float yValue = getYValueAlongAxis(bm7[labelCount],
									i, comp);
							g2.setColor(Color.RED);
							g2.draw(new Line2D.Double(axisX - 6, yValue, axisX,
									yValue));
							g2.drawString(axisNominalLabels[labelCount], axisX
									- 5 - labelSpaceCtr, yValue);
						} else if (comp.getAxisLabel(i).equals("BM14")) {
							float yValue = getYValueAlongAxis(bm14[labelCount],
									i, comp);
							g2.setColor(Color.RED);
							g2.draw(new Line2D.Double(axisX - 6, yValue, axisX,
									yValue));
							g2.drawString(axisNominalLabels[labelCount], axisX
									- 5 - labelSpaceCtr, yValue);
						} else if (comp.getAxisLabel(i).equals(
								"Family<BR>Income")) {
							float yValue = getYValueAlongAxis(
									famInc[labelCount], i, comp);
							g2.setColor(Color.RED);
							g2.draw(new Line2D.Double(axisX - 6, yValue, axisX,
									yValue));
							g2.drawString(axisNominalLabels[labelCount], axisX
									- 5 - labelSpaceCtr, yValue);
						} else if (comp.getAxisLabel(i).equals("Education")) {
							float yValue = getYValueAlongAxis(edu[labelCount],
									i, comp);
							g2.setColor(Color.RED);
							g2.draw(new Line2D.Double(axisX - 6, yValue, axisX,
									yValue));
							g2.drawString(axisNominalLabels[labelCount], axisX
									- 5 - labelSpaceCtr, yValue);
						}
						// else {
						// NOTE: Need to introduce a condition where
						// // to isolate different axis and label conditions
						//
						// float labelY = axisYH - labelCount * labelHeight;
						// g2.drawString(axisNominalLabels[labelCount],
						// axisX - 10, labelY);
						// g2.setColor(Color.RED);
						// g2.draw(new Line2D.Double(axisX - 6, labelY, axisX,
						// labelY));
						// g2.drawString(axisNominalLabels[labelCount],
						// axisX - 10, labelY);
						// }
					}
					g2.setFont(originalFont);
					g2.setColor(Color.BLACK);
				} else {
					// To draw markings spots on the integer axis
					if (comp.getAxisLabel(i).equals("AgeDx")) {
						float yValue = getYValueAlongAxis(10, i, comp);
						g2.setColor(Color.RED);
						g2.fill(new Ellipse2D.Double(stepx * i - 2.5,
								yValue - 2.5, 5, 5));
						g2.drawString("10", stepx * i + 3, yValue);
						g2.setColor(Color.BLACK);

					} else if (comp.getAxisLabel(i).equals("log(WBC)")) {
						float yValue = getYValueAlongAxis(2.31f, i, comp);
						g2.setColor(Color.RED);
						g2.fill(new Ellipse2D.Double(stepx * i - 2.5,
								yValue - 2.5, 5, 5));
						g2.drawString("10", stepx * i + 3, yValue);

						yValue = getYValueAlongAxis(4.61f, i, comp);
						g2.fill(new Ellipse2D.Double(stepx * i - 2.5,
								yValue - 2.5, 5, 5));
						g2.drawString("100", stepx * i + 3, yValue);
						g2.setColor(Color.BLACK);

					} else if (comp.getAxisLabel(i).equals("Hgb")) {
						float yValue = getYValueAlongAxis(10, i, comp);
						g2.setColor(Color.RED);
						g2.fill(new Ellipse2D.Double(stepx * i - 2.5,
								yValue - 2.5, 5, 5));
						g2.drawString("10", stepx * i + 3, yValue);
						g2.setColor(Color.BLACK);

					} else if (comp.getAxisLabel(i).equals("Platelets")) {
						float yValue = getYValueAlongAxis(100, i, comp);
						g2.setColor(Color.RED);
						g2.fill(new Ellipse2D.Double(stepx * i - 2.5,
								yValue - 2.5, 5, 5));
						g2.drawString("100", stepx * i + 3, yValue);
						g2.setColor(Color.BLACK);

					} else if (comp.getAxisLabel(i).equals("Emplo<BR>yment")) {
						float yValue = getYValueAlongAxis(70, i, comp);
						g2.setColor(Color.RED);
						g2.fill(new Ellipse2D.Double(stepx * i - 2.5,
								yValue - 2.5, 5, 5));
						g2.drawString("70", stepx * i + 3, yValue);
						g2.setColor(Color.BLACK);
					} else if (comp.getAxisLabel(i)
							.equals("Single<BR>Families")) {
						float yValue = getYValueAlongAxis(10, i, comp);
						g2.setColor(Color.RED);
						g2.fill(new Ellipse2D.Double(stepx * i - 2.5,
								yValue - 2.5, 5, 5));
						g2.drawString("10", stepx * i + 3, yValue);
						g2.setColor(Color.BLACK);
					}
				}
			}

			// To display the top and bottom axis labels
			boolean isNominal = comp.getAxisIsNominal(i);
			if (!isNominal) {
				String axisLabel1 = "" + comp.getAxisOffset(i);
				g2.drawString(axisLabel1, axisX, axisYL);

				String axisLabel2 = ""
						+ (comp.getAxisOffset(i) + comp.getAxisScale(i));
				if (axisLabel2.length() > 4) {
					axisLabel2 = axisLabel2.substring(0, 4);
				}
				g2.drawString(axisLabel2, axisX, axisYH);
				// drawArrow(g2, (int) curx, -20, 8, false,
				// (comp.getAxisScale(i) < 0));
			}
		}

		// brush Hover
		if (inBrush) {
			g2.setColor(new Color(0.7f, 0.0f, 0.0f));
			g2.setStroke(new BasicStroke(2.5f));
			g2.draw(new Line2D.Float(brushHoverX, brushHoverStart, brushHoverX,
					brushHoverEnd));
		}

		// angular brushing
		if (inAngularBrush) {

			if (angularPhase1) {

				int startx, endx, starty, endy;

				if (angularCurX != angularRefX) { // avoid div. by zero
					startx = angularRegion * stepx;
					endx = (angularRegion + 1) * stepx;
					starty = (int) (angularRefY - (angularRefX - (float) startx)
							/ ((float) (angularCurX - angularRefX))
							* (angularCurY - (float) angularRefY));
					endy = (int) (angularCurY + (endx - (float) angularCurX)
							/ ((float) (angularCurX - angularRefX))
							* (angularCurY - (float) angularRefY));
				} else {
					startx = angularCurX;
					endx = angularCurX;
					starty = 0 - borderV;
					endy = height + borderV;
				}
				g2.setColor(comp.getForeground());
				g2.setStroke(new BasicStroke(1.0f));
				g2.drawLine(startx, starty, endx, endy);

				g2.drawRect(angularRefX - 2, angularRefY - 2, 4, 4);
				g2.drawRect(angularCurX - 2, angularCurY - 2, 4, 4);
			} else {

				int startx, endx, starty, endy;
				if (angularStartX != angularRefX) { // avoid div. by zero
					startx = angularRegion * stepx;
					endx = (angularRegion + 1) * stepx;
					starty = (int) (angularRefY - (angularRefX - (float) startx)
							/ ((float) (angularStartX - angularRefX))
							* (angularStartY - (float) angularRefY));
					endy = (int) (angularStartY + (endx - (float) angularStartX)
							/ ((float) (angularStartX - angularRefX))
							* (angularStartY - (float) angularRefY));
				} else {
					startx = angularStartX;
					endx = angularStartX;
					starty = 0 - borderV;
					endy = height + borderV;
				}

				g2.setColor(comp.getForeground());
				g2.setStroke(new BasicStroke(1.0f));
				g2.drawLine(startx, starty, endx, endy);

				if (angularCurX != angularRefX) { // avoid div. by zero
					startx = angularRegion * stepx;
					endx = (angularRegion + 1) * stepx;
					starty = (int) (angularRefY - (angularRefX - (float) startx)
							/ ((float) (angularCurX - angularRefX))
							* (angularCurY - (float) angularRefY));
					endy = (int) (angularCurY + (endx - (float) angularCurX)
							/ ((float) (angularCurX - angularRefX))
							* (angularCurY - (float) angularRefY));
				} else {
					startx = angularCurX;
					endx = angularCurX;
					starty = 0 - borderV;
					endy = height + borderV;
				}

				g2.drawLine(startx, starty, endx, endy);

				g2.drawRect(angularRefX - 2, angularRefY - 2, 4, 4);
				g2.drawRect(angularStartX - 2, angularStartY - 2, 4, 4);
				g2.drawRect(angularCurX - 2, angularCurY - 2, 4, 4);

				Color tc = comp.getForeground();

				g2.setColor(new Color(tc.getRed(), tc.getGreen(), tc.getBlue(),
						50));

				g2
						.fillArc(
								angularRefX - 50,
								angularRefY - 50,
								100,
								100,
								(int) (angularAngle1 * 180.0f / Math.PI),
								(int) ((angularAngle2 - angularAngle1) * 180.0f / Math.PI));
			}
		}

		if (comp.getBoolPreference("zones")) {

			for (int i = 0; i < numDimensions; i++) {
				float curx = i * stepx;
				curx -= stepx / 4;

				// triangle zone for wbc axis
				if (comp.getAxisLabel(i).equals("log(WBC)")) {

					g2.setColor(new Color(1.0f, 1.0f, 1.0f, 0.8f));
					g2.fillRect((int) curx, 0, stepx / 2, height);
					g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.4f));
					g2.drawRect((int) curx, 0, stepx / 2, height);
					g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.25f));

					curx = i * stepx;
					float axisX = curx + 2;
					int xPoints[] = { (int) (axisX - 14.0), (int) (axisX - 2),
							(int) (axisX + 11.0) };

					int yPoints[] = { (int) getYValueAlongAxis(5.0f, i, comp),
							(int) getYValueAlongAxis(0.0f, i, comp),
							(int) getYValueAlongAxis(5.0f, i, comp) };

					g2.fill(new Polygon(xPoints, yPoints, 3));

				} else if (comp.getAxisLabel(i).equals("AgeDx")) {

					g2.setColor(new Color(1.0f, 1.0f, 1.0f, 0.8f));
					g2.fillRect((int) curx, 0, stepx / 2, height);
					g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.4f));
					g2.drawRect((int) curx, 0, stepx / 2, height);
					g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.25f));

					curx = i * stepx;
					float axisX = curx + 2;
					int xPoints[] = { (int) (axisX - 14.0), (int) (axisX - 2),
							(int) (axisX + 11.0) };

					int yPoints[] = { (int) getYValueAlongAxis(15.0f, i, comp),
							(int) getYValueAlongAxis(0.0f, i, comp),
							(int) getYValueAlongAxis(15.0f, i, comp) };

					g2.fill(new Polygon(xPoints, yPoints, 3));

				} else if (comp.getAxisLabel(i).equals("Platelets")) {

					g2.setColor(new Color(1.0f, 1.0f, 1.0f, 0.8f));
					g2.fillRect((int) curx, 0, stepx / 2, height);
					g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.4f));
					g2.drawRect((int) curx, 0, stepx / 2, height);
					g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.25f));

					curx = i * stepx;
					float axisX = curx + 2;
					int xPoints[] = { (int) (axisX - 14.0), (int) (axisX - 2),
							(int) (axisX + 11.0) };

					int yPoints[] = { (int) getYValueAlongAxis(0.0f, i, comp),
							(int) getYValueAlongAxis(125.0f, i, comp),
							(int) getYValueAlongAxis(0.0f, i, comp) };

					g2.fill(new Polygon(xPoints, yPoints, 3));

				} else if (comp.getAxisLabel(i).equals("Hgb")) {

					g2.setColor(new Color(1.0f, 1.0f, 1.0f, 0.8f));
					g2.fillRect((int) curx, 0, stepx / 2, height);
					g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.4f));
					g2.drawRect((int) curx, 0, stepx / 2, height);
					g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.25f));

					curx = i * stepx;
					float axisX = curx + 2;
					int xPoints[] = { (int) (axisX - 14.0), (int) (axisX - 2),
							(int) (axisX + 11.0) };

					int yPoints[] = { (int) getYValueAlongAxis(13.0f, i, comp),
							(int) getYValueAlongAxis(0.0f, i, comp),
							(int) getYValueAlongAxis(13.0f, i, comp) };

					g2.fill(new Polygon(xPoints, yPoints, 3));

				} else if (comp.getAxisLabel(i).equals("BM14")) {

					g2.setColor(new Color(1.0f, 1.0f, 1.0f, 0.8f));
					g2.fillRect((int) curx, 0, stepx / 2, height);
					g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.4f));
					g2.drawRect((int) curx, 0, stepx / 2, height);

					curx = i * stepx;
					float axisX = curx + 2;
					g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.25f));

					// get max value along the axis
					float maxAxisVal = comp.getModel().getMaxValue(i);

					// box for infinite < X < 25
					g2.fillRect((int) axisX, (int) getYValueAlongAxis(
							maxAxisVal, i, comp), stepx / 2,
							(int) getYValueAlongAxis(25.0f, i, comp));

					// box for 25 < X < 5
					if (maxAxisVal < 25.0f) {
						g2.fillRect((int) axisX - 8, (int) getYValueAlongAxis(
								maxAxisVal, i, comp), stepx / 4,
								(int) getYValueAlongAxis(5.0f, i, comp)
										- (int) getYValueAlongAxis(maxAxisVal,
												i, comp));
					} else {
						g2.fillRect((int) axisX - 8, (int) getYValueAlongAxis(
								25.0f, i, comp), stepx / 4,
								(int) getYValueAlongAxis(5.0f, i, comp)
										- (int) getYValueAlongAxis(25.0f, i,
												comp));
					}

					// box for infinite 5 < X < -1
					g2.fillRect((int) axisX - 6, (int) getYValueAlongAxis(5.0f,
							i, comp), stepx / 6, (int) getYValueAlongAxis(
							-1.0f, i, comp)
							- (int) getYValueAlongAxis(5.0f, i, comp));
				}
			}
		}

		// axis histograms
		if (comp.getBoolPreference("histogram")) {

			int bins = comp.getIntPreference("histogramBins");
			float stepy = (float) height / bins;

			g2.setStroke(new BasicStroke(1.0f));

			for (int i = 0; i < numDimensions; i++) {
				float curx = i * stepx;

				// if (i > 0)
				curx -= stepx / 4;
				// if (i == (numDimensions-1)) curx -= stepx/4;

				g2.setColor(new Color(1.0f, 1.0f, 1.0f, 0.8f));
				g2.fillRect((int) curx, 0, stepx / 2, height);
				g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.4f));
				g2.drawRect((int) curx, 0, stepx / 2, height);

				float xscale = comp.getAxisScale(i);
				float xoffset = comp.getAxisOffset(i);

				// include records right at the border
				xoffset -= xscale * 0.00005f;
				xscale += xscale * 0.0001f;

				int j;
				int baseWidth = 0;

				if (comp.getIntPreference("histogramWidth") != ParallelDisplay.HISTO_TOTALREC) {
					if (comp.getIntPreference("histogramWidth") == ParallelDisplay.HISTO_BINREC) {
						for (j = 0; j < bins; j++) {
							float upper = xoffset + (j * stepy / height)
									* xscale;
							float lower = xoffset + ((j + 1) * stepy / height)
									* xscale;
							int count = comp.getNumRecordsInRange(i, Math.min(
									lower, upper), Math.max(lower, upper));

							if (count > baseWidth)
								baseWidth = count;
						}
					} else if (comp.getIntPreference("histogramWidth") == ParallelDisplay.HISTO_BRUSHREC) {
						for (j = 0; j < bins; j++) {
							float upper = xoffset + (j * stepy / height)
									* xscale;
							float lower = xoffset + ((j + 1) * stepy / height)
									* xscale;
							int count = comp.getNumBrushedInRange(i, Math.min(
									lower, upper), Math.max(lower, upper));

							if (count > baseWidth)
								baseWidth = count;
						}
					} else { // unknown preference
						baseWidth = comp.getNumRecords();
					}
				} else {
					baseWidth = comp.getNumRecords();
				}

				for (j = 0; j < bins; j++) {

					float upper = xoffset + (j * stepy / height) * xscale;
					float lower = xoffset + ((j + 1) * stepy / height) * xscale;
					float count = comp.getNumRecordsInRange(i, Math.min(lower,
							upper), Math.max(lower, upper));
					float hwidth = ((count / baseWidth) * stepx / 2);

					if (hwidth > stepx / 2) {
						hwidth = stepx / 2;
					}

					g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.4f));
					g2.fillRect((int) (curx + stepx / 4 - hwidth / 2),
							(int) (j * stepy), (int) hwidth, (int) stepy + 1);

					count = comp.getNumBrushedInRange(i,
							Math.min(lower, upper), Math.max(lower, upper));
					hwidth = ((count / baseWidth) * stepx / 2);

					g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.6f));
					g2.fillRect((int) (curx + stepx / 4 - hwidth / 2),
							(int) (j * stepy), (int) hwidth, (int) stepy + 1);
				}
			}
		}

		// hovering over record
		if ((comp.getBoolPreference("hoverLine")) && (hoverRecord != -1)) {
			// System.out.println("Painting record " + i);
			// Color col = new Color(1.0f, 1.0f, 0.8f);
			Font oldfont = g2.getFont();
			Font newfont = new Font(oldfont.getName(), Font.PLAIN, oldfont
					.getSize() - 2);
			g2.setFont(newfont);

			GeneralPath rPath = new GeneralPath();
			float yval = getYValue(hoverRecord, 0, comp);
			rPath.moveTo(0, yval);

			for (int j = 1; j < numDimensions; j++) {
				yval = getYValue(hoverRecord, j, comp);
				rPath.lineTo(stepx * j, yval);
			}

			g2.setStroke(new BasicStroke(2.5f));
			g2.draw(rPath);

			g2.setStroke(new BasicStroke(1.5f));
			g2.setColor(Color.red);
			g2.draw(rPath);

			g2.setFont(oldfont);
		}

		if ((comp.getBoolPreference("hoverText")) && (hoverRecord != -1)) {
			Color col = new Color(1.0f, 1.0f, 0.8f);
			for (int j = 0; j < numDimensions; j++) {

				String toolTipLabel = comp.getAxisLabel(j).replaceAll("<BR>",
						"");
				toolTipLabel.replaceAll("-y", "y");
				float yval = getYValue(hoverRecord, j, comp);
				// drawTooltip(g2, comp.getAxisLabel(j) + "=\n"
				// + comp.getValue(hoverRecord, j), stepx * j, (int) yval,
				// col);

				// Condition over here to show the actual category values
				// for the data
				boolean isNominal = comp.getAxisIsNominal(j);
				String nominalAxisDataVal = null;
				if (isNominal) {
					nominalAxisDataVal = getNomialAxisDataValues(toolTipLabel,
							comp.getValue(hoverRecord, j));
					drawTooltip(g2, toolTipLabel + "=\n" + nominalAxisDataVal,
							stepx * j, (int) yval, col);
				} else {
					drawTooltip(g2, toolTipLabel + "=\n"
							+ comp.getValue(hoverRecord, j), stepx * j,
							(int) yval, col);

				}
			}
		}

		// dragging axis
		if (dragAxis) {

			AlphaComposite ac = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.7f);
			g2.setComposite(ac);

			g2.setStroke(new BasicStroke(0.5f));
			g2.drawLine(dragX - borderH, 0, dragX - borderH, height);
		}

		// tooltips
		if ((comp.getBoolPreference("hoverLine")) && (metaText != null)) {
			drawTooltip(g2, metaText, metaX, metaY + 10, new Color(0.7f, 0.7f,
					1.0f));
		}

		g2.translate(-borderH, -borderV);
	}

	// Tooltip information obtained based on the values
	private String getNomialAxisDataValues(String toolTipLabel, float value) {

		String str = null;
		if (toolTipLabel.equals("Relapse(Months)")) {
			if (value == -1) {
				str = "No Relapse";
			} else {
				str = value + "";
			}
		} else if (toolTipLabel.equals("RelapseSite")) {
			if (value == -1) {
				str = "No Value";
			} else if (value == 1) {
				str = "BM";
			} else if (value == 2) {
				str = "BM Combo";
			} else if (value == 3) {
				str = "CNS/Testis";
			} else if (value == 4) {
				str = "No relapse";
			}
		} else if (toolTipLabel.equals("Deceased")) {
			if (value == 1) {
				str = "Dead";
			} else {
				str = "Alive";
			}
		} else if (toolTipLabel.equals("Sex")) {
			if (value == 1) {
				str = "Female";
			} else {
				str = "Male";
			}
		} else if (toolTipLabel.equals("Immuno")) {
			if (value == 1) {
				str = "B-cell";
			} else {
				str = "T-cell";
			}
		} else if (toolTipLabel.equals("GeneticCategory")) {
			if (value == 0) {
				str = "No Value";
			} else if (value == 1) {
				str = "hyperdiploid";
			} else if (value == 2) {
				str = "diploid";
			} else if (value == 3) {
				str = "pseudodiploid";
			} else if (value == 4) {
				str = "hypodiploid";
			}
		} else if (toolTipLabel.equals("FamilyIncome")) {
			if (value == 0) {
				str = "No Value";
			} else if (value == 1) {
				str = "Very Low Income";
			} else if (value == 2) {
				str = "Low Income";
			} else if (value == 3) {
				str = "Moderate Income";
			} else if (value == 4) {
				str = "Middle Income";
			} else {
				str = "Upper Income";
			}
		} else if (toolTipLabel.equals("Education")) {
			if (value == 0) {
				str = "No Value";
			} else if (value == 1) {
				str = "No High School Diploma";
			} else if (value == 2) {
				str = "High School Diploma or GED";
			} else if (value == 3) {
				str = "Some College";
			} else if (value == 4) {
				str = "College Graduate";
			}
		} else if (toolTipLabel.equals("Race")) {
			if (value == 0) {
				str = "No Value";
			} else if (value == 1) {
				str = "More than one Race";
			} else if (value == 2) {
				str = "Caucasian, White/Non-Hispanic";
			} else if (value == 3) {
				str = "Asian";
			} else if (value == 4) {
				str = "Black";
			} else if (value == 5) {
				str = "Hispanic";
			}
		} else {
			str = value + "";
		}
		return str;
	}

	/**
	 * Bigger axis labels are split
	 * 
	 * @param g2
	 * @param curx
	 * @param label
	 */
	private void drawMultiLineAxisLabel(Graphics2D g2, float curLabelX,
			float curLabelY, String label) {
		String[] labelParts = null;

		if (label.indexOf(LABEL_NEWLINE_SEPARATOR) != -1) {
			// Label contains new line specifier
			labelParts = label.split(LABEL_NEWLINE_SEPARATOR);
		} else {
			labelParts = new String[1];
			labelParts[0] = label;
		}

		for (int labelCount = 0; labelCount < labelParts.length; ++labelCount) {
			g2.drawString(labelParts[labelCount], curLabelX, curLabelY);
			curLabelY += 10;
		}
	}

	/**
	 * Helper function to trigger rendering of a given region.
	 */
	void renderRegion(int startAxis, int stopAxis) {
		if (startAxis < 0)
			startAxis = 0;
		if (stopAxis >= numDimensions)
			stopAxis = numDimensions - 1;

		renderThread.setRegion(startAxis, stopAxis);
		renderThread.render();

		if (brushImg != null) {
			// if we do this, we have to add another buffer img for the brush in
			// paint()
			// brushThread.setRegion(startAxis, stopAxis);
			brushThread.render();
		}
	}

	/**
	 * Helper function to trigger rendering of the whole display.
	 */
	void renderAll() {
		renderRegion(0, numDimensions - 1);
	}

	/**
	 * Helper function to trigger rendering of the brush image.
	 */
	void renderBrush() {
		brushThread.setRegion(0, numDimensions - 1);
		brushThread.render();
	}

	/**
	 * Draws the given record on the given Graphics object. This method is
	 * called by the RenderThread and can be overridden to modify the look of
	 * the rendering.
	 * 
	 * @param g2
	 *            The Graphics2D object to draw the record on.
	 * @param comp
	 *            The ParallelDisplay component to get the record data from.
	 * @param num
	 *            The id of the record to draw.
	 * @param startAxis
	 *            The start axis to draw from.
	 * @param stopAxis
	 *            The stop axis to draw to.
	 */
	void drawRecord(Graphics2D g2, ParallelDisplay comp, int num,
			int startAxis, int stopAxis) {

		GeneralPath rPath = new GeneralPath();
		rPath.moveTo(stepx * startAxis, getYValue(num, startAxis, comp));

		for (int j = startAxis + 1; j <= stopAxis; j++) {
			rPath.lineTo(stepx * j, getYValue(num, j, comp));
		}
		g2.draw(rPath);
	}

	/**
	 * Helper function to draw a "tooltip" on the given graphics object.
	 * 
	 * @param g2
	 *            The Graphics2D Object to draw on.
	 * @param text
	 *            The (multiline) text of the tooltip.
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param col
	 *            The background color.
	 */
	private void drawTooltip(Graphics2D g2, String text, int x, int y, Color col) {
		int i;
		int mheight, mwidth = 0;
		int numLines, lineHeight;

		StringTokenizer tok = new StringTokenizer(text, "\n");
		numLines = tok.countTokens();
		String lines[] = new String[numLines];

		for (i = 0; i < numLines; i++) {
			lines[i] = tok.nextToken();

			int tempwidth = g2.getFontMetrics().stringWidth(lines[i]) + 6;
			if (tempwidth > mwidth)
				mwidth = tempwidth;
		}

		lineHeight = g2.getFontMetrics().getHeight();
		mheight = numLines * lineHeight + 2;

		x += 10;
		y += 10;
		if (x + mwidth > width)
			x -= (mwidth + 20);

		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.7f);
		g2.setComposite(ac);

		g2.setStroke(new BasicStroke(0.5f));
		g2.setColor(new Color(0.2f, 0.2f, 0.2f));
		g2.drawRect(x, y, mwidth, mheight);
		g2.setColor(col);
		g2.fillRect(x + 1, y + 1, mwidth - 1, mheight - 1);

		g2.setColor(Color.black);

		ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g2.setComposite(ac);

		for (i = 0; i < numLines; i++) {
			g2.drawString(lines[i], x + 3, y + (i + 1) * lineHeight - 4);
		}

	}

	/**
	 * Helper function to draw an arrow.
	 * 
	 * @param g2
	 *            The Graphics2D Object to draw on.
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param size
	 *            The size in pixels.
	 * @param horizontal
	 *            If true, the arrow is drawn horizontally, if false vertically.
	 * @param topright
	 *            If true, the arrowhead is top/right, if false bottom/left.
	 */
	private void drawArrow(Graphics2D g2, int x, int y, int size,
			boolean horizontal, boolean topright) {

		if (horizontal) {

			g2.drawLine(x - size / 2, y, x + size / 2, y);

			if (topright) {
				g2.drawLine(x + size / 4, y - size / 4, x + size / 2, y);
				g2.drawLine(x + size / 4, y + size / 4, x + size / 2, y);
			} else {
				g2.drawLine(x - size / 4, y - size / 4, x - size / 2, y);
				g2.drawLine(x - size / 4, y + size / 4, x - size / 2, y);
			}
		} else {

			g2.drawLine(x, y - size / 2, x, y + size / 2);

			if (topright) {
				g2.drawLine(x + size / 4, y - size / 4, x, y - size / 2);
				g2.drawLine(x - size / 4, y - size / 4, x, y - size / 2);
			} else {
				g2.drawLine(x + size / 4, y + size / 4, x, y + size / 2);
				g2.drawLine(x - size / 4, y + size / 4, x, y + size / 2);
			}
		}
	}

	/**
	 * Helper function, returns the y value (on screen) for a given record.
	 * Scale factors and translation is applied.
	 * 
	 * @param record
	 *            The recordnumber.
	 * @param axis
	 *            The axis to calculate the y value for.
	 * @param comp
	 *            our "parent" component.
	 */
	private float getYValue(int record, int axis, ParallelDisplay comp) {
		return getYValueAlongAxis(comp.getValue(record, axis), axis, comp);
	}

	private float getYValueAlongAxis(float value, int axis, ParallelDisplay comp) {
		value -= comp.getAxisOffset(axis);
		value *= (comp.getHeight() - 2 * borderV) / comp.getAxisScale(axis);
		return value;
	}

	private int getYValueforZones(int value, int axis, ParallelDisplay comp) {
		value -= comp.getAxisOffset(axis);
		value *= (comp.getHeight() - 2 * borderV) / comp.getAxisScale(axis);
		return value;
	}

	// record old coordinates for dragging
	int oldMouseX, oldMouseY;

	int activeAxis = -1;

	float oldScale, oldOffset;

	// 0.0 - 1.0 value of loacion of click on axis
	float clickValue;

	// actual value of point clicked on axis
	float clickAxisValue;

	int clickModifiers;

	boolean tempBrushSwap = false;

	/**
	 * Invoked when the mouse exits the component.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Invoked when a mouse button has been released on a component. Checks if
	 * something has been dragged and finishes the drag process.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	public void mouseReleased(MouseEvent e) {
		ParallelDisplay comp = (ParallelDisplay) e.getComponent();

		if (e.isPopupTrigger()) {
			// System.out.println("showing popup...");
			comp.popupMenu.show(comp, e.getX(), e.getY());
			return;
		}

		switch (comp.getEditMode()) {
		case ParallelDisplay.REORDER:
			dragAxis = false;
			break;
		case ParallelDisplay.BRUSH:
			if (!angularPhase1) {
				inBrush = false;
				inAngularBrush = false;
				comp.setCurrentBrush(tempBrush);
				tempBrushSwap = true;
			}
			break;
		}
	}

	private int brushmode = 0;

	private static final int BRUSH_NORMAL = 0;

	private static final int BRUSH_ADD = 1;

	private static final int BRUSH_SUBTRACT = 2;

	private static final int BRUSH_INTERSECT = 3;

	private boolean inAngularBrush = false;

	private boolean angularPhase1 = false;

	private int angularRefX = 0;

	private int angularRefY = 0;

	private int angularCurX = 0;

	private int angularCurY = 0;

	private int angularStartX = 0;

	private int angularStartY = 0;

	private float angularAngle1 = 0.0f;

	private float angularAngle2 = 0.0f;

	private int angularRegion = 0;

	private int hoverRegion = 0;

	private int dragBrushCtr = 0;

	/**
	 * Invoked when a mouse button has been pressed on a component. Checks if
	 * the user starts dragging something.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	public void mousePressed(MouseEvent e) {
		ParallelDisplay comp = (ParallelDisplay) e.getComponent();

		if (e.isPopupTrigger()) {
			comp.popupMenu.show(comp, e.getX(), e.getY());
			return;
		}

		oldMouseX = e.getX();
		oldMouseY = e.getY();

		activeAxis = hoverAxis;

		clickModifiers = e.getModifiers();

		if (activeAxis != -1) {

			oldScale = comp.getAxisScale(activeAxis);
			oldOffset = comp.getAxisOffset(activeAxis);

			clickValue = ((float) oldMouseY - borderV)
					/ (comp.getHeight() - 2 * borderV);
			clickAxisValue = comp.getAxisOffset(activeAxis) + clickValue
					* comp.getAxisScale(activeAxis);
		}

		switch (comp.getEditMode()) {
		case ParallelDisplay.REORDER:
			dragAxis = true;
			break;
		case ParallelDisplay.BRUSH:
			inBrush = true;

			if (inAngularBrush && angularPhase1) {
				angularPhase1 = false;

				angularStartX = angularCurX;
				angularStartY = angularCurY;
			} else {
				if (activeAxis != -1) {
					brushHoverStart = oldMouseY - borderV;
					brushHoverEnd = oldMouseY - borderV;
					brushHoverX = oldMouseX - borderH;
					inAngularBrush = false;
				} else {
					inAngularBrush = true;
					angularPhase1 = true;
					angularRefX = e.getX() - borderH;
					angularRefY = e.getY() - borderV;
					angularCurX = angularRefX;
					angularCurY = angularRefY;
					angularRegion = hoverRegion;
				}
			}

			Color color = Color.GREEN; // temp color
			dragBrush = new Brush(comp.getNumRecords(), color);
			dragBrushCtr++; // To generate diff strokes for each dragBrush

			if (e.isControlDown()) {
				brushmode = BRUSH_INTERSECT;
			} else if (e.isShiftDown()) {
				brushmode = BRUSH_ADD;
			} else if (e.isAltDown()) {
				brushmode = BRUSH_SUBTRACT;
			} else {
				brushmode = BRUSH_NORMAL;
			}

			hoverRecord = -1;

			if (brushImg == null) {
				brushImg = new BufferedImage(comp.getWidth(), comp.getHeight(),
						BufferedImage.TYPE_4BYTE_ABGR);
				Graphics2D ig = brushImg.createGraphics();
				// fill with transparent white
				ig.setColor(new Color(1.0f, 1.0f, 1.0f, 0.0f));
				ig.fillRect(0, 0, comp.getWidth(), comp.getHeight());
			}
		}
	}

	/**
	 * Invoked when a mouse button is pressed on a component and then dragged.
	 * Mouse drag events will continue to be delivered to the component where
	 * the first originated until the mouse button is released (regardless of
	 * whether the mouse position is within the bounds of the component).
	 * 
	 * Depending on the current mode, this method performs scaling, translating
	 * or reordering of axes.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	public void mouseDragged(MouseEvent e) {
		ParallelDisplay comp = (ParallelDisplay) e.getComponent();

		int mouseX = e.getX();
		int mouseY = e.getY();

		setMetaInfo(null, 0, 0);

		switch (comp.getEditMode()) {
		case ParallelDisplay.SCALE:
			if (activeAxis != -1) {
				float way = ((float) (oldMouseY - mouseY))
						/ (comp.getHeight() - 2 * borderV);
				comp.setAxisScale(activeAxis, oldScale + (way * oldScale));
				float newValue = clickValue
						* (comp.getAxisScale(activeAxis) - oldScale);
				comp.setAxisOffset(activeAxis, oldOffset - newValue);

				renderRegion(activeAxis - 1, activeAxis + 1);
			}
			break;
		case ParallelDisplay.TRANSLATE:
			if (activeAxis != -1) {
				float way = ((float) (oldMouseY - mouseY))
						/ (comp.getHeight() - 2 * borderV);
				way *= comp.getAxisScale(activeAxis);
				comp.setAxisOffset(activeAxis, oldOffset + way);

				renderRegion(activeAxis - 1, activeAxis + 1);
			}
			break;
		case ParallelDisplay.REORDER:
			if (activeAxis != -1) {
				int deltaX = mouseX - oldMouseX;
				int num = activeAxis + deltaX / stepx;

				if (num < 0)
					num = 0;
				if (num >= numDimensions)
					num = numDimensions - 1;

				dragX = mouseX;
				if (activeAxis != num) {
					comp.swapAxes(activeAxis, num);

					// System.out.println("setting repaint axes: " +
					// (Math.min(num,activeAxis) - 1) + ", " +
					// (Math.max(num,activeAxis) + 1));

					renderRegion(Math.min(num, activeAxis) - 1, Math.max(num,
							activeAxis) + 1);

					activeAxis = num;
					hoverAxis = num;
					oldMouseX = stepx * num + borderH;
				}
				// to display hoverAxis
				comp.repaint();
			}
			break;
		case ParallelDisplay.BRUSH:

			if (!inAngularBrush) {
				brushHoverEnd = mouseY - borderV;
				float releaseValue = ((float) mouseY - borderV)
						/ (comp.getHeight() - 2 * borderV);
				releaseValue = comp.getAxisOffset(activeAxis) + releaseValue
						* comp.getAxisScale(activeAxis);
				float lowerBound = Math.min(clickAxisValue, releaseValue);
				float upperBound = Math.max(clickAxisValue, releaseValue);

				// System.out.println("-Brush mode: lowerbound = " + lowerBound
				// + " upperbound = " + upperBound);

				boolean doSoft = comp.getBoolPreference("softBrush");
				float radius = 0.0f;
				int ids[];
				if (doSoft) {

					radius = comp.getFloatPreference("brushRadius")
							* (upperBound - lowerBound);
					if (radius == 0.0f) {
						System.out.println("radius is zero");
						doSoft = false;
					}

					ids = comp.getRecordsByValueRange(activeAxis, lowerBound
							- radius, upperBound + radius);

				} else {

					ids = comp.getRecordsByValueRange(activeAxis, lowerBound,
							upperBound);
				}

				dragBrush.setColorValueArray(comp.getNumRecords());
				dragBrush.setStrokeValueArray(comp.getNumRecords());

				Vector valExistsVector = new Vector();

				int id = 0;
				for (int i = 0; i < comp.getNumRecords(); i++) {
					float brushVal = 0.0f;

					if ((ids.length > 0) && (i == ids[id])) {
						// record is inside brush region
						brushVal = 1.0f;

						if (doSoft) {
							float val = comp.getValue(i, activeAxis);

							if (val < lowerBound) {
								brushVal = 1.0f - (-val + lowerBound) / radius;
							}
							if (val > upperBound) {
								brushVal = 1.0f - (val - upperBound) / radius;
							}
						}

						if (id < ids.length - 1)
							id++;

					}

					// TODO: Do similar to this .. when it comes to establishing
					// the
					// coordinated view
					String recNo = "" + i;
					if (!valExistsVector.contains(recNo)) {
						valExistsVector.add(recNo);
						Color recColor = (Color) recColorHash.get(recNo);
						// System.out.println("Vec Size: "
						// + valExistsVector.size() + " ReC:" + recNo
						// + " Color: " + recColor);

						// Color and stroke is given on line basis
						dragBrush.setColorValue(i, recColor);
						String recNoString = "" + i;
						recColorHash.put(recNoString, recColor);

						// Setting strokes for lines selected during the
						// mouseDragged event
						if (dragBrushCtr == 1) {
							float[] dashPattern = { 5, 5 };
							BasicStroke stroke = new BasicStroke(1.0f,
									BasicStroke.CAP_BUTT,
									BasicStroke.JOIN_MITER, 10, dashPattern, 0);
							dragBrush.setStrokeValue(i, stroke);
							recStrokeHash.put(recNoString, (Object) stroke);

						} else if (dragBrushCtr == 2) {
							float[] dashPattern = { 20, 10 };
							BasicStroke stroke = new BasicStroke(1.0f,
									BasicStroke.CAP_BUTT,
									BasicStroke.JOIN_MITER, 10, dashPattern, 0);
							dragBrush.setStrokeValue(i, stroke);
							recStrokeHash.put(recNoString, (Object) stroke);

						} else {
							float[] dashPattern = { 30, 10, 5, 10 };
							BasicStroke stroke = new BasicStroke(1.0f,
									BasicStroke.CAP_BUTT,
									BasicStroke.JOIN_MITER, 10, dashPattern, 0);
							dragBrush.setStrokeValue(i, stroke);
							recStrokeHash.put(recNoString, (Object) stroke);

							dragBrushCtr = 0; // re-initialize the counter
							// after 3
						}

					}
					dragBrush.setBrushValue(i, brushVal);
				}

			} else { // angular brushing
				angularCurX = mouseX - borderH;
				angularCurY = mouseY - borderV;
				if (!angularPhase1) {
					float maxratio = (angularRefY - angularStartY)
							/ (float) (angularStartX - angularRefX);
					float tempratio = (angularRefY - angularCurY)
							/ (float) (angularCurX - angularRefX);
					float minratio = Math.min(maxratio, tempratio);
					maxratio = Math.max(maxratio, tempratio);

					angularAngle1 = (float) Math.atan(maxratio);
					angularAngle2 = (float) Math.atan(minratio);

					// System.out.println("a1: " + angularAngle1 + " a2: " +
					// angularAngle2);

					for (int i = 0; i < dragBrush.getNumValues(); i++) {
						float val1 = (comp.getValue(i, angularRegion) - comp
								.getAxisOffset(angularRegion))
								/ comp.getAxisScale(angularRegion)
								* (comp.getHeight() - 2 * borderV);
						float val2 = (comp.getValue(i, angularRegion + 1) - comp
								.getAxisOffset(angularRegion + 1))
								/ comp.getAxisScale(angularRegion + 1)
								* (comp.getHeight() - 2 * borderV);
						float ratio = (val1 - val2) / stepx;

						// System.out.println("val1: " + val1 + " val2: " + val2
						// + " ratio: " + ratio + "minratio: " + minratio + "
						// maxratio: " + maxratio);
						// System.out.println("axis1: " + angularRegion);
						if (ratio >= minratio && ratio <= maxratio) {
							// System.out.println("setting brush num " + i + "
							// ratio: " + ratio + " minratio: " + minratio + "
							// maxratio: " + maxratio);
							dragBrush.setBrushValue(i, 1.0f);
						} else {
							dragBrush.setBrushValue(i, 0.0f);
						}
					}
				}
			}

			if (brushmode == BRUSH_INTERSECT) {
				tempBrush = comp.getCurrentBrush().intersect(dragBrush);
			} else if (brushmode == BRUSH_ADD) {
				System.out.println("Inside Brushmode .. BRUSH_ADD");
				tempBrush = comp.getCurrentBrush().add(dragBrush);
			} else if (brushmode == BRUSH_SUBTRACT) {
				tempBrush = comp.getCurrentBrush().subtract(dragBrush);
			} else {
				tempBrush = dragBrush;
			}

			comp.fireBrushModified(tempBrush);

			if (tempBrush.getNumBrushed() > 0) {
				brushThread.setBrush(tempBrush);
				renderBrush();
				// to see brush line in realtime
			}
			comp.repaint();
			break;

		}

	}

	/**
	 * Invoked when the mouse has been clicked on a component.
	 * 
	 * Checks if the click hit an arrow and inverts the corresponding axis.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	public void mouseClicked(MouseEvent e) {

		ParallelDisplay comp = (ParallelDisplay) e.getComponent();

		// arrow clicked or invert mode
		if ((comp.getEditMode() == ParallelDisplay.INVERT)
				|| (e.getY() <= 25 && e.getY() > 12)) {
			if (hoverAxis != -1) {
				comp.setAxisOffset(hoverAxis, comp.getAxisOffset(hoverAxis)
						+ comp.getAxisScale(hoverAxis));
				comp.setAxisScale(hoverAxis, comp.getAxisScale(hoverAxis) * -1);

				renderRegion(activeAxis - 1, activeAxis + 1);

			}
		}

		/*
		 * Identifies the clicked axis - Computes freq of record values on
		 * clicked axis - Sorts the record values - high -> low
		 */
		if ((e.getClickCount() == 2) && (hoverAxis != -1)) {
			System.out.println("Clicked Axis No:" + hoverAxis);

			// get the label to highlight the text of selected axis
			setSelAxisLabel(comp.getAxisLabel(hoverAxis));
			comp.setBasicParallelDisplayUIObject(this);

			// Sort the values on a given axis
			HashMap axisValHash = computeAxisValueFrequency(comp, hoverAxis);
			// System.out.println("KeySet: " + axisValHash.keySet());

			ArrayList alist = new ArrayList(axisValHash.keySet());
			/*
			 * As 'alist' contains String values - normal sorting returns
			 * results in alpha-sorting format. Hence a comparator was added to
			 * do the numeric sorting of values
			 */
			Collections.sort(alist, new Comparator() {
				public int compare(Object o1, Object o2) {
					double doub1 = Double.parseDouble((String) o1);
					double doub2 = Double.parseDouble((String) o2);
					return (int) (doub2 - doub1);
				}
			});

			// System.out.println("Sorted Alist: " + alist);
			Color colour = Color.GREEN;
			// Create a new brush
			newBrush = new Brush(comp.getNumRecords(), colour);
			newBrush.setColorValueArray(comp.getNumRecords());

			this.computeColorSteps(comp.getNumRecords());
			recColorHash.clear();

			int k = 0;
			for (int i = 0; i < alist.size(); i++) {

				String s = alist.get(i).toString();
				float recVal = Float.valueOf(s.trim()).floatValue();
				int[] recNos = comp.getRecordsByValue(hoverAxis, recVal);

				for (int j = 0; j < recNos.length; j++) {

					// Color code the lines in different colors
					Color color = new Color((255 - (redStep * k)),
							-(greenStep * k), 0);

					newBrush.setColorValue(recNos[j], color);
					newBrush.setBrushValue(recNos[j], 0.5f);

					String recNoString = "" + recNos[j];
					recColorHash.put(recNoString, (Object) color);
					k++;
				}
			}
			comp.setCurrentBrush(newBrush);
		}
	}

	// Computes the incr color steps to color different lines
	private void computeColorSteps(int nosOfRecords) {

		Color maxColor = Color.RED;
		Color minColor = Color.GREEN;
		// Color maxBlueColor = Color.BLUE;

		int redRange = maxColor.getRed() - minColor.getRed();
		int greenRange = maxColor.getGreen() - minColor.getGreen();
		// int blueRange = maxBlueColor.getBlue() - minColor.getBlue();

		redStep = redRange / nosOfRecords;
		greenStep = greenRange / nosOfRecords;
		// blueStep = blueRange / nosOfRecords;

		// System.out.println("Nos of Rec: " + nosOfRecords + " BB: "
		// + blueRange + " BS:" + blueStep);
	}

	// Calculates the freq of values for the clicked axis
	private HashMap computeAxisValueFrequency(ParallelDisplay comp,
			int hoverAxis2) {

		HashMap hash = new HashMap();

		for (int i = 0; i < comp.getNumRecords(); i++) {
			if (!hash.containsKey(String.valueOf(comp.getValue(i, hoverAxis2)))) {
				hash.put(String.valueOf(comp.getValue(i, hoverAxis2)), "1");

			} else {
				int ctr = Integer.parseInt((String) hash.get(String
						.valueOf(comp.getValue(i, hoverAxis2))));
				ctr++;
				hash.remove(String.valueOf(comp.getValue(i, hoverAxis2)));
				hash.put(String.valueOf(comp.getValue(i, hoverAxis2)), Integer
						.toString(ctr));
			}
		}

		return hash;

	}

	/**
	 * Invoked when the mouse enters a component.
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Invoked when the mouse button has been moved on a component (with no
	 * buttons no down).
	 * 
	 * Displays tooltips if mouse is hovering over axes or records.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	public void mouseMoved(MouseEvent e) {

		ParallelDisplay comp = (ParallelDisplay) e.getComponent();

		if (inBrush) {
			if (inAngularBrush && angularPhase1) {
				angularCurX = e.getX() - borderH;
				angularCurY = e.getY() - borderV;
				comp.repaint();
			}
		} else {
			int mousex = e.getX() - borderH;
			int mousey = e.getY() - borderV;

			int oldAxis = hoverAxis;
			int oldRecord = hoverRecord;

			hoverAxis = -1;

			for (int i = 0; i < numDimensions; i++) {
				if ((mousex > (i * stepx - 6)) && (mousex < (i * stepx + 6))) {
					hoverAxis = i;
				}
				if ((mousex > i * stepx) && (mousex < (i + 1) * stepx)) {
					comp.popupMenu.setTargetRegion(i + 1);
					hoverRegion = i;
				}
			}

			if (mousex < 0)
				comp.popupMenu.setTargetRegion(0);

			hoverRecord = getRecordByCoordinates(mousex, mousey, comp);
			// System.out.println(">> : "+ hoverRecord);

			if ((oldAxis != hoverAxis) || (oldRecord != hoverRecord)) {
				if (hoverAxis != -1) {
					setMetaInfo(comp.getAxisLabel(hoverAxis), mousex, mousey);

					switch (comp.getEditMode()) {
					case ParallelDisplay.REORDER:
						comp.setCursor(Cursor
								.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
						break;
					case ParallelDisplay.SCALE:
						comp.setCursor(Cursor
								.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
						break;
					case ParallelDisplay.TRANSLATE:
						comp.setCursor(Cursor
								.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
						break;
					case ParallelDisplay.INVERT:
						comp.setCursor(Cursor
								.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
						break;
					case ParallelDisplay.BRUSH:
						comp.setCursor(Cursor
								.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
						break;
					}

				} else {
					setMetaInfo(null, 0, 0);
					comp.resetCursor();
				}

				if (hoverRecord != -1) {
					setMetaInfo(comp.getRecordLabel(hoverRecord), mousex,
							mousey);
				}

				comp.repaint();
			}
		}

	}

	/**
	 * Helper method to display a tooltip on hover.
	 */
	private void setMetaInfo(String text, int x, int y) {
		metaText = text;
		metaX = x;
		metaY = y;
	}

	/**
	 * Returns the record that goes through the screen coordinates x,y. The
	 * first record that is found is returned.
	 * 
	 * @param x
	 *            The x screen coordinate.
	 * @param y
	 *            The y screen coordinate.
	 * @param comp
	 *            The "parent" component.
	 * 
	 * @return The recordnumber of the first record found passing through the
	 *         given point.
	 */
	public int getRecordByCoordinates(int x, int y, ParallelDisplay comp) {
		for (int i = 0; i < numDimensions - 1; i++) {
			if ((x >= i * stepx) && (x < (i + 1) * stepx)) {
				float part = (x - i * stepx) / (float) stepx;
				for (int j = 0; j < numRecords; j++) {
					float recVal = (1 - part) * getYValue(j, i, comp) + part
							* getYValue(j, i + 1, comp);

					if (Math.abs(recVal - y) < 3.0)
						return j;
				}
				break;
			}
		}
		return -1;
	}

	public String getSelAxisLabel() {
		return selAxisLabel;
	}

	public void setSelAxisLabel(String selAxisLabel) {
		this.selAxisLabel = selAxisLabel;
		System.out.println("Sel Axis Label: " + selAxisLabel);
	}

	public HashMap getRecColorHash() {
		return recColorHash;
	}

	public HashMap getRecStrokeHash() {
		return recStrokeHash;
	}

	// Obtain the selected patientNos from other view and highlight
	// them in the parallel coordinate view
	public void displayMatrixSelectedRecords(Vector vecMatSelectedPatients,
			HashMap hashMSelPatients) {

		ParallelDisplay comp = MainFrame.getInstance().getParallelDisplay();

		Color colour = Color.GREEN;
		newBrush = new Brush(comp.getNumRecords(), colour);
		newBrush.setColorValueArray(comp.getNumRecords());

		for (int j = 0; j < vecMatSelectedPatients.size(); j++) {
			int recIndex = Integer.parseInt((String) vecMatSelectedPatients
					.get(j));
			// System.out.println("rec: " + comp.getRecordLabel(recIndex - 1));

			colour = (Color) hashMSelPatients
					.get(vecMatSelectedPatients.get(j));
			newBrush.setColorValue(recIndex - 1, colour);
			newBrush.setBrushValue(recIndex - 1, 2.5f);
		}
		comp.setCurrentBrush(newBrush);

	}

}

// New class to sort the values of a given data vector
class ValueComparator implements Comparator {
	private Map patientNoHash;

	public ValueComparator(Map patientNoHash) {
		this.patientNoHash = patientNoHash;
	}

	public int compare(Object o1, Object o2) {
		String v1 = (String) patientNoHash.get(o1);
		String v2 = (String) patientNoHash.get(o2);

		int result = 1;
		if (v1 != null && v2 != null) {
			try {
				int int1 = Integer.parseInt(v1);
				int int2 = Integer.parseInt(v2);
				result = int2 - int1;
			} catch (NumberFormatException e) {
				result = v1.compareTo(v2);
			}
		}

		if (result == 0) {
			result = 1;
		}
		return result;
	}

}
