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
#include <time.h>
#include <cstdlib>
#include <ctime>


#include "mpi.h"

using namespace std;
using std::cerr;
using std::endl;

typedef vector<map<int, float> > training_data_t;


string g_codebookPath;
string g_trainingDataPath;


// set by loadInitialCodebook()
int g_rows = -1;
int g_columns = -1;
int g_dim = -1;

int g_numberOfJobs = 1;

// set by loadTrainingVectors()
int g_numberOfVectors = -1;


// TODO Experiment with getting rid of Coordinate altogether.  Benchmark first.
struct Coordinate {
	int row;
	int column;

	Coordinate(int r, int c) { row = r; column = c; }
};


//training_data_t* randomlySampleTrainingVectors(training_data_t* trainingVectors, int resultSize) {
//	training_data_t* sampled = new training_data_t();
//
//	for (int i = 0; i < resultSize; i++) {
//		int selectedIndex = rand() % trainingVectors->size();
//		sampled->push_back((*trainingVectors)[selectedIndex]);
//	}
//
//	return sampled;
//}

char* getAsctime() {
	time_t rawTime;
	time(&rawTime);
	struct tm* localTime = localtime(&rawTime);
	return asctime(localTime);
}

void zeroOut(float* array, int size) {
	for (int i = 0; i < size; i++) {
		array[i] = 0.0;
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
		cout << "Error: leftSum = " << leftSum << "; rightSum = " << rightSum << endl;
	}

	return (leftSum + rightSum);
}

float calculateCosineSimilarity(float* netVector, map<int, float> sparseVector, float recentSquaredNorm) {
	float dotProduct = 0.0;

	for (map<int, float>::const_iterator it = sparseVector.begin(); it != sparseVector.end(); it++) {
		dotProduct += (it->second) * netVector[it->first];
	}

	return (dotProduct / (sqrt(sparseVector.size()) * sqrt(recentSquaredNorm)));
}

/* NOTE: This entire notion breaks down when the cosine similarity may be negative.
 * This will not happen with the data we're currently considering.
 */
float calculateCosineDissimilarity(float* netVector, map<int, float> sparseVector, float recentSquaredNorm) {
	return 1 - calculateCosineSimilarity(netVector, sparseVector, recentSquaredNorm);
}


// myRank is given only to limit reporting.
float* loadInitialCodebook(int myRank) {
	cout << "Loading initial codebook.. " << endl;

	if (myRank == 0) {
		cout << "Starting initial codebook load at " << getAsctime() << endl;
	}

	float* net;

	ifstream codebookFile(g_codebookPath.c_str());
	if (codebookFile.is_open()) {
		// Read parameters on first line.
		string topology; // Ignored for now.
		string neighborhood; // Ignored for now.
		codebookFile >> g_dim >> topology >> g_columns >> g_rows >> neighborhood;

		int numberOfNodes = g_rows * g_columns;
		net = new float[numberOfNodes * g_dim];

		// TODO Check eof during and after, perhaps.
		for (int i = 0; i < numberOfNodes; i++) {
			float* currentNode = net + i * g_dim;

			bool allZero = true;
			for (int weightIndex = 0; weightIndex < g_dim; weightIndex++) {
				codebookFile >> currentNode[weightIndex];

				if (currentNode[weightIndex] != 0) {
					allZero = false;
				}
			}

			if (allZero) {
				cerr << "ERROR: Codebook contained an all-zero vector.  Quitting now." << endl;
				exit(1);
			}
		}

		if (myRank == 0) {
			cout << "Finishing initial codebook load at " << getAsctime() << endl;
		}

		codebookFile.close();
	} else {
		cerr << "Error opening random codebook file!" << endl;
		exit(1);
	}

	cout << "Done." << endl;

	return net;
}

void readConfigurationFile() {
	cout << "Reading configuration file.." << endl;

	ifstream configurationFile("qerrorpaths.cfg");

	if (configurationFile.is_open()) {
		configurationFile >> g_codebookPath;
		configurationFile >> g_trainingDataPath;
	}

	configurationFile.close();

	cout << "Done." << endl;
}


// Assumes that the file contains lines which give indices to non-zeroes in a binary-valued vector.
training_data_t* loadMyTrainingVectorsFromSparse(int myRank) {
	cout << "Loading training vectors (from sparse representation).. " << endl;

	if (myRank == 0) {
		cout << "Starting training vectors load at " << getAsctime() << endl;
	}

	ifstream trainingFile(g_trainingDataPath.c_str());

	if (trainingFile.is_open()) {
		string firstLine;
		getline(trainingFile, firstLine);
		istringstream firstLineStream(firstLine);
		int dimensions;
		firstLineStream >> g_numberOfVectors >> dimensions;
		if (dimensions != g_dim) {
			cerr << "Dimensionality of the training set (" << dimensions << ") does not agree with the dimensionality of the codebook vectors (" << g_dim << ")." << endl;
			exit(1);
		}

		const string commentMarker("#");

		training_data_t* trainingVectors = new training_data_t();

		int skippedVectorCount = 0;
		int lineNumber = 0;
		string line;
		while (getline(trainingFile, line)) {
			// Skip comments.
			if (0 == line.find(commentMarker)) {
				continue;
			}

			// Only read lines assigned to my rank.
			if (lineNumber % g_numberOfJobs == myRank) {
				istringstream linestream(line);

				// Read and ignore document ID.
				int documentID;
				linestream >> documentID;

				/* TODO Could be mapping into ints, or shorts, or bools even.
				 * Or even some nice bitset instead of a map.
				 * For the work we're currently doing, this could be a set of ints.  Test speed-up.
				 */
				map<int, float> trainingVector;

				int oneBasedIndex;
				while (linestream >> oneBasedIndex) {
					int zeroBasedIndex = oneBasedIndex - 1;

					if (zeroBasedIndex < g_dim) {
						trainingVector[zeroBasedIndex] = 1;
					} else {
						cerr << "Dimensionality of training vectors exceeds that of the codebook." << endl;
						exit(1);
					}
				}

				if (trainingVector.empty()) {
					skippedVectorCount++;
				} else {
					trainingVectors->push_back(trainingVector);
				}
			}

			lineNumber++;
		}

		trainingFile.close();

		if (myRank == 0) {
			cout << "Finishing training vectors load at " << getAsctime() << endl;
		}

		if (skippedVectorCount > 0) {
			if (myRank == 0) {
				cerr << "WARNING: Skipped " << skippedVectorCount
					<< " training vectors with no non-zero coordinates.  "
					<< "If you are not using the cosine similarity metric than remove this check/skip!"
					<< endl;
			}
		}

		cout << "Done." << endl;

		return trainingVectors;
	} else {
		cerr << "Error opening training data file!" << endl;
		exit(1);
	}
}

Coordinate findWinnerCosine(map<int, float> trainingVector, float* net, float* recentSquaredNorms) {
    /* Long term TODO: In late training (or when convergence can be presumed decent),
	 * start recording the net (i, j) for the BMU on the previous timestep or two, then
	 * search only in neighborhoods of that rather than the whole net.
	 */
    float leastDissimilarity = numeric_limits<float>::max();
    Coordinate winner(0, 0);

    for (int row = 0; row < g_rows; row++) {
    	for (int column = 0; column < g_columns; column++) {
    		Coordinate coord(row, column);

    		float recentSquaredNorm = recentSquaredNorms[(row * g_columns) + column];

    		float dissimilarity =
    				calculateCosineDissimilarity(calculateNodeIndex(net, coord), trainingVector, recentSquaredNorm);

    		if (dissimilarity < leastDissimilarity) {
				leastDissimilarity = dissimilarity;
				winner = coord;
    		}
    	}
    }

    return winner;
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

float calculateCosineQuantizationError(
		int myRank, training_data_t* trainingVectors, float* net, float* recentSquaredNorms) {
	float totalDissimilarity = 0.0;

	int total = trainingVectors->size();

	for (vector<map<int, float> >::const_iterator vecIt = trainingVectors->begin();
			vecIt != trainingVectors->end();
			vecIt++) {
		map<int, float> trainingVector = *vecIt;

		Coordinate winnerCoordinate = findWinnerCosine(trainingVector, net, recentSquaredNorms);
		float* winner = calculateNodeIndex(net, winnerCoordinate);

		float recentSquaredNorm =
				recentSquaredNorms[(winnerCoordinate.row * g_columns) + winnerCoordinate.column];
		float dissimilarity = calculateCosineDissimilarity(winner, trainingVector, recentSquaredNorm);

		totalDissimilarity += dissimilarity;


		if (myRank == 0) {
			int completedCount = vecIt - trainingVectors->begin();

			if (completedCount % ((int) (total / 25.0)) == 0) {
				cout << getAsctime() << ": " << (100.0 * completedCount / total) << "% complete." << endl;
			}
		}
	}

	return (totalDissimilarity / (*trainingVectors).size());
}

void reportAverageQuantizationError(
		training_data_t* myTrainingVectors, float* net, int myRank, float* recentSquaredNorms) {
	if (myRank == 0) {
		cout << "Quantization error report starting at " << getAsctime() << endl;
	}

	float myQuantizationError =
				calculateCosineQuantizationError(myRank, myTrainingVectors, net, recentSquaredNorms);
	//				calculateEuclideanQuantizationError(myTrainingVectors, net, recentSquaredNorms);

	cout << "Process " << myRank << "'s local qerror is\t" << myQuantizationError << endl;

	float* qerrorLocal = new float[1];
	*qerrorLocal = myQuantizationError;

	float* qerrorTotal = new float[1];

	try {
		MPI::COMM_WORLD.Reduce(qerrorLocal, qerrorTotal, 1, MPI::FLOAT, MPI::SUM, 0);
	} catch (MPI::Exception e) {
		cerr << "MPI error reducing quantization errors: " << e.Get_error_string() << endl;
		MPI::COMM_WORLD.Abort(1);
	}

	float qerror = (*qerrorTotal) / g_numberOfJobs;

	if (myRank == 0) {
		cout << "Quantization error = " << qerror << endl;
	}

	if (myRank == 0) {
		cout << "Quantization error report finishing at " << getAsctime() << endl;
	}
}


//training_data_t::const_iterator sampleStart = myTrainingVectors->begin();
//int sampleSize = 100; // TODO Just a thought for now.  Can we do better?
//if (sampleSize > myTrainingVectors->size()) {
//	sampleSize = myTrainingVectors->size();
//}
//training_data_t::const_iterator sampleEnd = myTrainingVectors->begin() + sampleSize;
//training_data_t sampledTrainingVectors(sampleStart, sampleEnd);
//
//reportAverageQuantizationError(&sampledTrainingVectors, net, myRank, recentSquaredNorms);


int main(int argc, char *argv[]) {
	srand(time(0));

	MPI::Init(argc, argv);
	MPI::COMM_WORLD.Set_errhandler(MPI::ERRORS_THROW_EXCEPTIONS);

	int myRank = MPI::COMM_WORLD.Get_rank();

	if (myRank == 0) {
		cout << "Qerror starting at " << getAsctime() << endl;
	}

	cout << "My rank is " << myRank << endl;

	g_numberOfJobs = MPI::COMM_WORLD.Get_size();

	readConfigurationFile();

	float* net = loadInitialCodebook(myRank);

	// TODO Manually swapping out these function names is lame.
	training_data_t* myTrainingVectors = loadMyTrainingVectorsFromSparse(myRank);

	int numberOfNodes = g_rows * g_columns;
	float* squaredNorms = new float[numberOfNodes];
	zeroOut(squaredNorms, numberOfNodes);
	calculateSquaredNorms(squaredNorms, net);

	reportAverageQuantizationError(myTrainingVectors, net, myRank, squaredNorms);

	if (myRank == 0) {
		cout << "Qerror finishing at " << getAsctime() << endl;
	}

	MPI::Finalize();

	return 0;
}
