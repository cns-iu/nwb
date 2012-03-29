// File: EdgeListGraph.h
// -- simple graph handling header file
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

#ifndef EDGE_LIST_GRAPH_H
#define EDGE_LIST_GRAPH_H

#include <stdlib.h>
#include <stdio.h>
#include <iostream>
#include <iomanip>
#include <fstream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>


using namespace std;

typedef enum EDGE_LIST_GRAPH_TYPE {
	EDGE_LIST_GRAPH_TYPE_WEIGHTED = 0,
	EDGE_LIST_GRAPH_TYPE_UNWEIGHTED,
};

class EdgeListGraph {
public:
	// I *think* each vector<pair<int, int> > is a set of edges for that node.
	vector<vector<pair<int, int> > > links;

	EdgeListGraph(string fileName, EDGE_LIST_GRAPH_TYPE type);

	void clean();
	void renumber();
	void display();
	void displayBinary(char* filename);

private:
	EDGE_LIST_GRAPH_TYPE type;
};

#endif // EDGE_LIST_GRAPH_H
