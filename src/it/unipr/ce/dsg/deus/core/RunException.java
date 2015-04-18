package it.unipr.ce.dsg.deus.core;

/**
 * This class represents the exceptions thrown in case the run method of
 * simulation events fails.
 * 
 * @author Matteo Agosti
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class RunException extends Exception {

	public RunException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;

}
