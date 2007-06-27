package edu.iu.nwb.converter.nwbgraphml;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.service.log.LogService;
import java.io.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

/**
 * Converts from GraphML to NWB file format via the Stax libraries
 * @author Megha 
 */

public class GraphMLToNWBbyStax implements Algorithm {
	
	  Data[] data;
	  Dictionary parameters;
	  CIShellContext ciContext;
	  LogService logger;
	  GUIBuilderService guiBuilder;
	  private XMLInputFactory inputFactory = null;    
	  private XMLStreamReader xmlReader = null;
	  private ArrayList keyNodeList;
	  private ArrayList keyEdgeList;
	  private ArrayList keyDataList;
	  private BufferedWriter nodeWriter;
	  private BufferedWriter undirectedEdgeWriter;
	  private BufferedWriter directedEdgeWriter;
	  private boolean boolDirected;
	  private int nodeCount=0;
	  private int undirectedEdgeCount=0;
	  private int directedEdgeCount=0;
	  private Hashtable NodeIdMapList;
	  private KeyDO objKeyDO = null;
	  private String tempPath = null;	
	  private File tempDir = null;
	  
	  /**
     * Intializes the algorithm
     * @param data List of Data objects to convert
     * @param parameters Parameters passed to the converter
     * @param context Provides access to CIShell services
     * @param transformer 
     */

	    public GraphMLToNWBbyStax(Data[] data, Dictionary parameters, CIShellContext context) {
	        this.data = data;
	        this.parameters = parameters;
	        this.ciContext = context;
	        this.logger = (LogService)ciContext.getService(LogService.class.getName());
	        
	        keyNodeList = new ArrayList();
	   	 	keyEdgeList = new ArrayList();
	   	 	NodeIdMapList = new Hashtable();
	   	 	
	   	 	inputFactory = XMLInputFactory.newInstance();
	   	 	
	       
	   	 	
	   	 	try{
	   	 		xmlReader = inputFactory.createXMLStreamReader(new FileReader((data[0].getData()).toString()));
	   	 	}
	   	 	catch(XMLStreamException ex)
	   	 	{}
	   	    catch(FileNotFoundException foe)
	   	    {
	   	    	logger.log(LogService.LOG_ERROR, "GraphML file not found ");
	   	    	foe.printStackTrace();
				
	   	    }
	   	 
	   	 	
	   	 	try
	   	 	{

	   	 		tempPath = System.getProperty("java.io.tmpdir");
	   	 		tempDir = new File(tempPath+File.separator+"temp");
	   	 		nodeWriter = new BufferedWriter(new FileWriter(getTempFile("node")));
	   	 		undirectedEdgeWriter = new BufferedWriter(new FileWriter(getTempFile("undirectedEdge")));
	   	 		directedEdgeWriter = new BufferedWriter(new FileWriter(getTempFile("directedEdge")));
	   	 	}
	   	 	catch(IOException eio)
	   	 	{
	   	 		eio.printStackTrace();
	   	 	}
	   	 	
	   	 	
	       
	    } 

	public Data[] execute(){
		System.err.println("inside exceute method of stax parsing ");
		 Object inFile = data[0].getData();
		 if (inFile instanceof File){
	          try{
	        	   	read();            
			      }catch(Exception exception){
			    	  logger.log(LogService.LOG_ERROR, "Problem executing transformation from GraphML to NWB");  
			           exception.printStackTrace();
			      }
			 return new Data[] {new BasicData(new File(tempDir.toString()+ File.separator + "node.nwb"), "file:text/nwb")};
		 }else
			 return null;
	 }
	
	 public void read() throws Exception
	 {
		pl("inside read method");
		 while (xmlReader.hasNext())
	    {   
	        int eventType = xmlReader.next();
	        if (eventType == XMLEvent.START_ELEMENT){
	        	//check for graph element
	        	if (xmlReader.getLocalName().equals("graph")&& xmlReader.getAttributeCount() > 0)
	        	{
	        		if (getAttributeValue("edgedefault").equals("directed"))
	        			boolDirected = true;
	        	}
				
	    		//check for node element
	        	if (xmlReader.getLocalName().equals("node")&& xmlReader.getAttributeCount() > 0)
	        	{
	        		NodeIdMapList.put( getAttributeValue("id"),Integer.valueOf(nodeCount));
	        		nodeCount++;
	        		
	        		if (nodeCount == 1)
	        			printNodeHeader(nodeCount);
	        		
	    			printNode(nodeCount);
	        	}
		            
	        	// check for edge element
	        	if (xmlReader.getLocalName().equals("edge")&& xmlReader.getAttributeCount() > 0)
	        	{
	        		if(isDirectedEdge())
	        		{
	        			directedEdgeCount++;
	        			if(directedEdgeCount==1)
	        				printEdgeHeader(true);
	        			printEdge(true);
	        		}else
	        		{
	        			undirectedEdgeCount++;
	        			if(undirectedEdgeCount==1)
	        				printEdgeHeader(false);
	        			printEdge(false);
	        		}
	        	}
	        	
	          	//check for key element
	        	if (xmlReader.getLocalName().equals("key")&& xmlReader.getAttributeCount() > 0)
	        		addKeyDOToList();
	            	
	        }
	    }
	          
	    xmlReader.close();
	    directedEdgeWriter.close();
	    undirectedEdgeWriter.close();
	    nodeWriter.close();
	    
	    MergeFile();
	   
	    		File	undirectedFile = new File(tempDir.toString()+File.separator + "directedEdge.nwb");
	    		undirectedFile.delete();
	    		File	directedFile = new File(tempDir.toString()+File.separator + "undirectedEdge.nwb");
	    		directedFile.delete();
	    
	   

	}
	 private void MergeFile()
	 {
		 RandomAccessFile directedEdge=null;
		 RandomAccessFile undirectedEdge=null;
		 String str = null;
			
		 try{
			 	
			if (directedEdgeCount>0)
			{
				 directedEdge = new RandomAccessFile(tempDir.toString()+File.separator + "directedEdge.nwb" ,"rw");
				 directedEdge.seek("*DirectedEdges".length());
				 directedEdge.writeBytes("\t" + String.valueOf(directedEdgeCount));
				 directedEdge.seek(0);
			}
			
			if (undirectedEdgeCount>0)
			{
				undirectedEdge = new RandomAccessFile(tempDir.toString()+File.separator + "undirectedEdge.nwb" ,"rw");
				 undirectedEdge.seek("*UndirectedEdges".length());
				 undirectedEdge.writeBytes("\t" + String.valueOf(undirectedEdgeCount));
				 undirectedEdge.seek(0);
			} 
			
			if (nodeCount>0)
			{
				 RandomAccessFile node = new RandomAccessFile(tempDir.toString()+File.separator + "node.nwb" ,"rw");
				 node.seek("*Node".length());
				 node.writeBytes("\t" + String.valueOf(nodeCount));
				 node.seek(node.length());
				 if (directedEdge!=null)
				 {
					
					 while((str = directedEdge.readLine())!= null) 
					 {
						
						 node.writeBytes("\n"+ str);
						 
					 }
						directedEdge.close();
				 }
				 if (undirectedEdge!=null)
				 {
					 
					while((str = undirectedEdge.readLine())!= null) 
					 {
						 
						 node.writeBytes("\n"+ str);
						 
					 }
					 undirectedEdge.close();
				 }
				node.close();
				
			}
		 }catch(IOException ioe)
		 {
			 
		 }
		 

	 }
	 public boolean isDirectedEdge()
	 {
		 if (hasAttribute("directed"))
			 return boolDirected && Boolean.parseBoolean(getAttributeValue("directed"));
		 else
			 return boolDirected;
	 }
	 
	 public boolean hasAttribute(String attr_name)
	 {
		 for (int i=0; i< xmlReader.getAttributeCount(); i++)
		 {
			 if (xmlReader.getAttributeLocalName(i).equals(attr_name))
			 {
				 return true;
			 }
		 }
		 return false;
	 }
	 public void printEdgeHeader(boolean directed)
	 {
		  String printLineData = null;
		    KeyDO objKeyDO = null;
		    if (!directed)
		    {
		    	printLineData = "*UndirectedEdges     ";
		    	wf(printLineData, undirectedEdgeWriter);
		    }
		    else
		    {
		    	printLineData = "*DirectedEdges     ";
		    	wf(printLineData, directedEdgeWriter);
		    }
		    
		    printLineData = "source*int" + "\t" + "target*int";
		    
		    
			for (int i=0; i< keyEdgeList.size(); i++)
			{
				objKeyDO = (KeyDO)keyEdgeList.get(i);
				printLineData += "\t" + objKeyDO.getAttr_name()+"*"+ objKeyDO.getAttr_type();
			}
			if (!directed)
				wf(printLineData, undirectedEdgeWriter);
			else
				wf(printLineData, directedEdgeWriter);
	 }

	 public void printEdge(boolean directed)
	 {
		String printline = null;
		String keydata = null;
		KeyDO keyDOTemp = null;
		
		printline = NodeIdMapList.get(getAttributeValue("source")) + "\t" + NodeIdMapList.get(getAttributeValue("target"));

		// get data value of current edge
	 	addDataToList("edge");	
		for(int i=0; i < keyEdgeList.size(); i++)
		{
			if (keyDataList.get(i) == null)
			{
				keyDOTemp = (KeyDO)keyEdgeList.get(i);
				printline += "\t" + keyDOTemp.getAttr_value();
			}else
			{
				keydata = (String)keyDataList.get(i);
				printline += "\t" + keydata;
			}
		}
		
		pl(printline);
		
		if (directed)
			wf(printline,directedEdgeWriter);
		else
			wf(printline,undirectedEdgeWriter);
		// need to write a code to store data value of specific edge
		
		
	}
		
		

	 public void printNodeHeader(int intCount)
	   {
		    String printLineData = null;
		    KeyDO objKeyDO = null;
		    
		    printLineData = "*Node     ";
		    wf(printLineData,nodeWriter);
		    
		    
		    printLineData = "id*int";
		    
			for (int i=0; i< keyNodeList.size(); i++)
			{
				objKeyDO = (KeyDO)keyNodeList.get(i);
				printLineData += "\t" + objKeyDO.getAttr_name()+"*"+ objKeyDO.getAttr_type();
			}
			
			wf(printLineData,nodeWriter);
	   }
	public void addDataToList(String type)
	{
		 int eventType = -1;
		 int index = -1;
		 boolean boolLoop = true;
		 String keydataid = null;
		 ArrayList listTemp;
		 String keydata = null;
		 
		 if (type.equals("node"))
			 listTemp = keyNodeList;
		 else
			 listTemp = keyEdgeList;
				 
		 
		 
		keyDataList = new ArrayList(listTemp.size());
		   
		   for (int i=0; i < listTemp.size(); i++)
			   keyDataList.add(i, null);

		   try
		    {
			    while (boolLoop)
			 	{
			 		if (xmlReader.hasNext())
			 		{
			 			eventType = xmlReader.next();
			 			if (eventType == XMLEvent.START_ELEMENT){
			 				if (xmlReader.getLocalName().equals("data")&& xmlReader.getAttributeCount() > 0)
			 				{
			 					keydataid = getAttributeValue("key");
			 					index = getKeyDataIndex(keydataid, type);
			 					keydata = getElementText("data");
			 					
			 					if (keydata.equals("null"))
			 						keydata = "*";
			 					keyDataList.add(index, keydata);
			 				}
			 				
			 			}
			 			else if (eventType == XMLEvent.END_ELEMENT)
			 			{
			 				if (xmlReader.getLocalName().equals(type))
			 					boolLoop = false;
			 			}
			 		}
			 		else
				    {
				    	return;
				    }
			 	}
			    
		    }
			catch(XMLStreamException ese)
	   	 	{}

	}
	   public void printNode(int intCount)
	   {
		  
		   String keydata = null;
		   String printline = null;
		   KeyDO keyDOTemp = null;

			
			printline = String.valueOf(intCount);
			addDataToList("node");
			
			for(int i=0; i < keyNodeList.size(); i++)
			{
				if (keyDataList.get(i) == null)
				{
					keyDOTemp = (KeyDO)keyNodeList.get(i);
					printline += "\t" + keyDOTemp.getAttr_value();
				}else
				{
					keydata = (String)keyDataList.get(i);
					printline += "\t" + keydata;
				}
			}
			
			wf(printline,nodeWriter);
			
	   }
	   
	   public String getElementText(String elmName)
	   {
		   boolean boolLoop = true;
		   int eventType = -1;
		   
		   try
		    {
			    while (boolLoop)
			 	{
			 		if (xmlReader.hasNext())
			 		{
			 			eventType = xmlReader.next();

			 			
			 			if (eventType == XMLEvent.CHARACTERS)
			 			{
			 				
			 				return xmlReader.getText();
			 				
			 			}
			 			else if (eventType == XMLEvent.END_ELEMENT)
			 			{
			 				if (xmlReader.getLocalName().equals(elmName))
			 					boolLoop = false;
			 			}
			 		}
			 		else
				    {
				    	return null;
				    }
			 	}
			    
		    }
			catch(XMLStreamException ese)
	  	 	{}
			
		   return null;
	   }
	  
	   public int getKeyDataIndex(String id, String type)
	   {
		   KeyDO objKeyDOTemp = null;
		   
		   if (type.equals("node"))
		   {
			   for(int i=0;i < keyNodeList.size(); i++)
			   {
				   objKeyDOTemp = (KeyDO)keyNodeList.get(i);
				   
				   if (objKeyDOTemp.getId().equals(id))
						   return i;
			   }
		   }
		   else
		   {
			   for(int i=0;i < keyEdgeList.size(); i++)
			   {
				   objKeyDOTemp = (KeyDO)keyEdgeList.get(i);
				   
				   if (objKeyDOTemp.getId().equals(id))
						   return i;
			   }
		   }

		   return -1;
		   
	   }
	   
	   public void wf(String data,BufferedWriter writer)
	   {
		   
		   try
		   {
			    pl(data);
			   writer.write(data);
			   writer.newLine();
			   writer.flush();
			   
			 //  writer.close();

		   }
		   catch(IOException eio)
		   {
			   
		   }
	   }
	   
	   public void pl(String data)
	   {
		   System.out.println(data);
	   }
	  
	 
	   public void addKeyDOToList()
	   {
			objKeyDO = new KeyDO();
			int eventType = 0;
			boolean boolLoop = true;
			boolean boolNode = false;
			
			boolNode = getAttributeValue("for").equals("node");
				
	   	 	for (int i=0; i< xmlReader.getAttributeCount(); i++)
	   		{
	   			if (xmlReader.getAttributeLocalName(i).equals("id"))
	   			{
	   				objKeyDO.setId(xmlReader.getAttributeValue(i));
	   			}
	   			if (xmlReader.getAttributeLocalName(i).equals("attr.name"))
	   			{
	   				objKeyDO.setAttr_name(xmlReader.getAttributeValue(i));
	   			}
	   			if (xmlReader.getAttributeLocalName(i).equals("attr.type"))
	   			{
	   				objKeyDO.setattr_type(xmlReader.getAttributeValue(i));
	   			}
	   		
	   		}
		   	 
	   	 	try
	   	 	{
	   	 		while (boolLoop)
	   	 		{
	   	 			if (xmlReader.hasNext())
	   	 			{
	   	 				eventType = xmlReader.next();
	   	 				if (eventType == XMLEvent.START_ELEMENT)
	   	 				{
	   	 					if (xmlReader.getLocalName().equals("default"))
	   	 						objKeyDO.setAttr_value(getElementText("default"));
	   	 				}
	   	 				else if (eventType == XMLEvent.END_ELEMENT)
	   	 				{
	   	 					if (xmlReader.getLocalName().equals("key"))
	   	 						boolLoop = false;
	   	 				}
	   	 			}
	   	 			else
	   	 			{
	   	 				return;
	   	 			}
	   	 		}
	   	 	}
	   	 	catch(XMLStreamException ese)
	   	 	{}

	   	 	if (boolNode)
	   	 		keyNodeList.add(objKeyDO);
	   	 	else
	   	 		keyEdgeList.add(objKeyDO);
	       	 	
	        }
	   
	   public String getAttributeValue(String attrName)
	   {
	   	String attrValue = null;
	   	for(int i=0;i<xmlReader.getAttributeCount();i++)
	   	{
	   		if (xmlReader.getAttributeLocalName(i).equals(attrName))
	   			attrValue = xmlReader.getAttributeValue(i);
	   	}
	   	
	   	return attrValue;
	   }
	           


	
	 /**
	    * Creates a temporary file for the NWB file
	    * @return The temporary file
	  */
	  public File getTempFile(String fileName){
			File tempFile = null;
			String fullFileName = tempDir.toString()+ File.separator + fileName + ".nwb";
			
			if(!tempDir.exists())
				tempDir.mkdir();
			try{
				
				tempFile = new File (fullFileName);
			
			}catch (Exception e){
				e.printStackTrace();
			}
			
			return tempFile;
		}
}
