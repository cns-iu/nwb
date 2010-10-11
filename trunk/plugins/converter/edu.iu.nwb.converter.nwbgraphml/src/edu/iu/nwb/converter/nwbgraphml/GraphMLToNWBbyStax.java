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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;

import edu.iu.nwb.util.nwbfile.NWBFileProperty;

/**
 * Converts from GraphML to NWB file format via the Stax libraries
 * @author Megha 
 */

public class GraphMLToNWBbyStax implements Algorithm {
	private File inGraphMLFile;


	public GraphMLToNWBbyStax(Data[] data) {
		inGraphMLFile = (File) data[0].getData();
	} 

	public Data[] execute() throws AlgorithmExecutionException {
		File outNWBFile = createOutNWBFile(inGraphMLFile);
		
		return new Data[] { new BasicData( outNWBFile, NWBFileProperty.NWB_MIME_TYPE) };
	}

	private File createOutNWBFile(File graphMLFile) throws AlgorithmExecutionException {
		File outData = null;
		
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xmlReader = null;		
		try {
			try {
				xmlReader =
					inputFactory.createXMLStreamReader(
							new FileInputStream(graphMLFile));
			} catch (XMLStreamException e) {
				String message =
					"Error parsing GraphML file: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			} catch (FileNotFoundException e) {
				String message =
					"Error: Couldn't find GraphML file: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			}

			try {
				outData = this.convert(xmlReader);
			} catch (XMLStreamException e) {
				String message =
					"Error parsing GraphML file: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			} catch (IOException e) {
				String message =
					"File access error: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			}
		} finally {
			if (xmlReader != null) {
				try {
					xmlReader.close();
				} catch (XMLStreamException e) {
					String message =
						"Error: Unable to close XML Stream: " + e.getMessage();
					throw new AlgorithmExecutionException(message, e);
				}
			}
		}

		return outData;
	}

	protected File convert(XMLStreamReader xmlReader)
			throws XMLStreamException, IOException {
		boolean directed = false;
		int nodeCount = 0;
		int directedEdgeCount = 0;
		int undirectedEdgeCount = 0;

		String labelKey = null;

		Map<String, Integer> nodeIds = new Hashtable<String, Integer>();
		List<Attribute> nodeAttributes = new ArrayList<Attribute>();
		List<Attribute> edgeAttributes = new ArrayList<Attribute>();

		File nodeFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
					"NWB-node-", "nwb");
		BufferedWriter nodeWriter =
			new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(nodeFile), "UTF-8"));
		
		File undirectedEdgeFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
					"NWB-undirected-", "nwb");
		BufferedWriter undirectedEdgeWriter =
			new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(undirectedEdgeFile), "UTF-8"));
		
		File directedEdgeFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
					"NWB-directed-", "nwb");
		BufferedWriter directedEdgeWriter =
			new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(directedEdgeFile), "UTF-8"));
		
		boolean wroteNodeHeader = false;
		boolean wroteEdgeHeader = false;

		/* TODO All of these magic strings should refer to fields common with
		 * the NWBToGraphML classes
		 */
		while (xmlReader.hasNext())	{   
			int eventType = xmlReader.next();
			if (eventType == XMLStreamConstants.START_ELEMENT) {
				//check for graph element
				if (xmlReader.getLocalName().equals("graph")
						&& xmlReader.getAttributeCount() > 0) {
					if ("directed".equals(xmlReader.getAttributeValue(null, "edgedefault"))) {
						directed = true;
					}
				}

				// Check for node element
				if (xmlReader.getLocalName().equals("node")&& xmlReader.getAttributeCount() > 0)
				{
					nodeCount++;
					nodeIds.put( xmlReader.getAttributeValue(null, "id"), new Integer(nodeCount));
					if (nodeCount == 1) {
						nodeWriter.write(createNodeHeader(nodeAttributes));
						wroteNodeHeader = true;
					}


					nodeWriter.write(createNode(nodeCount, labelKey, extractAttributes(xmlReader, "node"), nodeAttributes));
				}

				// check for edge element
				if (xmlReader.getLocalName().equals("edge")&& xmlReader.getAttributeCount() > 0)
				{
					boolean isDirected = isDirectedEdge(directed, xmlReader);
					Integer source = nodeIds.get(xmlReader.getAttributeValue(null, "source"));
					Integer target = nodeIds.get(xmlReader.getAttributeValue(null, "target"));
					Map<String, String> attributeValues = extractAttributes(xmlReader, "edge");
					if(isDirected) {
						directedEdgeCount++;
						if(directedEdgeCount==1) {
							directedEdgeWriter.write(createDirectedEdgeHeader(edgeAttributes));
							wroteEdgeHeader = true;
						}
						
						directedEdgeWriter.write(createEdge(source.intValue(), target.intValue(), attributeValues, edgeAttributes));
					} else {
						undirectedEdgeCount++;
						if(undirectedEdgeCount==1) {
							undirectedEdgeWriter.write(createUndirectedEdgeHeader(edgeAttributes));
							wroteEdgeHeader = true;
						}
						
						undirectedEdgeWriter.write(createEdge(source.intValue(), target.intValue(), attributeValues, edgeAttributes));
					}
				}

				// Check for key element
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
				}
			}
		}
		
		if (! wroteNodeHeader) {
			nodeWriter.write(createNodeHeader(nodeAttributes));
		}
		if (! wroteEdgeHeader) {
			undirectedEdgeWriter.write(createUndirectedEdgeHeader(edgeAttributes));
		}

		xmlReader.close();
		directedEdgeWriter.close();
		undirectedEdgeWriter.close();
		nodeWriter.close();

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

	protected String createNode(int id, String labelKey, Map<String, String> attributeValues, List<Attribute> nodeAttributes) {
		return "" + id + "\t" + nodeAttributes(id, labelKey, attributeValues, nodeAttributes) + "\n";
	}

	protected String createUndirectedEdgeHeader(List<Attribute> edgeAttributes) {
		return "*UndirectedEdges\nsource*int target*int" + attributesHeader(edgeAttributes) + "\n";
	}

	protected String createDirectedEdgeHeader(List<Attribute> edgeAttributes) {
		return "*DirectedEdges\nsource*int target*int" + attributesHeader(edgeAttributes) + "\n";
	}

	protected String createEdge(int source, int target, Map<String, String> attributeValues, List<Attribute> edgeAttributes) {
		return "" + source + "\t" + target + "\t" + edgeAttributes(attributeValues, edgeAttributes) + "\n";
	}

	protected Attribute readAttribute(XMLStreamReader xmlReader) throws XMLStreamException {
		Attribute attribute = new Attribute();
		int eventType;

		attribute.setDomain(xmlReader.getAttributeValue(null, "for"));
		attribute.setId(xmlReader.getAttributeValue(null, "id"));
		attribute.setName(xmlReader.getAttributeValue(null, "attr.name"));
		attribute.setType(xmlReader.getAttributeValue(null, "attr.type"));

		while (xmlReader.hasNext()) {
			eventType = xmlReader.next();
			if (eventType == XMLStreamConstants.START_ELEMENT) {
				if (xmlReader.getLocalName().equals("default")) {
					attribute.setDefault(getElementText(xmlReader));
				}
			}
			else if (eventType == XMLStreamConstants.END_ELEMENT) {
				break;
			}
		}

		return attribute;
	}

	public boolean isDirectedEdge(boolean defaultValue, XMLStreamReader xmlReader) {
		String attributeValue = xmlReader.getAttributeValue(null, "directed");
		if(defaultValue) {
			return !"false".equals(attributeValue);
		} else {
			return "true".equals(attributeValue);
		}
	}

	public String createNodeHeader(List<Attribute> nodeAttributes) {
		return "*Nodes\nid*int label*string " + attributesHeader(nodeAttributes) + "\n";
	}

	public String attributesHeader(List<Attribute> attributesList) {
		StringBuffer header = new StringBuffer();
		Iterator<Attribute> attributes = attributesList.iterator();
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();

			header.append(" ");
			header.append(attribute.getName());
			header.append("*");
			header.append(attribute.getType());

		}
		
		return header.toString();
	}

	protected String nodeAttributes(
			int id, String labelKey, Map<String, String> attributeValues, List<Attribute> nodeAttributes) {
		String label = "" + id;

		if(labelKey != null && attributeValues.containsKey(labelKey)) {
			label = formatString(attributeValues.get(labelKey));
		}

		String value = attributesString(nodeAttributes, attributeValues);


		return "\"" + label + "\"" + value.toString();
	}

	protected String formatString(String string) {
		return string.replaceAll("\"", "");
	}

	protected Map<String, String> extractAttributes(XMLStreamReader xmlReader, String endElement) throws XMLStreamException {
		int eventType;
		Map<String, String> attributeValues = new Hashtable<String, String>();

		while (xmlReader.hasNext()) {
			eventType = xmlReader.next();
			if (eventType == XMLStreamConstants.START_ELEMENT) {
				if (xmlReader.getLocalName().equals("data")) {
					attributeValues.put(xmlReader.getAttributeValue(null, "key"), getElementText(xmlReader));
				}
			}
			else if (eventType == XMLStreamConstants.END_ELEMENT) {
				if(xmlReader.getLocalName().equals(endElement)) {
					break;
				}
			}
		}
		return attributeValues;
	}

	protected String edgeAttributes(Map<String, String> attributeValues, List<Attribute> edgeAttributes) {
		return attributesString(edgeAttributes, attributeValues);
	}

	protected String attributesString(List<Attribute> attributeTypes, Map<String, String> attributeValues) {
		StringBuffer value = new StringBuffer();

		Iterator<Attribute> attributes = attributeTypes.iterator();
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();

			value.append(' ');
			if(attributeValues.containsKey(attribute.getId())) {
				String attributeValue = attributeValues.get(attribute.getId());
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


	protected String getElementText(XMLStreamReader xmlReader) throws XMLStreamException {
		int eventType;

		StringBuffer value = new StringBuffer();

		while (xmlReader.hasNext()) {
			eventType = xmlReader.next();


			if (eventType == XMLStreamConstants.CHARACTERS) {

				value.append(xmlReader.getText());

			}
			else if (eventType == XMLStreamConstants.END_ELEMENT) {
				break;
			}
		}
		
		return value.toString();
	}
}
