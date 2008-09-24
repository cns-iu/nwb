//============================================================================
// Name        : create_bibliographic_coupling_network.cpp
// Author      : Bruce Herr (bh2@bh2.net), Todd Holloway
// Version     : 1.0
// Copyright   : TBD
// Description : Bibliographic coupling network creation for Wikipedia. Modeled 
//     			 after Todd Holloway's Perl-based bib-coupling network creator.
//============================================================================

#include <iostream>
#include <set>
#include <vector>
#include <algorithm>
#include <map>
#include <string>
#include <sstream>
#include <fstream>
#include <iomanip>
#include <math.h>
using namespace std;

int main(int argc, char *argv[]) {
    char* ARTICLE_NETWORK;
    char* SIMILARITY_NETWORK;
    char DELIMITER = '|';
    int MAX_EDGES = -1;
    
	if (argc == 3) {
		ARTICLE_NETWORK = argv[1];
		SIMILARITY_NETWORK = argv[2];
	} else if (argc == 4) {
	    MAX_EDGES = atoi(argv[1]);
		ARTICLE_NETWORK = argv[2];
		SIMILARITY_NETWORK = argv[3];
		
		if (MAX_EDGES == 0) {
		  MAX_EDGES = -1;
		}
	} else {
		cout << argv[0] << ": <maxEdgesPerNode (optional)> <articleNetworkFile> <outSimilarityFile.sim>" << endl;
		return -1;
	}
	
	map< int, vector<int> > n2citations;
	map< int, vector<int> > n2reverseCitations;
	
	//read in article network
	ifstream in(ARTICLE_NETWORK);
	
	int n1, n2;
	string line;

	int numEdges = 0;
	while (getline(in, line)) {
		replace(line.begin(), line.end(), DELIMITER, ' ');
		istringstream linestream(line);

		linestream >> n1 >> n2;
		
		n2citations[n1].push_back(n2);
		n2reverseCitations[n2].push_back(n1);
		numEdges++;
	}
	cout << n2citations.size() << " nodes, "<< numEdges << " edges read from network." << endl;
	in.close();
	
	//remove all duplicate entries in the citations
	map< int, vector<int> >::iterator i;
	vector<int> citations;
	int node1;
	
	for (i = n2citations.begin(); i != n2citations.end(); i++) {
	   node1 = i->first;
	   
	   sort(n2citations[node1].begin(),n2citations[node1].end());
	   n2citations[node1].erase(unique(n2citations[node1].begin(),n2citations[node1].end()),n2citations[node1].end());
	}
	for (i = n2reverseCitations.begin(); i != n2reverseCitations.end(); i++) {
	   node1 = i->first;
	   
	   sort(n2reverseCitations[node1].begin(),n2reverseCitations[node1].end());
	   n2reverseCitations[node1].erase(unique(n2reverseCitations[node1].begin(),n2reverseCitations[node1].end()),n2reverseCitations[node1].end());
	}
	
	//compute and output bibliographically coupled similarity network
	ofstream out(SIMILARITY_NETWORK);
	
	vector<int>::iterator j;
	vector<int>::iterator k;
	map<int,int>::iterator l;
	multimap<double,int>::reverse_iterator m;
	double sim;
	int node2, citedNode, citationCount1, citationCount2, sharedCitations, edges_written;
		
	map< int, set<int> > seen;
	int num = 0, num_scores = 0;
	unsigned long int total_edges = 0;
	for (i = n2citations.begin(); i != n2citations.end(); i++) {
		node1 = i->first;
		citationCount1 = n2citations[node1].size();
		
		map<int,int> biblioCounts;
		multimap<double,int> simScores;
		num++;
		
		//periodic feedback
		if (num % 100 == 0) {
			//cout << num << " nodes, " << num_scores << " scores written" << endl; 
			num_scores = 0;
		}
		
		//find and compute counts of bibliographically coupled nodes
		for (j = n2citations[node1].begin(); j != n2citations[node1].end(); j++) {
			citedNode = *j;
			
			for (k = n2reverseCitations[citedNode].begin(); k != n2reverseCitations[citedNode].end(); k++) {
				node2 = *k;
				
				if (biblioCounts.count(node2) == 0) { //if doesn't exist
					biblioCounts[node2] = 1;
				} else {
					biblioCounts[node2]++;
				}
			}
		}
		
		//compute similarity of bibliographically coupled nodes
		for (l = biblioCounts.begin(); l != biblioCounts.end(); l++) {
			node2 = l->first;
			
			if (node1 != node2) {
				citationCount2 = n2citations[node2].size();
				sharedCitations = l->second;
				//sim = ((double)(2*sharedCitations)) / (double)(citationCount1 + citationCount2);
				sim = ((double)sharedCitations) / sqrt((double)citationCount1*citationCount2);
				
				if (sim >= (double)0.001) {
					simScores.insert(pair<double,int>(sim,node2));
									    
					num_scores++;
				}
			}
		}
		
		//write out the sim scores in descending similarity order
		
		//we iterate through all of the sim scores, even if above 
		//MAX_EDGES so that the seen structure can eliminate edges
		//that were written before, but may not be as high on the 
		//similarity priority as this node.
		edges_written = 0;
		for (m = simScores.rbegin(); m != simScores.rend(); m++) {
		    sim = m->first;
		    node2 = m->second;
		    
		    if (seen[node1].erase(node2) == 0 && (MAX_EDGES == -1 || edges_written < MAX_EDGES)) {
		    	out << node1 << '\t' << node2 << '\t' << fixed << setprecision(3) << sim << endl;
		    	
		    	seen[node2].insert(node1);
		    	total_edges++;
		    }
		    edges_written++;
		}
	}
	
	out.close();
	cout << total_edges << " similarity scores written." << endl; 
	
	return 0;
}
