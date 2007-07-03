package edu.iu.nwb.converter.nwbgraphml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

/**
 * Converts from GraphML to NWB file format via the Stax libraries
 * @author Megha 
 */

public class GraphMLToNWBbyStax implements Algorithm {

	Data[] data;
	Dictionary parameters;
	CIShellContext ciContext;
	LogService logger;



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
	} 

	public Data[] execute(){

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xmlReader;

		try{
			xmlReader = inputFactory.createXMLStreamReader(new FileInputStream((File) data[0].getData()));
		}
		catch(XMLStreamException ex)
		{
			logger.log(LogService.LOG_ERROR, "Unable to open XML Stream", ex);
			ex.printStackTrace();
			return null;
		}
		catch(FileNotFoundException foe)
		{
			logger.log(LogService.LOG_ERROR, "GraphML file not found ", foe);
			foe.printStackTrace();
			return null;
		}



		File outData;
		try {
			outData = this.convert(xmlReader);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(LogService.LOG_ERROR, "Unable to convert graphml to NWB", e);
			return null;
		}

		return new Data[] {new BasicData(outData, "file:text/nwb")};
	}

	protected String tmpFileLocation(String base, String extension) {

		String tmpRoot = System.getProperty("java.io.tmpdir") + File.separator + "temp";

		File tmpDir = new File(tmpRoot);

		if(!tmpDir.exists()) {
			tmpDir.mkdir();
		}
		return tmpRoot + File.separator + base + System.currentTimeMillis() + "." + extension;
	}

	protected File convert(XMLStreamReader xmlReader) throws XMLStreamException, IOException
	{
		boolean directed = false;
		int nodeCount = 0;
		int directedEdgeCount = 0;
		int undirectedEdgeCount = 0;

		String labelKey = null;
		String weightKey = null;

		Map nodeIds = new Hashtable();
		List nodeAttributes = new ArrayList();
		List edgeAttributes = new ArrayList();

		String nodeFileLocation = tmpFileLocation("node", "nwb");
		String undirectedEdgeFileLocation = tmpFileLocation("undirected", "nwb");
		String directedEdgeFileLocation = tmpFileLocation("directed", "nwb");

		BufferedWriter nodeWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nodeFileLocation), "UTF-8"));
		BufferedWriter undirectedEdgeWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(undirectedEdgeFileLocation), "UTF-8"));
		BufferedWriter directedEdgeWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directedEdgeFileLocation), "UTF-8"));

		while (xmlReader.hasNext())
		{   
			int eventType = xmlReader.next();
			if (eventType == XMLEvent.START_ELEMENT){
				//check for graph element
				if (xmlReader.getLocalName().equals("graph")&& xmlReader.getAttributeCount() > 0)
				{
					if ("directed".equals(xmlReader.getAttributeValue(null, "edgedefault"))) {
						directed = true;
					}
				}

				//check for node element
				if (xmlReader.getLocalName().equals("node")&& xmlReader.getAttributeCount() > 0)
				{
					nodeCount++;
					nodeIds.put( xmlReader.getAttributeValue(null, "id"), Integer.valueOf(nodeCount));
					if (nodeCount == 1) {
						nodeWriter.write(createNodeHeader(nodeAttributes));
					}


					nodeWriter.write(createNode(nodeCount, labelKey, extractAttributes(xmlReader, "node"), nodeAttributes));
				}

				// check for edge element
				if (xmlReader.getLocalName().equals("edge")&& xmlReader.getAttributeCount() > 0)
				{
					boolean isDirected = isDirectedEdge(directed, xmlReader);
					Integer source = (Integer) nodeIds.get(xmlReader.getAttributeValue(null, "source"));
					Integer target = (Integer) nodeIds.get(xmlReader.getAttributeValue(null, "target"));
					Map attributeValues = extractAttributes(xmlReader, "edge");
					if(isDirected)
					{
						directedEdgeCount++;
						if(directedEdgeCount==1) {
							directedEdgeWriter.write(createDirectedEdgeHeader(edgeAttributes));
						}


						directedEdgeWriter.write(createEdge(source.intValue(), target.intValue(), weightKey, attributeValues, edgeAttributes));
					}else
					{
						undirectedEdgeCount++;
						if(undirectedEdgeCount==1) {
							undirectedEdgeWriter.write(createUndirectedEdgeHeader(edgeAttributes));
						}
						undirectedEdgeWriter.write(createEdge(source.intValue(), target.intValue(), weightKey,  attributeValues, edgeAttributes));
					}
				}

				//check for key element
				if (xmlReader.getLocalName().equals("key")&& xmlReader.getAttributeCount() > 0) {
					Attribute attribute = readAttribute(xmlReader);
					if(!attribute.isReservedForNode() && attribute.isForNode()) {
						nodeAttributes.add(attribute);
					} 
					if(!attribute.isReservedForEdge() && attribute.isForEdge()) {
						edgeAttributes.add(attribute);
					}

					if(attribute.isForNode() && attribute.isForLabel()) {
						labelKey = attribute.getId();
					}
					if(attribute.isForEdge() && attribute.isForWeight()) {
						weightKey = attribute.getId();
					}
				}


			}
		}

		xmlReader.close();
		directedEdgeWriter.close();
		undirectedEdgeWriter.close();
		nodeWriter.close();


		File directedEdgeFile = new File(directedEdgeFileLocation);

		File undirectedEdgeFile = new File(undirectedEdgeFileLocation);


		File nodeFile = new File(nodeFileLocation);

		File output = mergeFiles(nodeFile, undirectedEdgeFile, directedEdgeFile);

		directedEdgeFile.delete();

		undirectedEdgeFile.delete();

		return output;


	}
	protected File mergeFiles(File nodeFile, File undirectedEdgeFile, File directedEdgeFile) throws IOException {

		FileOutputStream nodeStream = new FileOutputStream(nodeFile, true);

		//nodeStream.write('\n');
		//no need to insert extra newlines because every line must end with a newline

		SequenceInputStream edgeStream = new SequenceInputStream(new FileInputStream(undirectedEdgeFile), new FileInputStream(directedEdgeFile));

		int c;

		while((c = edgeStream.read()) != -1) {
			nodeStream.write(c);
		}

		return nodeFile;
	}

	protected String createNode(int id, String labelKey, Map attributeValues, List nodeAttributes) throws XMLStreamException {
		return "" + id + "\t" + nodeAttributes(id, labelKey, attributeValues, nodeAttributes) + "\n";
	}

	protected String createUndirectedEdgeHeader(List edgeAttributes) {
		return "*UndirectedEdges\nsource*int target*int weight*float" + attributesHeader(edgeAttributes) + "\n";
	}

	protected String createDirectedEdgeHeader(List edgeAttributes) {
		return "*DirectedEdges\nsource*int target*int weight*float" + attributesHeader(edgeAttributes) + "\n";
	}

	protected String createEdge(int source, int target, String weightKey, Map attributeValues, List edgeAttributes) throws XMLStreamException {
		return "" + source + "\t" + target + "\t" + edgeAttributes(weightKey, attributeValues, edgeAttributes) + "\n";
	}

	protected Attribute readAttribute(XMLStreamReader xmlReader) throws XMLStreamException {
		Attribute attribute = new Attribute();
		int eventType;


		attribute.setDomain(xmlReader.getAttributeValue(null, "for"));
		attribute.setId(xmlReader.getAttributeValue(null, "id"));
		attribute.setName(xmlReader.getAttributeValue(null, "attr.name"));
		attribute.setType(xmlReader.getAttributeValue(null, "attr.type"));

		while (xmlReader.hasNext())
		{

			eventType = xmlReader.next();
			if (eventType == XMLEvent.START_ELEMENT)
			{
				if (xmlReader.getLocalName().equals("default")) {
					attribute.setDefault(getElementText(xmlReader));
				}
			}
			else if (eventType == XMLEvent.END_ELEMENT)
			{
				break;
			}
		}

		return attribute;
	}

	public boolean isDirectedEdge(boolean defaultValue, XMLStreamReader xmlReader)
	{
		String attributeValue = xmlReader.getAttributeValue(null, "directed");
		if(defaultValue) {
			return !"false".equals(attributeValue);
		} else {
			return "true".equals(attributeValue);
		}
	}




	public String createNodeHeader(List nodeAttributes)
	{
		return "*Nodes\nid*int label*string " + attributesHeader(nodeAttributes) + "\n";
	}

	public String attributesHeader(List attributesList) {
		StringBuffer header = new StringBuffer();
		Iterator attributes = attributesList.iterator();
		while(attributes.hasNext()) {
			Attribute attribute = (Attribute) attributes.next();

			header.append(" ");
			header.append(attribute.getName());
			header.append("*");
			header.append(attribute.getType());

		}
		return header.toString();
	}

	protected String nodeAttributes(int id, String labelKey, Map attributeValues, List nodeAttributes) throws XMLStreamException {
		String label = "" + id;

		if(labelKey != null && attributeValues.containsKey(labelKey)) {
			label = formatString((String) attributeValues.get(labelKey));
		}

		String value = attributesString(nodeAttributes, attributeValues);


		return "\"" + label + "\"" + value.toString();
	}

	protected String formatString(String string) {
		return string.replaceAll("\"", "");
	}

	protected Map extractAttributes(XMLStreamReader xmlReader, String endElement) throws XMLStreamException {
		int eventType;
		Map attributeValues = new Hashtable();

		while (xmlReader.hasNext())
		{
			eventType = xmlReader.next();
			if (eventType == XMLEvent.START_ELEMENT)
			{
				if (xmlReader.getLocalName().equals("data")) {
					attributeValues.put(xmlReader.getAttributeValue(null, "key"), getElementText(xmlReader));
				}
			}
			else if (eventType == XMLEvent.END_ELEMENT)
			{
				if(xmlReader.getLocalName().equals(endElement)) {
					break;
				}
			}
		}
		return attributeValues;
	}

	protected String edgeAttributes(String weightKey, Map attributeValues, List edgeAttributes) throws XMLStreamException {

		String weight = "" + 1;

		if(weightKey != null && attributeValues.containsKey(weightKey)) {
			weight = (String) attributeValues.get(weightKey);
			if("null".equals(weight)) {
				weight = "*";
			}
		}

		String value = attributesString(edgeAttributes, attributeValues);


		return weight + value;
	}

	protected String attributesString(List attributeTypes, Map attributeValues) {
		StringBuffer value = new StringBuffer();

		Iterator attributes = attributeTypes.iterator();
		while(attributes.hasNext()) {
			Attribute attribute = (Attribute) attributes.next();

			value.append(' ');
			if(attributeValues.containsKey(attribute.getId())) {
				String attributeValue = (String) attributeValues.get(attribute.getId());
				if(attribute.isString()) {
					value.append('"');
					value.append(formatString(attributeValue));
					value.append('"');
				} else {
					if("null".equals(attributeValue)) {
						value.append('*');
					} else {
						value.append(attributeValue);
					}
				}
			} else if(attribute.hasDefault()) {
				String attributeValue = attribute.getDefault();
				if(attribute.isString()) {
					value.append('"');
					value.append(formatString(attributeValue));
					value.append('"');
				} else {
					if("null".equals(attributeValue)) {
						value.append('*');
					} else {
						value.append(attributeValue);
					}
				}
			} else {
				value.append('*');
			}

		}
		return value.toString();
	}


	protected String getElementText(XMLStreamReader xmlReader) throws XMLStreamException
	{
		int eventType;

		StringBuffer value = new StringBuffer();

		while (xmlReader.hasNext())
		{
			eventType = xmlReader.next();


			if (eventType == XMLEvent.CHARACTERS)
			{

				value.append(xmlReader.getText());

			}
			else if (eventType == XMLEvent.END_ELEMENT)
			{
				break;
			}
		}
		return  value.toString();
	}
}
