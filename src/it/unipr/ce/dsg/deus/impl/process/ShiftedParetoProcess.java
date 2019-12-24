package it.unipr.ce.dsg.deus.impl.process;

import java.util.ArrayList;
import java.util.Properties;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.util.Distributions;

public class ShiftedParetoProcess extends Process {

	private static final String ALPHA = "alpha";
	private float alpha = 0;
	private static final String BETA = "beta";
	private float beta = 0;
	
	public ShiftedParetoProcess(String id, Properties params,
            ArrayList<Node> referencedNodes, ArrayList<Event> referencedEvents)
            throws InvalidParamsException {
    super(id, params, referencedNodes, referencedEvents);
    	initialize();
	}
	
	public void initialize() throws InvalidParamsException {
        if (params.getProperty(ALPHA) == null)
            throw new InvalidParamsException(ALPHA + " param is expected.");
        try {
            alpha = Float.parseFloat(params.getProperty(ALPHA));
        } catch (NumberFormatException ex) {
            throw new InvalidParamsException(ALPHA + " must be a valid float value.");
        }
        if (params.getProperty(BETA) == null)
            throw new InvalidParamsException(BETA + " param is expected.");
        try {
            beta = Float.parseFloat(params.getProperty(BETA));
        } catch (NumberFormatException ex) {
            throw new InvalidParamsException(BETA + " must be a valid float value.");
        }
	}
	
	public float getNextTriggeringTime(Event event, float virtualTime) {
        return virtualTime + (float) Distributions.shiftedPareto(event.getEventRandom(), alpha, beta);
	}
}
