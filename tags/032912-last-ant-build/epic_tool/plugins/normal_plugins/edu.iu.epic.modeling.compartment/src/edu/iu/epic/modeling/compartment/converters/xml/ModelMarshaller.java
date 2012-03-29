package edu.iu.epic.modeling.compartment.converters.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.cishell.utilities.FileUtilities;
import org.xml.sax.SAXException;

import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.Compartments;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.Compartments.Compartment;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.Infections;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.Infections.Infection;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.Infections.Infection.Infector;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.RatioTransitions;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.RatioTransitions.RatioTransition;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.Variables;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.Variables.Variable;
import edu.iu.epic.modeling.compartment.converters.xml.generated.ObjectFactory;
import edu.iu.epic.modeling.compartment.converters.xml.in.ModelReaderAlgorithm;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.Transition;
import edu.iu.epic.modeling.compartment.model.exception.ModelModificationException;
public final class ModelMarshaller {
	public static final String XML_SCHEMA_RESOURCE_PATH =
		"/edu/iu/epic/modeling/compartment/converters/xml/compartmentalModel.xsd";

	private ModelMarshaller() {
		// Static methods only.
	}
	
	
	public static File marshalModelToFile(Model model, boolean isValidating)
			throws JAXBException, IOException, SAXException, UnknownTransitionTypeException {		
		CompartmentalModel xModel = convertModelToJAXBModel(model);	
		
		return marshalJAXBModelToFile(isValidating, xModel);
	}


	private static CompartmentalModel convertModelToJAXBModel(Model model)
			throws UnknownTransitionTypeException {
		ObjectFactory objectFactory = new ObjectFactory();
		CompartmentalModel xModel = objectFactory.createCompartmentalModel();
		
		// Convert compartments.
		Compartments xCompartments = objectFactory.createCompartmentalModelCompartments();
		xModel.setCompartments(xCompartments);
		
		for (edu.iu.epic.modeling.compartment.model.Compartment c : model.getCompartments()) {
			Compartment xCompartment =
				objectFactory.createCompartmentalModelCompartmentsCompartment();
			xCompartment.setId(c.getName());
			xCompartment.setX(c.getPosition().getX());
			xCompartment.setY(c.getPosition().getY());
			xCompartment.setIsSecondary(c.isSecondary());
			
			xCompartments.getCompartment().add(xCompartment);
		}
		
		// Convert transitions.
		RatioTransitions xRatioTransitions =
			objectFactory.createCompartmentalModelRatioTransitions();
		xModel.setRatioTransitions(xRatioTransitions);
		Infections xInfections =
			objectFactory.createCompartmentalModelInfections();
		xModel.setInfections(xInfections);
		
		for (edu.iu.epic.modeling.compartment.model.Transition t : model.getTransitions()) {
			if (t instanceof edu.iu.epic.modeling.compartment.model.RatioTransition) {
				edu.iu.epic.modeling.compartment.model.RatioTransition rt =
					(edu.iu.epic.modeling.compartment.model.RatioTransition) t;
				RatioTransition xRatioTransition =
					objectFactory.createCompartmentalModelRatioTransitionsRatioTransition();
				xRatioTransition.setSource(rt.getSource().getName());
				xRatioTransition.setTarget(rt.getTarget().getName());
				xRatioTransition.setRatio(rt.getRatio());
				
				xRatioTransitions.getRatioTransition().add(xRatioTransition);
			} else if (t instanceof edu.iu.epic.modeling.compartment.model.InfectionTransition) {
				/* TODO We could glob multiple infectors into one infection wherever 
				 * sources and targets are shared, if desired.
				 */
				edu.iu.epic.modeling.compartment.model.InfectionTransition it =
					(edu.iu.epic.modeling.compartment.model.InfectionTransition) t;
				Infection xInfection =
					objectFactory.createCompartmentalModelInfectionsInfection();
				xInfection.setSource(it.getSource().getName());
				xInfection.setTarget(it.getTarget().getName());
				
				Infector xInfector =
					objectFactory.createCompartmentalModelInfectionsInfectionInfector();
				xInfector.setSource(it.getInfector().getName());
				xInfector.setRatio(it.getRatio());
				
				xInfection.getInfector().add(xInfector);
				
				xInfections.getInfection().add(xInfection);
			} else {
				throw new UnknownTransitionTypeException(t);
			}
		}		
		
		// Convert variables.
		Variables xVariables = objectFactory.createCompartmentalModelVariables();
		xModel.setVariables(xVariables);
		for (Entry<String, String> p : model.getParameterDefinitions().entrySet()) {
			Variable xVariable = objectFactory.createCompartmentalModelVariablesVariable();
			xVariable.setName(p.getKey());
			xVariable.setValue(p.getValue());
			
			xModel.getVariables().getVariable().add(xVariable);
		}
		
		return xModel;
	}


	private static File marshalJAXBModelToFile(boolean isValidating, CompartmentalModel xModel)
			throws JAXBException, SAXException, IOException {
		JAXBContext jaxbContext =
			JAXBContext.newInstance(
					CompartmentalModel.class.getPackage().getName(),
					CompartmentalModel.class.getClassLoader());
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output", true);
		if (isValidating) {
			marshaller.setSchema(createCompartmentalModelSchema());
		}
		File outputFile = File.createTempFile("marshalledModel", ".mdl");
		FileWriter outWriter = new FileWriter(outputFile);
		marshaller.marshal(xModel, outWriter);
		
		return outputFile;
	}

	protected static Schema createCompartmentalModelSchema() throws SAXException {
		SchemaFactory schemaFactory =
			SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
		InputStream schemaStream =
			ModelMarshaller.class.getResourceAsStream(XML_SCHEMA_RESOURCE_PATH);
		Schema schema = schemaFactory.newSchema(new StreamSource(schemaStream));
		
		System.out.println("The schema is " + schema.toString());
		
		return schema;
	}
	

	public static void main(String[] args) {
		try {
			File testFile =
				FileUtilities.loadFileFromClassPath(
						ModelReaderAlgorithm.class,
						ModelReaderAlgorithm.TEST_FILE_PATH);
			Model model = ModelUnmarshaller.unmarshalModelFrom(testFile, true);
			System.out.println("Unmarshalled model:");
			System.out.println(model);
			
			System.out.println("\n");
			
			
			try {
				File xmlFile = marshalModelToFile(model, true);
				
				BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
				
				System.out.println("Marshalled XML:");
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					System.out.println(line);
				}
				
				
				Model model2 = ModelUnmarshaller.unmarshalModelFrom(xmlFile, true);
				System.out.println("Unmarshalled model2:");
				System.out.println(model2);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownTransitionTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ModelModificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static class UnknownTransitionTypeException extends Exception {
		private static final long serialVersionUID = 1L;
		
		public UnknownTransitionTypeException(Transition transition) {
			super("Unrecognized transition type for transition '" +  transition + "'.");
		}				
	}
}
