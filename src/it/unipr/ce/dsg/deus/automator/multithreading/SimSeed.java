package it.unipr.ce.dsg.deus.automator.multithreading;

import it.unipr.ce.dsg.deus.automator.MyObjectSimulation;
import it.unipr.ce.dsg.deus.automator.multithreading.messages.handler.HandlerMessage;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author Mirco Rosa (mirco.rosa.91@gmail.com) [multithreading]
 * */

public class SimSeed {

	private MyObjectSimulation simulation;
	private SimStep parentStep;
	private SimTab parentTab;
	private SimMT simMT;

	private int index;
	private String seed;
	private SimRunnable simRunnable;
	private ArrayBlockingQueue<HandlerMessage> queue;
	private boolean completed = false;

	public SimSeed(MyObjectSimulation simulation, SimStep parentStep, SimTab parentTab, SimMT simMT, int index) {
		this.simulation = simulation;
		this.parentStep = parentStep;
		this.parentTab = parentTab;
		this.simMT = simMT;
		this.index = index;

		this.seed = simulation.getEngine().get(parentTab.getIndex()).getSeed().get(index);

		initializeSimRunnables();
	}

	private void initializeSimRunnables() {
		queue = new ArrayBlockingQueue<>(1);

		String logFileName ="."+ File.separator+"temp"+File.separator+simMT.getComputerName()+"-"
					+simulation.getSimulationName()+"-"
					+parentStep.getIndex()+"-"
					+seed;

			int[] coordinates = {parentTab.getIndex(),parentStep.getIndex(),index};
			simRunnable=new SimRunnable(simMT.getXmlAndIncrementNum(),logFileName,simMT.getHandlerQueue(),queue,coordinates);
	}

	public void executeSeed(ExecutorService executorService) {
		executorService.submit(simRunnable);
	}

	public ArrayBlockingQueue<HandlerMessage> getSimSeedQueue() {
		return queue;
	}

	public void complete() {
		completed = true;
	}

	public boolean isCompleted() {
		return completed;
	}

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public SimRunnable getSimRunnable() {
		return simRunnable;
	}
}
