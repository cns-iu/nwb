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
long seedirn;

void readEdgeWeightData(char *);
void initializeSeedOfRandomNumberGenerator(long *, unsigned int);
void randomizeWeightDistribution(double *, int, double, long *);
int integerRandomNumber(int, long *);
double ran2IntegerRandomNumber(long *);
void writeEdgeAttribute(double *, int, char *, char *);

int main(int argc, char **argv)
{
  char input_filename[NAMELENGTH+1];
  char quantityname[NAMELENGTH+1];
  char output_type[NAMELENGTH+1];
  unsigned int seedrand;
  double factorofshuffling;

  if(argc > 3)
    {
      sprintf(input_filename, "%s", argv[1]);
      sscanf(argv[2], "%lf", &factorofshuffling);
      sscanf(argv[3], "%d", &seedrand);
      printf("Factor of pair-selection : %lf\n", factorofshuffling);
      printf("Seed of random number generator : %d\n", seedrand);

      readEdgeWeightData(input_filename);

      initializeSeedOfRandomNumberGenerator(&seedirn, seedrand);

      randomizeWeightDistribution(edgeweight, numberofedges, factorofshuffling, &seedirn);

      sprintf(quantityname, "randomweight");
      sprintf(output_type, "edges");
      writeEdgeAttribute(edgeweight, numberofedges, quantityname, output_type);
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

void randomizeWeightDistribution(double *w, int nedge, double fshuffle, long *seedirn)
{
  int ctre1, ctre2;
  double nshuffle, ctrs, tmpw;

  nshuffle=ceil((fshuffle*(double)nedge));
  printf("Number of pair-selection : %0.lf\n", nshuffle);
  for(ctrs=0.0; ctrs < nshuffle; ctrs=ctrs+1.0)
    {
      ctre1=integerRandomNumber(nedge, seedirn);
      ctre2=integerRandomNumber(nedge, seedirn);
      if(ctre1!=ctre2)
	{
	  tmpw=w[ctre1];
	  w[ctre1]=w[ctre2];
	  w[ctre2]=tmpw;
	}
    }
}

int integerRandomNumber(int n, long *idum)
{
  int ctr;
  double delta, size, interval;
  double randomprobability;

  size=(double) n;
  delta=1.0/size;
  interval=delta;
  randomprobability=ran2IntegerRandomNumber(idum);
  ctr=0;
  while(randomprobability > interval)
    {
      interval=interval+delta;
      ctr++;
    }
  return ctr;
}

void writeEdgeAttribute(double *s, int nnode, char *quantityname, char *output_type)
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

