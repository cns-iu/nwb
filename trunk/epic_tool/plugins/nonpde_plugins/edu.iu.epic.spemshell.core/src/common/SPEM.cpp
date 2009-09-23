/** -*- mode: C++;-*-
 * @file SPEM.cpp
 * @author Bruno Goncalves
 *
 * Created by Bruno Goncalves on 10/24/08
 * Copywright 2008 Bruno Goncalves. All rights reserved.
 *
 */

#include<stdint.h>
#include<Rand.h>
#include<cstdio>
#include<cstdlib>
#include<input.h>
#include<Model.h>
#include<Parse.h>
#include<Calendar.h>

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
  string susceptible = input.getStringOpt("susceptible");
  string infections = input.getStringOpt("infections");
  unsigned pop = input.getUnsignedOpt("population");
  string output = input.getStringOpt("output");
  unsigned days = input.getUnsignedOpt("days");
  unsigned seed = input.getUnsignedOpt("seed");
  string calendar = input.getStringOpt("calendar");
  string date = input.getStringOpt("date");
  string name = input.getStringOpt("name");
  unsigned events = input.getUnsignedOpt("events", 0);

  //Ensure that the directory name has a trailing "/"
  if(output[output.size()-1]!='/')
    output+="/";

/* Note we've changed both extensions here from .dat to .txt
 * and switched from stdfoo = bar to using freopen.
 */
#ifdef _WIN32
freopen((output + "SPEM." + name + ".out.txt").c_str(), "w", stdout);
freopen((output + "SPEM." + name + ".err.txt").c_str(), "w", stderr);
#else
stdout = fopen((output+"SPEM."+name+".out.txt").c_str(),"w");
stderr = fopen((output+"SPEM."+name+".err.txt").c_str(),"w");
#endif // _WIN32

#ifdef BGDEBUG
  input.writeToFile(stderr);
  input.writeToFile(stderr, modelFile);
  input.writeToFile(stderr, infections);
#endif

  Parse parse(modelFile);
  Rand r(seed);
  
  Model model(parse, r);

  vector<int64_t> population(parse.NStates(),0);

  unsigned susIndex = parse.label(susceptible);
  population[susIndex] = pop;

  bool outbreak = false;
  ifstream in;
  std::string line;

  do{
    // Note we've removed the .gz extensions from all three of these filenames.
    FILE *fpout = input.openFile(output + "SPEM." + name + ".out.dat");
    FILE *fperr = input.openFile(output + "SPEM." + name + ".err.dat");
    FILE *fpsec = input.openFile(output + "SPEM." + name + ".sec.dat");

    in.open(infections.c_str());
    
    if(!in.is_open())
      {
	fprintf(stderr,"Error opening file: %s\n",infections.c_str());
	exit(9);
      }
    
    while(!in.eof())
      {
	getline(in, line);
	
	if(line.size() == 0)
	  continue;
	
	stringstream ss(line);
	
	unsigned delta;
	std::string comp;
	
	double inf;
	
	ss >> comp;
	ss >> inf;
	
	if(inf<1.0)
	  delta = unsigned(round(population[susIndex]*inf));
	else
	  delta = unsigned(round(inf));
	
	unsigned infIndex = parse.label(comp);
	
	population[infIndex] += delta;
	population[susIndex] -= delta;
      }
    
    in.close();
   
#ifdef BGDEBUG 
    input.writeToFile(fpout);
    input.writeToFile(fperr);
    
    input.writeToFile(fpout, modelFile);
    input.writeToFile(fperr, modelFile);
    
    input.writeToFile(fpout, infections);
    input.writeToFile(fperr, infections);
#endif

    fprintf(fpout,"# time");
    
    for(unsigned j=0; j<population.size(); ++j)
      fprintf(fpout," %s", parse.index(j).c_str());
    
    fprintf(fpout,"\n");
    
    Calendar cal(calendar, 2007);
    
    unsigned day0 = cal.day(date);

    unsigned total = 0;

    for(unsigned k=day0; k<= day0 + days; ++k)
      {
	unsigned month = cal.month(k);
	unsigned delta = model.step(population, pop, month, 0, &fperr);

	total += delta;

	fprintf(fpout,"%u",k);

	for(unsigned i=0;i<=population.size();++i)
	  fprintf(fpout," %lu",population[i]);

	fprintf(fpout,"\n");

	fprintf(fpsec, "%u %u\n",k-day0, delta);
      }

    pclose(fpout);
    pclose(fperr);
    pclose(fpsec);

    if(total > events)
      outbreak = true;

  }while(outbreak == false);
  
  return 0;
}
