package it.unipr.ce.dsg.deus.editor;

import it.unipr.ce.dsg.deus.schema.Automator;
import it.unipr.ce.dsg.deus.schema.Engine;
import it.unipr.ce.dsg.deus.schema.Event;
import it.unipr.ce.dsg.deus.schema.Network;
import it.unipr.ce.dsg.deus.schema.Node;
import it.unipr.ce.dsg.deus.schema.Param;
import it.unipr.ce.dsg.deus.schema.Params;
import it.unipr.ce.dsg.deus.schema.Process;
import it.unipr.ce.dsg.deus.schema.Logger;
import it.unipr.ce.dsg.deus.schema.Reference;
import it.unipr.ce.dsg.deus.schema.Resource;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;


/**
 * A class that unmarshall the file XML and draw on Graph corresponding
 * instances
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 * 
 * @author Stefano Sebastio (stefano.sebastio@imtlucca.it)
 */
public class Unmarshalling implements SetVisualizationView {

	private DeusVertex engine, node, event, process, network;
	private VisualizationViewer<DeusVertex, DeusEdge> visView;
	private ArrayList<DeusVertex> processes, events, nodes;

	public void createGraphAutomator(File file) throws JAXBException,
			ClassNotFoundException, IllegalArgumentException,
			SecurityException, InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, SAXException {

		JAXBContext jc = JAXBContext.newInstance("it.unipr.ce.dsg.deus.schema");
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		@SuppressWarnings("unused")
		Schema schema = schemaFactory
				.newSchema(new File("schema/automator.xsd"));
		Unmarshaller unmarshaller = jc.createUnmarshaller();

		/*
		 * validation
		 * 
		 * unmarshaller.setSchema(schema); unmarshaller.setEventHandler(new
		 * ValidationEventHandler() {
		 * 
		 * public boolean handleEvent(ValidationEvent ve) { if (ve.getSeverity()
		 * == ValidationEvent.FATAL_ERROR || ve.getSeverity() ==
		 * ValidationEvent.ERROR || ve.getSeverity() == ValidationEvent.WARNING)
		 * { ValidationEventLocator locator = ve.getLocator();
		 * System.out.println("Invalid configuration file: " +
		 * locator.getURL()); System.out.println("Error at column " +
		 * locator.getColumnNumber() + ", line " + locator.getLineNumber());
		 * System.out.println("Error: " + ve.getMessage()); return false; }
		 * return true; }
		 * 
		 * });
		 */

		// *******Automator

		Automator automator = (Automator) ((JAXBElement) unmarshaller
				.unmarshal(file)).getValue();

		Graph<DeusVertex, DeusEdge> graph = visView.getGraphLayout().getGraph();

		Engine engineXml = automator.getEngine();
		List<Process> processList = automator.getProcess();
		List<Event> eventList = automator.getEvent();
		List<Node> nodeList = automator.getNode();
		Network networkXml = automator.getNetwork();

		// *************Node
		nodes = new ArrayList<DeusVertex>();
		for (Node nodeXml : nodeList) {
			node = new DeusVertex("Node");
			nodes.add(node);
			node.setId(nodeXml.getId());
			node.setHandler(nodeXml.getHandler());
			Params params = nodeXml.getParams();
			if (params != null) {
				List<Param> paramList = params.getParam();
				ArrayList<String> nameList = new ArrayList<String>();
				ArrayList<String> valueList = new ArrayList<String>();
				for (Param paramXml : paramList) {
					nameList.add(paramXml.getName());
					valueList.add(paramXml.getValue());
				}
				node.setParam(nameList, valueList, true);
			}

			Logger logger = nodeXml.getLogger();
			if (logger != null) {
				node.setLogger(logger.getLevel(), logger.getPathPrefix(), true);
			}
			if (nodeXml.getResources() != null) {
				List<Resource> resurceList = nodeXml.getResources()
						.getResource();
				ResourceParam resPar = new ResourceParam();
				ArrayList<ResourceParam> resParamList = new ArrayList<ResourceParam>();
				for (Resource res : resurceList) {

					if (res.getParams() != null) {
						List<Param> paramList = new ArrayList<Param>();
						paramList = res.getParams().getParam();
						ArrayList<String> nameList = new ArrayList<String>();
						ArrayList<String> valueList = new ArrayList<String>();

						for (Param p : paramList) {
							nameList.add(p.getName());
							valueList.add(p.getValue());
						}

						resPar.setHandlerRes(res.getHandler());
						resPar.setNameResParamList(nameList);
						resPar.setValueResParmList(valueList);
						resParamList.add(resPar);
					}

				}
				node.setResource(resParamList, true);
			}

			graph.addVertex(node);

		}

		// ******Event
		events = new ArrayList<DeusVertex>();
		for (Event eventXml : eventList) {

			event = new DeusVertex("Event");
			events.add(event);
			event.setId(eventXml.getId());
			event.setHandler(eventXml.getHandler());

			if (eventXml.getSchedulerListener() != null)
				event.setSchedListener(eventXml.getSchedulerListener(), true);
			if (eventXml.isOneShot() != null)
				event.setOneShot(eventXml.isOneShot().toString(), true);
			if (eventXml.getDistributionLevel() != null)
				event.setDisLevel(eventXml.getDistributionLevel(), true);

			Params params = eventXml.getParams();
			if (params != null) {
				List<Param> paramList = params.getParam();
				ArrayList<String> nameList = new ArrayList<String>();
				ArrayList<String> valueList = new ArrayList<String>();
				for (Param paramXml : paramList) {
					nameList.add(paramXml.getName());
					valueList.add(paramXml.getValue());
				}
				event.setParam(nameList, valueList, true);
			}

			Logger logger = eventXml.getLogger();
			if (logger != null) {
				event.setLogger(logger.getLevel(), logger.getPathPrefix(), true);
			}

			graph.addVertex(event);

		}
		// events reference
		for (Event eventXml : eventList) {

			if (eventXml.getEvents() != null) {
				List<Reference> eventsList = eventXml.getEvents()
						.getReference();
				for (Reference eventl : eventsList) {
					DeusEdge edge = new DeusEdge();
					graph.addEdge(
							edge,
							DeusUtility.getVertexById(eventXml.getId(), events),
							DeusUtility.getVertexById(eventl.getId(), events));
				}
			}
		}

		// ***************Process
		processes = new ArrayList<DeusVertex>();
		for (Process processXml : processList) {

			process = new DeusVertex("Process");
			processes.add(process);
			process.setId(processXml.getId());
			process.setHandler(processXml.getHandler());

			Params params = processXml.getParams();
			if (params != null) {
				List<Param> paramList = params.getParam();
				ArrayList<String> nameList = new ArrayList<String>();
				ArrayList<String> valueList = new ArrayList<String>();
				for (Param paramXml : paramList) {
					nameList.add(paramXml.getName());
					valueList.add(paramXml.getValue());
				}
				process.setParam(nameList, valueList, true);
			}

			Logger logger = processXml.getLogger();
			if (logger != null) {
				process.setLogger(logger.getLevel(), logger.getPathPrefix(),
						true);
			}
			if (processXml.getEvents() != null) {
				List<Reference> eventsList = processXml.getEvents()
						.getReference();
				for (Reference event : eventsList) {
					DeusEdge edge = new DeusEdge();
					graph.addEdge(edge, process,
							DeusUtility.getVertexById(event.getId(), events));

				}
			}
			if (processXml.getNodes() != null) {
				List<Reference> nodesList = processXml.getNodes()
						.getReference();
				for (Reference node : nodesList) {
					DeusEdge edge = new DeusEdge();
					graph.addEdge(edge, process,
							DeusUtility.getVertexById(node.getId(), nodes));
				}
			}
			graph.addVertex(process);

		}

		if (networkXml != null) {
			network = new DeusVertex("Network");
			network.setId(networkXml.getId());
			network.setHandler(networkXml.getHandler());

			if (networkXml.getNodes() != null) {
				List<Reference> nodeNetList = networkXml.getNodes()
						.getReference();
				for (Reference nodeNet : nodeNetList) {
					DeusEdge edge = new DeusEdge();
					if (nodeNet.getNumber() != null)
						edge.setNumber(nodeNet.getNumber());
					graph.addEdge(edge, network,
							DeusUtility.getVertexById(nodeNet.getId(), nodes));
				}
			}
			graph.addVertex(network);
		}

		// ***************Engine
		if (engineXml != null) {
			engine = new DeusVertex("Engine");
			engine.setMaxVT(engineXml.getMaxvt());
			engine.setSeed(engineXml.getSeed());
			engine.setPrng(engineXml.getPrng(), true);
			if (engineXml.getKeyspacesize() != null)
				engine.setKeySS(engineXml.getKeyspacesize(), true);

			Logger logger = engineXml.getLogger();
			if (logger != null) {
				engine.setLogger(logger.getLevel(), logger.getPathPrefix(),
						true);
			}
			if (engineXml.getProcesses() != null) {
				List<Reference> processesList = engineXml.getProcesses()
						.getReference();
				for (Reference proc : processesList) {
					DeusEdge edge = new DeusEdge();
					graph.addEdge(edge, engine,
							DeusUtility.getVertexById(proc.getId(), processes));

				}
			}
			graph.addVertex(engine);
		}

		Layout<DeusVertex, DeusEdge> frLayout = new FRLayout<DeusVertex, DeusEdge>(
				graph);
		frLayout.setSize(visView.getSize());

		MainGUI.layout = new StaticLayout<DeusVertex, DeusEdge>(graph, frLayout);
		// Layout<DeusVertex, DeusEdge> staticL= new StaticLayout<DeusVertex,
		// DeusEdge>(graph, frLayout);
		visView.setGraphLayout(MainGUI.layout);

		// If file *.pos exist restore position. Only for xml file.
		if (!(file.getName().contains(".bup"))) {
			String nameFile = file.getAbsolutePath().replaceAll(".xml", ".pos");
			File f = new File(nameFile);
			if (f.exists()) {
				DeusUtility.restoreVertexPosition(visView, f.getAbsolutePath());
			}
		}
		visView.repaint();
	}

	@Override
	public void setVisualizationView(
			VisualizationViewer<DeusVertex, DeusEdge> vv) {

		visView = vv;

	}

}