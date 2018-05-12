package it.unipr.ce.dsg.deus.automator.gui;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 *
 */
public class EngineParameter {

	private String seedValue;

	public EngineParameter(){
		this.seedValue = "123456789";
	}
	
	public EngineParameter(String seedValue) {
		super();
		this.seedValue = seedValue;
	}
	
	public String getSeedValue() {
		return seedValue;
	}

	public void setSeedValue(String seedValue) {
		this.seedValue = seedValue;
	}
	
}
