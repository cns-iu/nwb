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

#define ROW(coord) ((coord).first)
#define COLUMN(coord) ((coord).second)

typedef int cluster_id_t;
typedef pair<cluster_id_t, cluster_id_t> neighbor_pair_t;

typedef int document_id_t;

typedef pair<int, int> coordinate_t;

string g_codebookPath;
string g_inCalibratedDataPath;
int g_requestedMinimumSize;
string g_outClusterDataPath;

int g_rows = -1;
int g_columns = -1;
int g_dim = -1;

int g_numberOfVectors = -1;


cluster_id_t g_nextAvailableClusterID = 0;
/* These structures are indexed by cluster_id_t. */
vector<float*> g_vectors;
vector<set<document_id_t> > g_documents;
vector<set<cluster_id_t> > g_neighbors;
vector<set<coordinate_t> > g_coordinates;


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

class NearestNeighborCompare {
public:
	bool operator() (const neighbor_pair_t& neighbors1, const neighbor_pair_t& neighbors2) const {
		float dissimilarity1 =
				calculateCosineDissimilarity(
						g_vectors[neighbors1.first], g_vectors[neighbors1.second]);
		float dissimilarity2 =
				calculateCosineDissimilarity(
						g_vectors[neighbors2.first], g_vectors[neighbors2.second]);

		return (dissimilarity1 > dissimilarity2);
	}
};

typedef priority_queue<neighbor_pair_t, vector<neighbor_pair_t>, NearestNeighborCompare> nearest_neighbor_heap_t;


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

void readCodebook(
		map<coordinate_t, cluster_id_t>* coordinateToCluster,
		set<cluster_id_t>* topLevelClusters) {
	cout << "Starting codebook load at " << getAsctime() << endl;

	ifstream codebookFile(g_codebookPath.c_str());
	if (codebookFile.is_open()) {
		string firstLine;
		getline(codebookFile, firstLine);
		istringstream firstLineStream(firstLine);

		// Read parameters on first line.
		string topology; // Ignored for now.
		string neighborhood; // Ignored for now.
		firstLineStream >> g_dim >> topology >> g_columns >> g_rows >> neighborhood;

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
			coordinate_t coordinate(row, column);


			/* Create a new cluster and initialize its data. */
			cluster_id_t cluster = g_nextAvailableClusterID++;

			g_vectors.push_back(vector);
			g_documents.push_back(set<document_id_t>());
			g_neighbors.push_back(set<cluster_id_t>());
			g_coordinates.push_back(set<coordinate_t>());
			g_coordinates[cluster].insert(coordinate);

			topLevelClusters->insert(cluster);
			(*coordinateToCluster)[coordinate] = cluster;

			lineNumber++;
		}

		codebookFile.close();
	} else {
		cerr << "Error opening codebook file!" << endl;
		exit(1);
	}

	cout << "Finishing initial codebook load at " << getAsctime() << endl;
}

void readCalibratedData(map<coordinate_t, cluster_id_t>* coordinateToCluster) {
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

			int documentID;
			linestream >> documentID;

			int row;
			int column;
			linestream >> row;
			linestream >> column;
			coordinate_t coordinate(row, column);

			cluster_id_t cluster = (*coordinateToCluster)[coordinate];
			g_documents[cluster].insert(documentID);

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

int up(int row) {
	return row - 1;
}

int down(int row) {
	return row + 1;
}

int dueLeft(int column) {
	return column - 1;
}
int slantLeft(coordinate_t coord) {
	int slantLeftColumn = COLUMN(coord);
	if (ROW(coord) % 2 == 0) {
		slantLeftColumn -= 1;
	}

	return slantLeftColumn;
}

int dueRight(int column) {
	return column + 1;
}
int slantRight(coordinate_t coord) {
	int slantRightColumn = COLUMN(coord);
	if (ROW(coord) % 2 == 1) {
		slantRightColumn += 1;
	}

	return slantRightColumn;
}

coordinate_t upLeft(coordinate_t coord) {
	return coordinate_t(up(ROW(coord)), slantLeft(coord));
}

coordinate_t upRight(coordinate_t coord) {
	return coordinate_t(up(ROW(coord)), slantRight(coord));
}

coordinate_t dueLeft(coordinate_t coord) {
	return coordinate_t(ROW(coord), dueLeft(COLUMN(coord)));
}

coordinate_t dueRight(coordinate_t coord) {
	return coordinate_t(ROW(coord), dueRight(COLUMN(coord)));
}

coordinate_t downLeft(coordinate_t coord) {
	return coordinate_t(down(ROW(coord)), slantLeft(coord));
}

coordinate_t downRight(coordinate_t coord) {
	return coordinate_t(down(ROW(coord)), slantRight(coord));
}

bool coordinateInRange(coordinate_t coord) {
	return ((0 <= ROW(coord) && ROW(coord) < g_rows)
			&& (0 <= COLUMN(coord) && COLUMN(coord) < g_columns));
}

void tryInsertNeighbor(
		cluster_id_t cluster,
		coordinate_t neighborCoord,
		map<coordinate_t, cluster_id_t>* coordinateToCluster,
		nearest_neighbor_heap_t* heap) {
	if (coordinateInRange(neighborCoord)) {
		cluster_id_t neighbor = (*coordinateToCluster)[neighborCoord];
		g_neighbors[cluster].insert(neighbor);
		if (cluster < neighbor) { // TODO Right?
			heap->push(neighbor_pair_t(cluster, neighbor));
		}
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

//cluster_id_t findNearestNeighbor(cluster_id_t center, set<cluster_id_t>* topLevelClusters) {
//	float leastDistance = numeric_limits<float>::max();
//	cluster_id_t nearestNeighbor = -1;
//
//	for (set<cluster_id_t>::iterator neighbors = g_neighbors[center].begin();
//			neighbors != g_neighbors[center].end();
//			neighbors++) {
//		cluster_id_t neighbor = *neighbors;
//
//		if (topLevelClusters->find(neighbor) != topLevelClusters->end()) {
//			float distance = calculateCosineDissimilarity(g_vectors[center], g_vectors[neighbor]);
//
//			if (distance < leastDistance) {
//				leastDistance = distance;
//				nearestNeighbor = neighbor;
//			}
//		}
//	}
//
//	if (nearestNeighbor < 0) {
//		cerr << "Error: Couldn't identify any nearest neighbor for cluster " << center << endl;
////		printCluster(cerr, center);
//		exit(1);
//	}
//
//	return nearestNeighbor;
//}

/* Create new cluster.
 * 		Vector = average(?) of input vectors.
 * 		Documents set = union of input documents sets.
 * 		Neighbors set = union of input neighbors sets.
 * 			Note the input clusters would be in this.. specifically exclude them or don't bother?
 */
cluster_id_t combine(cluster_id_t cluster1, cluster_id_t cluster2) {
	float* meanVector = vectorMean(g_vectors[cluster1], g_vectors[cluster2]);

	cluster_id_t mergedCluster = g_nextAvailableClusterID++;
	g_vectors.push_back(meanVector);

	g_documents.push_back(set<document_id_t>());
	g_documents[mergedCluster].insert(g_documents[cluster1].begin(), g_documents[cluster1].end());
	g_documents[mergedCluster].insert(g_documents[cluster2].begin(), g_documents[cluster2].end());

	g_neighbors.push_back(set<cluster_id_t>());
	g_neighbors[mergedCluster].insert(g_neighbors[cluster1].begin(), g_neighbors[cluster1].end());
	g_neighbors[mergedCluster].insert(g_neighbors[cluster2].begin(), g_neighbors[cluster2].end());
//	g_neighbors[mergedCluster].erase(cluster1); // TODO Necessary?
//	g_neighbors[mergedCluster].erase(cluster2); // TODO Necessary?
	// Tell all of the neighbors I just inherited that I am their new neighbor.
	for (set<cluster_id_t>::iterator neighbors = g_neighbors[mergedCluster].begin();
			neighbors != g_neighbors[mergedCluster].end();
			neighbors++) {
		cluster_id_t neighbor = *neighbors;
		g_neighbors[neighbor].insert(mergedCluster);
	}


	g_coordinates.push_back(set<coordinate_t>());
	g_coordinates[mergedCluster].insert(g_coordinates[cluster1].begin(), g_coordinates[cluster1].end());
	g_coordinates[mergedCluster].insert(g_coordinates[cluster2].begin(), g_coordinates[cluster2].end());

	/* TODO Explicitly destroy cluster1, cluster2?  If so, here or in caller? */

	return mergedCluster;
}

bool mergingIsComplete(set<cluster_id_t>* topLevelClusters) {
	for (set<cluster_id_t>::iterator it = topLevelClusters->begin(); it != topLevelClusters->end(); it++) {
		cluster_id_t cluster = *it;

		if (g_documents[cluster].size() == 0) {
			continue;
		}

		if (g_documents[cluster].size() >= g_requestedMinimumSize) {
			continue;
		}

		return false;
	}

	return true;
}

void writeOutClusterFile(set<cluster_id_t>* topLevelClusters) {
	cout << "Write out to cluster file starting at " << getAsctime() << endl;

	ofstream outClusterFile(g_outClusterDataPath.c_str());
	if (outClusterFile.is_open()) {
		outClusterFile << g_dim << " " << "hexa" << " " << g_columns << " " << g_rows << " " << "gaussian" << endl;

		outClusterFile << "Cluster ID | Contained documents | Contained coordinates | Reference vector" << endl;

		for (set<cluster_id_t>::iterator it = topLevelClusters->begin(); it != topLevelClusters->end(); it++) {
			cluster_id_t cluster = *it;

			/* Cluster ID. */
			outClusterFile << cluster << " ";

			outClusterFile << "| ";


			/* Contained documents. */
			for (set<document_id_t>::iterator it = g_documents[cluster].begin();
					it != g_documents[cluster].end();
					it++) {
				outClusterFile << (*it) << " ";
			}

			outClusterFile << "| ";


			/* Contained coordinates. */
			for (set<coordinate_t>::iterator it = g_coordinates[cluster].begin();
								it != g_coordinates[cluster].end();
								it++) {
				coordinate_t coordinate = *it;

				outClusterFile << ROW(coordinate) << "," << COLUMN(coordinate) << "; ";
			}

			outClusterFile << "| ";


			/* Reference vector. */
			float* vector = g_vectors[cluster];
			for (int ii = 0; ii < g_dim; ii++) {
				outClusterFile << vector[ii] << " ";
			}


			outClusterFile << endl;
		}

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

	map<coordinate_t, cluster_id_t> coordinateToCluster;
	set<cluster_id_t> topLevelClusters;

	readCodebook(&coordinateToCluster, &topLevelClusters);
	readCalibratedData(&coordinateToCluster);

	nearest_neighbor_heap_t heap;

	/* For each initial cluster.. */
	for (int row = 0; row < g_rows; row++) {
		for (int column = 0; column < g_columns; column++) {
			coordinate_t coordinate(row, column);

			/* Set the initial neighbors and push neighboring cluster pairs on to the heap. */
			cluster_id_t center = coordinateToCluster[coordinate];

			tryInsertNeighbor(center, upLeft(coordinate), &coordinateToCluster, &heap);
			tryInsertNeighbor(center, upRight(coordinate), &coordinateToCluster, &heap);
			tryInsertNeighbor(center, dueLeft(coordinate), &coordinateToCluster, &heap);
			tryInsertNeighbor(center, dueRight(coordinate), &coordinateToCluster, &heap);
			tryInsertNeighbor(center, downLeft(coordinate), &coordinateToCluster, &heap);
			tryInsertNeighbor(center, downRight(coordinate), &coordinateToCluster, &heap);
		}
	}

	cout << "Initial heap population finishing at " << getAsctime() << endl;

	bool done = false;
	while (!done) {
		// Read and pop off the cluster with the fewest documents.
		neighbor_pair_t nearestNeighbors = heap.top();
		heap.pop();

		cluster_id_t cluster1 = nearestNeighbors.first;
		cluster_id_t cluster2 = nearestNeighbors.second;

		if ((topLevelClusters.find(cluster1) != topLevelClusters.end())
				&& (topLevelClusters.find(cluster2) != topLevelClusters.end())) {
//			cluster_id_t nearestNeighbor = findNearestNeighbor(smallestCluster, &topLevelClusters);
			cluster_id_t mergedCluster = combine(cluster1, cluster2);

			if (mergedCluster % 500 == 0) {
				cout << "Created merge cluster " << mergedCluster;
				cout << "; nearest neighbors starting this step had dissimilarity ";
				cout << calculateCosineDissimilarity(g_vectors[cluster1], g_vectors[cluster2]);
				cout << "with sizes " << g_documents[cluster1].size() << " and ";
				cout << g_documents[cluster2].size();
				cout << ".  Time is " << getAsctime() << endl;
			}

			topLevelClusters.erase(cluster1);
			topLevelClusters.erase(cluster2);

			topLevelClusters.insert(mergedCluster);

			for (set<cluster_id_t>::iterator neighbors = g_neighbors[mergedCluster].begin();
					neighbors != g_neighbors[mergedCluster].end();
					neighbors++) {
				cluster_id_t neighbor = *neighbors;

				heap.push(neighbor_pair_t(mergedCluster, neighbor));
			}

			if ((g_documents[mergedCluster].size() == 0)
					|| (g_documents[mergedCluster].size() >= g_requestedMinimumSize)) {
				done = mergingIsComplete(&topLevelClusters);
			}
		}
	}

	writeOutClusterFile(&topLevelClusters);

	cout << "mergeToSize finishing at " << getAsctime() << endl;
}

int main(int argc, char *argv[]) {
	cout << "MergeClustersBySimilarity starting at " << getAsctime() << endl;

	readConfigurationFile();

	mergeToSize();

	cout << "MergeClustersBySimilarity finishing at " << getAsctime() << endl;

	return 0;
}
