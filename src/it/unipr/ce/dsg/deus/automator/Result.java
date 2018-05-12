package it.unipr.ce.dsg.deus.automator;


/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Marco Pigoni
 *
 */
public class Result {

	private String name = null;
	private Double value = 0.0;
	
	public Result(String name, Double value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	public void addToValue(Double value) {
		this.value += value;
	}
	
}
