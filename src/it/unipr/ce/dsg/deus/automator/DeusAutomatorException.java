package it.unipr.ce.dsg.deus.automator;


/**
 * Exception that is thrown when the points that are necessary to draw a figure are not sufficient
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * @author Marco Pigoni
 *
 */
@SuppressWarnings("serial")
public class DeusAutomatorException extends Exception {

	String error = "";
	
	public DeusAutomatorException( String error ){
		this.error = error;
	}
	
	public String toString(){
		return (this.error);
	}
	
}
