package edu.iu.sci2.visualization.geomaps.viz.coding;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;

import com.google.common.collect.ImmutableMap;

import edu.iu.sci2.visualization.geomaps.data.interpolation.Interpolator;
import edu.iu.sci2.visualization.geomaps.data.scaling.ScalingException;
import edu.iu.sci2.visualization.geomaps.utility.Averages;
import edu.iu.sci2.visualization.geomaps.utility.Continuum;
import edu.iu.sci2.visualization.geomaps.utility.numberformat.NumberFormatFactory.NumericFormatType;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;
import edu.iu.sci2.visualization.geomaps.viz.legend.ColorLegend;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReference;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReferenceGradient;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;

public abstract class AbstractColorCoding<D extends Enum<D> & VizDimension> extends AbstractCoding<D, Color> {
	public static final ImmutableMap<String, Continuum<Color>> COLOR_RANGES =
			ImmutableMap.<String, Continuum<Color>>builder()
			.put("White to Green", Continuum.between(new Color(237, 248, 251), new Color(1, 109, 44)))
			.put("White to Purple", Continuum.between(new Color(237, 248, 251), new Color(129, 14, 124)))
			.put("Yellow to Orange", Continuum.between(new Color(255, 255, 212), new Color(153, 52, 4)))
			.put("Yellow to Red", Continuum.between(new Color(255, 255, 178), new Color(189, 0, 38)))
			.put("Yellow to Blue", Continuum.between(new Color(255, 255, 204), new Color(38, 52, 148)))
			.put("White to Black", Continuum.between(new Color(247, 247, 247), new Color(37, 37, 37)))
			.build();

	public AbstractColorCoding(Binding<D> binding, Continuum<Double> usableRange, Interpolator<Color> interpolator) {
		super(binding, usableRange, interpolator);
	}

	public abstract Point2D.Double lowerLeft(PageLayout pageLayout);

	public abstract Color defaultColor();

	@Override
	public LabeledReference makeLabeledReference(
			PageLayout pageLayout, NumericFormatType numericFormatType) throws LegendCreationException {
		LabeledReferenceGradient labeledReferenceGradient = new LabeledReferenceGradient(
				createColorLegend(numericFormatType), lowerLeft(pageLayout),
				pageLayout, pageLayout.colorGradientDimensions());

		return labeledReferenceGradient;
	}

	private ColorLegend createColorLegend(NumericFormatType numericFormatType) throws LegendCreationException {
		try {
			double midpointOfScaledData = Averages.meanOfDoubles(
					interpolator.getInRange().lowerEndpoint(), interpolator.getInRange().upperEndpoint());
			double unscaledValueForMidrangeColor = scaling().invert(midpointOfScaledData);

			ColorLegend colorLegend = new ColorLegend(
					makeVizLegend(numericFormatType),
					unscaledValueForMidrangeColor);

			return colorLegend;
		} catch (ScalingException e) {
			throw new LegendCreationException("TODO Problem formatting numbers for legend.", e);
		}
	}

	public static void addColorRangeParameter(DropdownMutator mutator, String rangeParameterId) {
		mutator.add(rangeParameterId, new ArrayList<String>(AbstractColorCoding.COLOR_RANGES.keySet()));
	}
}
