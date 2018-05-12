package it.unipr.ce.dsg.deus.automator.gui;

import java.util.ArrayList;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * 
 */
public class Node {

	private String nodeId;
	private ArrayList<NodeParameter> nodeParameterList;
	private ArrayList<NodeResource> nodeResourceList;

	public Node(String nodeIdValue) {

		this.nodeId = nodeIdValue;

		nodeParameterList = new ArrayList<NodeParameter>();
		nodeResourceList = new ArrayList<NodeResource>();
	}

	public boolean equals(Object obj) {

		Node nodeObj = (Node) obj;

		if (this.nodeId.equals(nodeObj.getNodeId()))
			return true;
		else
			return false;
	}

	public ArrayList<NodeParameter> getNodeParameterList() {
		return nodeParameterList;
	}

	public void setNodeParameterList(ArrayList<NodeParameter> nodeParameterList) {
		this.nodeParameterList = nodeParameterList;
	}

	public ArrayList<NodeResource> getNodeResourceList() {
		return nodeResourceList;
	}

	public void setNodeResourceList(ArrayList<NodeResource> nodeResourceList) {
		this.nodeResourceList = nodeResourceList;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String oneLineString() {

		String line = "ID: " + this.nodeId;

		for (int index = 0; index < this.nodeParameterList.size(); index++) {
			NodeParameter nodeParameter = this.nodeParameterList.get(index);
			line = line + " " + nodeParameter.getParamName() + " "
					+ nodeParameter.getInitialValue() + " "
					+ nodeParameter.getFinalValue() + " "
					+ nodeParameter.getStepValue();
		}

		for (int index = 0; index < this.nodeResourceList.size(); index++) {
			NodeResource nodeResource = this.nodeResourceList.get(index);
			line = line + " " + nodeResource.getHandlerName() + " "
					+ nodeResource.getResParamValue() + " "
					+ nodeResource.getInitialValue() + " " + " "
					+ nodeResource.getFinalValue() + " "
					+ nodeResource.getStepValue();
		}

		return line;
	}

	/**
	 * Generate the tag associated to the node
	 * 
	 * <node id="serverNode"> <paramName name="uploadSpeed">
	 * <initialValue>0.5</initialValue> <finalValue>3.0</finalValue>
	 * <stepValue>0.5</stepValue> </paramName> <paramName name="downloadSpeed">
	 * <initialValue>7.0</initialValue> <finalValue>4.5</finalValue>
	 * <stepValue>0.5</stepValue> </paramName> <resourceParamName
	 * handlerName="it.unipr.ce.dsg.deus.impl.resource.AllocableResource"
	 * resParamValue="MaxAcceptedConnection" > <initialValue>4.5</initialValue>
	 * <finalValue>7.0</finalValue> <stepValue>0.5</stepValue>
	 * </resourceParamName> </node>
	 * 
	 * @return
	 */
	public String getXMLTag() {

		String tag = "\n<node id=\"" + this.nodeId + "\">" + "\n\t";

		for (int index = 0; index < this.nodeParameterList.size(); index++) {
			NodeParameter nodeParameter = this.nodeParameterList.get(index);
			tag = tag + "<paramName name=\"" + nodeParameter.getParamName()
					+ "\">";
			tag = tag + "\n\t\t" + "<initialValue>"
					+ nodeParameter.getInitialValue() + "</initialValue>";
			tag = tag + "\n\t\t" + "<finalValue>"
					+ nodeParameter.getFinalValue() + "</finalValue>";
			tag = tag + "\n\t\t" + "<stepValue>" + nodeParameter.getStepValue()
					+ "</stepValue>";
			tag = tag + "\n\t" + "</paramName>" + "\n\t";
		}

		for (int index = 0; index < this.nodeResourceList.size(); index++) {
			NodeResource nodeResource = this.nodeResourceList.get(index);
			tag = tag + "<resourceParamName handlerName=\""
					+ nodeResource.getHandlerName() + "\" resParamValue=\""
					+ nodeResource.getResParamValue() + "\">";
			tag = tag + "\n\t\t" + "<initialValue>"
					+ nodeResource.getInitialValue() + "</initialValue>";
			tag = tag + "\n\t\t" + "<finalValue>"
					+ nodeResource.getFinalValue() + "</finalValue>";
			tag = tag + "\n\t\t" + "<stepValue>" + nodeResource.getStepValue()
					+ "</stepValue>";
			tag = tag + "\n\t" + "</resourceParamName>" + "\t";
		}

		tag = tag + "\n" + "</node>\n";
		return tag;
	}

}
