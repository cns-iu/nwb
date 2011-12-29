/**
 * @file ExactModel.cpp
 * @author Bruno Goncalves
 * @package HIV
 *
 * @date Created by Bruno Goncalves on 7/15/10.
 * Copyright 2010 Bruno Goncalves. All rights reserved.
 *
 */

#include <ExactModel.h>

ExactModel::ExactModel(Parse &parse)
{
	Parameters.transList = parse.getTransitions();
	Parameters.NStates = parse.NStates();
	Parameters.transCount = Parameters.transList.size();
	
	T = gsl_odeiv_step_rk8pd;
	s = gsl_odeiv_step_alloc (T, Parameters.NStates + Parameters.transCount);
	c = gsl_odeiv_control_y_new (1e-6, 0.0);
	e = gsl_odeiv_evolve_alloc (Parameters.NStates + Parameters.transCount);
	
	h = 1e-6;
	
	gsl_odeiv_system sys2 = {func, jac, Parameters.NStates + Parameters.transCount, &Parameters};
	
	sys = sys2;
}

int ExactModel::step(double &t, double t1, double *y)
{
	return gsl_odeiv_evolve_apply (e, c, s, &sys, &t, t1, &h, y);
}

ExactModel::~ExactModel()
{
	gsl_odeiv_evolve_free (e);
	gsl_odeiv_control_free (c);
	gsl_odeiv_step_free (s);
}

