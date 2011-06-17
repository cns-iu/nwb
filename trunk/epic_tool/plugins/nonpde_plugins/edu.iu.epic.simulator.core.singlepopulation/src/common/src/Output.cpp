/** -*- mode: C++;-*-
 * @file Output.cpp
 * @author Bruno Goncalves
 *
 * Created by Bruno Goncalves on 09/22/09
 * Copywright 2009 Bruno Goncalves. All rights reserved.
 *
 */

#include <cstdlib>
#include <vector>
#include <stdint.h>
#include <string>
#include <cstdio>
#include <cstdlib>

#include "Output.h"

void Output::initVector(std::vector<std::vector<uint64_t> > &vec, unsigned size, unsigned time)
{
  vec.resize(0);
  vec.resize(size);

  for(unsigned i=0;i<vec.size();++i)
    vec[i].resize(time, 0);
}

void Output::writeVector(std::vector<std::vector<uint64_t> > &vec, std::string filename, std::vector<uint64_t> &pop)
{
  std::vector<uint64_t> cumul(vec.size(),0);
  
  unsigned t;

  for(t=0;t<vec[0].size();++t)
    {
      char name[1000];
      sprintf(name,"%s.%u.dat",filename.c_str(), t);

      FILE *fp=fopen(name,"a");
	
      if(fp == NULL)
	{
	  fprintf(stderr, "-- Error opening file!: %s\n",name);
	  exit(12);
	}

      for(unsigned i=0;i<vec.size();++i)
	{
	  cumul[i] += vec[i][t];

	  fprintf(fp,"%u ",i); 
	  fprintf(fp,"%g ", double(vec[i][t])/double(pop[i])*1000.0);
	  fprintf(fp,"%g\n", double(cumul[i])/double(pop[i])*1000.0);
	}

      fclose(fp);
    }
}

void Output::init(unsigned pop, unsigned time)
{
  unsigned BASINS = 1;

  popBasin.resize(BASINS);

  popBasin[0] = pop;

  initVector(basin, BASINS, time);
}

void Output::add(std::vector<uint64_t> values, unsigned time)
{
  for(unsigned i=0;i<values.size();++i)
    basin[i][time] = values[i];
}

void Output::add(std::vector<unsigned> values, unsigned time)
{
  for(unsigned i=0;i<values.size();++i)
    basin[i][time] = values[i];
}

void Output::add(unsigned id, uint64_t value, unsigned time)
{
  basin[id][time] = value;
}

  
void Output::writeToFile(std::string filename)
{
  writeVector(basin, filename+".basins", popBasin);
}
