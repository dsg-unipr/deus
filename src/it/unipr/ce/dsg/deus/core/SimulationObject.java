package it.unipr.ce.dsg.deus.core;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to abstract each simulation object, adding the member and methods
 * to support the logging process.
 * 
 * @author Matteo Agosti
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class SimulationObject {

	private String loggerPathPrefix = Engine.DEFAULT_LOGGER_PATH_PREFIX;

	private Level loggerLevel = Engine.DEFAULT_LOGGER_LEVEL;

	/**
	 * Returns an instance of the logger.
	 * 
	 * @return an instance of the logger.
	 */
	public Logger getLogger() {
		return Engine.getDefault().getLogger(this, loggerPathPrefix,
				loggerLevel);
	}

	/**
	 * Sets the logger base path in which store log files.
	 * 
	 * @param loggerPathPrefix
	 *            the logger base path in which store files.
	 */
	public void setLoggerPathPrefix(String loggerPathPrefix) {
		if (loggerPathPrefix == null)
			return;

		this.loggerPathPrefix = loggerPathPrefix;
	}

	/**
	 * Sets the logger level of logging. String value passed corresponds to the
	 * logging level of the default Java Logger.
	 * 
	 * @param loggerLevel
	 *            the string representing the logging level.
	 */
	public void setLoggerLevel(String loggerLevel) {
		if (loggerLevel == null)
			return;

		if (loggerLevel.equals("OFF"))
			this.loggerLevel = Level.OFF;
		else if (loggerLevel.equals("SEVERE"))
			this.loggerLevel = Level.SEVERE;
		else if (loggerLevel.equals("WARNING"))
			this.loggerLevel = Level.WARNING;
		else if (loggerLevel.equals("INFO"))
			this.loggerLevel = Level.INFO;
		else if (loggerLevel.equals("INFO"))
			this.loggerLevel = Level.INFO;
		else if (loggerLevel.equals("CONFIG"))
			this.loggerLevel = Level.CONFIG;
		else if (loggerLevel.equals("FINE"))
			this.loggerLevel = Level.FINE;
		else if (loggerLevel.equals("FINER"))
			this.loggerLevel = Level.FINER;
		else if (loggerLevel.equals("FINEST"))
			this.loggerLevel = Level.FINEST;
		else if (loggerLevel.equals("ALL"))
			this.loggerLevel = Level.ALL;
	}

}
