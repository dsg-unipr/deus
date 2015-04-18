package it.unipr.ce.dsg.deus.core;

import java.util.Properties;

/**
 * This class represent the generic resource associated to a node.
 * 
 * @author Matteo Agosti
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public abstract class Resource {

	protected Properties params = null;

	/**
	 * Class constructor that builds the resource with its minimal set of
	 * properties. Each implementing class should call the super constructor and
	 * immediately after invoke the initialize method to check that the resource
	 * parameters are correct.
	 * 
	 * @param params
	 *            the properties that will be handled by the resource.
	 * @throws InvalidParamsException
	 *             if the given parameter are wrong.
	 */
	public Resource(Properties params) throws InvalidParamsException {
		this.params = params;
	}

	/**
	 * Provides the initialization of the resource according to the given
	 * parameters. This method should also perform a check on the parameters
	 * values.
	 * 
	 * @throws InvalidParamsException
	 *             if the parameters passed to the resource are wrong.
	 */
	public abstract void initialize() throws InvalidParamsException;

	/**
	 * Each extending class must implement the equals method.
	 */
	public abstract boolean equals(Object o);

}
