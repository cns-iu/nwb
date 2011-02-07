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
double *edgedegree;
double minedgedegree, maxedgedegree;
double *newedgedegree;
double *edgedegreedistribution;
double *averageedgeweight;
double *newedgedegree_logbinned;
double *edgedegreedistribution_logbinned;
double *averageedgeweight_logbinned;

void readEdgeWeightData(char *);
void allocateMemoryForNodeAttribute(int);
void allocateMemoryForEdgeAttribute(int);
void determineNodeDegree(int *, int *, int *, int);
void determineEdgeDegree(double *, int *, int *, int, int *);
void writeEdgeAttribute(double *, int, char *, char *);
void determineExtremumValuesOfDoubleTypeQuantity(double *, double *, double *, int);
void allocateMemoryForGlobalMeasurementLinearBinned(int);
void determineAverageQuantityWithRespectToDegreeLinearBinned(double *, double *, double *, double *, double, double, double *, int, int);
void allocateMemoryForGlobalMeasurementLogarithmicBinned(int);
void determineAverageQuantityWithRespectToDegreeLogarithmicBinned(double *, double *, double *, double *, double, double, double *, int, int);
void writeAverageQuantityBinned(double *, double *, double *, int, char *, char *);
void freeMemoryOfGlobalMeasurementLogarithmicBinned(void);
void freeMemoryOfGlobalMeasurementLinearBinned(void);
void freeMemoryOfEdgeAttribute(void);
void freeMemoryOfNodeAttribute(void);

int main(int argc, char **argv)
{
  char input_filename[NAMELENGTH+1];
  char quantityname[NAMELENGTH+1];
  char output_type[NAMELENGTH+1];
  int numberofbins, numberofbins_logbinned;

  if(argc > 3)
    {
      sprintf(input_filename, "%s", argv[1]);
      sscanf(argv[2], "%d", &numberofbins);
      sscanf(argv[3], "%d", &numberofbins_logbinned);
      printf("Number of bins for linear binning : %d\n", numberofbins);
      printf("Number of bins for logarithmic binning : %d\n", numberofbins_logbinned);

      // Read the edges in (source node, target node, weight)
      readEdgeWeightData(input_filename);

      // Allocate the memory for the node degree (node) attribute.
      allocateMemoryForNodeAttribute(numberofnodes);
      // sourcenodeid and targetnodeid are of size numberofedges
      determineNodeDegree(nodedegree, sourcenodeid, targetnodeid, numberofedges);

      sprintf(quantityname, "endpointdegree");
      sprintf(output_type, "edges");
      // Allocate the memory for the degree (edge) attribute.
      allocateMemoryForEdgeAttribute(numberofedges);
      // Calculate the edge degree (degree(edge[s, t]) = degree(node[s]) * degree(node[t]))
      determineEdgeDegree(edgedegree, sourcenodeid, targetnodeid, numberofedges, nodedegree);
      // Write the edge degree to the output file (quantityname.output_type)
      writeEdgeAttribute(edgedegree, numberofedges, quantityname, output_type);

      determineExtremumValuesOfDoubleTypeQuantity(&minedgedegree, &maxedgedegree, edgedegree, numberofedges);
      printf("Minimum edge-degree : %.0lf\n", minedgedegree);
      printf("Maximum edge-degree : %.0lf\n", maxedgedegree);

      sprintf(quantityname, "endpointdegree_averageweight.linbinned");
      sprintf(output_type, "plot");
      allocateMemoryForGlobalMeasurementLinearBinned(numberofbins);
      determineAverageQuantityWithRespectToDegreeLinearBinned(averageedgeweight, edgedegreedistribution, newedgedegree,
								   edgeweight, minedgedegree, maxedgedegree, edgedegree, numberofedges, numberofbins);
      writeAverageQuantityBinned(averageedgeweight, edgedegreedistribution, newedgedegree,
							  numberofbins, quantityname, output_type);
      sprintf(quantityname, "endpointdegree_averageweight.logbinned");
      sprintf(output_type, "plot");
      allocateMemoryForGlobalMeasurementLogarithmicBinned(numberofbins_logbinned);
      determineAverageQuantityWithRespectToDegreeLogarithmicBinned(averageedgeweight_logbinned, edgedegreedistribution_logbinned, newedgedegree_logbinned,
								   edgeweight, minedgedegree, maxedgedegree, edgedegree, numberofedges, numberofbins_logbinned);
      writeAverageQuantityBinned(averageedgeweight_logbinned, edgedegreedistribution_logbinned, newedgedegree_logbinned,
							       numberofbins_logbinned, quantityname, output_type);

      freeMemoryOfGlobalMeasurementLogarithmicBinned();
      freeMemoryOfGlobalMeasurementLinearBinned();
      freeMemoryOfEdgeAttribute();
      freeMemoryOfNodeAttribute();
    }
  else
    printf("Error: Insufficient number of inputs\n");
  return 0;
}

void allocateMemoryForNodeAttribute(int nnode)
{
  nodedegree=(int *) calloc(nnode, sizeof(int));
}

void freeMemoryOfNodeAttribute(void)
{
  free(nodedegree);
}

void allocateMemoryForEdgeAttribute(int nedge)
{
  edgedegree=(double *) calloc(nedge, sizeof(double));
}

void freeMemoryOfEdgeAttribute(void)
{
  free(edgedegree);
}

void determineNodeDegree(int *d, int *sid, int *tid, int nedge)
{
	/* d is nodedegree
	 * sid is sourcenodeid
	 * tid is targetnodeid
	 * nedge is numberofedges
	 */

	/* ctre is the loop counter
	 */
  int ctre, ctrs, ctrt;

  for(ctre=0; ctre < nedge; ctre++)
    {
      ctrs=sid[ctre];	// Get the current source node id.
      ctrt=tid[ctre];	// Get the current target node id.
      d[ctrs]++;	// Increase the degree count for the source node.
      d[ctrt]++;	// Increase the degree count for the target node.
    }
}

void determineEdgeDegree(double *ed, int *sid, int *tid, int nedge, int *nd)
{
	/* ed is edgedegree
	 * sid is sourcenodeid
	 * tid is targetnodeid
	 * nedge is numberofedges
	 * nd is nodedegree
	 */
  int ctre, ctrs, ctrt;

  for(ctre=0; ctre < nedge; ctre++)
    {
      ctrs=sid[ctre];	// Get the current source node id
      ctrt=tid[ctre];	// Get the current target node id
      ed[ctre]=((double)nd[ctrs])*((double)nd[ctrt]);
    }
}

void writeEdgeAttribute(double *s, int nnode, char *quantityname, char *output_type)
{
	/* s is edgedegree
	 * nnode is numberofedges
	 * quantityname is quantityname ("endpointdegree")
	 * output_type is output_type ("edges")
	 */
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
    fprintf(fp, "%.0lf\n", s[ctr]);
  fclose(fp);
}

void determineExtremumValuesOfDoubleTypeQuantity(double *min, double *max, double *d, int nnode)
{
  int ctr;

  *min=1.0E+100;
  *max=0.0;
  for(ctr=0; ctr < nnode; ctr++)
    {
      if(d[ctr] > *max) *max=d[ctr];
      if(d[ctr] < *min) *min=d[ctr];
    }
}

void allocateMemoryForGlobalMeasurementLinearBinned(int nbin)
{
  edgedegreedistribution=(double *) calloc(nbin, sizeof(double));
  newedgedegree=(double *) calloc(nbin, sizeof(double));
  averageedgeweight=(double *) calloc(nbin, sizeof(double));
}

void freeMemoryOfGlobalMeasurementLinearBinned(void)
{
  free(edgedegreedistribution);
  free(newedgedegree);
  free(averageedgeweight);
}

void determineAverageQuantityWithRespectToDegreeLinearBinned(double *avgs, double *ddist, double *nd, double *s, double mind, double maxd, double *d, int nnode, int nbin)
{
  int ctrn, ctrb;
  double delta;

  delta=(maxd-mind)/((double)nbin);
  printf("Width of bins has been chosen as %lf for linear binning\n", delta);
  for(ctrn=0; ctrn < nnode; ctrn++)
    {
      ctrb=(int)((d[ctrn]-mind)/delta);
      if(ctrb >= nbin)
	ctrb=nbin-1;
      ddist[ctrb]=ddist[ctrb]+1.0;
      avgs[ctrb]=avgs[ctrb]+s[ctrn];
    }
  for(ctrb=0; ctrb < nbin; ctrb++)
    {
      //nd[ctrb]=mind+delta*ctrb; //Minimum value in bin ctrb
      nd[ctrb]=mind+delta*((double)ctrb+0.5); //Center of bins ctrb and ctrb+1
      avgs[ctrb]=avgs[ctrb]/ddist[ctrb];
      ddist[ctrb]=ddist[ctrb]/delta;
      ddist[ctrb]=ddist[ctrb]/((double)nnode);
    }
}

void allocateMemoryForGlobalMeasurementLogarithmicBinned(int nbin)
{
  edgedegreedistribution_logbinned=(double *) calloc(nbin, sizeof(double));
  newedgedegree_logbinned=(double *) calloc(nbin, sizeof(double));
  averageedgeweight_logbinned=(double *) calloc(nbin, sizeof(double));
}

void freeMemoryOfGlobalMeasurementLogarithmicBinned(void)
{
  free(edgedegreedistribution_logbinned);
  free(newedgedegree_logbinned);
  free(averageedgeweight_logbinned);
}

void determineAverageQuantityWithRespectToDegreeLogarithmicBinned(double *avgs, double *ddist, double *nd, double *s, double mind, double maxd, double *d, int nnode, int nbin)
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

void writeAverageQuantityBinned(double *avgs, double *ddist, double *nd, int nbin, char *quantityname, char *output_type)
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

