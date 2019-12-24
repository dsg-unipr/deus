package it.unipr.ce.dsg.deus.automator.multithreading;

import it.unipr.ce.dsg.deus.automator.*;
import it.unipr.ce.dsg.deus.automator.multithreading.messages.handler.TabReadyMessage;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class GnuPlotWrapper {

	private int[] coordinates;
	private TabReadyMessage message;

	public GnuPlotWrapper(int[] coordinates, TabReadyMessage message) {
		this.coordinates = coordinates;
		this.message = message;
	}

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

	public void generateGnuPlot() {

		// -> @author Stefano Sebastio: new gnuplot generator
		for (int z = 0; z < message.getSimulations().get(coordinates[0]).getGnuplot().size(); z++) {
			/*	gnuPlotXY(averageFileList,
						simulations.get(j).getSimulationName() + "-" + simulations.get(j).getGnuplot().get(z).getFileName() + "-" + z,
						simulations.get(j).getGnuplot().get(z).getAxisX(),
						simulations.get(j).getGnuplot().get(z).getAxisY(),
						simulations.get(j));
				*/
			gnuPlotArray(message.getAverageFileList(),
					message.getSimulations().get(coordinates[0]).getSimulationName() + "-" + message.getSimulations().get(coordinates[0]).getGnuplot().get(z).getFileName() + "-" + z,
					message.getSimulations().get(coordinates[0]).getGnuplot().get(z).getAxisX(),
					message.getSimulations().get(coordinates[0]).getGnuplot().get(z).getAxisY(),
					message.getSimulations().get(coordinates[0]));
		}
	}

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

}
