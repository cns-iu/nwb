/**
 * @file Population.cpp
 * @author Bruno Goncalves
 * @package HIV
 *
 * @date Created by Bruno Goncalves on 3/19/10.
 * Copyright 2010 Bruno Goncalves. All rights reserved.
 *
 */

#include "Population.h"
#include <Rand.h>
#include <Parse.h>
#include <vector>
#include <iostream>

#ifndef BGDEBUG
#define at(x) operator[](x)
#endif

unsigned Population::size()
{
	return states.at(0).size();
}

unsigned Population::size(unsigned time)
{
	return states.at(time).size();
}

unsigned Population::memorySize()
{
	return states.size();
}

unsigned Population::operator()(unsigned t, unsigned id)
{
	unsigned value = states.at(t%states.size()).at(id);

	return value;
}

unsigned Population::setValue(unsigned t, unsigned id, unsigned value)
{
	states.at(t%states.size()).at(id) = value;
	
	return states.at(t%states.size()).at(id);
}

void Population::seeding(std::string infections, unsigned t, Parse &parse, Rand &r2)
{
	r = &r2;
	
	ifstream in(infections.c_str());
	std::string line;
	unsigned now = t%states.size();
	
	std::vector<std::string> fields;
	
	unsigned seeds = 0;
	
	while(!in.eof())
	{
		getline(in, line);
		stringstream ss(line);
		std::string f;
		
		if(line.size() == 0 || line.at(0) == '#')
			continue;
		
		fields.resize(0);
		
		while(ss >> f)
			fields.push_back(f);
		
		if(fields.size() != 2)
		{
			fprintf(stderr, "Unknown infection file format:%s\n", line.c_str());
			exit(3);
		}
		
		if(parse.isLabel(fields.at(0)))
		{ // Comp Number
			unsigned comp = parse.label(fields.at(0));
			stringstream ss(fields.at(1));
			unsigned number;
			
			ss >> number;
			seeds += number;
			
			for(unsigned i=0; i<number; ++i)
			{
				unsigned id = (*r).get()%states.at(now).size();
				
				states.at(now).at(id) = comp;
			}
		}
		else
		{ // ID Comp
			unsigned id;
			stringstream ss(fields.at(0));

			ss >> id;
			seeds ++;
			unsigned comp = parse.label(fields.at(1));
			
			states.at(now).at(id) = comp;
		}
	}
	
	in.close();
	
	fprintf(stderr, "# Found %u seeds\n", seeds);
}

unsigned Population::getNLabels()
{
	return labelCounts().size();
}

std::map<short, unsigned> Population::labelCounts()
{
	std::map<short, unsigned> counts;
	
	for(unsigned i=0; i<labels.size(); ++i)
	{
		unsigned l = labels.at(i);
		
		if(counts.find(l) == counts.end())
			counts.at(l) = 0;
		
		counts.at(l) += 1;
	}	
	
	return counts;
}

std::map<short, std::vector<unsigned> > Population::aggregateLabels(unsigned t, unsigned NStates)
{
	std::map<short, std::vector<unsigned> > agg;
	unsigned now = t%states.size();
	unique.resize(0);
	
	for(unsigned i=0; i<states.at(now).size(); ++i)
	{
		unsigned comp = states.at(now).at(i);
		short l = labels.at(i);
		
		if(agg.find(l) == agg.end())
		{	
			agg[l].resize(NStates, 0); //= (* new std::vector<unsigned> (NStates, 0));
			unique.push_back(l);
		}
		
#ifdef BGDEBUG
		if(comp >= agg[l].size())
		{
			fprintf(stderr, "Unknown compartment: %u\n", comp);
			exit(10);
		}
#endif
		
		++agg[l].at(comp);
	}
		
	return agg;
}


std::vector<unsigned> Population::aggregate(unsigned t, unsigned NStates)
{
	std::vector<unsigned> agg(NStates, 0);
	unsigned now = t%states.size();

	for(unsigned i=0; i<states.at(now).size(); ++i)
	{
		unsigned comp = states.at(now).at(i);

#ifdef BGDEBUG
		if(comp >= agg.size())
		{
			fprintf(stderr, "Unknown compartment: %u\n", comp);
			exit(10);
		}
#endif
	
		++agg.at(comp);
	}
	
	return agg;
}

void Population::setLabels(std::vector<short> &l)
{
	if(l.size() != states.at(0).size())
	{
		fprintf(stderr, "Wrong vector length for label vector: %u %u\n", unsigned(l.size()), unsigned(states.at(0).size()));
		exit(5);
	}
	
	labels.resize(0);
	labels.resize(l.size());
	
	for(unsigned i=0;i<l.size();++i)
		labels.at(i) = l.at(i);
}

void Population::saveLabelPopulations(unsigned t, unsigned NStates, FILE *fp)
{
	if(fp == NULL)
	{
		fprintf(stderr,"Invalid file pointer!\n");
		exit(5);
	}
	
	std::map<short, std::vector<unsigned> > agg = aggregateLabels(t, NStates);
	
	for(unsigned j=0; j< unique.size(); ++j)
	{		
		unsigned key = j;
		
		fprintf(fp, "%u %i", t, key);
		
		for(unsigned i=0;i< agg.at(key).size();++i)
			fprintf(fp, " %u", agg.at(key).at(i)); 
		
		fprintf(fp, "\n");
	}
}

void Population::saveState(unsigned t, FILE *fp)
{
	unsigned now = t%states.size();
	
	fprintf(fp, "%u ", t);
	
	for(unsigned i=0;i<states.at(0).size();++i)
		fprintf(fp, " %u", states.at(now).at(i));
	
	fprintf(fp, "\n");
	
}

short &Population::label(unsigned id)
{
	if(id > states.at(0).size())
	{
		fprintf(stderr, "Invalid node id: %u\n", id);
		exit(5);
	}
	
	return labels.at(id);
}

void Population::savePopulations(unsigned t, unsigned NStates, FILE *fp)
{
	if(fp == NULL)
	{
		fprintf(stderr, "Invalid file pointer!\n");
		exit(5);
	}
	
	std::vector<unsigned> population = aggregate(t, NStates);
	
	fprintf(fp, "%u", t);
		
	for(unsigned i=0;i<population.size();++i)
		fprintf(fp, " %u", population.at(i));
	
	fprintf(fp, "\n");
}

void Population::resize(unsigned N1)
{
	unsigned M = states.size();
	unsigned N = states.at(0).size();
	
	for(unsigned i=0; i<M; ++i)
		states.at(i).resize(N + N1, susIndex);
}

void Population::resize(unsigned N1, std::vector<short> &l)
{
	if(l.size() != N1)
	{
		fprintf(stderr, "Invalid label vector size: %u %u\n", N1, unsigned(l.size()));
		exit(5);
	}
	
	resize(N1);
	
	unsigned N = states.at(0).size();
	
	labels.resize(N+N1);
	
	for(unsigned i=0;i<N1;++i)
		labels.at(N+i) = l.at(i);
}

void Population::getDead(unsigned t, std::vector<unsigned> &dead, unsigned newNodes)
{
	unsigned M = states.size();
	unsigned time = (t-1+M)%M;
	unsigned now = (t)%M;
	
	dead.resize(0);
	
	for(unsigned i=0; i<states.at(time).size(); ++i)
		if(states.at(time).at(i) == recIndex && states.at(now).at(i) == recIndex)
		{				
			dead.push_back(i);
			
			if(dead.size() > newNodes)
				break;
		}
}

void Population::birth(unsigned t, std::vector<unsigned> &dead, unsigned newNodes)
{
	getDead(t, dead, newNodes);
	
	unsigned time = (t+1)%states.size();
	unsigned hades = dead.size();
	
	unsigned N = states.at(0).size(); 
	
	if(hades < newNodes)
	{
		newNodes -= hades;
		
		for(unsigned i=0; i<newNodes; ++i)
			dead.push_back(N+i);
	}
	else
	{
		random_shuffle(dead.begin(), dead.end(), *r);
		dead.resize(newNodes);
	}
	
	for(unsigned i=0;i<dead.size(); ++i)
	{
		unsigned node = dead[i];
		
		if(node >= states.at(0).size())
		{
			for(unsigned j=0; j<states.size(); ++j)
				states.at(j).resize(node+1, susIndex);
		}
		
		states.at(time).at(node) = susIndex;
	}
	
	N = states.at(0).size();
	labels.resize(N);
}

