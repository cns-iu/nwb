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
#include <boost/date_time/gregorian/gregorian.hpp>
#include <defs.h>

class Calendar
{
  boost::gregorian::date birthday;
  void setDate(std::string date);
  char buffer[BUFFER_SIZE];
  
public:	
  Calendar(std::string date);
  unsigned day();
  unsigned day(unsigned d, unsigned m, unsigned y);
  unsigned increment();
  std::string getDate();
  std::string getDateTomorrow();
};

#endif /* CALENDAR_H */
