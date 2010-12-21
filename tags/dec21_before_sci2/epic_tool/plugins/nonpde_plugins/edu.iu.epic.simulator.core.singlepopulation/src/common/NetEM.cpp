/**
 * @file NetEM.cpp
 * @author Bruno Goncalves
 * @package HIV
 *
 * @date Created by Bruno Goncalves on 3/12/10.
 * Copyright 2010 Bruno Goncalves. All rights reserved.
 *
 */

#include <Rand.h>
#include <cstdio>
#include <cstdlib>
#include <vector>
#include <string>
#include <input.h>
#include <Parse.h>
#include <Graph.h>
#include <ModelNetwork.h>
#include <Population.h>
#include <Output.h>
#include <Calendar.h>

std::vector<std::string> getVector(std::string outVal)
{
  std::vector<std::string> comp;
  std::string temp = "";

  for(unsigned i=0;i<outVal.size();++i)
    {
      if(outVal[i]==' ' || outVal[i]=='\n')
	continue;

      if(outVal[i] == ';')
	{
	  comp.push_back(temp);
	  temp = "";
	  continue;
	}

      temp += outVal[i];
    }
  
  if(comp.size()>0 && temp.size()>0 && comp[comp.size()-1]!=temp)
    comp.push_back(temp);

  return comp;
}

int main(int argc, char *argv[])
{
	input_file input(argc, argv);
	
	std::string mdl = input.getStringOpt("model");
	unsigned seed = input.getUnsignedOpt("seed");
	unsigned M = input.getUnsignedOpt("M", 3);
	
	if(M<2)
	{
		fprintf(stderr, "Memory (M) must be larger or equal to 2\n");
		exit(3);
	}

	std::string infections = input.getStringOpt("infections");
	std::string network = input.getStringOpt("network");
	std::string susceptible = input.getStringOpt("susceptible");
	std::string output = input.getStringOpt("output");
	std::string name = input.getStringOpt("name", std::string("0"));
	unsigned days = input.getUnsignedOpt("days");
	unsigned events = input.getUnsignedOpt("events", 2);
	string outVal = input.getStringOpt("outVal");
	string date = input.getStringOpt("date");
	string frames = input.getStringOpt("frames", std::string("none"));
	
#ifdef BGDEBUG
	input.writeToFile(stderr);
	input.writeToFile(stderr, mdl);
    input.writeToFile(stderr, infections);
#endif
      //Ensure that the directory name has a trailing "/"
	if(output[output.size()-1]!='/')
		output+="/";
    
#ifdef _WIN32
	freopen((output + "NetEM." + name + ".out.dat").c_str(), "w", stdout);
	freopen((output + "NetEM." + name + ".err.dat").c_str(), "w", stderr);
#else
	stdout = fopen((output+"NetEM."+name+".out.dat").c_str(),"w");
	stderr = fopen((output+"NetEM."+name+".err.dat").c_str(),"w");
#endif // _WIN32
	
	Parse parse(mdl);
	Rand r(seed);

	Graph g(network);
	std::vector<std::vector<NODE> > graph = g.getGraph();
	
	unsigned N = graph.size();
	unsigned susIndex = parse.susceptible();
	unsigned recIndex = parse.recovered();
	unsigned NStates = parse.NStates();
	std::vector<unsigned> Ni = g.getPi();
	ModelNetwork model(parse, &r);
	uint64_t secondary;
	
	  std::vector<std::string> outComp = getVector(outVal);
  std::vector<unsigned> outCompIndex;
  
  fprintf(stderr,"# Found %u comps\n", unsigned(outComp.size()));
  
  std::vector<Transition> transList = parse.getTransitions();

	std::vector<unsigned> outTrans(transList.size(), 0);
  
  for(unsigned i=0;i<outComp.size();++i)
    {
      fprintf(stderr,"%u \"%s\"",i,outComp[i].c_str());
      
      unsigned comp = parse.label(outComp[i]);
      
      outCompIndex.push_back(comp);
      
      for(unsigned j=0;j<transList.size();++j)
	{
	  if(transList[j].j == comp)
	    {
	      outTrans[j] = 1;
	      fprintf(stderr," %u",j);
	    }
	}
      
      fprintf(stderr, "\n");
    }
	
	bool hasFrames = false;
	
	if(frames != "none")
		hasFrames = true;
		
	do{
		Population nodes(M, N, susIndex, recIndex);
	
		nodes.seeding(infections, 0, parse, r);

		FILE *fpout = input.openFile((output + "NetEM." + name + ".out"));
		FILE *fperr = input.openFile((output + "NetEM." + name + ".err"));
		FILE *fpsec = input.openFile((output + "NetEM." + name + ".sec"));
		
		FILE *fpstate;
		
		if(hasFrames)
			fpstate = input.openFile((frames + "NetEM." + name + ".state"));
		
		fprintf(fpout, "# time");

		Output outData(N, days+1);
		for(unsigned i=0; i<NStates; ++i)
			fprintf(fpout, " %s", parse.index(i).c_str());
		
		fprintf(fpout, "\n");
		
		secondary = 0;
		
		vector<uint64_t> transCount;
		Calendar cal(date);
	    unsigned day0 = cal.day();
		for(unsigned step = 0; step < days; ++step)
		{
			unsigned delta = model.step(graph, nodes, Ni, step, fperr,  outTrans, transCount);
			
			secondary += delta;
			outData.add(transCount, step);
			
			if(hasFrames)
				nodes.saveState(step, fpstate);
				
			nodes.savePopulations(step, NStates, fpout);

			fprintf(fpsec, "%u %u\n",step, delta);
		}
		
		pclose(fpout);
		pclose(fperr);
		pclose(fpsec);
		pclose(fpstate);

		fprintf(stderr, "outbreak = %Lu\n", secondary);
		
		if(secondary > events)
		{
			outData.writeToFile(output+"sum");
		}
		
	}while(secondary < events);

	
	
	return 0;
}
