package edu.iu.sci2.visualization.geomaps;

import java.util.Dictionary;
import java.util.EnumSet;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationCanceledException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.ColumnNotFoundException;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.metatype.Parameters;
import edu.iu.sci2.visualization.geomaps.viz.FeatureDimension;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.coding.AbstractColorCoding;
import edu.iu.sci2.visualization.geomaps.viz.model.RegionAnnotationMode;
import edu.iu.sci2.visualization.geomaps.viz.ps.HowToRead;

public abstract class GeoMapsRegionsFactory implements AlgorithmFactory, ParameterMutator {
	public static final String SUBTITLE = "Choropleth Map";
	public static final StringTemplate TEMPLATE_FOR_HOW_TO_READ = 
			HowToRead.TEMPLATE_GROUP.getInstanceOf("choropleth");
	
	@Override
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters,	CIShellContext ciShellContext) {
		String featureNameColumnName = (String) parameters.get(RegionAnnotationMode.FEATURE_NAME_ID);
		
		return new GeoMapsAlgorithm<String, FeatureDimension>(
				data,
				parameters,
				getPageLayout(),
				new RegionAnnotationMode(featureNameColumnName),
				SUBTITLE,
				TEMPLATE_FOR_HOW_TO_READ,
				(LogService) ciShellContext.getService(LogService.class.getName()));
	}
	
	abstract PageLayout getPageLayout();
	
	public static class Print extends GeoMapsRegionsFactory {
		@Override
		PageLayout getPageLayout() {
			return PageLayout.PRINT;
		}		
	}
	
	public static class Web extends GeoMapsRegionsFactory {
		@Override
		PageLayout getPageLayout() {
			return PageLayout.WEB;
		}
	}
	
	@Override
	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition oldOCD) {
		Table table = (Table) data[0].getData();
		
		List<String> numericColumnNames = ImmutableList.of();
		try {
			numericColumnNames =
					Lists.newArrayList(TableUtilities.getValidNumberColumnNamesInTable(table));
		} catch (ColumnNotFoundException e) {
			// No problem, the color coding is optional so no numeric columns are strictly necessary
		}
		
		List<String> stringColumnNames = ImmutableList.of();
		try {
			stringColumnNames =
					Lists.newArrayList(TableUtilities.getValidStringColumnNamesInTable(table));
		} catch (ColumnNotFoundException e) {
			throw new AlgorithmCreationCanceledException(
					"A text-valued column identifying US states or countries is required " +
					"but no such column was detected.", e);
		}
		
		
		DropdownMutator mutator = new DropdownMutator();
		
		Parameters.addShapefileAndProjectionParameters(mutator);
		
		for (VizDimension dimension : EnumSet.allOf(FeatureDimension.class)) {
			dimension.addOptionsToAlgorithmParameters(mutator, numericColumnNames);
		}

		mutator.add(RegionAnnotationMode.FEATURE_NAME_ID, stringColumnNames);
		
		mutator.add(RegionAnnotationMode.COLOR_SCALING_ID,
				Collections2.transform(EnumSet.allOf(Scaling.class), Functions.toStringFunction()));

		mutator.add(
				RegionAnnotationMode.COLOR_RANGE_ID, AbstractColorCoding.COLOR_CONTINUUMS.keySet());
		
		return mutator.mutate(oldOCD);
	}
}

