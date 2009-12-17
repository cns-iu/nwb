/*
 * ParallelBatchSOM.cpp
 * TODO
 *  Created on: Dec 16, 2009
 *      Author: jrbibers
 */

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <limits>

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


struct Node {
	int row;
	int column;
	vector<float> weightVector;
};

struct Map {
	int xdim;
	int ydim;
	int weightDimension;
	Node nodes[];
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

// Only for comparing two weight vectors.
float cheapEuclideanDistance(vector<float> vector1, vector<float> vector2) {
	// Euclidean distance, not bothering to sqrt when finding the norms.
}

// TODO Might revisit some optimizations Russell mentioned in the long term.
// h_(ck)(t) = exp(-||r_k - r_c||^2 / width(t)^2);
float gaussian(vector<float> vector1, vector<float> vector2, float width) {

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
Map loadInitialMap() {
	ifstream codebookFile("random.cod"); // TODO Parameterize

	if (codebookFile.is_open()) {
		// Read parameters on first line.
		int weightdim;
		string topology; // TODO Ignored for now.
		int xdim;
		int ydim;
		string neighborhood; // TODO Ignored for now.
		codebookFile >> weightdim >> topology >> xdim >> ydim >> neighborhood;

		Map map;
		map.xdim = xdim;
		map.ydim = ydim;
		map.weightdim = weightdim;

		Node nodes[xdim * ydim];
		map.nodes = nodes;

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
			vector<float> weightVector(weightdim);
			istringstream iss(line);
			for (int j = 0; j < weightdim; j++) {
				string coordinateString;
				iss >> coordinateString;

				float coordinate = atof(coordinateString.c_str());
				weightVector.at(j) = coordinate;
			}

			Node node;
			node.row = (int) i / ydim;
			node.column = i % ydim;
			node.weightVector = weightVector;

			map.nodes[i] = node;

			i++;
		}

		// TODO Debug only.
//		cout << map.xdim << ", " << map.ydim << ", " << map.weightdim << endl;
//		for (int i = 0; i < map.xdim * map.ydim; i++) {
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

		// TODO Sanity check: i == xdim * ydim (- 1?)
	} else {
		cerr << "Error opening random codebook file!";
	}
}

Node findWinningNode(vector<int> trainingVector, Map *map) {
    /* Long term TODO: In late training (or when convergence can be presumed decent),
	 * start recording the map (i, j) for the BMU on the previous timestep or two, then
	 * search only in neighborhoods of that rather than the whole map.
	 */
    float shortestDistance = numeric_limits<float>::max();
    Node winningNode;
    // TODO Note we use equation 8 here rather than equation 6, to exploit sparseness.
    for(Node node; /* in map */;){
        /* TODO Take note of the comment just after equation 8: we don't need a live
		 * weight vector here; only needs to be as fresh as the latest epoch.
		 */
        float distance = distanceToSparse(node.weightVector, trainingVector);
        if(distance < shortestDistance){
            shortestDistance = distance;
            winningNode = node;
        }
    }

    // TODO Should always have a winning node now, but check it.

    return winningNode;
}

void train(Map* map) {
	int t = 0;
	int dimension = map->weightDimension;

	float width = INITIAL_WIDTH;
	vector<float> eq5num(dimension);
	float eq5den = 0.0;

	int numbersOfTimesteps =
			(int) ((NUMBER_OF_TRAINING_STEPS + NUMBER_OF_JOBS - 1) / NUMBER_OF_JOBS);
	// Beware mix of 0- and 1-indexing throughout.
	for (int t = 0; t < numbersOfTimesteps; t++) {
		/* TODO Get my piece of the training set.
		 * How?  Divide up the input file (by the number of processes) before starting even?
		 * Or index modularly into one big file?
		 */
		vector<int> trainingVector; // = TODO

		Node winningNode = findWinningNode(trainingVector, map);

		// equation 5 (exploit sparseness here, too)
		for (Node node; /* in map */;) {
			float distance =
					gaussian(node.weightVector, winningNode.weightVector, calculateWidthAtTime(t));

			// TODO increment eq5num
			// TODO increment eq5den
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
			vector<float> eq5num(dimension);
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

		width = calculateWidthAtTime(t, numbersOfTimesteps); // TODO Could hardcode minimum width (1?)
	}

//	// Beware mix of 0- and 1-indexing throughout.
//	for (int epoch = 1; epoch <= NUMBER_OF_EPOCHS; epoch++) {
//		int width = calculateWidth(t, tFinal);
//
//		vector<float> eq5num(dimension);
//		float eq5den = 0.0;
//
//		/* TODO Get my piece of the training set.
//		 * How?  Divide up the input file (by the number of processes) before starting even?
//		 */
//		for (vector<float> trainingVector;;/* in my training vectors */) {
//			t++;
//
//			Node winningNode = findWinningNode(trainingVector, map);
//
//			// equation 5 (exploit sparseness here, too)
//			for (Node node; /* in map */;) {
//				// add into eq5num and eq5den.
//				float distance = gaussian(node->weightVector, winningNode, calculateWidthAtTime(t));
//
//				eq5num +=
//			}
//		}
//
//		/* TODO MPI.Allreduce goes here.
//		 * Add up the eq5nums and eq5dens from each process.
//		 * send		local (per-process) eq5num and eq5den
//		 * count	2
//		 * receive	total eq5num and eq5den
//		 * datatype	MPI::FLOAT?
//		 * op		MPI::SUM?
//		 * comm		world?  don't know. TODO
//		 * remember to check return value for error.
//		 */
//
//		for (Node node;;/* in map*/) {
//			// update node using eq5num/eq5den (zero-check den first).
//		}
//	}
}


int main(int argc, char *argv[]) {
	MPI_Init(&argc, &argv);

	int myRank;
	MPI_Comm_rank(MPI_COMM_WORLD, &myRank);
	int numProcs;
	MPI_Comm_size(MPI_COMM_WORLD, &numProcs);

	/*  Okay. The preparations have been made. */

	/* TODO Parse any command line arguments here */

	/* Might want to read the number of lines, too? and split up the file (one piece per process).
	 */
	Map map = loadInitialMap();

	train(&map);

	MPI_Finalize();

	return 0;
}
