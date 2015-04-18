package it.unipr.ce.dsg.deus.mobility.event;

import java.util.Properties;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.RunException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.mobility.node.MobilePeer;

/**
 * 
 * @author Marco Picone (picone@ce.unipr.it)
 *
 */
public class MovePeerEvent extends Event {
	
	public MovePeerEvent(String id, Properties params,
			Process parentProcess)
			throws InvalidParamsException {
		
		super(id, params, parentProcess);
	
	}

	public Object clone() {
		MovePeerEvent clone = (MovePeerEvent) super.clone();
		return clone;
	}

	public void run() throws RunException {
		MobilePeer currentNode = (MobilePeer) getAssociatedNode();		
		currentNode.move(triggeringTime);
	}

}
