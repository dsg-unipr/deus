package it.unipr.ce.dsg.deus.mobility.node;

import java.util.ArrayList;
import java.util.Properties;

import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Resource;
import it.unipr.ce.dsg.deus.mobility.GeoLocation;
import it.unipr.ce.dsg.deus.mobility.MobilityPath;
import it.unipr.ce.dsg.deus.mobility.MobilityPathIndex;
import it.unipr.ce.dsg.deus.mobility.SwitchStation;
import it.unipr.ce.dsg.deus.mobility.event.MovePeerEvent;
import it.unipr.ce.dsg.deus.mobility.model.ISpeedModel;
import it.unipr.ce.dsg.deus.mobility.util.GeoDistance;
import it.unipr.ce.dsg.deus.mobility.util.SwitchStationController;
import it.unipr.ce.dsg.deus.p2p.node.Peer;

/**
 * @author Marco Picone picone@ce.unipr.it
 *
 */
public abstract class MobilePeer extends Peer implements IMobilePeer{

	static public SwitchStationController switchStationController = null;
	private MobilityPathIndex mobilityPathIndex =  null;
	private SwitchStation switchStation = null;
	private GeoLocation geoLocation = null;
	private MobilityPath mobilityPath = null;
	private ISpeedModel mobilityModel = null;
	
	private boolean isMovingActive = false;
	
	public boolean isMovingActive() {
		return isMovingActive;
	}

	public void setMovingActive(boolean isMovingActive) {
		this.isMovingActive = isMovingActive;
	}

	public MobilityPathIndex getMobilityPathIndex() {
		return mobilityPathIndex;
	}

	public void setMobilityPathIndex(MobilityPathIndex mobilityPathIndex) {
		this.mobilityPathIndex = mobilityPathIndex;
	}

	public SwitchStation getSwitchStation() {
		return switchStation;
	}

	public void setSwitchStation(SwitchStation switchStation) {
		this.switchStation = switchStation;
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	public MobilityPath getMobilityPath() {
		return mobilityPath;
	}

	public void setMobilityPath(MobilityPath mobilityPath) {
		this.mobilityPath = mobilityPath;
	}

	public ISpeedModel getMobilityModel() {
		return mobilityModel;
	}

	public void setMobilityModel(ISpeedModel mobilityModel) {
		this.mobilityModel = mobilityModel;
	}

	public MobilePeer(String id, Properties params, ArrayList<Resource> resources) throws InvalidParamsException {
		super(id, params, resources);	
	}
	
	/**
	 * 
	 * @param triggeringTime
	 */
	public void mobilityInit(float triggeringTime, ISpeedModel mobilityModel)
	{
		//Mobility Model init
		this.mobilityModel = mobilityModel;
		
		//Select Randomly a starting Switch Station
		int ssIndex = Engine.getDefault().getSimulationRandom().nextInt(switchStationController.getSwitchStationList().size());
		this.switchStation  = switchStationController.getSwitchStationList().get(ssIndex);	
		
		//Select a path from its starting switch station
		ArrayList<MobilityPath> availablePaths = switchStationController.getPathListFromSwithStation(this.switchStation);
		
		//Pick Up a random path among available
		int pathIndex = Engine.getDefault().getSimulationRandom().nextInt(availablePaths.size());
		this.mobilityPath = availablePaths.get(pathIndex);
		this.mobilityPath.addPeerInPath(this.key);
		this.mobilityPathIndex = new MobilityPathIndex(0, this.mobilityPath.getPathPoints().size());
		
		//Set peer position
		this.geoLocation=this.mobilityPath.getStartPoint();

	}
	
	/**
	 * 
	 * @param triggeringTime
	 */
	public void scheduleMove(float triggeringTime) {
		
		if(this.isMovingActive == true)
		{
			try 
			{
				float delay = 0;
				double distance = 0.0;
			
	    		GeoLocation nextStep = this.mobilityPath.getPathPoints().get(this.mobilityPathIndex.getIndex()+1);
				
				distance = GeoDistance.distance(this.geoLocation,nextStep);
				
				//Update mobility model parameters
				this.configureMobilityModelParameter();
				
				double speed = this.mobilityModel.evaluateSpeedValue();
				
				//delay = (float)( ( (double)distance / (double)speed ) *60.0*16.6);
				delay = (float)(this.sec2VT(60.0*60.0*((double)distance/(double)speed)));
				
				//System.out.println("Delay: " + delay);
				
				if(!(delay>0) && !(delay==0) && !(delay<0))
					delay = 0;
				
				MovePeerEvent moveEvent = (MovePeerEvent) new MovePeerEvent("node_move_event", params, null).createInstance(triggeringTime + delay);
				moveEvent.setOneShot(true);
				moveEvent.setAssociatedNode(this);
				Engine.getDefault().insertIntoEventsList(moveEvent);
			
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	

	/**
	 * 
	 * @param triggeringTime
	 */
	public void move(float triggeringTime) {			
		
		//Move to next position among CityPath
		this.mobilityPathIndex.next();
		
		//If there isn't other point on the path pick up a new one
		//System.out.println("Peer:"+this.key+" City Path Index:"+this.ci.getIndex()+" Max:"+this.cp.getPathPoints().size());
		if(!this.mobilityPathIndex.hasNextStep())
		{	
			//System.out.println("Peer:"+this.key+" changing switch station !");
	
			//Decrement the number of cars for actual path
			this.mobilityPath.removePeerInPath(this.key);
			
			//Actual Switch Station is the last point of the path
			SwitchStation actualSS = null;
			
			
			if(this.mobilityPathIndex.isBackward() == false)
				actualSS = new SwitchStation(this.mobilityPath.getEndPoint().getLatitude(), this.mobilityPath.getEndPoint().getLongitude());
			else
				actualSS = new SwitchStation(this.mobilityPath.getStartPoint().getLatitude(), this.mobilityPath.getStartPoint().getLongitude());
			
			this.mobilityPath = this.selectNextSwitchSation(actualSS);
			this.mobilityPath.addPeerInPath(this.key);
			this.mobilityPathIndex = new MobilityPathIndex(0, this.mobilityPath.getPathPoints().size());
			this.geoLocation = this.mobilityPath.getStartPoint();		
		}
		else{
			this.geoLocation = this.mobilityPath.getPathPoints().get(this.mobilityPathIndex.getIndex());
		}
		
		this.moved(triggeringTime);
		this.scheduleMove(triggeringTime);
	}
	
	
	/**
	 * 
	 */
	public MobilityPath selectNextSwitchSation(SwitchStation actualSS)
	{
		//Select a path from its starting switch station
		ArrayList<MobilityPath> availablePaths = switchStationController.getPathListFromSwithStation(actualSS);
		
		int pathIndex = Engine.getDefault().getSimulationRandom().nextInt(availablePaths.size());
		
		return availablePaths.get(pathIndex);
	}

	@Override
	public void startMoving(float triggeringTime) {
		
		this.isMovingActive = true;
		
		//Schedule the first movement
		this.scheduleMove(triggeringTime);
	
	}

	@Override
	public void stopMoving(float triggeringTime) {
		this.isMovingActive = false;
	}
	
	

}
