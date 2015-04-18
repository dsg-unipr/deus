package it.unipr.ce.dsg.deus.impl.resource;

import java.util.Properties;

import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Resource;

/**
 * This class represents a generic allocable resource. This kind of resource has
 * a <code>type</code> (string) and <code>amount</code> (double) parameter that
 * should be specified into the simulation configuration file.
 * 
 * @author Matteo Agosti (agosti@ce.unipr.it)
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class AllocableResource extends Resource {
	private static final String TYPE_PARAM = "type";
	private static final String AMOUNT_PARAM = "amount";

	private String type = null;
	private double amount = 0;

	public AllocableResource(Properties params) throws InvalidParamsException {
		super(params);
		initialize();
	}

	public boolean equals(Object o) {
		if (!(o instanceof AllocableResource))
			return false;
		return type.equals(((AllocableResource) o).getType());
	}

	public void initialize() throws InvalidParamsException {
		if (params.containsKey(TYPE_PARAM))
			type = params.getProperty(TYPE_PARAM);
		else
			throw new InvalidParamsException("The parameter " + TYPE_PARAM
					+ " must be specified");
		if (params.containsKey(AMOUNT_PARAM))
			try {
				amount = Double.parseDouble(params.getProperty(AMOUNT_PARAM));
			} catch (NumberFormatException e) {
				throw new InvalidParamsException("The parameter "
						+ AMOUNT_PARAM + " must be a valid double value");
			}
		else
			throw new InvalidParamsException("The parameter " + AMOUNT_PARAM
					+ " must be specified");
	}

	public String getType() {
		return type;
	}

	public double getAmount() {
		return amount;
	}

}
