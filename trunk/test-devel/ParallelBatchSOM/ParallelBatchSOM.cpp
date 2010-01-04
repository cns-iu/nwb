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
#include <time.h> // TODO Add time reporting at all of the critical couts.

#include "mpi.h"

using namespace std;
using std::cerr;
using std::endl;

typedef vector< map<int, float> > training_data_t;

/* TODO Put these numbers together in a way that ensures we always finish training
 * with a net update after a full batch.
 */
const int NUMBER_OF_TRAINING_STEPS = 500000;
const int NUMBER_OF_STEPS_BETWEEN_UPDATES = 1000;

const int INITIAL_WIDTH = 250; // TODO Calculate this from xdim and ydim?  Command line argument?
const int FINAL_WIDTH = 1; // TODO Or what?

// set by loadInitialNet()
int g_rows = -1;
int g_columns = -1;
int g_dim = -1;

int g_numberOfJobs = 1;

// set by loadTrainingVectors()
int g_numberOfVectors = -1;

float* g_myNumerators;
float* g_myNumerators2;
float* g_myDenominators;
float* g_myDenominators2;

// TODO Experiment with getting rid of Coordinate altogether.  Benchmark first.
struct Coordinate {
	int row;
	int column;

	Coordinate(int r, int c) { row = r; column = c; }
};



void zeroOut(float* array, int size) {
	for (int i = 0; i < size; i++) {
		array[i] = 0.0;
	}
}

void addScaledSparseVectorToNodeVector(float* node, float scalar, map<int, float> sparseVector) {
	for (map<int, float>::const_iterator it = sparseVector.begin(); it != sparseVector.end(); it++) {
		node[it->first] += scalar * (it->second);
	}
}

void scaleNodeVector(float* node, float* numerator, float scalar) {
	for (int i = 0; i < g_dim; i++) {
		node[i] = numerator[i] * scalar;
	}
}

/* For binary-valued training vectors, would could optimize by replacing the
 * map<int, float>s with some kind of bit array.
 * As Russell mentions, perhaps for certain degrees of sparseness we may be better served
 * by doing simple BLAS arithmetic and trusting in the L2 cache.
 */
float* calculateNodeIndex(float* net, Coordinate coord) {
	return net + (g_dim * (coord.row * g_columns + coord.column));
}

/* TODO Replace linear algebra functions with calls to BLAS? */

/* According to equation 8, exploiting sparseness.
 * vector is w_k and indexToOnes represents a binary-valued training vector.
 */
float distanceToSparse(float* vector, map<int, float> sparseVector, float recentSquaredNorm) {
	float leftSum = 0.0;

	for (map<int, float>::const_iterator it = sparseVector.begin(); it != sparseVector.end(); it++) {
		int sparseIndex = it->first;
		float sparseValue = it->second;

		leftSum += sparseValue * (sparseValue - 2 * vector[sparseIndex]);
	}

	float rightSum = recentSquaredNorm;

	if (leftSum + rightSum < 0) {
		cout << "leftSum = " << leftSum << "; rightSum = " << rightSum << endl;
	}

	return (leftSum + rightSum);
}

/* TODO: Test this.
 * TODO: Also implement cheap cosine similarity.
 * Will ignore sqrting the norms when only comparison to other such numbers is desired.
 * Could even pull out the trainingVector norm to be calculated only once per epoch.
 */
float calculateCosineSimilarity(float* netVector, map<int, float> sparseVector) {
	float dotProduct = 0.0;
	float sparseVectorSquaredNorm = 0.0;

	for (map<int, float>::const_iterator it = sparseVector.begin(); it != sparseVector.end(); it++) {
		int sparseIndex = it->first;
		float sparseValue = it->second;

		dotProduct += sparseValue * netVector[sparseIndex];
		sparseVectorSquaredNorm += sparseValue * sparseValue;
	}


	float netVectorSquaredNorm = 0.0;

	for (int weightIndex = 0; weightIndex < g_dim; weightIndex++) {
		float coordinate = netVector[weightIndex];
		netVectorSquaredNorm += coordinate * coordinate;
	}


	return (dotProduct / (sqrt(sparseVectorSquaredNorm) * sqrt(netVectorSquaredNorm)));
}

// From SOM_PAK
float hexa_dist(int bx, int by, int tx, int ty){
	float ret, diff;

	diff = bx - tx;

	if (((by - ty) % 2) != 0) {
		if ((by % 2) == 0) {
			diff -= 0.5;
		}
		else {
			diff += 0.5;
		}
	}

	ret = diff * diff;
	diff = by - ty;
	ret += 0.75 * diff * diff;

	return ret;
}

/* TODO Might revisit some optimizations Russell mentioned in the long term.
 * One is that we could exploit the circular symmetry of the gaussian to reduce the number
 * of calls to this function eightfold (for rectangular topologies; not certain about the reduction
 * for hexagonal).
 * Another is that we could calculate the gaussian only once per batch (that is,
 * only once per width interpolation).
 */
float gaussian(Coordinate coord1, Coordinate coord2, float width) {
	return exp(-hexa_dist(coord1.row, coord1.column, coord2.row, coord2.column) / (width * width));
}

float interpolate(float x, float x0, float x1, float y0, float y1) {
	if (x0 == x1) {
		cerr << "Can't interpolate over an empty input range." << endl;
		exit(1);
	} else {
		return y0 + (x - x0) * (y1 - y0) / (x1 - x0);
	}
}

float calculateWidthAtTime(int t, int tFinal) {
	return interpolate(t, 0, tFinal, INITIAL_WIDTH, FINAL_WIDTH);
}

float* loadInitialNet() {
	cout << "Loading initial codebook.. ";

	float* net;

	ifstream codebookFile("random.cod"); // TODO Parameterize
	if (codebookFile.is_open()) {
		// Read parameters on first line.
		string topology; // Ignored for now.
		string neighborhood; // Ignored for now.
		// TODO The rows and columns seem backwards here.  Ensure it works out.
		codebookFile >> g_dim >> topology >> g_columns >> g_rows >> neighborhood;

		int numberOfNodes = g_rows * g_columns;
		net = new float[numberOfNodes * g_dim];

		// TODO Check eof during and after, perhaps.
		for (int i = 0; i < numberOfNodes; i++) {
			float* currentNode = net + i * g_dim;

			for (int weightIndex = 0; weightIndex < g_dim; weightIndex++) {
				codebookFile >> currentNode[weightIndex];
			}
		}
	} else {
		cerr << "Error opening random codebook file!";
	}

	cout << "Done." << endl;

	return net;
}

void writeNetToFile(float* net) {
	cout << "Writing codebook to file.. ";

	ofstream outFile("trained.cod");
	if (outFile.is_open()) {
		outFile << g_dim << " " << "hexa" << " " << g_columns << " " << g_rows << " " << "gaussian";

		int numberOfNodes = g_rows * g_columns;

		for (int i = 0; i < numberOfNodes; i++) {
			int nodeIndex = i * g_dim;

			outFile << endl;

			for (int weightIndex = 0; weightIndex < g_dim; weightIndex++) {
				outFile << net[nodeIndex + weightIndex] << " ";
			}
		}
	} else {
		cerr << "Error opening output codebook file.";
		exit (4);
	}

	cout << "Done." << endl;
}

training_data_t* loadMyTrainingVectorsFromDense(int myRank) {
	cout << "Loading training vectors (from dense representation).. ";

	ifstream trainingFile("color.dat"); // TODO Parameterize

	if (trainingFile.is_open()) {

		int dimensions;
		trainingFile >> g_numberOfVectors >> dimensions;
		if (dimensions != g_dim) { // TODO Ugh.
			cerr << "Dimensionality of the training set (" << dimensions << ") does not agree with the dimensionality of the codebook vectors (" << g_dim << ")." << endl;
			exit (1);
		}

		training_data_t* trainingVectors = new training_data_t();

		int vectorLineNumber = 0;
		while (!trainingFile.eof()) {

			// Only read lines assigned to my rank.
			if (vectorLineNumber % g_numberOfJobs == myRank) {
				map<int, float> trainingVector;

				for (int j = 0; j < dimensions; j++) {
					float coordinate;
					trainingFile >> coordinate;

					if (coordinate != 0.0) {
						trainingVector[j] = coordinate;
					}
				}

				trainingVectors->push_back(trainingVector);
			}

			vectorLineNumber++;
		}
		// TODO Sanity check: i == g_rows * g_columns (- 1?)

		cout << "Done." << endl;

		return trainingVectors;
	} else {
		cerr << "Error opening training data file!";
		exit (1);
	}
}

// Assumes that the file contains lines which give indices to non-zeroes in a binary-valued vector.
training_data_t* loadMyTrainingVectorsFromSparse(int myRank) {
	cout << "Loading training vectors (from sparse representation).. ";

	ifstream trainingFile("color.dat"); // TODO Parameterize

	if (trainingFile.is_open()) {
		training_data_t* trainingVectors = new training_data_t();

		int lineNumber = 0;
		string line;
		while (getline(trainingFile, line)) {
			// Only read lines assigned to my rank.
			if (lineNumber % g_numberOfJobs == myRank) {
				istringstream linestream(line);

				// Read and ignore document ID.
				int documentID;
				linestream >> documentID;

				/* TODO Could be mapping into ints, or shorts, or bools even.
				 * Or even some nice bitset instead of a map.
				 */
				map<int, float> trainingVector;

				/* ===============================================================================================
				 * TODO Halt all further testing until all questions of one-vs-zero based indexing
				 * and whether a documentID is given are resolved!!
				 * ===============================================================================================
				 */
				int oneBasedIndex;
				while (linestream >> oneBasedIndex) {
					int zeroBasedIndex = oneBasedIndex - 1;

					if (zeroBasedIndex < g_dim) {
						trainingVector[zeroBasedIndex] = 1;
					} else {
						cerr << "Dimensionality of training vectors exceeds that of the codebook." << endl;
						exit (1);
					}
				}

				trainingVectors->push_back(trainingVector);
			}

			lineNumber++;
		}
		// TODO Sanity check: i == g_rows * g_columns (- 1?)

//		// TODO Debug only.
//		for (vector<map<int, float> >::const_iterator vecIt = trainingVectors->begin(); vecIt != trainingVectors->end(); vecIt++) {
//			map<int, float> m = *vecIt;
//
//			for (map<int, float>::const_iterator it = m.begin(); it != m.end(); it++) {
//				cout << it->first << ", " << it->second << endl;
//			}
//
//			cout << endl;
//		}

		cout << "Done." << endl;

		return trainingVectors;
	} else {
		cerr << "Error opening training data file!";
		exit (1);
	}
}

Coordinate findWinner(map<int, float> trainingVector, float* net, float* recentSquaredNorms) {
    /* Long term TODO: In late training (or when convergence can be presumed decent),
	 * start recording the net (i, j) for the BMU on the previous timestep or two, then
	 * search only in neighborhoods of that rather than the whole net.
	 */
    float shortestDistance = numeric_limits<float>::max();
    Coordinate winner(0, 0);

    for (int row = 0; row < g_rows; row++) {
    	for (int column = 0; column < g_columns; column++) {
    		Coordinate coord(row, column);

    		float recentSquaredNorm = recentSquaredNorms[(row * g_columns) + column];

    		float distance =
    				distanceToSparse(calculateNodeIndex(net, coord), trainingVector, recentSquaredNorm);

    		if (distance < shortestDistance) {
				shortestDistance = distance;
				winner = coord;
    		}
    	}
    }

    return winner;
}

// Allreduce and perform the final calculation for equation 5.
void calculateNewNet(float* net) {
	cout << "Synchronizing.. ";

	int numberOfNodes = g_rows * g_columns;
	int flatSize = numberOfNodes * g_dim;

	float* numTemp = g_myNumerators;
	float* denTemp = g_myDenominators;

	// TODO Investigate MPI error handling in C++.
	// TODO Think about being trickier with in-place swaps.
    MPI::COMM_WORLD.Allreduce(g_myNumerators, g_myNumerators2, flatSize, MPI::FLOAT, MPI::SUM);
    MPI::COMM_WORLD.Allreduce(g_myDenominators, g_myDenominators2, numberOfNodes, MPI::FLOAT, MPI::SUM);

    g_myNumerators = g_myNumerators2;
    g_myNumerators2 = numTemp;

    g_myDenominators = g_myDenominators2;
    g_myDenominators2 = denTemp;

    for (int row = 0; row < g_rows; row++){
		for (int column = 0; column < g_columns; column++){
			Coordinate coord(row, column);

			scaleNodeVector(
					calculateNodeIndex(net, coord),
					calculateNodeIndex(g_myNumerators, coord),
					(1.0 / g_myDenominators[(row * g_columns) + column]));

			/* TODO Put a bunch of error-reporting here
			 * for when a denominator is, inevitably, zero.
			 */
		}
	}

    cout << "Done." << endl;
}

void calculateSquaredNorms(float* oldSquaredNorms, float* net) {
	int numberOfNodes = g_rows * g_columns;

	for (int nodeIndex = 0; nodeIndex < numberOfNodes; nodeIndex++) {
		float* currentNode = net + nodeIndex * g_dim;
		float* currentNorm = oldSquaredNorms + nodeIndex;

		for (int weightIndex = 0; weightIndex < g_dim; weightIndex++) {
			float weight = currentNode[weightIndex];

			(*currentNorm) += weight * weight;
		}
	}
}

/* TODO Periodically check the quantization error (and maybe even write out the codebook).
 * Not more often than every 15 minutes or so.
 * Could even append qerrors out to a file so that it can be read during the run and,
 * when it starts to level off, we can interrupt it.
 */
/* TODO Perhaps overload with one that takes squaredNorms so that we can calculate this during
 * training without the extra memory and time hits.
 */
float calculateQuantizationError(training_data_t* trainingVectors, float* net, float* squaredNorms) {
	int numberOfNodes = g_rows * g_columns;

	float totalDiscrepancy = 0.0;

	// TODO Consider reading only some of the training vectors.
	for (vector<map<int, float> >::const_iterator vecIt = trainingVectors->begin();
			vecIt != trainingVectors->end();
			vecIt++) {
		map<int, float> trainingVector = *vecIt;

		Coordinate winnerCoordinate = findWinner(trainingVector, net, squaredNorms);

		float squaredNorm =
				squaredNorms[(winnerCoordinate.row * g_columns) + winnerCoordinate.column];
		float* winner = calculateNodeIndex(net, winnerCoordinate);

		float discrepancy =	distanceToSparse(winner, trainingVector, squaredNorm);

		totalDiscrepancy += discrepancy;
	}

	return (totalDiscrepancy / numberOfNodes);
}

float calculateQuantizationError(training_data_t* trainingVectors, float* net) {
	int numberOfNodes = g_rows * g_columns;
	float* squaredNorms = new float[numberOfNodes];
	zeroOut(squaredNorms, numberOfNodes);
	calculateSquaredNorms(squaredNorms, net);

	float quantizationError = calculateQuantizationError(trainingVectors, net, squaredNorms);

	delete [] squaredNorms;

	return quantizationError;
}



void train(int myRank, float* net, training_data_t* myTrainingVectors) {
	cout << "Training.. ";

	float width = INITIAL_WIDTH;

	const int numberOfNodes = g_rows * g_columns;
	const int flatSize = numberOfNodes * g_dim;

	g_myNumerators = new float[flatSize];
	zeroOut(g_myNumerators, flatSize);
	g_myNumerators2 = new float[flatSize];
	zeroOut(g_myNumerators2, flatSize);

	g_myDenominators = new float[numberOfNodes];
	zeroOut(g_myDenominators, numberOfNodes);
	g_myDenominators2 = new float[numberOfNodes];
	zeroOut(g_myDenominators2, numberOfNodes);

	float* recentSquaredNorms = new float[numberOfNodes];
	zeroOut(recentSquaredNorms, numberOfNodes);
	calculateSquaredNorms(recentSquaredNorms, net);

	if (myRank == 0) {
		float quantizationErrorStart =
				calculateQuantizationError(myTrainingVectors, net, recentSquaredNorms);
		cout << "At initialization, quantization error = " << quantizationErrorStart << endl;
	}

	// TODO Think about tweaking this so that we never need a final synch..
	int numberOfTimesteps =
			(int) ((NUMBER_OF_TRAINING_STEPS + g_numberOfJobs - 1) / g_numberOfJobs);

	int t;

	for (t = 0; t < numberOfTimesteps; t++) {
		map<int, float> trainingVector = myTrainingVectors->at(t % myTrainingVectors->size());

		Coordinate winner = findWinner(trainingVector, net, recentSquaredNorms);

		// Accumulate for equation 5 (exploit sparseness here, too).
		/* If we do non-gaussian neighborhoods in the future, be smarter about which nodes to
		 * update.
		 *
		 * Eventually, may want to change this to exploit eight-fold symmetry
		 * in circles centered on the winner (for Gaussian neighborhoods in
		 * rectangular topology).
		 */
		for (int row = 0; row < g_rows; row++) {
			for (int column = 0; column < g_columns; column++) {
				//TODO Make a Coordinate iterator?
				Coordinate coord(row, column);

				float gauss = gaussian(coord, winner, width);

				addScaledSparseVectorToNodeVector(
						calculateNodeIndex(g_myNumerators, coord), gauss, trainingVector);

				g_myDenominators[(row * g_columns) + column] += gauss;
			}
		}

		if ((t + 1) % NUMBER_OF_STEPS_BETWEEN_UPDATES == 0) {
			calculateNewNet(net);

			// Reset for next "epoch"

			width = calculateWidthAtTime(t, numberOfTimesteps);

			cout << "Updated net; new width = " << width << endl;

			zeroOut(g_myNumerators, flatSize);
			zeroOut(g_myDenominators, numberOfNodes);

			zeroOut(recentSquaredNorms, numberOfNodes);
			calculateSquaredNorms(recentSquaredNorms, net);

			if (myRank == 0) {
				float quantizationErrorMid =
						calculateQuantizationError(myTrainingVectors, net, recentSquaredNorms);
				cout << "Finished synchronizing, quantization error = " << quantizationErrorMid << endl;
			}
		}
	}

	// If we had timesteps beyond the final reduce, reduce once more to pick up the stragglers.
	if ((t + 1) % NUMBER_OF_STEPS_BETWEEN_UPDATES != 1) {
		cout << "Warning: Had to perform one last net update." << endl;

		calculateNewNet(net);
	}

	if (myRank == 0) {
		float quantizationErrorFinal =
				calculateQuantizationError(myTrainingVectors, net, recentSquaredNorms);
		cout << "Finally, quantization error = " << quantizationErrorFinal << endl;
	}

	cout << "Done." << endl;
}


int main(int argc, char *argv[]) {
	MPI::Init(argc, argv);

	int myRank = MPI::COMM_WORLD.Get_rank();

	cout << "My rank is " << myRank << endl;

	g_numberOfJobs = MPI::COMM_WORLD.Get_size();

	/* TODO Parse any command line arguments here */

	float* net = loadInitialNet();

	// TODO Manually swapping out these function names is lame.
	training_data_t* trainingVectors = loadMyTrainingVectorsFromSparse(myRank);

	train(myRank, net, trainingVectors);

	if (myRank == 0) {
		writeNetToFile(net);
	}

	MPI::Finalize();

	return 0;
}
