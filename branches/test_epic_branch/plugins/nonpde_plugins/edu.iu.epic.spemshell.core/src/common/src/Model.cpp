/** -*- mode: C++;-*-
 * @file Model.cpp
 * @author Bruno Goncalves
 *
 * Created by Bruno Goncalves on 09/14/08
 * Copywright 2008 Bruno Goncalves. All rights reserved.
 *
 */

#include <defs.h>
#include <Parser.h>
#include <vector>
#include <map>
#include <Rand.h>
#include <stdint.h>
#include <sstream>
#include <Model.h>
#include <fstream>
#include <iostream>
#include <string>

Model::Model(Parser &parse, Rand &r2) : r(&r2)
{
  states = parse.getStates();
  vector<Transition> temp = parse.getTransitions();

  transitions.resize(states.size());

  for(unsigned i=0;i<temp.size();++i)
    transitions[temp[i].i].push_back(temp[i]);
      
  unsigned max = 0;

  for(unsigned i=0;i<transitions.size();++i)
    if(transitions[i].size()>max)
      max = transitions[i].size();

  ++max;

  p = (double *) malloc(max*sizeof(double));
  n = (unsigned *) malloc(max*sizeof(unsigned));

  newState.resize(states.size());

  labels.resize(states.size());

  for(unsigned i=0;i<labels.size();++i)
    labels[i] = parse.index(i);

  files = parse.getFiles();

  if(files.size()!=0)
    {
      rates.resize(files.size());

      for(unsigned i=0; i<files.size(); ++i)
	{
	  ifstream in(files[i].c_str());

	  if(!in.is_open())
	    {
	      fprintf(stderr,"Error opening file: %s\n",files[i].c_str());
	      exit(9);
	    }
	      
	  std::string input;

	  while(!in.eof())
	    {
	      getline(in, input);

	      stringstream ss(input);
		  
	      vector<double> data;

	      double value;

	      while(ss >> value)
		data.push_back(value);

	      rates[i].push_back(data);
	    }

	  in.close();
	}
    }
}

/**
 * @param current
 * @param N
 * @param x
 * @param y
 * @param fp
 * @return The number of secondary infections
 */
int  Model::step(std::vector<int64_t> &current, int64_t N, int x, int y, FILE **fp)
{
  if(current.size() != states.size())
    {
      fprintf(stderr,"Invalid current state! Aborting!\n");
      exit(10);
    }

  //Calculate the total population if necessary
  if(N == 0)
    {
      for(unsigned i=0; i<current.size() ; ++i)
	N+=current[i];
    }

  for(unsigned i=0;i<current.size(); ++i)
    newState[i] = current[i];

  unsigned total = 0;

  // For each initial state
  for(unsigned i=0; i<transitions.size(); ++i)
    {
      unsigned size = transitions[i].size();
      unsigned K = size+1;
      double norm = 1.0;
      double pij;

      for(unsigned k=0; k<size; ++k)
	{
	  unsigned pos = k;
	  unsigned agent = transitions[i][pos].agent;

	  if(transitions[i][pos].rate < 0)
	    pij = -1*transitions[i][pos].rate*rates[transitions[i][pos].index][x][y];
	  else
	    pij = transitions[i][pos].rate;
	    
	  if(transitions[i][pos].type == INTERACTION)
	    pij = current[agent]/double(N)*pij;
	    
	  p[k] = pij;
	  norm -= pij;
	}
	
      p[K-1] = norm;
	
      r->multinomial(K, current[i], p, n);
	
      for(unsigned k = 0; k < K-1; ++k)
	{
	  unsigned end = transitions[i][k].j;
	  unsigned start = i;
	  unsigned agent = transitions[i][k].agent;
	    
	  unsigned delta = n[k];

	  newState[end] += delta;
	  newState[start] -= delta;

	  if(delta != 0) 
	    {
	      if(transitions[i][k].type == INTERACTION)
		fprintf(*fp,"%s -- %s %u (%s) ", labels[start].c_str(), labels[end].c_str(), delta, labels[agent].c_str());
	      else
		fprintf(*fp,"%s -> %s %u ", labels[start].c_str(), labels[end].c_str(), delta);
		
	      if(transitions[i][k].secondary == true)
		total+=delta;
	    }
	}
    }
    

#ifdef BGDEBUG
  int64_t N2=0;

  for(unsigned i=0;i<current.size(); ++i)
    N2+= newState[i];

  if(N2!=N)
    {
      fprintf(stderr,"ERROR: Population growing! %lld %lld\n",N, N2);
      exit(3);
    }
#endif

  for(unsigned i=0; i<current.size(); ++i)
    current[i] = newState[i];

  //Return the total number of secondary cases
  return total;
}
