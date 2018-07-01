package it.unipr.ce.dsg.deus.example.recursivenetworks;

import java.util.Properties;
import java.util.Random;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;


public class RecursiveNetworkConfigurationEvent extends Event {

	private static final String THIRD_LEVEL = "thirdLevel";
	private boolean thirdLevel = false;
	private static final String NOHR = "nohr";
	private boolean nohr = false;
	private static final String MAX_RT_SIZE = "maxRTSize";
	private int maxRTSize = 30;
	
	public RecursiveNetworkConfigurationEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		initialize();
	}

	public void initialize() {
		if (params.containsKey(THIRD_LEVEL))
			thirdLevel = Boolean.parseBoolean(params.getProperty(THIRD_LEVEL));
		if (params.containsKey(NOHR))
			nohr = Boolean.parseBoolean(params.getProperty(NOHR));
		if (params.containsKey(MAX_RT_SIZE))
			maxRTSize = Integer.valueOf(params.getProperty(MAX_RT_SIZE));
	}
	
	@Override
	public void run() throws RunException {
		
		int n = ((RecursiveNetworkPeer) this.getEngine().getNodes().get(0)).getNumSubnetworks();
		//System.out.println("n = " + n);
		int[] subnetSize = new int[n];
		for (int i = 0; i < n; i++)
			subnetSize[i] = 1;
		
		Random random = this.getEngine().getSimulationRandom(); 
		
		// set names
		for (int i = 0; i < this.getEngine().getNodes().size(); i++) {
			RecursiveNetworkPeer peer = (RecursiveNetworkPeer) this.getEngine().getNodes().get(i);
			
			if (peer.getNeighbors().size() > 0) {
				RecursiveNetworkPeer neighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(random.nextInt(peer.getNeighbors().size()));
				String sn = neighbor.getSubnetworkNumber();
				int j = -1;
				if (sn == null) 
					j = random.nextInt(n)+1;
				else
					j = Integer.valueOf(sn);
				peer.setName(j + "." + subnetSize[j-1]);
				//System.out.println(peer.getName());
				subnetSize[j-1] += 1;	
			}
		}
		
		/*
		for (int i = 0; i < n; i++)
			System.out.println("NET" + (i+1) + " has " + subnetSize[i] + " nodes");
		*/
		
		
		if (nohr) {
			for (int i = 0; i < this.getEngine().getNodes().size(); i++) {
				RecursiveNetworkPeer peer = (RecursiveNetworkPeer) this.getEngine().getNodes().get(i);
				peer.getRoutingTable().clear();
				peer.storeInformation(1.0); // save initial information 
				peer.storeRouteHistogram(peer.getCurrentRouteHistogram()); // save initial route histogram
				String sn = peer.getSubnetworkNumber();
				for (int j = 0; j < peer.getNeighbors().size(); j++) {
					RecursiveNetworkPeer neighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(j);
					for (int m = 0; m < neighbor.getNeighbors().size(); m++) {
						RecursiveNetworkPeer neighborOfNeighbor = (RecursiveNetworkPeer) neighbor.getNeighbors().get(m);
						String nnsn = neighborOfNeighbor.getSubnetworkNumber();
						if (!neighborOfNeighbor.getName().equals(peer.getName())) {
							if (nnsn.equals(sn) && !peer.getNeighbors().contains(neighborOfNeighbor) && !peer.getRoutingTable().containsKey(neighborOfNeighbor.getName())) {
								if (peer.getRoutingTable().size() < maxRTSize)
									peer.getRoutingTable().put(neighborOfNeighbor.getName(), neighbor.getName());
								//System.out.println("Route: " + neighbor.getName());
							}
						}
					}
				}
			}	
			return;
		}
		
		
		// fill routing tables
		for (int i = 0; i < this.getEngine().getNodes().size(); i++) {
			RecursiveNetworkPeer peer = (RecursiveNetworkPeer) this.getEngine().getNodes().get(i);
			peer.storeInformation(1.0); // save initial information 
			peer.storeRouteHistogram(peer.getCurrentRouteHistogram()); // save initial route histogram 
			peer.configureRoutingTable(maxRTSize, thirdLevel);
		}
		
		
	}

}
