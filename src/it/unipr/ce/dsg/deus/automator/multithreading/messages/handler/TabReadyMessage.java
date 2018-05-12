package it.unipr.ce.dsg.deus.automator.multithreading.messages.handler;

import it.unipr.ce.dsg.deus.automator.MyObjectSimulation;

import java.util.ArrayList;

public class TabReadyMessage extends HandlerMessage {

	private ArrayList<MyObjectSimulation> simulations;
	private ArrayList<String> averageFileList;

	public TabReadyMessage(boolean ready) {
		super(ready);
	}

	public TabReadyMessage(boolean ready, ArrayList<MyObjectSimulation> simulations, ArrayList<String> averageFileList) {
		super(ready);
		this.simulations = simulations;
		this.averageFileList = averageFileList;
	}

	public ArrayList<MyObjectSimulation> getSimulations() {
		return simulations;
	}

	public ArrayList<String> getAverageFileList() {
		return averageFileList;
	}

	public void setSimulations(ArrayList<MyObjectSimulation> simulations) {
		this.simulations = simulations;
	}

	public void setAverageFileList(ArrayList<String> averageFileList) {
		this.averageFileList = averageFileList;
	}
}
