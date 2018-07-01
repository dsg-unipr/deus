package it.unipr.ce.dsg.deus.example.recursivenetworks;

import java.util.Properties;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;


public class RecursiveNetworkRTUpdateEvent extends Event {

	private static final String THIRD_LEVEL = "thirdLevel";
	private boolean thirdLevel = false;
	private static final String NOHR = "nohr";
	private boolean nohr = false;
	private static final String DELTA_RT_SIZE = "deltaRTSize";
	private int deltaRTSize = 5;
	
	public RecursiveNetworkRTUpdateEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		initialize();
	}

	public void initialize() {
		if (params.containsKey(THIRD_LEVEL))
			thirdLevel = Boolean.parseBoolean(params.getProperty(THIRD_LEVEL));
		if (params.containsKey(NOHR))
			nohr = Boolean.parseBoolean(params.getProperty(NOHR));
		if (params.containsKey(DELTA_RT_SIZE))
			deltaRTSize = Integer.valueOf(params.getProperty(DELTA_RT_SIZE));
	}
	
	@Override
	public void run() throws RunException {
		
		//System.out.println(">>>>>>>>> RTUpdate <<<<<<<<<<");
				
		if (nohr) {
			for (int i = 0; i < this.getEngine().getNodes().size(); i++) {
				RecursiveNetworkPeer peer = (RecursiveNetworkPeer) this.getEngine().getNodes().get(i);
				peer.getRoutingTable().clear();
				String sn = peer.getSubnetworkNumber();
				//System.out.println("Peer: " + peer.getName());
				for (int j = 0; j < peer.getNeighbors().size(); j++) {
					RecursiveNetworkPeer neighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(j);
					for (int m = 0; m < neighbor.getNeighbors().size(); m++) {
						RecursiveNetworkPeer neighborOfNeighbor = (RecursiveNetworkPeer) neighbor.getNeighbors().get(m);
						String nnsn = neighborOfNeighbor.getSubnetworkNumber();
						if (!neighborOfNeighbor.getName().equals(peer.getName())) {
							if (nnsn.equals(sn) && !peer.getNeighbors().contains(neighborOfNeighbor) && !peer.getRoutingTable().containsKey(neighborOfNeighbor.getName())) {
								if (peer.getRoutingTable().size() < peer.getRoutingTable().size() + deltaRTSize)
									peer.getRoutingTable().put(neighborOfNeighbor.getName(), neighbor.getName());
							}
						}
					}
				}
				//System.out.println("RT size: " + peer.getRoutingTable().size());
			}
			return;
		}
		
		// fill routing tables
		for (int i = 0; i < this.getEngine().getNodes().size(); i++) {
			RecursiveNetworkPeer peer = (RecursiveNetworkPeer) this.getEngine().getNodes().get(i);
			peer.configureRoutingTable(peer.getRoutingTable().size() + deltaRTSize, thirdLevel);
		}
		
	}

}
