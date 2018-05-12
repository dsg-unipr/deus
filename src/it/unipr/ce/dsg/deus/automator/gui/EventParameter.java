package it.unipr.ce.dsg.deus.automator.gui;

/**
 * @author Mirco Rosa (mirco.rosa.91@gmail.com) [Event Parametrization]
 * */

public class EventParameter {

	private String eventId;
	private String paramName;
	private Double initialValue;
	private Double finalValue;
	private Double stepValue;

	public EventParameter() {
		this.eventId = "connection";
		this.paramName = "parameter";
		this.initialValue = 10.0;
		this.finalValue = 12.0;
		this.stepValue = 1.0;
	}

	public EventParameter(String eventId, String paramName, Double initialValue, Double finalValue, Double stepValue) {
		super();
		this.eventId = eventId;
		this.paramName = paramName;
		this.initialValue = initialValue;
		this.finalValue = finalValue;
		this.stepValue = stepValue;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
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
