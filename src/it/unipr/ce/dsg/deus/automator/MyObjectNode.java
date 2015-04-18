package it.unipr.ce.dsg.deus.automator;
import java.util.ArrayList;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Marco Pigoni
 *
 */
public class MyObjectNode {
	
	private String objectName;
	private ArrayList<MyObjectParam> objectParam = new ArrayList<MyObjectParam>();	
	private ArrayList<MyObjectResourceParam> objectResourceParam = new ArrayList<MyObjectResourceParam>();;	
		
	public MyObjectNode() {
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


	public ArrayList<MyObjectResourceParam> getObjectResourceParam() {
		return objectResourceParam;
	}


	public void setObjectResourceParam(
			ArrayList<MyObjectResourceParam> objectResourceParam) {
		this.objectResourceParam = objectResourceParam;
	}

}
