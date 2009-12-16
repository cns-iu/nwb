/*
 * ParallelBatchSOM.cpp
 * TODO
 *  Created on: Dec 16, 2009
 *      Author: jrbibers
 */

#include <iostream>
#include <vector>
#include <limits>

using namespace std;
using std::cerr;
using std::endl;

const int NUMBER_OF_EPOCHS = 1;
const int INITIAL_WIDTH = 50;
const int FINAL_WIDTH = 1;

struct Node {
	int row;
	int column;
	vector<float> weight;
};

struct Map {
	int xdim;
	int ydim;
	int weightDimension;
	/* TODO A good data structure for a rectangle of Nodes */
};

/* According to equation 8, exploiting sparseness.
 * vector is w_k and indexToOnes represents a binary-valued x.
 */
float calculateDistance(vector<float> vector, vector<int> indexToOnes) {
	float leftSum = 0.0;

	// TODO Double-check edge cases.
	for (vector<int>::iterator it = indexToOnes.begin(); it != indexToOnes.end(); it++) {
		int indexToOne = *it;

		// TODO Either correct input to be zero-indexed or use one-indexed here.
		leftSum += (1 - 2 * vector.at(indexToOne));
	}

	/* TODO Add in the right summand (either here or by the caller), which is computed per-epoch
	 * rather than per-timestep.
	 */
	float rightSum = 0;

	return leftSum + rightSum;
}

// TODO int or float?
int calculateWidthAtTime(int t, int tFinal) {
	return (int) interpolate(t, 0, tFinal, INITIAL_WIDTH, FINAL_WIDTH);
}

float interpolate(float x, float x0, float x1, float y0, float y1) {
	if (x0 == x1) {
		cerr << "Can't interpolate over an empty input range." << endl;
		exit(1);
	} else {
		return y0 + (x - x0) * (y1 - y0) / (x1 - x0);
	}
}

void train(Map* map/*TODO ?*/) {
	initializeWeightVectors(weightVectors);
	int t = 0;

	// Beware mix of 0- and 1-indexing throughout.
	for (int epoch = 1; epoch <= NUMBER_OF_EPOCHS; epoch++) {
		int width = calculateWidth(t, tFinal);

		float eq5num = 0.0;
		float eq5den = 0.0;

		/* TODO Get my piece of the training set.
		 * How?  Divide up the input file (by the number of processes) before starting even?
		 */
		for (float trainingVector[];;/* training vector in my training vectors */) {
			t++;

			/* Long term TODO: In late training (or when convergence can be presumed decent),
			 * start recording the map (i, j) for the BMU on the previous timestep or two, then
			 * search only in neighborhoods of that rather than the whole map.
			 */
			float shortestDistance = numeric_limits<float>::max();
			Node winningNode = NULL;
			// TODO Note we use equation 8 here rather than equation 6, to exploit sparseness.
			for (Node node;;/* in map */) {
				/* TODO Take note of the comment just after equation 8: we don't need a live
				 * weight vector here; only needs to be as fresh as the latest epoch.
				 */
				float distance =
						calculateDistance(
								node->weightVector, trainingVector, map->weightDimension);

				if (distance < shortestDistance) {
					shortestDistance = distance;
					winningNode = node;
				}
			}

			if (winningNode == NULL) {
				cerr << "Horrible error calculating best-matching node." << endl;
				exit (2);
			}
		}
	}
}
