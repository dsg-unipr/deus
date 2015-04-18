package it.unipr.ce.dsg.deus.impl.event;

import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

import java.util.Properties;

/**
 * This event represents the death of a simulation node. During the execution of
 * the event the specified node will be killed or, in case nothing is specified,
 * a random node will be killed.
 * 
 * @author Matteo Agosti (agosti@ce.unipr.it)
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class DeathEvent extends Event {

	private Node nodeToKill = null;

	public DeathEvent(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
	}

	public Object clone() {
		DeathEvent clone = (DeathEvent) super.clone();
		clone.nodeToKill = null;
		return clone;
	}

	public void setNodeToKill(Node nodeToKill) {
		this.nodeToKill = nodeToKill;
	}

	@Override
	public void run() throws RunException {
		if (nodeToKill == null) {
			if (Engine.getDefault().getNodes().size() > 0) {
				int nodeIndex = Engine.getDefault().getSimulationRandom().nextInt(Engine.getDefault().getNodes().size());
				Engine.getDefault().removeNode(Engine.getDefault().getNodes().get(nodeIndex));
			}
				/*
				Engine.getDefault().getNodes().remove(
						Engine.getDefault().getSimulationRandom().nextInt(
								Engine.getDefault().getNodes().size()));
								*/
		} else {
			//System.out.println("death: nodeToKill = " + nodeToKill.getKey());
			Engine.getDefault().removeNode(nodeToKill);
			/*
			int n = Engine.getDefault().getNodes().indexOf(nodeToKill);
			if (n > -1)
				Engine.getDefault().getNodes().remove(n);
				*/
		}

	}

}
