package it.unipr.ce.dsg.deus.automator.gui;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * 
 */
public class NodeParameter {

	private String nodeId;
	private String paramName;
	private Double initialValue;
	private Double finalValue;
	private Double stepValue;

	public NodeParameter() {
		this.nodeId = "pcNode";
		this.paramName = "uploadSpeed";
		this.initialValue = 0.1;
		this.finalValue = 0.3;
		this.stepValue = 0.1;
	}

	public NodeParameter(Double finalValue, Double initialValue, String nodeId,
			String paramName, Double stepValue) {
		super();
		this.finalValue = finalValue;
		this.initialValue = initialValue;
		this.nodeId = nodeId;
		this.paramName = paramName;
		this.stepValue = stepValue;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
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

}
