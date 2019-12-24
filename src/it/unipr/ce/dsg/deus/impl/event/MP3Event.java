package it.unipr.ce.dsg.deus.impl.event;

import java.util.Properties;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

import javazoom.jl.player.Player;


/**
 * This class implements an event that play a MP3 file
 * 
 * @author  Michele Amoretti (michele.amoretti@unipr.it)
 */
public class MP3Event extends Event {
	private static final String FILE_NAME = "fileName";
	private static final String MODALITY = "modality";
	private String fileName;
	private String modality;
    private Player player; 
    private BufferedInputStream bis = null;
	
	public MP3Event(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
		if (params.containsKey(FILE_NAME))
			fileName = params.getProperty(FILE_NAME);
		if (params.containsKey(MODALITY))
			modality = params.getProperty(MODALITY);
	}

	public void run() throws RunException {
		if (modality.equals("loop"))
			this.playLoop() ;
		else if (modality.equals("once"))
			this.playOnce();
	}
	
    public boolean isComplete() {
    	return player.isComplete();
    }
    
    public void close() { 
    	if (player != null) 
    		player.close(); 
    }

    public void playOnce() {
        // run in new thread to play in background
        new Thread() {
            public void run() {
                try { 
                	FileInputStream fis = new FileInputStream(fileName);
                    bis = new BufferedInputStream(fis);
                    player = new Player(bis);
                	player.play(); 
                }
                catch (Exception e) { System.out.println(e); }           
            }
        }.start();
    }
    
    public void playLoop() {
        // run in new thread to play in background
        new Thread() {
            public void run() {
            	while (true) {
            		try { 
            			FileInputStream fis = new FileInputStream(fileName);
                        bis = new BufferedInputStream(fis);
                        player = new Player(bis);
            			player.play(); 
            			player.close();
            		}
            		catch (Exception e) { System.out.println(e); }
            	}
            }
        }.start();
       
    }

}
