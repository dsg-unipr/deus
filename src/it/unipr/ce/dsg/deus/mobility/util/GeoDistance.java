package it.unipr.ce.dsg.deus.mobility.util;

import it.unipr.ce.dsg.deus.mobility.GeoLocation;

/**
 * @author Marco Picone picone@ce.unipr.it
 *
 */
public class GeoDistance {

	
	/**
	 * Calculate the distance between two different GeoLocation
	 * 
	 */
	public static double distance(GeoLocation gl1, GeoLocation gl2) {
		
		double lon1 = gl1.getLongitude();
		double lon2 = gl2.getLongitude();
		double lat1 = gl1.getLatitude();
		double lat2 = gl2.getLatitude();
		
		double theta = lon1 - lon2;

		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		dist = dist * 1.609344;

		return (dist);

	}
	
	public static double distance(double lon1,double lat1,double lon2,double lat2) {
		
		double theta = lon1 - lon2;

		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		dist = dist * 1.609344;


		return (dist);

	}
	
	/**
	 *   This function converts decimal degrees to radians        
	 */
	private static double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}

	/**
	 * This function converts radians to decimal degrees     
	 */
	private static double rad2deg(double rad) {
	  return (rad * 180.0 / Math.PI);
	}
}
