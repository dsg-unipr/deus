package it.unipr.ce.dsg.deus.p2p.event;

import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;
import it.unipr.ce.dsg.deus.p2p.node.Peer;

import java.util.Properties;


/**
 * <p>
 * This NodeEvent connects the associatedNode (which must be a Peer) 
 * to a specified target Peer (if target is null, the Peer is randomly chosen). 
 * </p>
 * 
 * @author Matteo Agosti (agosti@ce.unipr.it)
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 *
 */
public class SingleConnectionEvent extends Event {
	private static final String IS_BIDIRECTIONAL = "isBidirectional";
	
	private boolean isBidirectional = false;
	private Peer target = null;
	
	public SingleConnectionEvent(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
		initialize();
	}

	public void initialize() throws InvalidParamsException {
		if (params.containsKey(IS_BIDIRECTIONAL))
			isBidirectional = Boolean.parseBoolean(params.getProperty(IS_BIDIRECTIONAL)); 
	}

	public void setNodeToConnectTo(Peer target) {
		this.target = target;
	}

	public Object clone() {
		SingleConnectionEvent clone = (SingleConnectionEvent) super.clone();
		clone.target = null;
		return clone;
	}

	public void run() throws RunException {
		if (!(associatedNode instanceof Peer))
			throw new RunException("The associated node is not a Peer!");
		
		if (target == null) {
			if (Engine.getDefault().getNodes().size() > 1) {
				do {
					int randomInt = Engine.getDefault().getSimulationRandom().nextInt(
							Engine.getDefault().getNodes().size());
					Node n = Engine.getDefault().getNodes().get(randomInt);
					if (!(n instanceof Peer)) {
						target = null;
						continue;
					}
					target = (Peer) n; 
				} while ( (target == null) || target.getKey() == associatedNode.getKey());
			}
			else
				return;
		}
		
		((Peer) associatedNode).addNeighbor(target);
		if (isBidirectional)
			target.addNeighbor((Peer) associatedNode);
	}

}
