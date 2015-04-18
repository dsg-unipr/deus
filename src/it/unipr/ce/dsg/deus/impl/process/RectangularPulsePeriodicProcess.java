package it.unipr.ce.dsg.deus.impl.process;

import java.util.ArrayList;
import java.util.Properties;

import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;


/**
 * This class represents a generic periodic process defined within a time interval 
 * between vt = 0 and vt = VTmax. It accepts the following parameters: "period" (float), 
 * that is used to compute next event's triggering time, "startVtThreshold" (float) 
 * and "stopVtThreshold" (float) which are the limits of the time interval in 
 * which the Poisson process is defined. Each time the process receives a request 
 * for generating a new triggering time, it will compute it by adding the period 
 * value to the current simulation virtual time. 
 * 
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class RectangularPulsePeriodicProcess extends Process {
	private static final String PERIOD = "period";
	private static final String START_VT_THRESHOLD = "startVtThreshold";
	private static final String STOP_VT_THRESHOLD = "stopVtThreshold";
	
	private float period = 0;
	private float startVtThreshold = 0;
	private float stopVtThreshold = 0;
	
	public RectangularPulsePeriodicProcess(String id, Properties params,
			ArrayList<Node> referencedNodes, ArrayList<Event> referencedEvents)
	throws InvalidParamsException {
		super(id, params, referencedNodes, referencedEvents);
		initialize();
	}
	
	public float getPeriod() {
		return period;
	}
	
	public float getStartVtThreshold() {
		return startVtThreshold;
	}
	
	public float getStopVtThreshold() {
		return stopVtThreshold;
	}
	
	public void initialize() throws InvalidParamsException {		
		if (params.getProperty(START_VT_THRESHOLD) == null)
			throw new InvalidParamsException(START_VT_THRESHOLD
					+ " param is expected.");
		try {
			startVtThreshold = Float.parseFloat(params
					.getProperty(START_VT_THRESHOLD));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(START_VT_THRESHOLD
					+ " must be a valid float value.");
		}
		
		if (params.getProperty(STOP_VT_THRESHOLD) == null)
			throw new InvalidParamsException(STOP_VT_THRESHOLD
					+ " param is expected.");
		try {
			stopVtThreshold = Float.parseFloat(params
					.getProperty(STOP_VT_THRESHOLD));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(STOP_VT_THRESHOLD
					+ " must be a valid float value.");
		}
		
		if (startVtThreshold > stopVtThreshold)
			throw new InvalidParamsException(STOP_VT_THRESHOLD
					+ " must be higher than " + START_VT_THRESHOLD);
		
		if (params.getProperty(PERIOD) == null)
			throw new InvalidParamsException(PERIOD
					+ " param is expected.");
		try {
			period = Float.parseFloat(params
					.getProperty(PERIOD));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(PERIOD
					+ " must be a valid float value.");
		}
	}
	
	public float getNextTriggeringTime(Event event, float virtualTime) {
		
		if ((virtualTime < startVtThreshold) && ((startVtThreshold + period) < stopVtThreshold))
			return startVtThreshold + period;
		else if ((virtualTime >= startVtThreshold) && ((virtualTime + period) < stopVtThreshold))
			return virtualTime + period;
		else
			return virtualTime + Engine.getDefault().getMaxVirtualTime(); // thus the event will not be executed
		
		/*
		if (virtualTime < startVtThreshold)
			return virtualTime + startVtThreshold;
		else if ((virtualTime >= startVtThreshold) && (virtualTime < stopVtThreshold))
			return virtualTime + period;
		else
			return virtualTime + Engine.getDefault().getMaxVirtualTime(); // thus the event will not be executed
		*/
	}

}
