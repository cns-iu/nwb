/** -*- mode:C++; -*-
 * @file Calculator.cpp
 * @author Bruno Goncalves
 * @package HIV
 *
 * @date Created by Bruno Goncalves on 09/17/08
 * Copywright 2008 Bruno Goncalves. All rights reserved.
 *
 */

#include <cstdlib>
#include <map>
#include <string>
#include <vector>
#include <cmath>
#include <sstream>
#include <set>
#include <Calculator.h>
#include <cstdio>

std::vector<std::string> Calculator::Parse(std::string expr)
{				
  std::vector<std::string> numbers;
  std::vector<char> opers;
	
  std::string temp;
	
  char lastOp='|';
  char curOp;
	
  for(unsigned i=0;i<=expr.size();i++)
	{
		if(i!=expr.size() && prec.find(expr[i])==prec.end())
			temp+=expr[i];
		else
		{
			numbers.push_back(temp);
			
			curOp=expr[i];
			
			if(prec[curOp]>prec[lastOp])
	    {
	      temp="";
	      opers.push_back(expr[i]);
	    }
			else
	    {
	      while(prec[curOp]<=prec[lastOp])
				{
					if(opers.empty())
						break;
					
					lastOp=opers[opers.size()-1];
					opers.pop_back();
					
					temp=lastOp;
					
					numbers.push_back(temp);
				}
				
	      opers.push_back(curOp);
	    }
			
			lastOp=expr[i];
			temp="";
		}
	}
	
  return numbers;
}

double Calculator::Evaluate(std::vector<std::string> &numbers)
{
  if(numbers.size() == 0)
    return 0.0;
	
  std::string item=numbers[numbers.size()-1];
  numbers.pop_back();
	
  std::vector<std::string> element;
  double n1, n2;
	
  switch(item[0])
	{
    case '*':
      n1=Evaluate(numbers);
      n2=Evaluate(numbers);
			
      return n2*n1;
			
    case '/':
      n1=Evaluate(numbers);      
      n2=Evaluate(numbers);
			
      return n2/n1;
			
    case '^':
      n1=Evaluate(numbers);      
      n2=Evaluate(numbers);
			
      return pow(n2,n1);
			
    case '+':
      n1=Evaluate(numbers);      
      n2=Evaluate(numbers);
			
      return n2+n1;
			
    case '-':
      n1=Evaluate(numbers);      
      n2=Evaluate(numbers);
			
      return n2-n1;
			
    case '$':				
      item = item.substr(1,item.size()-1);
			
      if(vars.find(item) != vars.end())
			{
				return calculate(vars[item]);
			}
				
      else
			{
				fprintf(stderr,"-- Error parsing expression: %s\n",item.c_str());
				exit(1);
			}
			
    case '0':
    case '1':
    case '2':
    case '3':
    case '4':
    case '5':
    case '6':
    case '7':
    case '8':
    case '9':
		{
			std::stringstream ss(item);
			double value;
			ss >> value;
			
			return value;
		}
			
    default:
			
      if(vars.find(item) == vars.end())
			{
				fprintf(stderr,"-- Error parsing expression: %s\n", item.c_str());
				exit(1);
			}
      else
			{
				return calculate(vars[item]);				
				//return vars[item];
			}
				
	}	
	
  return 0.0;
}

std::set<std::string> Calculator::getVars(std::string exp)
{
  std::vector<std::string> temp = Parse(exp);
	
  std::set<std::string> v;
	
  for(unsigned i=0;i<temp.size(); ++i)
    if(prec.find(temp[i][0]) == prec.end())
      v.insert(temp[i]);
	
  return v;
}

