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
#include <math.h>

//#include <mpi.h>

/* TODO Check passing by value or reference throughout.. */

using namespace std;
using std::cerr;
using std::endl;

const int NUMBER_OF_TRAINING_STEPS = 500;
const int NUMBER_OF_JOBS = 4;
const int NUMBER_OF_STEPS_BETWEEN_UPDATES = 500;

const int NUMBER_OF_EPOCHS = 1;
const int INITIAL_WIDTH = 50;
const int FINAL_WIDTH = 1; // or 0?

// set by loadInitialNet()
int g_xdim = -1;
int g_ydim = -1;
int g_dim = -1;

// set by loadTrainingVectors()
int g_numberOfVectors = -1;

struct Node {
	int row;
	int column;
};

// TODO Used?
void scaleVector(float scalar, float vector[]) {
	for (int i = 0; i < g_dim; i++) {
		vector[i] *= scalar;
	}
}

// TODO Used?
void scaleSparseVector(float scalar, map<int, float> sparseVector) {
	for(map<int, float>::const_iterator it = sparseVector.begin(); it != sparseVector.end(); ++it) {
		sparseVector[it->first] = scalar * it->second;
	}
}

void incrementVectorBy(float vector[], float addend[]) {
	for (int i = 0; i < g_dim; i++) {
		vector[i] += addend[i];
	}
}

/* TODO For binary-valued training vectors, would could optimize by replacing the
 * map<int, float>s with some kind of bit array.
 */
int calculateNodeIndex(Node node) {
	return g_dim * (node.row * g_ydim + node.column); // TODO Triple check this index.
}

float* getWeightVector(float net[], Node node) {
	return &net[calculateNodeIndex(node)];
//	float weightVector[g_dim];
//
//	int startingIndex = calculateNodeIndex(node);
//
//	for (int i = 0; i < g_dim; i++) {
//		weightVector[i] = net[startingIndex + i];
//	}
//
//	return weightVector;
}

void setNode(float net[], Node node, float weight[]) {
	int startingIndex = calculateNodeIndex(node);

	for (int i = 0; i < g_dim; i++) {
		net[startingIndex + i] = weight[i];
	}
}

/* TODO Replace linear algebra functions with calls to BLAS. */

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

float sumOfSquaredDifferences(float vector1[], float vector2[]) {
	float sumOfSquaredDifferences = 0.0;

	for (int i = 0; i < g_dim; i++) {
		float difference = vector1[i] - vector2[i];
		sumOfSquaredDifferences += difference * difference;
	}

	return sumOfSquaredDifferences;
}

// For use between two weight vectors.
float euclideanDistance(float vector1[], float vector2[]) {
	return sqrt(sumOfSquaredDifferences(vector1, vector2));
}

/* TODO: Cosine similarity and cheap cosine similarity
 * The latter ignores sqrting the norms when only comparison to other such numbers is desired.
 */

// TODO Might revisit some optimizations Russell mentioned in the long term.
// h_(ck)(t) = exp(-||r_k - r_c||^2 / width(t)^2);
float gaussian(Node node1, Node node2, float width) {
	int rowDiff = node1.row - node2.row;
	int columnDiff = node1.column - node2.column;

	return exp(-(rowDiff*rowDiff + columnDiff*columnDiff) / (width * width));
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
float* loadInitialNet() {
	ifstream codebookFile("random.cod"); // TODO Parameterize

	if (codebookFile.is_open()) {
		// Read parameters on first line.
		string topology; // TODO Ignored for now.
		string neighborhood; // TODO Ignored for now.
		codebookFile >> g_dim >> topology >> g_xdim >> g_ydim >> neighborhood;

		float net[g_xdim * g_ydim * g_dim];

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
			setNode(net, node, weightVector);

			i++;
		}

		// TODO Debug only.
//		cout << net.g_xdim << ", " << net.g_ydim << ", " << net.g_dim << endl;
//		for (int i = 0; i < net.g_xdim * net.g_ydim; i++) {
//			Node node = net.nodes[i];
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

map<int, float>* loadMyTrainingVectors(int myRank) {
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

			// Skip lines that aren't mine.
			while (i % k != myRank && !trainingFile.eof()) {
				getline(trainingFile, line);
				i++;
			}
			if (trainingFile.eof()) {
				break;
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

Node findWinningNode(map<int, float> trainingVector, float* net) {
    /* Long term TODO: In late training (or when convergence can be presumed decent),
	 * start recording the net (i, j) for the BMU on the previous timestep or two, then
	 * search only in neighborhoods of that rather than the whole net.
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

    		float* weightVector = getWeightVector(net, node);
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

	float myNetUpdate[g_xdim * g_ydim * g_dim];
	float myDenominator[g_xdim * g_ydim];

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
				Node node;
				node.row = row;
				node.column = column;

				float gauss = gaussian(node, winningNode, width);

				// Increment equation 5 numerator.
				scaleVector(gauss, trainingVector);
				setNode(myNetUpdate, node, v);

				// Increment equation 5 denominator.
				myDenominator[row * g_xdim + column] += gauss;
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
			for (Node node;;/* in net*/) {
				// update node using eq5num/eq5den (zero-check den first).
			}

			// Reset for next "epoch"
			width = calculateWidthAtTime(t, numbersOfTimesteps);
			memset(myNetUpdate, 0, sizeof myNetUpdate); // TODO Any more efficient way?
		}
	}

	// If we had timesteps beyond the final reduce, reduce once more to pick up the stragglers.
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
		for (Node node;;/* in net*/) {
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
	float* net = loadInitialNet();

	map<int, float> trainingVectors[] = loadMyTrainingVectors(myRank);

	train(myRank, net, trainingVectors);

	// TODO Dump the net to file.

//	MPI_Finalize();

	return 0;
}
