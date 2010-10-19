/**
 * @file Graph.h
 * @author Bruno Goncalves
 * @package HIV
 *
 * @date Created by Bruno Goncalves on 3/12/10.
 * Copyright 2010 Bruno Goncalves. All rights reserved.
 *
 */

#ifndef _GRAPH_H_
#define _GRAPH_H_

#include<defs.h>
#include<vector>
#include<string>
#include<map>

#ifdef BGDEBUG
#include <gtest/gtest.h>
#endif
/**
 * Utility class to provide basic network graph ananlysis functionality.
 * An input file is read at instantiation time from which the dense adjacency 
 * list (as opposed to the sparse adjacency matrix) is initialized. Basic
 * network statistics such as number of nodes, \f$N\f$, number of edges, 
 * \f$E\f$ (or equivalently the number of non-zero elements in the adjacency 
 * matrix), average connectivity \f$k_{avg}\f$, maximum connectivity, 
 * \f$K_{max}\f$ and the connectivity distribution \f$P\left(k\right)\f$ are 
 * also calculated.
 * An interface to the elements of the Adjacenty Matrix is also provided using
 * the operator() formalism. It should be noted that the Adjacenty Matrix 
 * elements are simply calculated "just in time" from the Adjacency List. This
 * allows for an efficient use of memory at the cost of requiring an average
 * \f$nnz/N\f$ (usually small) operations per matrix element. 
 * An optional caching mechanism is also provided that stores all the
 * calculated quantities internally to speed multiple accesses by increasing
 * the memory fingerprint.
 * Finally, utility functions to save all the quantities of interest to a 
 * flat text file are provided as well.A pointer to the output file can 
 * be provided by the user or just a filename. If the file is unusable
 * (NULL pointer or if we are unable to create a new file with the given name)
 * an error message is printed out without any further consequences.
 */
class Graph
{
	protected:

#ifdef BGDEBUG
	friend class GraphTest;
	FRIEND_TEST(GraphTest, addEdge);
	FRIEND_TEST(GraphTest, backLink);
#endif

  std::vector<double> Pk;
  std::vector<std::vector <unsigned> > Ekk;
  unsigned nnz;
  unsigned Kmax;
	double Wmax;
  unsigned N;
  double kavg;
  double k2avg;
  unsigned shellmax;
  std::vector<unsigned> NN;
  std::vector<int> exclude;
  unsigned count;
	NODE tmp;
  
	std::map<unsigned,unsigned> dict;
	std::map<unsigned,unsigned> revDict;

  std::vector<int> marks;
  
  void mark(unsigned id, unsigned comp);
	void markD(unsigned id, unsigned comp);
  
 public:
	
	std::vector<std::vector <NODE> > graph;
	
  bool add(unsigned x, unsigned y, unsigned t=1, double w=1.0, bool directed=false, bool remap = true);
  
  Graph(std::string filename);
   
  Graph(std::vector<std::vector<NODE> > G);
  
  Graph()
	{
		nnz=0;
		Kmax=0;
		Wmax=0;
		k2avg=0;
		N=0;
		shellmax=0;
		count = 0; 
	}

	std::vector<int> comps();

	std::vector<int> depth();
	
  std::vector<unsigned> getPi();
  
  unsigned size()
  {
    return graph.size();
  }

	bool isNeighbor(unsigned nodeA, unsigned nodeB);
	
  std::vector<double> getPk();
 	
  FILE *savePk(FILE *fp);
	
	void savePk(std::string filename);
	
	void savePi(std::string filename);
 
  double getPk(unsigned k);
	
  unsigned getE()
  {
    return nnz;
  }
	
  int getK(unsigned i)
  {
		unsigned id;
		
		if(dict.find(i) == dict.end())
			return -1;
		
		id = dict[i];
		
    return graph[id].size();
  }
	
  unsigned getKmax()
  {
    return Kmax;
  }
	
	unsigned getWmax()
  {
    return Wmax;
  }
	
  unsigned getN()
  {
    N=0;
	  
    for(unsigned i=0;i<graph.size();i++)
      if(graph[i].size()!=0)
				N++;
	  
    return N;
  }
	
  double getKavg()
  {
    return kavg;
  }
	
  double getK2avg();
 	
  std::vector<std::vector<unsigned> > getEkk();
	
	std::vector<std::vector<double> > getPkk();
 	
  void savePkk(std::string filename);
 	
  FILE *savePkk(FILE *fp);
 	
  void saveEkk(std::string filename);
  	
  FILE *saveEkk(FILE *fp);
	
	std::vector<NODE> &getNN(unsigned i)
  {
    return graph[i];
  }
  
  unsigned operator()(unsigned i,unsigned j)
  {
    for(unsigned k=0;k<graph[i].size();k++)
      if(graph[i][k].endNode==j)
				return 1;
		
    return 0;
  }

  std::vector<std::vector<int> > getMaxPath(std::vector<int> s);
   
  std::vector<int> shells(unsigned current);

	double Wij(unsigned x, unsigned y)
	{
		x = dict[x];
		y = dict[y];
		
		for(unsigned i=0;i<graph[x].size();++i)
			if(graph[x][i].endNode == y)
				return graph[x][i].weight;
		
		fprintf(stderr, "Edge not found!: %u %u\n", revDict[x], revDict[y]);
		exit(10);
	}
	
  unsigned getShellMax()
  {
    return shellmax;
  }
  
  std::vector<std::vector<NODE> > &getGraph()
	{
		return graph;
	}

  std::vector<double> shellsPk(unsigned current);
	
	void shellsPk(unsigned current,std::vector<double> &sPk);
	
	void writeToFile(FILE *fp);
};
#endif
