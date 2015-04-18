package it.unipr.ce.dsg.deus.mobility;

/**
 * @author Marco Picone picone@ce.unipr.it
 *
 */
public class GeoLocation {

	private double latitude = 0.0;
	private double longitude = 0.0;
	
	public GeoLocation(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public boolean equals(Object obj) {
	
		GeoLocation locObj = (GeoLocation)obj;
		
		if(this.latitude == locObj.latitude && this.longitude == locObj.longitude)
			return true;
		else
			return false;
		
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}
