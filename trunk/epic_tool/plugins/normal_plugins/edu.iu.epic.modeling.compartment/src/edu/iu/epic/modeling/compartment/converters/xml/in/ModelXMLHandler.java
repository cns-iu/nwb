package edu.iu.epic.modeling.compartment.converters.xml.in;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.iu.epic.modeling.compartment.model.Compartment;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.exception.ModelModificationException;

public class ModelXMLHandler extends DefaultHandler {
	/* Not ignored recursively.  This simply means that we do no processing when we encounter
	 * any of these start elements.
	 */
	public static final Collection<String> NAMES_OF_IGNORED_ELEMENTS;
	static {
		Collection<String> c = new HashSet<String>();
		c.add("compartmentalModel");
		c.add("compartments");
		c.add("ratioTransitions");
		c.add("infections");
		c.add("variables");	
		NAMES_OF_IGNORED_ELEMENTS = Collections.unmodifiableCollection(c);
	}
	
	private Model model;
	private String lastSeenInfectionSource;
	private String lastSeenInfectionTarget;

	public ModelXMLHandler() {
		model = new Model();		
		lastSeenInfectionSource = null;
		lastSeenInfectionTarget = null;
	}
	
	@Override
	public void startElement(
			String uri, String localName, String qualifiedName, Attributes attributes)
				throws SAXException {
		try {
			// Note all other elements are simply skipped.
			if ("compartment".equals(localName)) {
				String id = attributes.getValue("id");
				
				// TODO Only INFECTED for now.
				model.addCompartment(id, Compartment.Type.INFECTED);
			} else if ("ratioTransition".equals(localName)) {
				Compartment source = model.getCompartment(attributes.getValue("source"));
				Compartment target = model.getCompartment(attributes.getValue("target"));
				String ratio = attributes.getValue("ratio");
				
				// TODO Only non-secondary for now.
				model.addRatioTransition(source, target, ratio, false);
			} else if ("infection".equals(localName)) {
				lastSeenInfectionSource = attributes.getValue("source");
				lastSeenInfectionTarget = attributes.getValue("target");
				
				/* We don't add anything to the model yet;
				 * this information is just retained for the parsing child "infector" element.
				 */
			} else if ("infector".equals(localName)) {
				Compartment source = model.getCompartment(lastSeenInfectionSource);
				Compartment infector = model.getCompartment(attributes.getValue("source"));
				Compartment target = model.getCompartment(lastSeenInfectionTarget);
				String ratio = attributes.getValue("ratio");
				
				// TODO Quadruple-check that this is the correct sequence of compartments!
				// TODO Only non-secondary for now.
				model.addInfectionTransition(source, infector, target, ratio, false);
			} else if ("variable".equals(localName)) {
				String name = attributes.getValue("name");
				String value = attributes.getValue("value");
				
				model.setParameterDefinition(name, value);
			} else {
				if (!NAMES_OF_IGNORED_ELEMENTS.contains(localName)) {
					throw new UnrecognizedXMLElementException(localName);
				}
			}
		} catch (ModelModificationException e) {
			throw new SAXException(e);
		} catch (UnrecognizedXMLElementException e) {
			throw new SAXException(e);
		}
	}
	
	public Model getModel() {
		return model;
	}
}
