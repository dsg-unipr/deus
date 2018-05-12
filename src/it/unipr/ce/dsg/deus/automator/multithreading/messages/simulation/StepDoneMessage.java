package it.unipr.ce.dsg.deus.automator.multithreading.messages.simulation;

public class StepDoneMessage extends SimulationMessage {

	String averageFileName;

	public StepDoneMessage(int[] coordinates, String averageFileName) {
		super(coordinates);
		this.averageFileName = averageFileName;
	}

	public String getAverageFileName() {
		return averageFileName;
	}
}
