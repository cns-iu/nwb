// -*- mode: C++;-*-

#ifndef RAND_H
#define RAND_H

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

  inline void circle(double &x,double &y)
  {
    gsl_ran_dir_2d(r,&x,&y);
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
