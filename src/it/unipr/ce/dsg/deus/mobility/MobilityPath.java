package it.unipr.ce.dsg.deus.mobility;

import it.unipr.ce.dsg.deus.core.Engine;

import java.util.ArrayList;

/**
 * @author Marco Picone picone@ce.unipr.it
 *
 */
public class MobilityPath {

	private ArrayList<MobilityPathPoint> pathPoints = null;
	private boolean hasTrafficJam = false;
	private double lenght = 0.0;
	private ArrayList<Integer> peerListInPath = null;
	private double speedLimit = 0.0;
	private boolean hasBadSurfaceCondition = false;
	private int badSurfaceIndex = -1;
	
	/**
	 * 
	 */
	public MobilityPath() {
		
		peerListInPath = new ArrayList<Integer>();
		
		this.pathPoints = new ArrayList<MobilityPathPoint>();
		this.lenght = 0.0;
		
		//Random select a speed limit
		int randomCase =  Engine.getDefault().getSimulationRandom().nextInt(4);
		
		switch (randomCase) {
		case 0:
			this.speedLimit = 30.0;
			break;
		case 1:
			this.speedLimit = 40.0;
			break;
		case 2:
			this.speedLimit = 50.0;
			break;
		case 3:
			this.speedLimit = 60.0;
			break;

			
		default:
			this.speedLimit = 30.0;
			break;
		}
		
	}
	
	public void addPeerInPath(Integer peerKey)
	{
		if(!this.peerListInPath.contains(peerKey))
			this.peerListInPath.add(peerKey);
	}
	
	public void removePeerInPath(Integer peerKey)
	{
		int index = this.peerListInPath.indexOf(peerKey);
		this.peerListInPath.remove(index);
	}
	
	public void addCityPathPoint(MobilityPathPoint point)
	{
		this.pathPoints.add(point);
	}
	
	
	public void incrementPathLength(double len)
	{
		this.lenght += len;
	}
	
	public GeoLocation getStartPoint()
	{
		if(this.pathPoints.size() > 0)				
			return this.pathPoints.get(0);
		else 
			return null;
	}
	
	public GeoLocation getEndPoint()
	{
		if(this.pathPoints.size() > 0)
			return this.pathPoints.get(this.pathPoints.size()-1);
		else 
			return null;
	}
	
	public ArrayList<MobilityPathPoint> getPathPoints() {
		return pathPoints;
	}

	public void setPathPoints(ArrayList<MobilityPathPoint> pathPoints) {
		this.pathPoints = pathPoints;
	}

	public boolean isHasTrafficJam() {
		return hasTrafficJam;
	}

	public void setHasTrafficJam(boolean hasTrafficJam) {
		this.hasTrafficJam = hasTrafficJam;
	}

	public double getLenght() {
		return lenght;
	}

	public void setLenght(double lenght) {
		this.lenght = lenght;
	}

	public int getNumOfCars() {
		return this.peerListInPath.size();
	}

	public double getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(double speedLimit) {
		this.speedLimit = speedLimit;
	}

	public boolean isBadSurfaceCondition() {
		return hasBadSurfaceCondition;
	}

	public void setHasBadSurfaceCondition(boolean hadBadSurfaceCondition) {
		this.hasBadSurfaceCondition = hadBadSurfaceCondition;
	}

	public int getBadSurfaceIndex() {
		return badSurfaceIndex;
	}

	public void setBadSurfaceIndex(int badSurfaceIndex) {
		this.badSurfaceIndex = badSurfaceIndex;
	}

	public boolean isHasBadSurfaceCondition() {
		return hasBadSurfaceCondition;
	}
	
}
