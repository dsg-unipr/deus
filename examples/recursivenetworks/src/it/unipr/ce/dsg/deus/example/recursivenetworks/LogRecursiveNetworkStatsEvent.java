package it.unipr.ce.dsg.deus.example.recursivenetworks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import it.unipr.ce.dsg.deus.automator.AutomatorLogger;
import it.unipr.ce.dsg.deus.automator.LoggerObject;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

public class LogRecursiveNetworkStatsEvent extends Event {

	public LogRecursiveNetworkStatsEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		initialize();
	}

	public void initialize() throws InvalidParamsException { }

	@Override
	public void run() throws RunException {
		
		RecursiveNetworkPeer logger = ((RecursiveNetworkPeer)this.getEngine().getNodes().get(0));
		
		AutomatorLogger a = new AutomatorLogger(engine.getLogFileName());
		ArrayList<LoggerObject> fileValue = new ArrayList<LoggerObject>();
		
		RecursiveNetworkPeer currentNode = null;
		//int numPeers = this.getEngine().getNodes().size();
		//System.out.println("Log numPeers: " + numPeers);
		double numKnownPeers = 0;
		double numVisitedPeers = 0;
		
		int numPeers = this.getEngine().getNodes().size();
		
		double totalInformation = 0;
		double totalE = 0;
		double totalS = 0;
		double totalC = 0;
		double totalH = 0;
		for (Iterator<Node> it = this.getEngine().getNodes().iterator(); it.hasNext();) {
			currentNode = (RecursiveNetworkPeer) it.next();
			if (currentNode.isKnown())
				numKnownPeers++;
			if (currentNode.isVisited())
				numVisitedPeers++;
			
			//IT analysis
			double Iin = currentNode.getStoredInformation();
			//System.out.println("Iin = " + Iin);
			double Iout = currentNode.getCurrentInformation();
			//System.out.println("Iout = " + Iout);
			double E = Iout/Iin;
			double S = Iin - Iout;
			double C = 4*E*S;
			double H = 1 - currentNode.getInformationHammingDistance();
			
			totalInformation += Iout;
			totalE += E;
			totalS += S;
			totalC += C;
			totalH += H;
		}
		
		fileValue.add(new LoggerObject("I-avg", totalInformation/(double)numPeers));
		fileValue.add(new LoggerObject("E-avg", totalE/(double)numPeers));
		fileValue.add(new LoggerObject("S-avg", totalS/(double)numPeers));
		fileValue.add(new LoggerObject("C-avg", totalC/(double)numPeers));
		fileValue.add(new LoggerObject("H-avg", totalH/(double)numPeers));
		
		fileValue.add(new LoggerObject("perc-known-peers", (double)numKnownPeers/numPeers));
		fileValue.add(new LoggerObject("perc-visited-peers", (double)numVisitedPeers/numPeers));
		fileValue.add(new LoggerObject("numRoutingOperations", logger.getNumRoutingOperations()));
		fileValue.add(new LoggerObject("numSuccessfulRoutingOperations", logger.getNumSuccessfulRoutingOperations()));
		fileValue.add(new LoggerObject("perc-successful-routing-operations", (double)logger.getNumSuccessfulRoutingOperations()/logger.getNumRoutingOperations()));
		fileValue.add(new LoggerObject("avg-route-length",(double)logger.getTotalHopCount()/logger.getNumRoutingOperations()));
		fileValue.add(new LoggerObject("num-exploration-messages", (double)logger.getNumExplorationMessages()));
		a.write(this.getEngine().getVirtualTime(), fileValue);
	}

}
