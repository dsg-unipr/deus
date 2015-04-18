package it.unipr.ce.dsg.deus.automator;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Marco Pigoni
 *
 */
public class MyObjectResourceParam {
		
	private String objectParam;	
	private String objectHandlerName;
	private String resParamValue;
	private float objectValue;
		

	public MyObjectResourceParam() {
	}

	public String getObjectParam() {
		return objectParam;
	}

	public void setObjectParam(String objectParam) {
		this.objectParam = objectParam;
	}

	public double getObjectValue() {
		return objectValue;
	}

	public void setObjectValue(Float double1) {
		this.objectValue = double1;
	}

	public String getObjectHandlerName() {
		return objectHandlerName;
	}

	public void setObjectHandlerName(String objectHandlerName) {
		this.objectHandlerName = objectHandlerName;
	}

	public String getResParamValue() {
		return resParamValue;
	}

	public void setResParamValue(String resParamValue) {
		this.resParamValue = resParamValue;
	}

}

