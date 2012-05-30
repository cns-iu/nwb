package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;

import edu.iu.sci2.visualization.geomaps.viz.PageLayout;


public class PageHeader implements PostScriptable {
	public static final double TITLE_FONT_BRIGHTNESS = 0.0;
	public static final double OTHER_DATA_FONT_BRIGHTNESS = 0.0;
	
	private final String title;
	private final Point2D.Double lowerLeft;
	private final PageLayout pageLayout;
	private final Collection<String> extraInfo;
	
	public PageHeader(String title, Point2D.Double lowerLeft, PageLayout pageLayout, String... extraInfo) {
		this.title = title;
		this.lowerLeft = lowerLeft;
		this.pageLayout = pageLayout;
		this.extraInfo = Collections2.filter(
				Arrays.asList(extraInfo),
				Predicates.not(new Predicate<String>() {
					@Override
					public boolean apply(String input) {
						return Strings.isNullOrEmpty(input);
					}					
				}));
	}
	
	
	@Override
	public String toPostScript() {	
		StringBuilder builder = new StringBuilder();
		
		builder.append("% Page info" + "\n");
		builder.append("gsave" + "\n");
		
		builder.append(INDENT + "% Show title and subtitle" + "\n");
		builder.append(INDENT + lowerLeft.x + " " + lowerLeft.y + " moveto" + "\n");
		
		builder.append(PSUtility.findscalesetfont(pageLayout.titleFont()) + "\n");
		builder.append(PSUtility.setgray(TITLE_FONT_BRIGHTNESS) + "\n");
		builder.append(INDENT + "gsave" + "\n");
		builder.append(INDENT + INDENT + "(" + title + ") show" + "\n");
		builder.append(INDENT + "grestore" + "\n");
		
		builder.append(INDENT + "% Show the rest of the info" + "\n");
		builder.append(INDENT + "0 " + (-(pageLayout.titleFont().getSize() + 5)) + " rmoveto");
		builder.append(PSUtility.findscalesetfont(pageLayout.contentFont()) + "\n");
		builder.append(PSUtility.setgray(OTHER_DATA_FONT_BRIGHTNESS) + "\n");
		for (String infoBit : extraInfo) {
			builder.append(INDENT + "gsave" + "\n");
			builder.append(INDENT + INDENT + "(" + infoBit + ")" + " show" + "\n");
			builder.append(INDENT + "grestore" + "\n");
			builder.append(INDENT + "0 " + (-(pageLayout.contentFont().getSize() + 5)) + " rmoveto" + "\n");
		}
		
		builder.append("grestore" + "\n");
		
		return builder.toString();
	}
}
