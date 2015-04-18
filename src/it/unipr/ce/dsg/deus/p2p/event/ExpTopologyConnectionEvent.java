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
 * to "1..numInitialConnections" other Peers, randomly chosen.
 * If this event is executed by the Engine after the initiator Peer
 * has been the target of connections from other Peers, new connections 
 * are created only if the initiator has less than "numInitialConnections". 
 * </p>
 * 
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 *
 */

public class ExpTopologyConnectionEvent extends Event {
	private static final String IS_BIDIRECTIONAL = "isBidirectional";
	private static final String N0 = "n0";
	private static final String M = "m";
	
	private boolean isBidirectional = false;
	private int n0 = 0;
	private int m = 0;
	
	public ExpTopologyConnectionEvent(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
		initialize();
	}

	public void initialize() throws InvalidParamsException {
		if (params.containsKey(IS_BIDIRECTIONAL))
			isBidirectional = Boolean.parseBoolean(params.getProperty(IS_BIDIRECTIONAL)); 
		if (params.containsKey(N0))
			n0 = Integer.parseInt(params.getProperty(N0));
		if (params.containsKey(M))
			m = Integer.parseInt(params.getProperty(M));
	}

	public Object clone() {
		ExpTopologyConnectionEvent clone = (ExpTopologyConnectionEvent) super.clone();
		return clone;
	}
 
	public void run() throws RunException {
		if (!(associatedNode instanceof Peer))
			throw new RunException("The associated node is not a Peer!");
		
		int n = Engine.getDefault().getNodes().size();
		if (n == n0) {
			// connect all nodes
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if ( i != j) {
						Peer target = (Peer) Engine.getDefault().getNodes().get(j);
						if (((Peer) associatedNode).addNeighbor(target)) {
							if (isBidirectional)
								target.addNeighbor(((Peer) associatedNode));
						}
					}					
				}
			}
		}
		else if (n > n0) {
			//int m = Engine.getDefault().getSimulationRandom().nextInt(numInitialConnections+1);
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
		
		//System.out.println("num neighbors = " + ((Peer) associatedNode).getNeighbors().size());
	}

	public int getN0() {
		return n0;
	}

	public void setN0(int n0) {
		this.n0 = n0;
	}
	
	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

}
