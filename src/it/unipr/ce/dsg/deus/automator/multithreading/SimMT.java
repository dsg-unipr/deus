package it.unipr.ce.dsg.deus.automator.multithreading;

import it.unipr.ce.dsg.deus.automator.MyObjectSimulation;
import it.unipr.ce.dsg.deus.automator.Runner;
import it.unipr.ce.dsg.deus.automator.multithreading.messages.handler.HandlerMessage;
import it.unipr.ce.dsg.deus.automator.multithreading.messages.handler.StepReadyMessage;
import it.unipr.ce.dsg.deus.automator.multithreading.messages.handler.TabReadyMessage;
import it.unipr.ce.dsg.deus.automator.multithreading.messages.simulation.*;

import java.beans.PropertyChangeSupport;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mirco Rosa (mirco.rosa.91@gmail.com) [multithreading]
 * */

public class SimMT {

	private ArrayList<MyObjectSimulation> simulations;
	private String computerName;
	private ArrayList<String> files = null;
	private Runner automatorRunner;
	private PropertyChangeSupport progressBarUpdater;
	private int progress = 0;

	private boolean completed = false;
	private int xmlNum = 0;
	private ArrayBlockingQueue<SimulationMessage> handlerQueue;

	private SimTab[] simTabs;

	public SimMT(ArrayList<MyObjectSimulation> simulations, ArrayList<String> files, Runner automatorRunner, PropertyChangeSupport progressBarUpdater) {
		this.simulations = simulations;
		this.files = files;
		this.automatorRunner=automatorRunner;
		this.progressBarUpdater = progressBarUpdater;

		handlerQueue = new ArrayBlockingQueue<>(getTotalNumberOfSims());

		initialize();

	}

	private void initialize() {
		System.out.println("Running simulations using multithreading");

		computerName = "";
		try {
			computerName = InetAddress.getLocalHost().getHostName();
			// System.out.println(computerName);
		} catch (Exception e) {
			System.out.println("Exception caught =" + e.getMessage());
		}

		simTabs = new SimTab[simulations.size()];

		for (int j = 0; j < simulations.size(); j++)
			simTabs[j]=new SimTab(simulations.get(j),j,this);

	}

	public void runMultithreadedSimulations() {
		//Job submission
		ExecutorService simExec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for(SimTab simTab : simTabs)
			simTab.executeAll(simExec);

		//Synchronization
		while(!completed) {
			try {
				SimulationMessage message = handlerQueue.take();
				int[] coordinates = message.getCoordinates();
				if(message instanceof SeedDoneMessage) {
					System.out.println("("+coordinates[0]+"."+coordinates[1]+"."+coordinates[2]+") SEED finished");

					if(((SeedDoneMessage) message).getLogFileName()!=null)
						simTabs[coordinates[0]].getSimSteps()[coordinates[1]].addLogFile(((SeedDoneMessage) message).getLogFileName());
					automatorRunner.incNumFile();

					markSeedAsCompleted(coordinates);
					prettyPrintProgressState();

					StepReadyMessage stepReadyMessage = new StepReadyMessage(isStepReady(coordinates));
					if(stepReadyMessage.isReady()) {
						stepReadyMessage.setComputerName(computerName);
						stepReadyMessage.setLogFiles(simTabs[coordinates[0]].getSimStep(coordinates[1]).getLogFiles());
						stepReadyMessage.setSimulation(simulations.get(coordinates[0]));
					}

					getSimQueue(coordinates).put(stepReadyMessage);
					System.out.println("STEP READY message sent to ("+coordinates[0]+"."+coordinates[1]+"."+coordinates[2]+"): "+isStepReady(coordinates)+"\n");
					updateProgressBar();
				} else if (message instanceof StepDoneMessage) {
					System.out.println("("+coordinates[0]+"."+coordinates[1]+"."+coordinates[2]+") STEP finished");

					simTabs[coordinates[0]].addAverageFile(((StepDoneMessage) message).getAverageFileName());

					markStepAsCompleted(coordinates);
					prettyPrintProgressState();

					TabReadyMessage tabReadyMessage = new TabReadyMessage(isTabReady(coordinates));
					if(tabReadyMessage.isReady()) {
						tabReadyMessage.setSimulations(simulations);
						tabReadyMessage.setAverageFileList(simTabs[coordinates[0]].getAverageFileList());
					}
					getSimQueue(coordinates).put(tabReadyMessage);
					System.out.println("TAB READY message sent to ("+coordinates[0]+"."+coordinates[1]+"."+coordinates[2]+"): "+isTabReady(coordinates)+"\n");
				} else if (message instanceof TabDoneMessage) {
					System.out.println("("+coordinates[0]+"."+coordinates[1]+"."+coordinates[2]+") TAB finished");
					markTabAsCompleted(coordinates);
					prettyPrintProgressState();
					updateSimulationStatus();
					System.out.println("TAB "+coordinates[0]+" FINISHED by to ("+coordinates[0]+"."+coordinates[1]+"."+coordinates[2]+"): "+isTabReady(coordinates)+"\n");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		simExec.shutdown();
		System.out.println("Multithreaded simulation finished.");
	}

	private void updateProgressBar() {
		progress++;
		progressBarUpdater.firePropertyChange("NumFileProperty" , (progress-1), progress);
	}

	private void updateSimulationStatus() {
		for(SimTab simTab : simTabs)
			if(!simTab.isCompleted()) return;
		this.complete();
	}

	private boolean isTabReady(int[] coordinates) {
		return simTabs[coordinates[0]].checkTabCompletion();
	}

	private boolean isStepReady(int[] coordinates) {
		return simTabs[coordinates[0]].getSimStep(coordinates[1]).checkStepCompletion();
	}

	private void markTabAsCompleted(int[] coordinates) {
		simTabs[coordinates[0]]
				.complete();
	}

	private void markStepAsCompleted(int[] coordinates) {
		simTabs[coordinates[0]]
				.getSimStep(coordinates[1])
				.complete();
	}

	private void markSeedAsCompleted(int[] coordinates) {
		simTabs[coordinates[0]]
				.getSimStep(coordinates[1])
				.getSimSeed(coordinates[2])
				.complete();
	}

	private void prettyPrintProgressState() {
		StringBuilder sb = new StringBuilder();
		sb.append("----------\n# Status Progress\nMachine: ").append(computerName).append("\n\n");
		for (int i = 0; i < simTabs.length; i++) {
			if (simTabs[i].isCompleted()) sb.append("[X] ");
			else  sb.append("[ ] ");
			sb.append(simTabs[i].getSimName()).append("(").append(i).append(")\n");
			for (int j = 0; j < simTabs[i].getSimSteps().length; j++) {
				if (simTabs[i].getSimSteps()[j].isCompleted()) sb.append("\t[X] ");
				else sb.append("\t[ ] ");
				sb.append("Step ").append(j).append("\n");
				for (int k = 0; k < simTabs[i].getSimSteps()[j].getSimSeeds().length; k++) {
					if (simTabs[i].getSimSteps()[j].getSimSeeds()[k].isCompleted()) sb.append("\t\t[X] ");
					else sb.append("\t\t[ ] ");
					sb.append("Seed ").append(simTabs[i].getSimSteps()[j].getSimSeeds()[k].getSeed()).append("\n");
				}
			}
		}
		sb.append("----------");
		System.out.println(sb.toString());
	}

	private int getTotalNumberOfSims() {
		int total=0;
		for (int j = 0; j < simulations.size(); j++)
			for (int k = 0; k < simulations.get(j).getSimulationNumber(); k++)
				for (int i = 0; i < new Integer(simulations.get(j).getSimulationNumberSeed()); i++)
					total++;
		return total;
	}

	public String getComputerName() {
		return computerName;
	}

	public void complete() {
		completed=true;
	}

	public boolean isCompleted() {
		return completed;
	}

	public String getXmlAndIncrementNum() {
		xmlNum++;
		return files.get(xmlNum-1);
	}

	public ArrayBlockingQueue<SimulationMessage> getHandlerQueue() {
		return handlerQueue;
	}

	private ArrayBlockingQueue<HandlerMessage> getSimQueue(int[] coordinates) {
		return simTabs[coordinates[0]]
				.getSimStep(coordinates[1])
				.getSimSeed(coordinates[2])
				.getSimSeedQueue();
	}
}