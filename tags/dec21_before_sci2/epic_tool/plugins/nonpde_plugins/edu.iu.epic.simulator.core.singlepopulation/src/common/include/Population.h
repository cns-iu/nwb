/**
 * @file Population.h
 * @author Bruno Goncalves
 * @package HIV
 *
 * @date Created by Bruno Goncalves on 3/19/10.
 * Copyright 2010 Bruno Goncalves. All rights reserved.
 *
 */

#ifndef _POPULATION_H_
#define _POPULATION_H_

#include <iostream>
#include <fstream>
#include <sstream>
#include <Parse.h>
#include <Rand.h>

class Population
{
	std::vector<std::vector<unsigned> > states;
	std::vector<short> labels;
	std::vector<short> unique;
	unsigned susIndex;
	unsigned recIndex;
	unsigned nlabels;
	Rand *r;
	
public:
	Population(unsigned M, unsigned N, unsigned sI, unsigned rI) : susIndex(sI),recIndex(rI) 
	{
		states.resize(M);
		labels.resize(N, 0);
		
		for(unsigned i=0; i<M; ++i)
			states[i].resize(N, susIndex);
	}
	void setLabels(std::vector<short> &l);
	unsigned size();
	unsigned size(unsigned t);
	unsigned memorySize();
	unsigned operator()(unsigned t, unsigned id);
	unsigned setValue(unsigned t, unsigned id, unsigned value);
	std::map<short, unsigned> labelCounts();
	std::map<short, std::vector<unsigned> > aggregateLabels(unsigned t, unsigned NStates);
	void seeding(std::string infections, unsigned t, Parse &parse, Rand &r2);
	std::vector<unsigned> aggregate(unsigned t, unsigned NStates);
	std::vector<unsigned> aggregate(unsigned t, unsigned NStates, short label);
	unsigned getNLabels();
	void resize(unsigned N1);
	void resize(unsigned N1, std::vector<short> &l);
	void saveLabelPopulations(unsigned t, unsigned NStates, FILE *fp);
	void savePopulations(unsigned t, unsigned NStates, FILE *fp);
	void saveState(unsigned t, FILE *fp);
	short &label(unsigned id);
	
	void birth(unsigned t, std::vector<unsigned> &dead, unsigned newNodes);
	void getDead(unsigned t, std::vector<unsigned> &dead, unsigned newNodes);
};

#endif /* _POPULATION_H_ */
