/**
 * @file ExactHIV.cpp
 * @author Bruno Goncalves
 * @package HIV
 *
 * @date Created by Bruno Goncalves on 7/15/10.
 * Copyright 2010 Bruno Goncalves. All rights reserved.
 *
 */

#include <Parse.h>
#include <defs.h>
#include <ExactModel.h>
#include <input.h>
#include <fstream>
#include <iostream>
#include <sstream>

int main (int argc, char *argv[])
{
	input_file input(argc, argv);
	
	std::string modelFile = input.getStringOpt("model");
	double days = input.getDoubleOpt("days");
	unsigned N = input.getUnsignedOpt("population");
	std::string infections = input.getStringOpt("infections");
    string output = input.getStringOpt("output");
    string name = input.getStringOpt("name");
    
      //Ensure that the directory name has a trailing "/"
  if(output[output.size()-1]!='/')
    output+="/";

    
#ifdef _WIN32
  freopen((output + "ExactEM." + name + ".out.dat").c_str(), "w", stdout);
  freopen((output + "ExactEM." + name + ".err.dat").c_str(), "w", stderr);
#else
  stdout = fopen((output+"ExactEM."+name+".out.dat").c_str(),"w");
  stderr = fopen((output+"ExactEM."+name+".err.dat").c_str(),"w");
#endif // _WIN32
	
  Parse parse(modelFile);
  ExactModel model(parse);

  unsigned NStates = parse.NStates();

  double *y = (double *) malloc(sizeof(double) *NStates);

  double t = 0.0;

	for(unsigned i=0;i<NStates; ++i)
		y[i] = 0;
	
	unsigned susIndex = parse.susceptible();
	
    y[susIndex] = 1.0 * N;

	std::ifstream in(infections.c_str());
	std::string line;

	std::string comp;
	unsigned seeds;
	
	while(!in.eof())
	{
		getline(in, line);
		
		std::stringstream ss(line);
		
		ss >> comp;
		ss >> seeds;

		unsigned infIndex = parse.label(comp);
		
		y[infIndex] = seeds;
		y[susIndex] -= seeds;
	}

    FILE *fpout = input.openFile(output + "ExactEM." + name + ".out");

    fprintf(fpout,"# time");
    
    for(unsigned j=0; j<NStates; ++j)
        fprintf(fpout," %s", parse.index(j).c_str());
    
    fprintf(fpout, "\n");

  while (t < days)
  {
    int status = model.step(t, days, y);
    
    if (status != GSL_SUCCESS) break;
     
    fprintf (fpout, "%g", t);
		
		for(unsigned i=0; i<NStates; ++i)
			fprintf(fpout, " %g", y[i]);
		
		fprintf(fpout, "\n");
  }

  return 0;
}
