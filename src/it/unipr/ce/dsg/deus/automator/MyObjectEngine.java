package it.unipr.ce.dsg.deus.automator;

import java.util.ArrayList;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Marco Pigoni
 *
 */
public class MyObjectEngine {
	
	private ArrayList<String> seed = new ArrayList<String>();
	private ArrayList<Float> vt = new ArrayList<Float>();		
	
	public MyObjectEngine() {
		super();
	}
	

	public ArrayList<String> getSeed() {
		return seed;
	}


	public void setSeed(ArrayList<String> seed) {
		this.seed = seed;
	}


	public ArrayList<Float> getVt() {
		return vt;
	}


	public void setVt(ArrayList<Float> vt) {
		this.vt = vt;
	}

}
