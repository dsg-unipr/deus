package it.unipr.ce.dsg.deus.automator.multithreading;

import it.unipr.ce.dsg.deus.automator.MyObjectSimulation;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

/**
 * @author Mirco Rosa (mirco.rosa.91@gmail.com) [multithreading]
 * */

public class SimStep {

	private MyObjectSimulation simulation;
	private int index;
	private SimMT simMT;
	private SimTab parentTab;
	private SimSeed[] simSeeds;
	private boolean completed = false;

	private ArrayList<String> logFiles = new ArrayList<>();

	public SimStep(MyObjectSimulation simulation, int index, SimMT simMT, SimTab parentTab) {
		this.simulation = simulation;
		this.index = index;
		this.simMT = simMT;
		this.parentTab=parentTab;

		initializeSimSeeds();
	}

	private void initializeSimSeeds() {
		simSeeds = new SimSeed[new Integer(simulation.getSimulationNumberSeed())];
		for (int i = 0; i < new Integer(simulation.getSimulationNumberSeed()); i++)
			simSeeds[i]=new SimSeed(simulation,this,parentTab,simMT,i);
	}

	public void executeAll(ExecutorService executorService) {
		for(SimSeed simSeed : simSeeds)
			simSeed.executeSeed(executorService);
	}

	public SimSeed getSimSeed(int index) {
		return simSeeds[index];
	}

	public SimSeed[] getSimSeeds() {
		return simSeeds;
	}

	public int getIndex() {
		return index;
	}

	public boolean checkStepCompletion() {
		for(SimSeed simSeed : simSeeds)
			if(!simSeed.isCompleted()) return false;
		return true;
	}

	public void addLogFile(String logFile) {
		logFiles.add(logFile);
	}

	public ArrayList<String> getLogFiles() {
		return logFiles;
	}

	public void complete() {
		completed = true;
	}

	public boolean isCompleted() {
		return completed;
	}
}
