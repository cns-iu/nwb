package edu.iu.epic.modeling.compartment.converters.xml.in;

public class UnrecognizedXMLElementException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnrecognizedXMLElementException(String xmlElementLocalName) {
		super("Unrecognize XML element '" + xmlElementLocalName + "'.");
	}	
}
