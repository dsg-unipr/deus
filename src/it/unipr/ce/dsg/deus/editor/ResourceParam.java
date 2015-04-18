package it.unipr.ce.dsg.deus.editor;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * A class that define a type of attribute for vertex Node
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 * 
 */
public class ResourceParam implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<String> nameResParam;
	private ArrayList<String> valueResParam;
	private String handlerRes;
	
	public void setNameResParamList(ArrayList<String> name){
		this.nameResParam=name;
	}
	
	public void setValueResParmList(ArrayList<String> value){
		this.valueResParam=value;
	}
	
	public void setHandlerRes(String handler){
		this.handlerRes=handler;
	}
	
	public ArrayList<String> getNameResParamList(){
		return this.nameResParam;
	}
	
	public ArrayList<String> getValueResParamList(){
		return this.valueResParam;
	}
	
	public String getHandlerRes(){
		return this.handlerRes;
	}

}
