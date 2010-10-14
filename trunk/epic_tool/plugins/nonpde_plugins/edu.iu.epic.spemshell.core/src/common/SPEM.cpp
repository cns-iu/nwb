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
#include<Output.h>

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

void initPopulation(vector<int64_t> &population, std::vector<std::pair<unsigned, double> > &initial, unsigned N, Rand &r)
{
  unsigned *n;
  double *p;

  n = (unsigned *) malloc(sizeof(unsigned)*initial.size());
  p = (double *) malloc(sizeof(double)*initial.size());

  std::string input;

  int country;
  int cPop;
  double frac;
    
  for(unsigned i=0;i<population.size();++i)
    population[i] = 0;

  double norm = 0;
  double fraction;
  
  for(unsigned j=0; j<initial.size(); ++j)
    {
      unsigned comp = initial[j].first;
      fraction = initial[j].second;
          
      if(fraction>1.0)
	fraction/=100.0;
      
      norm += fraction;
      
      p[j] = fraction;
    }
  
  if(fabs(norm-1.0)>1e-8)
    {
      fprintf(stderr,"-- OOPS!\n");
      exit(42);
    }
  
  r.multinomial(initial.size(), N, p, n);

  for(unsigned j=0;j<initial.size(); ++j)
    {
      int index = initial[j].first;
      
      population[index] = n[j];
    }

  free(n);
  free(p);
}

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

  string outVal = input.getStringOpt("outVal");
  string modelFile = input.getStringOpt("model");
  string infections = input.getStringOpt("infections");
  string initial = input.getStringOpt("initial");
  unsigned pop = input.getUnsignedOpt("population");
  string output = input.getStringOpt("output");
  unsigned days = input.getUnsignedOpt("days");
  unsigned seed = input.getUnsignedOpt("seed");
  string date = input.getStringOpt("date");
  string name = input.getStringOpt("name");
  unsigned events = input.getUnsignedOpt("events", 0);
  string h = input.getStringOpt("hemisphere",std::string("T"));

  char hemisphere = h[0];

  unsigned hemi = 2;

  switch(hemisphere)
  {
    case 'T':
    case 't':
      hemi = 2+0;
      break;

    case 'N':
    case 'n':
      hemi = 2+1;
      break;

    case 'S':
    case 's':
      hemi = 2-1;
      break;

    default:
      fprintf(stderr,"-- Non valid hemisphere: %c\n", hemisphere);
      exit(10);
  }

  //Ensure that the directory name has a trailing "/"
  if(output[output.size()-1]!='/')
    output+="/";

#ifdef _WIN32
  freopen((output + "SPEM." + name + ".out.dat").c_str(), "w", stdout);
  freopen((output + "SPEM." + name + ".err.dat").c_str(), "w", stderr);
#else
  stdout = fopen((output+"SPEM."+name+".out.dat").c_str(),"w");
  stderr = fopen((output+"SPEM."+name+".err.dat").c_str(),"w");
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

  FILE *fp_initial = fopen(initial.c_str(),"r");

  std::vector<std::pair<unsigned, double> > initialPop;

  if(fp_initial == NULL)
    {
      fprintf(stderr,"-- Error opening initial file!\n");
      exit(70);
    }

  while(!feof(fp_initial))
    {
      double p;
      char c[50];
      fscanf(fp_initial,"%s %lf\n",c,&p);

      unsigned c2 = parse.label(c);
      
      initialPop.push_back(std::pair<unsigned, double>(c2,p));
    }

  fclose(fp_initial);

  //unsigned susIndex = parse.label(susceptible);
  //population[susIndex] = pop;

  bool outbreak = false;
  ifstream in;
  std::string line;

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
  
  do{
    FILE *fpout = input.openFile(output + "SPEM." + name + ".out.dat.gz");
    FILE *fperr = input.openFile(output + "SPEM." + name + ".err.dat.gz");
    FILE *fpsec = input.openFile(output + "SPEM." + name + ".sec.dat.gz");

    initPopulation(population, initialPop, pop, r);

    in.open(infections.c_str());
    
    if(!in.is_open())
      {
	fprintf(stderr,"-- Error opening file: %s\n",infections.c_str());
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

	unsigned susIndex = (initialPop[0].first);

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
    
    unsigned month, day, year;

    Calendar cal(date);
    unsigned day0 = cal.day();

    unsigned total = 0;

    Output outData(pop, days+1);

    vector<uint64_t> transCount;

    for(unsigned k=day0; k<= day0 + days; ++k)
      {
	unsigned day = k;
	unsigned delta = model.step(population, pop, day, hemi, &fperr, outTrans, transCount);

	outData.add(transCount, k-day0);

	total += delta;

	fprintf(fpout,"%u",k);

	for(unsigned i=0;i<population.size();++i)
	  fprintf(fpout," %llu",population[i]);

	fprintf(fpout,"\n");

	fprintf(fpsec, "%u %u\n",k-day0, delta);
      }

    pclose(fpout);
    pclose(fperr);
    pclose(fpsec);

    if(total > events)
      outbreak = true;

    if(outbreak == true)
      {
	//	for(unsigned i=0;i<outData.size();++i)
	  outData.writeToFile(output+"sum");
      }
    
  }while(outbreak == false);
  
  return 0;
}
