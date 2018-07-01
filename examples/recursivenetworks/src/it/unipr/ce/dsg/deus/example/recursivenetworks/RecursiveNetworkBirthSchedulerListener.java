package it.unipr.ce.dsg.deus.example.recursivenetworks;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.SchedulerListener;
import it.unipr.ce.dsg.deus.impl.event.BirthEvent;
import it.unipr.ce.dsg.deus.p2p.event.MultipleRandomConnectionsEvent;
import it.unipr.ce.dsg.deus.p2p.event.ExpTopologyConnectionEvent;

public class RecursiveNetworkBirthSchedulerListener implements SchedulerListener {

	public void newEventScheduled(Event parentEvent, Event newEvent) {
		BirthEvent be = (BirthEvent) parentEvent; 
		if (newEvent instanceof MultipleRandomConnectionsEvent) {
			((MultipleRandomConnectionsEvent) newEvent).setAssociatedNode((RecursiveNetworkPeer) be.getAssociatedNode());
		}
		else if (newEvent instanceof ExpTopologyConnectionEvent) {
			((ExpTopologyConnectionEvent) newEvent).setAssociatedNode((RecursiveNetworkPeer) be.getAssociatedNode());
		}
	}


}
