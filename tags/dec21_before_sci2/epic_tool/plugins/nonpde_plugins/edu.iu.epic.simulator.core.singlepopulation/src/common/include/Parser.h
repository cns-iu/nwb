/**
 * @file Parser.h
 * @author Bruno Goncalves
 *
 * Created by Bruno Goncalves on 09/16/08
 * Copywright 2008 Bruno Goncalves. All rights reserved.
 *
 */

#ifndef _PARSER_H_
#define _PARSER_H_

#include<defs.h>
#include<string>
#include<vector>
#include<map>

class Parser
{
public:
  virtual int label(std::string state) = 0;
  virtual std::string index(unsigned index) = 0;
  virtual unsigned NStates() = 0;
  virtual std::map<std::string, unsigned> getStates() = 0;
  virtual std::vector<Transition> getTransitions() = 0;
  virtual std::vector<std::string> getFiles() = 0;
  virtual ~Parser(){ };
};

#endif /* _PARSER_H_ */
