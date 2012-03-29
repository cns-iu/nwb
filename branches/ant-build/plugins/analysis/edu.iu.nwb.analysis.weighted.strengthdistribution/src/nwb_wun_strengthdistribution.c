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
double *nodestrength;
double minnodestrength, maxnodestrength;
double *newnodestrength_linearbinned;
double *nodestrengthprobabilitydensity_linearbinned;
double *newnodestrength;
double *nodestrengthprobabilitydensity;

void readEdgeWeightData(char *);
void allocateMemoryForNodeAttribute(int);
void determineNodeStrength(double *, int *, int *, double *, int);
void determineExtremumValuesOfDoubleTypeQuantity(double*, double*, double *, int);
void allocateMemoryForGlobalMeasurementLinearBinned(int);
void determineDistributionOfDoubleTypeQuantityLinearBinned(double *, double *, double, double, double *, int, int);
void allocateMemoryForGlobalMeasurementLogarithmicBinned(int);
void determineDistributionOfDoubleTypeQuantityLogarithmicBinned(double *, double *, double, double, double *, int, int);
void writeDistributionOfDoubleTypeQuantityBinned(double *, double *, int, char *, char *);
void freeMemoryOfGlobalMeasurementLogarithmicBinned(void);
void freeMemoryOfGlobalMeasurementLinearBinned(void);
void freeMemoryOfNodeAttribute(void);

int main(int argc, char **argv)
{
  char input_filename[NAMELENGTH+1];
  char quantityname[NAMELENGTH+1];
  char output_type[NAMELENGTH+1];
  int numberofbinslinear=0, numberofbins=0;

  if(argc > 3)
    {
      sprintf(input_filename, "%s", argv[1]);
      sscanf(argv[2], "%d", &numberofbinslinear);
      sscanf(argv[3], "%d", &numberofbins);
      printf("Number of bins for linear binning : %d\n", numberofbinslinear);
      printf("Number of bins for logarithmic binning : %d\n", numberofbins);

      readEdgeWeightData(input_filename);

      allocateMemoryForNodeAttribute(numberofnodes);
      determineNodeStrength(nodestrength, sourcenodeid, targetnodeid, edgeweight, numberofedges);

      determineExtremumValuesOfDoubleTypeQuantity(&minnodestrength, &maxnodestrength, nodestrength, numberofnodes);
      printf("Minimum node-strength : %lf\n", minnodestrength);
      printf("Maximum node-strength : %lf\n", maxnodestrength);

      sprintf(quantityname, "strengthdistribution.linbinned");
      sprintf(output_type, "plot");
      allocateMemoryForGlobalMeasurementLinearBinned(numberofbinslinear);
      determineDistributionOfDoubleTypeQuantityLinearBinned(nodestrengthprobabilitydensity_linearbinned, newnodestrength_linearbinned, 
							    minnodestrength, maxnodestrength, 
							    nodestrength, numberofnodes, 
							    numberofbinslinear);
      writeDistributionOfDoubleTypeQuantityBinned(nodestrengthprobabilitydensity_linearbinned, newnodestrength_linearbinned, numberofbinslinear, 
						  quantityname, output_type);

      sprintf(quantityname, "strengthdistribution.logbinned");
      sprintf(output_type, "plot");
      allocateMemoryForGlobalMeasurementLogarithmicBinned(numberofbins);
      determineDistributionOfDoubleTypeQuantityLogarithmicBinned(nodestrengthprobabilitydensity, newnodestrength, 
								 minnodestrength, maxnodestrength, 
								 nodestrength, numberofnodes, 
								 numberofbins);
      writeDistributionOfDoubleTypeQuantityBinned(nodestrengthprobabilitydensity, newnodestrength, numberofbins, 
						  quantityname, output_type);

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
  nodestrength=(double *) calloc(nnode, sizeof(double));
}

void freeMemoryOfNodeAttribute(void)
{
  free(nodestrength);
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

void determineExtremumValuesOfDoubleTypeQuantity(double *min, double *max, double *w, int nedge)
{
  int ctr;

  *min=1.0E+100;
  *max=0.0;
  for(ctr=0; ctr < nedge; ctr++)
    {
      if(w[ctr] > *max) *max=w[ctr];
      if(w[ctr] < *min) *min=w[ctr];
    }
}

void allocateMemoryForGlobalMeasurementLinearBinned(int nbin)
{
  newnodestrength_linearbinned=(double *) calloc(nbin, sizeof(double));
  nodestrengthprobabilitydensity_linearbinned=(double *) calloc(nbin, sizeof(double));
}

void freeMemoryOfGlobalMeasurementLinearBinned(void)
{
  free(newnodestrength_linearbinned);
  free(nodestrengthprobabilitydensity_linearbinned);
}

void determineDistributionOfDoubleTypeQuantityLinearBinned(double *edgeweightprobabilitydensity, double *newedgeweight, 
							   double minw, double maxw, double *w, int nedge, int nbin)
{
  int ctre, ctrb;
  double delta;

  delta=(maxw-minw)/((double)nbin);
  printf("Width of bins has been chosen as %lf for linear binning\n", delta);
  for(ctre=0; ctre < nedge; ctre++)
    {
      ctrb=(int)((w[ctre]-minw)/delta);
      if(ctrb >= nbin)
	ctrb=nbin-1;
      edgeweightprobabilitydensity[ctrb]=edgeweightprobabilitydensity[ctrb]+1.0;
    }
  for(ctrb=0; ctrb < nbin; ctrb++)
    {
      //newedgeweight[ctrb]=minw+delta*ctrb; //Minimum value in bin ctrb
      newedgeweight[ctrb]=minw+delta*((double)ctrb+0.5); //Center of bins ctrb and ctrb+1
      edgeweightprobabilitydensity[ctrb]=edgeweightprobabilitydensity[ctrb]/delta;
      edgeweightprobabilitydensity[ctrb]=edgeweightprobabilitydensity[ctrb]/((double)nedge);
    }
}

void allocateMemoryForGlobalMeasurementLogarithmicBinned(int nbin)
{
  newnodestrength=(double *) calloc(nbin, sizeof(double));
  nodestrengthprobabilitydensity=(double *) calloc(nbin, sizeof(double));
}

void freeMemoryOfGlobalMeasurementLogarithmicBinned(void)
{
  free(newnodestrength);
  free(nodestrengthprobabilitydensity);
}

void determineDistributionOfDoubleTypeQuantityLogarithmicBinned(double *edgeweightprobabilitydensity, double *newedgeweight, 
								double minw, double maxw, double *w, int nedge, int nbin)
{
  int ctre, ctrb;
  double delta, base;

  delta=(log(maxw)-log(minw))/((double)nbin);
  base=exp(delta);
  printf("Base has been chosen as %lf for logarithmic binning\n", base);
  for(ctre=0; ctre < nedge; ctre++)
    {
      ctrb=(int)((log(w[ctre])-log(minw))/delta);
      if(ctrb >= nbin)
	ctrb=nbin-1;
      edgeweightprobabilitydensity[ctrb]=edgeweightprobabilitydensity[ctrb]+1.0;
    }
  for(ctrb=0; ctrb < nbin; ctrb++)
    {
      //newedgeweight[ctrb]=minw*pow(base, ctrb); //Minimum value in bin ctrb
      newedgeweight[ctrb]=minw*pow(base, ((double)ctrb+0.5)); //Center of bins ctrb and ctrb+1
      edgeweightprobabilitydensity[ctrb]=edgeweightprobabilitydensity[ctrb]/(minw*(pow(base, ctrb+1)-pow(base, ctrb)));
      edgeweightprobabilitydensity[ctrb]=edgeweightprobabilitydensity[ctrb]/((double)nedge);
    }
}

void writeDistributionOfDoubleTypeQuantityBinned(double *edgeweightprobabilitydensity, double *newedgeweight, int nbin, 
						 char *quantityname, char *output_type)
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
  fprintf(fp, "# center_of_bin\tprobability_density\n");
  for(ctr=0; ctr < nbin; ctr++)
    if(edgeweightprobabilitydensity[ctr] > 0.0)
      fprintf(fp, "%E\t%E\n", newedgeweight[ctr], edgeweightprobabilitydensity[ctr]);
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
