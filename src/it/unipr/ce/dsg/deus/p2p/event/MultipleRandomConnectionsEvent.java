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
 * to "numInitialConnections" other Peers, randomly chosen.
 * If this event is executed by the Engine after the initiator Peer
 * has been the target of connections from other Peers, new connections 
 * are created only if the initiator has less than "numInitialConnections". 
 * </p>
 * 
 * @author Matteo Agosti (agosti@ce.unipr.it)
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 *
 */

public class MultipleRandomConnectionsEvent extends Event {
	private static final String IS_BIDIRECTIONAL = "isBidirectional";
	private static final String MAX_NUM_INITIAL_CONNECTIONS = "maxNumInitialConnections";
	
	private boolean isBidirectional = false;
	private int maxNumInitialConnections = 0;
	
	public MultipleRandomConnectionsEvent(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
		initialize();
	}

	public void initialize() throws InvalidParamsException {
		if (params.containsKey(IS_BIDIRECTIONAL))
			isBidirectional = Boolean.parseBoolean(params.getProperty(IS_BIDIRECTIONAL)); 
		if (params.containsKey(MAX_NUM_INITIAL_CONNECTIONS))
			maxNumInitialConnections = Integer.parseInt(params.getProperty(MAX_NUM_INITIAL_CONNECTIONS));
	}

	public Object clone() {
		MultipleRandomConnectionsEvent clone = (MultipleRandomConnectionsEvent) super.clone();
		return clone;
	}
 
	public void run() throws RunException {
		if (!(associatedNode instanceof Peer))
			throw new RunException("The associated node is not a Peer!");
		int n = Engine.getDefault().getNodes().size();
		if (n == 1)
			return;
		int m = 0;
		if (n <= maxNumInitialConnections)
			m = Engine.getDefault().getNodes().size() - 1;
		else
			m = Engine.getDefault().getSimulationRandom().nextInt(maxNumInitialConnections+1);
		do {
			Peer target = null;			
			do {			
				int randomInt = Engine.getDefault().getSimulationRandom().nextInt(n);
				Node randomNode = Engine.getDefault().getNodes().get(randomInt);
				if (!(randomNode instanceof Peer)) {
					target = null;					
					continue;
				}
				target = (Peer) randomNode; 
			} while ((target == null) || (target.getKey() == ((Peer) associatedNode).getKey()));
			if (((Peer) associatedNode).addNeighbor(target)) {
				if (isBidirectional)
					target.addNeighbor(((Peer) associatedNode));
			}
		} while (((Peer) associatedNode).getNeighbors().size() < m);
	}

	public int getNumInitialConnections() {
		return maxNumInitialConnections;
	}

	public void setNumInitialConnections(int numInitialConnections) {
		this.maxNumInitialConnections = numInitialConnections;
	}

}
