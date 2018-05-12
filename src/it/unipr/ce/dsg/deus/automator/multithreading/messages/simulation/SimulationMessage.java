package it.unipr.ce.dsg.deus.automator.multithreading.messages.simulation;

public class SimulationMessage {

	private int[] coordinates;

	public SimulationMessage(int[] coordinates) {
		this.coordinates = coordinates;
	}

	public int[] getCoordinates() {
		return coordinates;
	}
}
