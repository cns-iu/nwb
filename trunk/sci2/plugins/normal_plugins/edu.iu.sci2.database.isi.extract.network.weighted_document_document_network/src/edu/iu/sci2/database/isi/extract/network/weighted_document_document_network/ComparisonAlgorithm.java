package edu.iu.sci2.database.isi.extract.network.weighted_document_document_network;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

public enum ComparisonAlgorithm {
	JaccardCoefficient {

		@Override
		public <T> float calculateSimilarity(Collection<T> c1, Collection<T> c2) {
			/*
			 * See http://en.wikipedia.org/wiki/Jaccard_coefficient for equation details
			 * 
			 * J = M11 / (M01 + M10 + M11) where M11 + M01 + M10 + M00 = n
			 */
			
			Set<T> set1 = new HashSet<T>(c1);
			Set<T> set2 = new HashSet<T>(c2);
			
			Set<T> M11 = Sets.intersection(set1, set2);
			Set<T> M01 = Sets.difference(set2, set1);
			Set<T> M10 = Sets.difference(set1, set2);
			
			float J = (float) M11.size() / (float) (M01.size() + M10.size() + M11.size());
			return J;
		}

	}, CosineSimilarity {

		/**
		 * This will calculate the cosine similarity of any two collections using the 
		 * formula given at http://en.wikipedia.org/wiki/Cosine_similarity.
		 * @param c1
		 * @param c2
		 * @return The result of the cosine similarity algorithm as a float.
		 */
		@Override
		public <T> float calculateSimilarity(Collection<T> c1, Collection<T> c2) {

			final float matchValue = 1.0f;
			final float missValue = 0.0f;

			Set<T> values = new HashSet<T>(c1.size());
			values.addAll(c1);
			values.addAll(c2);

			float numerator = 0.0f;
			float aSquaredSum = 0.0f;
			float bSquaredSum = 0.0f;
			
			for (T value : values) {
				float c1Value = missValue;
				float c2Value = missValue;

				if (c1.contains(value)) {
					c1Value = matchValue;
				}

				if (c2.contains(value)) {
					c2Value = matchValue;
				}

				assert !(missValue == c1Value && missValue == c2Value);

				float a = c1Value;
				float b = c2Value;

				numerator += (a * b);
				aSquaredSum += Math.pow(a, 2);
				bSquaredSum += Math.pow(b, 2);
			}

			
			float denominator = (float) (Math.sqrt(aSquaredSum) * Math
					.sqrt(bSquaredSum));

			float similarity = numerator / denominator;

			assert similarity <= 1.0f;
			assert similarity >= -1.0f;

			return similarity;
		}
	}, SørensenSimilarityIndex {
		@Override
		public <T> float calculateSimilarity(Collection<T> c1, Collection<T> c2) {
			/*
			 * See http://en.wikipedia.org/wiki/S%C3%B8rensen_similarity_index for
			 * equation details
			 * 
			 * QS = 2C / (A + B)
			 */
			Set<T> A = new HashSet<T>(c1);
			Set<T> B = new HashSet<T>(c2);

			Set<T> C = Sets.intersection(A, B);

			float QS = ((float) 2 * C.size()) / ((float) A.size() + B.size());
			return QS;
		}
	};
	
	public abstract <T> float calculateSimilarity(Collection<T> c1, Collection<T> c2);

}
