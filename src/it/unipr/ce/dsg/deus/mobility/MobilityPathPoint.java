package it.unipr.ce.dsg.deus.mobility;

import java.util.ArrayList;

/**
 * 
 * @author Marco Picone picone@ce.unipr.it
 *
 */
public class MobilityPathPoint extends GeoLocation {

	private ArrayList<Double> logMonitoredSpeed = null;
	private String surfaceCondition = null;
	
	public MobilityPathPoint(double latitude, double longitude) {
		super(latitude, longitude);
		this.logMonitoredSpeed = new ArrayList<Double>();
	}

	public void addMonitoredSpeed(double speedValue)
	{
		this.logMonitoredSpeed.add(speedValue);
	}
	
	public ArrayList<Double> getLogMonitoredSpeed() {
		return logMonitoredSpeed;
	}

	public void setLogMonitoredSpeed(ArrayList<Double> logMonitoredSpeed) {
		this.logMonitoredSpeed = logMonitoredSpeed;
	}


	public String getSurfaceCondition() {
		return surfaceCondition;
	}



	public void setSurfaceCondition(String surfaceCondition) {
		this.surfaceCondition = surfaceCondition;
	}
	
	

}
