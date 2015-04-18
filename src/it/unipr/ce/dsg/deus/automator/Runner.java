package it.unipr.ce.dsg.deus.automator;

import it.unipr.ce.dsg.deus.core.Deus;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
//import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Marco Pigoni
 * 
 * @author Stefano Sebastio (stefano.sebastio@imtlucca.it) [only Runner refactoring]
 * 
 */
public class Runner implements Runnable {

	private Document document;
	private Document doc;
	private String originalXML = "";
	private int numSim = 0;
	private String originalXml = "";
	private String automatorXml = "";
	private ArrayList<String> logFile = null;
	protected ArrayList<String> files = null;
	protected int numFile;
	private ArrayList<MyObjectSimulation> simulations = null;
	private Deus deus = null;
	
	protected PropertyChangeSupport propertyChangeSupportNumSim = null;
	
	protected char[] cbuf;
	
	public Runner(String originalXml, String automatorXml) {
		this.originalXml = originalXml;
		this.automatorXml = automatorXml;
		
		propertyChangeSupportNumSim = new PropertyChangeSupport(this);
		
	}
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    	propertyChangeSupportNumSim.addPropertyChangeListener(listener);
    }
	
    //To notify the simulation progress. Used (i.e., listened) by the RunnerGui
	private void incNumFile(){
		numFile++;
		propertyChangeSupportNumSim.firePropertyChange("NumFileProperty" , (numFile-1), numFile);
		//System.out.println("incNumFile");
	}
	
	private static boolean DelDir2(File dir) {
		if (dir.isDirectory()) {
			String[] content = dir.list();
			for (int i = 0; i < content.length; i++) {
				if (new File(dir + "/" + content[i]).exists())
					new File(dir + "/" + content[i]).delete();
			}
		}
		return true;
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

		// if these directories do not exist, create them
		new File("./temp").mkdirs();
		new File("./results").mkdirs();
		new File("./results/gnuplot").mkdirs();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		File f = new File(originalXml);

		DocumentBuilder builder = factory.newDocumentBuilder();

		doc = builder.parse(f);

		originalXML = originalXml;

		FileInputStream fis = new FileInputStream(originalXML);
		FileOutputStream fos = new FileOutputStream(originalXML + ".temp");

		byte[] data = new byte[fis.available()];

		fis.read(data);
		fos.write(data);

		fis.close();
		fos.close();

		DelDir2(new File("./xml"));

		// Create n XML files for the n simulations with DEUS
		files = new ArrayList<String>();

		// Insert in the ArrayList the names of the XML files to run
		simulations = readXML(automatorXml);

		// Insert in the ArrayList the names of the XML files to run
		files = writeXML(simulations, originalXml);

		numFile = 0;
		logFile = new ArrayList<String>();

		// Read the file that contains the information on the simulation to
		// execute
		File simfile = new File("simulations");
		FileInputStream simfileInputStream = new FileInputStream(simfile);
		InputStreamReader isr = new InputStreamReader(simfileInputStream);
		BufferedReader br = new BufferedReader(isr);

		cbuf = new char[simfileInputStream.available()];
		br.read(cbuf);
		
		br.close();
		
		return 0;
	}

	/**
	 * 
	 */
	public void runSimulations() {

		String computerName = "";
		try {
			computerName = InetAddress.getLocalHost().getHostName();
			// System.out.println(computerName);
		} catch (Exception e) {
			System.out.println("Exception caught =" + e.getMessage());
		}

		ArrayList<String> averageFileList = new ArrayList<String>();
		
		//System.out.println("simulations.size() = " + simulations.size());
		
		// Run the n simulations with respective n files
		for (int j = 0; j < simulations.size(); j++) {
			for (int k = 0; k < simulations.get(j).getSimulationNumber(); k++) {
				//System.out.println("k = " + k);
				
				//System.out.println("simulations.get(j).getSimulationNumberSeed() = " + simulations.get(j).getSimulationNumberSeed());
				
				for (int i = 0; i < new Integer(simulations.get(j).getSimulationNumberSeed()); i++) {
					//System.out.println("file " + files.get(numFile));
					//System.out.println("logFile " + simulations.get(j).getFileLog());
					
//					Path currentRelativePath = Paths.get("");
//					System.out.println(currentRelativePath..toString());
//					
					String logFileName = "." + File.separator + "temp" + File.separator
							+ computerName
							+ "-"
							+ simulations.get(j).getSimulationName()
							+ "-"
							+ k
							+ "-"
							+ simulations.get(j).getEngine().get(j).getSeed()
									.get(i);
					
//					String logFileName = "./temp/"
//							+ computerName
//							+ "-"
//							+ simulations.get(j).getSimulationName()
//							+ "-"
//							+ k
//							+ "-"
//							+ simulations.get(j).getEngine().get(j).getSeed()
//									.get(i);
					
//					Writer writer = null;
//
//					try {
//					    writer = new BufferedWriter(new OutputStreamWriter(
//					          new FileOutputStream(logFileName), "utf-8"));
//					    writer.write("Something");
//					} catch (IOException ex) {
//					  // report
//					} finally {
//					   try {writer.close();} catch (Exception ex) {}
//					}

					//System.out.println("the desired filename is " + logFileName);
					//deus = new Deus(files.get(numFile), simulations.get(j).getFileLog());
					this.deus = new Deus(files.get(numFile), logFileName);
					//System.out.println("i = " + i);
//					File log = new File(simulations.get(j).getFileLog());

					//System.out.println("seed = " + simulations.get(j).getEngine().get(j).getSeed().get(i));
//					log.renameTo(new File("./temp/"
//							+ computerName
//							+ "-"
//							+ simulations.get(j).getSimulationName()
//							+ "-"
//							+ k
//							+ "-"
//							+ simulations.get(j).getEngine().get(j).getSeed()
//									.get(i)));
//
//					log.delete();
					
					//check if the simulation has logged something
					File f = new File(logFileName);
					if(f.exists() && !f.isDirectory()) {
						logFile.add(logFileName);
					}
//					logFile.add("./temp/"
//							+ computerName
//							+ "-"
//							+ simulations.get(j).getSimulationName()
//							+ "-"
//							+ k
//							+ "-"
//							+ simulations.get(j).getEngine().get(j).getSeed()
//									.get(i));

					incNumFile();
					//numFile++;
					
					//System.out.println("numFile = " + numFile);

				}

				ResultAutomator resultAutomator = new ResultAutomator(logFile);

				String averageFileName = "";
				String varFileName = "";
				String sqrtvarFileName = "";

				// Add HostName in Destination File

				// Compute the mean and the variance (with several seeds) of the
				// data in the log files
				try {
					resultAutomator.readTotalResults();

					averageFileName = "./results//" + computerName
							+ "_Average_"
							+ simulations.get(j).getSimulationName() + "-" + k;
					varFileName = "./results//" + computerName + "_Var_"
							+ simulations.get(j).getSimulationName() + "-" + k;
					sqrtvarFileName = "./results//" + computerName
							+ "_SqrtVar_"
							+ simulations.get(j).getSimulationName() + "-" + k;

					resultAutomator.resultsAverage(averageFileName);
					resultAutomator.resultsVar(varFileName, sqrtvarFileName,
							averageFileName);
					
					averageFileList.add(averageFileName);
				} catch (IOException e) {
					e.printStackTrace();
				}

				logFile.clear();

				// Write the gnuplot files
				/*for (int z = 0; z < simulations.get(j).getGnuplot().size(); z++) {
					
					
					System.out.println("GNUPLOT section...");
					
					averageFileList.add(averageFileName);
					/**writeGnuPlot(averageFileName, simulations.get(j)
							.getSimulationName()
							+ "-"
							+ simulations.get(j).getGnuplot().get(z)
									.getFileName() + "-" + k, simulations
							.get(j).getGnuplot().get(z).getAxisX(), simulations
							.get(j).getGnuplot().get(z).getAxisY());
					**/
					/*gnuPlotXY(averageFileName, 
							simulations.get(j).getSimulationName() + "-" + simulations.get(j).getGnuplot().get(z).getFileName() + "-" + k,
							simulations.get(j).getGnuplot().get(z).getAxisX(),
							simulations.get(j).getGnuplot().get(z).getAxisY());
							*/
				//}*/
			}
			// -> @author Stefano Sebastio: new gnuplot generator 
			for (int z = 0; z < simulations.get(j).getGnuplot().size(); z++) {
			/*	gnuPlotXY(averageFileList, 
						simulations.get(j).getSimulationName() + "-" + simulations.get(j).getGnuplot().get(z).getFileName() + "-" + z,
						simulations.get(j).getGnuplot().get(z).getAxisX(),
						simulations.get(j).getGnuplot().get(z).getAxisY(),
						simulations.get(j));
				*/
				gnuPlotArray(averageFileList, 
						simulations.get(j).getSimulationName() + "-" + simulations.get(j).getGnuplot().get(z).getFileName() + "-" + z,
						simulations.get(j).getGnuplot().get(z).getAxisX(),
						simulations.get(j).getGnuplot().get(z).getAxisY(),
						simulations.get(j));
			}
			//System.out.println("AverageFileList size " + averageFileList.size());
		}
		//test function
		//this.printSimulations(simulations);

		if (files.size() == 0) {
			new Deus(originalXml, "deus_log");
		}

		// Remove all unused XML files
		for (int i = 0; i < files.size(); i++)
			new File(files.get(i)).delete();
	}

	
	
	/**
	 * 
	 * Generate the supporting files for gnuPlot script
	 * 
	 * @author Stefano Sebastio 
	 * 
	 * @param sourceFile
	 * @param destinationFile
	 * @param xLabel
	 * @param yLabel
	 * @param sim
	 */
	/*
	@Deprecated
	private void gnuPlotXY(ArrayList<String> sourceFiles, String destinationFile, String xLabel, String yLabel, MyObjectSimulation sim){
		//test function
		//printSimulations(simulations);
		//ArrayList<Double> xValues = new ArrayList<Double>();
		//ArrayList<Double> yValues = new ArrayList<Double>();
		//Configuration config = new PropertiesConfiguration("usergui.properties");
		// Add HostName in Destination File
		String computerName = "";
		try {
			computerName = InetAddress.getLocalHost().getHostName();
			// System.out.println(computerName);
		} catch (Exception e) {
			System.out.println("Exception caught =" + e.getMessage());
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("./results/gnuplot/"
					+ computerName + "_" + destinationFile);
			
			PrintStream Output = new PrintStream(fos);
		
		
			for (int i=0; i< sourceFiles.size(); i++){
				String sourceFile = sourceFiles.get(i);
				//System.out.println("reading from " + sourceFile);
				//read the file with average values
				File file = new File(sourceFile);
				FileInputStream fileInput;
				try {
					
					double x = 0;
					double y =0;
					boolean xFound = false;
					boolean yFound = false;
					
					fileInput = new FileInputStream(file);
					Properties properties = new Properties();
					try {
						properties.load(fileInput);
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						fileInput.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					//System.out.println("try to find " + xLabel + " and " + yLabel);
					//read the average value for the x and y value (if both are on the average file)
					if (properties.containsKey(xLabel)) {
						x = Double.parseDouble((String) properties.get(xLabel));
						//System.out.println("X found " + x);
						xFound = true;
					}
					if (properties.containsKey(yLabel)) {
						y = Double.parseDouble((String) properties.get(yLabel));
						//System.out.println("Y found " + y);
						yFound = true;
					}
					
					
					//if both (x and y) don't belong to log variable set the gnuplot construction is prevented
					if (!xFound && !yFound){
						System.err.println("GnuPlot supporting files not created. At least one of the coordinate must be a log variable");
						Output.close();
						return;
					}
					
					if (!xFound){
						//System.out.println("try to use extended research");
						Double x_Resp = findElsewhere(sim, xLabel, i);
						if (x_Resp != null) {
							x = x_Resp;
							xFound = true;
							//System.out.println("then x is " + x);
						}
					}
					if (!yFound){
						//System.out.println("try to use extended research");
						Double y_Resp = findElsewhere(sim, yLabel, i);
						if (y_Resp != null){
							y = y_Resp;
							yFound = true;
							//System.out.println("then y is " + y);
						}
					}

					if (xFound && yFound) {
						Output.println(x + " " + y);
					} else {
						System.err.println("Unable to create the GnuPlot supporting files");
						System.err.println("Impossible to find the gnuplot parameter");
						Output.close();
					}
						
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
	
			}
			
			Output.close();
			
			//Write the script
			String script = "./results/gnuplot/"+ computerName + "_" + destinationFile;
			this.writeGnuPlotScript(script, xLabel, yLabel, destinationFile);
			
			
			
		} catch (FileNotFoundException e1) {
				
				e1.printStackTrace();
			}
	}
	*/
	
	private void gnuPlotArray(ArrayList<String> sourceFiles, String destinationFile, String xLabel, String yLabel, MyObjectSimulation sim){
	
		//Configuration config = new PropertiesConfiguration("usergui.properties");

		String computerName = "";
		try {
			computerName = InetAddress.getLocalHost().getHostName();
			// System.out.println(computerName);
		} catch (Exception e) {
			System.out.println("Exception caught =" + e.getMessage());
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("./results/gnuplot/"
					+ computerName + "_" + destinationFile);
			
			PrintStream Output = new PrintStream(fos);
		
			boolean xArrayFound = false;
			boolean yArrayFound = false;
	
			ArrayList<Double> xArrayList = new ArrayList<Double>();
			ArrayList<Double> yArrayList = new ArrayList<Double>();
			
			for (int i=0; i< sourceFiles.size(); i++){
				String sourceFile = sourceFiles.get(i);
				//System.out.println("reading from " + sourceFile);
				//read the file with average values

				Configuration config = null;
				try {
					config = new PropertiesConfiguration(sourceFile);

					double x = 0;
					double y = 0;
					boolean xFound = false;
					boolean yFound = false;
					
					//fileInput = new FileInputStream(file);
					//Properties properties = new Properties();
					List<Object> xList = config.getList(xLabel);
					List<Object> yList = config.getList(yLabel);
					
					if ( !xFound && ( (xList != null) && ( xList.size() != 0)) ){
						
						if (xList.size() == 1){
							x = Double.parseDouble((String)xList.get(0));
						}
						else {
							//xArrayList = new ArrayList<Double>();
							//yArrayList = new ArrayList<Double>();
							for (Object o : xList){
								xArrayList.add(Double.parseDouble( (String) o) );
							}
							xArrayFound = true;
						}
						
						xFound = true;
					}
					
					if ( !yFound && ( (yList != null) && ( yList.size() != 0)) ){
						
						if (yList.size() == 1){
							y = Double.parseDouble((String)yList.get(0));
						}
						else {
							//xArrayList = new ArrayList<Double>();
							//yArrayList = new ArrayList<Double>();
							for (Object o : yList){
								yArrayList.add(Double.parseDouble( (String) o) );
							}
							yArrayFound = true;
						}
						
						yFound = true;
					}
					
					//if both (x and y) don't belong to log variable set the gnuplot construction is prevented
					if (!xFound && !yFound){
						System.err.println("GnuPlot supporting files not created. At least one of the coordinate must be a log variable");
						Output.close();
						return;
					}
					
					if (!xFound){
						
						Double x_Resp = findElsewhere(sim, xLabel, i);
						if (x_Resp != null){
							
							if (yArrayFound){
								xArrayList.add(x_Resp);
							}
							else{
								x = x_Resp;
							}
							xFound = true;
						}
						
					}
					if (!yFound){
						
						//System.out.println("try to use extended research");
						Double y_Resp = findElsewhere(sim, yLabel, i);
						if (y_Resp != null){
							
							if (xArrayFound){
								yArrayList.add(y_Resp);
							}
							else {
								y = y_Resp;
							}
							yFound = true;
							//System.out.println("then y is " + y);
						}
					}

					if (!xArrayFound && !yArrayFound){
						if (xFound && yFound) {
							Output.println(x + " " + y);
						} else {
							System.err.println("Unable to create the GnuPlot supporting files");
							System.err.println("Impossible to find the gnuplot parameter");
							Output.close();
						}
					}
					if ( (xArrayFound || yArrayFound) && (xArrayList.size() == yArrayList.size()) ){
						
						for (int k = 0; k<xArrayList.size(); k++){
							Output.println(xArrayList.get(k) + " " + yArrayList.get(k));
						}
						xArrayFound = true;
						yArrayFound = true;
						break;
					}
				} catch (ConfigurationException e1) {
					e1.printStackTrace();
				}
	
			}
			
			/*if (xArrayFound || yArrayFound){
				
				if (xArrayList.size() == yArrayList.size()){
					for (int i = 0; i<xArrayList.size(); i++){
						Output.println(xArrayList.get(i) + " " + yArrayList.get(i));
					}
				} else{
					System.err.println("Unable to create the GnuPlot supporting files");
					System.err.println("Impossible to find the gnuplot parameter, parameters length missmatch");
					Output.close();
				}
					
				
			}*/
			if ( (xArrayFound && !yArrayFound) || (yArrayFound && !xArrayFound) ){
				System.err.println("Unable to create the GnuPlot supporting files");
				System.err.println("Impossible to find the gnuplot parameter, parameters length mismatch");
				Output.close();
			}
			
			Output.close();
			
			//Write the script
			String script = "./results/gnuplot/"+ computerName + "_" + destinationFile;
			this.writeGnuPlotScript(script, xLabel, yLabel, destinationFile);
			
			
			
		} catch (FileNotFoundException e1) {
				
				e1.printStackTrace();
			}
		
		
	}
	
	/**
	 * Write a basic gnuplot script for the required parameter
	 * 
	 * @author Stefano Sebastio
	 * 
	 * @param dataFile
	 * @param x
	 * @param y
	 * @param output
	 */
	private void writeGnuPlotScript(String dataFile, String x, String y, String output){
		
		String gnuplot = new String(gnuplotScriptTemplate);
		
		gnuplot = gnuplot.replace(OUTPUT_GNUPLOT, output +".eps");
		gnuplot = gnuplot.replace(XLABEL_GNUPLOT, x);
		gnuplot = gnuplot.replace(YLABEL_GNUPLOT, y);
		gnuplot = gnuplot.replace(SOURCE_GNUPLOT, dataFile);
		gnuplot = gnuplot.replace(LINELABEL_GNUPLOT, output);
		
		FileOutputStream fos;
			
		try {
			fos = new FileOutputStream(dataFile+".plt");
			PrintStream outputStream = new PrintStream(fos);
			outputStream.println(gnuplot);
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/*//Test functions for explore the gui paramters
	public void printSimulations(ArrayList<MyObjectSimulation> simulations){
		for (int i = 0;  i< simulations.size(); i++){
			System.out.println("Sim " + i);
			MyObjectSimulation sim = simulations.get(i);
			ArrayList<ArrayList<MyObjectNode>> node = sim.getNode();
			this.printNodes(node);
		}
	}
	
	public void printNodes(ArrayList<ArrayList<MyObjectNode>> node){
		for(int i=0; i< node.size(); i++){
			ArrayList<MyObjectNode> innerNode = node.get(i);
			for (int j=0; j< innerNode.size(); j++){
				MyObjectNode objNode = innerNode.get(j);
				System.out.println(objNode.getObjectName() + " " + objNode.getObjectParam());
				for (int k =0; k< objNode.getObjectParam().size(); k++){//list the different parameters of the same node
					MyObjectParam params = objNode.getObjectParam().get(k);
					System.out.print(j + " is j"+ k + " is k " + params.getObjectName() + " " + params.getObjectParam() + " " + params.getObjectValue());
				}
				System.out.println("...");
			}
		}
	}
	*/
	
	/**
	 * Starting the search for gnuplot parameter not presents on the log file
	 * 
	 * @author Stefano Sebastio
	 * 
	 * @param sim
	 * @param label
	 * @param elemPos
	 * @return
	 */
	private Double findElsewhere(MyObjectSimulation sim, String label, int elemPos){
		Double foundOnNode = findOnNode(sim.getNode(), label, elemPos);
		//System.out.println("Looking for... " + label);
		if (foundOnNode != null){
			return foundOnNode;
		} else {
			//System.out.println("Nothing found on node");
			//search on other elements -> Node Resources
			Double foundOnResource = findOnNodeResource(sim.getNode(), label, elemPos);
			if (foundOnResource != null){
				return foundOnResource;
			} else {
				//System.out.println("Nothing found on node resource");
				//search on other  elements -> Process
				Double foundOnProcess = findOnProcess(sim.getProcess(), label, elemPos);
				if (foundOnProcess != null){
					return foundOnProcess;
				} /*else {
					System.out.println("Nothing found elsewhere");
				}*/
			}
			
		}
		//System.err.println("Impossible to find the gnuplot parameter");
		return null;
	}
	
	/**
	 * Search for the requested gnuplot value on parameterized node values 
	 * 
	 * @author Stefano Sebastio
	 * 
	 * @param node
	 * @param label
	 * @param pos
	 * @return
	 */
	private Double findOnNode(ArrayList<ArrayList<MyObjectNode>> node, String label, int pos){
		//ArrayList<Double> values = new ArrayList<Double>();
		Double value = null;
		for(int i=0; i< node.size(); i++){
			ArrayList<MyObjectNode> innerNode = node.get(i);
			//for (int j=0; j< innerNode.size(); j++){
				MyObjectNode objNode = innerNode.get(pos);
				if (objNode.getObjectParam().size() != 0) {
					for (int k =0; k< objNode.getObjectParam().size(); k++){ 
						MyObjectParam params = objNode.getObjectParam().get(k);
						//MyObjectParam params = objNode.getObjectParam().get(0);
						if(params.getObjectName().contains(label)){
							//values.add(params.getObjectValue());
							//System.out.println("Found the label " + params.getObjectName());
							value = new Double(params.getObjectValue());
							return value;
						}
					}
				}
			//}
		}
		return null;
	}
	
	/**
	 * Search for the requested gnuplot value on parameterized node resource values 
	 * 
	 * @author Stefano Sebastio
	 * 
	 * @param node
	 * @param label
	 * @param pos
	 * @return
	 */
	private Double findOnNodeResource(ArrayList<ArrayList<MyObjectNode>> node, String label, int pos){
		Double value = null;
		for(int i=0; i< node.size(); i++){
			ArrayList<MyObjectNode> innerNode = node.get(i);
			//for (int j=0; j< innerNode.size(); j++){
				MyObjectNode objNode = innerNode.get(pos);
				if (objNode.getObjectResourceParam().size() != 0) {
					for (int k =0; k< objNode.getObjectParam().size(); k++){	
						MyObjectResourceParam params = objNode.getObjectResourceParam().get(k);
						if(params.getObjectHandlerName().contains(label)){
							value = new Double(params.getObjectValue());
							return value;
						}
					}
				}
			//}
		}
		return null;
	}
	
	/**
	 * Search for the requested gnuplot value on parameterized process values 
	 * 
	 * @author Stefano Sebastio
	 * 
	 * @param process
	 * @param label
	 * @param pos
	 * @return
	 */
	private Double findOnProcess(ArrayList<ArrayList<MyObjectProcess>> process, String label, int pos){
		Double value = null;
		for(int i=0; i< process.size(); i++){
			ArrayList<MyObjectProcess> innerProcess = process.get(i);
			//for (int j=0; j< innerProcess.size(); j++){
				MyObjectProcess objProcess = innerProcess.get(pos);
				if (objProcess.getObjectParam().size() != 0) {
					for (int k =0; k< objProcess.getObjectParam().size(); k++){	
						MyObjectParam params = objProcess.getObjectParam().get(k);
						if(params.getObjectName().contains(label)){
							value = new Double(params.getObjectValue());
							return value;
						}
					}
				}
			//}
		}
		return null;	
	}
	
	/**
	 * Check if the gnuPlot configuration form DEUS GUI is admissible.
	 * To be acceptable at least one of the two parameters must be a log variable.  
	 * 
	 * @author Stefano Sebastio
	 * 
	 * @return
	 */
	public boolean checkGnuPlotIncompatibility(){
		
		try {
			simulations = readXML(automatorXml);
		} catch (DeusAutomatorException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int j = 0; j < simulations.size(); j++) {
			for (int z = 0; z < simulations.get(j).getGnuplot().size(); z++) {
				String xLabel = simulations.get(j).getGnuplot().get(z).getAxisX();
				String yLabel = simulations.get(j).getGnuplot().get(z).getAxisY();
				
				
				Double xFindResult = findElsewhere(simulations.get(j), xLabel, j);
				Double yFindResult = findElsewhere(simulations.get(j), yLabel, j);
				
				if ( (xFindResult != null) && (yFindResult != null) )
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Template for the gnuplot script
	 */
	private static final String gnuplotScriptTemplate = "set term postscript eps enhanced color\nset output \"OUTPUT.EPS\"\n"+
									"set xlabel \"XLABEL\"\nset ylabel \"YLABEL\"\n" +
									"set xtics border in scale 1,0.5 mirror norotate offset character 0, 0, 0\nset xtics autofreq  norangelimit\n"+
									"set ytics border in scale 1,0.5 mirror norotate offset character 0, 0, 0\nset ytics autofreq  norangelimit\n" +
									"plot 'SOURCE.TXT' title \"LINELABEL\" with linespoints";
	private static final String OUTPUT_GNUPLOT = "OUTPUT.EPS";
	private static final String XLABEL_GNUPLOT = "XLABEL";
	private static final String YLABEL_GNUPLOT = "YLABEL";
	private static final String SOURCE_GNUPLOT = "SOURCE.TXT";
	private static final String LINELABEL_GNUPLOT = "LINELABEL";
	
	

	/**
	 * Method for writing gnuplot files
	 * 
	 * @param sourceFile
	 *            , name of the file from which data must be read
	 * @param destinationFile
	 *            , name of the file to which results must be written
	 * @param axisX
	 * @param axisY
	 */
	/*
	private void writeGnuPlot(String sourceFile, String destinationFile,
			String axisX, String axisY) {

		try {
			File f = new File(sourceFile);
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			// Add HostName in Destination File
			String computerName = "";
			try {
				computerName = InetAddress.getLocalHost().getHostName();
				// System.out.println(computerName);
			} catch (Exception e) {
				System.out.println("Exception caught =" + e.getMessage());
			}

			FileOutputStream fos = new FileOutputStream("./results/gnuplot/"
					+ computerName + "_" + destinationFile);

			String linea = br.readLine();

			linea = linea.trim();
			axisX = axisX.trim();
			axisY = axisY.trim();

			String app = "";
			boolean assex = false;
			boolean assey = false;

			while (linea != null) {

				String firstString = linea.split("=")[0];

				if (firstString.equals(axisX) && assey == false) {

					assex = true;
					String val = linea.substring(linea.indexOf('=') + 1,
							linea.length());
					val = val + " ";
					fos.write(val.getBytes());
				}

				else if (firstString.equals(axisY) && assex == true) {
					assey = true;
					String val = linea.substring(linea.indexOf('=') + 1,
							linea.length());
					val = val + " ";
					fos.write(val.getBytes());
					fos.write("\n".getBytes());
					assex = false;
					assey = false;
				}

				else if (firstString.equals(axisY) && assex == false) {
					assey = true;
					app = linea.substring(linea.indexOf('=') + 1,
							linea.length());
					app = app + " ";
				}

				else if (firstString.equals(axisX) && assey == true) {

					String val = linea.substring(linea.indexOf('=') + 1,
							linea.length());
					val = val + " ";
					fos.write(val.getBytes());
					fos.write(app.getBytes());
					fos.write("\n".getBytes());
					assex = false;
					assey = false;
				}

				linea = br.readLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	 */
	
	/**
	 * Method that computes the paramaters of the simulations
	 * 
	 * @param initialValue
	 *            , initial value
	 * @param finalValue
	 *            , final value
	 * @param stepValue
	 *            , step
	 * @return ArrayList<> contains all the values between initialValue and
	 *         finalValue
	 */
	private ArrayList<Float> calculateParameters(String initialValue,
			String finalValue, String stepValue) {

		ArrayList<Float> parameters = new ArrayList<Float>();

		if (Float.parseFloat(stepValue) != 0
				&& (Float.parseFloat(initialValue) < Float
						.parseFloat(finalValue)))
			for (float i = Float.parseFloat(initialValue); i <= Float
					.parseFloat(finalValue) + 0.01; i = i
					+ Float.parseFloat(stepValue)) {
				parameters.add(i);
			}

		else if (Float.parseFloat(stepValue) != 0
				&& (Float.parseFloat(initialValue) > Float
						.parseFloat(finalValue)))
			for (float i = Float.parseFloat(initialValue); i >= Float
					.parseFloat(finalValue); i = i
					- Float.parseFloat(stepValue)) {
				parameters.add(i);
			}

		return parameters;
	}

	/**
	 * Method that reads the XML file for the atuomation of simulations
	 * 
	 * @param path
	 *            , path of the file to read
	 * @return an ArrayList<> containing the simulations to perform
	 * @throws DeusAutomatorException
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws IOException
	 */
	private ArrayList<MyObjectSimulation> readXML(String path)
			throws DeusAutomatorException, JAXBException, SAXException,
			IOException {
		JAXBContext jc = JAXBContext
				.newInstance("it.unipr.ce.dsg.deus.schema.automator");
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = schemaFactory.newSchema(new File(
				"schema/automator/deusAutomator.xsd"));

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		unmarshaller.setSchema(schema);
		//System.out.println("inside readXML");
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

			document = builder.parse(f);

			document.getDocumentElement().normalize();

			// root element
			NodeList simulationLst = document
					.getElementsByTagName("simulation");

			ArrayList<MyObjectSimulation> simulation = new ArrayList<MyObjectSimulation>();

			for (int w = 0; w < simulationLst.getLength(); w++) {

				MyObjectSimulation sim = new MyObjectSimulation();

				Node fstSimulation = simulationLst.item(w);

				if (fstSimulation.getAttributes()
						.getNamedItem("simulationName").getNodeValue() == null
						|| fstSimulation.getAttributes()
								.getNamedItem("simulationNumberSeed")
								.getNodeValue() == null)
					throw new DeusAutomatorException(
							"Errore manca simulationNumberSeed e/o simulationName nel tag simulation");

				String resultFolder = null;
				String inputFolder = null;

				String simulationNumberSeed = fstSimulation.getAttributes()
						.getNamedItem("simulationNumberSeed").getNodeValue();
				
				//System.out.println("simulationNumberSeed "  + simulationNumberSeed);
				
				//System.out.println(simulationNumberSeed);

				String simulationName = fstSimulation.getAttributes()
						.getNamedItem("simulationName").getNodeValue();

				if (fstSimulation.getAttributes().getNamedItem("resultFolder") != null)
					resultFolder = fstSimulation.getAttributes()
							.getNamedItem("resultFolder").getNodeValue();

				if (fstSimulation.getAttributes().getNamedItem("inputFolder") != null)
					inputFolder = fstSimulation.getAttributes()
							.getNamedItem("inputFolder").getNodeValue();

				sim.setResultFolder(resultFolder);
				sim.setInputFolder(inputFolder);
				sim.setSimulationName(simulationName);
				sim.setSimulationNumberSeed(simulationNumberSeed);

				NodeList nodeLst = document.getElementsByTagName("node");

				ArrayList<ArrayList<MyObjectNode>> nodes2 = new ArrayList<ArrayList<MyObjectNode>>();
				ArrayList<ArrayList<MyObjectProcess>> processes2 = new ArrayList<ArrayList<MyObjectProcess>>();

				// Search all tag nodes
				for (int s = 0; s < nodeLst.getLength(); s++) {

					Node fstNode = nodeLst.item(s);

					if (fstNode.getParentNode().equals(simulationLst.item(w))) {

						ArrayList<MyObjectNode> nodes = new ArrayList<MyObjectNode>();

						String messageType = fstNode.getAttributes()
								.getNamedItem("id").getNodeValue();

						Element fstElmnt = (Element) fstNode;
						NodeList fstNmElmntLst = fstElmnt
								.getElementsByTagName("paramName");

						// Retrieve params in node's ParamNode
						for (int j = 0; j < fstNmElmntLst.getLength(); j++) {

							Element paramElement = (Element) fstNmElmntLst
									.item(j);

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

							ArrayList<Float> value = calculateParameters(
									initialValue.item(0).getTextContent(),
									finalValue.item(0).getTextContent(),
									stepValue.item(0).getTextContent());

							if (value.size() > 0)
								sim.setStep(value.size());

							if (Float.parseFloat(stepValue.item(0)
									.getTextContent()) == 0.0) {
								MyObjectParam paramToWrite = new MyObjectParam();
								paramToWrite.setObjectParam("paramName");
								paramToWrite.setObjectName(paramName);
								paramToWrite.setObjectValue(Float
										.parseFloat(initialValue.item(0)
												.getTextContent()));

								MyObjectNode nodeToWrite = new MyObjectNode();
								nodeToWrite.setObjectName(messageType);
								nodeToWrite.getObjectParam().add(paramToWrite);
								writeXmlNodeParam(nodeToWrite);

							}

							if (numSim == 0)
								numSim = value.size();

							if (numSim != value.size()) {
								throw new DeusAutomatorException(
										"Errore nel numero di step in "
												+ simulationName + " di Node "
												+ messageType + " in "
												+ paramName);
							}

							for (int k = 0; k < value.size(); k++) {
								MyObjectParam param = new MyObjectParam();

								param.setObjectParam("paramName");
								param.setObjectName(paramName);
								param.setObjectValue(value.get(k));
								if (nodes.size() > k)
									nodes.get(k).getObjectParam().add(param);

								else {
									MyObjectNode node = new MyObjectNode();
									node.setObjectName(messageType);
									node.getObjectParam().add(param);
									nodes.add(node);
								}
							}
						}

						NodeList paramName = fstElmnt
								.getElementsByTagName("resourceParamName");

						// Retrieva all params in node's resourceParamName
						for (int j = 0; j < paramName.getLength(); j++) {
							Element paramElement = (Element) paramName.item(j);

							String handlerName = ((Node) paramName.item(j))
									.getAttributes()
									.getNamedItem("handlerName").getNodeValue();

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
										"Errore in initialValue , finalValue e stepValue in "
												+ simulationName + " di Node"
												+ messageType + " in "
												+ paramName);
							}

							ArrayList<Float> value = calculateParameters(
									initialValue.item(0).getTextContent(),
									finalValue.item(0).getTextContent(),
									stepValue.item(0).getTextContent());

							if (value.size() > 0)
								sim.setStep(value.size());

							if (Float.parseFloat(stepValue.item(0)
									.getTextContent()) == 0.0) {
								MyObjectResourceParam resourceParam = new MyObjectResourceParam();

								resourceParam
										.setObjectParam("resourceParamName");

								resourceParam.setObjectHandlerName(handlerName);
								resourceParam
										.setResParamValue(resParamValueName);

								resourceParam.setObjectValue(Float
										.parseFloat(initialValue.item(0)
												.getTextContent()));

								MyObjectNode nodeToWrite = new MyObjectNode();
								nodeToWrite.setObjectName(messageType);
								nodeToWrite.getObjectResourceParam().add(
										resourceParam);

								writeXmlNodeResource(nodeToWrite);

							}

							if (numSim == 0)
								numSim = value.size();

							if (numSim != value.size()) {
								throw new DeusAutomatorException(
										"Errore nel numero di step in "
												+ simulationName + " di Node "
												+ messageType + " in "
												+ paramName);
							}

							for (int k = 0; k < value.size(); k++) {
								MyObjectResourceParam resourceParam = new MyObjectResourceParam();

								resourceParam
										.setObjectParam("resourceParamName");

								resourceParam.setObjectHandlerName(handlerName);
								resourceParam
										.setResParamValue(resParamValueName);

								resourceParam.setObjectValue(value.get(k));
								if (nodes.size() > k)
									nodes.get(k).getObjectResourceParam()
											.add(resourceParam);

								else {
									MyObjectNode node = new MyObjectNode();
									node.setObjectName(messageType);
									node.getObjectResourceParam().add(
											resourceParam);
									nodes.add(node);
								}

							}
						}

						nodes2.add(nodes);
					}

				}

				if (nodes2.size() > 0)
					sim.setNode(nodes2);

				if (new Integer(sim.getNode().size()) > 0)
					sim.setSimulationNumber(new Integer(sim.getNode().get(0)
							.size()));

				NodeList processLst = document.getElementsByTagName("process");

				// Search all process tags
				for (int s = 0; s < processLst.getLength(); s++) {

					Node fstNode = processLst.item(s);

					if (fstNode.getParentNode().equals(simulationLst.item(w))) {

						ArrayList<MyObjectProcess> processes = new ArrayList<MyObjectProcess>();

						String messageType = fstNode.getAttributes()
								.getNamedItem("id").getNodeValue();

						Element fstElmnt = (Element) fstNode;
						NodeList fstNmElmntLst = fstElmnt
								.getElementsByTagName("paramName");

						// Retrieve all params in process' ParamName
						for (int j = 0; j < fstNmElmntLst.getLength(); j++) {

							Element paramElement = (Element) fstNmElmntLst
									.item(j);

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
												+ simulationName
												+ " di Process " + messageType
												+ " in " + paramName);
							}

							ArrayList<Float> value = calculateParameters(
									initialValue.item(0).getTextContent(),
									finalValue.item(0).getTextContent(),
									stepValue.item(0).getTextContent());

							if (value.size() > 0)
								sim.setStep(value.size());

							if (Float.parseFloat(stepValue.item(0)
									.getTextContent()) == 0.0) {
								MyObjectParam param = new MyObjectParam();

								param.setObjectParam("paramName");

								param.setObjectName(paramName);

								param.setObjectValue(Float
										.parseFloat(initialValue.item(0)
												.getTextContent()));

								MyObjectProcess processToWrite = new MyObjectProcess();
								processToWrite.setObjectName(messageType);
								processToWrite.getObjectParam().add(param);

								writeXmlProcess(processToWrite);

							}

							if (numSim == 0)
								numSim = value.size();

							if (numSim != value.size()) {
								throw new DeusAutomatorException(
										"Errore nel numero di step in "
												+ simulationName
												+ " di Process " + messageType
												+ " in " + paramName);
							}

							for (int k = 0; k < value.size(); k++) {
								MyObjectParam param = new MyObjectParam();

								param.setObjectParam("paramName");

								param.setObjectName(paramName);

								param.setObjectValue(value.get(k));

								if (processes.size() > k)
									processes.get(k).getObjectParam()
											.add(param);

								else {
									MyObjectProcess process = new MyObjectProcess();
									process.setObjectName(messageType);
									process.getObjectParam().add(param);
									processes.add(process);
								}

							}

						}
						if (processes.size() > 0)
							processes2.add(processes);
					}
				}
				if (processes2.size() > 0)
					sim.setProcess(processes2);

				numSim = 0;

				if (sim.getSimulationNumber() == 0)
					if (sim.getProcess().size() > 0)

						sim.setSimulationNumber(new Integer(sim.getProcess()
								.get(0).size()));

				NodeList engineLst = document.getElementsByTagName("engine");

				// Search engine tags
				for (int s = 0; s < engineLst.getLength(); s++) {

					MyObjectEngine engine = new MyObjectEngine();

					Node fstNode = engineLst.item(s);

					if (fstNode.getParentNode().equals(simulationLst.item(w))) {

						String startVt = "";
						String endVt = "";
						String stepVt = "";

						ArrayList<Float> value = new ArrayList<Float>();
						boolean vt = true;

						if (fstNode.getAttributes().getNamedItem("startVT") != null)
							startVt = fstNode.getAttributes()
									.getNamedItem("startVT").getNodeValue();

						else
							vt = false;

						if (fstNode.getAttributes().getNamedItem("endVT") != null)
							endVt = fstNode.getAttributes()
									.getNamedItem("endVT").getNodeValue();

						else
							vt = false;

						if (fstNode.getAttributes().getNamedItem("stepVT") != null)
							stepVt = fstNode.getAttributes()
									.getNamedItem("stepVT").getNodeValue();

						else
							vt = false;

						if (vt)
							value = calculateParameters(startVt, endVt, stepVt);

						for (int k = 0; k < value.size(); k++) {
							engine.getVt().add(value.get(k));
						}

						Element fstElmnt = (Element) fstNode;
						NodeList fstNmElmntLst = fstElmnt
								.getElementsByTagName("seed");
						
						//System.out.println("fstNmElmntLst.getLength() = " + fstNmElmntLst.getLength());

						// Retrieve all seedValues in seed
						for (int j = 0; j < fstNmElmntLst.getLength(); j++) {

							Element paramElement = (Element) fstNmElmntLst
									.item(j);

							NodeList seedValue = paramElement
									.getElementsByTagName("seedValue");

							if (seedValue.getLength() > Integer.parseInt(sim
									.getSimulationNumberSeed())) {
								throw new DeusAutomatorException(
										"Errore nel numero di seed inseriti, TROPPI rispetto a quelli indicati nel tag simulation in "
												+ simulationName);
							}

							if (seedValue.getLength() < Integer.parseInt(sim
									.getSimulationNumberSeed())) {
								throw new DeusAutomatorException(
										"Errore nel numero di seed inseriti, POCHI rispetto a quelli indicati nel tag simulation in "
												+ simulationName);
							}

							//System.out.println("seedValue.getLength() = " + seedValue.getLength());
							
							for (int o = 0; o < seedValue.getLength(); o++) {
								Node seedvalue = seedValue.item(o);
								engine.getSeed()
										.add(seedvalue.getTextContent());
							}

						}

					}
					// Add engine to simulation
					sim.getEngine().add(engine);
				}

				if (sim.getSimulationNumber() == 0)
					sim.setSimulationNumber(1);

				NodeList resultLogLst = document
						.getElementsByTagName("resultVT");

				for (int i = 0; i < resultLogLst.getLength(); i++) {

					Node fileLog = resultLogLst.item(i);

					if (fileLog.getParentNode().equals(simulationLst.item(w))) {

						sim.setFileLog(fileLog.getAttributes()
								.getNamedItem("outputLogFile").getNodeValue());
					}

				}

				NodeList GnuPlotLst = document
						.getElementsByTagName("resultXYFile");

				for (int i = 0; i < GnuPlotLst.getLength(); i++) {

					Node GnuPlotNode = GnuPlotLst.item(i);

					if (GnuPlotNode.getParentNode().equals(
							simulationLst.item(w))) {

						MyObjectGnuplot gnuplot = new MyObjectGnuplot();

						gnuplot.setFileName(GnuPlotNode.getAttributes()
								.getNamedItem("fileName").getNodeValue());

						gnuplot.setAxisX(GnuPlotNode.getAttributes()
								.getNamedItem("axisX").getNodeValue());

						gnuplot.setAxisY(GnuPlotNode.getAttributes()
								.getNamedItem("axisY").getNodeValue());

						sim.getGnuplot().add(gnuplot);
					}

				}

				simulation.add(sim);

			}

			return simulation;
		} catch (SAXException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}
		return null;

	}

	private void writeXmlNodeParam(MyObjectNode nodeToWrite)
			throws IOException, ParserConfigurationException, SAXException {

		FileOutputStream fos = new FileOutputStream(originalXML + ".temp");

		NodeList node = doc.getElementsByTagName("aut:node");
		for (int i = 0; i < node.getLength(); i++) {
			if (node.item(i).getAttributes().getNamedItem("id").getNodeValue()
					.equals(nodeToWrite.getObjectName())) {
				for (int l = 0; l < nodeToWrite.getObjectParam().size(); l++) {
					for (int m = 0; m < node.item(i).getChildNodes()
							.getLength(); m++)
						for (int b = 0; b < node.item(i).getChildNodes()
								.item(m).getChildNodes().getLength(); b++)
							if (node.item(i).getChildNodes().item(m)
									.getChildNodes().item(b).getNodeName()
									.equals("aut:param"))
								if (node.item(i)
										.getChildNodes()
										.item(m)
										.getChildNodes()
										.item(b)
										.getAttributes()
										.getNamedItem("name")
										.getNodeValue()
										.equals(nodeToWrite.getObjectParam()
												.get(l).getObjectName())) {
									node.item(i)
											.getChildNodes()
											.item(m)
											.getChildNodes()
											.item(b)
											.getAttributes()
											.getNamedItem("value")
											.setNodeValue(
													((Double) nodeToWrite
															.getObjectParam()
															.get(l)
															.getObjectValue())
															.toString());
								}
				}
			}
		}

		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();

			transformer.transform(domSource, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		fos.write(writer.toString().getBytes());
		
		fos.close();
	}

	private void writeXmlProcess(MyObjectProcess processToWrite)
			throws IOException, ParserConfigurationException, SAXException {

		FileOutputStream fos = new FileOutputStream(originalXML + ".temp");

		NodeList process = doc.getElementsByTagName("aut:process");
		for (int i = 0; i < process.getLength(); i++) {
			if (process.item(i).getAttributes().getNamedItem("id")
					.getNodeValue().equals(processToWrite.getObjectName())) {
				for (int l = 0; l < processToWrite.getObjectParam().size(); l++)
					for (int m = 0; m < process.item(i).getChildNodes()
							.getLength(); m++) {
						if (process.item(i).getChildNodes().item(m)
								.getNodeName().equals("aut:params"))
							for (int b = 0; b < process.item(i).getChildNodes()
									.item(m).getChildNodes().getLength(); b++)
								if (process.item(i).getChildNodes().item(m)
										.getChildNodes().item(b).getNodeName()
										.equals("aut:param")) {
									// System.out.println(process.item(i).getAttributes().getNamedItem("id").getNodeValue());
									if (process
											.item(i)
											.getChildNodes()
											.item(m)
											.getChildNodes()
											.item(b)
											.getAttributes()
											.getNamedItem("name")
											.getNodeValue()
											.equals(processToWrite
													.getObjectParam().get(l)
													.getObjectName())) {
										process.item(i)
												.getChildNodes()
												.item(m)
												.getChildNodes()
												.item(b)
												.getAttributes()
												.getNamedItem("value")
												.setNodeValue(
														((Double) processToWrite
																.getObjectParam()
																.get(l)
																.getObjectValue())
																.toString());
									}
								}
					}

			}
		}

		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		fos.write(writer.toString().getBytes());
		
		fos.close();

	}

	private void writeXmlNodeResource(MyObjectNode nodeToWrite)
			throws IOException, ParserConfigurationException, SAXException {

		FileOutputStream fos = new FileOutputStream(originalXML + ".temp");

		NodeList node = doc.getElementsByTagName("aut:node");
		for (int i = 0; i < node.getLength(); i++) {
			if (node.item(i).getAttributes().getNamedItem("id").getNodeValue()
					.equals(nodeToWrite.getObjectName())) {
				for (int l = 0; l < nodeToWrite.getObjectResourceParam().size(); l++) {
					for (int m = 0; m < node.item(i).getChildNodes()
							.getLength(); m++) {
						if (node.item(i).getChildNodes().item(m).getNodeName()
								.equals("aut:resources"))
							;
						for (int v = 0; v < node.item(i).getChildNodes()
								.item(m).getChildNodes().getLength(); v++)
							if (node.item(i).getChildNodes().item(m)
									.getChildNodes().item(v).getNodeName()
									.equals("aut:resource")
									&& node.item(i)
											.getChildNodes()
											.item(m)
											.getChildNodes()
											.item(v)
											.getAttributes()
											.getNamedItem("handler")
											.getNodeValue()
											.equals(nodeToWrite
													.getObjectResourceParam()
													.get(l)
													.getObjectHandlerName()))
								for (int z = 0; z < node.item(i)
										.getChildNodes().item(m)
										.getChildNodes().item(v)
										.getChildNodes().getLength(); z++)
									if (node.item(i).getChildNodes().item(m)
											.getChildNodes().item(v)
											.getChildNodes().item(z)
											.getNodeName().equals("aut:params"))
										for (int x = 0; x < node.item(i)
												.getChildNodes().item(m)
												.getChildNodes().item(v)
												.getChildNodes().item(z)
												.getChildNodes().getLength(); x++)
											if (node.item(i).getChildNodes()
													.item(m).getChildNodes()
													.item(v).getChildNodes()
													.item(z).getChildNodes()
													.item(x).getNodeName()
													.equals("aut:param"))
												if (node.item(i)
														.getChildNodes()
														.item(m)
														.getChildNodes()
														.item(v)
														.getChildNodes()
														.item(z)
														.getChildNodes()
														.item(x)
														.getAttributes()
														.getNamedItem("name")
														.getNodeValue()
														.equals("amount")) {
													node.item(i)
															.getChildNodes()
															.item(m)
															.getChildNodes()
															.item(v)
															.getChildNodes()
															.item(z)
															.getChildNodes()
															.item(x)
															.getAttributes()
															.getNamedItem(
																	"value")
															.setNodeValue(
																	((Double) nodeToWrite
																			.getObjectResourceParam()
																			.get(l)
																			.getObjectValue())
																			.toString());
												}

					}
				}
			}
		}

		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		fos.write(writer.toString().getBytes());
		
		fos.close();

	}

	/**
	 * Method that writes the XML files to be used when Deus is launched
	 * 
	 * @param simulation
	 *            , the list of simulations to perform
	 * @param path
	 *            , path of the base XML file to modify
	 * @return the names of the files to run
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	private ArrayList<String> writeXML(
			ArrayList<MyObjectSimulation> simulation, String path)
			throws JAXBException, SAXException, IOException,
			ParserConfigurationException {

		ArrayList<String> xmlFile = new ArrayList<String>();

		try {
			FileOutputStream simul = new FileOutputStream("simulations");
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			doc.getDocumentElement().normalize();

			for (int j = 0; j < simulation.size(); j++) {
				int end = 0;
				File f = new File(path + ".temp");

				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(f);
				end = simulation.get(j).getStep();

				if (simulation.get(j).getNode().size() > 0
						|| simulation.get(j).getProcess().size() > 0)
					for (int k = 0; k < end; k++) {
						simul.write(("\n"
								+ simulation.get(j).getSimulationName() + "-"
								+ k + "\n").getBytes());
						for (int u = 0; u < simulation.get(j).getNode().size(); u++) {
							NodeList node = doc
									.getElementsByTagName("aut:node");
							for (int i = 0; i < node.getLength(); i++) {
								if (simulation.get(j).getNode().get(u).size() > 0)
									if (node.item(i)
											.getAttributes()
											.getNamedItem("id")
											.getNodeValue()
											.equals(simulation.get(j).getNode()
													.get(u).get(k)
													.getObjectName())) {
										simul.write(("Node : "
												+ node.item(i).getAttributes()
														.getNamedItem("id")
														.getNodeValue() + "\n")
												.getBytes());
										for (int l = 0; l < simulation.get(j)
												.getNode().get(u).get(k)
												.getObjectParam().size(); l++) {
											for (int m = 0; m < node.item(i)
													.getChildNodes()
													.getLength(); m++)
												for (int b = 0; b < node
														.item(i)
														.getChildNodes()
														.item(m)
														.getChildNodes()
														.getLength(); b++)
													if (node.item(i)
															.getChildNodes()
															.item(m)
															.getChildNodes()
															.item(b)
															.getNodeName()
															.equals("aut:param"))
														if (node.item(i)
																.getChildNodes()
																.item(m)
																.getChildNodes()
																.item(b)
																.getAttributes()
																.getNamedItem(
																		"name")
																.getNodeValue()
																.equals(simulation
																		.get(j)
																		.getNode()
																		.get(u)
																		.get(k)
																		.getObjectParam()
																		.get(l)
																		.getObjectName())) {
															simul.write(("Parameter : "
																	+ node.item(
																			i)
																			.getChildNodes()
																			.item(m)
																			.getChildNodes()
																			.item(b)
																			.getAttributes()
																			.getNamedItem(
																					"name")
																			.getNodeValue() + " ")
																	.getBytes());
															simul.write((((Double) simulation
																	.get(j)
																	.getNode()
																	.get(u)
																	.get(k)
																	.getObjectParam()
																	.get(l)
																	.getObjectValue())
																	.toString() + "\n")
																	.getBytes());
															node.item(i)
																	.getChildNodes()
																	.item(m)
																	.getChildNodes()
																	.item(b)
																	.getAttributes()
																	.getNamedItem(
																			"value")
																	.setNodeValue(
																			((Double) simulation
																					.get(j)
																					.getNode()
																					.get(u)
																					.get(k)
																					.getObjectParam()
																					.get(l)
																					.getObjectValue())
																					.toString());
														}
										}

										for (int l = 0; l < simulation.get(j)
												.getNode().get(u).get(k)
												.getObjectResourceParam()
												.size(); l++) {
											for (int m = 0; m < node.item(i)
													.getChildNodes()
													.getLength(); m++) {
												if (node.item(i)
														.getChildNodes()
														.item(m)
														.getNodeName()
														.equals("aut:resources"))
													;
												for (int v = 0; v < node
														.item(i)
														.getChildNodes()
														.item(m)
														.getChildNodes()
														.getLength(); v++)
													if (node.item(i)
															.getChildNodes()
															.item(m)
															.getChildNodes()
															.item(v)
															.getNodeName()
															.equals("aut:resource")
															&& node.item(i)
																	.getChildNodes()
																	.item(m)
																	.getChildNodes()
																	.item(v)
																	.getAttributes()
																	.getNamedItem(
																			"handler")
																	.getNodeValue()
																	.equals(simulation
																			.get(j)
																			.getNode()
																			.get(u)
																			.get(k)
																			.getObjectResourceParam()
																			.get(l)
																			.getObjectHandlerName()))
														for (int z = 0; z < node
																.item(i)
																.getChildNodes()
																.item(m)
																.getChildNodes()
																.item(v)
																.getChildNodes()
																.getLength(); z++)
															if (node.item(i)
																	.getChildNodes()
																	.item(m)
																	.getChildNodes()
																	.item(v)
																	.getChildNodes()
																	.item(z)
																	.getNodeName()
																	.equals("aut:params"))
																for (int x = 0; x < node
																		.item(i)
																		.getChildNodes()
																		.item(m)
																		.getChildNodes()
																		.item(v)
																		.getChildNodes()
																		.item(z)
																		.getChildNodes()
																		.getLength(); x++)
																	if (node.item(
																			i)
																			.getChildNodes()
																			.item(m)
																			.getChildNodes()
																			.item(v)
																			.getChildNodes()
																			.item(z)
																			.getChildNodes()
																			.item(x)
																			.getNodeName()
																			.equals("aut:param"))
																		if (node.item(
																				i)
																				.getChildNodes()
																				.item(m)
																				.getChildNodes()
																				.item(v)
																				.getChildNodes()
																				.item(z)
																				.getChildNodes()
																				.item(x)
																				.getAttributes()
																				.getNamedItem(
																						"name")
																				.getNodeValue()
																				.equals("amount")) {
																			simul.write(("Resource Parameter : "
																					+ node.item(
																							i)
																							.getChildNodes()
																							.item(m)
																							.getChildNodes()
																							.item(v)
																							.getChildNodes()
																							.item(z)
																							.getChildNodes()
																							.item(x)
																							.getAttributes()
																							.getNamedItem(
																									"name")
																							.getNodeValue() + " ")
																					.getBytes());
																			simul.write((((Double) simulation
																					.get(j)
																					.getNode()
																					.get(u)
																					.get(k)
																					.getObjectResourceParam()
																					.get(l)
																					.getObjectValue())
																					.toString() + "\n")
																					.getBytes());
																			node.item(
																					i)
																					.getChildNodes()
																					.item(m)
																					.getChildNodes()
																					.item(v)
																					.getChildNodes()
																					.item(z)
																					.getChildNodes()
																					.item(x)
																					.getAttributes()
																					.getNamedItem(
																							"value")
																					.setNodeValue(
																							((Double) simulation
																									.get(j)
																									.getNode()
																									.get(u)
																									.get(k)
																									.getObjectResourceParam()
																									.get(l)
																									.getObjectValue())
																									.toString());
																		}
											}
										}
									}
							}
						}

						if (simulation.get(j).getProcess().size() > 0) {
							// simul.write(("\n" +
							// simulation.get(j).getSimulationName() + "-" + k +
							// "\n").getBytes());
							for (int g = 0; g < simulation.get(j).getProcess()
									.size(); g++) {
								NodeList process = doc
										.getElementsByTagName("aut:process");
								for (int i = 0; i < process.getLength(); i++) {
									if (simulation.get(j).getProcess().get(g)
											.size() > 0)
										if (process
												.item(i)
												.getAttributes()
												.getNamedItem("id")
												.getNodeValue()
												.equals(simulation.get(j)
														.getProcess().get(g)
														.get(k).getObjectName())) {
											simul.write(("Process : "
													+ process.item(i)
															.getAttributes()
															.getNamedItem("id")
															.getNodeValue() + "\n")
													.getBytes());
											for (int l = 0; l < simulation
													.get(j).getProcess().get(g)
													.get(k).getObjectParam()
													.size(); l++)
												for (int m = 0; m < process
														.item(i)
														.getChildNodes()
														.getLength(); m++) {
													if (process
															.item(i)
															.getChildNodes()
															.item(m)
															.getNodeName()
															.equals("aut:params"))
														for (int b = 0; b < process
																.item(i)
																.getChildNodes()
																.item(m)
																.getChildNodes()
																.getLength(); b++)
															if (process
																	.item(i)
																	.getChildNodes()
																	.item(m)
																	.getChildNodes()
																	.item(b)
																	.getNodeName()
																	.equals("aut:param")) {
																// System.out.
																// println
																// (process
																// .item(i).
																// getAttributes
																// (
																// ).getNamedItem
																// ("id").
																// getNodeValue
																// ());
																if (process
																		.item(i)
																		.getChildNodes()
																		.item(m)
																		.getChildNodes()
																		.item(b)
																		.getAttributes()
																		.getNamedItem(
																				"name")
																		.getNodeValue()
																		.equals(simulation
																				.get(j)
																				.getProcess()
																				.get(g)
																				.get(k)
																				.getObjectParam()
																				.get(l)
																				.getObjectName())) {
																	simul.write(("Parameter : "
																			+ process
																					.item(i)
																					.getChildNodes()
																					.item(m)
																					.getChildNodes()
																					.item(b)
																					.getAttributes()
																					.getNamedItem(
																							"name")
																					.getNodeValue() + " ")
																			.getBytes());
																	simul.write((((Double) simulation
																			.get(j)
																			.getProcess()
																			.get(g)
																			.get(k)
																			.getObjectParam()
																			.get(l)
																			.getObjectValue())
																			.toString() + "\n")
																			.getBytes());
																	process.item(
																			i)
																			.getChildNodes()
																			.item(m)
																			.getChildNodes()
																			.item(b)
																			.getAttributes()
																			.getNamedItem(
																					"value")
																			.setNodeValue(
																					((Double) simulation
																							.get(j)
																							.getProcess()
																							.get(g)
																							.get(k)
																							.getObjectParam()
																							.get(l)
																							.getObjectValue())
																							.toString());
																}
															}
												}

										}
								}

							}
						}

						for (int seed = 0; seed < simulation.get(j).getEngine()
								.get(j).getSeed().size(); seed++)

						{
							NodeList engine = doc
									.getElementsByTagName("aut:engine");
							for (int i = 0; i < engine.getLength(); i++) {
								engine.item(i)
										.getAttributes()
										.getNamedItem("seed")
										.setNodeValue(
												simulation.get(j).getEngine()
														.get(j).getSeed()
														.get(seed));
							}

							DOMSource domSource = new DOMSource(doc);
							StringWriter writer = new StringWriter();
							StreamResult result = new StreamResult(writer);
							TransformerFactory tf = TransformerFactory
									.newInstance();
							Transformer transformer;
							try {
								transformer = tf.newTransformer();

								transformer.transform(domSource, result);
							} catch (TransformerConfigurationException e) {
								e.printStackTrace();
							} catch (TransformerException e) {
								e.printStackTrace();
							}

							String computerName = "";
							try {
								computerName = InetAddress.getLocalHost()
										.getHostName();
								// System.out.println(computerName);
							} catch (Exception e) {
								System.out.println("Exception caught ="
										+ e.getMessage());
							}

							String filename = "./xml/"
									+ computerName
									+ "-"
									+ simulation.get(j).getSimulationName()
									+ "_"
									+ k
									+ "_"
									+ simulation.get(j).getEngine().get(j)
											.getSeed().get(seed);
							FileOutputStream file = new FileOutputStream(
									filename);

							file.write(writer.toString().getBytes());
							file.close();
							xmlFile.add(filename);
						}

					}

				if (simulation.get(j).getProcess().size() < 1
						&& simulation.get(j).getNode().size() < 1)
					for (int seed = 0; seed < simulation.get(j).getEngine()
							.get(j).getSeed().size(); seed++) {
						NodeList engine = doc
								.getElementsByTagName("aut:engine");
						for (int i = 0; i < engine.getLength(); i++) {
							engine.item(i)
									.getAttributes()
									.getNamedItem("seed")
									.setNodeValue(
											simulation.get(j).getEngine()
													.get(j).getSeed().get(seed));
						}

						DOMSource domSource = new DOMSource(doc);
						StringWriter writer = new StringWriter();
						StreamResult result = new StreamResult(writer);
						TransformerFactory tf = TransformerFactory
								.newInstance();
						Transformer transformer;
						try {
							transformer = tf.newTransformer();

							transformer.transform(domSource, result);
						} catch (TransformerConfigurationException e) {
							e.printStackTrace();
						} catch (TransformerException e) {
							e.printStackTrace();
						}

						String computerName = "";
						try {
							computerName = InetAddress.getLocalHost()
									.getHostName();
							// System.out.println(computerName);
						} catch (Exception e) {
							System.out.println("Exception caught ="
									+ e.getMessage());
						}

						String filename = "./xml/"
								+ computerName
								+ "-"
								+ simulation.get(j).getSimulationName()
								+ "_0_"
								+ simulation.get(j).getEngine().get(j)
										.getSeed().get(seed);
						FileOutputStream file = new FileOutputStream(filename);

						file.write(writer.toString().getBytes());
						xmlFile.add(filename);
						
						file.close();
					}

			}

			simul.close();

			if (new File(path + ".temp").exists())
				new File(path + ".temp").delete();

			return xmlFile;

		} catch (IOException e) {

			e.printStackTrace();
		}

		if (new File(path + ".temp").exists())
			new File(path + ".temp").delete();

		return null;
	}

	public void run() {
		try {
			start(originalXml, automatorXml);
		} catch (DeusAutomatorException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

}