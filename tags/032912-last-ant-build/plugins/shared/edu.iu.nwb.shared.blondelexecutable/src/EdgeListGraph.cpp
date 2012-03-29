// File: EdgeListGraph.cpp
// -- simple graph handling source file
//-----------------------------------------------------------------------------
// Community detection
// Based on the article "Fast unfolding of community hierarchies in large networks"
// Copyright (C) 2008 V. Blondel, J.-L. Guillaume, R. Lambiotte, E. Lefebvre
//
// This program must not be distributed without agreement of the above mentionned authors.
//-----------------------------------------------------------------------------
// Author   : E. Lefebvre, adapted by J.-L. Guillaume
// Email    : jean-loup.guillaume@lip6.fr
// Location : Paris, France
// Time	    : February 2008
//-----------------------------------------------------------------------------
// see readme.txt for more details

#include "EdgeListGraph.h"

#include <sstream>
#include <string>


using namespace std;

// #define _DEBUG_PRINT_
// #define _LOG_PRINT_

// TODO: Continue refactoring this.

void debugPrint(string message) {
#ifdef _DEBUG_PRINT_
	cerr << "(DEBUG) " << message << endl;
#endif // _DEBUG_PRINT_
}

void debugPrint(string messageString, int value) {
	ostringstream messageStream;
	messageStream << messageString << value;
	debugPrint(messageStream.str());
}

void debugPrint(int value, string messageString) {
	ostringstream messageStream;
	messageStream << value << messageString;
	debugPrint(messageStream.str());
}

void debugPrint(string prefix, int value, string messageString) {
	ostringstream messageStream;
	messageStream << prefix << value << messageString;
	debugPrint(messageStream.str());
}

void logPrint(string message) {
#ifdef _LOG_PRINT_
	clog << "(LOG) " << message << endl;
#endif // _DEBUG_PRINT_
}

void logPrint(string messageString, int value) {
	ostringstream messageStream;
	messageStream << messageString << value;
	logPrint(messageStream.str());
}

void logPrint(int value, string messageString) {
	ostringstream messageStream;
	messageStream << value << messageString;
	logPrint(messageStream.str());
}

void logPrint(string prefix, int value, string messageString) {
	ostringstream messageStream;
	messageStream << prefix << value << messageString;
	logPrint(messageStream.str());
}

void logPrint(string prefix, int value1, string messageString, int value2) {
	ostringstream messageStream;
	messageStream << prefix << value1 << messageString << value2;
	logPrint(messageStream.str());
}

void logPrintBytes(int bytes, long filePointer) {
	unsigned char firstByte = (unsigned char)(0xff & bytes);
	unsigned char secondByte = (unsigned char)(0xff & (bytes >> 8));
	unsigned char thirdByte = (unsigned char)(0xff & (bytes >> 16));
	unsigned char fourthByte = (unsigned char)(0xff & (bytes >> 24));
	ostringstream messageStream;
	messageStream << "Location: " << filePointer << endl;
	messageStream << "\tInt: " << (int)firstByte << " " << (int)secondByte << " " << (int)thirdByte << " " << (int)fourthByte;
	logPrint(messageStream.str());
}

EdgeListGraph::EdgeListGraph(string fileName, EDGE_LIST_GRAPH_TYPE type)
:		type(type) {
	ifstream inputFileStream;
	inputFileStream.open(fileName.c_str(), fstream::in);

	int linkCount = 0;

	while (!inputFileStream.eof()) {
		unsigned int sourceNodeIndex;
		unsigned int targetNodeIndex;
		unsigned int edgeWeight = 1;

		inputFileStream >> sourceNodeIndex >> targetNodeIndex;

		if (type == EDGE_LIST_GRAPH_TYPE_WEIGHTED) {
			inputFileStream >> edgeWeight;
		}

		if (inputFileStream) {
			logPrint("Found source node ", sourceNodeIndex);
			logPrint("Found target node ", targetNodeIndex);

			/*
			 * This ensures that this->links is big enough to hold all nodes,
			 *  given the provided node ids as indexes into it.
			 */
			unsigned int higherNodeIndex = max(sourceNodeIndex, targetNodeIndex);
			unsigned int requiredLinkCount = higherNodeIndex + 1;

			if (this->links.size() <= requiredLinkCount) {
				this->links.resize(requiredLinkCount);
			}

			this->links[sourceNodeIndex].push_back(
				make_pair(targetNodeIndex, edgeWeight));

			this->links[targetNodeIndex].push_back(
				make_pair(sourceNodeIndex, edgeWeight));

			linkCount++;
		}
	}

	logPrint(linkCount, " links");

	inputFileStream.close();
}

void EdgeListGraph::clean() {
	const size_t nodeCount = this->links.size();

	int totalNodeCount = 0;

	for (size_t ii = 0; ii < nodeCount; ii++) {
		for (size_t jj = 0; jj < this->links[ii].size(); jj++) {
			totalNodeCount++;
		}
	}

	// For all nodes...
	for (unsigned int ii = 0; ii < nodeCount; ii++) {
		unsigned int sourceNodeIndex = ii;
		
		map<int, int> edges;

		// For all edges for this node.
		for (size_t jj = 0; jj < this->links[sourceNodeIndex].size(); jj++) {
			unsigned int targetNodeIndex = jj;

			// Find the current edge.
			map<int, int>::iterator edgeIterator = edges.find(
				this->links[sourceNodeIndex][targetNodeIndex].first);
			
			// If the current edge wasn't found, insert it in.
			if (edgeIterator == edges.end()) {
				edges.insert(make_pair(
					this->links[sourceNodeIndex][targetNodeIndex].first,
					this->links[sourceNodeIndex][targetNodeIndex].second));
			}
			/*
			 * Otherwise, the current edge was found so sum its edgeWeight with
			 *  the instance we found if this is a weighted network.
			 * (If it's not a weighted network, just leave the edgeWeight as
			 *  one, which was set in the constructor.)
			 */
			else if (type == EDGE_LIST_GRAPH_TYPE_WEIGHTED) {
				edgeIterator->second +=
					this->links[sourceNodeIndex][targetNodeIndex].second;
			}
		}

		vector<pair<int,int> > cleanedEdges;

		for (map<int, int>::iterator edgeIterator = edges.begin();
			 edgeIterator != edges.end();
			 edgeIterator++) {

			cleanedEdges.push_back(*edgeIterator);
		}
		
		this->links[sourceNodeIndex].clear();
		this->links[sourceNodeIndex] = cleanedEdges;
	}
}

void EdgeListGraph::renumber() {
	vector<bool> nodeIsLinked(this->links.size(), false);
	vector<int> renumberedNodes(this->links.size(), -1);
	int currentRenumberingIndex = 0;

	// Mark nodes found in edges as being linked (in nodeIsLinked).
	for (unsigned int sourceNodeIndex = 0; sourceNodeIndex < this->links.size(); sourceNodeIndex++) {
		for (unsigned int edgeIndex = 0; edgeIndex < this->links[sourceNodeIndex].size(); edgeIndex++) {
			nodeIsLinked[sourceNodeIndex] = true;

			int targetNodeIndex = this->links[sourceNodeIndex][edgeIndex].first;
			nodeIsLinked[targetNodeIndex] = true;
		}
	}
	
	// Linearly renumber the marked (as being linked) nodes, but using renumberedNodes.
	for (unsigned int nodeIndex = 0; nodeIndex < this->links.size(); nodeIndex++) {
		if (nodeIsLinked[nodeIndex]) {
			renumberedNodes[nodeIndex] = currentRenumberingIndex;
			currentRenumberingIndex++;
		}
	}

	// Update the edges to refer to the renumbered nodes.
	for (unsigned int sourceNodeIndex = 0; sourceNodeIndex < this->links.size(); sourceNodeIndex++) {
		if (nodeIsLinked[sourceNodeIndex]) {
			int renumberedSourceNodeIndex = renumberedNodes[sourceNodeIndex];

			for (unsigned int edgeIndex = 0; edgeIndex < this->links[sourceNodeIndex].size(); edgeIndex++) {
				int targetNodeIndex = this->links[sourceNodeIndex][edgeIndex].first;
				int renumberedTargetNodeIndex = renumberedNodes[targetNodeIndex];
				this->links[sourceNodeIndex][edgeIndex].first = renumberedTargetNodeIndex;
			}
			
			this->links[renumberedSourceNodeIndex] = this->links[sourceNodeIndex];
		}
	}
	
	this->links.resize(currentRenumberingIndex);
}

void EdgeListGraph::display() {
	for (unsigned int ii = 0; ii < this->links.size(); ii++) {
		for (unsigned int jj = 0; jj < this->links[ii].size(); jj++) {
			int targetNodeIndex = this->links[ii][jj].first;
			int edgeWeight = this->links[ii][jj].second;
			
			if (type == EDGE_LIST_GRAPH_TYPE_WEIGHTED) {
				cout << ii << " " << targetNodeIndex << " " << edgeWeight << endl;
			} else {
				cout << ii << " " << targetNodeIndex << endl;
			}
		}
	}
}

void EdgeListGraph::displayBinary(char* filename) {
	ofstream foutput;
	foutput.open(filename, fstream::out | fstream::binary);
	long numBytesWritten = 0;

	// Write the node count.

	unsigned int nodeCount = this->links.size();

	foutput.write((char*)(&nodeCount), 4);
	logPrintBytes(nodeCount, numBytesWritten);
	numBytesWritten += 4;

	logPrint(nodeCount, " nodes");

	int totalEdgeCount = 0;

	logPrint("Edge counts:");

	// Write the edge count for each node (kind of).
	for (unsigned int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++) {
		int edgeCountForThisNode = this->links[nodeIndex].size();
		logPrint("\tNode ", nodeIndex, ": ", edgeCountForThisNode);
		totalEdgeCount += edgeCountForThisNode;
		foutput.write((char*)(&totalEdgeCount), 4);
		logPrintBytes(totalEdgeCount, numBytesWritten);
		numBytesWritten += 4;
	}

	logPrint("Node IDs:");

	// Write the target node id for every node.
	for (unsigned int sourceNodeIndex = 0; sourceNodeIndex < nodeCount; sourceNodeIndex++) {
		for (unsigned int edgeIndex = 0; edgeIndex < this->links[sourceNodeIndex].size(); edgeIndex++) {
			int targetNodeIndex = this->links[sourceNodeIndex][edgeIndex].first;
			logPrint("\tSource node: ", sourceNodeIndex, "; Target node: ", targetNodeIndex);
			int edgeWeight = this->links[sourceNodeIndex][edgeIndex].second;
			logPrint("\t\tWeight: ", edgeWeight);
			foutput.write((char*)(&targetNodeIndex), 4);
			logPrintBytes(targetNodeIndex, numBytesWritten);
			numBytesWritten += 4;
		}
	}

	// Write the edgeWeight for each edge for each node.
	if (type == EDGE_LIST_GRAPH_TYPE_WEIGHTED) {
		for (unsigned int sourceNodeIndex = 0; sourceNodeIndex < nodeCount; sourceNodeIndex++) {
			for (unsigned int edgeIndex = 0; edgeIndex < this->links[sourceNodeIndex].size(); edgeIndex++) {
				int edgeWeight = this->links[sourceNodeIndex][edgeIndex].second;
				foutput.write((char*)(&edgeWeight), 4);
				logPrintBytes(edgeWeight, numBytesWritten);
				numBytesWritten += 4;
			}
		}
	}

	foutput.close();
}

