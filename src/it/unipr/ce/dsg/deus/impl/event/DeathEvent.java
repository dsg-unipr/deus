package it.unipr.ce.dsg.deus.impl.event;

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
			if (this.engine.getNodes().size() > 0) {
				int nodeIndex = this.engine.getSimulationRandom().nextInt(this.engine.getNodes().size());
				this.engine.removeNode(this.engine.getNodes().get(nodeIndex));
			}
				/*
				this.engine.getNodes().remove(
						this.engine.getSimulationRandom().nextInt(
								this.engine.getNodes().size()));
								*/
		} else {
			//System.out.println("death: nodeToKill = " + nodeToKill.getKey());
			this.engine.removeNode(nodeToKill);
			/*
			int n = this.engine.getNodes().indexOf(nodeToKill);
			if (n > -1)
				this.engine.getNodes().remove(n);
				*/
		}

	}

}
