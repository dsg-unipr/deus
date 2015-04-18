package it.unipr.ce.dsg.deus.automator.gui;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * 
 */
public class NodeResource {

	private String handlerName;
	private String resParamValue;
	private Double initialValue;
	private Double finalValue;
	private Double stepValue;
	private String nodeId;

	public NodeResource() {
		this.nodeId = "";
		this.resParamValue = "ResParamValue";
		this.handlerName = "HandlerName";
		this.initialValue = 0.0;
		this.finalValue = 0.0;
		this.stepValue = 0.0;
	}

	public NodeResource(Double finalValue, String handlerName,
			Double initialValue, String nodeId, String resParamValue,
			Double stepValue) {
		super();
		this.finalValue = finalValue;
		this.handlerName = handlerName;
		this.initialValue = initialValue;
		this.nodeId = nodeId;
		this.resParamValue = resParamValue;
		this.stepValue = stepValue;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public String getResParamValue() {
		return resParamValue;
	}

	public void setResParamValue(String resParamValue) {
		this.resParamValue = resParamValue;
	}

	public Double getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(Double initialValue) {
		this.initialValue = initialValue;
	}

	public Double getFinalValue() {
		return finalValue;
	}

	public void setFinalValue(Double finalValue) {
		this.finalValue = finalValue;
	}

	public Double getStepValue() {
		return stepValue;
	}

	public void setStepValue(Double stepValue) {
		this.stepValue = stepValue;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

}
