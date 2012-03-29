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
double *nodestrength;

void readEdgeWeightData(char *);
void allocateMemoryForNodeAttribute(int);
void determineNodeDegree(int *, int *, int *, int);
void writeIntegerTypeNodeAttribute(int *, int, char *, char *);
void determineNodeStrength(double *, int *, int *, double *, int);
void writeDoubleTypeNodeAttribute(double *, int, char *, char *);
void freeMemoryOfNodeAttribute(void);

int main(int argc, char **argv)
{
  char input_filename[NAMELENGTH+1];
  char quantityname[NAMELENGTH+1];
  char output_type[NAMELENGTH+1];

  if(argc > 1)
    {
      sprintf(input_filename, "%s", argv[1]);

      // Read the edges (sourcenodeid, targetnodeid, edgeweight).
      readEdgeWeightData(input_filename);

      // Allocate memory for nodedegree and nodestrength.
      allocateMemoryForNodeAttribute(numberofnodes);

      sprintf(quantityname, "degree");
      sprintf(output_type, "nodes");

      // Determine the total node degree.
      determineNodeDegree(nodedegree, sourcenodeid, targetnodeid, numberofedges);
      //
      writeIntegerTypeNodeAttribute(nodedegree, numberofnodes, quantityname, output_type);

      sprintf(quantityname, "strength");
      sprintf(output_type, "nodes");
      determineNodeStrength(nodestrength, sourcenodeid, targetnodeid, edgeweight, numberofedges);
      writeDoubleTypeNodeAttribute(nodestrength, numberofnodes, quantityname, output_type);

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
}

void freeMemoryOfNodeAttribute(void)
{
  free(nodedegree);
  free(nodestrength);
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

void writeIntegerTypeNodeAttribute(int *d, int nnode, char *quantityname, char *output_type)
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
    fprintf(fp, "%d\n", d[ctr]);
  fclose(fp);
}

void determineNodeStrength(double *s, int *sid, int *tid, double *w, int nedge)
{
	/* s is nodestrength
	 * sid is sourcenodeid
	 * tid is targetnodeid
	 * w is edgeweight
	 * nedge is numberofedges
	 */
  int ctre, ctrs, ctrt;

  for(ctre=0; ctre < nedge; ctre++)
    {
      ctrs=sid[ctre];	// Get the current source node id.
      ctrt=tid[ctre];	// Get the current target node id.
      s[ctrs]=s[ctrs]+w[ctre];	// strength(node[s]) = sum(weight(edges[s]))
      s[ctrt]=s[ctrt]+w[ctre];	// strength(node[t]) = sum(weight(edges[t]))
    }
}

void writeDoubleTypeNodeAttribute(double *s, int nnode, char *quantityname, char *output_type)
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
