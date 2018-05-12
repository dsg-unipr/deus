package it.unipr.ce.dsg.deus.automator.multithreading;

import it.unipr.ce.dsg.deus.automator.*;
import it.unipr.ce.dsg.deus.automator.multithreading.messages.handler.HandlerMessage;
import it.unipr.ce.dsg.deus.automator.multithreading.messages.handler.StepReadyMessage;
import it.unipr.ce.dsg.deus.automator.multithreading.messages.handler.TabReadyMessage;
import it.unipr.ce.dsg.deus.automator.multithreading.messages.simulation.*;
import it.unipr.ce.dsg.deus.core.Deus;
//import org.apache.commons.configuration.Configuration;
//import org.apache.commons.configuration.ConfigurationException;
//import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.*;
//import java.net.InetAddress;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Mirco Rosa (mirco.rosa.91@gmail.com) [multithreading]
 * */

public class SimRunnable implements Runnable {

	private String xmlName, logName;
	private int[] coordinates;
	private ArrayBlockingQueue<SimulationMessage> handlerQueue;
	private ArrayBlockingQueue<HandlerMessage> simQueue;
	private Deus deus;

	public SimRunnable() {
	}

	public SimRunnable(String xmlName, String logName, ArrayBlockingQueue<SimulationMessage> handlerQueue, ArrayBlockingQueue<HandlerMessage> simQueue, int[] coordinates) {
		this.xmlName = xmlName;
		this.logName = logName;
		this.handlerQueue=handlerQueue;
		this.simQueue=simQueue;
		this.coordinates=coordinates;
	}

	public void setParameters(String xmlName, String logName, ArrayBlockingQueue<SimulationMessage> handlerQueue, ArrayBlockingQueue<HandlerMessage> simQueue, int[] coordinates) {
		this.xmlName = xmlName;
		this.logName = logName;
		this.handlerQueue=handlerQueue;
		this.simQueue=simQueue;
		this.coordinates=coordinates;
	}

	@Override
	public void run() {
		try {
			System.out.println(Thread.currentThread().toString()+" ("+coordinates[0]+"."+coordinates[1]+"."+coordinates[2]+") SEED started");

			try {
				deus = new Deus(xmlName, logName);
			} catch (Exception e) {
				e.printStackTrace();
			}

			File f = new File(logName);
			handlerQueue.put(f.exists() && !f.isDirectory() ? new SeedDoneMessage(coordinates, logName) : new SeedDoneMessage(coordinates, null));

			System.out.println(Thread.currentThread().toString()+" ("+coordinates[0]+"."+coordinates[1]+"."+coordinates[2]+") SEED finished");

			HandlerMessage message = simQueue.take();
			if(message.isReady()) {  //Step final computations (averages)
				if(message instanceof StepReadyMessage) {
					System.out.println(Thread.currentThread().toString()+"("+coordinates[0]+"."+coordinates[1]+"."+coordinates[2]+") STEP finished");

					handlerQueue.put(new StepDoneMessage(coordinates,generateResults((StepReadyMessage) message)));
					message=simQueue.take();
				} else   System.err.println("Wrong message type!");

				if(message.isReady()) {  //Tab final computations (GnuPlot)
					if(message instanceof TabReadyMessage) {

						GnuPlotWrapper gnuPlotWrapper = new GnuPlotWrapper(coordinates,(TabReadyMessage) message);
						gnuPlotWrapper.generateGnuPlot();

					   System.out.println("("+coordinates[0]+"."+coordinates[1]+"."+coordinates[2]+") TAB finished");
						handlerQueue.put(new TabDoneMessage(coordinates));
					} else   System.err.println("Wrong message type!");
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String generateResults(StepReadyMessage message) {
		ResultAutomator resultAutomator = new ResultAutomator(message.getLogFiles());

		String averageFileName = "";
		String varFileName = "";
		String sqrtvarFileName = "";

		try {
			resultAutomator.readTotalResults();

			averageFileName = "./results//" + message.getComputerName()
					+ "_Average_"
					+ message.getSimulation().getSimulationName() + "-" + coordinates[1];
			varFileName = "./results//" + message.getComputerName() + "_Var_"
					+ message.getSimulation().getSimulationName() + "-" + coordinates[1];
			sqrtvarFileName = "./results//" + message.getComputerName()
					+ "_SqrtVar_"
					+ message.getSimulation().getSimulationName() + "-" + coordinates[1];

			resultAutomator.resultsAverage(averageFileName);
			resultAutomator.resultsVar(varFileName, sqrtvarFileName,
					averageFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		message.getLogFiles().clear();
		return averageFileName;
	}



}
