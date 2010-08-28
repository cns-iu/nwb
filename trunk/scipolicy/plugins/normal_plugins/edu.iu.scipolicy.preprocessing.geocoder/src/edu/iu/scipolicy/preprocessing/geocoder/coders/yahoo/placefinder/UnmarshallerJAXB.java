package edu.iu.scipolicy.preprocessing.geocoder.coders.yahoo.placefinder;

import java.io.Reader;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import edu.iu.scipolicy.preprocessing.geocoder.coders.yahoo.placefinder.beans.ResultSet;

/**
 * 
 * Standard unmarshall configuration. This can be reused to load. 
 * any W3C XML schema 1.0
 * @author kongch
 *
 */
public final class UnmarshallerJAXB {
	private Unmarshaller unmarshaller;
	
	private UnmarshallerJAXB(URL schemaURL, String packageNameSpace) 
									throws JAXBException, SAXException {
		
		/* create JAXB context that contains the ResultSet.java (JAVA Object) */
		JAXBContext jc = JAXBContext.newInstance(packageNameSpace);
		this.unmarshaller = jc.createUnmarshaller();
		
		/* load schema */
		unmarshaller.setSchema(createSchema(schemaURL));
	}
	
	public static UnmarshallerJAXB newInstance(URL schemaURL, String packageNameSpace) {
		UnmarshallerJAXB unmarshallerJAXB = null;
		
		try {
			unmarshallerJAXB = new UnmarshallerJAXB(schemaURL, packageNameSpace);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		return unmarshallerJAXB;
	}
	
	private static Schema createSchema(URL url) throws SAXException {
		/* Obtain schema URL from class loader */
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		return schemaFactory.newSchema(url);
	}
	
	/**
	 *  unmarshal XML schema to the related Java Object - ResultSet.
	 * @throws JAXBException 
	 */
	public ResultSet unmarshal(Reader reader) throws JAXBException {
		return (ResultSet) unmarshaller.unmarshal(reader);
	}
}
