package it.unipr.ce.dsg.deus.example.recursivenetworks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Resource;
import it.unipr.ce.dsg.deus.p2p.node.Peer;

public class RecursiveNetworkPeer extends Peer {

	private static final String NUM_SUBNETWORKS = "numSubnetworks";
	private int numSubnetworks; // number of subnetworks
	private String name = null;
	private boolean known = false;
	private boolean visited = false;
	private HashMap<String,String> routingTable = null;
	private double storedInformation;
	private HashMap<String,Integer> routeHistogram;
	private HashMap<String,Integer> storedRouteHistogram;
	
	// logs
	private int numRoutingOperations = 0;
	private int totalHopCount = 0;
	private int numSuccessfulRoutingOperations = 0;
	private int numExplorationMessages = 0;
	public boolean flag = false;
	
	public RecursiveNetworkPeer(String id, Properties params,
			ArrayList<Resource> resources) throws InvalidParamsException {
		super(id, params, resources);
		initialize();
	}

	public void initialize() {
		if (params.containsKey(NUM_SUBNETWORKS))
			numSubnetworks = Integer.valueOf(params.getProperty(NUM_SUBNETWORKS));
	}
	
	public Object clone(Engine engine) {
		RecursiveNetworkPeer clone = (RecursiveNetworkPeer) super.clone(engine);
		clone.name = null;
		clone.known = false;
		clone.visited = false;
		clone.routingTable = new HashMap<String,String>();
		clone.routeHistogram = new HashMap<String,Integer>();
		clone.storedRouteHistogram = new HashMap<String,Integer>();
		return clone;
	}
	
	public int getNumSubnetworks() {
		return numSubnetworks;
	}
	
	public int getNumNeighbors(String subnetworkName) {
		int num = 0;
		for (int i = 0; i < this.getNeighbors().size(); i++) {
			RecursiveNetworkPeer neighbor = (RecursiveNetworkPeer) this.getNeighbors().get(i);
			String neighborSubnetworkName = "NET" + neighbor.getSubnetworkNumber();
			if (neighborSubnetworkName.equals(subnetworkName))
				num++;
		}
		//System.out.println("getNumNeighbors in " + subnetworkName + ": " + num);
		return num;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSubnetworkNumber() {
		//System.out.println(name);
		if (name != null)
			return name.substring(0, name.indexOf("."));
		else
			return null;
	}
	
	public boolean isKnown() {
		return known;
	}

	public void setKnown(boolean known) {
		this.known = known;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public void storeRoutingTableEntry(String destination, String route) {
		this.routingTable.put(destination, route);
	}
	
	public String getRoute(String destination) {
		return this.routingTable.get(destination);
	}
	
	public HashMap<String,String> getRoutingTable() {
		return this.routingTable;
	}
	
	public ArrayList<String> getRouteForSubnetwork(String subnetworkName) {
		ArrayList<String> route = new ArrayList<String>();
		String nextHop = subnetworkName;
		route.add(nextHop);
		do {
			String temp = this.getRoute(nextHop);
			if (temp != null) {
				nextHop = temp;
				route.add(nextHop);
			}
			else
				break;
			//System.out.println("nextHop = " + nextHop);
		} while ( (nextHop.contains("NET") && !nextHop.equals("NET" + this.getSubnetworkNumber())) );
		return route;
	}
	
	public String getLongestRouteEntry() {
		/*
		for (String dest: this.getRoutingTable().keySet()) {
			String route = this.getRoutingTable().get(dest);
			System.out.println("dest = " + dest + ", route = " + route);
		}
		*/
		String name = null;
		int maxLength = 0;
		for (int i = 1; i < numSubnetworks; i++) {
			String key = "NET"+i;
			String tempName = routingTable.get(key);
			//System.out.println(tempName);
			int tempLength = 0;
			while ( (tempName != null) && (!tempName.contains(".")) ) {
				tempLength++;
				tempName = routingTable.get(tempName);
				//System.out.println("while: tempName = " + tempName);
				if ( (tempName != null) && (!tempName.equals("NET"+this.getSubnetworkNumber())) && (tempLength > maxLength) ) { 
					maxLength = tempLength;
					name = tempName;
				}
			}		
		}
		//System.out.println("name = " + name);
		return name;
	}
	
	public int getNumRoutingOperations() {
		return numRoutingOperations;
	}

	public void setNumRoutingOperations(int numRoutingOperations) {
		this.numRoutingOperations = numRoutingOperations;
	}
	
	public int getTotalHopCount() {
		return totalHopCount;
	}

	public void setTotalHopCount(int totalHopCount) {
		this.totalHopCount = totalHopCount;
	}

	public int getNumSuccessfulRoutingOperations() {
		return numSuccessfulRoutingOperations;
	}

	public void setNumSuccessfulRoutingOperations(int numSuccessfulRoutingOperations) {
		this.numSuccessfulRoutingOperations = numSuccessfulRoutingOperations;
	}

	public int getNumExplorationMessages() {
		return numExplorationMessages;
	}

	public void setNumExplorationMessages(int numExplorationMessages) {
		this.numExplorationMessages = numExplorationMessages;
	}
	
	public double getPercVisitedPeers() {
		int numPeers = this.getEngine().getNodes().size();
		double numVisitedPeers = 0;
		RecursiveNetworkPeer currentNode = null;
		for (Iterator<Node> it = this.getEngine().getNodes().iterator(); it.hasNext();) {
			currentNode = (RecursiveNetworkPeer) it.next();
			if (currentNode.isVisited())
				numVisitedPeers++;
		}
		return (double)numVisitedPeers/numPeers;
	}
	
	
	public double getCurrentInformation() {
		double totalDest = (double)this.getRoutingTable().size();
		//System.out.println("total routes = " + totalDest + " " + (double)this.getCurrentRouteHistogram().size());
		if (this.getCurrentRouteHistogram().size() < 2)
			return 0;
		double Imax = - (Math.log(1.0/((double)this.getCurrentRouteHistogram().size())) / Math.log(2));
		//System.out.println("Imax = " + Imax);
		double I = 0;
		for (String route: this.getCurrentRouteHistogram().keySet()) {
			double p = ((double)this.getCurrentRouteHistogram().get(route))/totalDest;			
			I += - ( p * Math.log(p) / Math.log(2));
		}
		I = I/Imax;
		//System.out.println("I = " + I);
		return I;
	}
	
	public double getStoredInformation() {
		return storedInformation;
	}
	
	public void storeInformation(double information) {
		storedInformation = information;
	}
	
	public HashMap<String,Integer> getCurrentRouteHistogram() {
		routeHistogram = new HashMap<String,Integer>();
		for (String dest: this.routingTable.keySet()) {
			String route = this.routingTable.get(dest);
			if (routeHistogram.containsKey(route))
				routeHistogram.put(route, routeHistogram.get(route)+1);
			else
				routeHistogram.put(route, 1);
			//System.out.println("dest = " + dest + ", route = " + route);
		}
		return routeHistogram;
	}
	
	public HashMap<String,Integer> getStoredRouteHistogram() {
		return storedRouteHistogram;
	}
	
	public void storeRouteHistogram(HashMap<String,Integer> routeHistogram) {
		storedRouteHistogram = routeHistogram;
	}
	
	public double getInformationHammingDistance() {
		double hammingDistance = 0;
		for (int i=0; i < this.getNeighbors().size(); i++) {
			RecursiveNetworkPeer neighbor = (RecursiveNetworkPeer) this.getNeighbors().get(i);
			if (routeHistogram.containsKey(neighbor.getName()) && !storedRouteHistogram.containsKey(neighbor.getName()))
				hammingDistance += 1;
			else if (!routeHistogram.containsKey(neighbor.getName()) && storedRouteHistogram.containsKey(neighbor.getName()))
				hammingDistance += 1;
			else if (routeHistogram.containsKey(neighbor.getName()) && storedRouteHistogram.containsKey(neighbor.getName()))
				if (routeHistogram.get(neighbor.getName()) != storedRouteHistogram.get(neighbor.getName()))
					hammingDistance += 1;
		}
		return hammingDistance/(double)this.getNeighbors().size();
	}
	
	
	public void configureRoutingTable(int maxRTSize, boolean thirdLevel) {
		
		this.routingTable.clear();
			
			if (this.getName().equals("6.1"))
				System.out.println(">>>> peer " + this.getName());
			String sn = this.getSubnetworkNumber();
			
			// FIRST LEVEL: NEIGHBORS
			for (int j = 0; j < this.getNeighbors().size(); j++) {
				RecursiveNetworkPeer neighbor = (RecursiveNetworkPeer) this.getNeighbors().get(j);
				//System.out.println("neighbor " + neighbor.getName());
				String nsn = neighbor.getSubnetworkNumber();
				//System.out.println("nsn = " + nsn);
				//System.out.println("route to nsn = " + this.getRoute("NET"+nsn));
				if (!nsn.equals(sn) && (!this.routingTable.containsKey("NET"+nsn)))
					if (this.routingTable.size() < maxRTSize)
						this.storeRoutingTableEntry("NET"+nsn, neighbor.getName());
				//System.out.println("route to nsn = " + peer.getRoute("NET"+nsn));			
			}
			
			// SECOND LEVEL: NEIGHBORS OF NEIGHBORS
			// for each neighbor, explore its neighbors to find new subnetworks
			for (int j = 0; j < this.getNeighbors().size(); j++) {
				RecursiveNetworkPeer neighbor = (RecursiveNetworkPeer) this.getNeighbors().get(j);
				//System.out.println("neighbor " + neighbor.getName());
				String nsn = neighbor.getSubnetworkNumber();
				for (int m = 0; m < neighbor.getNeighbors().size(); m++) {
					RecursiveNetworkPeer neighborOfNeighbor = (RecursiveNetworkPeer) neighbor.getNeighbors().get(m);
					if (!neighborOfNeighbor.getName().equals(this.getName())) {
						String nnsn = neighborOfNeighbor.getSubnetworkNumber();
						if (!nnsn.equals(sn) && !nnsn.equals(nsn) && (!this.routingTable.containsKey("NET"+nnsn))) {
							if (this.routingTable.size() < maxRTSize)
								this.storeRoutingTableEntry("NET"+nnsn, "NET"+nsn);
						}
						if (nnsn.equals(sn) && !this.getNeighbors().contains(neighborOfNeighbor) && !this.routingTable.containsKey(neighborOfNeighbor.getName())) {
							if (this.routingTable.size() < maxRTSize)
								this.storeRoutingTableEntry(neighborOfNeighbor.getName(), neighbor.getName());
								//this.getRoutingTable().put(neighborOfNeighbor.getName(), neighbor.getName());
						}
					
						
						// THIRD LEVEL: NEIGHBORS OF NEIGHBORS OF NEIGHBORS
						if (thirdLevel) {
							for (int k = 0; k < neighborOfNeighbor.getNeighbors().size(); k++) {
								RecursiveNetworkPeer neighborOfNeighborOfNeighbor = (RecursiveNetworkPeer) neighborOfNeighbor.getNeighbors().get(k);
								if (!neighborOfNeighborOfNeighbor.getName().equals(this.getName())) {
									String nnnsn = neighborOfNeighborOfNeighbor.getSubnetworkNumber();
									if (!nnnsn.equals(sn) && !nnnsn.equals(nsn) && !nnnsn.equals(nnsn) && (!this.routingTable.containsKey("NET"+nnnsn))) {
										if (this.routingTable.size() < maxRTSize)
											this.storeRoutingTableEntry("NET"+nnnsn, "NET"+nnsn);
									}
								}
							}
						}
					}
				}
			}
			
			if (this.getName().equals("6.1")) {
			System.out.println("RT size = " + this.getRoutingTable().size());
			
			for (String dest: this.getRoutingTable().keySet()) {
				String route = this.getRoutingTable().get(dest);
				System.out.println("dest = " + dest + ", route = " + route);
			}
			}
	}
}
