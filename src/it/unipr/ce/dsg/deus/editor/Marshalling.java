package it.unipr.ce.dsg.deus.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import it.unipr.ce.dsg.deus.schema.Event;
import it.unipr.ce.dsg.deus.schema.Logger;
import it.unipr.ce.dsg.deus.schema.Network;
import it.unipr.ce.dsg.deus.schema.Node;
import it.unipr.ce.dsg.deus.schema.ObjectFactory;
import it.unipr.ce.dsg.deus.schema.Automator;
import it.unipr.ce.dsg.deus.schema.Engine;
import it.unipr.ce.dsg.deus.schema.Param;
import it.unipr.ce.dsg.deus.schema.Params;
import it.unipr.ce.dsg.deus.schema.Process;
import it.unipr.ce.dsg.deus.schema.Reference;
import it.unipr.ce.dsg.deus.schema.References;
import it.unipr.ce.dsg.deus.schema.Resource;
import it.unipr.ce.dsg.deus.schema.Resources;

import javax.swing.JOptionPane;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
//import javax.xml.bind.ValidationEventLocator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;


/**
 * A class useful to create a marshall file in xml after validation and save
 * position of vertex.
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 * 
 * @author Stefano Sebastio (stefano.sebastio@imtlucca.it)
 * 
 */
public class Marshalling {

	private static final String W3C_XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";
	
	private String packageName = "it.unipr.ce.dsg.deus.schema";
	private File filename;
	private JAXBContext jaxbContext;
	private ObjectFactory automObj;

	private Automator automatorIst;
	private Engine engineIst;
	private Process processIst;
	private Event eventIst;
	private Node nodeIst;
	private Logger loggerIst;
	private Param paramIst;
	private Params paramsIst;
	private References referencesIst;
	private Reference referenceIst;
	private Reference referenceEventIst;
	private Reference referenceNodeIst;
	private References referencesEventsIst;
	private References referencesNodesIst;
	private Resources resourcesIst;
	private Resource resourceIst;
	private Param paramResIst;
	private Params paramsResIst;
	private Network networkIst;
	private References referencesNodesNetIst;
	private Reference referenceNodeNetIst;
	private boolean doValidation;

	private Graph<DeusVertex, DeusEdge> graph;
	private JAXBElement<Automator> automRoot;
	private Collection<DeusVertex> vertNeighBCollection;
	private VisualizationViewer<DeusVertex, DeusEdge> vv;

	Marshalling(VisualizationViewer<DeusVertex, DeusEdge> vv) {

		this.graph = vv.getGraphLayout().getGraph();
		this.vv = vv;
	}

	public void createMarshallFile(File file, boolean savexmlComplete) {
		this.doValidation = savexmlComplete;
		this.filename = file;
		createContext();
		createNewAutomator();
		configureAutomator();
		marshallAutomator();

	}

	private void createContext() {

		try {
			jaxbContext = JAXBContext.newInstance(packageName);
		} catch (JAXBException e) {
			System.out
					.println("There was this problem creating a context " + e);
		}

	}

	private void createNewAutomator() {

		automObj = new ObjectFactory();
		automatorIst = automObj.createAutomator();
		engineIst = automObj.createEngine();

	}

	private void configureAutomator() {

		DeusVertex vert;
		DeusVertex vertLink;
		boolean checkNodes = false;

		for (Iterator<DeusVertex> graphInfo = this.graph.getVertices()
				.iterator(); graphInfo.hasNext();) {

			vert = graphInfo.next();

			if (vert.getElementType() == "Engine") {

				// ****** Engine

				// check neighbors vertices
				vertNeighBCollection = this.graph.getNeighbors(vert);

				referencesIst = automObj.createReferences();

				for (Iterator<DeusVertex> vertNeighB = vertNeighBCollection
						.iterator(); vertNeighB.hasNext();) {

					vertLink = vertNeighB.next();

					// only reference with Process
					referenceIst = automObj.createReference();
					referenceIst.setId(vertLink.getId());
					referencesIst.getReference().add(referenceIst);

				}

				engineIst.setProcesses(referencesIst);

				engineIst.setSeed(vert.getSeed());
				engineIst.setMaxvt(vert.getMaxVT());

				if (vert.getSelectKSS())
					engineIst.setKeyspacesize(vert.getKeySS());
				
				if (vert.getSelectPrng())
					engineIst.setPrng(vert.getPrng());

				if (vert.getSelectLog()) {

					loggerIst = automObj.createLogger();

					loggerIst.setLevel(vert.getLogLevel());
					loggerIst.setPathPrefix(vert.getLogPathPrefix());
					engineIst.setLogger(loggerIst);
				}

				automatorIst.setEngine(engineIst);
			}

			else if (vert.getElementType() == "Process") {

				// ****** Process

				processIst = automObj.createProcess();

				vertNeighBCollection = this.graph.getNeighbors(vert);

				referencesEventsIst = automObj.createReferences();
				referencesNodesIst = automObj.createReferences();

				for (Iterator<DeusVertex> vertNeighB = vertNeighBCollection
						.iterator(); vertNeighB.hasNext();) {

					vertLink = vertNeighB.next();

					// only reference with Event or Node
					if (vertLink.getElementType() == "Event") {
						referenceEventIst = automObj.createReference();
						referenceEventIst.setId(vertLink.getId());

						referencesEventsIst.getReference().add(
								referenceEventIst);
					} else if (vertLink.getElementType() == "Node") {
						referenceNodeIst = automObj.createReference();
						referenceNodeIst.setId(vertLink.getId());

						referencesNodesIst.getReference().add(referenceNodeIst);

						checkNodes = true;

					}

				}

				// set Nodes if there are reference node
				if (checkNodes) {
					processIst.setNodes(referencesNodesIst);
					checkNodes = false;
				}

				processIst.setEvents(referencesEventsIst);

				processIst.setId(vert.getId());
				processIst.setHandler(vert.getHandler());

				if (vert.getSelectLog()) {

					loggerIst = automObj.createLogger();

					loggerIst.setLevel(vert.getLogLevel());
					loggerIst.setPathPrefix(vert.getLogPathPrefix());
					processIst.setLogger(loggerIst);
				}

				if (vert.getSelectParam()) {

					processIst.setParams(setParams(vert));
				}

				automatorIst.getProcess().add(processIst);
			}

			else if (vert.getElementType() == "Event") {

				// ****** Event

				eventIst = automObj.createEvent();

				vertNeighBCollection = this.graph.getSuccessors(vert);

				referencesEventsIst = automObj.createReferences();

				for (Iterator<DeusVertex> vertS = vertNeighBCollection
						.iterator(); vertS.hasNext();) {

					vertLink = vertS.next();

					// only reference with Event
					if (vertLink.getElementType() == "Event") {

						referenceEventIst = automObj.createReference();
						referenceEventIst.setId(vertLink.getId());

						referencesEventsIst.getReference().add(
								referenceEventIst);

						if (!vertS.hasNext()) {
							eventIst.setEvents(referencesEventsIst);
						}
					}

				}

				eventIst.setId(vert.getId());
				eventIst.setHandler(vert.getHandler());

				// vengono inseriti nel file xml solo le opzioni spuntate
				if (vert.getSelectSL())
					eventIst.setSchedulerListener(vert.getSchedListener());

				if (vert.getSelectOS())
					eventIst.setOneShot(Boolean.valueOf(vert.getOneShot()));

				if (vert.getSelectDisLevel())
					eventIst.setDistributionLevel(vert.getDisLevel());

				if (vert.getSelectLog()) {

					loggerIst = automObj.createLogger();

					loggerIst.setLevel(vert.getLogLevel());
					loggerIst.setPathPrefix(vert.getLogPathPrefix());
					eventIst.setLogger(loggerIst);
				}

				if (vert.getSelectParam()) {

					eventIst.setParams(setParams(vert));
				}

				automatorIst.getEvent().add(eventIst);

			} else if (vert.getElementType() == "Node") {

				// ****** Node
				nodeIst = automObj.createNode();

				nodeIst.setId(vert.getId());
				nodeIst.setHandler(vert.getHandler());

				if (vert.getSelectLog()) {

					loggerIst = automObj.createLogger();

					loggerIst.setLevel(vert.getLogLevel());
					loggerIst.setPathPrefix(vert.getLogPathPrefix());
					nodeIst.setLogger(loggerIst);
				}

				if (vert.getSelectParam()) {

					nodeIst.setParams(setParams(vert));
				}

				if (vert.getSelectResources()) {

					resourcesIst = automObj.createResources();

					ResourceParam resPar = new ResourceParam();

					ArrayList<ResourceParam> resParamList = new ArrayList<ResourceParam>();
					resParamList = vert.getResource();

					Iterator<ResourceParam> itResources = resParamList
							.iterator();

					while (itResources.hasNext()) {

						resourceIst = automObj.createResource();

						paramsResIst = automObj.createParams();

						resPar = itResources.next();

						Iterator<String> iterName = resPar
								.getNameResParamList().iterator();
						Iterator<String> iterValue = resPar
								.getValueResParamList().iterator();

						while (iterName.hasNext()) {

							paramResIst = automObj.createParam();
							paramResIst.setName(iterName.next().toString());
							paramResIst.setValue(iterValue.next().toString());

							paramsResIst.getParam().add(paramResIst);
						}

						resourceIst.setParams(paramsResIst);
						resourceIst.setHandler(resPar.getHandlerRes());

						resourcesIst.getResource().add(resourceIst);

					}

					nodeIst.setResources(resourcesIst);

				}

				automatorIst.getNode().add(nodeIst);

			} else if (vert.getElementType() == "Network") {

				DeusEdge edge;

				networkIst = automObj.createNetwork();

				networkIst.setId(vert.getId());
				networkIst.setHandler(vert.getHandler());

				vertNeighBCollection = this.graph.getSuccessors(vert);

				referencesNodesNetIst = automObj.createReferences();

				for (Iterator<DeusVertex> vertS = vertNeighBCollection
						.iterator(); vertS.hasNext();) {

					vertLink = vertS.next();
					edge = this.graph.findEdge(vert, vertLink);

					// only reference with Node
					if (vertLink.getElementType() == "Node") {

						referenceNodeNetIst = automObj.createReference();
						referenceNodeNetIst.setId(vertLink.getId());
						// add reference number
						if (edge.getNumber() != 0)
							referenceNodeNetIst.setNumber(edge.getNumber());

						referencesNodesNetIst.getReference().add(
								referenceNodeNetIst);

						if (!vertS.hasNext()) {
							networkIst.setNodes(referencesNodesNetIst);
						}
					}

				}

				automatorIst.setNetwork(networkIst);
			}

			else {

				JOptionPane.showMessageDialog(MainGUI.frame, "No vertices.",
						"Information", JOptionPane.INFORMATION_MESSAGE);
			}

		}

	}

	private void marshallAutomator() {

		automRoot = automObj.createAutomator(automatorIst);

		try {

			NamespacePrefixMapper m = new PreferredMapper();

			Marshaller marshallerAutomator = jaxbContext.createMarshaller();

			marshallerAutomator.setProperty(
					"com.sun.xml.bind.namespacePrefixMapper", m);

			marshallerAutomator
					.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
							"http://dsg.ce.unipr.it/software/deus/schema/automator schema/automator.xsd");

			marshallerAutomator.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					new Boolean(true));

			// ******** validation before marshalling
			if (this.doValidation) {
				SchemaFactory sf = SchemaFactory
						.newInstance(W3C_XML_SCHEMA_NS_URI);
				// SchemaFactory sf =
				// SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
				// //For Mac Os X with old java version
				try {
					Schema schema = sf.newSchema(new File(
							"schema/automator.xsd"));

					marshallerAutomator.setSchema(schema);

				} catch (SAXException e) {
					e.printStackTrace();
				}

				marshallerAutomator
						.setEventHandler(new ValidationEventHandler() {
							public boolean handleEvent(ValidationEvent event) {

								if (event.getSeverity() == ValidationEvent.FATAL_ERROR
										|| event.getSeverity() == ValidationEvent.ERROR
										|| event.getSeverity() == ValidationEvent.WARNING) {
									// ValidationEventLocator locator =
									// event.getLocator();

									/*
									 * System.out.println("Error at column " +
									 * locator.getColumnNumber() + ", line " +
									 * locator.getLineNumber());
									 */

									// System.out.println("Error: " +
									// event.getMessage());

									JOptionPane.showMessageDialog(
											MainGUI.frame, event.getMessage(),
											"Error", JOptionPane.ERROR_MESSAGE);

									return false;
								}
								return true;
							}
						});

			}
			// ****** marshalling
			marshallerAutomator.marshal(automRoot, getFileStream());

			// ***if it is a xml file save vertex position in *.pos
			if (this.doValidation) {
				int n = JOptionPane.showConfirmDialog(MainGUI.frame,
						"Do you want to save the location's vertices?",
						"Save position", JOptionPane.YES_NO_OPTION);

				if (n == 0)
					DeusUtility.saveVertexPosition(vv,
							this.filename.getAbsolutePath());

			}

		} catch (JAXBException e) {

			// e.printStackTrace();
			// System.out.println(e);
		}

	}

	private FileOutputStream getFileStream() {

		try {
			return new FileOutputStream(this.filename);
		} catch (FileNotFoundException e) {
			System.out.println("The problem creating a destination file was "
					+ e);
			return null;
		}

	}

	private Params setParams(DeusVertex vertex) {

		paramsIst = automObj.createParams();

		Iterator<String> iterName = vertex.getParamName().iterator();
		Iterator<String> iterValue = vertex.getParamValue().iterator();

		while (iterName.hasNext()) {
			paramIst = automObj.createParam();
			paramIst.setName(iterName.next().toString());
			paramIst.setValue(iterValue.next().toString());
			paramsIst.getParam().add(paramIst);

		}
		return paramsIst;
	}

	public static class PreferredMapper extends NamespacePrefixMapper {
		@Override
		public String getPreferredPrefix(String namespaceUri,
				String suggestion, boolean requirePrefix) {

			if (requirePrefix) {
				if ("http://www.w3.org/2001/XMLSchema-instance"
						.equals(namespaceUri)) {
					return "xsi";
				}
				return suggestion;
			} else
				return "aut";
		}
	}

}
