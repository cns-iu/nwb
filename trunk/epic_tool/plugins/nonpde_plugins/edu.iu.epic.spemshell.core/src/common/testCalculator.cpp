/**
 * @file testCalculator.cpp
 * @author Bruno Goncalves
 *
 * Created by Bruno Goncalves on 09/17/08
 * Copywright 2008 Bruno Goncalves. All rights reserved.
 *
 */

#include <Calculator.h>
#include <cstdio>
#include <cstdlib>

int main(int argc, char *argv[])
{
  Calculator calc;

  calc.set("p",2.0);
  calc.set("g",3.0);
  calc.set("p",1.24);
  fprintf(stdout,"p=%g\n",calc.calculate("p+g*2.0"));

  return 0;
}
