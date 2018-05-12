package it.unipr.ce.dsg.deus.automator.multithreading;

import it.unipr.ce.dsg.deus.automator.MyObjectSimulation;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

/**
 * @author Mirco Rosa (mirco.rosa.91@gmail.com) [multithreading]
 * */

public class SimTab {

	private String simName;
	private int index;
	private MyObjectSimulation simulation;
	private SimMT simMT;
	private SimStep simSteps[];

	private boolean completed = false;

	private ArrayList<String> averageFileList = new ArrayList<String>();   //For GnuPlot

	public SimTab(MyObjectSimulation simulation, int index, SimMT simMT) {
		this.simulation=simulation;
		this.index=index;
		this.simMT = simMT;
		simName = simulation.getSimulationName();

		initializeSimSteps();
	}

	public SimStep getSimStep(int index) {
		return simSteps[index];
	}

	public SimStep[] getSimSteps() {
		return simSteps;
	}

	private void initializeSimSteps() {
		simSteps = new SimStep[simulation.getSimulationNumber()];
		for (int k = 0; k < simulation.getSimulationNumber(); k++)
			simSteps[k]=new SimStep(simulation,k,simMT,this);
	}

	public void executeAll(ExecutorService executorService) {
		for(SimStep simStep : simSteps)
			simStep.executeAll(executorService);
	}

	public int getIndex() {
		return index;
	}

	public String getSimName() {
		return simName;
	}

	public boolean checkTabCompletion() {
		for(SimStep simStep : simSteps)
			if(!simStep.isCompleted()) return false;
		return true;
	}

	public ArrayList<String> getAverageFileList() {
		return averageFileList;
	}

	public void addAverageFile(String averageFile) {
		averageFileList.add(averageFile);
	}

	public void complete() {
		completed=true;
	}

	public boolean isCompleted() {
		return completed;
	}
}
