/**
 * @file ExactModel.h
 * @author Bruno Goncalves
 * @package HIV
 *
 * @date Created by Bruno Goncalves on 7/15/10.
 * Copyright 2010 Bruno Goncalves. All rights reserved.
 *
 */

#ifndef _EXACTMODEL_H_
#define _EXACTMODEL_H_

#include <defs.h>
#include <Parse.h>

#include <gsl/gsl_errno.h>
#include <gsl/gsl_matrix.h>
#include <gsl/gsl_odeiv.h>

typedef struct
{
  std::vector<Transition> transList;
  unsigned NStates;
} PARAMETERS;

class ExactModel
{
private:
  PARAMETERS Parameters;
	const gsl_odeiv_step_type * T;
  gsl_odeiv_step *s;
  gsl_odeiv_control *c;
  gsl_odeiv_evolve *e;
  gsl_odeiv_system sys;
  double h;
	
  static int func (double t, const double population[], double f[], void *params)
	{
		PARAMETERS *parameters = (PARAMETERS *)params;
		
		unsigned NTrans = (*parameters).transList.size();
		unsigned NStates = (*parameters).NStates;
		
		t = 0;
		double N = 0;
		
		for(unsigned i=0;i<NStates; ++i)
		{
			f[i] = 0;
			N += population[i];
		}
		
		for(unsigned i=0;i<NTrans; ++i)
		{
			unsigned start = (*parameters).transList[i].i;
			unsigned end = (*parameters).transList[i].j;
			double rate = (*parameters).transList[i].rate;
			
			if((*parameters).transList[i].type == SPONTANEOUS)
			{
				f[start] -= rate*population[start];
				f[end] += rate*population[start];
			}
			else if((*parameters).transList[i].type == INTERACTION)
			{
				unsigned agent = (*parameters).transList[i].agent;
				
				f[start] -= rate*population[start]*population[agent]/N;
				f[end] += rate*population[start]*population[agent]/N;
			}
		}
		
		return GSL_SUCCESS;
	}
	
	static int jac (double t, const double population[], double *dfdy, double dfdt[], void *params)
	{
		PARAMETERS *parameters = (PARAMETERS *)params;
		
		unsigned NTrans = (*parameters).transList.size();
		unsigned NStates = (*parameters).NStates;
		
		std::vector<std::vector<double> > jaco;
		
		t = 0;
		
		jaco.resize(NStates);
		
		double N = 0;
		
		for(unsigned i=0;i<NStates; ++i)
		{
			jaco[i].resize(NStates, 0);
			dfdy[i] = 0;
			dfdt[i] = 0;
			N += population[i];
		}
		
		for(unsigned i=0;i<NTrans; ++i)
		{
			unsigned start = (*parameters).transList[i].i;
			unsigned end = (*parameters).transList[i].j;
			double rate = (*parameters).transList[i].rate;
			
			if((*parameters).transList[i].type == SPONTANEOUS)
			{
				jaco[start][start] -= rate;
				jaco[end][start] += rate;
			}
			else if((*parameters).transList[i].type == INTERACTION)
			{
				unsigned agent = (*parameters).transList[i].agent;
				
				jaco[start][start] -= rate*population[agent]/N;
				jaco[start][agent] -= rate*population[start]/N;
				
				jaco[end][start] += rate*population[agent]/N;
				jaco[end][agent] += rate*population[start]/N;
			}
		}
		
		gsl_matrix_view dfdy_mat = gsl_matrix_view_array (dfdy, NStates, NStates);
		gsl_matrix *m = &dfdy_mat.matrix; 
		
		for(unsigned i=0; i<NStates; ++i)
			for(unsigned j=0; j<NStates; ++j)
				gsl_matrix_set (m, i, j, jaco[i][j]);
		
		return GSL_SUCCESS;
	}
	
public:
  ExactModel(Parse &parse);
	int step(double &t, double t1, double *y);
	~ExactModel();
};

#endif /* _EXACTMODEL_H_ */
