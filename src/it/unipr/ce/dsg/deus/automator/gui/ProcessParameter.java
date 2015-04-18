package it.unipr.ce.dsg.deus.automator.gui;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * 
 */
public class ProcessParameter {

	private String processId;
	private String paramName;
	private Double initialValue;
	private Double finalValue;
	private Double stepValue;

	public ProcessParameter() {

		this.processId = "twospeedspoissonPcNode";
		this.paramName = "firstMeanArrival";
		this.initialValue = 10.0;
		this.finalValue = 12.0;
		this.stepValue = 1.0;
	}

	public ProcessParameter(String processId, String paramName,
			Double initialValue, Double finalValue, Double stepValue) {
		super();
		this.processId = processId;
		this.paramName = paramName;
		this.initialValue = initialValue;
		this.finalValue = finalValue;
		this.stepValue = stepValue;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
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
