/*
 * ParallelBatchSOM.cpp
 * TODO
 *  Created on: Dec 16, 2009
 *      Author: jrbibers
 */

#include <iostream>
#include <vector>
#include <limits>

//#include <mpi.h>

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
 * vector is w_k and indexToOnes represents a binary-valued training vector.
 */
float distanceToSparse(vector<float> vector, vector<int> indexToOnes) {
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

// For use between two weight vectors.
float euclideanDistance(vector<float> vector1, vector<float> vector2) {
	// Euclidean distance.
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

void initializeMap(Map map) {

}

Node findWinningNode(vector<float> trainingVector, Map *map) {
    /* Long term TODO: In late training (or when convergence can be presumed decent),
	 * start recording the map (i, j) for the BMU on the previous timestep or two, then
	 * search only in neighborhoods of that rather than the whole map.
	 */
    float shortestDistance = numeric_limits<float>::max();
    Node winningNode = NULL;
    // TODO Note we use equation 8 here rather than equation 6, to exploit sparseness.
    for(Node node; /* in map */;){
        /* TODO Take note of the comment just after equation 8: we don't need a live
		 * weight vector here; only needs to be as fresh as the latest epoch.
		 */
        float distance = distanceToSparse(node->weightVector, trainingVector, map->weightDimension);
        if(distance < shortestDistance){
            shortestDistance = distance;
            winningNode = node;
        }
    }

    if (winningNode == NULL) {
		cerr << "Horrible error calculating best-matching node." << endl;
		exit (2);
	}

    return winningNode;
}

void train(Map* map/*TODO ?*/) {
	int t = 0;

	// Beware mix of 0- and 1-indexing throughout.
	for (int epoch = 1; epoch <= NUMBER_OF_EPOCHS; epoch++) {
		int width = calculateWidth(t, tFinal);

		float eq5num = 0.0;
		float eq5den = 0.0;

		/* TODO Get my piece of the training set.
		 * How?  Divide up the input file (by the number of processes) before starting even?
		 */
		for (vector<float> trainingVector;;/* in my training vectors */) {
			t++;

			Node winningNode = findWinningNode(trainingVector, map);

			// equation 5 (exploit sparseness here, too)
			for(Node node; /* in map */;) {
				// add into eq5num and eq5den.
			}
		}

		/* TODO MPI.Allreduce goes here.
		 * send		local (per-process) eq5num and eq5den
		 * count	2
		 * receive	total eq5num and eq5den
		 * datatype	MPI::FLOAT?
		 * op		MPI::SUM?
		 * comm		world?  don't know. TODO
		 * remember to check return value for error.
		 */
		// add up the eq5nums and eq5dens from each process.

		for (Node node;;/* in map*/) {
			// update node using eq5num/eq5den (zero-check den first).
		}
	}
}


int main(int argc, char *argv[]) {
	/*  My ID number and the total number of processors: */
	int myrank, numProcs;

	/*  Variables for the computation. */
	int numberOfIntervals, interval;
	double intervalWidth, intervalMidPoint, totalArea, myArea = 0.0;

	/*  Initialize the MPI API: */
	MPI_Init(&argc, &argv);

	MPI_Comm_rank(MPI_COMM_WORLD, &myrank);
	MPI_Comm_size(MPI_COMM_WORLD, &numProcs);

	/*  Okay. The preparations have been made. */

	/* TODO Parse any command line arguments here */


	// Just one process initializes the Map.
	if (myrank == 0) {
		initializeMap(map);
	}

	MPI_Finalize();

	return 0;
}
