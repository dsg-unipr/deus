package it.unipr.ce.dsg.deus.example.recursivenetworks;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;
import it.unipr.ce.dsg.deus.p2p.node.Peer;

import java.util.Properties;
import java.util.Random;

/*
import org.dsg.p2pam.node.NodeDataCollector;
import org.dsg.p2pam.util.SimulationProperties;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;
import peersim.graph.Graph;
*/

public class BATopologyBuilderEvent extends Event {

	private static final String N0 = "n0";
	private int n0 = 20;
	private static final String M = "m";
	private int m = 18; // must be < N0

	public BATopologyBuilderEvent(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
		initialize();
	}
	
	public void initialize() {
		if (params.containsKey(N0))
			n0 = Integer.valueOf(params.getProperty(N0));
		if (params.containsKey(M))
			m = Integer.valueOf(params.getProperty(M));
	}

	@Override
	public void run() throws RunException {
		Random random = this.getEngine().getSimulationRandom();
		
		for (int i = 0; i < n0; i++)
			for (int j = 0; j < i; j++) {
				((Peer)this.getEngine().getNodes().get(i)).addNeighbor((Peer)this.getEngine().getNodes().get(j));
				((Peer)this.getEngine().getNodes().get(j)).addNeighbor((Peer)this.getEngine().getNodes().get(i));
			}
		
		int N = this.getEngine().getNodes().size();
		for (int i = n0; i < N; i++) {
			Peer target = (Peer) this.getEngine().getNodes().get(i);
			int totalSupernodesDegree = 2*m*(i+n0);
			while (target.getNeighbors().size() < m) {
				for (int j = 0; j < N; j++) {
					if ( target.getKey() == this.getEngine().getNodes().get(j).getKey()
							|| ((Peer)this.getEngine().getNodes().get(j)).getNeighbors().size() == 0
							|| target.getNeighbors().contains((Peer)this.getEngine().getNodes().get(j)) )
						continue;
					double connectionProbability = 
							(double) ((Peer)this.getEngine().getNodes().get(j)).getNeighbors().size() / totalSupernodesDegree;
					
					if (random.nextDouble() <= connectionProbability) {
						target.addNeighbor((Peer)this.getEngine().getNodes().get(j));
						((Peer)this.getEngine().getNodes().get(j)).addNeighbor(target);
						break;
					}
				}
			}
		}
		
		/*
		for (int i = 0; i < N; i++) {
			Peer target = (Peer) this.getEngine().getNodes().get(i);
			System.out.println(target.getNeighbors().size());
		}
		*/
	}

}
