// File: ConvertMain.cpp
// -- conversion of a graph from ascii to binary, sample main file
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


using namespace std;

class ProgramArguments {
public:
	ProgramArguments(void)
	:		inFile(NULL),
			outFile(NULL),
			graphType(EDGE_LIST_GRAPH_TYPE_UNWEIGHTED) {
	}

	ProgramArguments(
			char* inFile, char* outFile, EDGE_LIST_GRAPH_TYPE graphType)
	:		inFile(inFile),
			outFile(outFile),
			graphType(graphType) {
	}

	char* getInFile(void) {
		return this->inFile;
	}

	char* getOutFile(void) {
		return this->outFile;
	}

	EDGE_LIST_GRAPH_TYPE getGraphType(void) {
		return this->graphType;
	}

private:
	char* inFile;
	char* outFile;
	EDGE_LIST_GRAPH_TYPE graphType;
};

void printUsageMessage(char* programName, char* additionalText) {
	/*cerr << additionalText << endl;
	cerr << "usage: " << programName;
	cerr << " -i input_file -o outfile [options]" << endl << endl;
	cerr << "Read the graph and convert it to binary format." << endl;
	cerr << "-w\tRead the graph as having weights." << endl;
	cerr << "-h\tShow this usage message." << endl;*/

	exit(0);
}

ProgramArguments parseProgramArguments(int argumentCount, char** arguments) {
	char* inFile = NULL;
	char* outFile = NULL;
	EDGE_LIST_GRAPH_TYPE graphType = EDGE_LIST_GRAPH_TYPE_WEIGHTED;

	for (int ii = 1; ii < argumentCount; ii++) {
		if (arguments[ii][0] == '-') {
			switch(arguments[ii][1]) {
			case 'i':
				if (ii == argumentCount - 1) {
					printUsageMessage(arguments[0],
									  "The input file is missing.");
				}

				inFile = arguments[ii + 1];
				ii++;
				
				break;
			case 'o':
				if (ii == argumentCount - 1) {
					printUsageMessage(arguments[0],
									  "The output file is missing.");
				}

				outFile = arguments[ii + 1];
				ii++;
				
				break;
			case 'w':
				graphType = EDGE_LIST_GRAPH_TYPE_WEIGHTED;
				
				break;
			default:
				printUsageMessage(arguments[0],
								  "An unknown option was supplied.");
			}
		} else {
			printUsageMessage(arguments[0],
							  "More than one filename was supplied.");
		}
	}
	
	if (inFile == NULL || outFile == NULL) {
		printUsageMessage(arguments[0], "The input or output file missing.");
	}

	return ProgramArguments(inFile, outFile, graphType);
}

int main(int argumentCount, char** arguments) {
	ProgramArguments programArguments =
		parseProgramArguments(argumentCount, arguments);

	EdgeListGraph graph(programArguments.getInFile(),
						programArguments.getGraphType());

	graph.clean();
	graph.renumber();
	graph.displayBinary(programArguments.getOutFile());
}
