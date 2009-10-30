package edu.iu.scipolicy.visualization.horizontalbargraph.layouts;

import java.util.Collection;

import org.joda.time.DateTime;

import edu.iu.scipolicy.visualization.horizontalbargraph.BoundingBox;
import edu.iu.scipolicy.visualization.horizontalbargraph.PageOrientation;
import edu.iu.scipolicy.visualization.horizontalbargraph.VisualElement;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.Record;

public interface Layout {
	public static final int POINTS_PER_INCH = 72;
	
	public double getSpaceBetweenBars();
	
	public PageOrientation determinePageOrientation(
			double pageWidth,
			double pageHeight,
			Collection<? extends VisualElement> visualElements);
	
	public double calculateHorizontalMargin(double pageWidth);
	public double calculateVerticalMargin(double pageHeight);
	
	public double calculateX(DateTime date);
	// public double calculateY(DateTime date);
	
	public double calculateHeight(Record record);
	
	public BoundingBox calculateBoundingBox(
			double pageWidth, double pageHeight);
	
	public double calculatePageHeightToWidthRatio(
			double pageWidth, double pageHeight);;
	
	public double calculateTotalWidthWithoutMargins(double pageWidth);
	public double calculateTotalWidthWithMargins(double pageWidth);
	public double calculateTotalHeightWithoutMargins(
			Collection<? extends VisualElement> visualElements,
			double pageHeight);
	public double calculateTotalHeightWithMargins(
			Collection<? extends VisualElement> visualElements,
			double pageHeight);
	
	public Cursor createCursor();
	public double positionVisualElement(
			VisualElement visualElement, Cursor cursor);
	//public double updateCursor(double xAmount, double yAmount);
}