package edu.iu.nwb.converter.nwbpajeknet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.Iterator;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.UnicodeReader;

import edu.iu.nwb.converter.pajeknet.common.NETArcsnEdges;
import edu.iu.nwb.converter.pajeknet.common.NETAttribute;
import edu.iu.nwb.converter.pajeknet.common.NETFileProperty;
import edu.iu.nwb.converter.pajeknet.common.NETFileValidator;
import edu.iu.nwb.converter.pajeknet.common.NETVertex;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;

public class PajeknetToNWB implements Algorithm {
	private File inNetFile;
	
	public PajeknetToNWB(Data[] data, Dictionary<String, Object> parameters) {
		inNetFile = (File) data[0].getData();

		try {
			System.err.println(FileUtilities.readEntireTextFile(inNetFile));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}


	public Data[] execute() throws AlgorithmExecutionException {
		NETFileValidator validator = new NETFileValidator();

		try {
			validator.validateNETFormat(inNetFile);

			if (validator.getValidationResult()) {
				File outNWBFile = convertNetToNWB(validator, inNetFile);
				
				return createOutData(outNWBFile);
			} else {
				throw new AlgorithmExecutionException(
					"Error converting from Pajek .net to NWB: "
						+ validator.getErrorMessages());
			}
		} catch (FileNotFoundException e) {
			String message = "Couldn't find Pajek .net file: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		} catch (IOException e) {
			String message = "File access error: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		}
	}


	private Data[] createOutData(File outNWBFile) {
		Data[] dm = new Data[] {new BasicData(outNWBFile, "file:text/nwb")};
		return dm;
	}

	private File convertNetToNWB(NETFileValidator validator, File f)
			throws AlgorithmExecutionException{
		try {
			File outNWBFile =
				FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
						"NWB-", "nwb");
			PrintWriter out = new PrintWriter(outNWBFile, "UTF-8");
			//	out.flush();
			BufferedReader br = new BufferedReader(new UnicodeReader(new FileInputStream(f)));
			processFile(validator,br,out);	
			out.close();
			return outNWBFile;
		} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}

	private void processFile(NETFileValidator nv, BufferedReader reader, PrintWriter pw)
			throws IOException {
		boolean inVerticesSection = false;
		boolean inArcsSection = false;
		boolean inEdgesSection = false;

		String line = reader.readLine();

		while (line != null){
			//currentLine++;

			line = line.trim();
			if(line.startsWith(NETFileProperty.PREFIX_COMMENTS) || (line.length() < 1)){
				line = reader.readLine();
				continue;
			}

			if(line.toLowerCase().startsWith(NETFileProperty.HEADER_VERTICES)){

				writeHeader("Nodes "+nv.getTotalNumOfNodes(),pw);
				writeNodeAttributeList(nv,pw);
				inVerticesSection = true;
				inArcsSection = false;
				inEdgesSection = false;
				/*if((nv.getNumVertices() == 0) && (nv.getTotalNumOfNodes() > 0)){
					for(int i = 0; i < nv.getTotalNumOfNodes(); i++){
						String s = (i+1) + " \"" + (i+1) + "\"";
						try{
							NETVertex netv = new NETVertex(s);
							writeNode(nv,pw,netv);

						}
						catch(Exception e){
							e.printStackTrace();

						}
					}
				}*/
				line = reader.readLine();

				continue;
			}
			if(line.toLowerCase().startsWith(NETFileProperty.HEADER_ARCS)){
				writeHeader("DirectedEdges "+nv.getNumArcs(),pw);
				writeDirectedEdgeAttributeList(nv,pw);
				inVerticesSection = false;
				inArcsSection = true;
				inEdgesSection = false;
				line = reader.readLine();

				continue;
			}

			if(line.toLowerCase().startsWith(NETFileProperty.HEADER_EDGES)){
				writeHeader("UndirectedEdges "+nv.getNumEdges(),pw);
				writeUndirectedEdgeAttributeList(nv,pw);
				inVerticesSection = false;
				inArcsSection = false;
				inEdgesSection = true;
				line = reader.readLine();

				continue;
			}


			if(inVerticesSection){	
				NETVertex netv = nv.processVertices(line);
				writeNode(nv,pw,netv);
				line = reader.readLine();

				continue;
			}

			if(inEdgesSection){
				NETArcsnEdges ane = nv.processArcsnEdges(line);
				writeEdge(nv,pw,ane);
				line = reader.readLine();

				continue;
			}

			if(inArcsSection){
				NETArcsnEdges ane = nv.processArcsnEdges(line);
				writeArc(nv,pw,ane);
				line = reader.readLine();

				continue;
			}


			line = reader.readLine();
		}	

	}

	private void writeHeader(String s, PrintWriter out){
		//out.flush();
		String st = NWBFileProperty.PRESERVED_STAR+s;
		//System.out.println(st);
		out.println(st);
	}

	private void writeNodeAttributeList(NETFileValidator validator, PrintWriter writer) {
		String st = "";
		for (Iterator<NETAttribute> it = validator.getVertexAttrList().iterator(); it.hasNext();) {
			NETAttribute na = it.next();
			String s = na.getAttrName();

			if ((s.charAt(s.length() - 1) == 'c') && "float".equalsIgnoreCase(na.getDataType())) {
				for (int ii = 0; ii < 3; ii++) {
					st += s + (ii + 1) + NWBFileProperty.PRESERVED_STAR + na.getDataType() +  " ";
				}
			} else {
				st += s + NWBFileProperty.PRESERVED_STAR + na.getDataType() + " ";
			}
		}

		writer.println(st);
	}
	
	private void writeUndirectedEdgeAttributeList(NETFileValidator validator, PrintWriter out) {
		//out.flush();
		String st = "";//NWBFileProperty.PREFIX_COMMENTS;
		for(Iterator it = validator.getEdgeAttrList().iterator(); it.hasNext();){
			NETAttribute na = (NETAttribute) it.next();
			String s = na.getAttrName();
			if(s.charAt(s.length()-1) == 'c' && na.getDataType().equalsIgnoreCase("float")){
				for(int i = 0; i < 3; i++){
					st += s + (i+1) + NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
				}
			}
			else
				st += s+NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
			//st += na.getAttrName()+NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";

		}
		//System.out.println(st);
		out.println(st);
	}
	private void writeDirectedEdgeAttributeList(NETFileValidator validator, PrintWriter out) {
		//out.flush();
		String st = ""; //NWBFileProperty.PREFIX_COMMENTS;
		for(Iterator ii = validator.getArcAttrList().iterator(); ii.hasNext();){
			NETAttribute na = (NETAttribute) ii.next();
			String s = na.getAttrName();
			if(s.charAt(s.length()-1) == 'c' && na.getDataType().equalsIgnoreCase("float")){
				for(int i = 0; i < 3; i++){
					st += s + (i+1) + NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
				}
			}
			else {
				st += s+NWBFileProperty.PRESERVED_STAR+na.getDataType()+ " ";
			}

		}
		//	System.out.println(st);
		out.println(st);
	}
	
	private void writeNode(NETFileValidator validator, PrintWriter out, NETVertex nv) {
		String st = "";
		for(Iterator it = validator.getVertexAttrList().iterator(); it.hasNext();){
			NETAttribute na = (NETAttribute) it.next();
			try{
				Object o = nv.getAttribute(na.getAttrName());
				if(o.toString() == null){
					if(na.getDataType().equalsIgnoreCase("float") && na.getAttrName().matches("[bil]?c"))
						st += "* * * ";
					else
						st += "* ";
				}
				else
					if(na.getDataType().equalsIgnoreCase("string")){
						st +=  "\""+o + "\" ";
					}
					else {
						st +=  o + " ";
					}


			} catch(NullPointerException e) {
				if(na.getDataType().equalsIgnoreCase("float")
						&& na.getAttrName().matches("[bil]?c"))
					st += "* * * ";
				else
					st += "* ";
			}
		} 

		out.println(st);
	}


	private void writeEdge(NETFileValidator validator, PrintWriter out, NETArcsnEdges nae){
		//	out.flush();
		String st = "";
		for(Iterator it = validator.getEdgeAttrList().iterator(); it.hasNext();){
			NETAttribute na = (NETAttribute) it.next();
			try{
				Object o = nae.getAttribute(na.getAttrName());
				//System.out.println(o);
				if(o.toString() == null){
					if(na.getDataType().equalsIgnoreCase("float") && na.getAttrName().matches("[bil]?c")){
						//System.out.println("YES");
						st += "* * * ";
					}
					else{
						//System.out.println("NO");
						st += "* ";
					}

				}
				else 
					if(na.getDataType().equalsIgnoreCase("string")){
						st +=  "\""+o + "\" ";
					}
					else
						st +=  o + " ";

			} catch(NullPointerException e) {
				if(na.getDataType().equalsIgnoreCase("float")
						&& na.getAttrName().matches("[bil]?c")){
					//System.out.println("YES");
					st += "* * * ";
				}
				else{
					//System.out.println("NO");
					st += "* ";
				}

			}


		}
		//	System.out.println(st);
		out.println(st);
	}


	private void writeArc(NETFileValidator validator, PrintWriter out, NETArcsnEdges nae){
		//out.flush();
		String st = "";
		for(Iterator it = validator.getEdgeAttrList().iterator(); it.hasNext();){
			NETAttribute na = (NETAttribute) it.next();
			try{
				Object o = nae.getAttribute(na.getAttrName());
				if(o.toString() == null){
					if(na.getDataType().equalsIgnoreCase("float") && na.getAttrName().matches("[bil]?c"))
						st += "* * * ";
					else
						st += "* ";
				}
				else 
					if(na.getDataType().equalsIgnoreCase("string")){
						st +=  "\""+o + "\" ";
					}
					else
						st +=  o + " ";

			} catch(NullPointerException npe) {
				if(na.getDataType().equalsIgnoreCase("float")
						&& na.getAttrName().matches("[bil]?c")) {
					st += "* * * ";
				}
				else {
					st += "* ";
				}
			}


		}
		//	System.out.println(st);
		out.println(st);
	}
}
