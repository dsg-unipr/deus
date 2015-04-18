package it.unipr.ce.dsg.deus.impl.process;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;

import java.util.ArrayList;
import java.util.Properties;

/**
 * This class represents a generic Periodic process with two speeds. The process
 * starts with the first speed and after the current simulation virtual time
 * reached a defined threshold the process will change to the second speed. It
 * accepts parameters called "firstPeriodicArrival" (float) that is used to generate
 * the first speed, "secondPeriodicArrival" (float) that is used to generate the
 * second speed, "vtThreshold" (float) that is used to determine when change
 * from first to second speed. Each time the process receives a request for
 * generating a new triggering time, it will compute it by adding the current
 * simulation virtual the value of an Homogeneous Periodic Process with the rate
 * parameter [first|second]periodicArrival time.
 * 
 * @author Matteo Agosti (agosti@ce.unipr.it)
 * @author Marco Muro (marco.muro@studenti.unipr.it)
 * 
 */
public class TwoSpeedsPeriodicProcess extends Process {
	private static final String FIRST_PERIODIC_ARRIVAL = "firstPeriodArrival";
	private static final String SECOND_PERIODIC_ARRIVAL = "secondPeriodArrival";
	private static final String VT_THRESHOLD = "vtThreshold";

	private float firstPeriodArrival = 0;
	private float secondPeriodArrival = 0;
	private float vtThreshold = 0;

	public TwoSpeedsPeriodicProcess(String id, Properties params,
			ArrayList<Node> referencedNodes, ArrayList<Event> referencedEvents)
			throws InvalidParamsException {
		super(id, params, referencedNodes, referencedEvents);
		initialize();
	}

	public float getFirstMeanArrival() {
		return firstPeriodArrival;
	}

	public float getSecondMeanArrival() {
		return secondPeriodArrival;
	}

	public void initialize() throws InvalidParamsException {
		if (params.getProperty(FIRST_PERIODIC_ARRIVAL) == null)
			throw new InvalidParamsException(FIRST_PERIODIC_ARRIVAL
					+ " param is expected.");

		try {
			firstPeriodArrival = Float.parseFloat(params
					.getProperty(FIRST_PERIODIC_ARRIVAL));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(FIRST_PERIODIC_ARRIVAL
					+ " must be a valid float value.");
		}
		if (params.getProperty(SECOND_PERIODIC_ARRIVAL) == null)
			throw new InvalidParamsException(SECOND_PERIODIC_ARRIVAL
					+ " param is expected.");

		try {
			secondPeriodArrival = Float.parseFloat(params
					.getProperty(SECOND_PERIODIC_ARRIVAL));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(SECOND_PERIODIC_ARRIVAL
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
			return virtualTime + firstPeriodArrival;
		else
			return virtualTime + secondPeriodArrival;
		}

}
