package it.unipr.ce.dsg.deus.example.basic;

import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import it.unipr.ce.dsg.deus.core.AutomatorParser;
import it.unipr.ce.dsg.deus.core.Engine;

public class EngineCreator {

	public static void main(String[] args) {
		AutomatorParser automator;
		try {
			automator = new AutomatorParser("examples/basic/SingleSpeedGrowthExample.xml");
			Engine engine = automator.getEngine();
			System.out.println("current seed = " + engine.getCurrentSeed());
			engine.startNewSimulator(12345);
			System.out.println("current seed = " + engine.getCurrentSeed());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
