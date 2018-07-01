package it.unipr.ce.dsg.deus.example.recursivenetworks;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

public class RecursiveNetworkRoutingEvent extends Event {

	private float delay = 1;
	private int hopCount = 0;
	private RecursiveNetworkPeer destination = null;
	private static final String MAX = "max";
	private int max = 20;
	private static final String ADVANCED = "advanced";
	private boolean advanced = false;
	private static final String NOHR = "nohr";
	private boolean nohr = false;
	private ArrayList<String> currentRoute = null;

	private String previousHopName = null;
	
	public RecursiveNetworkRoutingEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		initialize();
	}
	
	public void initialize() {
		if (params.containsKey(MAX))
			max = Integer.valueOf(params.getProperty(MAX));
		if (params.containsKey(ADVANCED))
			advanced = Boolean.valueOf(params.getProperty(ADVANCED));
		if (params.containsKey(NOHR))
			nohr = Boolean.parseBoolean(params.getProperty(NOHR));
		currentRoute = new ArrayList<String>();
	}

	public Object clone() {
		RecursiveNetworkRoutingEvent clone = (RecursiveNetworkRoutingEvent) super.clone();
		clone.destination = null;
		clone.hopCount = 0;
		return clone;
	}
	
	public int getHopCount() {
		return hopCount;
	}

	public void setHopCount(int hopCount) {
		this.hopCount = hopCount;
	}
	
	@Override
	public void run() throws RunException {
		
		//System.out.println("---------------------------------");
		
		RecursiveNetworkPeer logger = ((RecursiveNetworkPeer)this.getEngine().getNodes().get(0));
		
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
		//System.out.println("peer = " + peer.getName());
		if (peer.getName() == null)
			return;
		/*
		for (String dest: peer.getRoutingTable().keySet()) {
			String route = peer.getRoutingTable().get(dest);
			System.out.println("dest = " + dest + ", route = " + route);
		}
		*/
		if (destination == null) {
			hopCount = 0;
			do {
				destination = (RecursiveNetworkPeer) this.getEngine()
						.getNodes().get(
								this.getEngine().getSimulationRandom()
										.nextInt(
												this.getEngine().getNodes()
														.size()));
			} while (destination.getName().equals(peer.getName()));
			logger.setNumRoutingOperations(logger.getNumRoutingOperations()+1);
			//System.out.println("destination = " + destination.getName());
		}
		
		if (peer.getName().equals(destination.getName())) {
			logger.setTotalHopCount(logger.getTotalHopCount() + 1);
			logger.setNumSuccessfulRoutingOperations(logger.getNumSuccessfulRoutingOperations()+1);
			
			// create new routing event
			RecursiveNetworkRoutingEvent routingEvent = (RecursiveNetworkRoutingEvent) this.getEngine().createEvent(RecursiveNetworkRoutingEvent.class,
							triggeringTime + delay);
			routingEvent.setAssociatedNode(null);
			routingEvent.setHopCount(0);
			routingEvent.setDestination(null);
			this.getEngine().insertIntoEventsList(routingEvent);
			
			return;
		}
		
		if (nohr) {
			if (this.hopCount < max) {
				// se tra i neighbor di peer c' destination, incrementa il contatore di salti e quello di successi, e fai partire nuova ricerca
				for (int i = 0; i < peer.getNeighbors().size(); i++) {
					RecursiveNetworkPeer neighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(i); 
					//System.out.println("Neighbor " + neighbor.getName());
					if (neighbor.getName().equals(destination.getName())) {
						logger.setTotalHopCount(logger.getTotalHopCount() + this.hopCount + 1);
						logger.setNumSuccessfulRoutingOperations(logger.getNumSuccessfulRoutingOperations()+1);
						
						// create new routing event
						RecursiveNetworkRoutingEvent routingEvent = (RecursiveNetworkRoutingEvent) this.getEngine().createEvent(RecursiveNetworkRoutingEvent.class,
										triggeringTime + delay);
						routingEvent.setAssociatedNode(null);
						routingEvent.setHopCount(0);
						routingEvent.setDestination(null);
						this.getEngine().insertIntoEventsList(routingEvent);
						
						return;
					}	
				}
				// se destination  in routing table come key allora per raggiungerlo bastano 2 hop
				if (peer.getRoutingTable().containsKey(destination.getName())) {
					logger.setTotalHopCount(logger.getTotalHopCount() + this.hopCount + 2);
					logger.setNumSuccessfulRoutingOperations(logger.getNumSuccessfulRoutingOperations()+1);
					
					// create new routing event
					RecursiveNetworkRoutingEvent routingEvent = (RecursiveNetworkRoutingEvent) this.getEngine().createEvent(RecursiveNetworkRoutingEvent.class,
									triggeringTime + delay);
					routingEvent.setAssociatedNode(null);
					routingEvent.setHopCount(0);
					routingEvent.setDestination(null);
					this.getEngine().insertIntoEventsList(routingEvent);
					
					return;
				}
				
				// altrimenti prendi una strada a caso
				Random random = this.getEngine().getSimulationRandom();
				RecursiveNetworkPeer targetNeighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(random.nextInt(peer.getNeighbors().size()));
				RecursiveNetworkRoutingEvent routingEvent = (RecursiveNetworkRoutingEvent) this.getEngine().createEvent(
								RecursiveNetworkRoutingEvent.class,
								triggeringTime + delay);
				//System.out.println("targetNeighbor " + targetNeighbor.getName());
				routingEvent.setAssociatedNode(targetNeighbor);
				routingEvent.previousHopName = peer.getName();
				routingEvent.setHopCount(this.hopCount + 1);
				//System.out.println("dest = " + destination.getName());
				routingEvent.setDestination(destination);
				this.getEngine().insertIntoEventsList(routingEvent);
				return;
			}
			return;
		}
		
		/*
		System.out.println("destination = " + destination.getName());
		System.out.println("hopCount = " + this.hopCount);
		System.out.println("max = " + max);
		*/
		
		if (this.hopCount < max) {
			
			// se tra i neighbor di peer c' destination, incrementa il contatore di salti e quello di successi, e fai partire nuova ricerca
			for (int i = 0; i < peer.getNeighbors().size(); i++) {
				RecursiveNetworkPeer neighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(i); 
				//System.out.println("Neighbor " + neighbor.getName());
				if (neighbor.getName().equals(destination.getName())) {
					logger.setTotalHopCount(logger.getTotalHopCount() + this.hopCount + 1);
					logger.setNumSuccessfulRoutingOperations(logger.getNumSuccessfulRoutingOperations()+1);
					
					// create new routing event
					RecursiveNetworkRoutingEvent routingEvent = (RecursiveNetworkRoutingEvent) this.getEngine().createEvent(RecursiveNetworkRoutingEvent.class,
									triggeringTime + delay);
					routingEvent.setAssociatedNode(null);
					routingEvent.setHopCount(0);
					routingEvent.setDestination(null);
					this.getEngine().insertIntoEventsList(routingEvent);
					
					return;
				}	
			}
			// se destination  in routing table come key allora per raggiungerlo bastano 2 hop
			if (peer.getRoutingTable().containsKey(destination.getName())) {
				logger.setTotalHopCount(logger.getTotalHopCount() + this.hopCount + 2);
				logger.setNumSuccessfulRoutingOperations(logger.getNumSuccessfulRoutingOperations()+1);
				
				// create new routing event
				RecursiveNetworkRoutingEvent routingEvent = (RecursiveNetworkRoutingEvent) this.getEngine().createEvent(RecursiveNetworkRoutingEvent.class,
								triggeringTime + delay);
				routingEvent.setAssociatedNode(null);
				routingEvent.setHopCount(0);
				routingEvent.setDestination(null);
				this.getEngine().insertIntoEventsList(routingEvent);
				
				return;
			}
			
			Random random = this.getEngine().getSimulationRandom(); 
			
			// prendi la sn di destination
			String destSn = "NET" + destination.getSubnetworkNumber();
			
			// se advanced sfrutto il currentRoute
			if (advanced) {
				if (currentRoute.size() > 0) {
					ArrayList<String> alternativeRoute = peer.getRouteForSubnetwork(destSn);
					if ((alternativeRoute.size() > 0) && (alternativeRoute.size() < currentRoute.size())) 
						currentRoute = alternativeRoute;
					String nextHop = currentRoute.get(currentRoute.size()-1);
					/*
					for (int i=0; i< currentRoute.size(); i++)
						System.out.println("route: " + currentRoute.get(i));
					*/
					RecursiveNetworkPeer targetNeighbor = null;
					if (nextHop.contains("NET")) {
						ArrayList<String> tempRoute = peer.getRouteForSubnetwork(nextHop);
						nextHop = tempRoute.get(tempRoute.size()-1);
						//System.out.println("new nextHop = " + nextHop);
						if (nextHop.equals("NET" + peer.getSubnetworkNumber())) {
							if (peer.getNumNeighbors("NET" + peer.getSubnetworkNumber()) == 0) {
								//System.out.println("1");
								do {
									targetNeighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(random.nextInt(peer.getNeighbors().size()));
								} while (!peer.getRoutingTable().containsValue(targetNeighbor.getName()));
							}
							else if (peer.getNumNeighbors("NET" + peer.getSubnetworkNumber()) == 1) {
								//System.out.println("2");
								do {
									targetNeighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(random.nextInt(peer.getNeighbors().size()));
								} while (!targetNeighbor.getSubnetworkNumber().equals(peer.getSubnetworkNumber()));
										//|| (!peer.getRoutingTable().containsValue(targetNeighbor.getName())));
							}
							else {
								//System.out.println("3");
								do {
									targetNeighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(random.nextInt(peer.getNeighbors().size()));
								} while ((targetNeighbor.getName().equals(this.previousHopName)) 
										|| (!targetNeighbor.getSubnetworkNumber().equals(peer.getSubnetworkNumber())));
										//|| (!peer.getRoutingTable().containsValue(targetNeighbor.getName())));
							}	
						}
						else {	
							for (int i = 0; i < peer.getNeighbors().size(); i++) {
								RecursiveNetworkPeer neighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(i); 
								if (neighbor.getName().equals(nextHop))
									targetNeighbor = neighbor;
							}
						}
					}
					else {	
						for (int i = 0; i < peer.getNeighbors().size(); i++) {
							RecursiveNetworkPeer neighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(i); 
							if (neighbor.getName().equals(nextHop))
								targetNeighbor = neighbor;
						}
						
					}
					if (!nextHop.equals("NET" + peer.getSubnetworkNumber()))
						currentRoute.remove(currentRoute.size()-1);
					RecursiveNetworkRoutingEvent routingEvent = (RecursiveNetworkRoutingEvent) this.getEngine().createEvent(RecursiveNetworkRoutingEvent.class,
									triggeringTime + delay);
					//System.out.println("targetNeighbor " + targetNeighbor.getName());
					routingEvent.setAssociatedNode(targetNeighbor);
					routingEvent.previousHopName = peer.getName();
					routingEvent.setHopCount(this.hopCount+1);
					//System.out.println("dest = " + destination.getName());
					routingEvent.setDestination(destination);
					routingEvent.setRoute(currentRoute);
					this.getEngine().insertIntoEventsList(routingEvent);
					return;
				}
			}
			
			if (destination.getSubnetworkNumber().equals(peer.getSubnetworkNumber())) { 
				//System.out.println("- #1 -");
				// se tra i neighbor di peer non c' destination, invia il messaggio a un neighbor di peer che  nella stessa sn, 
				// evitando il previous hop (a meno che non sia l'unica strada);
				// incrementa il contatore di salti;
				RecursiveNetworkPeer targetNeighbor = null;
				if (peer.getNeighbors().size() == 1) 
					targetNeighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(0);
				else {
					if (peer.getNumNeighbors("NET" + peer.getSubnetworkNumber()) == 0) {
						do {
							targetNeighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(random.nextInt(peer.getNeighbors().size()));
						} while (!peer.getRoutingTable().containsValue(targetNeighbor.getName()));
					}
				
					else if (peer.getNumNeighbors("NET" + peer.getSubnetworkNumber()) == 1) {
						do {
							targetNeighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(random.nextInt(peer.getNeighbors().size()));
							//System.out.println("A targetNeighbor.getSubnetworkNumber() = " + targetNeighbor.getSubnetworkNumber());
						} while (!targetNeighbor.getSubnetworkNumber().equals(peer.getSubnetworkNumber()));
					}
					else {
						do {
							targetNeighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(random.nextInt(peer.getNeighbors().size()));
							//System.out.println("B targetNeighbor.getSubnetworkNumber() = " + targetNeighbor.getSubnetworkNumber());
						} while ((targetNeighbor.getName().equals(this.previousHopName)) 
								|| (!targetNeighbor.getSubnetworkNumber().equals(peer.getSubnetworkNumber())));
					}
				}
				RecursiveNetworkRoutingEvent routingEvent = (RecursiveNetworkRoutingEvent) this.getEngine().createEvent(
								RecursiveNetworkRoutingEvent.class,
								triggeringTime + delay);
				//System.out.println("targetNeighbor " + targetNeighbor.getName());
				routingEvent.setAssociatedNode(targetNeighbor);
				routingEvent.previousHopName = peer.getName();
				routingEvent.setHopCount(this.hopCount + 1);
				//System.out.println("dest = " + destination.getName());
				routingEvent.setDestination(destination);
				this.getEngine().insertIntoEventsList(routingEvent);
				return;	
			}
			else {
				// se destSn  nella tabella di routing di peer, invia il messaggio al neighbor che permette di arrivare a destSn pi velocemente;
				if (peer.getRoutingTable().containsKey(destSn)) {
					//System.out.println("- #2 -");
					/*
					for (String dest: peer.getRoutingTable().keySet()) {
						String route = peer.getRoutingTable().get(dest);
						System.out.println("dest = " + dest + ", route = " + route);
					}
					*/
					ArrayList<String> route = peer.getRouteForSubnetwork(destSn);
					String nextHop = route.get(route.size()-1);
					RecursiveNetworkPeer targetNeighbor = null;
					if (nextHop.equals("NET" + peer.getSubnetworkNumber())) {
						if (peer.getNumNeighbors("NET" + peer.getSubnetworkNumber()) == 0) {
							//System.out.println("1");
							do {
								targetNeighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(random.nextInt(peer.getNeighbors().size()));
							} while (!peer.getRoutingTable().containsValue(targetNeighbor.getName()));
						}
						else if (peer.getNumNeighbors("NET" + peer.getSubnetworkNumber()) == 1) {
							//System.out.println("2");
							do {
								targetNeighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(random.nextInt(peer.getNeighbors().size()));
							} while (!targetNeighbor.getSubnetworkNumber().equals(peer.getSubnetworkNumber()));
									//|| (!peer.getRoutingTable().containsValue(targetNeighbor.getName())));
						}
						else {
							//System.out.println("3");
							do {
								targetNeighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(random.nextInt(peer.getNeighbors().size()));
							} while ((targetNeighbor.getName().equals(this.previousHopName)) 
									|| (!targetNeighbor.getSubnetworkNumber().equals(peer.getSubnetworkNumber())));
									//|| (!peer.getRoutingTable().containsValue(targetNeighbor.getName())));
						}	
					}
					else {	
						for (int i = 0; i < peer.getNeighbors().size(); i++) {
							RecursiveNetworkPeer neighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(i); 
							if (neighbor.getName().equals(nextHop))
								targetNeighbor = neighbor;
						}
					}
					route.remove(route.size()-1);
					RecursiveNetworkRoutingEvent routingEvent = (RecursiveNetworkRoutingEvent) this.getEngine().createEvent(RecursiveNetworkRoutingEvent.class,
									triggeringTime + delay);
					//System.out.println("targetNeighbor " + targetNeighbor.getName());
					routingEvent.setAssociatedNode(targetNeighbor);
					routingEvent.previousHopName = peer.getName();
					routingEvent.setHopCount(this.hopCount+1);
					//System.out.println("dest = " + destination.getName());
					routingEvent.setDestination(destination);
					if (advanced)
						routingEvent.setRoute(route);
					this.getEngine().insertIntoEventsList(routingEvent);
					return;
				}
				else { // select random neighbor (possibly avoiding previousHop)
					//System.out.println("- #3 -");
					RecursiveNetworkRoutingEvent routingEvent = (RecursiveNetworkRoutingEvent) this.getEngine().createEvent(RecursiveNetworkRoutingEvent.class,
									triggeringTime + delay);
					RecursiveNetworkPeer targetNeighbor = null;
					if (peer.getNeighbors().size() == 1) 
						targetNeighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(0);
					else {
						do {
							targetNeighbor = (RecursiveNetworkPeer) peer.getNeighbors().get(random.nextInt(peer.getNeighbors().size()));
						} while (targetNeighbor.getName().equals(this.previousHopName));
								//|| (!peer.getRoutingTable().containsValue(targetNeighbor.getName())));
					}
					//System.out.println("targetNeighbor " + targetNeighbor.getName());
					routingEvent.setAssociatedNode(targetNeighbor);
					routingEvent.previousHopName = peer.getName();
					routingEvent.setHopCount(this.hopCount+1);
					//System.out.println("dest = " + destination.getName());
					routingEvent.setDestination(destination);
					this.getEngine().insertIntoEventsList(routingEvent);
					return;
				}
			}
		}
		else { // this.hopCount == MAX
			//System.out.println("- #4 -");
			logger.setTotalHopCount(logger.getTotalHopCount() + this.hopCount);
			
			// create new routing event
			RecursiveNetworkRoutingEvent routingEvent = (RecursiveNetworkRoutingEvent) this.getEngine().createEvent(RecursiveNetworkRoutingEvent.class,
							triggeringTime + delay);
			routingEvent.setAssociatedNode(null);
			routingEvent.setHopCount(0);
			routingEvent.setDestination(null);
			this.getEngine().insertIntoEventsList(routingEvent);
			
			return;
		}
	}

	public RecursiveNetworkPeer getDestination() {
		return destination;
	}

	public void setDestination(RecursiveNetworkPeer destination) {
		this.destination = destination;
	}
	
	public void setRoute(ArrayList<String> route) {
		this.currentRoute = route;
	}

}
