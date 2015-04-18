package it.unipr.ce.dsg.deus.impl.event;

import java.util.ArrayList;
import java.util.Properties;

import it.unipr.ce.dsg.deus.automator.AutomatorLogger;
import it.unipr.ce.dsg.deus.automator.LoggerObject;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * This class represents a logging event. In particular, the population size
 * (number of living simulation nodes) will be printed.
 * 
 * @author Matteo Agosti (agosti@ce.unipr.it)
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class LogPopulationSizeEvent extends Event {

	public LogPopulationSizeEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
	}

	public void run() throws RunException {
		
		//AutomatorLogger a = new AutomatorLogger("./temp/logger");
		AutomatorLogger a = new AutomatorLogger();
		ArrayList<LoggerObject> fileValue = new ArrayList<LoggerObject>();
		
		fileValue.add(new LoggerObject("N", (double) Engine.getDefault().getNodes().size()));
		
		a.write(Engine.getDefault().getVirtualTime(), fileValue);
		
		/*
		getLogger().info(
				"## Network size: "
						+ Integer.toString(Engine.getDefault().getNodes()
								.size()));
		*/
	}

}
