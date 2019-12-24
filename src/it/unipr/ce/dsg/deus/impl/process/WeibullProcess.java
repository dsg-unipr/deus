package it.unipr.ce.dsg.deus.impl.process;

import java.util.ArrayList;
import java.util.Properties;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.util.Distributions;

public class WeibullProcess extends Process {

	private static final String K = "k";
	private float k = 0;
	private static final String ALPHA = "alpha";
	private float alpha = 0;
	
	public WeibullProcess(String id, Properties params,
            ArrayList<Node> referencedNodes, ArrayList<Event> referencedEvents)
            throws InvalidParamsException {
    super(id, params, referencedNodes, referencedEvents);
    	initialize();
	}

	public void initialize() throws InvalidParamsException {
        if (params.getProperty(K) == null)
        	throw new InvalidParamsException(K + " param is expected.");
        if (params.getProperty(ALPHA) == null)
            throw new InvalidParamsException(ALPHA + " param is expected.");
        try {
        	k = Float.parseFloat(params.getProperty(K));
        } catch (NumberFormatException ex) {
                throw new InvalidParamsException(K + " must be a valid float value.");
        }
        try {
            alpha = Float.parseFloat(params.getProperty(ALPHA));
        } catch (NumberFormatException ex) {
            throw new InvalidParamsException(ALPHA + " must be a valid float value.");
        }
	}
	
	public float getNextTriggeringTime(Event event, float virtualTime) {
        return virtualTime + (float) Distributions.weibull(event.getEventRandom(), k, alpha);
	}
}
