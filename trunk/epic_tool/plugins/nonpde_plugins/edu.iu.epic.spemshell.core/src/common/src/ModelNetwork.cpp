/**
 * @file ModelNetwork.cpp
 * @author Bruno Goncalves
 * @package HIV
 *
 * @date Created by Bruno Goncalves on 3/14/10.
 * Copyright 2010 Bruno Goncalves. All rights reserved.
 *
 */

#include "ModelNetwork.h"
#include <cstdio>
#include <cstdlib>
#include <algorithm>

#ifndef BGDEBUG
#define at(x) operator[](x)
#endif

ModelNetwork::ModelNetwork(Parse &parse, Rand *r2) : r(r2)
{
	init(parse, r2);
}

void ModelNetwork::init(Parse &parse, Rand *r2) 
{	
	r = r2;
	
	std::vector<Transition> transList = parse.getTransitions();
	transCount = transList.size();
	
	unsigned NStates = parse.NStates();
	recIndex = parse.recovered();
	
	transMatrix.resize(NStates);
	
	for(unsigned i=0; i<NStates; ++i)
		transMatrix.at(i).resize(NStates);
	
	deadEnd.resize(NStates, true);
	spontList.resize(NStates);
	
	for(unsigned i=0; i< transList.size(); ++i)
	{
		unsigned start = transList.at(i).i;
		//unsigned end = transList.at(i).j;
		unsigned agent = transList.at(i).agent;
		TYPE t = transList.at(i).type;
		
		switch(t)
		{
			case INTERACTION:
				transMatrix.at(start).at(agent).push_back(transList.at(i));
				break;
				
			case SPONTANEOUS:
				spontList.at(start).push_back(transList.at(i));
				break;
				
			default:
				fprintf(stderr, "Unknown transition: %u\n", t);
				exit(3);
		}
		
		if(deadEnd.at(start))
			deadEnd.at(start) = false;
	}	
	
	transListCount.resize(transCount, 0);
	
	p = (double *) malloc(sizeof(double) * TRANS_SIZE);
	n = (unsigned *) malloc(sizeof(unsigned) * TRANS_SIZE);
}

unsigned ModelNetwork::step(std::vector<std::vector<NODE> > &graph, Population &states, std::vector<unsigned> &Ni, unsigned t, FILE *fperr, std::vector<unsigned> &outTrans, vector<uint64_t> &transCountVector)
{
	if(graph.size() != states.size())
	{
		fprintf(stderr, "Graph and node vector sizes differ: %u %u\n", unsigned(graph.size()), unsigned(states.size()));
		exit(3);
	}
	
	sequence.resize(0);

  transCountVector.resize(0);
  transCountVector.resize(1);
	
	for(unsigned i=0; i<graph.size(); ++i)
	{
		sequence.push_back(i);		
	}
	
	random_shuffle(sequence.begin(), sequence.end(), *r);
	
	unsigned state;
	
	for(unsigned i=0; i<transCount; ++i)
		transListCount.at(i) = 0;
	
	unsigned sec = 0;
	
	for(unsigned n=0; n<sequence.size(); ++n)
	{
		unsigned current = sequence.at(n);
		unsigned start = states(t, current);
		
		state = start;
		
		//Nothing to see here, move along
		if(deadEnd.at(start))
		{
			states.setValue(t+1, current, state);
			continue;
		}
		
		NN.resize(0);
		
		for(unsigned i=0; i<graph.at(current).size(); ++i)
			NN.push_back(graph.at(current).at(i));

		random_shuffle(NN.begin(), NN.end(), *r);
		
		bool trans = false;
		
		//For each neighbor
		for(unsigned j=0; j<NN.size(); ++j)
		{
			unsigned neigh = NN.at(j).endNode;
			unsigned agent = states(t, neigh);
			
#ifdef BGDEBUG
			if(agent > transMatrix.size())
			{
				fprintf(stderr,"WTF???\n");
			}
#endif
			double weight = NN.at(j).weight;
			
			// There are possible interacting transitions
			if(transMatrix.at(start).at(agent).size() != 0)
				state=transition(transMatrix.at(start).at(agent), trans, transListCount, weight, sec, outTrans, transCountVector);

			//If we already transitioned
			//there is no point in checking the other neighbors
			if(trans)
				break;
		}
		
		// No transition yet
		// There are possible spontaneous transitions
		// Weight is identically one in the spontaneous case
		if(!trans && spontList.at(start).size() != 0)
			state=transition(spontList.at(start), trans, transListCount, 1, sec, outTrans, transCountVector);
		
		// Alas, poor "current" is dead!
		if(state == recIndex)
			Ni.at(current) = 0;
		
		states.setValue(t+1, current, state);
	}
	
	for(unsigned i=0; i<transCount; ++i)
		if(transListCount.at(i) != 0)
			fprintf(fperr, "%u %u 0 %u\n", i, t, transListCount.at(i)); 
	
	//fprintf(fpsec, "%u %u\n", t, sec);
	
	return sec;
}

unsigned ModelNetwork::transition(std::vector<Transition> &transList, bool &trans, std::vector<unsigned> &transCount, double weight, unsigned &sec, std::vector<unsigned> &outTrans, vector<uint64_t> &transCountVector)
{
	unsigned K = 0;
	double prob = 1.0;
	trans = false;
	
	unsigned state = transList.at(0).i;
	
	for(unsigned i=0;i<transList.size(); ++i)
	{
		double rate = transList.at(i).rate;
		
		if(transList.at(i).type == INTERACTION)
			p[i] = rate*weight;
		else
			p[i] = rate;
		
		n[i] = 0;
		
		prob -= rate;
		K += 1;
	}
	
	p[K] = prob;
	n[K] = 0;
	K += 1;
	
	if(prob < 0 || K >= TRANS_SIZE)
	{
		fprintf(stderr, "Negative probability: %lf or too many transitions: %u\n", prob, K);
		exit(10);
	}
	
	r->multinomial(K, 1, p, n);
	
	for(unsigned i=0;i<K-1;++i)
		if(n[i] == 1)
		{
			trans = true;
			state = transList.at(i).j;
			++transCount.at(transList.at(i).id);
			
			unsigned id = transList.at(i).id;
			
			if(outTrans[id] == 1)
				++transCountVector[0];
			
			if(transList.at(i).secondary)
				++ sec;
			
			break;
		}
	
	return state;
}

ModelNetwork::~ModelNetwork()
{
	free(p);
	free(n);
}
