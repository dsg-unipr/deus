package it.unipr.ce.dsg.deus.p2p.event;

import it.unipr.ce.dsg.deus.automator.AutomatorLogger;
import it.unipr.ce.dsg.deus.automator.LoggerObject;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;
import it.unipr.ce.dsg.deus.p2p.node.Peer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

/**
 * This class represents a logger that works out on Peer nodes. It calculates
 * the node degree distribution for each peer of the network. The results is a
 * list of degree starting from 1 up to the maximum degree inside the netowkr.
 * For each degree is computed the number of nodes that has it.
 * 
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class LogNodeDegreeEvent extends Event {

	private static final String K_MAX = "kMax";
    	int kMax = 100;
    
	public LogNodeDegreeEvent(String id, Properties params, Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		initialize();
		if (params.containsKey(K_MAX))
			kMax = Integer.parseInt(params.getProperty(K_MAX));
	}

	public void initialize() throws InvalidParamsException {
	}

	public void run() throws RunException {
	    AutomatorLogger a = new AutomatorLogger();
	    ArrayList<LoggerObject> fileValue = new ArrayList<LoggerObject>();
	    
	    int kValues[] = new int[kMax+1];
	    for (int i = 0; i < kMax+1; i++)
	            kValues[i] = 0;
	    
	    for (Iterator<Node> it = Engine.getDefault().getNodes().iterator(); it.hasNext();) {
	            Node n = it.next();
	            if (!(n instanceof Peer))
	                    continue;
	            kValues[((Peer) n).getNeighbors().size()]++;
	    }
	
	    for (int i = 0; i < kValues.length; i++)
	            fileValue.add(new LoggerObject(""+i+"", kValues[i]));
	    
	    a.write(Engine.getDefault().getVirtualTime(), fileValue);
	}
}
