package edu.iu.scipolicy.visualization.horizontalbargraph.bar;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;

import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BasicLayout;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.Record;

public class BarFactory {
	public static Collection<Bar> createBars(
			Collection<Record> records, BasicLayout layout) {
		Collection<Bar> bars = new ArrayList<Bar>();
		
		for (Record record : records) {
			bars.add(createBar(record, layout));
		}
		
		return bars;
	}
	
	public static Bar createBar(
			Record record, BasicLayout layout) {
		DateTime recordStartDate = record.getStartDate();
		DateTime recordEndDate = record.getEndDate();
		double recordAmount = record.getAmount();
		
		double startX = layout.calculateX(recordStartDate);
		double endX = layout.calculateX(recordEndDate);
		double width = endX - startX;
		double height = layout.calculateHeight(record);
		
		return new Bar(
			record.getLabel(),
			record.hasStartDate(),
			record.hasEndDate(),
			startX,
			width,
			height,
			recordAmount);
	}
}