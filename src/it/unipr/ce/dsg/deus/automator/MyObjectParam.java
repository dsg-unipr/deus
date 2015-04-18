package it.unipr.ce.dsg.deus.automator;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Marco Pigoni
 *
 */
public class MyObjectParam {
		
	private String objectParam;	
	private String objectName;
	private float objectValue;
	
	public MyObjectParam(String objectParam, String objectId, float objectValue) {
		super();
		this.objectParam = objectParam;
		this.objectName = objectId;
		this.objectValue = objectValue;
	}

	public MyObjectParam() {
	}

	public String getObjectParam() {
		return objectParam;
	}

	public void setObjectParam(String objectParam) {
		this.objectParam = objectParam;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectId) {
		this.objectName = objectId;
	}

	public double getObjectValue() {
		return objectValue;
	}

	public void setObjectValue(Float double1) {
		this.objectValue = double1;
	}

}
