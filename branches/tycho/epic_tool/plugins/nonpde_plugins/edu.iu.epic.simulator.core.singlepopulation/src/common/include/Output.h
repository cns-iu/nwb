/** -*- mode: C++;-*-
 * @file Output.h
 * @author Bruno Goncalves
 *
 * Created by Bruno Goncalves on 09/22/09
 * Copywright 2009 Bruno Goncalves. All rights reserved.
 *
 */

#ifndef __OUTPUT_H__
#define __OUTPUT_H__

#include <vector>
#include <stdint.h>
#include<cstdio>

class Output
{
  std::vector<std::vector<uint64_t> > basin;
  std::vector<uint64_t> popBasin;

  void initVector(std::vector<std::vector<uint64_t> > &vec, unsigned size, unsigned time);

  void writeVector(std::vector<std::vector<uint64_t> > &vec, std::string filename, std::vector<uint64_t> &pop);

public:
  Output() {}

  Output(unsigned pop, int time) 
  {
    popBasin.resize(1);

    popBasin[0] = pop;

    init(pop, time);
  }

  void init(unsigned pop, unsigned time);

  void add(std::vector<uint64_t> values, unsigned time);
  void add(std::vector<unsigned> values, unsigned time);
  void add(unsigned id, uint64_t value, unsigned time);
  void writeToFile(std::string filename);
};

#endif
