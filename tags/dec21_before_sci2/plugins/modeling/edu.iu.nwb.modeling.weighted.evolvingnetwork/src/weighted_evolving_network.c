#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<string.h>
#include<math.h>

#define NAMELENGTH 100000

int initialnumberofnodes;
int initialnumberofedges;
int numberofedges;
int *sourcenodeid;
int *targetnodeid;
double *edgeweight;
long seedirn;

void initializeSeedOfRandomNumberGenerator(long *, unsigned int);
void setInitialNumberOfNodes(int *, int);
void setInitialNumberOfEdges(int *, int);
void determineFinalNumberOfEdges(int *, int, int, int, int);
void allocateMemoryForEdgeAttribute(int);
void setInitialWeightData(int *, int *, double *, int);
void constructWeightedNetwork(int *, int *, double *, int, double, int, int, int, int);
void determineNodeStrength(double *, int *, int *, double *, int);
void determineEdgeProbability(double *, double *, int);
void determineEdge(int *, int *, double *, int *, int, double *, int *);
int chooseANodeRandomly(double *, int, long *);
void addEdge(int *, int *, double *, int *, int, int);
void updateEdgeProbability(double *, int, int);
void updateEdgeWeight(int *, int *, double *, int, int, double *, double);
void writeNetworkInNWBFileFormat(int, int, double, unsigned int, char *);
void freeMemoryOfEdgeAttribute(void);
double ran2IntegerRandomNumber(long *);

int main(int argc, char **argv)
{
  char output_filename[NAMELENGTH+1];
  unsigned int seedrand;
  int numberofnodes;
  int numberofnewedges;
  double delta;

  if(argc > 4)
    {
      sscanf(argv[1], "%d", &numberofnodes);
      printf("Total number of nodes : %d\n", numberofnodes);
      sscanf(argv[2], "%d", &numberofnewedges);
      printf("Number of links set by a newly added node : %d\n", numberofnewedges);
      sscanf(argv[3], "%lf", &delta);
      printf("Increase in strength of a node as result of a newly added connection : %lf\n", delta);
      sscanf(argv[4], "%d", &seedrand);
      printf("Seed of random number generator : %d\n", seedrand);

      initializeSeedOfRandomNumberGenerator(&seedirn, seedrand);

      setInitialNumberOfNodes(&initialnumberofnodes, numberofnewedges);
      printf("Initial number of nodes is set to %d\n", initialnumberofnodes);

      setInitialNumberOfEdges(&initialnumberofedges, initialnumberofnodes);
      printf("Initial number of edges is set to %d\n", initialnumberofedges);

      determineFinalNumberOfEdges(&numberofedges, numberofnodes, initialnumberofnodes, numberofnewedges, initialnumberofedges);
      printf("Final number of edges : %d\n", numberofedges);

      allocateMemoryForEdgeAttribute(numberofedges);
      setInitialWeightData(sourcenodeid, targetnodeid, edgeweight, initialnumberofnodes);
      constructWeightedNetwork(sourcenodeid, targetnodeid, edgeweight, numberofnewedges, delta, 
			       initialnumberofnodes, numberofnodes, initialnumberofedges, numberofedges);

      sprintf(output_filename, "evolvingweightednetwork");
      writeNetworkInNWBFileFormat(numberofnodes, numberofnewedges, delta, seedrand, output_filename);
      freeMemoryOfEdgeAttribute();
    }
  else
    printf("Error: Insufficient number of inputs\n");
  return 0;
}

void initializeSeedOfRandomNumberGenerator(long *seedirn, unsigned int seedrand)
{
  srand(seedrand);
  *seedirn=(long)(-1*rand());
}

void setInitialNumberOfNodes(int *innode, int m)
{
  *innode=(m+1);
}

void setInitialNumberOfEdges(int *inedge, int innode)
{
  double tmpinnode, tmpinedge;

  tmpinnode=(double)innode;
  tmpinedge=(tmpinnode*(tmpinnode-1.0))/2.0;
  *inedge=(int)tmpinedge;
}

void determineFinalNumberOfEdges(int *nedge, int nnode, int innode, int m, int inedge)
{
  double tmpnedge, tmpm;

  tmpm=(double)m;
  tmpnedge=(nnode-innode)*tmpm+inedge;
  if(tmpnedge > 1.0E+9)
    {
      printf("Error: Final number of edges is out of limit !...\n");
      printf("Program will be terminated ...\n");
      exit(1);
    }
  *nedge=(int)tmpnedge;
}

void allocateMemoryForEdgeAttribute(int nedge)
{
  sourcenodeid=(int *) calloc(nedge, sizeof(int));
  targetnodeid=(int *) calloc(nedge, sizeof(int));
  edgeweight=(double *) calloc(nedge, sizeof(double));
}

void freeMemoryOfEdgeAttribute(void)
{
  free(sourcenodeid);
  free(targetnodeid);
  free(edgeweight);
}

void setInitialWeightData(int *sid, int *tid, double *w, int innode)
{
  int ctr1, ctr2, nedge;

  nedge=0;
  for(ctr1=0; ctr1 < innode-1; ctr1++)
    for(ctr2=ctr1+1; ctr2 < innode; ctr2++)
      {
	sid[nedge]=ctr1;
	tid[nedge]=ctr2;
	w[nedge]=1.0;
	nedge++;
      }
}

void constructWeightedNetwork(int *sid, int *tid, double *w, int m, double delta, int innode, int fnnode, int inedge, int fnedge)
{
  int t, nnode, nedge;
  double *nodestrength, *edgeprobability;

  nnode=innode;
  nedge=inedge;
  for(t=1; t <= (fnnode-m-1); t++)
    {
      nodestrength=(double *) calloc(nnode, sizeof(double));
      edgeprobability=(double *) calloc(nnode, sizeof(double));
      determineNodeStrength(nodestrength, sid, tid, w, nedge);
      determineEdgeProbability(edgeprobability, nodestrength, nnode);
      determineEdge(sid, tid, w, &nedge, m, edgeprobability, &nnode);
      updateEdgeWeight(sid, tid, w, nedge, m, nodestrength, delta);
      free(edgeprobability);
      free(nodestrength);
    }
}

void determineNodeStrength(double *s, int *sid, int *tid, double *w, int nedge)
{
  int ctre, ctrs, ctrt;

  for(ctre=0; ctre < nedge; ctre++)
    {
      ctrs=sid[ctre];
      ctrt=tid[ctre];
      s[ctrs]=s[ctrs]+w[ctre];
      s[ctrt]=s[ctrt]+w[ctre];
    }
}

void determineEdgeProbability(double *p, double *s, int nnode)
{
  int ctr;
  double normalization;

  normalization=0.0;
  for(ctr=0; ctr < nnode; ctr++)
    {
      p[ctr]=s[ctr];
      normalization=normalization+p[ctr];
    }
  for(ctr=0; ctr < nnode; ctr++)
    p[ctr]=p[ctr]/normalization;
}

void determineEdge(int *sid, int *tid, double *w, int *nedge, int m, double *p, int *nnode)
{
  int ctr, id;

  for(ctr=0; ctr < m; ctr++)
    {
      id=chooseANodeRandomly(p, *nnode, &seedirn);
      addEdge(sid, tid, w, nedge, *nnode, id);
      updateEdgeProbability(p, *nnode, id);
    }
  *nnode=*nnode+1;
}

int chooseANodeRandomly(double *p, int nnode, long *idum)
{
  int ctr;
  double randomprobability, interval;

  randomprobability=ran2IntegerRandomNumber(idum);
  ctr=0;
  interval=p[ctr];
  while(interval < randomprobability)
    {
      ctr++;
      interval=interval+p[ctr];
    }
  if(ctr >= nnode)
    {
      printf("Error : Improper normalization !...\n");
      printf("Program will be terminated ...\n");
      exit(1);
    }
  return ctr;
}

void addEdge(int *sid, int *tid, double *w, int *nedge, int nnode, int id)
{
  sid[*nedge]=nnode;
  tid[*nedge]=id;
  w[*nedge]=1.0;
  *nedge=*nedge+1;
}

void updateEdgeProbability(double *p, int nnode, int id)
{
  int ctr;
  double normalization;

  normalization=1.0-p[id];
  p[id]=0.0;
  for(ctr=0; ctr < nnode; ctr++)
    p[ctr]=p[ctr]/normalization;
}

void updateEdgeWeight(int *sid, int *tid, double *w, int nedge, int m, double *s, double delta)
{
  int ctre1, ctre2, id;
  double *tmpw;

  tmpw=(double *) calloc(nedge, sizeof(double));
  for(ctre1=nedge-m; ctre1 < nedge; ctre1++)
    {
      id=tid[ctre1];
      for(ctre2=0; ctre2 < nedge-m; ctre2++)
	if(sid[ctre2]==id || tid[ctre2]==id)
	  tmpw[ctre2]=tmpw[ctre2]+(delta*w[ctre2])/s[id];
    }
  for(ctre1=0; ctre1 < nedge; ctre1++)
    w[ctre1]=w[ctre1]+tmpw[ctre1];
  free(tmpw);
}

void writeNetworkInNWBFileFormat(int numberofnodes, int numberofnewedges, double delta, unsigned int seedrand, char *quantity)
{
  char output_filename[NAMELENGTH+1];
  int ctrn, ctre;
  FILE *fp;

  sprintf(output_filename, "%s.nwb", quantity);
  fp=fopen(output_filename, "w");
  if(fp==NULL)
    {
      printf("Can not open file %s! ...\n", output_filename);
      exit(1);
    }
  fprintf(fp, "# Evolving weighted network :\n");
  fprintf(fp, "# Total number of nodes : %d\n", numberofnodes);
  fprintf(fp, "# Number of links set by a newly added node : %d\n", numberofnewedges);
  fprintf(fp, "# Increase in strength of a node as result of a newly added connection : %lf\n", delta);
  fprintf(fp, "# Seed of random number generator : %d\n", seedrand);
  fprintf(fp, "*Nodes %d\n", numberofnodes);
  fprintf(fp, "id*int label*string\n");
  for(ctrn=0; ctrn < numberofnodes; ctrn++)
    fprintf(fp, "%d \"%d\"\n", ctrn+1, ctrn+1);
  fprintf(fp, "*UndirectedEdges %d\n", numberofedges);
  fprintf(fp, "source*int target*int weight*float\n");
  for(ctre=0; ctre < numberofedges; ctre++)
    fprintf(fp, "%d %d %E\n", sourcenodeid[ctre]+1, targetnodeid[ctre]+1, edgeweight[ctre]);
  fclose(fp);
}

// (C) Copr. 1986-92 Numerical Recipes Software.

#define IM1 2147483563
#define IM2 2147483399
#define AM (1.0/IM1)
#define IMM1 (IM1-1)
#define IA1 40014
#define IA2 40692
#define IQ1 53668
#define IQ2 52774
#define IR1 12211
#define IR2 3791
#define NTAB 32
#define NDIV (1+IMM1/NTAB)
#define EPS 1.2e-7
#define RNMX (1.0-EPS)

double ran2IntegerRandomNumber(long *idum)
{
	int j;
	long k;
	static long idum2_irn=123456789;
	static long iy_irn=0;
	static long iv_irn[NTAB];
	double temp;

	if (*idum <= 0) {
		if (-(*idum) < 1) *idum=1;
		else *idum = -(*idum);
		idum2_irn=(*idum);
		for (j=NTAB+7;j>=0;j--) {
			k=(*idum)/IQ1;
			*idum=IA1*(*idum-k*IQ1)-k*IR1;
			if (*idum < 0) *idum += IM1;
			if (j < NTAB) iv_irn[j] = *idum;
		}
		iy_irn=iv_irn[0];
	}
	k=(*idum)/IQ1;
	*idum=IA1*(*idum-k*IQ1)-k*IR1;
	if (*idum < 0) *idum += IM1;
	k=idum2_irn/IQ2;
	idum2_irn=IA2*(idum2_irn-k*IQ2)-k*IR2;
	if (idum2_irn < 0) idum2_irn += IM2;
	j=iy_irn/NDIV;
	iy_irn=iv_irn[j]-idum2_irn;
	iv_irn[j] = *idum;
	if (iy_irn < 1) iy_irn += IMM1;
	if ((temp=AM*iy_irn) > RNMX) return RNMX;
	else return temp;
}
