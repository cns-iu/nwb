/**
 * @file input.h
 * Functions to read formatted and commented input files.
 *
 *  Created by Bruno Goncalves on 06/16/05.
 *  Copyright 2005 Bruno Goncalves. All rights reserved.
 *
 */
#ifndef INPUT_H
#define INPUT_H

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

using namespace std;
/**
 * Reads input parameters from a file on disk.
 * Opens an input file, parses it and stores the variables in a 
 * string->string map. The entire contents of the file is commented
 * and stored in a header variable so it can then be dumped in to
 * any number of output files.
 */
class input_file
{
  vector<string> header;
  map<string,string> vars;
  map<string,string>::iterator iter;
  vector<FILE *> files;
  string usage;
	
  FILE *gzOpen(string filename,string mode);

  string Evaluate(string expr);
	
  vector<string> Parse(string expr);
	
  string Evaluate(vector<string> &numbers);
	

    /**
   * Convert a string to an int
   */
  int AToInt(string temp);
	
  /**
   * Convert a string to an unsigned int
   */
  int AToUnsigned(string temp);
	
  /**
   * Convert a string to a double
   */
  double AToDouble(string temp);
	
  string pad(string line);
	
  string separator();
	
  string empty_line();
	
  void printUsage(string opt);
	
  map<string,string> read_input(string filename,string c_char="#");
	
  void input(string filename,unsigned argc, char **argv, string c_char="#");
	
public:
		
  input_file(unsigned argc, char **argv, char **env=NULL, string us="", string c_char="#");
	
  vector<string> getOptions();
	
  /**
   * Read variables from a input file
   * 
   * @param filename The input file to parse
   * @param c_char String that identifies comments in the file
   */
  input_file(string filename,string c_char="#");
	
  /**
   *
   */
  input_file() {	}
	
  /**
   * Return the complete header for external processing
   */
  vector<string> getHeader()
  {
    return header;
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
  FILE *openFile(string filename,bool verbose=true);
	
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
  FILE *readFile(string filename, bool verbose=true, char c_char='#');
	
  string getVarFromFile(string filename,string variable);
	
  /**
   * Directly index the variable map
   *
   * @param which The variable to access
   * @return The value corresponding value
   */
  string operator[](string which)
  {
    return vars[which.c_str()];
  }
	
  bool getBoolOpt(string which);
	
  bool getBoolOpt(string which, bool value);
	
  /**
   * Return the value of an unsigned integer variable
   * 
   * @param which The variable to access
   * @return The variable's unsigned integer value
   */
  unsigned getUnsignedOpt(string which);

  /**
   * Return the value of an unsigned integer variable
   * 
   * @param which The variable to access
   * @param value The variable's default value.
   * @return The variable's unsigned integer value
   */
  unsigned getUnsignedOpt(string which,unsigned value);
	
  /**
   * Return the value of an integer variable
   * 
   * @param which The variable to access
   * @return The variable's integer value
   */
  int getIntOpt(string which);

  /**
   * Return the value of an integer variable
   * 
   * @param which The variable to access
   * @param value The variable's default value
   * @return The variable's integer value
   */
  int getIntOpt(string which,int value);
	
  /**
   * Return the value of a double precision variable
   * 
   * @param which The variable to access
   * @return The variable's double precision value
   */
  double getDoubleOpt(string which);

  /**
   * Return the value of a double precision variable
   * 
   * @param which The variable to access.
   * @param value The variable's default value.
   * @return The variable's double precision value.
   */
  double getDoubleOpt(string which, double value);
	
  /**
   * Return the value of a string variable
   * 
   * @param which The variable to access
   * @return The variable's string value
   */
  string getStringOpt(string which);

  /**
   * Return the value of a string variable
   * 
   * @param which The variable to access.
   * @param value The variable's default value.
   * @return The variable's string value.
   */
  string getStringOpt(string which, string value);

  /**
   * Return the value of a string variable
   * 
   * @param which The variable to access.
   * @param value The variable's default value.
   * @return The variable's string value.
   */
  string getStringOpt(string which,char value[]);
	
  /**
   * Write the header to a file. The end user is responsible
   * for providing a valid file pointer and closing the file
   * when s/he is done with it.
   *
   * @param fp A pointer to a file with write permissions
   */
  void writeToFile(FILE *fp);
	
  void writeToFile(FILE *fp, std::string filename);

  vector<double> readDoubleVector(FILE *fp,unsigned size=100);

  ~input_file();
};
#endif
