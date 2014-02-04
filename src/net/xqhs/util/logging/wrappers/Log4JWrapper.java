/*******************************************************************************
 * Copyright (C) 2013 Andrei Olaru.
 * 
 * This file is part of Logging.
 * 
 * Logging is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Logging is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Logging.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.xqhs.util.logging.wrappers;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import net.xqhs.util.logging.LoggerSimple.Level;
import net.xqhs.util.logging.logging.LogWrapper;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;

/**
 * A {@link LogWrapper} implementation wrapping Apache's Log4J logger.
 * 
 * @author Andrei Olaru
 */
public class Log4JWrapper extends LogWrapper
{
	/**
	 * The underlying logger.
	 */
	protected Logger		theLog		= null;
	
	/**
	 * Set of appenders for this log.
	 */
	@SuppressWarnings("unused")
	// type arguments required by Java 1.6
	protected Set<Appender>	appenders	= new HashSet<Appender>();
	
	/**
	 * Creates a new instance.
	 * 
	 * @param name
	 *            - the name of the log.
	 */
	public Log4JWrapper(String name)
	{
		theLog = Logger.getLogger(name);
	}
	
	@Override
	public void setLevel(Level level)
	{
		theLog.setLevel(toWrapedLevel(level));
	}
	
	@Override
	public void addDestination(String format, OutputStream destination)
	{
		Appender appender = new WriterAppender(new PatternLayout(format), destination);
		theLog.addAppender(appender);
		appenders.add(appender);
	}
	
	/**
	 * Converts {@link Level} to {@link org.apache.log4j.Level}.
	 * 
	 * @param level
	 *            - the level to convert.
	 * @return - the resulting level.
	 */
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
	
	@Override
	public void exit()
	{
		theLog.setLevel(org.apache.log4j.Level.OFF);
		for(Appender appender : appenders)
			theLog.removeAppender(appender);
	}
	
}
