package edu.iu.nwb.converter.edgelist;

//Java
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
//OSGi
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;
//CIShell
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.guibuilder.GUIBuilderService;




/**
 * @author Weixia(Bonnie) Huang 
 */
public class EdgeListValidatorFactory implements AlgorithmFactory {
	
	   protected void activate(ComponentContext ctxt) {}
	   protected void deactivate(ComponentContext ctxt) { }
	
	   /**
	    * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
	    */
	   public MetaTypeProvider createParameters(Data[] dm) {
	        return null;
	   }
	    
	   /**
	    * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	    */
	   public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
	           CIShellContext context) {
		   return new EdgeListValidator(dm, parameters, context);
	   }
	    
	   public class EdgeListValidator implements Algorithm {
	        
	    	Data[] data;
	        Dictionary parameters;
	        CIShellContext ciContext;
	        
	        public EdgeListValidator(Data[] dm, Dictionary parameters, CIShellContext context) {
	        	this.data = dm;
	            this.parameters = parameters;
	            this.ciContext = context;
	        }

	        public Data[] execute() {

	        	GUIBuilderService guiBuilder = 
	    			(GUIBuilderService)ciContext.getService(GUIBuilderService.class.getName());
				String fileHandler = (String) data[0].getData();
				
				File inData = new File(fileHandler);
				ValidateEdgeFile validator = new ValidateEdgeFile();
				try{ 
					validator.validateEdgeFormat(inData);

					if(validator.getValidationResult()){	

						Data[] dm = new Data[] {new BasicData(inData, "file:text/edge")};
						dm[0].getMetaData().put(DataProperty.LABEL, "edge file: " + fileHandler);
						dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
						return dm;

					} else {
						return null;
					}

				} catch (FileNotFoundException e){
					guiBuilder.showError("File Not Found Exception", 
							"Got an File Not Found Exception",e);	
					return null;
				} catch (IOException ioe){
					guiBuilder.showError("IOException", 
							"Got an IOException",ioe);
					return null;
				} catch (edu.iu.nwb.converter.edgelist.EdgeListValidatorFactory.ValidateEdgeFile.EdgeFormat ef) {
					System.out.println(">>>wrong format: "+validator.getErrorMessages());
					guiBuilder.showError("Bad edgelist Format", 
							"Sorry, your file does not comply with the edgelist file format specification.",
							"Sorry, your file does not comply with the edgelist file format specification.\n"+
							"Please review the latest edgelist file format Specification at "+
							"https://nwb.slis.indiana.edu/community/?n=LoadData.Edgelist, and update your file. \n"+
							validator.getErrorMessages());
					return null;	
				}
				
				

	        }
	      

	    }
	   


	   public class ValidateEdgeFile {
		   
		   private int currentLine;
		   private StringBuffer errorMessages = new StringBuffer();
		   private boolean isFileGood = true;

		   /* handle this merging of tokens with tail recursion */
		   public String[] tokenmanage (String[] tokens, int startFrom) {
			   //System.out.println("entering tokenmanage\n");
			   int i;
			   int j;
			   String newtokens[];
			   boolean breakflag = false;
			   for (i = startFrom;i < tokens.length;i++) {
				   if (breakflag) break;
				   // find "\"\w" tokens (position i) and match them with end tokens (position j)
				   // once matched and merged, set i = j, rinse and repeat
				   if (tokens[i].matches("\".*\"")) { // it's  a single word, quoted
					   // continue
				
				   }
				   else if (tokens[i].matches("\".*\".*")) { // end-quote happens in the middle of the token
					   // probably we should just continue...
				
				   }
				   else if (tokens[i].startsWith("\"")) {
					   // starts with ", but since previous ifs didn't match, we know it doesn't end
					   // with " or have a " in the middle
				
					   j = i+1;
					   if (j < tokens.length) {
						   for (;j < tokens.length;j++) {
							   if (tokens[j].matches(".*\"")) {
								   newtokens = tokenmerge(tokens,i,j);
								   return tokenmanage(newtokens, i+1);
								   /*				breakflag = true;
		    						break;*/

							   }
						   }
					   }
				   }

			   }
			   return tokens;
		   }

		   /*
		    * Takes a String[] of tokens and merges all tokens from index i to index j
		    * Returns the number of 
		    */
		   public String[] tokenmerge(String[] tokens, int i, int j) {
			   int x,y;
			   String [] out = new String[tokens.length - (j - i)];
			   String merged = "";
			   for (y = i;y < j+1;y++){
				   if (y == j) {
					   merged = merged.concat(tokens[y]);
				   } else {
					   merged = merged.concat(tokens[y] + " ");	
				   }

			   }
			   for (x=0;x < tokens.length - (j - i);x++) {
				   if (x < i) {
					   out[x] = tokens[x];
				   }
				   else if (x == i) {
					   out[x] = merged;
				   } else { // x > i
					   out[x] = tokens[x + (j - i)];
				   }
			   }
			   return out;
		   }



		   public String getErrorMessages() {
			   return errorMessages.toString();
		   }

		   public boolean getValidationResult() {
			   return isFileGood;
		   }

		  
		   /*
		    * validateEdgeFormat invokes processFile with a BufferedReader corresponding to the file
		    * that we will validate.
		    * -- Felix Terkhorn
		    * -- May 31 2007
		    */
		   public void validateEdgeFormat(File fileHandler)
		   throws FileNotFoundException, IOException, EdgeFormat {
			   //System.out.println("inside validateEdgeFormat\n");
			   currentLine = 0;
			   BufferedReader reader = new BufferedReader(new FileReader(fileHandler));
			   try {
				   this.processFile(reader);
			   } catch (EdgeFormat ef) {
				   throw ef;
			   }
		   }
		   
		   public class EdgeFormat extends Exception {
			   
		   }
		   
		   public void processFile(BufferedReader reader) throws EdgeFormat, IOException {
			   //System.out.println("inside processFile method\n");
			   String line = reader.readLine();

			   while (line != null && isFileGood) {
				   currentLine++;
				   //System.out.println("inside while loop at processFile. currentLine = "+currentLine+"\n");
				   // process section header that looks like
				   // *nodes or *nodes 1000
				   if (this.validateEdge(line) && isFileGood) {
					   //System.out.println("inside validateEdge clause\n");
					   processEdge(line);
					   if (isFileGood) {
						   this.checkFile();
					   }
					   line = reader.readLine();
					   continue;
				   } else {
					   throw new EdgeFormat();
				   }
				 
				   
			   }
		   }

		   /* 
		    * This method saves information about the edge in this object
		    */
		   public void processEdge(String s) {
			   
		   }
		   
		   /*
		    * This method checks to see if the given line validates edgelist rules.
		    * Invalid edge formats:
		    * 	case1-> node						  			<single node line>
		    * 	case2-> "node1 node2							<no matching end-quote in this line>
		    * 	case3-> node1 node2"							<no matching begin-quote in this line>
		    * 	case4-> "" node2								<empty node name>
		    * 	case5-> node1	node2	three				<weight must be an int OR float value>
		    */
		   public boolean validateEdge(String s) {
			   //System.out.println("inside validateEdge\n");
			   String[] tokens = tokenmanage(s.trim().split("\\s+"),0);
			   if (tokens.length < 2) { // case 1 
				   return false;
			   } else if (s.matches("[^\"]*\"[^\"]*")) { // cases 2 & 3
				   return false;
			   } else if (s.matches(".*\"\".*")) { // case 4
				   return false;
			   } else if (tokens.length > 2 && !tokens[2].matches("[0-9]+")) { // case 5
				   return false;
			   } else {  // if none of the above clauses are true, this line is OK.
				   return true;
			   }
		   }
		   
		   public void checkFile() {
				
				// not sure
			}
		   
	   }

}






  

    
    
    

