/*
 * ParallelBatchSOM.cpp
 * TODO
 *  Created on: Dec 16, 2009
 *      Author: jrbibers
 */

#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <vector>
#include <limits>
#include <map>

//#include <mpi.h>

using namespace std;
using std::cerr;
using std::endl;

const int NUMBER_OF_TRAINING_STEPS = 500;
const int NUMBER_OF_JOBS = 4;
const int NUMBER_OF_STEPS_BETWEEN_UPDATES = 500;

const int NUMBER_OF_EPOCHS = 1;
const int INITIAL_WIDTH = 50;
const int FINAL_WIDTH = 1; // or 0?

// set by loadInitialMap()
int g_xdim = -1;
int g_ydim = -1;
int g_dim = -1;

// set by loadTrainingVectors()
int g_numberOfVectors = -1;

struct Node {
	int row;
	int column;
};

/* TODO For binary-valued training vectors, would could optimize by replacing the
 * map<int, float>s with some kind of bit array.
 */
int calculateNodeIndex(Node node) {
	return g_dim * (node.row * g_ydim + node.column); // TODO Triple check this index.
}

float* getWeightVector(float map[], Node node) {
	return &map[calculateNodeIndex(node)];
//	float weightVector[g_dim];
//
//	int startingIndex = calculateNodeIndex(node);
//
//	for (int i = 0; i < g_dim; i++) {
//		weightVector[i] = map[startingIndex + i];
//	}
//
//	return weightVector;
}

void setNode(float map[], Node node, float weight[]) {
	int startingIndex = calculateNodeIndex(node);

	for (int i = 0; i < g_dim; i++) {
		map[startingIndex + i] = weight[i];
	}
}

/* According to equation 8, exploiting sparseness.
 * vector is w_k and indexToOnes represents a binary-valued training vector.
 */
float distanceToSparse(float vector[], map<int, float> sparseVector) {
	float leftSum = 0.0;

	// TODO Double-check edge cases.. which side does ++ belong on?
	for (map<int, float>::const_iterator it = sparseVector.begin(); it != sparseVector.end(); ++it) {
		int sparseIndex = it->first;
		float sparseValue = it->second;

		// TODO Either correct input to be zero-indexed or use one-indexed here.
		leftSum += sparseValue * (sparseValue - 2 * vector[sparseIndex]);
	}

	/* TODO Add in the right summand (either here or by the caller), which is computed per-epoch
	 * rather than per-timestep.
	 */
	float rightSum = 0.0;

	return leftSum + rightSum;
}

// For use between two weight vectors.
float euclideanDistance(float vector1[], float vector2[]) {
	// TODO
}

// Euclidean distance, not bothering to sqrt when finding the norms.
// Only for comparing two weight vectors.
float cheapEuclideanishDistance(float vector1[], float vector2[]) {
	// TODO
}

// TODO Might revisit some optimizations Russell mentioned in the long term.
// h_(ck)(t) = exp(-||r_k - r_c||^2 / width(t)^2);
float gaussian(float vector1[], float vector2[], float width) {

}

float interpolate(float x, float x0, float x1, float y0, float y1) {
	if (x0 == x1) {
		cerr << "Can't interpolate over an empty input range." << endl;
		exit(1);
	} else {
		return y0 + (x - x0) * (y1 - y0) / (x1 - x0);
	}
}

// TODO int or float?
float calculateWidthAtTime(int t, int tFinal) {
	return interpolate(t, 0, tFinal, INITIAL_WIDTH, FINAL_WIDTH);
}

// TODO Split this out into its own script.
float* loadInitialMap() {
	ifstream codebookFile("random.cod"); // TODO Parameterize

	if (codebookFile.is_open()) {
		// Read parameters on first line.
		string topology; // TODO Ignored for now.
		string neighborhood; // TODO Ignored for now.
		codebookFile >> g_dim >> topology >> g_xdim >> g_ydim >> neighborhood;

		float map[g_xdim * g_ydim * g_dim];

		// Read the codebook.
		string line;
		int i = 0;
		bool onFirstLine = true;
		while (!codebookFile.eof()) {
			getline(codebookFile, line);
			// Skip first line.
			if (onFirstLine) {
				getline(codebookFile, line);
				onFirstLine = false;
			}

			// Read weight vector.
			float weightVector[g_dim];
			istringstream iss(line);
			for (int j = 0; j < g_dim; j++) {
				string coordinateString;
				iss >> coordinateString;

				float coordinate = atof(coordinateString.c_str());
				weightVector[j] = coordinate;
			}

			Node node;
			node.row = (int) i / g_ydim;
			node.column = i % g_ydim;
			setNode(map, node, weightVector);

			i++;
		}

		// TODO Debug only.
//		cout << map.g_xdim << ", " << map.g_ydim << ", " << map.g_dim << endl;
//		for (int i = 0; i < map.g_xdim * map.g_ydim; i++) {
//			Node node = map.nodes[i];
//			cout << node.row << ", " << node.column << endl;
//			vector<float> weightVector = node.weightVector;
//
//			vector<float>::const_iterator cii;
//			for (cii = weightVector.begin(); cii != weightVector.end(); cii++) {
//				cout << *cii << " ";
//			}
//			cout << endl;
//		}

		// TODO Sanity check: i == g_xdim * g_ydim (- 1?)
	} else {
		cerr << "Error opening random codebook file!";
	}
}

map<int, float>* loadTrainingVectors() {
	ifstream trainingFile("dummy.dat"); // TODO Parameterize

	if (trainingFile.is_open()) {
		int numberOfVectors;
		int dimensions;
		trainingFile >> g_numberOfVectors >> dimensions;
		if (dimensions != g_dim) { // TODO Ugh.
			cerr << "Dimensionality of the training set (" << dimensions << ") does not agree with the dimensionality of the codebook vectors (" << g_dim << ")" << endl;
		}

		map<int, float> trainingVectors[numberOfVectors];

		string line;
		bool onFirstLine = true;
		int i = 0;
		while (!trainingFile.eof()) {
			getline(trainingFile, line);
			// Skip first line.
			if (onFirstLine) {
				getline(trainingFile, line);
				onFirstLine = false;
			}

			// Read training vector into sparse representation.
			float trainingVector[dimensions];
			istringstream iss(line);
			for (int j = 0; j < dimensions; j++) {
				string coordinateString;
				iss >> coordinateString;

				float coordinate = atof(coordinateString.c_str());
				if (coordinate != 0.0) {
					trainingVectors[i][j] = coordinate;
				}
			}

			i++;
		}

		// TODO Debug only.
		cout << numberOfVectors << ", " << dimensions << endl;
		for (int i = 0; i < numberOfVectors; i++) {
			map<int, float> trainingVector = trainingVectors[i];

			for(map<int, float>::const_iterator it = trainingVector.begin(); it != trainingVector.end(); ++it) {
				cout << i << ": " << it->first << " to " << it->second << endl;
			}

			cout << endl;
		}

		// TODO Sanity check: i == g_xdim * g_ydim (- 1?)
	} else {
		cerr << "Error opening random codebook file!";
	}
}

Node findWinningNode(map<int, float> trainingVector, float* map) {
    /* Long term TODO: In late training (or when convergence can be presumed decent),
	 * start recording the map (i, j) for the BMU on the previous timestep or two, then
	 * search only in neighborhoods of that rather than the whole map.
	 */
    float shortestDistance = numeric_limits<float>::max();
    Node winningNode;

    /* TODO Take note of the comment just after equation 8: we don't need a live
	 * weight vector here; only needs to be as fresh as the latest epoch.
	 */
    for (int row = 0; row < g_xdim; row++) {
    	for (int column = 0; column < g_ydim; column++) {
    		Node node;
    		node.row = row;
    		node.column = column;

    		float* weightVector = getWeightVector(map, node);
    		float distance = distanceToSparse(weightVector, trainingVector);

    		if(distance < shortestDistance){
				shortestDistance = distance;
				winningNode = node;
    		}
    	}
    }

    // TODO Should always have a winning node now, but check it.

    return winningNode;
}

void train(int myRank, float* net, map<int, float>* trainingVectors) {
	int t = 0;

	float width = INITIAL_WIDTH;
	float eq5num[g_dim];
	float eq5den = 0.0;

	int numbersOfTimesteps =
			(int) ((NUMBER_OF_TRAINING_STEPS + NUMBER_OF_JOBS - 1) / NUMBER_OF_JOBS);

	// Beware mix of 0- and 1-indexing throughout.
	for (int t = 0; t < numbersOfTimesteps; t++) {
		/* TODO Get my piece of the training set.
		 * How?  Divide up the input file (by the number of processes) before starting even?
		 * Or index modularly into one big file?
		 */
		map<int, float> trainingVector = trainingVectors[(myRank + k * t) % g_numberOfVectors]; // TODO ?

		Node winningNode = findWinningNode(trainingVector, net);

		// accumulate for equation 5 (exploit sparseness here, too)
		/* TODO If we do non-gaussian neighborhoods in the future, be smarter about which nodes to
		 * update.
		 */
		for (int row = 0; row < g_xdim; row++) {
			for (int column = 0; column < g_ydim; column++) {
				float distance =
						gaussian(
								getWeightVector(net, node),
								getWeightVector(net, winningNode),
								width);

				// TODO increment eq5num
				// TODO increment eq5den
			}
		}

		if ((t + 1) % NUMBER_OF_STEPS_BETWEEN_UPDATES == 0) {
			/* TODO MPI.Allreduce goes here.
			 * Add up the eq5nums and eq5dens from each process.
			 * send		local (per-process) eq5num and eq5den
			 * count	2
			 * receive	total eq5num and eq5den
			 * datatype	MPI::FLOAT?
			 * op		MPI::SUM?
			 * comm		world?  don't know. TODO
			 * remember to check return value for error.
			 */
			for (Node node;;/* in map*/) {
				// update node using eq5num/eq5den (zero-check den first).
			}

			// Reset for next "epoch"
			width = calculateWidthAtTime(t, numbersOfTimesteps);
			float eq5num[g_dim];
			float eq5den = 0.0;
		}
	}

	if ((t + 1) % NUMBER_OF_STEPS_BETWEEN_UPDATES != 0) {
		/* TODO MPI.Allreduce goes here.
		 * Add up the eq5nums and eq5dens from each process.
		 * send		local (per-process) eq5num and eq5den
		 * count	2
		 * receive	total eq5num and eq5den
		 * datatype	MPI::FLOAT?
		 * op		MPI::SUM?
		 * comm		world?  don't know. TODO
		 * remember to check return value for error.
		 */
		for (Node node;;/* in map*/) {
			// update node using eq5num/eq5den (zero-check den first).
		}

		width = calculateWidthAtTime(t, numbersOfTimesteps); // TODO Could hardcode minimum width here (1?)
	}
}


int main(int argc, char *argv[]) {
//	MPI_Init(&argc, &argv);
//
//	int myRank;
//	MPI_Comm_rank(MPI_COMM_WORLD, &myRank);
//	int numProcs;
//	MPI_Comm_size(MPI_COMM_WORLD, &numProcs);

	/*  Okay. The preparations have been made. */

	/* TODO Parse any command line arguments here */

	/* Might want to read the number of lines, too? and split up the file (one piece per process).
	 */
	float* map = loadInitialMap();

	vector<int, float> trainingVectors[] = loadTrainingVectors();

	train(myRank, map, trainingVectors);

	// TODO Dump the map to file.

//	MPI_Finalize();

	return 0;
}
