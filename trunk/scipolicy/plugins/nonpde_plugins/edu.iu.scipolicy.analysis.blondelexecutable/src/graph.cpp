// File: graph.cpp
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

#include "graph.h"

using namespace std;

Graph::Graph(char *filename, int type) {
  ifstream finput;
  finput.open(filename,fstream::in);

  int nb_links=0;

  while (!finput.eof()) {
    if (nb_links%10000000==0) {cerr << nb_links << endl; fflush(stderr);}

    unsigned int src, dest, weight=1;

    if (type==WEIGHTED)
      finput >> src >> dest >> weight;
    else
      finput >> src >> dest;
    
    if (finput) {
		// This ensures that links is big enough to hold all nodes, given the provided node ids as indexes into it.
      if (links.size()<=max(src,dest)+1)
        links.resize(max(src,dest)+1);
      
      links[src].push_back(make_pair(dest,weight));
      links[dest].push_back(make_pair(src,weight));
      nb_links++;
    }
  }

  finput.close();
}

void Graph::renumber(int type) {
	vector<bool> nodeIsLinked(links.size(), false);
	vector<int> renumberedNodes(links.size(), -1);
	int currentRenumberingIndex = 0;

	// Mark nodes found in edges as being linked (in nodeIsLinked).
	for (unsigned int sourceNodeIndex = 0; sourceNodeIndex < links.size(); sourceNodeIndex++) {
		for (unsigned int edgeIndex = 0; edgeIndex < links[sourceNodeIndex].size(); edgeIndex++) {
			nodeIsLinked[sourceNodeIndex] = true;

			int targetNodeIndex = links[sourceNodeIndex][edgeIndex].first;
			nodeIsLinked[targetNodeIndex] = true;
		}
	}
	
	// Linearly renumber the marked (as being linked) nodes, but using renumberedNodes.
	for (unsigned int nodeIndex = 0; nodeIndex < links.size(); nodeIndex++) {
		if (nodeIsLinked[nodeIndex]) {
			// cerr << "Node " << nodeIndex << " is getting renumbered to " << currentRenumberingIndex << endl;
			renumberedNodes[nodeIndex] = currentRenumberingIndex;
			currentRenumberingIndex++;
		}
	}

	// Update the edges to refer to the renumbered nodes.
	for (unsigned int sourceNodeIndex = 0; sourceNodeIndex < links.size(); sourceNodeIndex++) {
		if (nodeIsLinked[sourceNodeIndex]) {
			int renumberedSourceNodeIndex = renumberedNodes[sourceNodeIndex];

			for (unsigned int edgeIndex = 0; edgeIndex < links[sourceNodeIndex].size(); edgeIndex++) {
				int targetNodeIndex = links[sourceNodeIndex][edgeIndex].first;
				int renumberedTargetNodeIndex = renumberedNodes[targetNodeIndex];
				links[sourceNodeIndex][edgeIndex].first = renumberedTargetNodeIndex;
			}
			
			links[renumberedSourceNodeIndex] = links[sourceNodeIndex];
		}
	}
	
	links.resize(currentRenumberingIndex);
}

void Graph::clean(int type) {
	const size_t numNodes = links.size();

	int totalNodeCount = 0;

	for (size_t ii = 0; ii < numNodes; ii++) {
		for (size_t jj = 0; jj < links[ii].size(); jj++) {
			totalNodeCount++;
		}
	}

	// For all nodes...
	for (unsigned int i = 0; i < numNodes; i++) {
		// Just print a dot for the first one.  This is dumb.
		if (i % 10000000 == 0) {
			fflush(stderr);
		}

		map<int, int> edges;

		// For all edges for this node.
		for (size_t j = 0; j < links[i].size(); j++) {
			// Find the current edge.
			map<int, int>::iterator it = edges.find(links[i][j].first);
			
			// If the current edge wasn't found, insert it in.
			if (it == edges.end()) {
				edges.insert(make_pair(links[i][j].first, links[i][j].second));
			}
			// Otherwise, the current edge was found so sum its weight with the
			// instance we found if this is a weighted network.
			// (If it's not a weighted network, just leave the weight as one,
			// which was set in the constructor.)
			else if (type == WEIGHTED) {
				it->second += links[i][j].second;
			}
		}

		vector<pair<int,int> > cleanedEdges;

		for (map<int, int>::iterator it = edges.begin(); it != edges.end(); it++) {
			cleanedEdges.push_back(*it);
		}
		
		links[i].clear();
		links[i] = cleanedEdges;
	}
}

void Graph::display(int type) {
  for (unsigned int i=0 ; i<links.size() ; i++) {
    for (unsigned int j=0 ; j<links[i].size() ; j++) {
      int dest   = links[i][j].first;
      int weight = links[i][j].second;
      if (type==WEIGHTED)
	cout << i << " " << dest << " " << weight << endl;
      else
	cout << i << " " << dest << endl;
    }
  }
}

void Graph::display_binary(char* filename, int type) {
	ofstream foutput;
	foutput.open(filename, fstream::out | fstream::binary);
	long numBytesWritten = 0;

	// Write the node count.

	unsigned int nodeCount = links.size();

	foutput.write((char*)(&nodeCount), 4);
	numBytesWritten += 4;

	int totalEdgeCount = 0;

	// Write the edge count for each node (kind of).
	for (unsigned int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++) {
		int edgeCountForThisNode = links[nodeIndex].size();
		totalEdgeCount += edgeCountForThisNode;
		foutput.write((char*)(&totalEdgeCount), 4);
		numBytesWritten += 4;
	}

	// Write the target node id for every node.
	for (unsigned int sourceNodeIndex = 0; sourceNodeIndex < nodeCount; sourceNodeIndex++) {
		for (unsigned int edgeIndex = 0; edgeIndex < links[sourceNodeIndex].size(); edgeIndex++) {
			int targetNodeIndex = links[sourceNodeIndex][edgeIndex].first;
			foutput.write((char*)(&targetNodeIndex), 4);
			numBytesWritten += 4;
		}
	}

	// Write the weight for each edge for each node.
	if (type == WEIGHTED) {
		for (unsigned int sourceNodeIndex = 0; sourceNodeIndex < nodeCount; sourceNodeIndex++) {
			for (unsigned int edgeIndex = 0; edgeIndex < links[sourceNodeIndex].size(); edgeIndex++) {
				// int targetNodeIndex = links[sourceNodeIndex][edgeIndex].first;
				int weight = links[sourceNodeIndex][edgeIndex].second;
				foutput.write((char*)(&weight), 4);
				numBytesWritten += 4;
			}
		}
	}

	foutput.close();
}

