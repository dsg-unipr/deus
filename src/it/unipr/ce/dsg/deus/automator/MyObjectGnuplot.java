package it.unipr.ce.dsg.deus.automator;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Marco Pigoni
 *
 */
public class MyObjectGnuplot {
	
	private String fileName;
	private String axisX;
	private String axisY;		
	
	public MyObjectGnuplot() {
		super();
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getAxisX() {
		return axisX;
	}
	
	public void setAxisX(String axisX) {
		this.axisX = axisX;
	}
	
	public String getAxisY() {
		return axisY;
	}
	
	public void setAxisY(String axisY) {
		this.axisY = axisY;
	}
	
}
