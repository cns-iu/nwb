#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<math.h>

#define NAMELENGTH 1000000

int numberofnodes;
int numberofedges;
int *sourcenodeid;
int *targetnodeid;
double *edgeweight;
int **nodeneighborid;
int **nodeneighboredgeid;
int *nodedegree;
int minnodedegree, maxnodedegree;
double *nodestrength;
double *nodeclusteringcoefficient;
int *nodedegreedistribution;
double *averagenodeclusteringcoefficient;
double *newnodedegree_logbinned;
double *nodedegreedistribution_logbinned;
double *averagenodeclusteringcoefficient_logbinned;

void readEdgeWeightData(char *);
void allocateMemoryForNodeAttribute(int);
void determineNodeDegree(int *, int *, int *, int);
void determineNodeStrength(double *, int *, int *, double *, int);
void allocateMemoryForConnectivity(int *, int);
void constructConnectivity(int **, int **, int *, int, int *, int *, int);
void determineNodeClusteringCoefficient(double *, int **, int **, int *, double *, int, double *);
int determineConnectivity(int, int **, int *, int);
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
void freeMemoryOfConnectivity(int);
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

      sprintf(quantityname, "clusteringcoefficient");
      sprintf(output_type, "nodes");
      allocateMemoryForNodeAttribute(numberofnodes);
      determineNodeDegree(nodedegree, sourcenodeid, targetnodeid, numberofedges);
      determineNodeStrength(nodestrength, sourcenodeid, targetnodeid, edgeweight, numberofedges);
      allocateMemoryForConnectivity(nodedegree, numberofnodes);
      constructConnectivity(nodeneighborid, nodeneighboredgeid, nodedegree, numberofnodes, sourcenodeid, targetnodeid, numberofedges);
      determineNodeClusteringCoefficient(nodeclusteringcoefficient, nodeneighborid, nodeneighboredgeid, nodedegree, nodestrength, numberofnodes, edgeweight);
      writeNodeAttribute(nodeclusteringcoefficient, numberofnodes, quantityname, output_type);

      determineExtremumValuesOfIntegerTypeQuantity(&minnodedegree, &maxnodedegree, nodedegree, numberofnodes);
      printf("Minimum node-degree : %d\n", minnodedegree);
      printf("Maximum node-degree : %d\n", maxnodedegree);

      sprintf(quantityname, "degree_averageclusteringcoefficient.linbinned");
      sprintf(output_type, "plot");
      allocateMemoryForGlobalMeasurementLinearBinned(minnodedegree, maxnodedegree);
      determineAverageQuantityWithRespectToDegreeLinearBinned(averagenodeclusteringcoefficient, nodedegreedistribution, nodeclusteringcoefficient, 
							      minnodedegree, maxnodedegree, nodedegree, numberofnodes);
      writeAverageQuantityWithRespectToDegreeLinearBinned(averagenodeclusteringcoefficient, nodedegreedistribution, minnodedegree, maxnodedegree, quantityname, output_type);

      sprintf(quantityname, "degree_averageclusteringcoefficient.logbinned");
      sprintf(output_type, "plot");
      allocateMemoryForGlobalMeasurementLogarithmicBinned(numberofbins);
      determineAverageQuantityWithRespectToDegreeLogarithmicBinned(averagenodeclusteringcoefficient_logbinned, nodedegreedistribution_logbinned, newnodedegree_logbinned, 
								   nodeclusteringcoefficient, minnodedegree, maxnodedegree, nodedegree, numberofnodes, numberofbins);
      writeAverageQuantityWithRespectToDegreeLogarithmicBinned(averagenodeclusteringcoefficient_logbinned, nodedegreedistribution_logbinned, newnodedegree_logbinned, 
 							       numberofbins, quantityname, output_type);

      freeMemoryOfGlobalMeasurementLogarithmicBinned();
      freeMemoryOfGlobalMeasurementLinearBinned();
      freeMemoryOfConnectivity(numberofnodes);
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
  nodeclusteringcoefficient=(double *) calloc(nnode, sizeof(double));
}

void freeMemoryOfNodeAttribute(void)
{
  free(nodedegree);
  free(nodestrength);
  free(nodeclusteringcoefficient);
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

void allocateMemoryForConnectivity(int *d, int nnode)
{
  int ctrn;

  nodeneighborid=(int **) calloc(nnode, sizeof(int *));
  nodeneighboredgeid=(int **) calloc(nnode, sizeof(int *));
  for(ctrn=0; ctrn < nnode; ctrn++)
    {
      nodeneighborid[ctrn]=(int *) calloc(d[ctrn], sizeof(int));
      nodeneighboredgeid[ctrn]=(int *) calloc(d[ctrn], sizeof(int));
    }
}

void freeMemoryOfConnectivity(int nnode)
{
  int ctrn;

  for(ctrn=0; ctrn < nnode; ctrn++)
    {
      free(nodeneighborid[ctrn]);
      free(nodeneighboredgeid[ctrn]);
    }
  free(nodeneighborid);
  free(nodeneighboredgeid);
}

void constructConnectivity(int **nid, int **neid, int *d, int nnode, int *sid, int *tid, int nedge)
{
  int ctrn, ctre, ctrs, ctrt;

  for(ctrn=0; ctrn < nnode; ctrn++)
    d[ctrn]=0;
  for(ctre=0; ctre < nedge; ctre++)
    {
      ctrs=sid[ctre];
      ctrt=tid[ctre];
      nid[ctrs][d[ctrs]]=ctrt;
      neid[ctrs][d[ctrs]]=ctre;
      d[ctrs]++;
      nid[ctrt][d[ctrt]]=ctrs;
      neid[ctrt][d[ctrt]]=ctre;
      d[ctrt]++;
    }
}

void determineNodeClusteringCoefficient(double *cc, int **nid, int **neid, int *d, double *s, int nnode, double *w)
{
  int ctrn, n1, n2;
  int id1, id2, eid1, eid2;
  int connectivity;
  double avgcc=0.0;

  for(ctrn=0; ctrn < nnode; ctrn++)
    {
      if(d[ctrn] > 1)
	{
	  for(n1=0; n1 < (d[ctrn]-1); n1++)
	    {
	      id1=nid[ctrn][n1];
	      eid1=neid[ctrn][n1];
	      for(n2=n1+1; n2 < d[ctrn]; n2++)
		{
		  id2=nid[ctrn][n2];
		  eid2=neid[ctrn][n2];
		  connectivity=determineConnectivity(id2, nid, d, id1);
		  cc[ctrn]=cc[ctrn]+(w[eid1]+w[eid2])*connectivity;
		}
	    }
	  cc[ctrn]=cc[ctrn]/s[ctrn];
	  cc[ctrn]=cc[ctrn]/((double)(d[ctrn]-1));
	  avgcc=avgcc+cc[ctrn];
	}
    }
  avgcc=avgcc/((double)nnode);
  printf("Average weighted clustering coefficient : %E\n", avgcc);
}

int determineConnectivity(int id2, int **nid, int *d, int id1)
{
  int ctr;

  for(ctr=0; ctr < d[id1]; ctr++)
    if(nid[id1][ctr]==id2)
      return 1;
  return 0;
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
  averagenodeclusteringcoefficient=(double *) calloc(max-min+1, sizeof(double));
}

void freeMemoryOfGlobalMeasurementLinearBinned(void)
{
  free(nodedegreedistribution);
  free(averagenodeclusteringcoefficient);
}

void determineAverageQuantityWithRespectToDegreeLinearBinned(double *avgs, int *ddist, double *s, int mind, int maxd, int *d, int nnode)
{
  int ctrn, ctrb;

  for(ctrn=0; ctrn < nnode; ctrn++)
    {
      if(d[ctrn] > 1) //Special to clustering coefficient calculation
	{
	  ctrb=d[ctrn]-mind;
	  ddist[ctrb]++;
	  avgs[ctrb]=avgs[ctrb]+s[ctrn];
	}
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
  averagenodeclusteringcoefficient_logbinned=(double *) calloc(nbin, sizeof(double));
}

void freeMemoryOfGlobalMeasurementLogarithmicBinned(void)
{
  free(nodedegreedistribution_logbinned);
  free(newnodedegree_logbinned);
  free(averagenodeclusteringcoefficient_logbinned);
}

void determineAverageQuantityWithRespectToDegreeLogarithmicBinned(double *avgs, double *ddist, double *nd, double *s, int mind, int maxd, int *d, int nnode, int nbin)
{
  int ctrn, ctrb;
  double delta, base;

  if(mind < 2) mind=2; //Special to clustering coefficient calculation
  delta=(log(maxd)-log(mind))/((double)nbin);
  base=exp(delta);
  printf("Base has been chosen as %lf for logarithmic binning\n", base);
  for(ctrn=0; ctrn < nnode; ctrn++)
    {
      if(d[ctrn] > 1) //Special to clustering coefficient calculation
	{
	  ctrb=(int)((log(d[ctrn])-log(mind))/delta);
	  if(ctrb >= nbin)
	    ctrb=nbin-1;
	  ddist[ctrb]=ddist[ctrb]+1.0;
	  avgs[ctrb]=avgs[ctrb]+s[ctrn];
	}
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
