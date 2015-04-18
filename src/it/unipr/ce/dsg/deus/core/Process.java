package it.unipr.ce.dsg.deus.core;

import java.util.ArrayList;
import java.util.Properties;

/**
 * <p>
 * The Process class represents the simulation object responsible to determine
 * the timing of events scheduling.
 * </p>
 * <p>
 * Each process is identified by the configuration id, a set of properties, a
 * set of referenced nodes and a set of referenced events.
 * </p>
 * 
 * @author Matteo Agosti 
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public abstract class Process extends SimulationObject {
	protected String id = null;
	protected Properties params = null;
	protected ArrayList<Node> referencedNodes = null;
	protected ArrayList<Event> referencedEvents = null;

	/**
	 * Class constructor that builds the process with its minimal set of
	 * properties. Each implementing class should call the super constructor and
	 * immediately after invoke the initialize method to check that the process
	 * parameters are correct.
	 * 
	 * @param id
	 *            the identifier of the event as specified in the configuration
	 *            file.
	 * @param params
	 *            the properties that will be handled by the event.
	 * @param referencedNodes
	 *            the set of referenced nodes.
	 * @param referencedEvents
	 *            the set of referenced events.
	 * @throws InvalidParamsException
	 *             if the given parameter are wrong.
	 */
	public Process(String id, Properties params,
			ArrayList<Node> referencedNodes, ArrayList<Event> referencedEvents)
			throws InvalidParamsException {
		this.id = id;
		this.params = params;
		this.referencedNodes = referencedNodes;
		this.referencedEvents = referencedEvents;
	}

	/**
	 * Provides the initialization of the process according to the given
	 * parameters. This method should also perform a check on the parameters
	 * values.
	 * 
	 * @throws InvalidParamsException
	 *             if the parameters passed to the process are wrong.
	 */
	public abstract void initialize() throws InvalidParamsException;

	/**
	 * This method computes the next triggering time with the knowledge of the
	 * current simulation virtual time.
	 * 
	 * @param virtualTime
	 *            the current simulation virtual time.
	 * @return the next triggering time.
	 */
	public abstract float getNextTriggeringTime(Event event, float virtualTime);

	/**
	 * Performs the standard Object.equals comparison by using the process id as
	 * the criteria.
	 */
	public boolean equals(Object o) {
		return id.equals(((Process) o).getId());
	}

	/**
	 * Returns the process id.
	 * 
	 * @return the process id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the list of referenced nodes of the current process.
	 * 
	 * @return the list of referenced nodes of the current process.
	 */
	public ArrayList<Node> getReferencedNodes() {
		return referencedNodes;
	}

	/**
	 * Returns the list of referenced events of the current process.
	 * 
	 * @return the list of referenced events of the current process.
	 */
	public ArrayList<Event> getReferencedEvents() {
		return referencedEvents;
	}
}
