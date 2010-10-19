/** -*- mode:C++; -*-
 * @file Calendar.h
 * @author Bruno Goncalves
 *
 * Created by Bruno Goncalves on 09/22/08
 * Copywright 2008 Bruno Goncalves. All rights reserved.
 *
 */

#ifndef _CALENDAR_H_
#define _CALENDAR_H_

#include <string>
#include <vector>
#include <iostream>
#include <fstream>
#include <map>
#include <sstream>

class Calendar
{
  std::vector<unsigned> months;
  std::map<std::string, unsigned> keys;
  bool leap;
  unsigned year;

  bool isLeap(unsigned year)
  {
    if(year%400 == 0)
      return true;

    if(year%100 == 0)
      return false;

    if(year%4 == 0)
      return true;

    return false;
  }


public:	
  Calendar(std::string m, unsigned y=2001) : year(y)
  {
    leap = isLeap(year);

    months.resize(12);

    std::ifstream in(m.c_str());

    if(!in.is_open())
      {
	fprintf(stderr,"Error opening file: %s\n",m.c_str());
	exit(3);
      }

    std::string input;

    while(!in.eof())
      {
	getline(in, input);

	if(input.size() == 0)
	  continue;

	std::stringstream ss(input);
	
	unsigned index, days;
	std::string key;

	ss >> index;
	ss >> key;
	ss >> days;

	if(index > 11)
	  {
	    fprintf(stderr,"Error: wrong format for month file %s\n", m.c_str());
	    exit(3);
	  }
	  
	keys[key] = index;
	months[index] = days;
      }

    in.close();
  }

  inline 
  unsigned day(std::string date)
  {

    for(unsigned i=0;i<date.size();++i)
      if(date[i] == '/')
	date[i] = ' ';

    std::stringstream ss(date);
    unsigned d, m, y;

    ss>>m;
    ss>>d;
    ss>>y;
    
    return day(d,m);
  }

  inline 
  unsigned day(unsigned d, unsigned m)
  {
    unsigned day = 0;
        
    if(d>months[m])
      {
	fprintf(stderr,"Unable to parse date: %02u/%02u\n",m,d);
	exit(3);
      }

    for(unsigned i=0; i<m-1; ++i)
      day += months[i];

    day += d;

    //In leap years Feb has one extra day
    if(leap == true && m>1)
      day ++;

    return day;
  }

  inline
  unsigned month(unsigned day)
  {
    unsigned count=0;

    unsigned dy = day/366;
    
    unsigned day2 = day;

    for(unsigned i=0;i<dy;++i)
      if(!isLeap(i+year))
	day2 = day2 - 365;
      else
	day2 = day2 - 366;


    if(leap or isLeap(year+dy))
      months[1]++;

    unsigned final = 12;

    for(unsigned i=0;i<months.size();++i)
      {
	count += months[i];

	if(count >= day2)
	  {
	    final = i;
	    break;
	  }
      }
    
    if(leap or isLeap(year+dy))
      months[1]--;

    return final;
  }
};

#endif
