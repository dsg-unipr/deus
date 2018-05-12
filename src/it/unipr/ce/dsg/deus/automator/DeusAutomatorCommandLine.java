package it.unipr.ce.dsg.deus.automator;

import it.unipr.ce.dsg.deus.automator.DeusSimulationPanelCommandLine;
import it.unipr.ce.dsg.deus.automator.gui.EngineParameter;
import it.unipr.ce.dsg.deus.automator.gui.GnuPlotFileElement;
import it.unipr.ce.dsg.deus.automator.gui.NodeParameter;
import it.unipr.ce.dsg.deus.automator.gui.NodeResource;
import it.unipr.ce.dsg.deus.automator.gui.ProcessParameter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * <p>
 * This class allows to start the Automator without opening its GUI.
 * </p>
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class DeusAutomatorCommandLine {

	public String originalXmlPath;
	public String outFileName;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length == 2) {

			DeusAutomatorCommandLine cmd = new DeusAutomatorCommandLine();

			cmd.originalXmlPath = args[0];
			cmd.outFileName = args[1];
			
			// Run saved file
			RunnerCommandLine runner = new RunnerCommandLine(
					cmd.originalXmlPath, cmd.outFileName);

			Thread automatorRunner = new Thread(runner,
					"Automator Thread Runner");
			automatorRunner.setPriority(10);
			automatorRunner.start();

			/*
			try {

				cmd.readXML(args[1]);

				cmd.writeAutomatorXML(cmd.outFileName);

				// Run saved file
				RunnerCommandLine runner = new RunnerCommandLine(
						cmd.originalXmlPath, cmd.outFileName);

				Thread automatorRunner = new Thread(runner,
						"Automator Thread Runner");
				automatorRunner.setPriority(10);
				automatorRunner.start();

			} catch (DeusAutomatorException e) {
				e.printStackTrace();
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
		} else
			System.err.println("Error in DeusAutomatorCommandLine Parameters ...");
	}

	private DeusSimulationPanelCommandLine deusSimulationPanel;
	private String simulationName = "Command Line Title";

	private void writeAutomatorXML(String fileName) throws IOException {
		String xmlString = "";

		xmlString = xmlString + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "\n\n";
		xmlString = xmlString
				+ "<deusAutomator xmlns=\"http://dsg.ce.unipr.it/software/deus/schema/deusAutomator\""
				+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
				+ " xsi:schemaLocation=\"http://dsg.ce.unipr.it/software/deus/schema/deusAutomator ../../schema/automator/deusAutomator.xsd\">"
				+ "\n\n";

		xmlString = xmlString
				+ deusSimulationPanel.createSimulationXML(simulationName, 0)
				+ "\n";

		xmlString = xmlString + "</deusAutomator>";

		FileOutputStream fos = new FileOutputStream(fileName);

		fos.write(xmlString.getBytes());
	}

	/**
	 * Reads the XML file for the automation of simulations
	 * 
	 * @param path
	 *            (of the XML file)
	 * @return an ArrayList<> that contains all the simulations to be executed
	 * @throws DeusAutomatorException
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void readXML(String path) throws DeusAutomatorException,
			JAXBException, SAXException, IOException {

		JAXBContext jc = JAXBContext
				.newInstance("it.unipr.ce.dsg.deus.schema.automator");
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = schemaFactory.newSchema(new File(
				"schema/automator/deusAutomator.xsd"));

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		unmarshaller.setSchema(schema);
		unmarshaller.setEventHandler(new ValidationEventHandler() {

			public boolean handleEvent(ValidationEvent ve) {
				if (ve.getSeverity() == ValidationEvent.FATAL_ERROR
						|| ve.getSeverity() == ValidationEvent.ERROR
						|| ve.getSeverity() == ValidationEvent.WARNING) {
					ValidationEventLocator locator = ve.getLocator();
					System.out.println("Invalid configuration file: "
							+ locator.getURL());
					System.out.println("Error at column "
							+ locator.getColumnNumber() + ", line "
							+ locator.getLineNumber());
					System.out.println("Error: " + ve.getMessage());
					return false;
				}
				return true;
			}

		});

		unmarshaller.unmarshal(new File(path));

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();

			File f = new File(path);

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(f);

			document.getDocumentElement().normalize();

			// Root element
			NodeList simulationLst = document
					.getElementsByTagName("simulation");

			// list of simulations
			for (int w = 0; w < simulationLst.getLength(); w++) {

				// DeusSimulationPanel on which values read from file are added
				deusSimulationPanel = null;

				Node fstSimulation = simulationLst.item(w);

				if (fstSimulation.getAttributes()
						.getNamedItem("simulationName").getNodeValue() == null
						|| fstSimulation.getAttributes()
								.getNamedItem("simulationNumberSeed")
								.getNodeValue() == null)
					throw new DeusAutomatorException(
							"Errore manca simulationNumberSeed e/o simulationName nel tag simulation");

				// String resultFolder = null;
				// String inputFolder = null;
				//
				// String simulationNumberSeed =
				// fstSimulation.getAttributes().getNamedItem("simulationNumberSeed").getNodeValue();

				// name of the simulation
				simulationName = fstSimulation.getAttributes()
						.getNamedItem("simulationName").getNodeValue();

				// Create new Panel and set it to be modified with new data read
				// from the XML file
				deusSimulationPanel = new DeusSimulationPanelCommandLine();

				// if(fstSimulation.getAttributes().getNamedItem("resultFolder")
				// != null)
				// resultFolder =
				// fstSimulation.getAttributes().getNamedItem("resultFolder").getNodeValue();
				//
				// if(fstSimulation.getAttributes().getNamedItem("inputFolder")
				// != null)
				// inputFolder =
				// fstSimulation.getAttributes().getNamedItem("inputFolder").getNodeValue();

				NodeList nodeLst = document.getElementsByTagName("node");

				// list of nodes
				for (int s = 0; s < nodeLst.getLength(); s++) {

					Node fstNode = nodeLst.item(s);

					if (fstNode.getParentNode().equals(simulationLst.item(w))) {

						// name of the node
						String messageType = fstNode.getAttributes()
								.getNamedItem("id").getNodeValue();

						Element fstElmnt = (Element) fstNode;
						NodeList fstNmElmntLst = fstElmnt
								.getElementsByTagName("paramName");

						// Retrieve all params in node's ParamName
						for (int j = 0; j < fstNmElmntLst.getLength(); j++) {

							Element paramElement = (Element) fstNmElmntLst
									.item(j);

							// name of the param
							String paramName = ((Node) fstNmElmntLst.item(j))
									.getAttributes().getNamedItem("name")
									.getNodeValue();

							NodeList initialValue = paramElement
									.getElementsByTagName("initialValue");

							NodeList finalValue = paramElement
									.getElementsByTagName("finalValue");

							NodeList stepValue = paramElement
									.getElementsByTagName("stepValue");

							if (initialValue == null || finalValue == null
									|| stepValue == null) {
								throw new DeusAutomatorException(
										"Errore in initalValue , finalValue e stepValue in "
												+ simulationName + " di Node "
												+ messageType + " in "
												+ paramName);
							}

							// value of params
							Double init = Double.parseDouble(initialValue.item(
									0).getTextContent());
							Double fin = Double.parseDouble(finalValue.item(0)
									.getTextContent());
							Double step = Double.parseDouble(stepValue.item(0)
									.getTextContent());

							// create new object to be inserted in the GUI
							NodeParameter nodeParameter = new NodeParameter(
									fin, init, messageType, paramName, step);

							deusSimulationPanel.addNodeParameter(nodeParameter);

						}

						NodeList paramName = fstElmnt
								.getElementsByTagName("resourceParamName");

						// retrieve all params in node's resourceParamName
						for (int j = 0; j < paramName.getLength(); j++) {

							Element paramElement = (Element) paramName.item(j);

							// handler name
							String handlerName = ((Node) paramName.item(j))
									.getAttributes()
									.getNamedItem("handlerName").getNodeValue();

							// resource param name
							String resParamValueName = ((Node) paramName
									.item(j)).getAttributes()
									.getNamedItem("resParamValue")
									.getNodeValue();

							NodeList initialValue = paramElement
									.getElementsByTagName("initialValue");

							NodeList finalValue = paramElement
									.getElementsByTagName("finalValue");

							NodeList stepValue = paramElement
									.getElementsByTagName("stepValue");

							if (initialValue == null || finalValue == null
									|| stepValue == null) {
								throw new DeusAutomatorException(
										"Errore in initalValue , finalValue e stepValue in "
												+ simulationName + " di Node"
												+ messageType + " in "
												+ paramName);
							}

							// values of params
							Double init = Double.parseDouble(initialValue.item(
									0).getTextContent());
							Double fin = Double.parseDouble(finalValue.item(0)
									.getTextContent());
							Double step = Double.parseDouble(stepValue.item(0)
									.getTextContent());

							NodeResource nodeResource = new NodeResource(fin,
									handlerName, init, messageType,
									resParamValueName, step);

							deusSimulationPanel.addNodeResource(nodeResource);

						}

					}

				}

				NodeList processLst = document.getElementsByTagName("process");

				// list of processes
				for (int s = 0; s < processLst.getLength(); s++) {

					Node fstNode = processLst.item(s);

					if (fstNode.getParentNode().equals(simulationLst.item(w))) {

						// process name
						String messageType = fstNode.getAttributes()
								.getNamedItem("id").getNodeValue();

						Element fstElmnt = (Element) fstNode;
						NodeList fstNmElmntLst = fstElmnt
								.getElementsByTagName("paramName");

						// Retrieve all params in process' ParamName
						for (int j = 0; j < fstNmElmntLst.getLength(); j++) {

							Element paramElement = (Element) fstNmElmntLst
									.item(j);

							// name of process param
							String paramName = ((Node) fstNmElmntLst.item(j))
									.getAttributes().getNamedItem("name")
									.getNodeValue();

							NodeList initialValue = paramElement
									.getElementsByTagName("initialValue");

							NodeList finalValue = paramElement
									.getElementsByTagName("finalValue");

							NodeList stepValue = paramElement
									.getElementsByTagName("stepValue");

							// values of params
							Double init = Double.parseDouble(initialValue.item(
									0).getTextContent());
							Double fin = Double.parseDouble(finalValue.item(0)
									.getTextContent());
							Double step = Double.parseDouble(stepValue.item(0)
									.getTextContent());

							ProcessParameter processParameter = new ProcessParameter(
									messageType, paramName, init, fin, step);

							deusSimulationPanel
									.addProcessParameter(processParameter);
						}

					}

				}

				NodeList engineLst = document.getElementsByTagName("engine");

				// engine
				for (int s = 0; s < engineLst.getLength(); s++) {

					Node fstNode = engineLst.item(s);

					if (fstNode.getParentNode().equals(simulationLst.item(w))) {

						/*
						 * // the following lines are useless [Michele] String
						 * startVt = ""; String endVt = ""; String stepVt = "";
						 * 
						 * boolean vt = true;
						 * if(fstNode.getAttributes().getNamedItem("startVT") !=
						 * null ) startVt =
						 * fstNode.getAttributes().getNamedItem(
						 * "startVT").getNodeValue(); else vt = false;
						 * 
						 * if(fstNode.getAttributes().getNamedItem("endVT") !=
						 * null ) endVt =
						 * fstNode.getAttributes().getNamedItem("endVT"
						 * ).getNodeValue(); else vt = false;
						 * 
						 * if(fstNode.getAttributes().getNamedItem("stepVT") !=
						 * null ) stepVt =
						 * fstNode.getAttributes().getNamedItem("stepVT"
						 * ).getNodeValue(); else vt = false;
						 */

						Element fstElmnt = (Element) fstNode;
						NodeList fstNmElmntLst = fstElmnt
								.getElementsByTagName("seed");

						// Retrieve all seedValues in seed
						for (int j = 0; j < fstNmElmntLst.getLength(); j++) {

							Element paramElement = (Element) fstNmElmntLst
									.item(j);

							NodeList seedValue = paramElement
									.getElementsByTagName("seedValue");

							for (int o = 0; o < seedValue.getLength(); o++) {
								// values of the seeds
								String seedvalue = seedValue.item(o)
										.getTextContent();

								EngineParameter engineParameter = new EngineParameter(
										seedvalue);

								deusSimulationPanel
										.addEngineParameter(engineParameter);
							}

						}

					}

				}

				// NodeList resultLogLst =
				// document.getElementsByTagName("resultVT");
				//
				// for (int i = 0; i < resultLogLst.getLength(); i++) {
				//
				// Node fileLog = resultLogLst.item(i);
				//
				// if(fileLog.getParentNode().equals(simulationLst.item(w))){
				//
				// sim.setFileLog(fileLog.getAttributes().getNamedItem("outputLogFile").getNodeValue());
				// }
				//
				// }
				//
				// GNUPLOT
				NodeList GnuPlotLst = document
						.getElementsByTagName("resultXYFile");

				for (int i = 0; i < GnuPlotLst.getLength(); i++) {

					Node GnuPlotNode = GnuPlotLst.item(i);

					if (GnuPlotNode.getParentNode().equals(
							simulationLst.item(w))) {

						String fileName = GnuPlotNode.getAttributes()
								.getNamedItem("fileName").getNodeValue();

						String asseX = GnuPlotNode.getAttributes()
								.getNamedItem("axisX").getNodeValue();

						String asseY = GnuPlotNode.getAttributes()
								.getNamedItem("axisY").getNodeValue();

						GnuPlotFileElement gnuPlotFileElement = new GnuPlotFileElement(
								fileName, asseX, asseY);

						deusSimulationPanel
								.addGnuPlotFileElement(gnuPlotFileElement);
					}

				}

			}

		} catch (SAXException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}

	}

	public String getOriginalXmlPath() {
		return originalXmlPath;
	}

	public void setOriginalXmlPath(String originalXmlPath) {
		this.originalXmlPath = originalXmlPath;
	}

	public String getOutFileName() {
		return outFileName;
	}

	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}

}
