package it.unipr.ce.dsg.deus.example.recursivenetworks;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;
import it.unipr.ce.dsg.deus.p2p.node.Peer;

import java.util.Properties;
import java.util.Random;


public class ERTopologyBuilderEvent extends Event {

	private static final String ALPHA = "alpha";
	private int alpha = 5;
	
	public ERTopologyBuilderEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		initialize();
	}

	public void initialize() {
		if (params.containsKey(ALPHA))
			alpha = Integer.valueOf(params.getProperty(ALPHA));
	}

	@Override
	public void run() throws RunException {
		Random random = this.getEngine().getSimulationRandom();
		
		int R = this.getEngine().getNodes().size();
		
		for (int i = 1; i < R; i++) {
			for (int j = 0; j < i; j++) {
				int l = random.nextInt(R+1);
				if (l <= alpha) {
					((Peer)this.getEngine().getNodes().get(i)).addNeighbor((Peer)this.getEngine().getNodes().get(j));
					((Peer)this.getEngine().getNodes().get(j)).addNeighbor((Peer)this.getEngine().getNodes().get(i));
				}
			}
		}
	
		/*
		for (int i = 0; i < R; i++) {
			Peer target = (Peer) this.getEngine().getNodes().get(i);
			System.out.println(target.getNeighbors().size());
		}
		*/
		
	}
	
}
