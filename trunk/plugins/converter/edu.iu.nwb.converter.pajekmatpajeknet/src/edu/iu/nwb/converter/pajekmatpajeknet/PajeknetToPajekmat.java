package edu.iu.nwb.converter.pajekmatpajeknet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.pajekmat.common.MATFileProperty;
import edu.iu.nwb.converter.pajeknet.common.NETArcsnEdges;
import edu.iu.nwb.converter.pajeknet.common.NETAttribute;
import edu.iu.nwb.converter.pajeknet.common.NETFileFunctions;
import edu.iu.nwb.converter.pajeknet.common.NETFileProperty;
import edu.iu.nwb.converter.pajeknet.common.NETVertex;
import edu.iu.nwb.converter.pajeknet.common.ValidateNETFile;

public class PajeknetToPajekmat implements Algorithm{

	Data[] data;
	Dictionary parameters;
	CIShellContext ciContext;
	LogService logger;
	String[] noPrintParameters = { NETFileProperty.ATTRIBUTE_ID, NETFileProperty.ATTRIBUTE_LABEL, "xpos", "ypos", "zpos", "shape",NETFileProperty.ATTRIBUTE_SOURCE, NETFileProperty.ATTRIBUTE_TARGET, NETFileProperty.ATTRIBUTE_WEIGHT };


	public PajeknetToPajekmat(Data[] dm, Dictionary parameters, CIShellContext cContext){
		this.data = dm;
		this.parameters = parameters;
		this.ciContext = cContext;
		this.logger = (LogService)ciContext.getService(LogService.class.getName());
	}

	private File getTempFile(){
		File tempFile;

		String tempPath = System.getProperty("java.io.tmpdir");
		File tempDir = new File(tempPath+File.separator+"temp");
		if(!tempDir.exists())
			tempDir.mkdir();
		try{
			tempFile = File.createTempFile("NWB-Session-", ".net", tempDir);

		}catch (IOException e){
			logger.log(LogService.LOG_ERROR, e.toString());
			tempFile = new File (tempPath+File.separator+"netTemp"+File.separator+"temp.net");

		}
		return tempFile;
	}

	public Data[] execute() {
		File inData, outData;
		Data [] dm = null;
		ValidateNETFile validator;
		//System.out.println("Executing PajeknetToPajekMat");
		Object inFile = data[0].getData();

		if (inFile instanceof File){
			inData = (File)inFile;

			validator = new ValidateNETFile();
		//System.out.println("Before try");
			try {
		//System.out.println("Trying");
				validator.validateNETFormat(inData);
				if(validator.getValidationResult()){
					outData = convertNetToMat(validator,inData);
					if(outData != null){
						dm = new Data[] {new BasicData(outData, MATFileProperty.MAT_MIME_TYPE)};
						return dm;
					}else {
						logger.log(LogService.LOG_ERROR, "Problem executing conversion from Pajek .net to .mat. Output file was not created");
						return null;
					}
				}else{
					logger.log(LogService.LOG_ERROR,"Problem executing conversion from Pajek .net to .mat " + validator.getErrorMessages());
					return null;
				}
			}
			catch (FileNotFoundException fnf){
				logger.log(LogService.LOG_ERROR, "Could not find the specified Pajek .net file.", fnf);
				return null;
			}
			catch (IOException ioe){
				logger.log(LogService.LOG_ERROR, "IO Error while converting from Pajek .net to .mat.", ioe);
				ioe.printStackTrace();
				return null;
			}
			catch( Exception ex){
				logger.log(LogService.LOG_ERROR, "Error while converting from Pajek .net to .mat." + validator.getErrorMessages(), ex);
				ex.printStackTrace();
				return null;
			}
		}
		else
			logger.log(LogService.LOG_ERROR, "Unable to convert the file. " +
			"Unable to convert from Pajek .net to .mat because input data is not a file");
		return null;
	}

	private File convertNetToMat(ValidateNETFile vmf, File f){
		try{
			//System.out.println("Converting net to mat");
			File mat = getTempFile();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(mat)));
			BufferedReader br = new BufferedReader(new FileReader(f));
			processFile(vmf,br,out);
			out.close();
			return mat;
		}catch (FileNotFoundException e){
			logger.log(LogService.LOG_ERROR, "Unable to find the temporary .mat file.", e);
			return null;
		}catch (IOException ioe){
			logger.log(LogService.LOG_ERROR, "IO Errors while writing to the temporary .mat file.", ioe);
			return null;
		}
	}
	
	private void processFile(ValidateNETFile nv, BufferedReader reader, PrintWriter pw) throws IOException{
		boolean inVerticesSection = false;
		boolean inArcsSection = false;
		boolean inEdgesSection = false;
		
		String line = reader.readLine();
		//float[] edges = new float[nv.getTotalNumOfNodes()];
		while (line != null){
			//currentLine++;
		

			if(line.startsWith(NETFileProperty.PREFIX_COMMENTS) || (line.length() < 1)){
				line = reader.readLine();
				continue;
			}

			if(line.toLowerCase().startsWith(NETFileProperty.HEADER_VERTICES)){
				
				pw.print("*Vertices "+ nv.getTotalNumOfNodes()+"\r\n");
				inVerticesSection = true;
				inArcsSection = false;
				inEdgesSection = false;
				line = reader.readLine();
				continue;
			}
			if(line.toLowerCase().startsWith(NETFileProperty.HEADER_ARCS)){
				pw.print("*Matrix "+"\r\n");
				inVerticesSection = false;
				inArcsSection = true;
				inEdgesSection = false;
				line = reader.readLine();

				continue;
			}

			if(line.toLowerCase().startsWith(NETFileProperty.HEADER_EDGES)){
				pw.print("*Matrix "+"\r\n");
				inVerticesSection = false;
				inArcsSection = false;
				inEdgesSection = true;
				line = reader.readLine();

				continue;
			}


			if(inVerticesSection){	
				NETVertex netv = nv.processVertices(line);
				writeVertex(nv,pw,netv);
				line = reader.readLine();

				continue;
			}

			if(inEdgesSection){
				ArrayList connections = new ArrayList();
					while(line != null){
						if(!line.startsWith(NETFileProperty.PRESERVED_STAR)){
					
						if(!(processLine(line).length() < 1)){
							NETArcsnEdges ane = nv.processArcsnEdges(line);
							connections.add(ane);
						}
						}
						line = reader.readLine();
					}
					
				
				writeUndirectedMatrix(nv,pw,connections);
				
				continue;
			}

			if(inArcsSection){
				ArrayList connections = new ArrayList();
				while(line != null){
					if(!line.startsWith(NETFileProperty.PRESERVED_STAR)){
					if(!(processLine(line).length() < 1)){
						NETArcsnEdges ane = nv.processArcsnEdges(line);
						connections.add(ane);
					}
					}
					line = reader.readLine();
				}
				writeDirectedMatrix(nv,pw,connections);
				continue;
			}


			line = reader.readLine();
		}	
		
	}
	
	private static String processLine(String line){
		if(line.startsWith(NETFileProperty.PREFIX_COMMENTS))
			return "";
		else
			return line;
	}

	private void writeVertex(ValidateNETFile vmf, PrintWriter pw, NETVertex nv){
		//pw.write(MATFileProperty.HEADER_VERTICES + " " + vmf.getVertices().size() + "\n");
			//System.out.println(nv);
			String s = "";
			for(int j = 0; j < NETVertex.getVertexAttributes().size(); j++){
				try{
					NETAttribute ma = (NETAttribute)NETVertex.getVertexAttributes().get(j);
					String attr = ma.getAttrName();
					String type = ma.getDataType();
					String value = nv.getAttribute(attr).toString();
					//System.out.println(attr + " " + value);
					if(value == null){
						s += "";
					}
					else{
						if(NETFileFunctions.isInList(attr,noPrintParameters)){
							if(type.equalsIgnoreCase("string"))
								s += "\"" + nv.getAttribute(attr) + "\" ";
							else
								s += nv.getAttribute(attr)+" ";
						}
						else {
							if(type.equalsIgnoreCase("string")){
								if(attr.startsWith("unknown"))
									s += " \"" + nv.getAttribute(attr) + "\" ";
								else
									s += attr + " \"" + nv.getAttribute(attr) + "\" ";
							}
							else
								s += attr + " " + nv.getAttribute(attr) + " ";
						}
					}
				}catch(NullPointerException ex){
					s += "";
				}
			}
		 
	
		pw.print(s+"\r\n");
	
	}

	private void writeDirectedMatrix(ValidateNETFile vmf, PrintWriter pw, ArrayList arcs){
		NETArcsnEdges nae;
		int source, target;
		float weight = (float)0.0;
		//System.out.println(vmf.getEdges().size());
		//pw.println(MATFileProperty.HEADER_MATRIX);
		for(int i = 0; i < vmf.getTotalNumOfNodes(); i++){
			for(int j = 0; j < vmf.getTotalNumOfNodes(); j++){
				for(int k = 0; k < arcs.size(); k++){
					nae = (NETArcsnEdges)arcs.get(k);
					target = ((Integer)nae.getAttribute(NETFileProperty.ATTRIBUTE_TARGET)).intValue();
					source = ((Integer)nae.getAttribute(NETFileProperty.ATTRIBUTE_SOURCE)).intValue();
					//System.out.println(nvi.getID() + ":"+ source + "\t" + nvj.getID()+":"+target);
					
					if(((i+1) == source) && ((j+1) == target)){
						try{
						weight = ((Float)nae.getAttribute(NETFileProperty.ATTRIBUTE_WEIGHT)).floatValue();
						}catch(NullPointerException npe){
							weight = 1;
						}
					//System.out.println(weight);
						break;
					}
					else {
						weight = (float)0.0;
					}
				}
				pw.print(weight + " ");
			}
			pw.print("\r\n");
		}
		
		}
	
	private void writeUndirectedMatrix(ValidateNETFile vmf, PrintWriter pw, ArrayList edges){
		//float[][] matrix = new float[vmf.getVertices().size()][vmf.getVertices().size()];
		NETArcsnEdges nae;
		int source, target;
		float weight = (float)0.0;
		//System.out.println(vmf.getEdges().size());
		//pw.println(MATFileProperty.HEADER_MATRIX);
		for(int i = 0; i < vmf.getTotalNumOfNodes(); i++){
			for(int j = 0; j < vmf.getTotalNumOfNodes(); j++){
				for(int k = 0; k < edges.size(); k++){
					nae = (NETArcsnEdges)edges.get(k);
					target = ((Integer)nae.getAttribute(NETFileProperty.ATTRIBUTE_TARGET)).intValue();
					source = ((Integer)nae.getAttribute(NETFileProperty.ATTRIBUTE_SOURCE)).intValue();
					//System.out.println(nvi.getID() + ":"+ source + "\t" + nvj.getID()+":"+target);
					
					if((((i+1) == source) && ((j+1) == target))||
							(((i+1) == target) && ((j+1) == source))){
						try{
						weight = ((Float)nae.getAttribute(NETFileProperty.ATTRIBUTE_WEIGHT)).floatValue();
						}catch(NullPointerException npe){
							weight = 1;
						}
						//System.out.println(weight);
						break;
					}
					else {
						weight = (float)0.0;
					}
					
				}
				pw.print(weight + " ");
			}
			pw.print("\r\n");
		}
		
	}
			
		
		
	}




