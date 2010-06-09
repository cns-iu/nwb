/** -*- mode:C++; -*-
 * @file defs.h
 * @author Bruno Goncalves
 *
 * Created by Bruno Goncalves on 09/14/08
 * Copywright 2008 Bruno Goncalves. All rights reserved.
 *
 */

#ifndef _DEFS_H_
#define _DEFS_H_

enum TYPE{
  INTERACTION = 0,
  SPONTANEOUS = 1
};

typedef struct
{
  char airport[4];
  unsigned population;
  unsigned country;
  unsigned region;
  unsigned continent;
  int hemisphere;
} City;

typedef struct
{
  unsigned id;
  unsigned i;
  unsigned j;
  int agent;
  double rate;
  TYPE type;
  bool secondary;
  int index;
} Transition;

#endif /* _DEFS_H_ */
