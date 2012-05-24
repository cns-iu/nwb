package edu.iu.sci2.visualization.geomaps.viz;

import java.util.Collection;
import java.util.Dictionary;
import java.util.EnumSet;

import org.antlr.stringtemplate.StringTemplate;

import prefuse.data.Table;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.iu.sci2.visualization.geomaps.GeoMapsNetworkFactory;
import edu.iu.sci2.visualization.geomaps.data.GeoDataset;
import edu.iu.sci2.visualization.geomaps.data.GeoDataset.Stage;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.metatype.Parameters;
import edu.iu.sci2.visualization.geomaps.utility.NicelyNamedEnums;
import edu.iu.sci2.visualization.geomaps.utility.numberformat.NumberFormatFactory.NumericFormatType;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;
import edu.iu.sci2.visualization.geomaps.viz.coding.Coding;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReference;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.model.GeoMap;
import edu.iu.sci2.visualization.geomaps.viz.model.GeoMapException;
import edu.iu.sci2.visualization.geomaps.viz.ps.HowToRead;

public abstract class AnnotationMode<G, D extends Enum<D> & VizDimension> {
	/**
	 * The quantitative dimensions visualized by this annotation mode.
	 */
	protected abstract EnumSet<D> dimensions();
	/**
	 * Read {@code table} using the specified {@code bindings} into a geo dataset.
	 */
	protected abstract GeoDataset<G, D> readTable(Table table, Collection<Binding<D>> bindings);
	/**
	 * Code the given dataset into a collection of circle views (if any).
	 */
	protected abstract Collection<Circle> makeCircles(
			GeoDataset<G, D> scaledData, Collection<? extends Coding<D>> codings);
	/**
	 * Code the given dataset into a collection of feature/region views (if any).
	 */
	protected abstract Collection<FeatureView> makeFeatureViews(
			GeoDataset<G, D> scaledData, Collection<? extends Coding<D>> codings);

	public GeoMap createGeoMap(
			final Table table,
			final Dictionary<String, Object> parameters,
			PageLayout pageLayout,
			String title,
			String subtitle,
			StringTemplate templateForHowToRead)
				throws LegendCreationException, GeoMapException {
		Shapefile shapefile = NicelyNamedEnums.getConstantNamed(
				Shapefile.class, (String) parameters.get(GeoMapsNetworkFactory.Parameter.SHAPEFILE_KEY.id()));
		
		KnownProjectedCRSDescriptor knownProjectedCRSDescriptor =
				shapefile.getDefaultProjectedCrs();
		String projection = (String) parameters.get(Parameters.PROJECTION_ID);
		if (projection != null) {
			knownProjectedCRSDescriptor = NicelyNamedEnums.getConstantNamed(
					KnownProjectedCRSDescriptor.class, projection);
		}
		
		Collection<Binding<D>> enabledBindings = bindTo(parameters);

		GeoDataset<G, D> usableData = readTable(table, enabledBindings);
		
		Collection<Coding<D>> codings = Sets.newHashSet();
		Collection<LabeledReference> legends = Lists.newArrayList();
		for (Binding<D> binding : enabledBindings) {
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
		
		Optional<HowToRead> howToRead = Optional.<HowToRead>absent();
		if (pageLayout.howToReadLowerLeft().isPresent()) {
			String mapKind = subtitle.toLowerCase();
			
			templateForHowToRead.setAttributes(ImmutableMap.of(
					"mapKind", mapKind,
					"baseMapDescription", shapefile.makeMapDescription(),
					"projectionName", knownProjectedCRSDescriptor.getDescription(),
					"hasInsets", shapefile.hasInsets(),
					"partType", shapefile.getComponentDescriptionPlain()));		
			String howToReadText = templateForHowToRead.toString().trim();
			
			howToRead = Optional.of(new HowToRead(pageLayout.howToReadLowerLeft().get(),
					pageLayout, howToReadText, mapKind));
		}
		
		return new GeoMap(
				title,
				shapefile,
				knownProjectedCRSDescriptor,
				makeFeatureViews(usableData, codings),
				makeCircles(usableData, codings),
				legends,
				pageLayout,
				howToRead);
	}
	
	private Collection<Binding<D>> bindTo(final Dictionary<String, Object> parameters) {
		return Collections2.filter(
				Collections2.transform(
					dimensions(),
					new Function<D, Binding<D>>() {
						@Override
						public Binding<D> apply(D dimension) {
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
