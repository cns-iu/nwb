//============================================================================
// Name        : create_cocitation_network.cpp
// Author      : Bruce Herr (bh2@bh2.net), Todd Holloway
// Version     : 1.0
// Copyright   : TBD
// Description : Co-citation network creation for Wikipedia. Modeled after 
//     			 Todd Holloway's Perl-based co-citation network creator.
//============================================================================

#include <iostream>
#include <set>
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
	
	map< int, set<int> > n2citations;
	map< int, set<int> > n2reverseCitations;
	
	//read in article network
	ifstream in(ARTICLE_NETWORK);
	
	int n1, n2;
	string line;

	int numEdges = 0;
	while (getline(in, line)) {
		replace(line.begin(), line.end(), DELIMITER, ' ');
		istringstream linestream(line);

		linestream >> n1 >> n2;
		
		n2citations[n1].insert(n2);
		n2reverseCitations[n2].insert(n1);
		numEdges++;
	}
	cout << n2citations.size() << " nodes, "<< numEdges << " edges read from network." << endl;
	in.close();
	
	//compute and output cocitation similarity network
	ofstream out(SIMILARITY_NETWORK);
	
	map< int, set<int> >::iterator i;
	set<int>::iterator j;
	set<int>::iterator k;
	map<int,int>::iterator l;
	multimap<double,int>::reverse_iterator m;
	double sim;
	int node1, node2, citingNode, citationCount1, citationCount2, sharedCitations, edges_written;
	
	map< int, set<int> > seen;
	int num = 0, num_scores = 0;
	unsigned long int total_edges = 0;
	for (i = n2citations.begin(); i != n2citations.end(); i++) {
		node1 = i->first;
		
		if (n2reverseCitations.count(node1) > 0) {
			citationCount1 = n2reverseCitations[node1].size();
			map<int,int> cocitationCounts;
			multimap<double,int> simScores;
			num++;
			
			//periodic feedback
			if (num % 100 == 0) {
				//cout << num << " nodes, " << num_scores << " scores written" << endl; 
				num_scores = 0;
			}
			
			//find and compute counts of cocited nodes
			for (j = n2reverseCitations[node1].begin(); j != n2reverseCitations[node1].end(); j++) {
				citingNode = *j;
				
				for (k = n2citations[citingNode].begin(); k != n2citations[citingNode].end(); k++) {
					node2 = *k;
					
					if (cocitationCounts.count(node2) == 0) { //if doesn't exist
						cocitationCounts[node2] = 1;
					} else {
						cocitationCounts[node2]++;
					}
				}
			}
			
			//compute similarity of cocited nodes
			for (l = cocitationCounts.begin(); l != cocitationCounts.end(); l++) {
				node2 = l->first;
				
				if (node1 != node2) {
					citationCount2 = n2reverseCitations[node2].size();
					sharedCitations = l->second;
					//sim = ((double)(2*sharedCitations)) / (double)(citationCount1 + citationCount2);
					sim = ((double)sharedCitations) / sqrt((double)citationCount1*citationCount2);
					
					if (sim >= (double)0.001 && seen[node1].erase(node2) == 0) {
						simScores.insert(pair<double,int>(sim,node2));
						seen[node2].insert(node1);
										    
						num_scores++;
					}
				}
			}
			
			//write out the sim scores in descending similarity order
			edges_written = 0;
			for (m = simScores.rbegin(); m != simScores.rend() && edges_written != MAX_EDGES; m++) {
			    sim = m->first;
			    node2 = m->second;
			    	
			    out << node1 << '\t' << node2 << '\t' << fixed << setprecision(3) << sim << endl;
    			edges_written++;
			}
			total_edges += edges_written;
		}
	}
	
	out.close();
	cout << total_edges << " similarity scores written." << endl; 
	
	return 0;
}
