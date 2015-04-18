package it.unipr.ce.dsg.deus.mobility.util;

import it.unipr.ce.dsg.deus.mobility.GeoLocation;
import it.unipr.ce.dsg.deus.mobility.MobilityPath;
import it.unipr.ce.dsg.deus.mobility.MobilityPathPoint;
import it.unipr.ce.dsg.deus.mobility.SwitchStation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Marco Picone picone@ce.unipr.it
 *
 */
public class SwitchStationController {

	private String pathFileName = null;
	private String switchStationFileName = null;
	private ArrayList<MobilityPath> pathList = null;
	private ArrayList<SwitchStation> switchStationList = null;
	private ArrayList<MobilityPathPoint> locationList = null;
	
	/**
	 * 
	 * @param pathFileName
	 * @param switchStationFileName
	 */
	public SwitchStationController(String switchStationFileName,String pathFileName) {
		super();
		this.pathFileName = pathFileName;
		this.switchStationFileName = switchStationFileName;
		this.pathList = new ArrayList<MobilityPath>();
		this.switchStationList = new ArrayList<SwitchStation>();
		this.locationList = new ArrayList<MobilityPathPoint>();
	}

	/**
	 * 
	 * @param ss
	 * @return
	 */
	public ArrayList<MobilityPath> getPathListFromSwithStation(SwitchStation ss)
	{
		 ArrayList<MobilityPath> pList = new ArrayList<MobilityPath>();
		 
		 for(int i=0; i<this.pathList.size();i++)
		 {
			 if(this.pathList.get(i).getStartPoint().equals(ss))
				 pList.add(this.pathList.get(i));
		 }
		 
		 return pList;
	}
	
	/**
	 * 
	 * @param geoLocation
	 * @param geoLocation2
	 * @return
	 */
	public MobilityPath getPathBetweenPoints(GeoLocation geoLocation, GeoLocation geoLocation2)
	{
		 
		 for(int i=0; i<this.pathList.size();i++)
		 {
			 if(this.pathList.get(i).getStartPoint().equals(geoLocation) && this.pathList.get(i).getEndPoint().equals(geoLocation2))
				 return this.pathList.get(i);
		 }
		
		return null;
	}
	
	
	/**
	 * 
	 */
	public void readSwitchStationFile()
	{
		try
		{
			BufferedReader br =new BufferedReader(new InputStreamReader(new FileInputStream(new File(this.switchStationFileName))));
			
			String line = null;
			line = br.readLine();
			
			while(line!= null)
			{	
				String[] coordinates = line.split(",");
				
				double lat = Double.parseDouble(coordinates[0]);
				double lon = Double.parseDouble(coordinates[1]);
					
				SwitchStation ss = new SwitchStation(lat, lon);
				
				this.switchStationList.add(ss);
	
				line = br.readLine();
			}
			
			br.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Switch Station: " + this.switchStationList.size());
	}
	
	/**
	 * 
	 */
	public void readPathFile()
	{
		try
		{
			BufferedReader br =new BufferedReader(new InputStreamReader(new FileInputStream(new File(this.pathFileName))));
			
			String line = null;
			line = br.readLine();
			
			while(line!= null)
			{	
				String[] points = line.split("#"); 
				
				MobilityPath path = new MobilityPath();
				
				for(int i=0; i<points.length; i++)
				{
					String[] coordinates = points[i].split(",");
					
					double lat = Double.parseDouble(coordinates[0]);
					double lon = Double.parseDouble(coordinates[1]);
					
					//GeoLocation point = new GeoLocation(lat, lon, 0);
					MobilityPathPoint point = new MobilityPathPoint(lat, lon);
					
					//If the point already exist in the locationList
					if(!this.locationList.contains(point))
						this.locationList.add(point);
					
					int index = this.locationList.indexOf(point);
					
					//Add new point to path
					path.addCityPathPoint(this.locationList.get(index));
					
					//Increment Path Length
					if(path.getPathPoints().size() >= 2)
					{
						MobilityPathPoint point1 = path.getPathPoints().get(path.getPathPoints().size()-1);
						MobilityPathPoint point2 = path.getPathPoints().get(path.getPathPoints().size()-2);
						
						double distance = GeoDistance.distance(point1, point2);
						
						if(!( !(distance > 0.0) && distance !=0.0 && !(distance <0.0) ))
								path.incrementPathLength(distance);
					}
					
					//path.addCityPathPoint(point);
				}
				
				this.pathList.add(path);
				
				line = br.readLine();
			}
			
			br.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("City Paths: " + this.pathList.size());
	}

	
	public String getPathFileName() {
		return pathFileName;
	}

	public void setPathFileName(String pathFileName) {
		this.pathFileName = pathFileName;
	}

	public String getSwitchStationFileName() {
		return switchStationFileName;
	}

	public void setSwitchStationFileName(String switchStationFileName) {
		this.switchStationFileName = switchStationFileName;
	}

	public ArrayList<MobilityPath> getPathList() {
		return pathList;
	}

	public void setPathList(ArrayList<MobilityPath> pathList) {
		this.pathList = pathList;
	}

	public ArrayList<SwitchStation> getSwitchStationList() {
		return switchStationList;
	}

	public void setSwitchStationList(ArrayList<SwitchStation> switchStationList) {
		this.switchStationList = switchStationList;
	}
	
}
