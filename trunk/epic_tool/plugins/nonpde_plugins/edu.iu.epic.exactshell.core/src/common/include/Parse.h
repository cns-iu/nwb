/** -*- mode: C++;-*-
 * @file Parse.h
 * @author Bruno Goncalves
 *
 * Created by Bruno Goncalves on 09/14/08
 * Copywright 2008 Bruno Goncalves. All rights reserved.
 *
 *
 */

#ifndef _PARSE_H_
#define _PARSE_H_

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

/**
 * This class is responsbile for parsing the model description file
 * and generating the appropriate data structures that will allow the Model
 * class to implement it properly.
 */
class Parse : public Parser
{
  std::map<std::string, unsigned> states;
  std::vector<std::string> indices;
  std::vector<Transition> transitions;
  std::map<std::string, std::string> time;
  std::map<std::string, unsigned> durations;
  std::vector<std::string> files;
  Calculator calc;
  Transition tmp;
  bool hasAge;
  std::vector<std::string> ageComp;

  bool addCompartment(std::string initial);
 
  double getRate(std::string &expr);
 
  std::string toLower(std::string myString)
  {
    std::transform(myString.begin(), myString.end(), myString.begin(),(int(*)(int)) tolower);
    
    return myString;
  }

  int getFile(std::string &expr);

  void makeLongTransition(std::string comp);

  void makeDurationStates(std::string comp);

  void makeAgeStructure(std::string filename);

  std::vector<std::string> split(std::string line, std::string sep);


 public:
  Parse(std::string filename);
    

  int label(std::string state)
  {
    if(states.find(state) == states.end())
      {
	fprintf(stderr,"-- Unknown state: %s\n",state.c_str());
	exit(2);
      }

    return states[state];
  }

    unsigned susceptible();

    unsigned recovered();

  std::string index(unsigned index)
    {
      if(index >= indices.size())
	{
	  fprintf(stderr,"-- Invalid index: %u\n",index);
	  exit(10);
	}

      return indices[index];
    }

  std::vector<std::string> getIndices()
  {
    return indices;
  }

  unsigned NStates()
  {
    return states.size();
  }

  std::map<std::string, unsigned> getStates()
  {
    return states;
  }
  
  std::vector<Transition> getTransitions()
  {
    return transitions;
  }

  bool hasAgeGroups()
  {
    return hasAge;
  }

  std::vector<std::string> getFiles()
  {
    return files;
  }

  std::vector<std::string> getAgeCompartments()
  {
    if(ageComp.size()!=0)
      return ageComp;

    return indices;
  }
};

#endif /* _PARSE_H_ */

