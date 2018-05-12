package it.unipr.ce.dsg.deus.automator.multithreading.messages.simulation;

public class SeedDoneMessage extends SimulationMessage {

	private String logFileName;

	public SeedDoneMessage(int[] coordinates, String logFileName) {
		super(coordinates);
		this.logFileName=logFileName;
	}

	public String getLogFileName() {
		return logFileName;
	}
}
