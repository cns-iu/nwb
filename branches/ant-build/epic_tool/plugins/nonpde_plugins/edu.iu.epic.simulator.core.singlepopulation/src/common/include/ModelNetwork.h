/**
 * @file ModelNetwork.h
 * @author Bruno Goncalves
 * @package HIV
 *
 * @date Created by Bruno Goncalves on 3/14/10.
 * Copyright 2010 Bruno Goncalves. All rights reserved.
 *
 */

#ifndef _MODELNETWORK_H_
#define _MODELNETWORK_H_

#include <defs.h>
#include <Parse.h>
#include <Rand.h>
#include <Population.h>

class ModelNetwork
{
#ifdef BGDEBUG
	friend class ModelNetworkTest;
	FRIEND_TEST(ModelNetworkTest, transition);
#endif
	
	std::vector<std::vector<std::vector< Transition > > > transMatrix;
	std::vector<std::vector<Transition> > spontList;
	std::vector<bool> deadEnd;
	std::vector<unsigned> sequence;
	Rand *r;
	double *p;
	unsigned *n;
	unsigned transCount;
	unsigned recIndex;
	std::vector<NODE> NN;
	std::vector<unsigned> transListCount;
	
	unsigned transition(std::vector<Transition> &transList, bool &trans, std::vector<unsigned> &transCount, double weight, unsigned &sec, std::vector<unsigned> &outTrans, vector<uint64_t> &transCountVector);

public:
	ModelNetwork() {};
	ModelNetwork(Parse &parse, Rand *r2);

	void init(Parse &parse, Rand *r2);
	unsigned step(std::vector<std::vector<NODE> > &graph, Population &states, std::vector<unsigned> &Ni, unsigned t, FILE *fperr, std::vector<unsigned> &outTrans, vector<uint64_t> &transCount);

	~ModelNetwork();
};

#endif /* _MODELNETWORK_H_ */
