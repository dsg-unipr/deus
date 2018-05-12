package it.unipr.ce.dsg.deus.automator;

import it.unipr.ce.dsg.deus.automator.gui.SimulationSummaryFrame;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.JProgressBar;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Marco Pigoni
 * 
 * @author Stefano Sebastio (stefano.sebastio@imtlucca.it) [only Runner refactoring]
 * 
 */
public class RunnerGui extends Runner {

	private JProgressBar simulationProgressBar = null;
	private boolean multithreading = false; /*@author Mirco Rosa (mirco.rosa.91@gmail.com) [multithreading]*/
	
	
	public RunnerGui(String originalXml, String automatorXml) {
		super(originalXml, automatorXml);

	}

	/**
	 * This method starts Deus
	 * 
	 * @param originalXml
	 *            , name of the base configuration file
	 * @param automatorXml
	 *            , name of the file that automates the simulations
	 * @throws ParserConfigurationException
	 */
	public int start(String originalXml, String automatorXml)
			throws DeusAutomatorException, JAXBException, SAXException,
			IOException, ParserConfigurationException {

		int result = super.start(originalXml, automatorXml);
		
		String summary = new String(cbuf);

		// Execute the GUI showing the summary of the simulation
		SimulationSummaryFrame simulationsummary = new SimulationSummaryFrame(
				this);
		simulationsummary.setMultithreading(multithreading);
		simulationsummary.getSimulationSummaryTextArea().setText(summary);
		simulationsummary.setVisible(true);

		return result;
	}

	/**
	 * 
	 */
	public void runSimulations(boolean multithreading) {
		if (simulationProgressBar != null) {
			simulationProgressBar.setMaximum(files.size());
			simulationProgressBar.setMinimum(0);
		}
		
		simulationProgressBar.setValue(0);
		
		NumFileListener listener = new NumFileListener();
		addPropertyChangeListener(listener);
		
		super.runSimulations(multithreading);

	}
	
	public JProgressBar getSimulationProgressBar() {
		return simulationProgressBar;
	}

	public void setSimulationProgressBar(JProgressBar simulationProgressBar) {
		this.simulationProgressBar = simulationProgressBar;
	}

	private class NumFileListener implements PropertyChangeListener {
	    @Override
	    public void propertyChange(PropertyChangeEvent event) {
	    	//System.out.println("property has changed");
	        if (event.getPropertyName().equals("NumFileProperty")) {
	        	//System.err.println("AAA !" );
	            //System.out.println(event.getNewValue().toString());
//	            simulationProgressBar.setValue(numFile);  //Modified by Mirco Rosa (mirco.rosa.91@gmail.com)
		        simulationProgressBar.setValue((int)event.getNewValue());
	        }
	    }
	}

	/*@author Mirco Rosa (mirco.rosa.91@gmail.com) [multithreading]*/
	public void setMultithreading(boolean multithreading) {
		this.multithreading = multithreading;
	}
}