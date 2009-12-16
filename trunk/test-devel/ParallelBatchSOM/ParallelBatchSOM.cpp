/*
 * ParallelBatchSOM.cpp
 * TODO
 *  Created on: Dec 16, 2009
 *      Author: jrbibers
 */

#include <iostream>
#include <limits>

using std::cerr;
using std::endl;

const int NUMBER_OF_EPOCHS = 1;
const int INITIAL_WIDTH = 50;
const int FINAL_WIDTH = 1;

struct Map {
	int xdim;
	int ydim;
/* Map data structure:
 * 	weight vectors (a rectangle of dense, float-valued vectors)
 *
 */
};



void train(Map* map/*TODO ?*/) {
	initializeWeightVectors(weightVectors);
	int t = 0;

	// Beware mix of 0- and 1-indexing throughout.
	for (int epoch = 1; epoch <= NUMBER_OF_EPOCHS; epoch++) {
		int width = calculateWidth(t, tFinal);

		float eq5num = 0.0;
		float eq5den = 0.0;

		/* TODO Get my piece of the training set.
		 * How?  Divide up the input file by the number of processed before starting even?
		 */
		for (;;/* training vector in my training vectors */) {
			t++;

			float shortestDistance = numeric_limits<float>::max();
			// TODO Note we use equation 8 here rather than equation 6, to exploit sparseness.
			for (;;/* weight vector in my map */) {

			}
		}
	}
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
