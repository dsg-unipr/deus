package it.unipr.ce.dsg.deus.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Simple log formatter that establish whats going to be written into log files.
 * To reduce the overhead of information only the log message will be printed.
 * 
 * @author Matteo Agosti (agosti@ce.unipr.it)
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class LogEntryFormatter extends Formatter {

	public String format(LogRecord record) {
		return record.getMessage() + "\r\n";
	}

}
