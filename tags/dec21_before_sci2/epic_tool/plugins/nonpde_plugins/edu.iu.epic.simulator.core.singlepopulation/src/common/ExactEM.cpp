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
#include <vector>
#include <cmath>
#include <gsl/gsl_errno.h>
#include <gsl/gsl_spline.h>


int interpolate(std::vector<double> &x0, std::vector<double> &y0, std::vector<double> &new_y)
{
	unsigned size = x0.size();
	unsigned steps = new_y.size();

	double *x = (double *)malloc(sizeof(double)*size);
	double *y = (double *)malloc(sizeof(double)*size);

  for(unsigned i=0;i<size;++i)
    {
      x[i] = x0[i];
      y[i] = y0[i];
    }

    gsl_interp_accel *acc = gsl_interp_accel_alloc ();
    gsl_spline *spline = gsl_spline_alloc (gsl_interp_linear, size);
    
    gsl_spline_init (spline, x, y, size);
    
    for (unsigned t = 0; t < steps; ++t)
	{
		double yi = gsl_spline_eval (spline, (double) t, acc);
		new_y[t] = yi;
	}

    gsl_spline_free (spline);
    gsl_interp_accel_free (acc);

  free(x);
  free(y);

	return 0;
}

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

	std::vector<std::vector<double> > data;
	data.resize(NStates+1);
	
	unsigned step_count = 0;
	
	while (t < days)
  	{
    	int status = model.step(t, days, y);
    
    	step_count += 1;
    
    	if (status != GSL_SUCCESS) 
    		break;
		
		for(unsigned i=0; i<NStates; ++i)
		{
			if(data[0].size() <= step_count)
			{
				for(unsigned j=0; j<=NStates; ++j)
					data[j].resize(step_count + 1);
			
				data[0][step_count] = t;
			}	
			
			data[i+1][step_count] = y[i];
		}
	}
	
	std::vector<std::vector<double> > final;
	
	final.resize(NStates+1);
	
	for(unsigned i=0;i<final.size(); ++i)
		final[i].resize(unsigned(days)+1);

	for(unsigned i=0; i<final[0].size(); ++i)
		final[0][i] = i;
		
	for(unsigned i=0;i<NStates; ++i)
	{
		interpolate(data[0], data[i+1], final[i+1]);
	}
	
	for(unsigned i=0;i<days+1; ++i)
	{
		fprintf(fpout, "%g", final[0][i]);
		
		for(unsigned j=1; j< final.size(); ++j)
			fprintf(fpout, " %g", final[j][i]);
			
		fprintf(fpout, "\n");
	}
	return 0;
}
