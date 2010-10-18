// -*- mode: C++;-*-

#ifndef RAND_H
#define RAND_H

#ifdef SWIG
%include "Rand.h"
%{
#include <gsl/gsl_rng.h>
#include <gsl/gsl_randist.h>
#include <Rand.h>
#include <math.h>
  %}
#endif

#include <gsl/gsl_rng.h>
#include <gsl/gsl_randist.h>
#include <gsl/gsl_math.h>
#include <math.h>
#include <time.h>
#include <unistd.h>

using namespace std;

/**
 * Wrapper class for the GSL pseudo random generator.
 * This class simplifies the use of the most commonly used Random Number
 * utilities provided by the GNU Scientific Library. It is meant to increase
 * its usability in an OOP enviornment by hidding the pointer to the random 
 * generator structure and maintaining a larger internal state that allows
 * for the simplification of the interface available to the client programs.
 */
class Rand
{
  gsl_rng *r;
  const gsl_rng_type *T;
  long int seed;
  
 public:
  Rand()
    {
      T=gsl_rng_default;
      r=gsl_rng_alloc(T);
      seed=gsl_rng_default_seed;
    }
  
  Rand(long int s)
    {
      T=gsl_rng_default;
      r=gsl_rng_alloc(T);
      setSeed(s);
    }
  
  inline void setSeed(long int s)
  {
    if(s==0)
      {
	time_t t;
	time(&t);
	s=unsigned(t)+getpid();
	fprintf(stdout,"# seed %ld\n",s);
	fprintf(stderr,"# seed %ld\n",s);
      }
  
    gsl_rng_set(r,s);
    seed=s;
  }
  
  inline unsigned binomial(double p, unsigned int n)
  {
	if(p<0 || p>1)
	  fprintf(stderr,"oops! %g\n", p);

    return gsl_ran_binomial(r,p,n);
  }

  void multinomial(unsigned K, unsigned N, double *p, unsigned *n)
  {
    gsl_ran_multinomial(r, K, N, p, n);
  }

  inline long unsigned getSeed()
  {
    return seed;
  }

  inline const char* getType()
  {
    return gsl_rng_name(r);
  }

  inline unsigned long int getMax()
  {
    return gsl_rng_max(r);
  }

  inline unsigned long int getMin()
  {
    return gsl_rng_min(r);
  }

  inline long unsigned get()
  {
    return gsl_rng_get(r);
  }
  
  inline double getProb()
  {
    return gsl_rng_uniform(r);
  }

  /**
   * This function returns a Gaussian random variate, with mean zero and
   * standard deviation sigma. The probability distribution for Gaussian 
   * random variates is, \f[p\left(x\right) dx = \left\{\frac{1}{\sqrt{2 \pi \sigma^2}}\right\}\exp^{\frac{-x^2}{2\sigma^2}} dx\f]
   * for \f$x \in [-\infty,+\infty]\f$. Use the transformation 
   * \f$z = \mu + x\f$ on the numbers returned by gaussian to obtain a 
   * Gaussian distribution with mean \f$\mu\f$. This function uses the 
   * Box-Mueller algorithm which requires two calls to the random number
   * generator r.
   *
   * @param sigma The standard deviation of the gaussian distribution
   */
  inline double gaussian(double sigma)
  {
    return gsl_ran_gaussian(r,sigma);
  }

  inline double poisson(double sigma)
  {
    return gsl_ran_poisson(r,sigma);
  }

  inline double exponential(double mu)
  {
    return gsl_ran_exponential(r,mu);
  }

  inline double exponential(double mu, double muMax)
  {
    double x;
	  
    while((x=gsl_ran_exponential(r,mu))>muMax){};
  
    return x;
  }

  inline double laplace(double a)
  {
    return gsl_ran_laplace(r,a);
  }

  inline double uniform(double a,double b)
  {
    return gsl_ran_flat(r,a,b);
  }

  /**
   * Exponential distirbution of base 2
   * \f[
   *     y=-\frac{\log\left[1-r\left(1-2^{-N\left(\tau-1\right)}\right)\right]}{\log\left(2\right)\left(\tau-1\right)}   
   * \f]
   */
  inline unsigned exp2(unsigned N,double tau)
  {
    unsigned y;
	 
    while((y=unsigned(floor(log(1-gsl_rng_uniform(r)*(1-pow(2.0,(1.0-tau)*N)))/((1.0-tau)*log(2.0)))))>=N){};
	  
    return y;
  }

  /**
   * Generates a random integer that obeys a power-law distribution with exponet \f$\tau\f$.
   * The numbers are generated using GSLs implementation of a Pareto
   * distribution of order \f$\tau-1\f$. Pareto distribution is of the form:
   * \f[p(x)\mathrm{d}x=\frac{\frac{a}{b}}{\left(\frac{x}{b}\right)^{a+1}}\mathrm{d}x \f]
   * By setting \f$a\f$ to \f$\tau-1\f$ and \f$b\f$ to \f$1\f$, we obtain the
   * desired power-law distribution:
   * \f[p(x)\mathrm{d}x=\frac{\tau-1}{x^\tau}\mathrm{d}x\f]
   * we then round the resulting double to the lowest integer and return it
   * as an int after having shifted it so as to include the value 0.
   * The claim that the resulting distribution is the expected one has been
   * verified by analysing the results in Matlab. If the value of \f$\tau\f$
   * specified is negative, its absolute value is used instead.
   *
   * @param tau The absolute value of the exponent of the power-law distribution.
   * @param N The upper limit for which the distribution should be defined
   * @return x An integer that obeys the power law distribution defined above.
   */
  inline int power(double tau,unsigned N=RAND_MAX)
  {
    tau=fabs(tau);

    if(N==RAND_MAX)
      return int(floor(gsl_ran_pareto(r,tau-1.0,1.0)-1.0));
    else
      {
	double temp;
		  
	do{
	  temp=gsl_ran_pareto(r,tau-1.0,1.0)-1.0;
	}while(temp>=N);
		  
	return int(floor(temp/(1-pow(double(N),1.0-tau))));
      }
  }

  inline void circle(double &x,double &y)
  {
    gsl_ran_dir_2d(r,&x,&y);
  }
  
  inline void sphere(double &x,double &y,double &z)
  {
    //double phi=2.0*M_PI*gsl_rng_uniform(r);
    double phi=gsl_ran_flat(r,0.0,2.0*M_PI);
	  
    //z=(2*gsl_rng_uniform(r)-1);
    z=gsl_ran_flat(r,-1.0,1.0);
    x=sqrt(1-z*z)*cos(phi);
    y=sqrt(1-z*z)*sin(phi);
  }
  
  inline double gamma(double a,double b)
  {
    return gsl_ran_gamma(r,a,b);
  }
  
  unsigned operator()(unsigned N)
  {
      return get()%N;
  }

  ~Rand()
    {
      gsl_rng_free(r);
    }
};
#endif
