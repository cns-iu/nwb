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
	float* vector;
	set<string>* documents;
	set<Cluster>* neighbors;
	set<Coordinate>* coordinates;

	Cluster(float* v) {
		vector = v;

		documents = new set<string>();
		neighbors = new set<Cluster>();
		coordinates = new  set<Coordinate>();
	}
};

class DocumentsSizeComparison {
	public:
	const bool operator()(const Cluster& cluster1, const Cluster& cluster2) {
	    return cluster1.documents->size() < cluster2.documents->size();
	}
};

typedef priority_queue<Cluster, vector<Cluster>, DocumentsSizeComparison> smallest_cluster_heap_t;


string g_codebookPath;
string g_inCalibratedDataPath;
int g_requestedMinimumSize;
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
		configurationFile >> g_requestedMinimumSize;
		configurationFile >> g_outClusterDataPath;
	}

	configurationFile.close();

	cout << "Done." << endl;
}

void readCodebook(map<Coordinate, Cluster>* coordinateToCluster, set<Cluster>* topLevelClusters) {
	cout << "Starting codebook load at " << getAsctime() << endl;

	ifstream codebookFile(g_codebookPath.c_str());
	if (codebookFile.is_open()) {
		// Read parameters on first line.
		string topology; // Ignored for now.
		string neighborhood; // Ignored for now.
		codebookFile >> g_dim >> topology >> g_columns >> g_rows >> neighborhood;

		int numberOfNodes = g_rows * g_columns;
//		codebook = new float[numberOfNodes * g_dim];

		const string commentMarker("#");

		int lineNumber = 0;
		string line;
		while (getline(codebookFile, line)) {
			/* Comments are ignored.
			 * They *must not* affect the line number.
			 */
			if (0 == line.find(commentMarker)) {
				continue;
			}

			istringstream linestream(line);

			float* vector = new float[g_dim];
			for (int ii = 0; ii < g_dim; ii++) {
				float value;
				linestream >> value;
				vector[ii] = value;
			}

			int row = ((int) lineNumber / g_columns);
			int column = lineNumber % g_columns;
			Coordinate coordinate(row, column);

			Cluster cluster(vector);
			cluster.coordinates->insert(coordinate);

			topLevelClusters->insert(cluster);
			(*coordinateToCluster)[coordinate] = cluster;


			lineNumber++;
		}

//		for (int i = 0; i < numberOfNodes; i++) {
//			float* currentNode = codebook + i * g_dim;
//
//			bool allZero = true;
//			for (int weightIndex = 0; weightIndex < g_dim; weightIndex++) {
//				codebookFile >> currentNode[weightIndex];
//
//				if (currentNode[weightIndex] != 0) {
//					allZero = false;
//				}
//			}
//
//			if (allZero) {
//				cerr << "ERROR: Codebook contained an all-zero vector.  Quitting now." << endl;
//				exit(1);
//			}
//		}

		codebookFile.close();
	} else {
		cerr << "Error opening codebook file!" << endl;
		exit(1);
	}

	cout << "Finishing initial codebook load at " << getAsctime() << endl;
}

void readCalibratedData(map<Coordinate, Cluster>* coordinateToCluster) {
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

			Cluster bestMatchingNeuronCluster = (*coordinateToCluster)[coordinate];

			bestMatchingNeuronCluster.documents->insert(documentID);

			/* Note the dissimilarity and document vectors are ignored. */

			lineNumber++;
		}

		calibratedDataFile.close();

		cout << "Finishing calibrated dataset load at " << getAsctime() << endl;
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
float calculateCosineDissimilarity(float* vector1, float* vector2) {
	return (1 - calculateCosineSimilarity(vector1, vector2));
}

int up(int row) {
	return row - 1;
}

int down(int row) {
	return row + 1;
}

int dueLeft(int column) {
	return column - 1;
}
int slantLeft(Coordinate coord) {
	int slantLeftColumn = coord.column;
	if (coord.row % 2 == 0) {
		slantLeftColumn -= 1;
	}

	return slantLeftColumn;
}

int dueRight(int column) {
	return column + 1;
}
int slantRight(Coordinate coord) {
	int slantRightColumn = coord.column;
	if (coord.row % 2 == 1) {
		slantRightColumn += 1;
	}

	return slantRightColumn;
}

Coordinate upLeft(Coordinate coord) {
	return Coordinate(up(coord.row), slantLeft(coord));
}

Coordinate upRight(Coordinate coord) {
	return Coordinate(up(coord.row), slantRight(coord));
}

Coordinate dueLeft(Coordinate coord) {
	return Coordinate(coord.row, dueLeft(coord.column));
}

Coordinate dueRight(Coordinate coord) {
	return Coordinate(coord.row, dueRight(coord.column));
}

Coordinate downLeft(Coordinate coord) {
	return Coordinate(down(coord.row), slantLeft(coord));
}

Coordinate downRight(Coordinate coord) {
	return Coordinate(down(coord.row), slantRight(coord));
}

bool coordinateInRange(Coordinate coord) {
	return ((0 <= coord.row && coord.row < g_rows)
			&& (0 <= coord.column && coord.column < g_columns));
}

void tryInsertNeighbor(
		Coordinate coordinate, map<Coordinate, Cluster> coordinateToCluster, set<Cluster>* neighbors) {
	if (coordinateInRange(coordinate)) {
		neighbors->insert(coordinateToCluster[coordinate]);
	}
}

/* TODO Consider recycling input memory. */
float* vectorMean(float* vector1, float* vector2) {
	float* meanVector = new float[g_dim];

	for (int ii = 0; ii < g_dim; ii++) {
		meanVector[ii] = (vector1[ii] + vector2[ii]) / 2.0;
	}

	return meanVector;
}

Cluster findNearestNeighbor(Cluster center, set<Cluster> topLevelClusters) {
	float leastDistance = numeric_limits<float>::max();
	Cluster nearestNeighbor = NULL;

	for (set<Cluster>::iterator neighbors = center.neighbors->begin();
			neighbors != center.neighbors->end();
			neighbors++) {
		Cluster neighbor = *neighbors;

		if (topLevelClusters.find(neighbor) != topLevelClusters.end()) {
			float distance = calculateCosineDissimilarity(center.vector, neighbor.vector);

			if (distance < leastDistance) {
				leastDistance = distance;
				nearestNeighbor = neighbor;
			}
		}
	}

	return nearestNeighbor;
}

/* Create new cluster.
 * 		Vector = average(?) of input vectors.
 * 		Documents set = union of input documents sets.
 * 		Neighbors set = union of input neighbors sets.
 * 			Note the input clusters would be in this.. specifically exclude them or don't bother?
 */
Cluster combine(Cluster cluster1, Cluster cluster2) {
	float* meanVector = vectorMean(cluster1.vector, cluster2.vector);

	Cluster mergedCluster(meanVector);

	mergedCluster.documents->insert(cluster1.documents->begin(), cluster1.documents->end());
	mergedCluster.documents->insert(cluster2.documents->begin(), cluster2.documents->end());

	mergedCluster.neighbors->insert(cluster1.neighbors->begin(), cluster1.neighbors->end());
	mergedCluster.neighbors->insert(cluster2.neighbors->begin(), cluster2.neighbors->end());
	// TODO Necessary?
	mergedCluster.neighbors->erase(cluster1);
	mergedCluster.neighbors->erase(cluster2);

	mergedCluster.coordinates->insert(cluster1.coordinates->begin(), cluster1.coordinates->end());
	mergedCluster.coordinates->insert(cluster2.coordinates->begin(), cluster2.coordinates->end());

	/* TODO Explicitly destroy cluster1, cluster2?  If so, here or in caller? */

	return mergedCluster;
}

bool mergingIsComplete(set<Cluster> topLevelClusters) {
	for (set<Cluster>::iterator it = topLevelClusters.begin(); it != topLevelClusters.end(); it++) {
		Cluster cluster = *it;

		if (cluster.documents->size() < g_requestedMinimumSize) {
			return false;
		}
	}

	return true;
}

void writeOutClusterFile(set<Cluster> topLevelClusters) {
	cout << "Write out to cluster file starting at " << getAsctime() << endl;

	ofstream outClusterFile(g_outClusterDataPath.c_str());
	if (outClusterFile.is_open()) {
		outClusterFile << g_dim << " " << "hexa" << " " << g_columns << " " << g_rows << " " << "gaussian";

		int numberOfNodes = g_rows * g_columns;


		for (set<Cluster>::iterator it = topLevelClusters.begin(); it != topLevelClusters.end(); it++) {
			Cluster cluster = *it;

			/* TODO Consider writing out some of this other stuff. */
//			float* vector;
//			set<string>* documents;
//			set<Cluster>* neighbors;
//			set<Coordinate>* coordinates;
			for (set<string>::iterator it = cluster.documents->begin();
					it != cluster.documents->end();
					it++) {
				outClusterFile << (*it) << " ";
			}

			outClusterFile << endl;
		}

//		for (int i = 0; i < numberOfNodes; i++) {
//			int nodeIndex = i * g_dim;
//
//			outClusterFile << endl;
//
//			for (int weightIndex = 0; weightIndex < g_dim; weightIndex++) {
//				outClusterFile << net[nodeIndex + weightIndex] << " ";
//			}
//		}

		outClusterFile.close();
	} else {
		cerr << "Error writing out cluster file." << endl;
		exit(1);
	}

	cout << "Write out to cluster file finishing at " << getAsctime() << endl;
}

void mergeToSize() {
	cout << "mergeToSize starting at " << getAsctime() << endl;

	const int numberOfNodes = g_rows * g_columns;
	const int flatSize = numberOfNodes * g_dim;

	map<Coordinate, Cluster> coordinateToCluster;
	set<Cluster> topLevelClusters;

	readCodebook(&coordinateToCluster, &topLevelClusters);
	readCalibratedData(&coordinateToCluster);

	/* Extra parentheses necessary here to tell the lexer that DocumentsSizeComparison() is not
	 * an unnamed function declaration..
	 */
	smallest_cluster_heap_t heap((DocumentsSizeComparison()));

	/* For each initial Cluster.. */
	for (int row = 0; row < g_rows; row++) {
		for (int column = 0; row < g_columns; column++) {
			Coordinate coordinate(row, column);

			/* Set the initial neighbors. */
			Cluster center = coordinateToCluster[coordinate];

			set<Cluster> neighbors;
			tryInsertNeighbor(upLeft(coordinate), coordinateToCluster, &neighbors);
			tryInsertNeighbor(upRight(coordinate), coordinateToCluster, &neighbors);
			tryInsertNeighbor(dueLeft(coordinate), coordinateToCluster, &neighbors);
			tryInsertNeighbor(dueRight(coordinate), coordinateToCluster, &neighbors);
			tryInsertNeighbor(downLeft(coordinate), coordinateToCluster, &neighbors);
			tryInsertNeighbor(downRight(coordinate), coordinateToCluster, &neighbors);

			(*(center.neighbors)) = neighbors;

			/* Push on to the heap. */
			heap.push(center);
		}
	}

	bool done = false;
	while (!done) {
		// Read and pop off the cluster with the fewest documents.
		Cluster smallestCluster = heap.top();
		heap.pop();

		if (topLevelClusters.find(smallestCluster) != topLevelClusters.end()) {
			Cluster nearestNeighbor = findNearestNeighbor(smallestCluster, topLevelClusters);

			Cluster mergedCluster = combine(smallestCluster, nearestNeighbor);

			topLevelClusters.erase(smallestCluster);
			topLevelClusters.erase(nearestNeighbor);

			topLevelClusters.insert(mergedCluster);

			heap.push(mergedCluster);

			if (mergedCluster.documents->size() >= g_requestedMinimumSize) {
				done = mergingIsComplete(topLevelClusters);
			}
		}
	}

	writeOutClusterFile(topLevelClusters);

	cout << "mergeToSize finishing at " << getAsctime() << endl;
}

int main(int argc, char *argv[]) {
	cout << "MergeClustersToSize starting at " << getAsctime() << endl;

	readConfigurationFile();

	mergeToSize();

	cout << "MergeClustersToSize finishing at " << getAsctime() << endl;

	return 0;
}
