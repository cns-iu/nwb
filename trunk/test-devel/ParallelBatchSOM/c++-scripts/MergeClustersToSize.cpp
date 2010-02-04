// TODO Trim includes.
#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <vector>
#include <limits>
#include <map>
#include <set>
#include <queue>
#include <math.h>
#include <iomanip>
#include <time.h>
#include <cstdlib>
#include <ctime>

using namespace std;
using std::cerr;
using std::endl;


struct Coordinate {
	int row;
	int column;

	Coordinate(int r, int c) { row = r; column = c; }
};

struct Cluster {
	bool topLevel;
	set<string>* documents;
	set<Cluster>* neighbors;

	float* vector;


	Cluster(float* v) {
		topLevel = true;
		documents = new set<string>();
		neighbors = new set<Cluster>();

		vector = v;
	}
};

struct CalibratedDatum {
	string id;
	Coordinate coordinate(0, 0);
//	map<int, float> sparseVector;

	CalibratedDatum(string i, Coordinate c) { id = i; coordinate = c; }
};

typedef vector<CalibratedDatum> calibrated_data_t;


string g_codebookPath;
string g_inCalibratedDataPath;
string g_outClusterDataPath;

int g_rows = -1;
int g_columns = -1;
int g_dim = -1;

int g_numberOfVectors = -1;


char* getAsctime() {
	time_t rawTime;
	time(&rawTime);
	struct tm* localTime = localtime(&rawTime);
	return asctime(localTime);
}

void readConfigurationFile() {
	cout << "Reading configuration file.." << endl;

	ifstream configurationFile("merge-paths.cfg");

	if (configurationFile.is_open()) {
		configurationFile >> g_codebookPath;
		configurationFile >> g_inCalibratedDataPath;
		configurationFile >> g_outClusterDataPath;
	}

	configurationFile.close();

	cout << "Done." << endl;
}

float* calculateNodeIndex(float* codebook, Coordinate coord) {
	return codebook + (g_dim * (coord.row * g_columns + coord.column));
}

float* loadCodebook() {
	cout << "Starting codebook load at " << getAsctime() << endl;

	float* codebook;

	ifstream codebookFile(g_codebookPath.c_str());
	if (codebookFile.is_open()) {
		// Read parameters on first line.
		string topology; // Ignored for now.
		string neighborhood; // Ignored for now.
		codebookFile >> g_dim >> topology >> g_columns >> g_rows >> neighborhood;

		int numberOfNodes = g_rows * g_columns;
		codebook = new float[numberOfNodes * g_dim];

		for (int i = 0; i < numberOfNodes; i++) {
			float* currentNode = codebook + i * g_dim;

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

		codebookFile.close();
	} else {
		cerr << "Error opening codebook file!" << endl;
		exit(1);
	}

	cout << "Finishing initial codebook load at " << getAsctime() << endl;

	return codebook;
}

calibrated_data_t* loadCalibratedData() {
	cout << "Starting calibrated dataset load at " << getAsctime() << endl;

	ifstream calibratedDataFile(g_inCalibratedDataPath.c_str());

	if (calibratedDataFile.is_open()) {
		string firstLine;
		getline(calibratedDataFile, firstLine);
		istringstream firstLineStream(firstLine);
		int dimensions;
		firstLineStream >> g_numberOfVectors >> dimensions;
		if (dimensions != g_dim) {
			cerr << "Dimensionality of the calibrated dataset (" << dimensions;
			cerr << ") does not agree with the dimensionality of the codebook vectors (";
			cerr << g_dim << ")." << endl;
			exit(1);
		}

		const string commentMarker("#");

		calibrated_data_t* calibratedData = new calibrated_data_t();

		int lineNumber = 0;
		string line;
		while (getline(calibratedDataFile, line)) {
			// Comments are ignored.
			if (0 == line.find(commentMarker)) {
				continue;
			}

			istringstream linestream(line);

			string documentID;
			linestream >> documentID;

			int row;
			int column;
			linestream >> row;
			linestream >> column;
			Coordinate coordinate(row, column);

			float dissimilarity;
			linestream >> dissimilarity;

			/* Note the actual document vectors are ignored. */

			CalibratedDatum calibratedDatum(documentID, coordinate);
			calibratedData->push_back(calibratedDatum);

			lineNumber++;
		}

		calibratedDataFile.close();

		cout << "Finishing calibrated dataset load at " << getAsctime() << endl;

		return calibratedData;
	} else {
		cerr << "Error opening calibrated dataset file!" << endl;
		exit(1);
	}
}

//float calculateCosineSimilarity(
//		float* vector, CalibratedDatum* calibratedDatum, float recentSquaredNorm) {
//	float dotProduct = 0.0;
//	float sparseSquaredNorm = 0.0;
//
//	map<int, float> sparseVector = calibratedDatum->vector;
//
//	for (map<int, float>::const_iterator it = sparseVector.begin(); it != sparseVector.end(); it++) {
//		dotProduct += (it->second) * vector[it->first];
//		sparseSquaredNorm += (it->second) * (it->second);
//	}
//
//	return (dotProduct / (sqrt(sparseSquaredNorm) * sqrt(recentSquaredNorm)));
//}
float calculateCosineSimilarity(float* vector1, float* vector2) {
	float dotProduct = 0.0;
	float v1SquaredNorm = 0.0;
	float v2SquaredNorm = 0.0;

	for (int ii = 0; ii < g_dim; ii++) {
		float coord1 = vector1[ii];
		float coord2 = vector2[ii];

		dotProduct += coord1 * coord2;
		v1SquaredNorm += coord1 * coord1;
		v2SquaredNorm += coord2 * coord2;
	}

	return (dotProduct / (sqrt(v1SquaredNorm) * sqrt(v2SquaredNorm)));
}

/* NOTE: This entire notion breaks down when the cosine similarity may be negative.
 * This will not happen with the data we're currently considering.
 */
float calculateCosineDissimilarity(
		float* vector, CalibratedDatum* calibratedDatum, float recentSquaredNorm) {
	return 1 - calculateCosineSimilarity(vector, calibratedDatum, recentSquaredNorm);
}

int up(int y) {
	return y - 1;
}

int down(int y) {
	return y + 1;
}

int left(int x) {
	return x - 1;
}

int right(int x) {
	return x + 1;
}

//void tryInsertLeftAndRight(
//		set<Cluster>* s, map<Coordinate, Cluster>* coordinateToCluster, int x, int y) {
//	if (leftOK(x)) {
//		Coordinate left(y, left(x));
//		neighbors->insert(coordinateToCluster[left]);
//	}
//
//	if (rightOK(x)) {
//		Coordinate right(y, right(x));
//		neighbors->insert(coordinateToCluster[right]);
//	}
//}

int slantLeft(Coordinate c) {
	int left = c.column;
	if (c.row % 2 == 0) {
		left -= 1;
	}

	return left;
}

int slantRight(Coordinate c) {
	int right = c.column;
	if (c.row % 2 == 1) {
		right += 1;
	}

	return right;
}

Coordinate upLeft(Coordinate c) {
	return coordinate(up(c.row), slantLeft(c));
}

Coordinate upRight(Coordinate c) {
	return coordinate(up(c.row), slantRight(c));
}

Coordinate dueLeft(Coordinate c) {
	return coordinate(c.row, left(c.column));
}

Coordinate dueRight(Coordinate c) {
	return coordinate(c.row, right(c.column));
}

Coordinate downLeft(Coordinate c) {
	return coordinate(down(c.row), slantLeft(c));
}

Coordinate downRight(Coordinate c) {
	return coordinate(down(c.row), slantRight(c));
}

bool coordinateInRange(Coordinate c) {
	return ((0 <= c.row && c.row < g_rows) && (0 <= c.column && c.column < g_columns));
}

void tryInsertNeighbor(
		Coordinate coordinate, map<Coordinate, Cluster> coordinateToCluster, set<Cluster>* neighbors) {
	if (coordinateInRange(coordinate)) {
		neighbors->insert(coordinateToCluster[coordinate]);
	}
}

Cluster combine(Cluster cluster1, Cluster cluster2) {
	/* Create new cluster.
	 * 		Vector = average(?) of input vectors.
	 * 		Documents set = union of input documents sets.
	 * 		Neighbors set = union of input neighbors sets.
	 * 			Note the input clusters would be in this.. specifically exclude them or don't bother?
	 */
}

bool allSizesSufficient(priority_queue<Cluster> clusters) {
	/* Check all top-level clusters have size >= minimum size. */
}

void mergeToSize(float* codebook, calibrated_data_t* calibratedData) {
	cout << "mergeToSize starting at " << getAsctime() << endl;

	const int numberOfNodes = g_rows * g_columns;
	const int flatSize = numberOfNodes * g_dim;

	float* recentSquaredNorms = new float[numberOfNodes];
	zeroOut(recentSquaredNorms, numberOfNodes);
	calculateSquaredNorms(recentSquaredNorms, codebook);

	map<Coordinate, Cluster> coordinateToCluster;

	/* Create one Cluster for each Coordinate (and record the correspondence. */
	for (int row = 0; row < g_rows; row++) {
		for (int column = 0; row < g_columns; column++) {
			Coordinate coordinate(row, column);

			float* node = calculateNodeIndex(codebook, coordinate);

			Cluster cluster(node);

			coordinateToCluster[coordinate] = cluster;
		}
	}

	/* Set the initial neighbors for each Cluster. */
	for (int row = 0; row < g_rows; row++) {
		for (int column = 0; row < g_columns; column++) {
			Coordinate coordinate(row, column);

			Cluster center = coordinateToCluster[coordinate];

			set<Cluster> neighbors;
			tryInsertNeighbor(upLeft(coordinate), coordinateToCluster, &neighbors);
			tryInsertNeighbor(upRight(coordinate), coordinateToCluster, &neighbors);
			tryInsertNeighbor(dueLeft(coordinate), coordinateToCluster, &neighbors);
			tryInsertNeighbor(dueRight(coordinate), coordinateToCluster, &neighbors);
			tryInsertNeighbor(downLeft(coordinate), coordinateToCluster, &neighbors);
			tryInsertNeighbor(downRight(coordinate), coordinateToCluster, &neighbors);

			center.neighbors = neighbors;
		}
	}

	/* TODO Read calibrated data and assign documents to clusters. */
	for (calibrated_data_t::const_iterator it = calibratedData->begin();
			it != calibratedData->end();
			it++) {
		CalibratedDatum calibratedDatum = *it;

		// TODO
	}


	bool allSizesSufficient = false;
	while (!allSizesSufficient) {
		// Pop smallest cluster off of heap.

		/* If cluster is top-level:
		 * 		Find the closest (top-level) neighbor.
		 *		Combine this cluster with that one.
		 *		Mark new cluster top-level and old clusters not.
		 *		Push the new cluster on to the heap.
		 *		If the resulting cluster has sufficient size, check the end condition.
		 */
	}



	cout << "mergeToSize finishing at " << getAsctime() << endl;
}

int main(int argc, char *argv[]) {
	cout << "MergeClustersToSize starting at " << getAsctime() << endl;

	readConfigurationFile();

	float* codebook = loadCodebook();

	calibrated_data_t* calibratedData = loadCalibratedData();

	mergeToSize(codebook, calibratedData);

	cout << "MergeClustersToSize finishing at " << getAsctime() << endl;

	return 0;
}
