package it.unipr.ce.dsg.deus.automator.multithreading.messages.handler;

import it.unipr.ce.dsg.deus.automator.MyObjectSimulation;

import java.util.ArrayList;

public class StepReadyMessage extends HandlerMessage {

	private String computerName;
	private ArrayList<String> logFiles;
	private MyObjectSimulation simulation;

	public StepReadyMessage(boolean ready, String computerName, ArrayList<String> logFiles, MyObjectSimulation simulation) {
		super(ready);
		this.computerName = computerName;
		this.logFiles = logFiles;
		this.simulation = simulation;
	}

	public StepReadyMessage(boolean ready) {
		super(ready);
	}

	public String getComputerName() {
		return computerName;
	}

	public ArrayList<String> getLogFiles() {
		return logFiles;
	}

	public MyObjectSimulation getSimulation() {
		return simulation;
	}

	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	public void setLogFiles(ArrayList<String> logFiles) {
		this.logFiles = logFiles;
	}

	public void setSimulation(MyObjectSimulation simulation) {
		this.simulation = simulation;
	}
}
