/** -*- mode:C++; -*-
 * @file Calendar.cpp
 * @author Bruno Goncalves
 *
 * Created by Bruno Goncalves on 09/22/08
 * Copywright 2008 Bruno Goncalves. All rights reserved.
 *
 */

#include <cstdlib>
#include <string>
#include <vector>
#include <iostream>
#include <fstream>
#include <map>
#include <sstream>
#include <Calendar.h>
#include <cstdlib>
#include <boost/date_time/gregorian/gregorian.hpp>

using namespace boost::gregorian;

void Calendar::setDate(std::string d2)
{
  for(unsigned i=0;i<d2.size();++i)
    if(d2[i] == '/')
      d2[i] = ' ';

  std::stringstream ss(d2);
  unsigned d, m, y;

  ss>>m;
  ss>>d;
  ss>>y;
    
  birthday = date(y, m, d);
}

Calendar::Calendar(std::string date)
{
  setDate(date);
}

unsigned Calendar::day()
{
  return birthday.day_of_year();
}

unsigned Calendar::day(unsigned d, unsigned m, unsigned y)
{
  date birthday(y, m, d);

  return birthday.day_of_year();
}

unsigned Calendar::increment()
{
  days one_day(1);
  birthday += one_day;

  return birthday.day_of_year();
}

std::string Calendar::getDate()
{
  date::ymd_type ymd = birthday.year_month_day();
  
  sprintf(buffer,"%04u-%02u-%02u", (int) ymd.year, (int) ymd.month, (int) ymd.day);

  return buffer;
}

std::string Calendar::getDateTomorrow()
{
  days one_day(1);
  date::ymd_type ymd = (birthday + one_day).year_month_day();
  
  sprintf(buffer,"%04u-%02u-%02u", (int) ymd.year, (int) ymd.month, (int) ymd.day);

  return buffer;
}
