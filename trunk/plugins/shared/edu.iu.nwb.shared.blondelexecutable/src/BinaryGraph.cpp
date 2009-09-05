// File: graph_binary.cpp
// -- graph handling source
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

#ifdef _WIN32
#include <windows.h>
#include <process.h>
#else
#include <sys/mman.h>
#endif // _WIN32

#include <string>
#include <sstream>

#include "BinaryGraph.h"


// #define _LOG_PRINT_

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

void logPrintBytes(string prefix, int bytes, long locationToPrint) {
	unsigned char firstByte = (unsigned char)(0xff & bytes);
	unsigned char secondByte = (unsigned char)(0xff & (bytes >> 8));
	unsigned char thirdByte = (unsigned char)(0xff & (bytes >> 16));
	unsigned char fourthByte = (unsigned char)(0xff & (bytes >> 24));
	ostringstream messageStream;
	messageStream << "Location: " << locationToPrint << endl;
	messageStream << "\t" << prefix << " ";
	messageStream << "Int: " << (int)firstByte << " " << (int)secondByte << " " << (int)thirdByte << " " << (int)fourthByte;
	messageStream << "; " << bytes;
	logPrint(messageStream.str());
}

void logPrintBytes(string prefix, int bytes, string messageString, long locationToPrint) {
	unsigned char firstByte = (unsigned char)(0xff & bytes);
	unsigned char secondByte = (unsigned char)(0xff & (bytes >> 8));
	unsigned char thirdByte = (unsigned char)(0xff & (bytes >> 16));
	unsigned char fourthByte = (unsigned char)(0xff & (bytes >> 24));
	ostringstream messageStream;
	messageStream << "Location: " << locationToPrint << endl;
	messageStream << "\t" << prefix << " ";
	messageStream << "Int: " << (int)firstByte << " " << (int)secondByte << " " << (int)thirdByte << " " << (int)fourthByte;
	messageStream << "; " << bytes << " " << messageString;
	logPrint(messageStream.str());
}

void logPrintBytes(int bytes, string messageString, long locationToPrint) {
	unsigned char firstByte = (unsigned char)(0xff & bytes);
	unsigned char secondByte = (unsigned char)(0xff & (bytes >> 8));
	unsigned char thirdByte = (unsigned char)(0xff & (bytes >> 16));
	unsigned char fourthByte = (unsigned char)(0xff & (bytes >> 24));
	ostringstream messageStream;
	messageStream << "Location: " << locationToPrint << endl;
	messageStream << "\tInt: " << (int)firstByte << " " << (int)secondByte << " " << (int)thirdByte << " " << (int)fourthByte;
	messageStream << "; " << bytes << " " << messageString;
	logPrint(messageStream.str());
}

void logPrintBytes(int bytes, int value, long locationToPrint) {
	unsigned char firstByte = (unsigned char)(0xff & bytes);
	unsigned char secondByte = (unsigned char)(0xff & (bytes >> 8));
	unsigned char thirdByte = (unsigned char)(0xff & (bytes >> 16));
	unsigned char fourthByte = (unsigned char)(0xff & (bytes >> 24));
	ostringstream messageStream;
	messageStream << "Location: " << locationToPrint << endl;
	messageStream << "\tInt: " << (int)firstByte << " " << (int)secondByte << " " << (int)thirdByte << " " << (int)fourthByte;
	messageStream << "; " << bytes << " (" << value << ")";
	logPrint(messageStream.str());
}

Graph::Graph() {
	nb_nodes = 0;
	nb_links = 0;
	total_weight = 0;
}

Graph::Graph(char* filename, int type) {
	//long locationToPrint = 0;

	ifstream finput;

	finput.open(filename, fstream::in | fstream::binary);

	/*long counter = 0;
	while(!finput.eof()) {
		unsigned char b = finput.get();
		cerr << counter++ << " \'" << b << "\'" << endl;
	}*/

	finput.read((char*)&nb_nodes, 4);
	/*logPrintBytes("total : ", nb_nodes, locationToPrint);
	locationToPrint += 4;*/

	// cerr << "num nodes: " << nb_nodes << endl;
	// Read cumulative degree sequence: 4 bytes for each node.
	/* cum_degree[0] = degree(0);
	cum_degree[1] = degree(0) + degree(1), etc.*/

	// cerr << "int in: " << nb_nodes << endl;

	// degrees = (int*)malloc((long)nb_nodes * 4);
	degrees = new int[(long)nb_nodes];
	finput.read((char*)degrees, (long)nb_nodes * 4);

	/*logPrint("Degrees:");
	for (int ii = 0; ii < nb_nodes; ii++) {
		logPrintBytes(degrees[ii], ii, locationToPrint);
		locationToPrint += 4;
	}*/

	// Read links: 4 bytes for each link (each link is counted twice).
	nb_links = degrees[nb_nodes - 1] / 2;
	// links = (int*)malloc((long)nb_links * 8);
	links = new int[nb_links * 2];
	finput.read((char*)links, (long)nb_links * 8);
	// cerr << "total: " << nb_links << endl;

	/*logPrint("Links:");
	for (int ii = 0; ii < nb_links; ii++) {
		logPrintBytes(links[ii], ii, locationToPrint);
		locationToPrint += 4;
	}*/

	if (type == WEIGHTED) {
		// Read weights: 4 bytes for each link (each link is counted twice).
		// weights = (int*)malloc((long)nb_links * 8);
		weights = new int[nb_links * 2];
		finput.read((char*)weights, (long)nb_links * 8);

		/*logPrint("Weights:");
		for (int ii = 0; ii < nb_links; ii++) {
			logPrintBytes(weights[ii], ii, locationToPrint);
			locationToPrint += 4;
		}*/

		total_weight = 0;
		
		for (int ii = 0; ii < nb_links * 2; ii++) {
			total_weight += weights[ii];
		}
	} else {
		weights = NULL;
		total_weight = nb_links * 2;
	}
}

#ifdef SUCK
// Generates a random graph using the benchmark approach.
Graph::Graph(int n1, int k1, int n2, int k2, int n3, int k3) {
	srand(time(NULL));
	nb_nodes = n1 * n2 * n3;

	vector<vector<int> > gr(nb_nodes);

	for (int ii = 0; ii < nb_nodes; ii++) {
		gr[ii].resize(nb_nodes, 0);
	}

	// cerr << (k1 * 1.0) / (n1 * 1.0) << " ";
	// cerr << (k2 * 1.0) / (n1 * n2 * 1.0) << " ";
	// cerr << (k3 * 1.0) / (n1 * n2 * n3 * 1.0) << endl;

	nb_links = 0;

	for (int ii = 0; ii < nb_nodes; ii++) {
		for (int jj = ii + 1; jj < nb_nodes; jj++) {
			double v = rand() * 1.0 / RAND_MAX;
			
			if (ii / n1 == jj / n1) {
				// ii and jj in the same subgroup.
				// cout << ii << " " << jj << " 1 : ";
				// cout << v << " " << (k1 * 1.0) / (n1 - 1.0);
				if (v <= (k1 * 1.0) / (n1 - 1.0)) {
					gr[ii][jj] = gr[jj][ii] = 1;
					nb_links++;
					// cout << " : ok";
				}
				
				// cout << endl;
			} else if (ii / (n1 * n2) == jj / (n1 * n2)) {
				// ii and jj in the same group.
				// cout << ii << " " << jj << " 2 : ";
				// cout << v << " " << (k2 * 1.0) / (n1 * (n2 - 1.0));
				if (v <= (k2 * 1.0) / (n1 * (n2 - 1.0))) {
					gr[ii][jj] = gr[jj][ii] = 1;
					nb_links++;
					// cout << " : ok";
				}
				
				// cout << endl;
			} else {
				// ii and jj in different groups.
				// cout << ii << " " << jj << " 3 : ";
				// cout << v << " " << (k3 * 1.0) / (n1 * n2 * (n3 - 1.0));
				if (v <= (k3 * 1.0) / (n1 * n2 * (n3 - 1.0))) {
					gr[ii][jj] = gr[jj][ii] = 1;
					nb_links++;
					// cout << " : ok";
				}
				
				// cout << endl;
			}
		}
	}

	// cerr << nb_links << endl;

	total_weight = 2 * nb_links;
	weights = NULL;

	degrees = (int*)malloc((long)nb_nodes * 4);
	
	for (int ii = 0; ii < nb_nodes; ii++) {
		int d = 0;
		
		for (int jj = 0; jj < nb_nodes; jj++) {
			d += gr[ii][jj];
		}
		
		degrees[ii] = d;
	}
	
	for (int ii = 1; ii < nb_nodes; ii++) {
		degrees[ii] += degrees[ii - 1];
	}

	links = (int*)malloc((long)nb_links * 8);
	int pos = 0;
	
	for (int ii = 0; ii < nb_nodes; ii++) {
		for (int jj = 0; jj < nb_nodes; jj++) {
			if (gr[ii][jj] == 1) {
				links[pos++] = jj;
			}
		}
	}

	/* for (int ii = 0; ii < nb_nodes; ii++) {
		cerr << degrees[ii] << " ";
	}
	cerr << endl; */
}

// generates a random graph using the benchmark approach
Graph::Graph(int n1, int k1, int n2, int k2) {
  srand(getpid());

//  srand(time(NULL));
  nb_nodes = n1*n2;

  vector<vector<int> > gr(nb_nodes);
  for (int i=0 ; i<nb_nodes ; i++)
    gr[i].resize(nb_nodes,0);

  nb_links = 0;
  for (int i=0 ; i<nb_nodes ; i++)
    for (int j=i+1 ; j<nb_nodes ; j++) {
      double v = rand()*1./RAND_MAX;
      if (i/n1==j/n1) { // i and j in the same subgroup
	if (v<=(k1*1.)/(n1-1.)) {
	  gr[i][j]=gr[j][i]=1;
	  nb_links++;
	}
      } else { // i and j in different groups
	if (v<=(k2*1.)/(n1*(n2-1.))) {
	  gr[i][j]=gr[j][i]=1;
	  nb_links++;
	}
      }
    }

  total_weight = 2*nb_links;
  weights      = NULL;

  degrees = (int *)malloc((long)nb_nodes*4);
  for (int i=0 ; i<nb_nodes ; i++) {
    int d = 0;
    for (int j=0 ; j<nb_nodes ; j++)
      d+=gr[i][j];
    degrees[i]=d;
  }
  for (int i=1 ; i<nb_nodes ; i++)
    degrees[i]+=degrees[i-1];

  links = (int *)malloc((long)nb_links*8);
  int pos=0;
  for (int i=0 ; i<nb_nodes ; i++)
    for (int j=0 ; j<nb_nodes ; j++)
      if (gr[i][j]==1)
	links[pos++]=j;
}

Graph::Graph(int n, int m, int t, int *d, int *l, int *w) {
  nb_nodes     = n;
  nb_links     = m;
  total_weight = t;
  degrees      = d;
  links        = l;
  weights      = w;
}
#endif // SUCK

void
Graph::display() {
  for (int node=0 ; node<nb_nodes ; node++) {
    pair<int *,int *> p = neighbors(node);
    for (int i=0 ; i<nb_neighbors(node) ; i++) {
      if (weights!=NULL)
	cout << node << " " << *(p.first+i) << " " << *(p.second+i) << endl;
      else {
		cout << (node+1) << " " << (*(p.first+i)+1) << endl;
	//	cout << (node) << " " << (*(p.first+i)) << endl;
      }
    }   
  }
}

void
Graph::display_binary(char *outfile) {
  ofstream foutput;
  foutput.open(outfile ,fstream::out | fstream::binary);

  foutput.write((char *)(&nb_nodes),4);
  foutput.write((char *)(degrees),4*nb_nodes);
  foutput.write((char *)(links),8*nb_links);
}
