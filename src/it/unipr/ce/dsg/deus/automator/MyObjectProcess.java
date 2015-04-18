package it.unipr.ce.dsg.deus.automator;

import java.util.ArrayList;


/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Marco Pigoni
 *
 */
public class MyObjectProcess {
	
	private String objectName;
	private ArrayList<MyObjectParam> objectParam = new ArrayList<MyObjectParam>();;	
	
	public MyObjectProcess() {
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

