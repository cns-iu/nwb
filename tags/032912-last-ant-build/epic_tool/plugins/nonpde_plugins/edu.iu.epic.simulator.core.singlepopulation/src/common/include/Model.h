/** -*- mode: C++;-*-
 * @file Model.h
 * @author Bruno Goncalves
 *
 * Created by Bruno Goncalves on 09/14/08
 * Copywright 2008 Bruno Goncalves. All rights reserved.
 *
 */

#ifndef _MODEL_H_
#define _MODEL_H_

#include <vector>
#include <map>
#include <stdint.h>
#include <sstream>

#include "defs.h"
#include "Rand.h"
#include "Parser.h"


/**
 * @class Model
 *
 */
class Model
{
  std::map<std::string, unsigned> states;
  std::vector<std::vector<Transition> > transitions;
  std::vector<unsigned> newState;
  std::vector<std::string> files;
  std::vector<std::vector<std::vector<double> > > rates; 
  Rand *r;
  std::vector<std::string> labels;
  double *p;
  unsigned *n;

 public:
   Model(Parser &parse, Rand &r2);

  std::vector<std::vector<std::vector<double> > > getRates()
  {
    return rates;
  }

  /**
   * @param current
   * @param N
   * @param x
   * @param y
   * @param fp
   * @return The number of secondary infections
   */
  int step(std::vector<int64_t> &current, int64_t N, int day, int hemi, FILE **fp, std::vector<unsigned> &outTrans, vector<uint64_t> &transCount);
};

#endif /* _MODEL_H_ */
