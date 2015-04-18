package it.unipr.ce.dsg.deus.example.basic;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.SchedulerListener;
import it.unipr.ce.dsg.deus.impl.event.DeathEvent;
import it.unipr.ce.dsg.deus.p2p.event.DisconnectionEvent;

/**
 * <p>
 * This class is used to initialize the events associated to DisconnectionEvent.
 * </p>
 * 
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 *
 */
public class DisconnectionSchedulerListener implements SchedulerListener {

	public void newEventScheduled(Event parentEvent, Event newEvent) {
		DisconnectionEvent de = (DisconnectionEvent) parentEvent; 
		if (newEvent instanceof DeathEvent) {
			((DeathEvent) newEvent).setNodeToKill(de.getAssociatedNode());
		} 
	}

}
