package edu.iu.nwb.converter.pajekmatpajeknet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;

import edu.iu.nwb.converter.pajekmat.common.MATArcs;
import edu.iu.nwb.converter.pajekmat.common.MATAttribute;
import edu.iu.nwb.converter.pajekmat.common.MATFileFunctions;
import edu.iu.nwb.converter.pajekmat.common.MATFileProperty;
import edu.iu.nwb.converter.pajekmat.common.MATVertex;
import edu.iu.nwb.converter.pajekmat.common.MATFileValidator;
import edu.iu.nwb.converter.pajeknet.common.NETFileProperty;

public class PajekmatToPajeknet implements Algorithm{
    public static final String[] noPrintParameters = { NETFileProperty.ATTRIBUTE_ID, NETFileProperty.ATTRIBUTE_LABEL, "xpos", "ypos", "zpos", "shape",NETFileProperty.ATTRIBUTE_SOURCE, NETFileProperty.ATTRIBUTE_TARGET, NETFileProperty.ATTRIBUTE_WEIGHT };
	
    private File inMATFile;

    
    public PajekmatToPajeknet(Data[] data, Dictionary parameters, CIShellContext context){
    	inMATFile = (File) data[0].getData();
    }
    
    
	public Data[] execute() throws AlgorithmExecutionException {
    	try {
			MATFileValidator validator = new MATFileValidator();
			validator.validateMATFormat(inMATFile);
			if(validator.getValidationResult()){
				File outNETFile = convertMatToNet(validator);
				
				return createOutData(outNETFile);
			} else {
				throw new AlgorithmExecutionException(
					"Error converting from Pajek .mat to Pajek .net: "
						+ validator.getErrorMessages());
			}
		} catch (FileNotFoundException e) {
			String message = "Couldn't find Pajek .mat file: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		} catch (IOException e) {
			String message = "File access error: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		}
	}


	private Data[] createOutData(File outNETFile) {
		Data[] dm = new Data[]{ new BasicData(
				outNETFile, NETFileProperty.NET_MIME_TYPE) };
		return dm;
	}
	
	private File convertMatToNet(MATFileValidator matValidator)
			throws AlgorithmExecutionException {
		try{
			File outNETFile =
				FileUtilities.createTemporaryFileInDefaultTemporaryDirectory("Pajek-", "net");
			PrintWriter out = new PrintWriter(outNETFile, "UTF-8");	
			writeVertices(matValidator, out);
			writeArcs(matValidator, out);
			out.close();
			return outNETFile;
		} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}
	
	private void writeVertices(MATFileValidator vmf, PrintWriter pw){
		pw.write(MATFileProperty.HEADER_VERTICES + " " + vmf.getTotalNumOfNodes() + "\n");
		for(int ii = 0; ii < vmf.getVertices().size(); ii++){
			MATVertex mv = (MATVertex)vmf.getVertices().get(ii);
			String s = "";
			for(int jj = 0; jj < MATVertex.getVertexAttributes().size(); jj++){
				MATAttribute ma = (MATAttribute)MATVertex.getVertexAttributes().get(jj);
				String attr = ma.getAttrName();
				String type = ma.getDataType();
				if(mv.getAttribute(attr) == null){
					s += "";
					continue;
				}
				if(MATFileFunctions.isInList(attr,noPrintParameters)){
					if(type.equalsIgnoreCase("string"))
						s += "\"" + mv.getAttribute(attr) + "\" ";
					else
						s += mv.getAttribute(attr)+" ";
				}
				else {
					if(type.equalsIgnoreCase("string")){
						if(attr.startsWith("unknown")){
						s += " \"" + mv.getAttribute(attr) + "\" ";
						}
						else{
							s += attr + " \"" + mv.getAttribute(attr) + "\" ";
						}
				}
						else
						s += attr + " " + mv.getAttribute(attr) + " ";
				}
			}
			pw.write(s + "\n");
		}
	}
	
	private void writeArcs(MATFileValidator vmf, PrintWriter pw){
		pw.write(MATFileProperty.HEADER_ARCS + " " + (vmf.getArcs().size()) + "\n");
		for(int ii = 0; ii < vmf.getArcs().size(); ii++){
			MATArcs mv = (MATArcs)vmf.getArcs().get(ii);
			String s = "";
			for(int jj = 0; jj < MATArcs.getArcsnEdgesAttributes().size(); jj++){
				MATAttribute ma = (MATAttribute)MATArcs.getArcsnEdgesAttributes().get(jj);
				String attr = ma.getAttrName();
				String type = ma.getDataType();
				if(mv.getAttribute(attr) == null){
					s += "";
					continue;
				}
				else if(MATFileFunctions.isInList(attr,noPrintParameters)){
					if(type.equalsIgnoreCase("string"))
						s += "\"" + mv.getAttribute(attr) + "\" ";
					else
						s += mv.getAttribute(attr)+" ";
				}
				else {
					if(type.equalsIgnoreCase("string"))
						s += attr + " \"" + mv.getAttribute(attr) + "\" ";
					else
						s += attr + " " + mv.getAttribute(attr) + " ";
				}
				
			}
			pw.write(s + "\n");
		}
	}
}
