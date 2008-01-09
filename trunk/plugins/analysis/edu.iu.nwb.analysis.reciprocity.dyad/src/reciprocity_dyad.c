#include<stdio.h>
#include<stdlib.h>
#include <string.h>

#define LINELENGTH 1000
#define NAMELENGTH 100
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
int **adjacencymatrix;

int numberofselfloops;
int numberofpairs, reciprocity;

void determineReciprocityByDyadMethod(int **);

void constructAdjacencyMatrix();
int determineInverseNodeCounter(int);
void eliminateSelfLoops(int **);

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
      sscanf(argv[1], "%s", input_filename);
      scanInputFile(input_filename);
      readNodeSection(input_filename);
      readEdgeSection(input_filename);
      constructAdjacencyMatrix();
      eliminateSelfLoops(adjacencymatrix);
      determineReciprocityByDyadMethod(adjacencymatrix);
    }
  else
    printf("Error: No file has been specified\n");
  return 0;
}

void determineReciprocityByDyadMethod(int **w)
{
  int i, j;
  double prevalence;
  
  numberofpairs=0;
  reciprocity=0;
  for(i=0; i < numberofnodes-1; i++)
    for(j=i+1; j < numberofnodes; j++)
      if(w[i][j]+w[j][i] > 0)
	{
	  numberofpairs++;
	  reciprocity=reciprocity+w[i][j]*w[j][i];
	}
  printf("Number of pairs: %d\n", numberofpairs);
  printf("Number of reciprocated ties: %d\n", reciprocity);
  prevalence=(double)reciprocity/(double)numberofpairs;
  printf("Reciprocity based on dyad method: %lf\n", prevalence);
}

void eliminateSelfLoops(int **w)
{
  int i;
  
  numberofselfloops=0;
  for(i=0; i < numberofnodes; i++)
    {
      numberofselfloops=numberofselfloops+w[i][i];
      w[i][i]=0;
    }
  printf("Number of self-loops: %d\n", numberofselfloops);
  if(numberofselfloops > 0)
    printf("Self-loops are going to be ignored during calculations\n");
}

void constructAdjacencyMatrix()
{
  int snctr, tnctr, ectr;

  adjacencymatrix=(int **) calloc(numberofnodes, sizeof(int *));
  for(snctr=0; snctr < numberofnodes; snctr++)
    adjacencymatrix[snctr]=(int *) calloc(numberofnodes, sizeof(int));
  for(ectr=0; ectr < numberofedges; ectr++)
    {
      snctr=determineInverseNodeCounter(sourcenodeid[ectr]);
      tnctr=determineInverseNodeCounter(targetnodeid[ectr]);
      adjacencymatrix[snctr][tnctr]=1;
    }
}

int determineInverseNodeCounter(int id)
{
  int ctr;

  for(ctr=0; ctr < numberofnodes; ctr++)
    if(nodeid[ctr]==id)
      return ctr;
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
	  sscanf(line, "%s", beginingofline);
	  while(strcmp(beginingofline, headerdirectededge)!=0 && strcmp(beginingofline, headerundirectededge)!=0 & !endoffile)
	    {
	      if(strncmp(beginingofline, comment, 1)!=0 && strspn(line, "1234567890") > 0)
		numberofnodes++;
	      endoffile=readLine(line, fp); 
	      ctrline++;
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
	  sscanf(line, "%s", beginingofline);
	  while(strcmp(beginingofline, headernode)!=0 & !endoffile)
	    {
	      if(strncmp(beginingofline, comment, 1)!=0 && strspn(line, "1234567890") > 0)
		numberofedges++;
	      endoffile=readLine(line, fp); 
	      ctrline++;
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
	  sscanf(line, "%s", beginingofline);
	  while(strcmp(beginingofline, headerdirectededge)!=0 && strcmp(beginingofline, headerundirectededge)!=0 & !endoffile)
	    {
	      if(strncmp(beginingofline, comment, 1)!=0 && strspn(line, "1234567890") > 0)
		{
		  sscanf(line, "%d", &id);
		  if(searchNodeList(id)==1)
		    {
		      nodeid[numberofnodes]=id;
		      determineNodeLabel(nodelabel, numberofnodes, line);		      
		      numberofnodes++;
		    }
		} 
	      endoffile=readLine(line, fp); 
	      ctrline++;
	      sscanf(line, "%s", beginingofline);	      		
	    }
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
	  sscanf(line, "%s", beginingofline);
	  while(strcmp(beginingofline, headernode)!=0 & !endoffile)
	    {
	      if(strncmp(beginingofline, comment, 1)!=0 && strspn(line, "1234567890") > 0)
		{
		  sscanf(line, "%d %d", &sid, &tid);
		  if(searchNodeList(sid)==1 || searchNodeList(tid)==1)
		    {
		      printf("Error: The nwb file is not properly formatted\n");
		      printf("Not all nodes are listed\n");
		      exit(1);
		    }
		  if(searchEdgeList(sid, tid)==1)
		    {
		      sourcenodeid[numberofedges]=sid;
		      targetnodeid[numberofedges]=tid;
		      numberofedges++;
		    }
		}
	      endoffile=readLine(line, fp); 
	      ctrline++;
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
