package edu.iu.epic.modeling.compartment.converters.xml;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXException;

import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.Compartments.Compartment;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.Infections.Infection;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.Infections.Infection.Infector;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.RatioTransitions.RatioTransition;
import edu.iu.epic.modeling.compartment.converters.xml.generated.CompartmentalModel.Variables.Variable;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.exception.CompartmentExistsException;
import edu.iu.epic.modeling.compartment.model.exception.InvalidCompartmentNameException;
import edu.iu.epic.modeling.compartment.model.exception.InvalidParameterExpressionException;
import edu.iu.epic.modeling.compartment.model.exception.InvalidParameterNameException;
import edu.iu.epic.modeling.compartment.model.exception.ModelModificationException;
import edu.iu.epic.modeling.compartment.model.exception.ParameterAlreadyDefinedException;

public class ModelUnmarshaller {
	public static Model unmarshalModelFrom(File file, boolean isValidating)
			throws ModelModificationException, JAXBException, SAXException, IOException {
		CompartmentalModel xModel = unmarshalJAXBModelFromFile(file, isValidating);

		return convertJAXBModelToModel(xModel);
	}

	private static CompartmentalModel unmarshalJAXBModelFromFile(File file, boolean isValidating)
			throws JAXBException, SAXException, IOException {
		JAXBContext jaxbContext =
			JAXBContext.newInstance(
					CompartmentalModel.class.getPackage().getName(),
					CompartmentalModel.class.getClassLoader());
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		FileInputStream inputStream = new FileInputStream(file);
		if (isValidating) {
			unmarshaller.setSchema(ModelMarshaller.createCompartmentalModelSchema());
		}
		CompartmentalModel xModel = (CompartmentalModel) unmarshaller.unmarshal(inputStream);

		return xModel;
	}

	private static Model convertJAXBModelToModel(CompartmentalModel xModel)
			throws CompartmentExistsException, InvalidCompartmentNameException,
			InvalidParameterExpressionException, InvalidParameterNameException,
			ParameterAlreadyDefinedException {
		Model model = new Model();

		for (Compartment xCompartment : xModel.getCompartments().getCompartment()) {
			Point2D position = new Point.Double(xCompartment.getX(), xCompartment.getY());

			edu.iu.epic.modeling.compartment.model.Compartment compartment =
				model.addCompartment(xCompartment.getId(), position);
			
			compartment.setSecondary(xCompartment.isIsSecondary());
		}

		for (RatioTransition xRatioTransition : xModel.getRatioTransitions().getRatioTransition()) {
			model.addRatioTransition(model.getOrAddCompartment(xRatioTransition.getSource()), model
					.getOrAddCompartment(xRatioTransition.getTarget()),
					xRatioTransition.getRatio());
		}

		for (Infection xInfection : xModel.getInfections().getInfection()) {
			for (Infector xInfector : xInfection.getInfector()) {
				// TODO Quadruple-check the infector order here.
				model.addInfectionTransition(model.getOrAddCompartment(xInfection.getSource()),
						model.getOrAddCompartment(xInfector.getSource()), model
								.getOrAddCompartment(xInfection.getTarget()), xInfector.getRatio());
			}
		}

		for (Variable xVariable : xModel.getVariables().getVariable()) {
			model.setParameterDefinition(xVariable.getName(), xVariable.getValue());
		}

		return model;
	}
}
