/*
Edited by Duygu Balcan
////////////////////////////////////////////////////////////
NWB files are loaded successfully
Only node labels are determined among attributes
Interactions are kept in mtrix format of char type
Self-loops are determined and eliminated for further calculations
////////////////////////////////////////////////////////////
Number of ordered triples (i->j and j->k) is determined
Number of transitive ordered triples (i->j and j->k and i->k) is determined
////////////////////////////////////////////////////////////
*/

#include<stdio.h>
#include<stdlib.h>
#include<string.h>

#define LINELENGTH 10000
#define NAMELENGTH 10000
#define SINGLE 1
#define HEADER 100

char comment[SINGLE+1]="#";
char headernode[HEADER+1]="*Nodes";
char headerdirectededge[HEADER+1]="*DirectedEdges";
char headerundirectededge[HEADER+1]="*UndirectedEdges";

int numberofnodes;
int *nodeid;
char **nodelabel;
int numberofedges;
int *sourcenodeid;
int *targetnodeid;
char **adjacencymatrix;

int *nodeoutdegree;
int *nodeindegree;
int numberofselfloops;
double numberoftriples, transitivity;

void determineTransitivityByAdjacency(char **);
void determineNodeOutDegree(char **);
void determineNodeInDegree(char **);

void determineNumberOfSelfLoops();
void constructAdjacencyMatrix();
void initializeAdjacencyMatrixToZero(char **, int);
int determineInverseNodeCounter(int);
void eliminateSelfLoops(char **);

void scanInputFile(char *);
void readNodeSection(char *);
int searchNodeList(int);
void determineNodeLabel(char **, int, char *);
void readEdgeSection(char *);
int searchEdgeList(int, int);
int readLine(char *, FILE *);

int main(int argc, char **argv)
{
  char input_filename[NAMELENGTH+1];
  
  if(argc > 1)
    {
      sprintf(input_filename, "%s", argv[1]);
      scanInputFile(input_filename);
      readNodeSection(input_filename);
      readEdgeSection(input_filename);
      determineNumberOfSelfLoops();
      constructAdjacencyMatrix();
      eliminateSelfLoops(adjacencymatrix);
      determineNodeOutDegree(adjacencymatrix);
      determineNodeInDegree(adjacencymatrix);
      determineTransitivityByAdjacency(adjacencymatrix);
    }
  else
    printf("Error: No file has been specified\n");
  return 0;
}

void determineTransitivityByAdjacency(char **w)
{
  int i, j, k, degree;
  double prevalence;
  int *outneighbor, *inneighbor;

  if(numberofselfloops > 0)
    printf("Self-loops are going to be ignored during calculations\n");
  numberoftriples=0.0;
  transitivity=0.0;
  for(j=0; j < numberofnodes; j++)
    {
      outneighbor=(int *) calloc(nodeoutdegree[j], sizeof(int));
      inneighbor=(int *) calloc(nodeindegree[j], sizeof(int));
      degree=0;
      for(i=0; i < numberofnodes; i++)
	if(w[j][i])
	  {
	    outneighbor[degree]=i;
	    degree++;
	  }
      degree=0;
      for(k=0; k < numberofnodes; k++)
	if(w[k][j])
	  {
	    inneighbor[degree]=k;
	    degree++;
	  }
      for(i=0; i < nodeindegree[j]; i++)
	for(k=0; k < nodeoutdegree[j]; k++)
	  if(inneighbor[i]!=outneighbor[k])
	    {
	      numberoftriples=numberoftriples+1.0;
	      transitivity=transitivity+(double)w[inneighbor[i]][outneighbor[k]];
	    }
      free(outneighbor);
      free(inneighbor);
    }
  printf("Number of ordered triples (i->j and j->k): %.0lf\n", numberoftriples);
  printf("Number of transitive ordered triples (i->j and j->k and i->k): %.0lf\n", transitivity);
  if(numberoftriples > 0.0)
    {
      prevalence=transitivity/numberoftriples;
      printf("Transitivity (ratio): %lf\n", prevalence);
    }
}

void determineNodeOutDegree(char **w)
{
  int i, j;

  nodeoutdegree=(int *) calloc(numberofnodes, sizeof(int));
  for(i=0; i < numberofnodes; i++)
    for(j=0; j < numberofnodes; j++)
      nodeoutdegree[i]=nodeoutdegree[i]+w[i][j];
}

void determineNodeInDegree(char **w)
{
  int i, j;

  nodeindegree=(int *) calloc(numberofnodes, sizeof(int));
  for(i=0; i < numberofnodes; i++)
    for(j=0; j < numberofnodes; j++)
      nodeindegree[i]=nodeindegree[i]+w[j][i];
}

void eliminateSelfLoops(char **w)
{
  int i;

  for(i=0; i < numberofnodes; i++)
    w[i][i]=0;
}

void constructAdjacencyMatrix()
{
  int snctr, tnctr, ectr;

  adjacencymatrix=(char **) calloc(numberofnodes, sizeof(char *));
  for(snctr=0; snctr < numberofnodes; snctr++)
    adjacencymatrix[snctr]=(char *) calloc(numberofnodes, sizeof(char));
  initializeAdjacencyMatrixToZero(adjacencymatrix, numberofnodes);
  for(ectr=0; ectr < numberofedges; ectr++)
    {
      snctr=determineInverseNodeCounter(sourcenodeid[ectr]);
      tnctr=determineInverseNodeCounter(targetnodeid[ectr]);
      adjacencymatrix[snctr][tnctr]=1;
    }
}

void initializeAdjacencyMatrixToZero(char **w, int size)
{
  int i, j;

  for(i=0; i < size; i++)
    for(j=0; j < size; j++)
      w[i][j]=0;
}

int determineInverseNodeCounter(int id)
{
  int ctr;

  for(ctr=0; ctr < numberofnodes; ctr++)
    if(nodeid[ctr]==id)
      return ctr;
}

void determineNumberOfSelfLoops()
{
  int ctr;
  
  numberofselfloops=0;
  for(ctr=0; ctr < numberofedges; ctr++)
    if(sourcenodeid[ctr]-targetnodeid[ctr]==0)
      numberofselfloops++;
  printf("Number of self loops: %d\n", numberofselfloops);
}

void scanInputFile(char *input_filename)
{
  char line[LINELENGTH+1];
  char beginingofline[LINELENGTH+1];
  int ctrline, endoffile;
  FILE *fp;
  
  fp=fopen(input_filename, "r");
  if(fp==NULL)
    {
      printf("Can not open file %s! ...\n", input_filename);
      exit(1);
    }
  ctrline=0;
  while(!readLine(line, fp))
    {
      ctrline++;
      sscanf(line, "%s", beginingofline);
      while(strncmp(beginingofline,comment,1)==0)
	{
	  readLine(line, fp);
	  ctrline++;
	  sscanf(line, "%s", beginingofline);
	}
      if(strcmp(beginingofline,headernode)==0)
	{
	  numberofnodes=0;
	  readLine(line, fp); //Skip node attribute names
	  ctrline++;
	  endoffile=readLine(line, fp);
	  ctrline++;
	  sprintf(beginingofline, "\0");
	  sscanf(line, "%s", beginingofline);
	  while(strcmp(beginingofline, headerdirectededge)!=0 && strcmp(beginingofline, headerundirectededge)!=0 & !endoffile)
	    {
	      if(strlen(beginingofline) > 0 && strncmp(beginingofline, comment, 1)!=0)
		numberofnodes++;
	      endoffile=readLine(line, fp); 
	      ctrline++;
	      sprintf(beginingofline, "\0");
	      sscanf(line, "%s", beginingofline);	      
	    }
	}
      if(strcmp(beginingofline,headerdirectededge)==0 || strcmp(beginingofline,headerundirectededge)==0)
	{
	  if(strcmp(beginingofline,headerundirectededge)==0)
	    {
	      printf("Error: The algorithm should be applied on directed networks\n");
	      exit(1);
	    }
	  numberofedges=0;
	  readLine(line, fp); //Skip edge attribute names
	  ctrline++;
	  endoffile=readLine(line, fp);
	  ctrline++;
	  sprintf(beginingofline, "\0");
	  sscanf(line, "%s", beginingofline);
	  while(strcmp(beginingofline, headernode)!=0 & !endoffile)
	    {
	      if(strlen(beginingofline) > 0 && strncmp(beginingofline, comment, 1)!=0)
		numberofedges++;
	      endoffile=readLine(line, fp); 
	      ctrline++;
	      sprintf(beginingofline, "\0");
	      sscanf(line, "%s", beginingofline);
	    }
	}
    }
  fclose(fp);
}

void readNodeSection(char *input_filename)
{
  char line[LINELENGTH+1];
  char beginingofline[LINELENGTH+1];
  int ctrline, endoffile;
  int id;
  FILE *fp;
  
  nodeid=(int *) calloc(numberofnodes, sizeof(int));  
  nodelabel=(char **) calloc(numberofnodes, sizeof(char *));
  for(id=0; id < numberofnodes; id++)
    nodelabel[id]=(char *) calloc(NAMELENGTH+1, sizeof(char));
  fp=fopen(input_filename, "r");
  if(fp==NULL)
    {
      printf("Can not open file %s! ...\n", input_filename);
      exit(1);
    }
  ctrline=0;
  while(!readLine(line, fp))
    {
      ctrline++;
      sscanf(line, "%s", beginingofline);
      while(strncmp(beginingofline,comment,1)==0)
	{
	  readLine(line, fp);
	  ctrline++;
	  sscanf(line, "%s", beginingofline);
	}
      if(strcmp(beginingofline,headernode)==0)
	{
	  numberofnodes=0;
	  readLine(line, fp); //Skip node attribute names
	  ctrline++;
	  endoffile=readLine(line, fp);
	  ctrline++;
	  sprintf(beginingofline, "\0");
	  sscanf(line, "%s", beginingofline);
	  while(strcmp(beginingofline, headerdirectededge)!=0 && strcmp(beginingofline, headerundirectededge)!=0 & !endoffile)
	    {
	      if(strlen(beginingofline) > 0 && strncmp(beginingofline, comment, 1)!=0)
		{
		  sscanf(line, "%d", &id);
		  if(searchNodeList(id))
		    {
		      nodeid[numberofnodes]=id;
		      determineNodeLabel(nodelabel, numberofnodes, line);		      
		      numberofnodes++;
		    }
		} 
	      endoffile=readLine(line, fp); 
	      ctrline++;
	      sprintf(beginingofline, "\0");
	      sscanf(line, "%s", beginingofline);	      		
	    }
	  printf("..........\n", numberofnodes);
	  printf("Number of nodes: %d\n", numberofnodes);
	  break;
	}
    }
  fclose(fp);  
}

int searchNodeList(int id)
{
  int ctr;
  
  for(ctr=0; ctr < numberofnodes; ctr++)
    if(id==nodeid[ctr])
      return 0;
  return 1;
}

void determineNodeLabel(char **nodelabel, int numberofnodes, char *line)
{
  char label[NAMELENGTH+1];
  int ctr_1, ctr_2;
  
  for(ctr_1=0; ctr_1 < strlen(line); ctr_1++)
    if(line[ctr_1]=='\"')
      break;
  if(ctr_1==strlen(line))
    nodelabel[numberofnodes][0]='*';
  else
    {
      nodelabel[numberofnodes][0]='\"';
      ctr_1++;
      for(ctr_2=ctr_1; line[ctr_2]!='\"'; ctr_2++)
	nodelabel[numberofnodes][ctr_2-ctr_1+1]=line[ctr_2];
      nodelabel[numberofnodes][ctr_2-ctr_1+1]=line[ctr_2];
    }
}

void readEdgeSection(char *input_filename)
{
  char line[LINELENGTH+1];
  char beginingofline[LINELENGTH+1];
  int ctrline, endoffile;
  int sid, tid;
  FILE *fp;

  sourcenodeid=(int *) calloc(numberofedges, sizeof(int));
  targetnodeid=(int *) calloc(numberofedges, sizeof(int));
  fp=fopen(input_filename, "r");
  if(fp==NULL)
    {
      printf("Can not open file %s! ...\n", input_filename);
      exit(1);
    }
  ctrline=0;
  while(!readLine(line, fp))
    {
      ctrline++;
      sscanf(line, "%s", beginingofline);
      while(strncmp(beginingofline,comment,1)==0)
	{
	  readLine(line, fp);
	  ctrline++;
	  sscanf(line, "%s", beginingofline);
	}
      if(strcmp(beginingofline,headerdirectededge)==0 || strcmp(beginingofline,headerundirectededge)==0)
	{
	  numberofedges=0;
	  readLine(line, fp); //Skip edge attribute names
	  ctrline++;
	  endoffile=readLine(line, fp);
	  ctrline++;
	  sprintf(beginingofline, "\0");
	  sscanf(line, "%s", beginingofline);
	  while(strcmp(beginingofline, headernode)!=0 & !endoffile)
	    {
	      if(strlen(beginingofline) > 0 && strncmp(beginingofline, comment, 1)!=0)
		{
		  sscanf(line, "%d %d", &sid, &tid);
		  if(searchNodeList(sid)+searchNodeList(tid) > 0)
		    {
		      printf("Error: The nwb file is not properly formatted - Not all nodes are listed\n");
		      exit(1);
		    }
		  if(searchEdgeList(sid, tid))
		    {
		      sourcenodeid[numberofedges]=sid;
		      targetnodeid[numberofedges]=tid;
		      numberofedges++;
		    }
		}
	      endoffile=readLine(line, fp); 
	      ctrline++;
	      sprintf(beginingofline, "\0");
	      sscanf(line, "%s", beginingofline);
	    }
	  printf("Number of edges: %d\n", numberofedges);
	  break;
	}
    }
  fclose(fp);
}

int searchEdgeList(int sid, int tid)
{
  int ctr;
  
  for(ctr=0; ctr < numberofedges; ctr++)
    if(sid==sourcenodeid[ctr] && tid==targetnodeid[ctr])
      return 0;
  return 1;
}

int readLine(char *line, FILE *fp)
{
  
  if(fgets(line, LINELENGTH, fp)==NULL) 
    return 1;
  return 0;
  
}
