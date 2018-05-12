package it.unipr.ce.dsg.deus.core;

/**
 * This class represents the exceptions thrown in case one of the components
 * into the simulation fails its execution.
 * 
 * @author Matteo Agosti
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class SimulationException extends Exception {

	public SimulationException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;

}
