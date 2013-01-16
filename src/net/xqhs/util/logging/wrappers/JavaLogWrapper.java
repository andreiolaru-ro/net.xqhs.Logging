package net.xqhs.util.logging.wrappers;

import java.io.OutputStream;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import net.xqhs.util.logging.Log;
import net.xqhs.util.logging.Log.Level;

/**
 * A {@link Log} wrapper of Java's {@link Logger}.
 * 
 * @author Andrei Olaru
 * 
 * @see Log
 * 
 */

public class JavaLogWrapper extends Log
{
	
	protected java.util.logging.Logger	theLog	= null;
	
	public JavaLogWrapper(String name)
	{
		theLog = java.util.logging.Logger.getLogger(name);
	}
	
	protected static java.util.logging.Level toWrapedLevel(Level level)
	{
		switch(level)
		{
		case ALL:
			return java.util.logging.Level.ALL;
		case ERROR:
			return java.util.logging.Level.SEVERE;
		case INFO:
			return java.util.logging.Level.INFO;
		case OFF:
			return java.util.logging.Level.OFF;
		case TRACE:
			return java.util.logging.Level.FINE;
		case WARN:
			return java.util.logging.Level.WARNING;
		default: // should be unreachable
			return java.util.logging.Level.INFO;
		}
	}
	
	@Override
	public void setLevel(Level level)
	{
		theLog.setLevel(toWrapedLevel(level));
	}
	
	@Override
	public void addDestination(String format, OutputStream destination)
	{
		theLog.addHandler(new StreamHandler(destination, new SimpleFormatter()));
	}
	
	@Override
	public void l(Level level, String message)
	{
		theLog.log(toWrapedLevel(level), message);
	}
}
