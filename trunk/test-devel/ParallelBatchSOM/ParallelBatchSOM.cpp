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

using namespace std;
using std::cerr;
using std::endl;

typedef vector< map<int, float> > training_data_t;


// TODO Test with more realistic numbers.
const int NUMBER_OF_TRAINING_STEPS = 800 * 20;
const int NUMBER_OF_JOBS = 1;
const int NUMBER_OF_STEPS_BETWEEN_UPDATES = 800;

const int INITIAL_WIDTH = 8;
const int FINAL_WIDTH = 1; // TODO Or what?

// set by loadInitialNet()
int g_rows = -1;
int g_columns = -1;
int g_dim = -1;

// set by loadTrainingVectors()
int g_numberOfVectors = -1;

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

//	for (int i = 0; i < g_dim; i++) {
//		*(node + i) += scalar * sparseVector.at(i);
//	}
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
	return net + (g_dim * (coord.row * g_columns + coord.column)); // TODO Triple check this index.
}

/* TODO Replace linear algebra functions with calls to BLAS. */

/* According to equation 8, exploiting sparseness.
 * vector is w_k and indexToOnes represents a binary-valued training vector.
 */
float distanceToSparse(float* vector, map<int, float> sparseVector, float recentSquaredNorm) {
	float leftSum = 0.0;

	for (map<int, float>::const_iterator it = sparseVector.begin(); it != sparseVector.end(); it++) {
		int sparseIndex = it->first;
		float sparseValue = it->second;

		leftSum += sparseValue * (sparseValue - 2 * vector[sparseIndex]);

//		cout << "sparseValue " << sparseValue << ", vector[sparseIndex] = " << vector[sparseIndex] << ", leftSum = " << leftSum << endl;
	}

	float rightSum = recentSquaredNorm;

	if (leftSum + rightSum < 0) {
		cout << "leftSum = " << leftSum << "; rightSum = " << rightSum << endl;
	}

	return (leftSum + rightSum);
}

/*
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
*/

/* TODO: Cosine similarity and cheap cosine similarity
 * The latter ignores sqrting the norms when only comparison to other such numbers is desired.
 */

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

// Might revisit some optimizations Russell mentioned in the long term.
// h_(ck)(t) = exp(-||r_k - r_c||^2 / width(t)^2);
/* TODO Oops, this is not quite right for non-rectangular topologies.
 * Since we're assuming hexagonal, this will need to be tweaked.
 * Perhaps use the trick from SOM_PAK.
 */
float gaussian(Coordinate coord1, Coordinate coord2, float width) {
//	int rowDiff = coord1.row - coord2.row;
//	int columnDiff = coord1.column - coord2.column;
//
//	cout << "rowDiff " << rowDiff << ", columnDiff " << columnDiff << endl;
//	cout << "  distance = " << exp(-(rowDiff*rowDiff + columnDiff*columnDiff) / (width * width)) << endl;

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

training_data_t loadMyTrainingVectors(int myRank) {
	cout << "Loading training vectors.. ";

	ifstream trainingFile("color.dat"); // TODO Parameterize

	if (trainingFile.is_open()) {

		int dimensions;
		trainingFile >> g_numberOfVectors >> dimensions;
		if (dimensions != g_dim) { // TODO Ugh.
			cerr << "Dimensionality of the training set (" << dimensions << ") does not agree with the dimensionality of the codebook vectors (" << g_dim << ")." << endl;
			exit (1);
		}

		training_data_t* trainingVectors = new training_data_t();

		string line;
		bool onFirstLine = true;
		int vectorLineNumber = 0;
		while (!trainingFile.eof()) {

			// Skip lines that aren't mine.
			if (vectorLineNumber % NUMBER_OF_JOBS == myRank) {
//				cout << "Reading training vector file line " << vectorLineNumber << endl;

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

		return (*trainingVectors);
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

    cout << "Winner is at " << winner.row << ", " << winner.column << " with distance " << shortestDistance << endl;

    return winner;
}

// Perform the final calculation for equation 5 and Allreduce.
void calculateNewNet(float* net, float* myNumerators, float* myDenominators) {
//	cout << "Synchronizing." << endl;

//	int flatSize = g_rows * g_columns * g_dim;
//	for (int i = 0; i < flatSize; i++) {
//
//	}

//	cout << "0, 0 denom = " << myDenominators[0] << endl;

    for (int row = 0; row < g_rows; row++){
        for (int column = 0; column < g_columns; column++){
            Coordinate coord(row, column);

//            if (row % 11 == 0 && column % 7 == 5) { // TODO Remove.
//            	cout << "Before numerator = " << *calculateNodeIndex(myNumerators, coord) << endl;
//            	cout << "Denominator = " << myDenominators.at(row).at(column) << endl;
//            }

            scaleNodeVector(
            		calculateNodeIndex(net, coord),
            		calculateNodeIndex(myNumerators, coord),
            		(1.0 / myDenominators[(row * g_columns) + column]));

//            if (row % 11 == 0 && column % 7 == 5) { // TODO Remove.
//				cout << "After numerator = " << *calculateNodeIndex(myNumerators, coord) << endl;
//			}

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
	 * comm		world?
	 * Remember to check return value for error.
	 */
}

void calculateSquaredNorms(float* oldSquaredNorms, float* net) {
//	cout << "Calculating squared norms." << endl;

	int numberOfNodes = g_rows * g_columns;

	for (int nodeIndex = 0; nodeIndex < numberOfNodes; nodeIndex++) {
		float* currentNode = net + nodeIndex * g_dim;
		float* currentNorm = oldSquaredNorms + nodeIndex;
//		int nodeIndex = g_dim * nodeIndex;

//		if (nodeIndex == 2) {
//			cout << "starting node 0,2" << endl;
//		}
		for (int weightIndex = 0; weightIndex < g_dim; weightIndex++) {
			float weight = currentNode[weightIndex];

			(*currentNorm) += weight * weight;

//			if (nodeIndex == 2) {
//				cout << "weight = " << weight << ", current norm = " << (*currentNorm) << endl;
//			}
		}
	}

	cout << "Zero indices in squared norms:" << endl;
	for (int i = 0; i < numberOfNodes; i++) {
		if (oldSquaredNorms[i] == 0) {
			cout << (i % g_columns) << ", " << (i / g_columns) << endl;
		}
	}
}

void train(int myRank, float* net, training_data_t myTrainingVectors) {
	cout << "Training.. ";

	float width = INITIAL_WIDTH;

	const int numberOfNodes = g_rows * g_columns;
	const int flatSize = numberOfNodes * g_dim;

	float* myNumerators = new float[flatSize];
	zeroOut(myNumerators, flatSize);

	float* myDenominators = new float[numberOfNodes];//vector<vector<float> > myDenominators(g_rows, vector<float>(g_columns));
	zeroOut(myDenominators, numberOfNodes);

	float* recentSquaredNorms = new float[numberOfNodes];
	calculateSquaredNorms(recentSquaredNorms, net);

	int numberOfTimesteps =
			(int) ((NUMBER_OF_TRAINING_STEPS + NUMBER_OF_JOBS - 1) / NUMBER_OF_JOBS);

	int t;

	for (t = 0; t < numberOfTimesteps; t++) {
		map<int, float> trainingVector = myTrainingVectors[t % myTrainingVectors.size()];

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
//				cout << "gaussian = " << gauss << endl;

				// Increment equation 5 numerator.
				addScaledSparseVectorToNodeVector(
						calculateNodeIndex(myNumerators, coord), gauss, trainingVector);

				// Increment equation 5 denominator.
				myDenominators[(row * g_columns) + column] += gauss;
			}
		}

		if ((t + 1) % NUMBER_OF_STEPS_BETWEEN_UPDATES == 0) {
			calculateNewNet(net, myNumerators, myDenominators);

//			for (Coordinate coord;;/* in net*/) {
//				// update node using eq5num/eq5den (zero-check den first).
//			}

			// Reset for next "epoch"
			width = calculateWidthAtTime(t, numberOfTimesteps);

			cout << "Just updated net; new width = " << width << endl;

			zeroOut(myNumerators, flatSize);
			zeroOut(myDenominators, numberOfNodes);

			zeroOut(recentSquaredNorms, numberOfNodes);
			calculateSquaredNorms(recentSquaredNorms, net);
		}
	}

	// If we had timesteps beyond the final reduce, reduce once more to pick up the stragglers.
	if ((t + 1) % NUMBER_OF_STEPS_BETWEEN_UPDATES != 1) {
		cout << "Handling one remaining synch." << endl;

		calculateNewNet(net, myNumerators, myDenominators);
	}

	cout << "Done." << endl;
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
	float* net = loadInitialNet();

	training_data_t trainingVectors = loadMyTrainingVectors(myRank);

	train(myRank, net, trainingVectors);

	// TODO Perhaps write a little quantization error function.

	writeNetToFile(net);

//	MPI_Finalize();

	return 0;
}
