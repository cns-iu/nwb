package edu.iu.sci2.visualization.geomaps.viz.coding;

import java.awt.Color;
import java.util.ArrayList;

import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;

import edu.iu.sci2.visualization.geomaps.data.interpolation.Interpolator;
import edu.iu.sci2.visualization.geomaps.data.scaling.ScalingException;
import edu.iu.sci2.visualization.geomaps.utility.Averages;
import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.CircleDimension;
import edu.iu.sci2.visualization.geomaps.viz.Constants;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;
import edu.iu.sci2.visualization.geomaps.viz.legend.ColorLegend;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReferenceGradient;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.NumberFormatFactory.NumericFormatType;
import edu.iu.sci2.visualization.geomaps.viz.ps.PostScriptable;

public abstract class AbstractColorCoding<D extends Enum<D> & VizDimension> extends AbstractCoding<D, Color> {

	public AbstractColorCoding(Binding<D> binding, Range<Double> usableRange, Interpolator<Color> interpolator) {
		super(binding, usableRange, interpolator);
	}

	public abstract double lowerLeftX();

	public abstract double lowerLeftY();

	public abstract String legendDescription();

	public abstract Color defaultColor();

	@Override
	public PostScriptable makeLabeledReference(NumericFormatType numericFormatType) throws LegendCreationException {
		LabeledReferenceGradient labeledReferenceGradient = new LabeledReferenceGradient(
				createColorLegend(numericFormatType), lowerLeftX(), lowerLeftY(),
				CircleDimension.INNER_COLOR_GRADIENT_WIDTH,
				CircleDimension.INNER_COLOR_GRADIENT_HEIGHT);

		return labeledReferenceGradient;
	}

	private ColorLegend createColorLegend(NumericFormatType numericFormatType) throws LegendCreationException {
		try {
			double midpointOfScaledData = Averages.meanOfDoubles(
					interpolator.inRange().pointA(), interpolator.inRange().pointB());
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
		mutator.add(rangeParameterId, new ArrayList<String>(Constants.COLOR_RANGES.keySet()));
	}
}
