package edu.iu.sci2.visualization.geomaps.viz.model;

import java.awt.Color;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;

import org.cishell.utilities.StringUtilities;
import org.geotools.factory.FactoryRegistryException;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import edu.iu.sci2.visualization.geomaps.GeoMapsRegionsFactory;
import edu.iu.sci2.visualization.geomaps.data.GeoDataset;
import edu.iu.sci2.visualization.geomaps.data.GeoDataset.Stage;
import edu.iu.sci2.visualization.geomaps.data.GeoDatum;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.viz.AnnotationMode;
import edu.iu.sci2.visualization.geomaps.viz.Circle;
import edu.iu.sci2.visualization.geomaps.viz.FeatureDimension;
import edu.iu.sci2.visualization.geomaps.viz.FeatureView;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;
import edu.iu.sci2.visualization.geomaps.viz.coding.Coding;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReference;
import edu.iu.sci2.visualization.geomaps.viz.ps.GeoMapViewPS.ShapefilePostScriptWriterException;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;

public class RegionAnnotationMode extends AnnotationMode<String, FeatureDimension> {
	public static final String FEATURE_NAME_ID = "featureName";
	public static final String COLOR_COLUMN_NAME_ID = "featureColorColumnName";
	public static final String COLOR_SCALING_ID = "featureColorScaling";
	public static final String COLOR_RANGE_ID = "featureColorRange";
	public static final String DEFAULT_FEATURE_NAME_ATTRIBUTE_KEY = "NAME";
	
	public static final String SUBTITLE = "Regions";
	public static final Color DEFAULT_FEATURE_COLOR = null;
	
	private final String featureNameColumnName;

	public RegionAnnotationMode(String featureNameColumnName) {
		this.featureNameColumnName = featureNameColumnName;
	}
	
	
	@Override
	protected EnumSet<FeatureDimension> dimensions() {
		return EnumSet.allOf(FeatureDimension.class);
	}
	
	@Override
	protected GeoDataset<String, FeatureDimension> readTable(Table table,
			Collection<Binding<FeatureDimension>> bindings) {
		return GeoDataset.fromTable(table, bindings, FeatureDimension.class,
				new Function<Tuple, String>() {
					@Override
					public String apply(Tuple row) {
						return StringUtilities.interpretObjectAsString(row.get(featureNameColumnName));
					}					
				});
	}
	
	@Override
	protected GeoMap createGeoMap(Shapefile shapefile,
			KnownProjectedCRSDescriptor projectedCrs,
			GeoDataset<String, FeatureDimension> scaledData,
			Collection<? extends Coding<FeatureDimension>> codings,
			Collection<LabeledReference> legends) throws ShapefilePostScriptWriterException, FactoryRegistryException, GeoMapException {
		Collection<FeatureView> featureViews = asFeatureViews(scaledData.geoData(Stage.SCALED), codings);
		
		return new GeoMap(
				GeoMapsRegionsFactory.SUBTITLE,
				shapefile,
				projectedCrs,
				featureViews,
				ImmutableSet.<Circle>of(),
				legends);
	}
	
	public static Collection<FeatureView> asFeatureViews(Collection<? extends GeoDatum<String, FeatureDimension>> valuedFeatures, final Collection<? extends Coding<FeatureDimension>> codings) {
		return Collections2.transform(
				valuedFeatures,
				new Function<GeoDatum<String, FeatureDimension>, FeatureView>() {
					@Override
					public FeatureView apply(GeoDatum<String, FeatureDimension> valuedFeature) {
						String featureName = valuedFeature.getGeo();
						
						EnumMap<FeatureDimension, Strategy> strategies = Maps.newEnumMap(FeatureDimension.class);
						for (Coding<FeatureDimension> coding : codings) {
							strategies.put(coding.dimension(), coding.strategyForValue(valuedFeature.valueInDimension(coding.dimension())));
						}
						
						return new FeatureView(featureName, strategies);
					}			
				});
	}
}
