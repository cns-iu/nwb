/** -*- mode:C++; -*-
 * @file Calculator.h
 * @author Bruno Goncalves
 * @package HIV
 *
 * @date Created by Bruno Goncalves on 09/17/08
 * Copywright 2008 Bruno Goncalves. All rights reserved.
 *
 */

#ifndef _CALCULATOR_H_
#define _CALCULATOR_H_

#include <map>
#include <string>
#include <vector>
#include <cmath>
#include <sstream>
#include <set>

class Calculator
{
  std::map<char,unsigned> prec;
  std::map<std::string, std::string> vars;

  double Evaluate(std::string expr)
  {
    std::vector<std::string> numbers=Parse(expr);
    return Evaluate(numbers);
  }
	
  std::vector<std::string> Parse(std::string expr);
	
  double Evaluate(std::vector<std::string> &numbers);
 
public:
  Calculator() 
  {
    prec['^']=4;
    prec['*']=3;
    prec['/']=3;
    prec['+']=2;
    prec['-']=2;
    prec['=']=1;
    prec['|']=0;
  }

  void set(std::string var, std::string value)
  {
    vars[var] = value;
  }

	void set(std::string var, double value)
  {
		char v[2048];
		
		sprintf(v, "%lf", value);
		
    vars[var] = v;
  }	
	
  double calculate(std::string exp)
  {
    return Evaluate(exp);
  }

  void reset()
  {
    vars.clear();
  }

  std::set<std::string> getVars(std::string exp);
};

#endif /* _CALCULATOR_ */
