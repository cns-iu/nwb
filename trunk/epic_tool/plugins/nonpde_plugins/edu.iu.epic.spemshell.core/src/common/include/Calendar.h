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

  bool isLeap(unsigned year);

public:	
  Calendar(std::string m, unsigned y=2001);

  unsigned day(std::string date);
  
  unsigned day(unsigned d, unsigned m);
  
  unsigned month(unsigned day);
};

#endif /* CALENDAR_H */
