package it.unipr.ce.dsg.deus.automator;

import it.unipr.ce.dsg.deus.core.Deus;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <p>
 * This class manages logs for the Automator.
 * </p>
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Marco Pigoni
 * 
 */
public class AutomatorLogger {
	
	private FileOutputStream file;
	
	/**
	 * constructor of the AutomatorLogger
	 * @param 
	 */
	public AutomatorLogger() {
		super();			
		 
		try {				
			this.file = new FileOutputStream(Deus.simulationLogName,true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that writes on the logger file
	 * @param f, time of the simulator
	 * @param fileValue, set of couples <name,values> to write on the file 
	 */
	public void write(float f, ArrayList<LoggerObject> fileValue){				
		
		String write = "";
		
		String vt = "VT=" + f + "\n";
		
		String toWrite = "";
		
		for(int i = 0; i < fileValue.size(); i++)
			toWrite = toWrite + fileValue.get(i).getDataName() + "=" + fileValue.get(i).getDataValue() + "\n";
		
		write = vt + toWrite;
		
		try {
			this.file.write(write.getBytes());
			this.file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}	
	
}
