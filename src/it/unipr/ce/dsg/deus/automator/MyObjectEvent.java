package it.unipr.ce.dsg.deus.automator;

import java.util.ArrayList;


/** @author Mirco Rosa (mirco.rosa.91@gmail.com) [Event Parametrization] */

public class MyObjectEvent {

	private String objectName;
	private ArrayList<MyObjectParam> objectParam = new ArrayList<MyObjectParam>();;

	public MyObjectEvent() {
		super();
	}

	public String getObjectName() {
		return objectName;
	}
	
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public ArrayList<MyObjectParam> getObjectParam() {
		return objectParam;
	}

	public void setObjectParam(ArrayList<MyObjectParam> objectParam) {
		this.objectParam = objectParam;
	}

}

