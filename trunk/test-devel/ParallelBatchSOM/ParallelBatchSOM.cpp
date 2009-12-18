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
#include <iomanip>

//#include <mpi.h>

/* TODO Check passing by value or reference throughout.. */

using namespace std;
using std::cerr;
using std::endl;

const int NUMBER_OF_TRAINING_STEPS = 1000;
const int NUMBER_OF_JOBS = 1;
const int NUMBER_OF_STEPS_BETWEEN_UPDATES = 1000;

const int INITIAL_WIDTH = 20;
const int FINAL_WIDTH = 1; // or 0?

// set by loadInitialNet()
int g_rows = -1;
int g_columns = -1;
int g_dim = -1;

// set by loadTrainingVectors()
int g_numberOfVectors = -1;

// TODO Experiment with getting rid of Node altogether.  Benchmark first.
struct Node {
	int row;
	int column;

	Node(int r, int c) { row = r; column = c; }
};

void addScaledSparseVectorToNodeVector(float* node, float scalar, map<int, float> sparseVector) {
	for (map<int, float>::const_iterator it = sparseVector.begin(); it != sparseVector.end(); it++) {
		*(node + it->first) += scalar * (it->second);
	}

//	for (int i = 0; i < g_dim; i++) {
//		*(node + i) += scalar * sparseVector.at(i);
//	}
}

void scaleNodeVector(float* node, float scalar) {
	for (int i = 0; i < g_dim; i++) {
		*(node + i) *= scalar;
	}
}

void setNode(float* node, float weightVector[]) {
	for (int i = 0; i < g_dim; i++) {
		*(node + i) = weightVector[i];
	}
}

/* TODO For binary-valued training vectors, would could optimize by replacing the
 * map<int, float>s with some kind of bit array.
 * Or as Russell mentions, perhaps for certain degrees of sparseness we may be better served
 * by doing simple BLAS arithmetic and trusting in the L2 cache.
 */
float* calculateNodeIndex(float* net, Node node) {
	return net + (g_dim * (node.row * g_columns + node.column)); // TODO Triple check this index.
}

/* TODO Replace linear algebra functions with calls to BLAS. */

/* According to equation 8, exploiting sparseness.
 * vector is w_k and indexToOnes represents a binary-valued training vector.
 */
float distanceToSparse(float vector[], map<int, float> sparseVector) {
	float leftSum = 0.0;

	// TODO Double-check edge cases.. which side does ++ belong on?
	for (map<int, float>::const_iterator it = sparseVector.begin(); it != sparseVector.end(); it++) {
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
/* TODO Oops, this is not quite right for non-rectangular topologies.
 * Since we're assuming hexagonal, this will need to be tweaked.
 * Perhaps use the trick from SOM_PAK.
 */
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
void loadInitialNet(float** net) {
	ifstream codebookFile("random.cod"); // TODO Parameterize

	if (codebookFile.is_open()) {
		// Read parameters on first line.
		string topology; // TODO Ignored for now.
		string neighborhood; // TODO Ignored for now.
		codebookFile >> g_dim >> topology >> g_columns >> g_rows >> neighborhood; // TODO Are rows and columns really swapped?

		*net = new float(g_rows * g_columns * g_dim);

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

			Node node((int) i / g_columns, i % g_columns);
			setNode(calculateNodeIndex(*net, node), weightVector);

			i++;
		}

		// TODO Debug only.
//		cout << net.g_rows << ", " << net.g_columns << ", " << net.g_dim << endl;
//		for (int i = 0; i < net.g_rows * net.g_columns; i++) {
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

		// TODO Sanity check: i == g_rows * g_columns (- 1?)
	} else {
		cerr << "Error opening random codebook file!";
	}
}

void writeNetToFile(float* net) {
	ofstream outFile("trained.cod");

	if (outFile.is_open()) {
		outFile << g_dim << " " << "hexa" << " " << g_rows << " " << g_columns << " " << "gaussian";

		for (int row = 0; row < g_rows; row++) {
			outFile << endl;

			for (int column = 0; column < g_columns; column++) {
				Node node(row, column);
				float* nodeIndex = calculateNodeIndex(net, node);

				outFile << *nodeIndex;

				if (column < g_columns - 1) {
					outFile << " ";
				}
			}
		}
	}
}

vector<map<int, float> > loadMyTrainingVectors(int myRank) {
	ifstream trainingFile("color.dat"); // TODO Parameterize

	if (trainingFile.is_open()) {
		int dimensions;
		trainingFile >> g_numberOfVectors >> dimensions;
		if (dimensions != g_dim) { // TODO Ugh.
			cerr << "Dimensionality of the training set (" << dimensions << ") does not agree with the dimensionality of the codebook vectors (" << g_dim << ")" << endl;
			exit (1);
		}

		vector<map<int, float> > trainingVectors(g_numberOfVectors);

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
			if (i % NUMBER_OF_JOBS == myRank) {
				// Read training vector into sparse representation.
				float trainingVector[dimensions];
				istringstream iss(line);

				for (int j = 0; j < dimensions; j++) {
					string coordinateString;
					iss >> coordinateString;

					float coordinate = atof(coordinateString.c_str());
					if (coordinate != 0.0) {
						(trainingVectors.at(i))[j] = coordinate;
					}
				}
			}

			i++;
		}
		// TODO Sanity check: i == g_rows * g_columns (- 1?)

//		//TODO Debug only.
//		cout << g_numberOfVectors << ", " << dimensions << endl;
//		for(vector<map<int, float> >::const_iterator vecIt = trainingVectors.begin(); vecIt != trainingVectors.end(); vecIt++) {
//			map<int, float> trainingVector = *vecIt;
//
//			for(map<int, float>::const_iterator mapIt = trainingVector.begin(); mapIt != trainingVector.end(); mapIt++) {
//				cout << mapIt->first << " to " << mapIt->second << endl;
//			}
//
//			cout << endl;
//		}

		return trainingVectors;
	} else {
		cerr << "Error opening training data file!";
		exit (1);
	}
}

Node findWinningNode(map<int, float> trainingVector, float* net) {
    /* Long term TODO: In late training (or when convergence can be presumed decent),
	 * start recording the net (i, j) for the BMU on the previous timestep or two, then
	 * search only in neighborhoods of that rather than the whole net.
	 */
    float shortestDistance = numeric_limits<float>::max();
    Node winningNode(0, 0);

    /* TODO Take note of the comment just after equation 8: we don't need a live
	 * weight vector here; only needs to be as fresh as the latest epoch.
	 */
    for (int row = 0; row < g_rows; row++) {
    	for (int column = 0; column < g_columns; column++) {
    		Node node(row, column);

    		float distance = distanceToSparse(calculateNodeIndex(net, node), trainingVector);

    		if(distance < shortestDistance){
				shortestDistance = distance;
				winningNode = node;
    		}
    	}
    }

    // TODO Should always have a winning node now, but check it.

    return winningNode;
}

// Perform the final calculation for equation 5 and Allreduce.
void synch(float *myNumerators, vector<vector<float> > myDenominators) {
    for (int row = 0; row < g_rows; row++){
        for (int column = 0; column < g_columns; column++){
            Node node(row, column);

            scaleNodeVector(
            		calculateNodeIndex(myNumerators, node),
            		1.0 / myDenominators.at(row).at(column));

            /* TODO Put a bunch of error-reporting here
			 * for when a denominator is, inevitably, zero.
			 */
        }
    }

    /* TODO MPI.Allreduce goes here.
	 * send		myNetUpdate
	 * count	g_rows * g_columns * g_dim
	 * receive	net
	 * datatype	MPI::FLOAT?
	 * op		MPI::SUM?
	 * comm		world?  don't know. TODO
	 * Remember to check return value for error.
	 */
}

// TODO Change trainingVectors from array to vector throughout.
void train(int myRank, float* net, vector<map<int, float> > myTrainingVectors) {
	int t = 0;

	float width = INITIAL_WIDTH;

	float* myNumerators = new float[g_rows * g_columns * g_dim];
	vector<vector<float> > myDenominators(g_rows, vector<float>(g_columns));//new float[][g_columns];

	int numberOfTimesteps =
			(int) ((NUMBER_OF_TRAINING_STEPS + NUMBER_OF_JOBS - 1) / NUMBER_OF_JOBS);

	// Beware mix of 0- and 1-indexing throughout.
	for (int t = 0; t < numberOfTimesteps; t++) {
		map<int, float> trainingVector = myTrainingVectors[t % myTrainingVectors.size()]; // TODO Right?

		Node winningNode = findWinningNode(trainingVector, net);

		// Accumulate for equation 5 (exploit sparseness here, too).
		/* TODO If we do non-gaussian neighborhoods in the future, be smarter about which nodes to
		 * update.
		 */
		/* TODO Eventually, may want to change this to exploit eight-fold symmetry
		 * in circles centered on the winning node (for Gaussian neighborhoods in
		 * rectangular topology).
		 */
		for (int row = 0; row < g_rows; row++) {
			for (int column = 0; column < g_columns; column++) {
				//TODO Make a Node iterator.
				Node node(row, column);

				float gauss = gaussian(node, winningNode, width);

				// Increment equation 5 numerator.
				addScaledSparseVectorToNodeVector(
						calculateNodeIndex(net, node), gauss, trainingVector);

				// Increment equation 5 denominator.
				myDenominators.at(row).at(column) += gauss;
			}
		}

		if ((t + 1) % NUMBER_OF_STEPS_BETWEEN_UPDATES == 0) {
			synch(net, myDenominators);

//			for (Node node;;/* in net*/) {
//				// update node using eq5num/eq5den (zero-check den first).
//			}

			// Reset for next "epoch"
			width = calculateWidthAtTime(t, numberOfTimesteps);
			memset(myNumerators, 0, sizeof myNumerators); // TODO Any more efficient way?
		}
	}

	// If we had timesteps beyond the final reduce, reduce once more to pick up the stragglers.
	if ((t + 1) % NUMBER_OF_STEPS_BETWEEN_UPDATES != 0) {
		synch(net, myDenominators);
	}
}


int main(int argc, char *argv[]) {
//	MPI_Init(&argc, &argv);
//
	int myRank = 0; // TODO Remove initialization.
//	MPI_Comm_rank(MPI_COMM_WORLD, &myRank);
//	int numProcs;
//	MPI_Comm_size(MPI_COMM_WORLD, &numProcs);

	/*  Okay. The preparations have been made. */

	/* TODO Parse any command line arguments here */

	/* Might want to read the number of lines, too? and split up the file (one piece per process).
	 */
	float* net;
	loadInitialNet(&net);

	vector<map<int, float> > trainingVectors = loadMyTrainingVectors(myRank);

	train(myRank, net, trainingVectors);

	writeNetToFile(net);

//	MPI_Finalize();

	return 0;
}
