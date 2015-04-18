package it.unipr.ce.dsg.deus.automator;

import java.util.ArrayList;


/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Marco Pigoni
 *
 */
public class VTResults {

	private Double VT = null;
	private ArrayList<Result> vtResults = new ArrayList<Result>();
	
	public VTResults(Double vt, ArrayList<Result> vtResults) {
		VT = vt;
		this.vtResults = vtResults;
	}
	
	public VTResults(Double vt) {
		VT = vt;
	}
	
	public boolean equals(Object obj) {	
	
		VTResults result = (VTResults) obj;
		
		if(this.VT.equals(result.VT))
			{
			return true;
			}
		else
			return false;
	}

	public void addResult(Result result){
		
		this.vtResults.add(result);
	}
	
	public Double getVT() {
		return VT;
	}
	
	public void setVT(Double vt) {
		VT = vt;
	}
	
	public ArrayList<Result> getVtResultsList() {
		return vtResults;
	}
	
	public void setVtResults(ArrayList<Result> vtResults) {
		this.vtResults = vtResults;
	}
	
}
