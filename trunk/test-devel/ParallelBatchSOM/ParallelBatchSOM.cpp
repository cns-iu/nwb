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


int g_numberOfTrainingSteps;
int g_epochLengthInTimesteps;
int g_initialWidth;
int g_finalWidth;
string g_initialCodebookPath;
string g_trainingDataPath;
string g_outputCodebookPathStem;


// set by loadInitialCodebook()
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

///* Don't bother sqrting the norms when only comparison to other such numbers is desired.
// * Could pull out the netVector norm to be calculated only once per epoch.
// * Could pull out the trainingVector norm to be calculated only once.
// */
//float cheapCosineSimilarity(float* netVector, map<int, float> sparseVector) {
//	float dotProduct = 0.0;
//	float sparseVectorSquaredNorm = 0.0;
//
//	for (map<int, float>::const_iterator it = sparseVector.begin(); it != sparseVector.end(); it++) {
//		int sparseIndex = it->first;
//		float sparseValue = it->second;
//
//		dotProduct += sparseValue * netVector[sparseIndex];
//		sparseVectorSquaredNorm += sparseValue * sparseValue;
//	}
//
//
//	float netVectorSquaredNorm = 0.0;
//
//	for (int weightIndex = 0; weightIndex < g_dim; weightIndex++) {
//		float coordinate = netVector[weightIndex];
//		netVectorSquaredNorm += coordinate * coordinate;
//	}
//
//
//	return (dotProduct / (sparseVectorSquaredNorm * netVectorSquaredNorm));
//}

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

float hexa_dist(int xDelta, int yDelta) {
	return hexa_dist(0, 0, xDelta, yDelta);
}

float* g_gaussians;
void updateGaussians(float width) {
	for (int rowDelta = 0; rowDelta < g_rows; rowDelta++) {
		for (int colDelta = 0; colDelta < g_columns; colDelta++) {
			int flatIndex = rowDelta * g_columns + colDelta;

			g_gaussians[flatIndex] = exp(-hexa_dist(rowDelta, colDelta) / (width * width));
		}
	}
}

//float gaussian(Coordinate coord1, Coordinate coord2, float width) {
//	return exp(-hexa_dist(coord1.row, coord1.column, coord2.row, coord2.column) / (width * width));
//}

float interpolate(float x, float x0, float x1, float y0, float y1) {
	if (x0 == x1) {
		cerr << "Can't interpolate over an empty input range." << endl;
		exit(1);
	} else {
		return y0 + (x - x0) * (y1 - y0) / (x1 - x0);
	}
}

float calculateWidthAtTime(int t, int tFinal) {
	return interpolate(t, 0, tFinal, g_initialWidth, g_finalWidth);
}

// myRank is given only to limit reporting.
float* loadInitialCodebook(int myRank) {
	cout << "Loading initial codebook.. " << endl;

	if (myRank == 0) {
		cout << "Starting initial codebook load at " << getAsctime() << endl;
	}

	float* net;

	ifstream codebookFile(g_initialCodebookPath.c_str());
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

void writeCodebookToFile(float* net, string filenameID) {
	cout << "Codebook snapshot starting at " << getAsctime() << endl;

	string outputCodebookPath(g_outputCodebookPathStem);
	outputCodebookPath.append(filenameID);
	outputCodebookPath.append(".cod");

	ofstream outFile(outputCodebookPath.c_str());
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

		outFile.close();
	} else {
		cerr << "Error opening output codebook file." << endl;
		exit(1);
	}

	cout << "Codebook snapshot finishing at " << getAsctime() << endl;
}

void readConfigurationFile() {
	cout << "Reading configuration file.." << endl;

	ifstream configurationFile("paths.cfg");

	if (configurationFile.is_open()) {
		configurationFile >> g_numberOfTrainingSteps;
		configurationFile >> g_epochLengthInTimesteps;
		configurationFile >> g_initialWidth;
		configurationFile >> g_finalWidth;
		configurationFile >> g_initialCodebookPath;
		configurationFile >> g_trainingDataPath;
		configurationFile >> g_outputCodebookPathStem;
	}

	configurationFile.close();

	cout << "Done." << endl;
}

training_data_t* loadMyTrainingVectorsFromDense(int myRank) {
	cout << "Loading training vectors (from dense representation).. " << endl;

	ifstream trainingFile(g_trainingDataPath.c_str());

	if (trainingFile.is_open()) {
		int dimensions;
		trainingFile >> g_numberOfVectors >> dimensions;
		if (dimensions != g_dim) { // TODO Ugh.
			cerr << "Dimensionality of the training set (" << dimensions << ") does not agree with the dimensionality of the codebook vectors (" << g_dim << ")." << endl;
			exit(1);
		}

		training_data_t* trainingVectors = new training_data_t();

		int skippedVectorCount = 0;
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

				if (trainingVector.empty()) {
					skippedVectorCount++;
				} else {
					trainingVectors->push_back(trainingVector);
				}
			}

			vectorLineNumber++;
		}
		// TODO Sanity check: i == g_rows * g_columns (- 1?)

		trainingFile.close();

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

Coordinate findWinnerEuclidean(map<int, float> trainingVector, float* net, float* recentSquaredNorms) {
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
    				distanceToSparse(
    						calculateNodeIndex(net, coord), trainingVector, recentSquaredNorm);

    		if (distance < shortestDistance) {
				shortestDistance = distance;
				winner = coord;
    		}
    	}
    }

    return winner;
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

// Allreduce and perform the final calculation for equation 5.
void calculateNewCodebook(float* net) {
	cout << "Synchronizing.. " << endl;

	int numberOfNodes = g_rows * g_columns;
	int flatSize = numberOfNodes * g_dim;

	float* numTemp = g_myNumerators;
	float* denTemp = g_myDenominators;

	// TODO Think about being trickier with in-place swaps.
	try {
		MPI::COMM_WORLD.Allreduce(g_myNumerators, g_myNumerators2, flatSize, MPI::FLOAT, MPI::SUM);
	} catch (MPI::Exception e) {
		cerr << "MPI error reducing numerators: " << e.Get_error_string() << endl;
		MPI::COMM_WORLD.Abort(1);
	}
	try {
		MPI::COMM_WORLD.Allreduce(g_myDenominators, g_myDenominators2, numberOfNodes, MPI::FLOAT, MPI::SUM);
	} catch (MPI::Exception e) {
		cerr << "MPI error reducing denominators: " << e.Get_error_string() << endl;
		MPI::COMM_WORLD.Abort(1);
	}

    g_myNumerators = g_myNumerators2;
    g_myNumerators2 = numTemp;

    g_myDenominators = g_myDenominators2;
    g_myDenominators2 = denTemp;

    for (int row = 0; row < g_rows; row++){
		for (int column = 0; column < g_columns; column++){
			Coordinate coord(row, column);

			if (g_myDenominators[(row * g_columns) + column] <= 0) {
				cout << "ERROR: BAD DENOM in row " << row << ", column " << column << ": "
						<< g_myDenominators[(row * g_columns) + column] << endl;
			}

			scaleNodeVector(
					calculateNodeIndex(net, coord),
					calculateNodeIndex(g_myNumerators, coord),
					(1.0 / g_myDenominators[(row * g_columns) + column]));
		}
	}

    /* TODO Loop over the codebook here and ensure there are no non-zero vectors?
     * The chance is essentially zero.. but with the cosine similarity metric it
     * would be fatal.
     */

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

float calculateEuclideanQuantizationError(training_data_t* trainingVectors, float* net, float* squaredNorms) {
	float totalDiscrepancy = 0.0;

	for (vector<map<int, float> >::const_iterator vecIt = trainingVectors->begin();
			vecIt != trainingVectors->end();
			vecIt++) {
		map<int, float> trainingVector = *vecIt;

		Coordinate winnerCoordinate = findWinnerEuclidean(trainingVector, net, squaredNorms);

		float squaredNorm =
				squaredNorms[(winnerCoordinate.row * g_columns) + winnerCoordinate.column];
		float* winner = calculateNodeIndex(net, winnerCoordinate);

		float discrepancy =	distanceToSparse(winner, trainingVector, squaredNorm);

		totalDiscrepancy += discrepancy;
	}

	return (totalDiscrepancy / (*trainingVectors).size());
}

float calculateEuclideanQuantizationError(training_data_t* trainingVectors, float* net) {
	int numberOfNodes = g_rows * g_columns;
	float* squaredNorms = new float[numberOfNodes];
	zeroOut(squaredNorms, numberOfNodes);
	calculateSquaredNorms(squaredNorms, net);

	float quantizationError = calculateEuclideanQuantizationError(trainingVectors, net, squaredNorms);

	delete [] squaredNorms;

	return quantizationError;
}

float calculateCosineQuantizationError(
		training_data_t* trainingVectors, float* net, float* recentSquaredNorms) {
	float totalDissimilarity = 0.0;

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
	}

	return (totalDissimilarity / (*trainingVectors).size());
}

void reportAverageQuantizationError(
		training_data_t* sampledTrainingVectors, float* net, int myRank, float* recentSquaredNorms) {
	if (myRank == 0) {
		cout << "Quantization error report starting at " << getAsctime() << endl;
	}

	float quantizationErrorStart =
				calculateCosineQuantizationError(sampledTrainingVectors, net, recentSquaredNorms);
	//				calculateEuclideanQuantizationError(myTrainingVectors, net, recentSquaredNorms);
	float* qerrorLocal = new float[1];
	*qerrorLocal = quantizationErrorStart;

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


void train(int myRank, float* net, training_data_t* myTrainingVectors) {
	if (myRank == 0) {
		cout << "Training.. " << endl;
	}

	float width = g_initialWidth;

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

	g_gaussians = new float[numberOfNodes];
	updateGaussians(width);

	training_data_t::const_iterator sampleStart = myTrainingVectors->begin();
	int sampleSize = 100; // TODO Just a thought for now.  Can we do better?
	if (sampleSize > myTrainingVectors->size()) {
		sampleSize = myTrainingVectors->size();
	}
	training_data_t::const_iterator sampleEnd = myTrainingVectors->begin() + sampleSize;
	training_data_t sampledTrainingVectors(sampleStart, sampleEnd);

	reportAverageQuantizationError(&sampledTrainingVectors, net, myRank, recentSquaredNorms);

	int numberOfTimesteps =
			(int) (((float) g_epochLengthInTimesteps) * ceil(((float) g_numberOfTrainingSteps) / (((float) g_numberOfJobs) * ((float) g_epochLengthInTimesteps))));

	if (myRank == 0) {
		cout << "============ Run length figures ============" << endl;
		cout << "Number of training steps = " << g_numberOfTrainingSteps << endl;
		cout << "Epoch length = " << g_epochLengthInTimesteps << endl;
		cout << "Number of jobs = " << g_numberOfJobs << endl;
		cout << "Calculated number of timesteps (per job) = " << numberOfTimesteps << endl;
		cout << "Derived total number of timesteps (across jobs) = " << (g_numberOfJobs * numberOfTimesteps) << endl;
		cout << "Derived number of batches = " << (numberOfTimesteps / g_epochLengthInTimesteps) << endl;
		cout << "============================================" << endl;
	}

	int t;

	for (t = 0; t < numberOfTimesteps; t++) {
		map<int, float> trainingVector = myTrainingVectors->at(t % myTrainingVectors->size());

		Coordinate winner = findWinnerCosine(trainingVector, net, recentSquaredNorms);
//		Coordinate winner = findWinnerEuclidean(trainingVector, net, recentSquaredNorms);

		// Accumulate for equation 5 (exploit sparseness here, too).
		/* If we do non-gaussian neighborhoods in the future, be smarter about which nodes to
		 * update.
		 */
		for (int row = 0; row < g_rows; row++) {
			for (int column = 0; column < g_columns; column++) {
				//TODO Make a Coordinate iterator?
				Coordinate coord(row, column);

				int rowDelta = abs(coord.row - winner.row);
				int colDelta = abs(coord.column - winner.column);
				int flatIndex = rowDelta * g_columns + colDelta;
				float gauss = g_gaussians[flatIndex];
//				float gauss = gaussian(coord, winner, width);

				addScaledSparseVectorToNodeVector(
						calculateNodeIndex(g_myNumerators, coord), gauss, trainingVector);

				g_myDenominators[(row * g_columns) + column] += gauss;
			}
		}

		if ((t + 1) % g_epochLengthInTimesteps == 0) {
			if (myRank == 0) {
				cout << "Update starting at " << getAsctime() << endl;
			}

			calculateNewCodebook(net);

			if (myRank == 0) {
				stringstream filenameIDStream;
				filenameIDStream << t;
				string filenameID = filenameIDStream.str();
				writeCodebookToFile(net, filenameID);
			}

			// Reset for next "epoch"

			width = calculateWidthAtTime(t, numberOfTimesteps);

			updateGaussians(width);

			if (myRank == 0) {
				cout << "Updated net; new width = " << width << endl;
			}

			zeroOut(g_myNumerators, flatSize);
			zeroOut(g_myDenominators, numberOfNodes);

			zeroOut(recentSquaredNorms, numberOfNodes);
			calculateSquaredNorms(recentSquaredNorms, net);

			reportAverageQuantizationError(&sampledTrainingVectors, net, myRank, recentSquaredNorms);

			if (myRank == 0) {
				cout << "Update finishing at " << getAsctime() << endl;
			}
		}
	}

//	/* TODO Maybe this is just a bad idea altogether.
//	 * With the Jan 5 update to the derived figure numberOfTimesteps,
//	 * this should never be necessary, as there will be no fractional epochs.
//	 */
//	// If we had timesteps beyond the final reduce, reduce once more to pick up the stragglers.
//	if ((t + 1) % g_epochLengthInTimesteps != 1) {
//		cout << "Warning: Had to perform one last net update." << endl;
//
//		calculateNewCodebook(net);
//
//		reportAverageQuantizationError(&sampledTrainingVectors, net, myRank, recentSquaredNorms);
//	}

	cout << "Done." << endl;
}


int main(int argc, char *argv[]) {
	srand(time(0));

	MPI::Init(argc, argv);
	MPI::COMM_WORLD.Set_errhandler(MPI::ERRORS_THROW_EXCEPTIONS);

	int myRank = MPI::COMM_WORLD.Get_rank();

	if (myRank == 0) {
		cout << "Program starting at " << getAsctime() << endl;
	}

	cout << "My rank is " << myRank << endl;

	g_numberOfJobs = MPI::COMM_WORLD.Get_size();

	readConfigurationFile();

	float* net = loadInitialCodebook(myRank);

	// TODO Manually swapping out these function names is lame.
	training_data_t* trainingVectors = loadMyTrainingVectorsFromSparse(myRank);

	train(myRank, net, trainingVectors);

	if (myRank == 0) {
		writeCodebookToFile(net, "");
	}

	if (myRank == 0) {
		cout << "Program finishing at " << getAsctime() << endl;
	}

	MPI::Finalize();

	return 0;
}
