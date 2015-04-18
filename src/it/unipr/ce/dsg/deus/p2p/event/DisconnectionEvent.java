package it.unipr.ce.dsg.deus.p2p.event;

import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;
import it.unipr.ce.dsg.deus.p2p.node.Peer;

import java.util.Properties;


/**
 * <p>
 * This NodeEvent disconnects the associatedNode (which must be a Peer) 
 * from a specified target Peer (if target is null, the associatedNode 
 * is disconnected from all its neighbors). 
 * </p>
 * 
 * @author Matteo Agosti (agosti@ce.unipr.it)
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 *
 */
public class DisconnectionEvent extends Event {

	private Peer target = null;

	public DisconnectionEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		initialize();
	}

	public void initialize() throws InvalidParamsException {

	}

	public void setNodeToDisconnectFrom(Peer target) {
		this.target = target;
	}
	
	public Object clone() {
		DisconnectionEvent clone = (DisconnectionEvent) super.clone();
		clone.target = null;
		return clone;
	}

	public void run() throws RunException {
		String nodeId = null;
		if (associatedNode == null) {
			do {
				nodeId = getParentProcess().getReferencedNodes().get(
						Engine.getDefault().getSimulationRandom().nextInt(
								getParentProcess().getReferencedNodes().size())).getId();
				associatedNode = Engine.getDefault().getNodes().get(Engine.getDefault().getSimulationRandom()
								.nextInt(Engine.getDefault().getNodes().size()));
			} while (!associatedNode.getId().equals(nodeId) && !(associatedNode instanceof Peer));
		}
		if (!(associatedNode instanceof Peer))
			throw new RunException("The associated node is not a Peer!");
		if (target != null)
			((Peer) associatedNode).removeNeighbor(this.target);
		else { // disconnect from all neighbors
			((Peer) associatedNode).resetNeighbors();
		}
	}

}
