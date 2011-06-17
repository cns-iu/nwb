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
  unsigned id;
  unsigned i;
  unsigned j;
  int agent;
  double rate;
  TYPE type;
  bool secondary;
  int index;
} Transition;

typedef struct
{
        unsigned endNode;
        double weight;
        int backLink;
        unsigned type;
} NODE;

#define BUFFER_SIZE 2048
#define TRANS_SIZE 10

#endif /* _DEFS_H_ */
