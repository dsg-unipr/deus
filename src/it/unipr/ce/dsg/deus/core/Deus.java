package it.unipr.ce.dsg.deus.core;

import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;


/**
 * 
 * This is the main class of the simulation environment.
 * 
 * @author Matteo Agosti 
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class Deus {
	
	public static String simulationLogName = null;
	
	private AutomatorParser automator;
	
	public Deus(String fileName, String logFileName) {
		super();
		simulationLogName = logFileName;
		//AutomatorParser automator;
		try {
			this.automator = new AutomatorParser(fileName);
			try {
				this.automator.getEngine().run();
			} catch (SimulationException e) {
				e.printStackTrace();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}		
	}

	public static void main(String args[]) {
		if (args.length != 1) {
			System.out.println("Usage: java " + Deus.class.getCanonicalName()
					+ " configfile.xml");
			return;
		}

		try {
			AutomatorParser automator = new AutomatorParser(args[0]);
			automator.getEngine().run();
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SimulationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

	}

	public AutomatorParser getAutomator() {
		return automator;
	}
	
}
