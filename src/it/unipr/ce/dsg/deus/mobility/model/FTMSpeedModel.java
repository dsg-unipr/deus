package it.unipr.ce.dsg.deus.mobility.model;

/**
 * 
 * @author Marco Picone (picone@ce.unipr.it)
 *
 */
public class FTMSpeedModel implements ISpeedModel {

	private double carMinSpeed = 0.0; 
	private double carMaxSpeed = 0.0;
	private double pathLenght = 0.0;
	private double numCarsInPath = 0.0;

	@Override
	public double evaluateSpeedValue() {
		
		double v_min = carMinSpeed;
		double v_max = carMaxSpeed;
		
		double k_jam = 0.25; //250 cars in 1 km
		
		double path_len = pathLenght;
		double car_in_path = numCarsInPath;
		
		double k = car_in_path/(path_len*1000.0);
		
		double speed = Math.max(v_min, v_max*(1-(k/k_jam)));
		
		return speed;
	}

	public double getCarMinSpeed() {
		return carMinSpeed;
	}

	public void setCarMinSpeed(double carMinSpeed) {
		this.carMinSpeed = carMinSpeed;
	}

	public double getCarMaxSpeed() {
		return carMaxSpeed;
	}

	public void setCarMaxSpeed(double carMaxSpeed) {
		this.carMaxSpeed = carMaxSpeed;
	}

	public double getPathLenght() {
		return pathLenght;
	}

	public void setPathLenght(double pathLenght) {
		this.pathLenght = pathLenght;
	}

	public double getNumCarsInPath() {
		return numCarsInPath;
	}

	public void setNumCarsInPath(double numCarsInPath) {
		this.numCarsInPath = numCarsInPath;
	}	
}
