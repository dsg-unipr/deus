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
 * This class represents a Homogeneous Poisson Process, that is a process that schedules
 * events with interarrival time being an exponentially distributed random variable. 
 * The process accepts one parameter called "meanArrival" (float), representing the
 * mean interarrival time between two consecutive events, that is used 
 * to generate the triggering time of next event.
 * 
 * @author Matteo Agosti (agosti@ce.unipr.it)
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class PoissonProcess extends Process {
	private static final String MEAN_ARRIVAL = "meanArrival";

	private float meanArrival = 0;

	public PoissonProcess(String id, Properties params,
			ArrayList<Node> referencedNodes, ArrayList<Event> referencedEvents)
			throws InvalidParamsException {
		super(id, params, referencedNodes, referencedEvents);
		initialize();
	}

	public float getMeanArrival() {
		return meanArrival;
	}

	public float getNextTriggeringTime(Event event, float virtualTime) {
		//return virtualTime + expRandom(event.getEventRandom(), (float) 1 / meanArrival);
		return virtualTime + (float) Distributions.exp(event.getEventRandom(), (float) 1 / meanArrival);
	}

	public void initialize() throws InvalidParamsException {
		if (params.getProperty(MEAN_ARRIVAL) == null)
			throw new InvalidParamsException(MEAN_ARRIVAL
					+ " param is expected.");

		try {
			meanArrival = Float.parseFloat(params.getProperty(MEAN_ARRIVAL));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(MEAN_ARRIVAL
					+ " must be a valid float value.");
		}
	}

	// returns exponentially distributed random variable
	/*
	private float expRandom(Random random, float lambda) {
		float randomFloat = (float) (-Math.log(1-random.nextFloat()) / lambda);
		return randomFloat;
	}
	*/
}
