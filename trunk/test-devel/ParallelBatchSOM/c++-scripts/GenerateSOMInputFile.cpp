/*
 * GenerateSOMInputFile.cpp
 *
 *  Created on: Nov 1, 2009
 *      Author: André Skupin
 */

#include <iostream>
#include <fstream>
#include <stdio.h>
#include <stdlib.h>

#include <string.h>
#include <iomanip.h>

using namespace std;

int compareOn1 (const void * a, const void * b)
{
	return ( *(int*)a - *(int*)b );
}

int compareOn2 (const void *a, const void *b)
{
	const int *inta = (const int *)a;
	const int *intb = (const int *)b;
	return inta[1]-intb[1];
}

int compareOn3 (const void *a, const void *b)
{
	const int *inta = (const int *)a;
	const int *intb = (const int *)b;
	return inta[2]-intb[2];
}

int main () {
	cout << "NIH Project: Generate SOM Input file" << endl; // prints NIH Project: Analysis of Mesh Terms
	char* infile1 = "sts-mesh-adj-AggregateSeqMeshTermsBySeqPMID.txt";
	char* outfile1 = "sts-mesh-adj-SOMInput.txt";
	int lineCount;
	int TermOccCount;
	int MeshCount;
	int SeqTermID;
	int SeqPMID;
	char* token;
	char line[500];
	char* delimiterSpace = " ";
	char* delimiterTab = "\t";
	char* delimiterPipe = "|";
	char numberstring[15];
	int PMIDCount;
	int nTermsInPMID;
	int PMID;
	static int PMIDs[2153769][100];
	/* PMIDs array holds all PMIDs and some extra info, like this:
	 * [SeqPMID, randomInteger, nTermsInPMID, PMIDterm1, PMIDterm2, ..., PMIDtermn]
	 *
	 * static to accommodate large array size */
	typedef int TwoIntArray[2];
	typedef int Int100Array[100];
	static TwoIntArray TermOccs[25901212]; /* holds all term occurences */
	int first, last, mid; /* used for binary search within PMIDs[] */
	int MeshTermDocCount;
	int nTotalUniqueTerms = 23347;
	int termVector[23347];
	int nTermsUsed;
	int nDocsUsed;
	char MeshTerm[100];
	char previousMeshTerm[100];
	int i=0;
	int j=0;
	int k=0;
	int n=0;
	srand((unsigned)time(0));
	int random_integer;

	/* Read list of PMIDs */
	ifstream in1;
	in1.open(infile1, ios::in);
	PMIDCount=0;
	while (in1.getline(line, 500))
		{
		PMIDCount++;
		token = NULL;
		token = strtok(line,delimiterSpace);
		strcpy(numberstring,token);
		PMIDs[PMIDCount-1][0] = atol(numberstring); // Read Sequential PMID
		nTermsInPMID = 0;
		token = strtok(NULL,delimiterSpace);
		while (token != NULL)
			{
			nTermsInPMID++;
			PMIDs[PMIDCount-1][2]=nTermsInPMID;
			strcpy(numberstring,token);
			PMIDs[PMIDCount-1][2+nTermsInPMID] = atol(numberstring); // Read Sequential Term ID
			token = strtok(NULL,delimiterSpace);
			}
		// finally, assign each PMID a random number, which will be used for random sampling
	    random_integer = rand();
	    PMIDs[PMIDCount-1][1] = random_integer;
		}
	in1.close();

//	qsort(PMIDs,PMIDCount,sizeof(Int100Array),compareOn2);
//  qsort(PMIDs,PMIDCount,sizeof(Int100Array),compareOn3);
////	qsort(TermOccs,TermOccCount,sizeof(TwoIntArray),compareOn2);
//
	ofstream out1;
	out1.open(outfile1, ios::out);

//	for (i=0; i<PMIDCount; i++)
//		{
//		out1 << PMIDs[i][0] << " " << PMIDs [i][1] << "\n";
//		}

	int seqPMIDlowerbound = 1;
	int seqPMIDupperbound = 1000;// PMIDCount;
	int seqTermIDlowerbound = 21112; //19278; //16711; //14412; //12522; //9242;//1; //2000, 1000, 500, 300, 200, 100, all
	int seqTermIDupperbound = nTotalUniqueTerms;

	nDocsUsed = seqPMIDupperbound - seqPMIDlowerbound + 1;
	nTermsUsed = seqTermIDupperbound - seqTermIDlowerbound + 1;
	out1 << nTermsUsed << "\n";
	out1 << "# SOM_PAK input file generated from: " << infile1 << "\n";
	out1 << "# number of documents used: " << nDocsUsed << " out of " << PMIDCount << "\n";
	out1 << "# number of terms used: " << nTermsUsed << " out of " << nTotalUniqueTerms << "\n";
	for (i = seqPMIDlowerbound - 1; i < seqPMIDupperbound; i++) // for all documents
		{
		for (n=0; n < nTotalUniqueTerms; n++) termVector[n] = 0; // reset term vector to all zeros
		for (j=0; j<PMIDs[i][2]; j++)
			{ // set the weight for all actually contained terms to 1
			SeqTermID = PMIDs[i][j+3];
			termVector[SeqTermID-1] = 1;
			}
		//out1 << i+1 << " " << PMIDs[i][0] << " " << /*PMIDs[i][1] << " " <<*/ PMIDs[i][2] << " " << PMIDs[i][3] <<"\n";
		// now output the term vector for current PMID
		for (k = seqTermIDlowerbound - 1; k < seqTermIDupperbound; k++)
			{
			out1 << termVector[k] << " ";
			}
		out1 << "\n";
		}


//
//	int terms[100];
//	int nTerms=0;
//	i=0;
//
///* for each Seq PMID, output sorted list of Seq mesh terms */
//	while (i<TermOccCount)
//		{
//		if (nTerms == 0)
//			{
//			nTerms++;
//			terms[0]=TermOccs[i][1];
//			}
//		/* collect all terms contained in current document */
//		if (TermOccs[i][0] == TermOccs[i+1][0])
//			{
//			// add the corresponding term to the list of terms for the PMID
//			nTerms++; // increase term count by 1
//			terms[nTerms-1] = TermOccs[i+1][1]; // insert ID of term
//			}
//		else
//			{
//			// sort and output the terms for the previous PMID, since they're all collected
//			out1 << TermOccs[i][0];
//			qsort(terms,nTerms,sizeof(int),compareOn1);
//			for (int j = 0; j < nTerms; j++)
//				{
//				out1 << " " << terms[j];
//				}
//			out1 << "\n";
//			nTerms = 0; // reset term list
//			}
//		i++;
//		}

	out1.close();
	return 0;
//    exit(0);
}
