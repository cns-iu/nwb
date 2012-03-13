package edu.iu.sci2.visualization.geomaps.viz.coding;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;

import com.google.common.collect.ImmutableMap;

import edu.iu.sci2.visualization.geomaps.data.interpolation.Interpolator;
import edu.iu.sci2.visualization.geomaps.data.scaling.ScalingException;
import edu.iu.sci2.visualization.geomaps.utility.Averages;
import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;
import edu.iu.sci2.visualization.geomaps.viz.legend.ColorLegend;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReference;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReferenceGradient;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.NumberFormatFactory.NumericFormatType;

public abstract class AbstractColorCoding<D extends Enum<D> & VizDimension> extends AbstractCoding<D, Color> {
	public static final ImmutableMap<String, Range<Color>> COLOR_RANGES = ImmutableMap.of(
			"Yellow to Blue", Range.between(new Color(255, 255, 158), new Color(37, 52, 148)),
			"Yellow to Red", Range.between(new Color(254, 204, 92), new Color(177, 4, 39)),
			"Green to Red", Range.between(new Color(98, 164, 44), new Color(123, 21, 21)),
			"Blue to Red", Range.between(new Color(49, 243, 255), new Color(127, 4, 27)),
			"Gray to Black", Range.between(new Color(214, 214, 214), new Color(0, 0, 0)));

	public AbstractColorCoding(Binding<D> binding, Range<Double> usableRange, Interpolator<Color> interpolator) {
		super(binding, usableRange, interpolator);
	}

	public abstract Point2D.Double lowerLeft(PageLayout pageLayout);

	public abstract Color defaultColor();

	@Override
	public LabeledReference makeLabeledReference(PageLayout pageLayout, NumericFormatType numericFormatType) throws LegendCreationException {
		LabeledReferenceGradient labeledReferenceGradient = new LabeledReferenceGradient(
				createColorLegend(numericFormatType), lowerLeft(pageLayout),
				pageLayout.colorGradientDimensions());

		return labeledReferenceGradient;
	}

	private ColorLegend createColorLegend(NumericFormatType numericFormatType) throws LegendCreationException {
		try {
			double midpointOfScaledData = Averages.meanOfDoubles(
					interpolator.getInRange().getPointA(), interpolator.getInRange().getPointB());
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
