package it.unipr.ce.dsg.deus.automator;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * 
 * @author Stefano Sebastio (stefano.sebastio@imtlucca.it) [only Runner refactoring]
 * 
 */
public class RunnerCommandLine extends Runner {


	public RunnerCommandLine(String originalXml, String automatorXml) {
		super(originalXml, automatorXml);
	}


	/**
	 * This method starts Deus
	 * 
	 * @param originalXml
	 *            , name of the base configuration file
	 * @param automatorXml
	 *            , name of the file that automates the simulations
	 * @throws ParserConfigurationException
	 */
	public int start(String originalXml, String automatorXml)
			throws DeusAutomatorException, JAXBException, SAXException,
			IOException, ParserConfigurationException {

		int result = super.start(originalXml, automatorXml);

		runSimulations();
		return result;
	}

}