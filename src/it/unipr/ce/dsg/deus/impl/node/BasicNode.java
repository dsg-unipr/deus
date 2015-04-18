package it.unipr.ce.dsg.deus.impl.node;

import java.util.ArrayList;
import java.util.Properties;

import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Resource;

/**
 * Basic implementation of the node class, without any specific properties.
 * 
 * @author Matteo Agosti (agosti@ce.unipr.it)
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class BasicNode extends Node {

	public BasicNode(String id, Properties params, ArrayList<Resource> resources)
			throws InvalidParamsException {
		super(id, params, resources);
		initialize();
	}

	@Override
	public void initialize() throws InvalidParamsException {
		// nothing to be done
	}

	public Object clone() {
		BasicNode clone = (BasicNode) super.clone();
		return clone;
	}
}
