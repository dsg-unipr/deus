package it.unipr.ce.dsg.deus.impl.process;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.util.Distributions;

import java.util.ArrayList;
import java.util.Properties;
//import java.util.Random;

/**
 * This class represents a Homogeneous Poisson Process with two speeds. The process
 * starts with the first speed and after the current simulation virtual time
 * reached a defined threshold the process will change to the second speed. It
 * accepts parameters called "firstMeanArrival" (float) that is used to generate
 * the first speed, "secondMeanArrival" (float) that is used to generate the
 * second speed, "vtThreshold" (float) that is used to determine when change
 * from first to second speed. 
 * 
 * @author Matteo Agosti (agosti@ce.unipr.it)
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class TwoSpeedsPoissonProcess extends Process {
	private static final String FIRST_MEAN_ARRIVAL = "firstMeanArrival";
	private static final String SECOND_MEAN_ARRIVAL = "secondMeanArrival";
	private static final String VT_THRESHOLD = "vtThreshold";

	private float firstMeanArrival = 0;
	private float secondMeanArrival = 0;
	private float vtThreshold = 0;

	public TwoSpeedsPoissonProcess(String id, Properties params,
			ArrayList<Node> referencedNodes, ArrayList<Event> referencedEvents)
			throws InvalidParamsException {
		super(id, params, referencedNodes, referencedEvents);
		initialize();
	}

	public float getFirstMeanArrival() {
		return firstMeanArrival;
	}

	public float getSecondMeanArrival() {
		return secondMeanArrival;
	}

	public void initialize() throws InvalidParamsException {
		if (params.getProperty(FIRST_MEAN_ARRIVAL) == null)
			throw new InvalidParamsException(FIRST_MEAN_ARRIVAL
					+ " param is expected.");

		try {
			firstMeanArrival = Float.parseFloat(params
					.getProperty(FIRST_MEAN_ARRIVAL));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(FIRST_MEAN_ARRIVAL
					+ " must be a valid float value.");
		}
		if (params.getProperty(SECOND_MEAN_ARRIVAL) == null)
			throw new InvalidParamsException(SECOND_MEAN_ARRIVAL
					+ " param is expected.");

		try {
			secondMeanArrival = Float.parseFloat(params
					.getProperty(SECOND_MEAN_ARRIVAL));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(SECOND_MEAN_ARRIVAL
					+ " must be a valid float value.");
		}
		if (params.getProperty(VT_THRESHOLD) == null)
			throw new InvalidParamsException(VT_THRESHOLD
					+ " param is expected.");

		try {
			vtThreshold = Float.parseFloat(params.getProperty(VT_THRESHOLD));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(VT_THRESHOLD
					+ " must be a valid float value.");
		}
	}

	public float getNextTriggeringTime(Event event, float virtualTime) {
		if (virtualTime < vtThreshold)
			//return virtualTime + expRandom(event.getEventRandom(), (float) 1 / firstMeanArrival);
			return virtualTime + (float) Distributions.exp(event.getEventRandom(), (float) 1 / firstMeanArrival);
		else
			//return virtualTime + expRandom(event.getEventRandom(), (float) 1 / secondMeanArrival);
			return virtualTime + (float) Distributions.exp(event.getEventRandom(), (float) 1 / secondMeanArrival);
	}

	/*
	// returns exponentially distributed random variable
	private float expRandom(Random random, float lambda) {
		float myRandom = (float) (-Math.log(1-random.nextFloat()) / lambda);
		return myRandom;
	}
	*/
}
