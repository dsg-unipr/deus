package it.unipr.ce.dsg.deus.example.recursivenetworks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;
import it.unipr.ce.dsg.deus.p2p.node.Peer;

public class RecursiveNetworkExplorationEvent extends Event {

	private float meanArrivalTriggeredWalk = 1;
	
	private static final String STRATEGY = "strategy";
	private String strategy = null; 
	private String previousHopName = null;
	private int ttl = 4;
	
	
	public RecursiveNetworkExplorationEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		initialize();
	}
	
	public void initialize() {
		if (params.containsKey(STRATEGY))
			strategy = params.getProperty(STRATEGY);
	}

	
	@Override
	public void run() throws RunException {
		
		//System.out.println("exploration event at VT = " + this.triggeringTime);
		
		RecursiveNetworkPeer logger = ((RecursiveNetworkPeer)this.getEngine().getNodes().get(0));
		
		if ((logger.getPercVisitedPeers() == 1.0) && (logger.flag == false)) {
			System.out.println(logger.getNumExplorationMessages());
			logger.flag = true;
		}
		
		logger.setNumExplorationMessages(logger.getNumExplorationMessages()+1);
		
		// associate this event with a node, if necessary
		RecursiveNetworkPeer peer = (RecursiveNetworkPeer) associatedNode;
		if (peer == null) {
			peer = (RecursiveNetworkPeer) this.getEngine()
					.getNodes().get(
							this.getEngine().getSimulationRandom()
									.nextInt(
											this.getEngine().getNodes()
													.size()));
		}
		
		/*
		int numVisited = 0;
		int numNotVisited = 0;
		for (int i = 1; i < this.getEngine().getNodes().size(); i++) {

			if (((RecursiveNetworkPeer) this.getEngine().getNodes().get(i))
					.isVisited())
				numVisited++;
			else
				numNotVisited++;
		}
		if (peer.isVisited())
			System.out.println("Step: " + logger.getNumExplorationMessages()
					+ " - V = " + numVisited + " - NV = " + numNotVisited);
		else
			System.out.println(">> Step: " + logger.getNumExplorationMessages()
					+ " - V = " + numVisited + " - NV = " + numNotVisited);
		*/
		
		peer.setKnown(true);
		peer.setVisited(true);
		
		// within the same network, HR-based exploration takes into account already visited nodes
		ArrayList<RecursiveNetworkPeer> neighborsSameNetNotVisited = new ArrayList<RecursiveNetworkPeer>();
		ArrayList<RecursiveNetworkPeer> neighborsOtherNet = new ArrayList<RecursiveNetworkPeer>();
		for (Iterator<Peer> it = peer.getNeighbors().iterator(); it.hasNext();) {
			RecursiveNetworkPeer currentNeighbor = (RecursiveNetworkPeer) it.next();
			currentNeighbor.setKnown(true);
			if (currentNeighbor.getSubnetworkNumber().equals(peer.getSubnetworkNumber()) && !currentNeighbor.isVisited())
				neighborsSameNetNotVisited.add(currentNeighbor);
			else
				neighborsOtherNet.add(currentNeighbor);
		}
		
		// create new exploration event and associate it to a neighbor, then schedule it
		RecursiveNetworkExplorationEvent explorationEvent = (RecursiveNetworkExplorationEvent) this.getEngine().createEvent(RecursiveNetworkExplorationEvent.class,
						triggeringTime + meanArrivalTriggeredWalk);
		
		Random random = this.getEngine().getSimulationRandom(); 
		RecursiveNetworkPeer targetPeer = null;
		if (peer.getNeighbors().size() > 0) {
			
			if (strategy.equals("random")) {
				targetPeer = (RecursiveNetworkPeer) peer.getNeighbors().get(random.nextInt(peer.getNeighbors().size()));
				explorationEvent.previousHopName = peer.getName();
				explorationEvent.setAssociatedNode(targetPeer);
				this.getEngine().insertIntoEventsList(explorationEvent);
			}
			else if (strategy.equals("flooding")) {
				for (int i = 0; i < peer.getNeighbors().size(); i++) {
					targetPeer = (RecursiveNetworkPeer) peer.getNeighbors().get(i);
					if (this.getTtl() > 0) {
						RecursiveNetworkExplorationEvent newExplorationEvent = (RecursiveNetworkExplorationEvent) this.getEngine().createEvent(RecursiveNetworkExplorationEvent.class,
										triggeringTime + meanArrivalTriggeredWalk);
						newExplorationEvent.setTtl(this.getTtl() - 1);
						newExplorationEvent.previousHopName = peer.getName();
						newExplorationEvent.setAssociatedNode(targetPeer);
						this.getEngine().insertIntoEventsList(newExplorationEvent);
					}
				}
			}
			else if (strategy.equals("hr")) {	
				if (peer.getNeighbors().size() == 1) {
					targetPeer = (RecursiveNetworkPeer) peer.getNeighbors().get(0);
				}
				else {		
					if (neighborsSameNetNotVisited.size() > 0) {
						targetPeer = neighborsSameNetNotVisited.get(random.nextInt(neighborsSameNetNotVisited.size()));
					}
					else if (neighborsSameNetNotVisited.size() == 0) { 
						
						if ((neighborsOtherNet.size() == 1) && (neighborsOtherNet.get(0).getName().equals(this.previousHopName))) {
							String name = peer.getLongestRouteEntry();
							for (int i=0; i < peer.getNeighbors().size(); i++) {
								if (((RecursiveNetworkPeer)peer.getNeighbors().get(i)).getName().equals(name))
									targetPeer = (RecursiveNetworkPeer) peer.getNeighbors().get(i);
							}	
						}
						else {
						
							do {
								targetPeer = neighborsOtherNet.get(random.nextInt(neighborsOtherNet.size()));
							} while (targetPeer.getName().equals(this.previousHopName));
						}
					}			
				}
				explorationEvent.previousHopName = peer.getName();
				explorationEvent.setAssociatedNode(targetPeer);
				this.getEngine().insertIntoEventsList(explorationEvent);
			}
		}
		else
			return;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
	
	public final static void clearConsole()
	{
	    try
	    {
	        final String os = System.getProperty("os.name");

	        if (os.contains("Windows"))
	        {
	            Runtime.getRuntime().exec("cls");
	        }
	        else
	        {
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (final Exception e)
	    {
	        //  Handle any exceptions.
	    }
	}

}
