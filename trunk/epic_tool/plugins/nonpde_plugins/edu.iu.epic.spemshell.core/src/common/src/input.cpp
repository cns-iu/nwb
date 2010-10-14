/**
 * @file input.cpp
 * Functions to read formatted and commented input files.
 *
 *  Created by Bruno Goncalves on 06/16/05.
 *  Copyright 2005 Bruno Goncalves. All rights reserved.
 *
 */
#include<cstdio>
#include<string>
#include<ctime>
#include<iostream>
#include<fstream>
#include<sstream>
#include<map>
#include<vector>
#include<cmath>
#include<cctype>
#include<input.h>
#include<cstdlib>
#include<defs.h>

using namespace std;


FILE *input_file::gzOpen(string filename,string mode)
{
  string command;
	    
  if(mode=="r")
    command="gunzip -c ";
  else if(mode=="w")
    command="gzip > ";
  else
    {
      fprintf(stderr,"-- Error unknown mode: %s\n",mode.c_str());
      exit(1);
    }
	    		
  FILE *fp=popen((command + filename).c_str(),mode.c_str());

  if(fp==NULL)
    {
      fprintf(stderr,"-- Error opening file %s\n",filename.c_str());
      perror("popen");
      exit(1);
    }
	    
  return fp;
}
	
string input_file::Evaluate(string expr)
{
  vector<string> numbers=Parse(expr);
  return Evaluate(numbers);
}
	
vector<string> input_file::Parse(string expr)
{
  map<char,unsigned> prec;
		
  prec['^']=4;
  prec['*']=3;
  prec['/']=3;
  prec['+']=2;
  prec['-']=2;
  prec['=']=1;
  prec['|']=0;
		
  vector<string> numbers;
  vector<char> opers;
		
  string temp;
		
  char lastOp='|';
  char curOp;
		
  for(unsigned i=0;i<=expr.size();i++)
    {
      if(i!=expr.size() && prec.find(expr[i])==prec.end())
	temp+=expr[i];
      else
	{
	  if(!isalpha(temp[0]))
	    numbers.push_back(temp);
				
	  curOp=expr[i];
				
	  if(prec[curOp]>prec[lastOp])
	    {
	      temp="";
	      opers.push_back(expr[i]);
	    }
	  else
	    {
	      while(prec[curOp]<=prec[lastOp])
		{
		  if(opers.empty())
		    break;
						
		  lastOp=opers[opers.size()-1];
		  opers.pop_back();
						
		  temp=lastOp;
						
		  numbers.push_back(temp);
		}
					
	      opers.push_back(curOp);
	    }
				
	  lastOp=expr[i];
	  temp="";
	}
    }
		
  return numbers;
}
	
string input_file::Evaluate(vector<string> &numbers)
{
  if(numbers.size()==0)
    return "";
  string item=numbers[numbers.size()-1];
  numbers.pop_back();
		
  vector<string> element;
  string n1,n2;
  char buffer[BUFFER_SIZE];
		
  switch(item[0])
    {
    case '*':
      n1=Evaluate(numbers);
      n2=Evaluate(numbers);
				
      sprintf(buffer,"%g",AToDouble(n2)*AToDouble(n1));
      return buffer;
				
    case '/':
      n1=Evaluate(numbers);      
      n2=Evaluate(numbers);
				
      sprintf(buffer,"%g",AToDouble(n2)/AToDouble(n1));
      return buffer;
				
    case '^':
      n1=Evaluate(numbers);      
      n2=Evaluate(numbers);
				
      sprintf(buffer,"%g",pow(AToDouble(n2),AToDouble(n1)));
      return buffer;
				
    case '+':
      n1=Evaluate(numbers);      
      n2=Evaluate(numbers);
				
      sprintf(buffer,"%g",AToDouble(n2)+AToDouble(n1));
      return buffer;
				
    case '-':
      n1=Evaluate(numbers);      
      n2=Evaluate(numbers);
				
      sprintf(buffer,"%g",AToDouble(n2)-AToDouble(n1));
      return buffer;
				
    case '$':
      if(item[1]!='(' || item[item.size()-1]!=')')
	{
	  fprintf(stderr,"-- Error parsing expression: %s\n",item.c_str());
	  exit(1);
	}
				
      string temp;
				
      for(unsigned i=2;i<item.size()-1;i++)
	temp+=item[i];
					
      return Evaluate(vars[temp]);
    }
		
  return item;
}	
	
/**
 * Convert a string to an int
 */
int input_file::AToInt(string temp)
{
  int number = 0;
  istringstream stream(temp);
		
  stream >> number;
		
  return number;
}
	
/**
 * Convert a string to an unsigned int
 */
int input_file::AToUnsigned(string temp)
{
  unsigned number = 0;
  istringstream stream(temp);
		
  stream >> number;
		
  return number;
}
	
/**
 * Convert a string to a double
 */
double input_file::AToDouble(string temp)
{
  double number = 0;
  istringstream stream(temp);
		
  stream >> number;
		
  return number;
}
	
string input_file::pad(string line)
{
  if(line.length()<=78)
    {			
      line.append(78-line.length(),' ');
      line.append("#");
    }
		
  return line;
}
	
string input_file::separator()
{
  string line="";
		
  line.append(79,'#');
		
  return line;
}
	
string input_file::empty_line()
{
  string line="#";
		
  line.append(77,' ');
  line.append("#");
		
  return line;
}
	
void input_file::printUsage(string opt)
{
  fprintf(stderr,"Parameter not defined: %s\n",opt.c_str());
		
  if(usage.size()!=0)
    fprintf(stderr,"%s\n",usage.c_str());

  exit(3);
}
	
map<string,string> input_file::read_input(string filename,string c_char)
{
  string input,key,value;
  ifstream in(filename.c_str());
		
  /* Open the file */
  if(!in.is_open())
    {
      fprintf(stderr,"-- Error opening file: %s\n",filename.c_str());
      exit(9);
    }
		
  /* Read in the file, one line at a time */
  while(!in.eof())
    {
      //Have we reached the end of the file?
      if(getline(in,input)==NULL)
	break;
			
      //Ignore blank lines.
      if(input.size()==0)
	continue;
			
      unsigned int pos=0; 
			
      /**
       * Parse the assignment. Lines can be either in the form
       * X Y
       * or in the form
       * X=Y
       * with any number of white space or equal signs.
       */
      do{
	pos=input.find("=",0);
				
	if(pos<input.length())
	  input.replace(pos,1," ");
				
      }while(pos<input.length());
			
      stringstream ss(input);
			
      ss>>key;
      ss>>value;
			
      if(vars[key].size()==0)
	vars[key]=value;
			
      //If its a comment, just store it
      if(input.substr(0,c_char.length())==c_char)
	{
	  header.push_back(pad(input.c_str()));
	  //Move on to the next line
	  continue;
	}
      else
	{
	  //Comment the line and store it
	  header.push_back(pad(c_char+"& "+key+" "+vars[key]));
	}
			
    }
		
  return vars;
}
	
void input_file::input(string filename,unsigned argc, char **argv, string c_char)
{
  unsigned i=1;
		
  while(i<argc)
    {
      if(argv[i][0]=='-' && (i+1)<argc)
	{
	  vars[&argv[i][1]]=argv[i+1];
	  i++;
	}
      i++;
    };
		
  if(filename!="")
    vars=read_input(filename,c_char);
		
  if(header.size()==0)
    header.push_back(separator());
			
  header.push_back(pad("# Command line options:"));
  header.push_back(empty_line());
		
  string line="# ";
		
  for(unsigned i=0;i<argc;i++)
    {
      line = line + argv[i] + " ";
    }
		
  header.push_back(pad(line));
  header.push_back(empty_line());
  header.push_back(pad("# These options override the input file variables, where applicable"));
  header.push_back(separator());		
}
	
	
input_file::input_file(unsigned argc, char **argv, char **env, string us, string c_char)
{
  if(us=="")
    {
      us="Correct usage:\n\n";
      us+=string(argv[0]);
      us+=" <input_file> [<options>]\n";
    }
		
  usage=us;
		
  if(argc>=2 && argv[1][0]!='-')
    {
      input(argv[1],argc,argv,c_char);
    }
  else
    input("",argc,argv);
		
  if(env!=NULL)
    {
      for(unsigned i=0;env[i]!=NULL;++i)
	{
	  string key;
	  string temp;
				
	  for(unsigned j=0;env[i][j]!='\0';++j)
	    {
	      if(env[i][j]!='=')
		temp+=env[i][j];
	      else
		{
		  key=temp;
		  temp="";
		}
	    }
	  vars[key]=temp;
	}
    }
}
	
	
vector<string> input_file::getOptions()
{
  vector<string> opts;
		
  for(iter=vars.begin();iter!=vars.end();iter++)
    opts.push_back((*iter).first);
		
  return opts;
}
	
/**
 * Read variables from a input file
 * 
 * @param filename The input file to parse
 * @param c_char String that identifies comments in the file
 */
input_file::input_file(string filename,string c_char)
{
  vars=read_input(filename,c_char);
}

	
/**
 * Open a file for writting and optionally write the complete header 
 * to it. The user is responsible for closing the file when s/he 
 * is done with it.
 *
 * @param filename The name of the file to open
 * @param verbose Should the header be writen to the file?
 * @return fp The file pointer that was created.
 */
FILE *input_file::openFile(string filename,bool verbose)
{
  FILE *fp;

  if(filename.find(".gz")!=string::npos)
    fp=gzOpen(filename,"w");
  else
    fp=fopen(filename.c_str(),"w");
	    
  if(fp==NULL)
    {
      fprintf(stderr,"-- Error opening file: %s\n",filename.c_str());
      exit(-1);
    }
		
  if(verbose)
    {
      for(unsigned i=0;i<header.size();i++)
	fprintf(fp,"%s\n",header[i].c_str());
    }
		
  return fp;
}
	
/**
 * Open a file for reading.
 * The user is responsible for closing the file when s/he is done
 * with it.
 *
 * @param filename The name of the file to open
 * @param verbose Should the name of the file being read be echoed out to stdout and stderr?
 * @param c_char The character signaling a comment
 * @return fp The file pointer that was created.
 */
FILE *input_file::readFile(string filename, bool verbose, char c_char)
{
  FILE *fp;
		
  if(filename.find(".gz")!=string::npos)
    fp=gzOpen(filename,"r");
  else
    {
      fp=fopen(filename.c_str(),"r");
			
      if(fp==NULL)
	{
	  fprintf(stderr,"-- Error opening file: %s\n",filename.c_str());
	  exit(-1);
	}
			
    }
		
  char lastc,nextc;
	    
  if(verbose)
    {
      fprintf(stdout,"%s\n",pad(("# Reading "+filename)).c_str());
      fprintf(stderr,"%s\n",pad(("# Reading "+filename)).c_str());
    }
		
  while ((lastc = getc(fp)) == c_char)
    {
			
      if(verbose)
	{
	  fprintf(stdout,"%c",lastc);
	  fprintf(stderr,"%c",lastc);
	}
			
      while ((nextc = getc(fp)) != EOF)
	{
	  if(verbose)
	    {
	      fprintf(stdout,"%c",nextc);
	      fprintf(stderr,"%c",nextc);
	    }
				
	  if(nextc=='\n')
	    break;
	}
    }
		
  ungetc(lastc,fp);
		
  return fp;
}
	
string input_file::getVarFromFile(string filename,string variable)
{
  string input,key,value;
  ifstream in(filename.c_str());
		
  /* Open the file */
  if(!in.is_open())
    {
      fprintf(stderr,"-- Error opening file: %s\n",filename.c_str());
      exit(9);
    }
		
  /* Read in the file, one line at a time */
  while(!in.eof())
    {
      //Have we reached the end of the file?
      if(getline(in,input)==NULL)
	break;
			
      //Ignore blank lines.
      if(input.size()==0)
	continue;
			
      unsigned int pos=0; 
			
      /**
       * Parse the assignment. Lines can be either in the form
       * X Y
       * or in the form
       * X=Y
       * with any number of white space or equal signs.
       */
      do{
	pos=input.find("=",0);
				
	if(pos<input.length())
	  input.replace(pos,1," ");
				
      }while(pos<input.length());
			
      stringstream ss(input);
			
      ss>>key;
			
      if(key=="#&")
	{
	  ss>>key;
				
	  ss>>value;
			
	  if(key==variable)
	    {
	      in.close();
					
	      return value;
	    }
	}
    }
		
  fprintf(stderr,"-- Variable \"%s\" not found in file!\n",variable.c_str());
  exit(1);
		
  return value;
}

	
bool input_file::getBoolOpt(string which)
{
  if(vars.find(which)==vars.end())
    printUsage(which);
		
  string opt=vars[which.c_str()];
  char c=opt[0];
  c=tolower(c);
		
  if(c=='y' || c=='t' || c=='1')
    return true;
		
  if(c=='n' || c=='f' || c=='0')
    return false;
		
  fprintf(stderr,"-- ERROR: Invalid \"boolean\" option: %s\n",opt.c_str());
  exit(1);
}
	
bool input_file::getBoolOpt(string which, bool value)
{
  if(vars.find(which)==vars.end())
    return value;
			
  string opt=vars[which.c_str()];
  char c=opt[0];
  c=tolower(c);
			
  if(c=='y' || c=='t' || c=='1')
    return true;
			
  if(c=='n' || c=='f' || c=='0')
    return false;
			
  fprintf(stderr,"-- ERROR: Invalid \"boolean\" option: %s\n",opt.c_str());
  exit(1);
}
	
/**
 * Return the value of an unsigned integer variable
 * 
 * @param which The variable to access
 * @return The variable's unsigned integer value
 */
unsigned input_file::getUnsignedOpt(string which)
{
  if(vars.find(which)==vars.end())
    printUsage(which);
		
  return AToUnsigned(Evaluate(vars[which.c_str()]));
}	

/**
 * Return the value of an unsigned integer variable
 * 
 * @param which The variable to access
 * @param value The variable's default value.
 * @return The variable's unsigned integer value
 */
unsigned input_file::getUnsignedOpt(string which,unsigned value)
{
  if(vars.find(which)==vars.end())
    return value;
		
  return AToUnsigned(Evaluate(vars[which.c_str()]));
}
	
/**
 * Return the value of an integer variable
 * 
 * @param which The variable to access
 * @return The variable's integer value
 */
int input_file::getIntOpt(string which)
{
  if(vars.find(which)==vars.end())
    printUsage(which);
	
  return AToInt(Evaluate(vars[which.c_str()]));
}

/**
 * Return the value of an integer variable
 * 
 * @param which The variable to access
 * @param value The variable's default value
 * @return The variable's integer value
 */
int input_file::getIntOpt(string which,int value)
{
  if(vars.find(which)==vars.end())
    return value;
	
  return AToInt(Evaluate(vars[which.c_str()]));
}
	
/**
 * Return the value of a double precision variable
 * 
 * @param which The variable to access
 * @return The variable's double precision value
 */
double input_file::getDoubleOpt(string which)
{
  if(vars.find(which)==vars.end())
    printUsage(which);
		
  return AToDouble(Evaluate(vars[which.c_str()]));
}

/**
 * Return the value of a double precision variable
 * 
 * @param which The variable to access.
 * @param value The variable's default value.
 * @return The variable's double precision value.
 */
double input_file::getDoubleOpt(string which, double value)
{
  if(vars.find(which)==vars.end())
    return value;
		
  return AToDouble(Evaluate(vars[which.c_str()]));
}
	
/**
 * Return the value of a string variable
 * 
 * @param which The variable to access
 * @return The variable's string value
 */
string input_file::getStringOpt(string which)
{
  if(vars.find(which)==vars.end())
    printUsage(which);

  return vars[which.c_str()];
}

/**
 * Return the value of a string variable
 * 
 * @param which The variable to access.
 * @param value The variable's default value.
 * @return The variable's string value.
 */
string input_file::getStringOpt(string which, string value)
{
  if(vars.find(which)==vars.end())
    return value;

  return vars[which.c_str()];
}
	
/**
 * Return the value of a string variable
 * 
 * @param which The variable to access.
 * @param value The variable's default value.
 * @return The variable's string value.
 */
string input_file::getStringOpt(string which,char value[])
{
  if(vars.find(which)==vars.end())
    {
      string temp;
			
      temp+=value;
      return temp;
    }
		
  return vars[which.c_str()];
}
	
/**
 * Write the header to a file. The end user is responsible
 * for providing a valid file pointer and closing the file
 * when s/he is done with it.
 *
 * @param fp A pointer to a file with write permissions
 */
void input_file::writeToFile(FILE *fp)
{
  time_t t=time(NULL);
  char token[]={'=','+','-','/','^','$'};
		
  files.push_back(fp);

  if(fp==NULL)
    {
      fprintf(stderr,"# Error opening file for output. Invalid file pointer provided\n");
      return;
    }
		
  string type,key,value;
		
  bool flag;
		
  for(unsigned i=0;i<header.size();i++)
    {
      //fprintf(fp,"%s\n",header[i].c_str());
      flag=true;
			
      stringstream ss(header[i]);
			
      ss>>type;
			
      if(type=="#&")
	{
	  ss>>key;
	  ss>>value;
				
	  for(unsigned j=0;j<6;++j)
	    if(value.find(token[j],0)!=string::npos)
	      {
		flag=false;
		break;
	      }
						
	  if(flag==false)
	    fprintf(fp,"%s\n",(pad(type+" "+key+" "+value+" # "+Evaluate(vars[key]))).c_str());
	}
			
      if(flag==true)
	fprintf(fp,"%s\n",header[i].c_str());
    }
			
			
#ifdef BGDEBUG
  fprintf(fp,"# Debugging flags are ON\n");
#endif
		
  fprintf(fp,"# Started on %s",ctime(&t));
}
	
void input_file::writeToFile(FILE *fp, std::string filename)
{
  string input;
  ifstream in(filename.c_str());
	  
  if(fp==NULL)
    {
      fprintf(stderr,"# Error opening file for output. Invalid file pointer provided\n");
      return;
    }

  /* Open the file */
  if(!in.is_open())
    {
      fprintf(stderr,"-- Error opening file: %s\n",filename.c_str());
      exit(9);
    }
	  
	
  fprintf(fp, "%s", "\n");
  fprintf(fp, "%s", separator().c_str());
  fprintf(fp, "%s", "\n");
  fprintf(fp, "%s", empty_line().c_str());
  fprintf(fp, "%s", "\n");
  fprintf(fp, "%s", (pad("# Contents of " + filename)+"\n").c_str());
  fprintf(fp, "%s", empty_line().c_str());
  fprintf(fp, "%s", "\n");
  fprintf(fp, "%s", separator().c_str());
  fprintf(fp, "%s", "\n");

  /* Read in the file, one line at a time */
  while(!in.eof())
    {
      //Have we reached the end of the file?
      if(getline(in,input)==NULL)
	break;

      //Ignore blank lines
      if(input.size()==0)
	continue;

      if(input[0]=='#')
	fprintf(fp, "%s", (pad(input)+"\n").c_str());
      else
	fprintf(fp, "%s", (pad("#& "+input)+"\n").c_str());
    }

  in.close();
  fprintf(fp, "%s", separator().c_str());
  fprintf(fp, "%s", "\n\n");
}

vector<double> input_file::readDoubleVector(FILE *fp,unsigned size)
{
  if(fp==(FILE *)NULL)
    {
      fprintf(stderr,"-- Invalid file pointer\n");
      exit(1);
    }
		
  vector<double> vec(size,0.0);
		
  size=0;
		
  unsigned i;
  double x;
		
  do{
    fscanf(fp,"%u %lf\n",&i,&x);
			
    if(vec.size()<=i)
      vec.resize(i+1);
			
    if(size<=i)
      size=i+1;

    vec[i]=x;
			
  }while(!feof(fp));
		
  vec.resize(size);
		
  return vec;
}
	
input_file::~input_file()
{
  time_t t=time(NULL);
		
  fprintf(stdout,"# Finished on %s",ctime(&t));
  fprintf(stderr,"# Finished on %s",ctime(&t));
}
