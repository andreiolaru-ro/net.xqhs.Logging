package net.xqhs.util.logging;


import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;



public class Log4JWrapper extends Log
{
	protected Logger	theLog	= null;
	
	public Log4JWrapper(String name)
	{
		theLog = Logger.getLogger(name);
	}
	
	@Override
	public void setLevel(Level level)
	{
		theLog.setLevel(org.apache.log4j.Level.toLevel(level.toString()));
	}
	
	@Override
	public void addDestination(String format, OutputStream destination)
	{
		theLog.addAppender(new WriterAppender(new PatternLayout(format), destination));		
	}

	protected static org.apache.log4j.Level toWrapedLevel(Level level)
	{
		switch(level)
		{
		case ALL:
			return org.apache.log4j.Level.ALL;
		case ERROR:
			return org.apache.log4j.Level.ERROR;
		case INFO:
			return org.apache.log4j.Level.INFO;
		case OFF:
			return org.apache.log4j.Level.OFF;
		case TRACE:
			return org.apache.log4j.Level.TRACE;
		case WARN:
			return org.apache.log4j.Level.WARN;
		default: // should be unreachable
			return org.apache.log4j.Level.INFO;
		}
	}
	@Override
	public void l(Level level, String message)
	{
		theLog.log(toWrapedLevel(level), message);
	}
	
}
