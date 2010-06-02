//package edu.iu.scipolicy.visualization.horizontalbargraph.visualizationgeneration.generator;
//
//import edu.iu.cns.visualization.generator.VisualizationGenerator;
//import edu.iu.scipolicy.visualization.horizontalbargraph.Metadata;
//import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BasicLayout;
//import edu.iu.scipolicy.visualization.horizontalbargraph.record.RecordCollection;
//import edu.iu.scipolicy.visualization.horizontalbargraph.visualizationgeneration.visualization.HorizontalBarGraphVisualization;
//
//public abstract class HorizontalBarGraphVisualizationGenerator<
//		T extends HorizontalBarGraphVisualization> implements VisualizationGenerator<T> {
//	private BasicLayout layout;
//	private Metadata metadata;
//	private RecordCollection recordCollection;
//
//	public HorizontalBarGraphVisualizationGenerator(
//			BasicLayout layout, Metadata metadata, RecordCollection recordCollection) {
//		this.layout = layout;
//		this.metadata = metadata;
//		this.recordCollection = recordCollection;
//	}
//
//	public BasicLayout getLayout() {
//		return this.layout;
//	}
//
//	public Metadata getMetadata() {
//		return this.metadata;
//	}
//
//	public RecordCollection getRecordCollection() {
//		return this.recordCollection;
//	}
//
//	public abstract T generateVisualization();
//}