/** -*- mode: C++;-*-
 * @file Parse.cpp
 * @author Bruno Goncalves
 *
 * Created by Bruno Goncalves on 09/14/08
 * Copywright 2008 Bruno Goncalves. All rights reserved.
 *
 *
 */

#include<string>
#include<cstdio>
#include<cstdlib>
#include<vector>
#include<map>
#include<defs.h>
#include<Parser.h>
#include<Calculator.h>
#include<algorithm>
#include<cctype>
#include<iostream>
#include<fstream>
#include<Parse.h>

/**
 * This class is responsbile for parsing the model description file
 * and generating the appropriate data structures that will allow the Model
 * class to implement it properly.
 */

unsigned Parse::susceptible()
{
        std::vector<Transition> transList = getTransitions();
        std::vector<unsigned> trans(states.size());
        
        for(unsigned i=0; i<transList.size(); ++i)
                trans[transList[i].j]++;
        
        unsigned susIndex;
        
        for(unsigned i=0;i<trans.size();++i)
                if(trans[i] == 0)
                {
                        susIndex = i;
                        break;
                }
        
        return susIndex;
}

unsigned Parse::recovered()
{
        std::vector<Transition> transList = getTransitions();
        std::vector<unsigned> trans(states.size());
        
        for(unsigned i=0; i<transList.size(); ++i)
                trans[transList[i].i]++;
        
        unsigned recIndex;
        
        for(unsigned i=0;i<trans.size();++i)
                if(trans[i] == 0)
                {
                        recIndex = i;
                        break;
                }
        
        return recIndex;
}

bool Parse::addCompartment(std::string initial)
{
  if(states.find(initial) == states.end())
    {
      indices.push_back(initial);
      states[initial]=indices.size()-1;
      fprintf(stderr,"# Found state %s\n", initial.c_str());
      return true;
    }
    

  return false;
}

double Parse::getRate(std::string &expr)
{
  std::set<std::string> vars = calc.getVars(expr);
  std::set<std::string>::iterator i;

  bool isSeasonal = false;

  for(i=vars.begin(); i!=vars.end(); ++i)
    if(time.find(*i) != time.end())
      {
	isSeasonal = true;
	break;
      }

  double rate = calc.calculate(expr);

  if(isSeasonal)
    rate *= -1;

  return rate;
}

int Parse::getFile(std::string &expr)
{
  std::set<std::string> vars = calc.getVars(expr);
  std::set<std::string>::iterator i;


  for(i = vars.begin(); i!=vars.end(); ++i)
    if(time.find(*i) != time.end())
      {
	for(unsigned j=0; j<files.size(); ++j)
	  if(files[j] == time[*i])
	    return j;

	files.push_back(time[*i]);

	return files.size()-1;
      }

  return -1;
}

void Parse::makeLongTransition(std::string comp)
{
  tmp = transitions[transitions.size()-1];
  transitions.pop_back();

  char newComp[BUFFER_SIZE];

  //Long agent is long
  unsigned duration = durations[comp];
    
  if(durations.find(indices[tmp.j]) != durations.end())
    {
      sprintf(newComp,"%s0",indices[tmp.j].c_str());	    
      tmp.j = states[newComp];
    }
    
  for(unsigned i = 0; i<duration; ++i)
    {
      sprintf(newComp,"%s%u",comp.c_str(),i);
	
      tmp.agent = states[newComp];
      transitions.push_back(tmp);
    }
}

std::vector<std::string> Parse::split(std::string line, std::string sep=" ")
{
  std::vector<std::string> vars;
  std::string temp="";
  
  unsigned pos=0;
  bool isSep;
        
  while(pos!=line.size())
    {
      isSep=false;
      
      for(unsigned i=0;i<sep.size();++i)
	if(line[pos]==sep[i] && temp.size()!=0)
	  {
	    vars.push_back(temp);
	    temp="";
	    isSep=true;
	    break;
	  }
      
      if(isSep==false && line[pos]!='\n')
	temp+=line[pos];
      
      ++pos;
    }
  
  vars.push_back(temp);
  
  return vars;
}


void Parse::makeDurationStates(std::string comp)
{
  char newComp[BUFFER_SIZE];
  unsigned last;
  unsigned duration = durations[comp];

  sprintf(newComp,"%s0",comp.c_str());
  addCompartment(newComp);
  last = states[newComp];
  tmp.type = SPONTANEOUS;
  tmp.secondary = false;
  tmp.rate = 1.0;
  tmp.agent = -1;


  for(unsigned i=1;i<duration;++i)
    {
      sprintf(newComp,"%s%u",comp.c_str(),i);
      addCompartment(newComp);
	
      tmp.i = last;
      tmp.j = states[newComp];

      last = tmp.j;
      transitions.push_back(tmp);
    }
}

void Parse::makeAgeStructure(std::string filename)
{
  std::ifstream in(filename.c_str());
  std::vector<std::vector<double> > matrix;

  if(!in.is_open())
    {
      fprintf(stderr,"-- Error age file: %s\n",filename.c_str());
      exit(9);
    }
    
  std::string input, initial, final;
  double rate;
    
  while(!in.eof())
    {
      //Have we reached the end of the file
      if(getline(in, input) == NULL)
	break;
	
      //Ignore blank lines and comments
      if(input.size() == 0 || input[0] == '#')
	continue;
	
      std::stringstream ss(input);
      ss >> initial;
      ss >> final;
      ss >> rate;

      int pos1 = -1;
      int pos2 = -1;

      for(unsigned i=0; i<ageComp.size(); ++i)
	if(ageComp[i] == initial)
	  {
	    pos1 = i;
	    break;
	  }

      if(pos1==-1)
	{					
	  pos1 = ageComp.size();
	  ageComp.push_back(initial);
	}

      for(unsigned i=0; i<ageComp.size(); ++i)
	if(ageComp[i] == final)
	  {
	    pos2 = i;
	    break;
	  }

      if(pos2==-1)
	{					
	  pos2 = ageComp.size();
	  ageComp.push_back(final);
	}

      if(int(matrix.size()) <= pos1)
	matrix.resize(pos1+1);

      if(int(matrix.size()) <= pos2)
	matrix.resize(pos2+1);

      if(int(matrix[pos1].size()) <= pos2)
	matrix[pos1].resize(pos2+1);

      if(int(matrix[pos2].size()) <= pos1)
	matrix[pos2].resize(pos1+1);

      matrix[pos1][pos2] = rate;
    }
    
  in.close();

  std::vector<std::string> indicesOld(indices.size());
    
  for(unsigned i=0; i<indices.size(); ++i)
    {
      indicesOld[i] = indices[i];
      states.erase(indicesOld[i]);
    }

  indices.resize(0);

  for(unsigned j=0; j<ageComp.size(); ++j)
    for(unsigned i=0; i<indicesOld.size(); ++i)
      {
	std::string comp = indicesOld[i] + "-" + ageComp[j];
	
	addCompartment(comp);
      }

  unsigned transLen = transitions.size();

  for(unsigned i=0; i<transLen; ++i)
    {
      if(transitions[i].type == INTERACTION)
	{
	  tmp = transitions[i];

	  std::string comp;

	  for(unsigned j=1; j<ageComp.size(); ++j)
	    {
	      comp = indicesOld[transitions[i].agent] + "-" + ageComp[j];
	      tmp.agent = states[comp];
	  
	      tmp.rate = transitions[i].rate * matrix[0][j];

	      transitions.push_back(tmp);
	    }
      
	  transitions[i].rate *= matrix[0][0];
	}
    }

  transLen = transitions.size();

  for(unsigned i=0; i<transLen; ++i)
    {
      tmp = transitions[i];

      std::string comp;

      for(unsigned j=1; j<ageComp.size(); ++j)
	{
	  comp = indicesOld[transitions[i].i] + "-" + ageComp[j];
	  tmp.i = states[comp];

	  if(tmp.type == INTERACTION)
	    {
	      unsigned pos = tmp.agent/ageComp.size();

	      tmp.rate = transitions[i].rate * (matrix[j][pos]/matrix[0][pos]);
	    }

	  comp = indicesOld[transitions[i].j] + "-" + ageComp[j];
	  tmp.j = states[comp];
	
	  transitions.push_back(tmp);
	}
    }
  
  for(unsigned i=0; i<transitions.size(); ++i)
    {
      if(transitions[i].type == SPONTANEOUS)
	printf("(%s) -> (%s) %g %u\n", indices[transitions[i].i].c_str(), indices[transitions[i].j].c_str(), transitions[i].rate, transitions[i].secondary);
      else
	printf("(%s) -- (%s) (%s) %g %u\n", indices[transitions[i].i].c_str(), indices[transitions[i].j].c_str(), indices[transitions[i].agent].c_str(), transitions[i].rate, transitions[i].secondary);
    }

}

Parse::Parse(std::string filename)
{
  std::ifstream in(filename.c_str());
      
  if(!in.is_open())
    {
      fprintf(stderr,"-- Error opening model file: %s\n",filename.c_str());
      exit(9);
    }

  std::string input, initial, operation, agent, final;
  double rate;

  hasAge = false;
  std::string age = "";

  while(!in.eof())
    {
      //Have we reached the end of the file
      if(getline(in, input)==NULL)
	break;
	  
      //Ignore blank lines and comments
      if(input.size()==0 || input[0]=='#')
	continue;

      std::vector<std::string> fields = split(input);
      
      unsigned count = 0;
      initial = fields[count++];
      operation = fields[count++];

      if(toLower(initial) == "time")
	{
	  fprintf(stderr,"# Found a seasonal effect from file %s\n",operation.c_str());
	  time["seasonal"] = operation;
	  files.push_back(operation);
	  continue;
	}
      else if(toLower(initial) == "duration")
	{
	  std::string expr = fields[count++];

	  unsigned duration = lround(calc.calculate(expr));
	  durations[operation] = duration;
	  fprintf(stderr,"# Set duration of state %s to %u days\n",operation.c_str(),duration);
	  makeDurationStates(operation);
	  continue;
	}
      else if(toLower(initial) == "age")
	{
	  final = fields[count++];

	  fprintf(stderr,"# Found age structure %s from file %s\n",operation.c_str(), final.c_str());
	  calc.set(operation, 1.0);
	  hasAge = true;
	  age = final;
	  continue;
	}
	  
      if(operation != "->" && operation != "--" && operation != "=")
	{
	  fprintf(stderr,"# Unknown operation: %s Skipped!\n",operation.c_str());
	  continue;
	}

      agent = "";
      unsigned i,j;
      int k=-1;

      std::string expr;

      if(operation == "=")
	{
	  expr = fields[count++];	  
	  
	  //double value = calc.calculate(expr);
	  //calc.set(initial, value);
	  calc.set(initial, expr);
	  fprintf(stderr,"# Defined variable %s with value %s\n",initial.c_str(),expr.c_str());
	  continue;
	}
      else
	{
	  final = fields[count++];
	  expr = fields[count++];

	  std::string temp;
	      
	  switch(expr[0])
	    {
	    case '=':
	      agent = final;
	      final = fields[count++];
	      expr = fields[count++];
		  
	      rate = getRate(expr);
	      break;
		  
	    default:
	      agent = final;
	      rate = getRate(expr);
	      break;
	    }
	}
	  
      char newComp[BUFFER_SIZE];
	  
      if(durations.find(initial) != durations.end())
	{
	  sprintf(newComp,"%s%u",initial.c_str(),durations[initial]-1);
	  initial = newComp;
	}
      else
	addCompartment(initial);
	  
      if(agent != ""  && durations.find(agent) == durations.end())
	addCompartment(agent);
      else
	k=0;

      if(durations.find(final) != durations.end())
	{
	  sprintf(newComp,"%s0",final.c_str());
	  final = newComp;
	}
      else
	addCompartment(final);
	  
      i = states[initial];
      j = states[final];

      if(k!=0)
	k = states[agent];
      else
	k = states[final];

      tmp.i = i;
      tmp.j = j;
      tmp.agent = k;
      tmp.rate = rate;
      tmp.secondary = false;
      tmp.index = -1;

      while(count != fields.size())
	{
	  std::string sec = fields[count++];
	      
	  if(toLower(sec) == "secondary")
	    tmp.secondary = true;
	  else if(toLower(sec) == "seasonal")
	    tmp.rate *= -1;
	}

      if(operation == "->")
	{
	  tmp.type = SPONTANEOUS;

	  if(tmp.rate>0)
	    fprintf(stderr,"# Created spontaneous transition from state %s to state %s with rate %g\n", initial.c_str(), final.c_str(), rate);
	  else
	    {
	      fprintf(stderr,"# Created spontaneous transition from state %s to state %s with time dependent rate \"%s\"\n", initial.c_str(), final.c_str(), expr.c_str());
	      
	      for(unsigned j=0; j<files.size(); ++j)
		if(files[j] == time["seasonal"])
		  {
		    tmp.index = j;
		    break;
		    //tmp.index = time["seasonal"]; //getFile(name);
		  }
	    }
	}
      else if(operation == "--")
	{
	  tmp.type = INTERACTION;

	  if(tmp.rate>0)
	    fprintf(stderr,"# Created interating transition from state %s to state %s via state %s with rate %g\n",initial.c_str(), final.c_str(), agent.c_str(), rate);
	  else
	    {
	      fprintf(stderr,"# Created interating transition from state %s to state %s via state %s with time dependent rate \"%s\"\n", initial.c_str(), final.c_str(), agent.c_str(), expr.c_str());

	      for(unsigned j=0; j<files.size(); ++j)
		if(files[j] == time["seasonal"])
		  {
		    tmp.index = j;
		    break;
		    //tmp.index = time["seasonal"]; //getFile(name);
		  }
	    }
	}
	  
      transitions.push_back(tmp);

      if(durations.find(agent) != durations.end())
	makeLongTransition(agent);
    }
      
  if(hasAge)
    makeAgeStructure(age);

  for(unsigned i=0;i<transitions.size();++i)
    {
      transitions[i].id = i;
	
      if(transitions[i].type == SPONTANEOUS)
	printf("%u (%s) -> (%s) %g\n", i, indices[transitions[i].i].c_str(), indices[transitions[i].j].c_str(), transitions[i].rate);
      else
	printf("%u (%s) -- (%s) (%s) %g\n", i, indices[transitions[i].i].c_str(), indices[transitions[i].j].c_str(), indices[transitions[i].agent].c_str(), transitions[i].rate);
    }
}
