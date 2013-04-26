package edu.iu.sci2.preprocessing.geocoder.coders.bing.placefinder;

import java.io.Reader;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import edu.iu.sci2.preprocessing.geocoder.coders.bing.placefinder.beans.Response;

/**
 * 
 * Standard unmarshall configuration. This can be reused to load. 
 * any W3C XML schema 1.0
 * @author kongch
 *
 */
public final class UnmarshallerJAXB {
	private Unmarshaller unmarshaller;
	
	private UnmarshallerJAXB(URL schemaURL) 
									throws JAXBException, SAXException {
		
		/* create JAXB context that contains the ResultSet.java (JAVA Object) */
		JAXBContext jc =
			JAXBContext.newInstance(
					Response.class.getPackage().getName(),
					Response.class.getClassLoader());
		this.unmarshaller = jc.createUnmarshaller();
		
		/* load schema */
		unmarshaller.setSchema(createSchema(schemaURL));
	}
	
	public static UnmarshallerJAXB newInstance(URL schemaURL) {
		UnmarshallerJAXB unmarshallerJAXB = null;
		
		try {
			unmarshallerJAXB = new UnmarshallerJAXB(schemaURL);
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
	public Response unmarshal(Reader reader) throws JAXBException {
		JAXBElement<Response> resultSetElement =
			unmarshaller.unmarshal(new StreamSource(reader), Response.class);
		
		return resultSetElement.getValue();
	}
}
