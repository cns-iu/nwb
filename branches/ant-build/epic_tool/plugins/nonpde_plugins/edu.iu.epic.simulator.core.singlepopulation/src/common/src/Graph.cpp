/**
 * @file Graph.cpp
 * @author Bruno Goncalves
 * @package HIV
 *
 * @date Created by Bruno Goncalves on 3/12/10.
 * Copyright 2010 Bruno Goncalves. All rights reserved.
 *
 */

#ifndef _GRAPH_CPP_
#define _GRAPH_CPP_

#include<cstdio>
#include<cstdlib>
#include<vector>
#include<string>
#include <map>
#include "Graph.h"
#include <iostream>
#include <fstream>
#include <sstream>

#ifndef BGDEBUG
#define at(x) operator[](x)
#endif

void Graph::mark(unsigned id, unsigned comp)
{
	if(marks.at(id) != -1)
		return;
	else
		marks.at(id) = comp;
	
	for(unsigned i=0;i<graph.at(id).size(); ++i)
		mark(graph.at(id).at(i).endNode, comp);
}

void Graph::markD(unsigned id, unsigned comp)
{
	if(marks.at(id) != -1)
		return;
	else
		marks.at(id) = comp;
	
	for(unsigned i=0;i<graph.at(id).size(); ++i)
		markD(graph.at(id).at(i).endNode, comp+1);
}


bool Graph::add(unsigned x, unsigned y, unsigned t, double w, bool directed, bool remap)
{
	if(x == y)
		return false;
	
	if(remap)
	{
		if(dict.find(x)==dict.end())
		{
			dict[x] = count++;
			revDict[dict[x]]=x;
		}
		
		if(dict.find(y)==dict.end())
		{
			dict[y] = count++;
			revDict[dict[y]] = y;
		}
		
		x=dict[x];
		y=dict[y];
	}
	
	if(x>=graph.size())
		graph.resize(x+1);
	
	if(y>=graph.size())
		graph.resize(y+1);
	
	for(unsigned i=0;i<graph.at(x).size();i++)
		if(graph.at(x).at(i).endNode == y)
			return false;
	
	tmp.endNode = y;
	tmp.weight = w;
	tmp.type = t;
	tmp.backLink = -1;
	
	if(w > Wmax)
		Wmax = w;
	
	if(!directed)
	{		
		for(unsigned i=0;i<graph.at(y).size();i++)
			if(graph.at(y).at(i).endNode == x)
				return false;
		
		graph.at(x).push_back(tmp);
		
		if(graph.at(x).size()>Kmax)
			Kmax=graph.at(x).size();		
		
		tmp.endNode = x;
		
		graph.at(y).push_back(tmp);
		graph.at(y).at(graph.at(y).size()-1).backLink = graph.at(x).size()-1;
		graph.at(x).at(graph.at(x).size()-1).backLink = graph.at(y).size()-1;
		
		unsigned posX = graph.at(y).at(graph.at(y).size()-1).backLink;
		unsigned posY = graph.at(x).at(graph.at(x).size()-1).backLink;
		
		if(graph.at(x).at(posX).endNode != y || graph.at(y).at(posY).endNode != x)
			fprintf(stderr,"OOPS!\n");
		
		if(graph.at(y).size()>Kmax)
			Kmax=graph.at(y).size();
	}
	else
	{
		graph.at(x).push_back(tmp);
		
		if(graph.at(x).size()>Kmax)
			Kmax=graph.at(x).size();
	}

	nnz++;
	return true;
}

void Graph::saveHeader(FILE *fpstate)
{
    fprintf(fpstate, "# time");
    
    for(unsigned i=0; i<graph.size(); ++i)
        fprintf(fpstate, " %u", revDict[i]);
        
    fprintf(fpstate, "\n");
}

bool Graph::isNeighbor(unsigned nodeA, unsigned nodeB)
{
	for(unsigned i=0; i<graph[nodeA].size(); ++i)
		if(graph[nodeA][i].endNode == nodeB)
			return true;

	return false;
}

Graph::Graph(std::string filename)
{
	nnz=0;
	Kmax=0;
	k2avg=0;
	N=0;
	shellmax=0;
	Wmax = 0;
	count = 0;
	
	std::ifstream in(filename.c_str());
	
	if(!in.is_open())
	{
	  fprintf(stderr,"Error opening file...%s\n",filename.c_str());
	  exit(1);
	}
	
	std::string line;
	std::vector<std::string> fields;
	
	do{
		std::getline(in, line);
		
		std::stringstream ss(line);
		unsigned x;
		unsigned y;
		double w;
		
		ss >> x;
		ss >> y;
		
		if(ss >> w)
			add(x, y, w);
		else
			add(x, y);
		
	}while(!in.eof());
	
	getPk();
	
	in.close();
}

Graph::Graph(std::vector<std::vector<NODE> > G)
{
	graph=G;
	N=graph.size();
	nnz=0;
	Kmax=0;
	k2avg=0;
	N=0;
	shellmax=0;
	count = 0; 
	
	
	for(unsigned i=0;i<graph.size();i++)
	{
		nnz+=graph.at(i).size();
		
		if(graph.at(i).size() > Kmax)
			Kmax = graph.at(i).size();
	}
		
	
	nnz/=2;
}

std::vector<int> Graph::comps()
{
	marks.resize(0);
	marks.resize(graph.size(), -1);
	
	unsigned comp = 0;
	
	for(unsigned i=0;i<graph.size();++i)
	{
		if(marks.at(i) != -1)
			continue;
		
		mark(i, comp++);
	}
	
	return marks;
}

std::vector<int> Graph::depth()
{
	marks.resize(0);
	marks.resize(graph.size(), -1);
	
	std::vector<int> Kin(graph.size(), 0);
	std::vector<int> Kout(graph.size(), 0);
	
	for(unsigned i=0;i<graph.size();++i)
	{
		Kout.at(i) = graph.at(i).size();
		
		for(unsigned j=0; j<graph.at(i).size();++j)
			Kin.at(graph.at(i).at(j).endNode)++;
	}	
	
	for(unsigned i=0;i<graph.size();++i)
	{
		// Start at the root
		if(marks.at(i) != -1 or Kin.at(i)!= 0)
			continue;
		
		markD(i, 0);
	}
	
	return marks;
}

std::vector<unsigned> Graph::getPi()
{
	std::vector<unsigned> Pi;
	
	Pi.resize(graph.size());
	
	for(unsigned i = 0; i < Pi.size(); i++)
		Pi.at(i) = graph.at(i).size();
	
	return Pi; 
}

std::vector<double> Graph::getPk()
{
	if(Pk.size()!=0)
		return Pk;
	
	if(N==0)
		getN();
	
	Pk.resize(0);
	Pk.resize(Kmax+1);
	
	for(unsigned i=0;i<graph.size();i++)
		if(graph.at(i).size()!=0)
			Pk.at(graph.at(i).size())+=1.0;
	
	kavg=0.0;
	
	for(unsigned i=0;i<Pk.size();i++)
	{
	  Pk.at(i)/=N;
	  kavg+=(i*Pk.at(i));
	}
	
	return Pk;
}

FILE *Graph::savePk(FILE *fp)
{
	if(fp==NULL)
	{
		fprintf(stderr,"NULL file pointer was provided to savePk()\n");
		return fp;
	}
	
	if(Pk.size()==0)
		getPk();
	
	for(unsigned i=0;i<Pk.size();i++)
		if(Pk.at(i)!=0)
			fprintf(fp,"%u %g\n",i,Pk.at(i));
	
	return fp;
}

void Graph::savePk(std::string filename)
{
	FILE *fp=fopen(filename.c_str(),"w");
	
	if(fp==NULL)
	{
		fprintf(stderr,"Error opening output file %s\n",filename.c_str());
		return;
	}
	
	savePk(fp);
	
	fclose(fp);
	
	return;
}

void Graph::savePi(std::string filename)
{
	FILE *fp=fopen(filename.c_str(),"w");
	
	if(fp==NULL)
	{
		fprintf(stderr,"Error opening output file: %s\n",filename.c_str());
		exit(1);
	}
	
	for(unsigned i=0;i<graph.size();i++)
		fprintf(fp,"%u %u\n",i,unsigned(graph.at(i).size()));
	
	fclose(fp);
	
	return;
}

double Graph::getPk(unsigned k)
{
	if(k>=Kmax)
		return -1.0;
	
	if(Pk.size()==0)
		getPk();
	
	return Pk.at(k);
}

double Graph::getK2avg()
{
	double k2avg=0.0;
	
	if(Pk.size()==0)
		getPk();
	
	for(unsigned i=1;i<Pk.size();i++)
		k2avg+=(Pk.at(i)*i*i);
	
	return k2avg;
}

std::vector<std::vector<unsigned> > Graph::getEkk()
{
	Ekk.resize(Kmax+1);
	
	for(unsigned i=0;i<=Kmax;i++)
		Ekk.at(i).resize(Kmax+1);
	
	for(unsigned i=0;i<graph.size();i++)
	{
	  unsigned k1=graph.at(i).size();
		
	  for(unsigned j=0;j<k1;j++)
		{
			unsigned NN=graph.at(i).at(j).endNode;
			unsigned k2=graph.at(NN).size();
			
			Ekk.at(k1).at(k2)++;
		}
	}
	
	return Ekk;
}

std::vector<std::vector<double> > Graph::getPkk()
{
	std::vector<std::vector<double> > Pkk;
	
	if(Ekk.size()==0)
		getEkk();
	
	Pkk.resize(Ekk.size());
	
	for(unsigned i=0;i<Pkk.size();i++)
		Pkk.at(i).resize(Kmax+1);
	
	for(unsigned i=0;i<Ekk.size();i++)
		for(unsigned j=0;j<Ekk.at(i).size();j++)
			Pkk.at(i).at(j)=double(Ekk.at(i).at(j))/(kavg*N);
	
	return Pkk;
}

void Graph::savePkk(std::string filename)
{
	FILE *fp=fopen(filename.c_str(),"w");
	
	if(fp==NULL)
	{
		fprintf(stderr,"Error opening output file %s\n",filename.c_str());
		return;
	}
	
	savePkk(fp);
	
	fclose(fp);
	
	return;
}

FILE *Graph::savePkk(FILE *fp)
{
	std::vector<std::vector<double> >Pkk=getPkk();
	
	for(unsigned i=0;i<Pkk.size();i++)
		for(unsigned j=0;j<Pkk.at(i).size();j++)
			if(Pkk.at(i).at(j)!=0)
				fprintf(fp,"%u %u %g\n",i,j,Pkk.at(i).at(j));
	
	return fp;
}

void Graph::saveEkk(std::string filename)
{
	FILE *fp=fopen(filename.c_str(),"w");
	
	if(fp==NULL)
	{
		fprintf(stderr,"Error opening output file %s\n",filename.c_str());
		return;
	}
	
	saveEkk(fp);
	
	fclose(fp);
	
	return;
}

FILE *Graph::saveEkk(FILE *fp)
{
	if(Ekk.size()==0)
		getEkk();
	
	for(unsigned i=0;i<Ekk.size();i++)
		for(unsigned j=0;j<Ekk.at(i).size();j++)
			fprintf(fp,"%u %u %u\n",i,j,Ekk.at(i).at(j));
	
	return fp;
}

std::vector<std::vector<int> > Graph::getMaxPath(std::vector<int> s)
{
	std::vector<std::vector<int> > path;
	
	unsigned current;
	int shell=0;
	
	for(unsigned i=0;i<s.size();++i)
		if(s.at(i)>shell)
	  {
	    shell=s.at(i);
	    current=i;
	  }
	
	path.resize(shell+1);
	
	for(unsigned i=0;i<s.size();++i)
		if(s.at(i)>=0)
			path.at(s.at(i)).push_back(i);
	
	return path;
}

std::vector<int> Graph::shells(unsigned current)
{
	unsigned n=graph.size();
	
	exclude.resize(n);
	
	for(unsigned i=0;i<n;i++)
		exclude.at(i)=-1;
	
	// The root node is shell 0
	exclude.at(current)=0;
	
	int shell=0;
	NN.push_back(current);
	
	//Continue until we run out of neighbours.
	while(NN.size()!=0)
	{
	  shell++;
		
	  for(unsigned i=0;i<NN.size();i++)
	    for(unsigned j=0;j<graph.at(NN.at(i)).size();j++)
			{
				current=graph.at(NN.at(i)).at(j).endNode;
				
				if(exclude.at(current)==-1)
					exclude.at(current)=shell;
			}
		
	  unsigned size=NN.size();
		
	  for(unsigned i=0;i<size;i++)
	    NN.pop_back();
		
	  for(unsigned i=0;i<n;i++)
	    if(exclude.at(i)==shell)
	      NN.push_back(i);
	}
	
	if(shell>int(shellmax))
		shellmax=shell;
	
	return exclude;
}

std::vector<double> Graph::shellsPk(unsigned current)
{
	shells(current);
	
	std::vector<double> sPk;
	sPk.resize(shellmax+1);
	
	for(unsigned i=0;i<sPk.size();i++)
		sPk.at(i)=0.0;
	
	for(unsigned i=0;i<exclude.size();i++)
		if(exclude.at(i)>0)
			sPk.at(exclude.at(i))++;
	
	for(unsigned i=0;i<sPk.size();i++)
		sPk.at(i)/=double(N-1);
	
	return sPk;
}

void Graph::shellsPk(unsigned current,std::vector<double> &sPk)
{
	shells(current);
	
	sPk.resize(0);
	sPk.resize(shellmax+1);
	
	for(unsigned i=0;i<sPk.size();i++)
		sPk.at(i)=0.0;
	
	for(unsigned i=0;i<exclude.size();i++)
		if(exclude.at(i)>0)
			sPk.at(exclude.at(i))++;
	
	for(unsigned i=0;i<sPk.size();i++)
		sPk.at(i)/=double(N-1);
}

void Graph::writeToFile(FILE *fp)
{
	if(fp==NULL)
	{
		fprintf(stderr,"ERROR writing to file!\n");
		exit(1);
	}
	
	for(unsigned i=0;i<graph.size();++i)
		for(unsigned j=0;j<graph.at(i).size();++j)
			fprintf(fp,"%u %u %g %u\n",i,graph.at(i).at(j).endNode, graph.at(i).at(j).weight, graph.at(i).at(j).type);
}
#endif
