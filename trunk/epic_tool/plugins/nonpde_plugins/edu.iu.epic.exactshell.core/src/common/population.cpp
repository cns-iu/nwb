/** -*- mode: C++;-*-
 * @file population.cpp
 * @author Bruno Goncalves
 *
 * Created by Bruno Goncalves on 09/14/08
 * Copywright 2008 Bruno Goncalves. All rights reserved.
 *
 */

#include<stdint.h>

#include<cstdio>
#include<cstdlib>
#include<input.h>
#include<Model.h>
#include<Parse.h>
#include<Rand.h>
#include<Airports.h>
#include<Calendar.h>
#include<OutbreakDetector.h>

int64_t getN(vector<vector<int64_t> > &pop)
{
  int64_t N=0;

  for(unsigned i=0;i<pop.size();++i)
    for(unsigned j=0;j<pop[i].size();++j)
      N+=pop[i][j];

  return N;

}

int main(int argc, char *argv[])
{
  input_file input(argc, argv);

  string modelFile = input.getStringOpt("model");
  string airport = input.getStringOpt("airport");
  string susceptible = input.getStringOpt("susceptible");
  string infections = input.getStringOpt("infections");
  string output = input.getStringOpt("output");
  string city = input.getStringOpt("city");
  unsigned days = input.getUnsignedOpt("days");
  double alpha = input.getDoubleOpt("alpha");
  unsigned seed = input.getUnsignedOpt("seed");
  string NT = input.getStringOpt("nonTravel");
  string calendar = input.getStringOpt("calendar");
  string date = input.getStringOpt("date");
  string name = input.getStringOpt("name");

  //Ensure that the directory name has a trailing "/"
  if(output[output.size()-1]!='/')
    output+="/";

  input.writeToFile(stderr);
  input.writeToFile(stderr, modelFile);
  input.writeToFile(stderr, NT);
  input.writeToFile(stderr, infections);

  Parse parse(modelFile);
  Rand r(seed);
  
  vector<bool> nonTravel(parse.NStates(), false);

  string line;

  ifstream in(NT.c_str());

  if(!in.is_open())
    {
      fprintf(stderr,"Error opening file: %s\n",NT.c_str());
      exit(9);
    }

  while(!in.eof())
    {
      getline(in, line);

      //Ignore blank lines
      if(line.size()==0)
	continue;

      nonTravel[parse.label(line)] = true;
    }
  
  in.close();

  Model model(parse, r);
  Airports airports(city, airport, output, alpha, nonTravel, r);


  vector<unsigned> pop = airports.getNeighborPopulation();

  for(unsigned i = 0; i<pop.size(); ++i)
    fprintf(stdout, "%u %u\n", i, pop[i]);
  
  return 0;
}
