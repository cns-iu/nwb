package edu.iu.sci2.visualization.geomaps.viz;

import java.util.Collection;
import java.util.Dictionary;
import java.util.EnumSet;

import org.geotools.factory.FactoryRegistryException;

import prefuse.data.Table;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.data.GeoDataset;
import edu.iu.sci2.visualization.geomaps.data.GeoDataset.Stage;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;
import edu.iu.sci2.visualization.geomaps.viz.coding.Coding;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReference;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.NumberFormatFactory.NumericFormatType;
import edu.iu.sci2.visualization.geomaps.viz.model.GeoMap;
import edu.iu.sci2.visualization.geomaps.viz.model.GeoMapException;
import edu.iu.sci2.visualization.geomaps.viz.ps.GeoMapViewPS.ShapefilePostScriptWriterException;

public abstract class AnnotationMode<G, D extends Enum<D> & VizDimension> {
	protected abstract EnumSet<D> dimensions();
	protected abstract GeoDataset<G, D> readTable(Table table, Collection<Binding<D>> bindings);
	protected abstract GeoMap createGeoMap(
			String title,
			Shapefile shapefile,
			KnownProjectedCRSDescriptor projectedCrs,
			GeoDataset<G, D> scaledData,
			Collection<? extends Coding<D>> codings,
			Collection<LabeledReference> legends,
			PageLayout pageLayout) throws ShapefilePostScriptWriterException, FactoryRegistryException, GeoMapException;

	public GeoMap createGeoMap(
			final Table table,
			final Dictionary<String, Object> parameters,
			PageLayout pageLayout,
			String title)
				throws LegendCreationException, ShapefilePostScriptWriterException, FactoryRegistryException, GeoMapException {
		Shapefile shapefile = Shapefile.forNiceNameInTitleCase(
				(String) parameters.get(GeoMapsAlgorithm.SHAPEFILE_ID));
		
		KnownProjectedCRSDescriptor knownProjectedCRSDescriptor = shapefile.getDefaultProjectedCrs();
		if (GeoMapsAlgorithm.LET_USER_CHOOSE_PROJECTION) {
			knownProjectedCRSDescriptor = KnownProjectedCRSDescriptor.forNiceNameInTitleCase(
					(String) parameters.get(GeoMapsAlgorithm.PROJECTION_ID));
		}
		
		Collection<Binding<D>> enabledBindings = bindTo(parameters);

		GeoDataset<G, D> usableData = readTable(table, enabledBindings);//.viewScalableOnly();
//		GeoDataset<G, D> scaledData = usableData.viewScaled();
		
		Collection<Coding<D>> codings = Sets.newHashSet();
		Collection<LabeledReference> legends = Lists.newArrayList();
		for (Binding<D> binding : enabledBindings) {
//			Range<Double> dataRange   = usableData.calculateRangeOver(binding.dimension());
//			Range<Double> scaledRange = scaledData.calculateRangeOver(binding.dimension());
			
			NumericFormatType numericFormatType =
					NumericFormatType.guessFor(
							binding.columnName(),
							usableData.calculateRangeOver(binding.dimension(), Stage.SCALABLE));
			
			Coding<D> coding = binding.codingForDataRange(
					usableData.calculateRangeOver(binding.dimension(), Stage.SCALABLE),
					usableData.calculateRangeOver(binding.dimension(), Stage.SCALED),
					shapefile);
			codings.add(coding);
			
			LabeledReference legend = coding.makeLabeledReference(pageLayout, numericFormatType);			
			legends.add(legend);
		}

		return createGeoMap(
				title, shapefile, knownProjectedCRSDescriptor, usableData, codings, legends, pageLayout);
	}
	
	private Collection<Binding<D>> bindTo(final Dictionary<String, Object> parameters) {
		return Collections2.filter(
				Collections2.transform(
					dimensions(),
					new Function<D, Binding<D>>() {
						@Override
						public Binding<D> apply(D dimension) {
							// TODO ?
							return (Binding<D>) dimension.bindingFor(parameters);
						}
					}),
				new Predicate<Binding<D>>() {
					@Override
					public boolean apply(Binding<D> binding) {
						return binding.isEnabled();
					}
				});
	}
}
