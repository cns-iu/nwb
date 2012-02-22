package edu.iu.sci2.visualization.geomaps.viz;

import java.util.Collection;
import java.util.Dictionary;
import java.util.EnumSet;

import prefuse.data.Table;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.data.GeoDataset;
import edu.iu.sci2.visualization.geomaps.data.scaling.ScalingException;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;
import edu.iu.sci2.visualization.geomaps.viz.coding.Coding;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.ps.PSWriter;
import edu.iu.sci2.visualization.geomaps.viz.ps.PSWriter.ShapefilePostScriptWriterException;
import edu.iu.sci2.visualization.geomaps.viz.ps.PostScriptable;

public abstract class AnnotationMode<G, D extends Enum<D> & VizDimension> {
	protected abstract EnumSet<D> dimensions();
	protected abstract GeoDataset<G, D> readTable(Table table, Collection<Binding<D>> bindings);
	protected abstract PSWriter createPSWriter(
			Shapefile shapefile,
			KnownProjectedCRSDescriptor projectedCrs,
			GeoDataset<G, D> scaledData,
			Collection<? extends Coding<D>> codings,
			Collection<PostScriptable> legends)	throws ShapefilePostScriptWriterException;

	public PSWriter createPSWriter(
			Table table,
			final Dictionary<String, Object> parameters)
				throws ScalingException, LegendCreationException, ShapefilePostScriptWriterException {
		Shapefile shapefile = Shapefile.PRETTY_NAME_TO_SHAPEFILE.get(parameters.get(GeoMapsAlgorithm.SHAPEFILE_ID));
		
		KnownProjectedCRSDescriptor knownProjectedCRSDescriptor = shapefile.defaultProjectedCrs();
		if (GeoMapsAlgorithm.LET_USER_CHOOSE_PROJECTION) {
			knownProjectedCRSDescriptor = KnownProjectedCRSDescriptor.forName((String) parameters.get(GeoMapsAlgorithm.PROJECTION_ID));
		}
		
		Collection<Binding<D>> enabledBindings = bindTo(parameters);

		GeoDataset<G, D> usableData = readTable(table, enabledBindings).viewScalableOnly();
		GeoDataset<G, D> scaledData = usableData.viewScaled();
		
		Collection<Coding<D>> codings = Sets.newHashSet();
		Collection<PostScriptable> legends = Lists.newArrayList();
		for (Binding<D> binding : enabledBindings) {
			Range<Double> dataRange   = usableData.calculateRangeOver(binding.getDimension());
			Range<Double> scaledRange = scaledData.calculateRangeOver(binding.getDimension());
			
			Coding<D> coding = binding.codingForDataRange(scaledRange);
			codings.add(coding);
			
			PostScriptable legend = coding.makeLabeledReference(dataRange, scaledRange);			
			legends.add(legend);
		}

		return createPSWriter(shapefile, knownProjectedCRSDescriptor, scaledData, codings, legends);
	}

	private Collection<Binding<D>> bindTo(final Dictionary<String, Object> parameters) {
		return Collections2.filter(
				Collections2.transform(
					dimensions(),
					new Function<D, Binding<D>>() {
						public Binding<D> apply(D dimension) {
							// TODO ?
							return (Binding<D>) dimension.bindingFor(parameters);
						}
					}),
				new Predicate<Binding<D>>() {
					public boolean apply(Binding<D> binding) {
						return binding.isEnabled();
					}
				});
	}
}
