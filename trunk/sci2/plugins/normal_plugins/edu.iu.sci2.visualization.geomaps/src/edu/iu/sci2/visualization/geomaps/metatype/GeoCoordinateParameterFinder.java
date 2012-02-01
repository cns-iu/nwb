package edu.iu.sci2.visualization.geomaps.metatype;

import java.io.Serializable;
import java.util.List;

import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;

import com.google.common.collect.Ordering;

public class GeoCoordinateParameterFinder {

	public static void addLatitudeParameter(DropdownMutator mutator,
			List<String> numericColumnNames, String parameterToMutate) {
		mutator.add(parameterToMutate, new Latitudishness().reverse()
				.sortedCopy(numericColumnNames));
	}

	public static void addLongitudeParameter(DropdownMutator mutator,
			List<String> numericColumnNamesIn, String parameterToMutate) {
		mutator.add(parameterToMutate, new Longitudishness().reverse()
				.sortedCopy(numericColumnNamesIn));
	}

	protected static class Latitudishness extends Ordering<String> implements Serializable {
		private static final long serialVersionUID = -7532091564878950295L;

		public int compare(String left, String right) {
			return score(left).compareTo(score(right));
		}

		private Integer score(String s) {
			String normal = s.toLowerCase();

			if (normal.contains("latitude")) {
				return 3;
			} else if (normal.contains("lat.")) {
				return 2;
			} else if (normal.contains("lat")) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	protected static class Longitudishness extends Ordering<String> implements Serializable {
		private static final long serialVersionUID = -2121660356652701588L;

		public int compare(String left, String right) {
			return score(left).compareTo(score(right));
		}

		private Integer score(String s) {
			String normal = s.toLowerCase();

			if (normal.contains("longitude")) {
				return 3;
			} else if (normal.contains("lng")) {
				return 2;
			} else if (normal.contains("long")) {
				return 1;
			} else {
				return 0;
			}
		}
	}

}
