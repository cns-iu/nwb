#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<string.h>
#include<math.h>

#define NAMELENGTH 1000000

int numberofnodes;
int numberofedges;
int *sourcenodeid;
int *targetnodeid;
double *edgeweight;
int *nodedegree;
int minnodedegree, maxnodedegree;
double *nodestrength;
double *nodenearestneighbordegree;
int *nodedegreedistribution;
double *averagenodenearestneighbordegree;
double *newnodedegree_logbinned;
double *nodedegreedistribution_logbinned;
double *averagenodenearestneighbordegree_logbinned;

void readEdgeWeightData(char *);
void allocateMemoryForNodeAttribute(int);
void determineNodeDegree(int *, int *, int *, int);
void determineNodeStrength(double *, int *, int *, double *, int);
void determineNodeNearestNeighborDegree(double *, int *, double *, int, int *, int *, double *, int);
void writeNodeAttribute(double *, int, char *, char *);
void determineExtremumValuesOfIntegerTypeQuantity(int *, int *, int *, int);
void allocateMemoryForGlobalMeasurementLinearBinned(int, int);
void determineAverageQuantityWithRespectToDegreeLinearBinned(double *, int *, double *, int, int, int *, int);
void writeAverageQuantityWithRespectToDegreeLinearBinned(double *, int *, int, int, char *, char *);
void allocateMemoryForGlobalMeasurementLogarithmicBinned(int);
void determineAverageQuantityWithRespectToDegreeLogarithmicBinned(double *, double *, double *, double *, int, int, int *, int, int);
void writeAverageQuantityWithRespectToDegreeLogarithmicBinned(double *, double *, double *, int, char *, char *);
void freeMemoryOfGlobalMeasurementLogarithmicBinned(void);
void freeMemoryOfGlobalMeasurementLinearBinned(void);
void freeMemoryOfNodeAttribute(void);

int main(int argc, char **argv)
{
  char input_filename[NAMELENGTH+1];
  char quantityname[NAMELENGTH+1];
  char output_type[NAMELENGTH+1];
  int numberofbins;

  if(argc > 2)
    {
      sprintf(input_filename, "%s", argv[1]);
      sscanf(argv[2], "%d", &numberofbins);

      readEdgeWeightData(input_filename);

      sprintf(quantityname, "averagenearestneighbordegree");
      sprintf(output_type, "nodes");
      allocateMemoryForNodeAttribute(numberofnodes);
      determineNodeDegree(nodedegree, sourcenodeid, targetnodeid, numberofedges);
      determineNodeStrength(nodestrength, sourcenodeid, targetnodeid, edgeweight, numberofedges);
      determineNodeNearestNeighborDegree(nodenearestneighbordegree, nodedegree, nodestrength, numberofnodes,
					 sourcenodeid, targetnodeid, edgeweight, numberofedges);
      writeNodeAttribute(nodenearestneighbordegree, numberofnodes, quantityname, output_type);

      determineExtremumValuesOfIntegerTypeQuantity(&minnodedegree, &maxnodedegree, nodedegree, numberofnodes);
      printf("Minimum node-degree : %d\n", minnodedegree);
      printf("Maximum node-degree : %d\n", maxnodedegree);

      sprintf(quantityname, "degree_averagenearestneighbordegree.linbinned");
      sprintf(output_type, "plot");
      allocateMemoryForGlobalMeasurementLinearBinned(minnodedegree, maxnodedegree);
      determineAverageQuantityWithRespectToDegreeLinearBinned(averagenodenearestneighbordegree, nodedegreedistribution, nodenearestneighbordegree, 
							      minnodedegree, maxnodedegree, nodedegree, numberofnodes);
      writeAverageQuantityWithRespectToDegreeLinearBinned(averagenodenearestneighbordegree, nodedegreedistribution, minnodedegree, maxnodedegree, quantityname, output_type);

      sprintf(quantityname, "degree_averagenearestneighbordegree.logbinned");
      sprintf(output_type, "plot");
      allocateMemoryForGlobalMeasurementLogarithmicBinned(numberofbins);
      determineAverageQuantityWithRespectToDegreeLogarithmicBinned(averagenodenearestneighbordegree_logbinned, nodedegreedistribution_logbinned, newnodedegree_logbinned, 
								   nodenearestneighbordegree, minnodedegree, maxnodedegree, nodedegree, numberofnodes, numberofbins);
      writeAverageQuantityWithRespectToDegreeLogarithmicBinned(averagenodenearestneighbordegree_logbinned, nodedegreedistribution_logbinned, newnodedegree_logbinned, 
							       numberofbins, quantityname, output_type);

      freeMemoryOfGlobalMeasurementLogarithmicBinned();
      freeMemoryOfGlobalMeasurementLinearBinned();
      freeMemoryOfNodeAttribute();
    }
  else
    printf("Error: Insufficient number of inputs\n");
  return 0;
}

void allocateMemoryForNodeAttribute(int nnode)
{
  nodedegree=(int *) calloc(nnode, sizeof(int));
  nodestrength=(double *) calloc(nnode, sizeof(double));
  nodenearestneighbordegree=(double *) calloc(nnode, sizeof(double));
}

void freeMemoryOfNodeAttribute(void)
{
  free(nodedegree);
  free(nodestrength);
  free(nodenearestneighbordegree);
}

void determineNodeDegree(int *d, int *sid, int *tid, int nedge)
{
  int ctre, ctrs, ctrt;

  for(ctre=0; ctre < nedge; ctre++)
    {
      ctrs=sid[ctre];
      ctrt=tid[ctre];
      d[ctrs]++;
      d[ctrt]++;
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

void determineNodeNearestNeighborDegree(double *nnd, int *d, double *s, int nnode, 
					int *sid, int *tid, double *w, int nedge)
{
  int ctre, ctrs, ctrt, ctrn;

  for(ctre=0; ctre < nedge; ctre++)
    {
      ctrs=sid[ctre];
      ctrt=tid[ctre];
      nnd[ctrs]=nnd[ctrs]+w[ctre]*d[ctrt];
      nnd[ctrt]=nnd[ctrt]+w[ctre]*d[ctrs];
    }
  for(ctrn=0; ctrn < nnode; ctrn++)
    if(s[ctrn] > 0.0)
      nnd[ctrn]=nnd[ctrn]/s[ctrn];
}

void writeNodeAttribute(double *s, int nnode, char *quantityname, char *output_type)
{
  char output_filename[NAMELENGTH+1];
  int ctr;
  FILE *fp;

  sprintf(output_filename, "%s.%s", quantityname, output_type);
  fp=fopen(output_filename, "w");
  if(fp==NULL)
    {
      printf("Can not open file %s! ...\n", output_filename);
      exit(1);
    }
  for(ctr=0; ctr < nnode; ctr++)
    fprintf(fp, "%E\n", s[ctr]);
  fclose(fp);
}

void determineExtremumValuesOfIntegerTypeQuantity(int *min, int *max, int *d, int nnode)
{
  int ctr;

  *min=nnode;
  *max=0;
  for(ctr=0; ctr < nnode; ctr++)
    {
      if(d[ctr] > *max) *max=d[ctr];
      if(d[ctr] < *min) *min=d[ctr];
    }
}

void allocateMemoryForGlobalMeasurementLinearBinned(int min, int max)
{
  nodedegreedistribution=(int *) calloc(max-min+1, sizeof(int));
  averagenodenearestneighbordegree=(double *) calloc(max-min+1, sizeof(double));
}

void freeMemoryOfGlobalMeasurementLinearBinned(void)
{
  free(nodedegreedistribution);
  free(averagenodenearestneighbordegree);
}

void determineAverageQuantityWithRespectToDegreeLinearBinned(double *avgs, int *ddist, double *s, int mind, int maxd, int *d, int nnode)
{
  int ctrn, ctrb;

  for(ctrn=0; ctrn < nnode; ctrn++)
    {
      ctrb=d[ctrn]-mind;
      ddist[ctrb]++;
      avgs[ctrb]=avgs[ctrb]+s[ctrn];
    }
  for(ctrb=0; ctrb < (maxd-mind+1); ctrb++)
    if(ddist[ctrb] > 0)
      avgs[ctrb]=avgs[ctrb]/((double)ddist[ctrb]);
}

void writeAverageQuantityWithRespectToDegreeLinearBinned(double *avgs, int *ddist, int min, int max, char *quantityname, char *output_type)
{
  char output_filename[NAMELENGTH+1];
  int ctrb;
  FILE *fp;

  sprintf(output_filename, "%s.%s", quantityname, output_type);
  fp=fopen(output_filename, "w");
  if(fp==NULL)
    {
      printf("Can not open file %s! ...\n", output_filename);
      exit(1);
    }
  fprintf(fp, "# degree\taverage\n");
  for(ctrb=0; ctrb < (max-min+1); ctrb++)
    if(ddist[ctrb] > 0)
      fprintf(fp, "%d\t%E\n", ctrb+min, avgs[ctrb]);
  fclose(fp);
}

void allocateMemoryForGlobalMeasurementLogarithmicBinned(int nbin)
{
  nodedegreedistribution_logbinned=(double *) calloc(nbin, sizeof(double));
  newnodedegree_logbinned=(double *) calloc(nbin, sizeof(double));
  averagenodenearestneighbordegree_logbinned=(double *) calloc(nbin, sizeof(double));
}

void freeMemoryOfGlobalMeasurementLogarithmicBinned(void)
{
  free(nodedegreedistribution_logbinned);
  free(newnodedegree_logbinned);
  free(averagenodenearestneighbordegree_logbinned);
}

void determineAverageQuantityWithRespectToDegreeLogarithmicBinned(double *avgs, double *ddist, double *nd, double *s, int mind, int maxd, int *d, int nnode, int nbin)
{
  int ctrn, ctrb;
  double delta, base;

  delta=(log(maxd)-log(mind))/((double)nbin);
  base=exp(delta);
  printf("Base has been chosen as %lf for logarithmic binning\n", base);
  for(ctrn=0; ctrn < nnode; ctrn++)
    {
      ctrb=(int)((log(d[ctrn])-log(mind))/delta);
      if(ctrb >= nbin)
	ctrb=nbin-1;
      ddist[ctrb]=ddist[ctrb]+1.0;
      avgs[ctrb]=avgs[ctrb]+s[ctrn];
    }
  for(ctrb=0; ctrb < nbin; ctrb++)
    {
      //nd[ctrb]=mind*pow(base, ctrb); //Minimum value in bin ctrb
      nd[ctrb]=mind*pow(base, ((double)ctrb+0.5)); //Center of bins ctrb and ctrb+1
      avgs[ctrb]=avgs[ctrb]/ddist[ctrb];
      ddist[ctrb]=ddist[ctrb]/(mind*(pow(base, ctrb+1)-pow(base, ctrb)));
      ddist[ctrb]=ddist[ctrb]/((double)nnode);
    }
}

void writeAverageQuantityWithRespectToDegreeLogarithmicBinned(double *avgs, double *ddist, double *nd, int nbin, char *quantityname, char *output_type)
{
  char output_filename[NAMELENGTH+1];
  int ctrb;
  FILE *fp;

  sprintf(output_filename, "%s.%s", quantityname, output_type);
  fp=fopen(output_filename, "w");
  if(fp==NULL)
    {
      printf("Can not open file %s! ...\n", output_filename);
      exit(1);
    }
  fprintf(fp, "# center_of_bin\taverage\n");
  for(ctrb=0; ctrb < nbin; ctrb++)
    if(ddist[ctrb] > 0.0)
      fprintf(fp, "%E\t%E\n", nd[ctrb], avgs[ctrb]);
  fclose(fp);
}

void readEdgeWeightData(char *input_filename)
{
  int ctr;
  FILE *fp;

  fp=fopen(input_filename, "r");
  if(fp==NULL)
    {
      printf("Can not open file %s! ...\n", input_filename);
      exit(1);
    }
  fscanf(fp, "%d %d", &numberofnodes, &numberofedges);
  printf("Number of nodes : %d\n", numberofnodes);
  printf("Number of edges : %d\n", numberofedges);
  sourcenodeid=(int *) calloc(numberofedges, sizeof(int));
  targetnodeid=(int *) calloc(numberofedges, sizeof(int));
  edgeweight=(double *) calloc(numberofedges, sizeof(double));
  for(ctr=0; ctr < numberofedges; ctr++)
    fscanf(fp, "%d %d %lf", &sourcenodeid[ctr], &targetnodeid[ctr], &edgeweight[ctr]);
  fclose(fp);
}

