/*******************************************************************************
 * Copyright (C) 2018 Andrei Olaru.
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
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import net.xqhs.util.logging.LoggerSimple.Level;
import net.xqhs.util.logging.logging.LogWrapper;

/**
 * A {@link LogWrapper} wrapper of Java's {@link java.util.logging.Logger}.
 * 
 * @author Andrei Olaru
 * 
 * @see LogWrapper
 * 
 */

public class JavaLogWrapper extends LogWrapper
{
	/**
	 * The underlying logger.
	 */
	protected java.util.logging.Logger	theLog	= null;
	
	/**
	 * The set of handlers added to the log.
	 */
	@SuppressWarnings("unused") // type arguments needed for Java 1.6.
	protected Set<StreamHandler> handlers = new HashSet<StreamHandler>();
	
	/**
	 * Creates a new instance.
	 * 
	 * @param name - the name of the log.
	 */
	public JavaLogWrapper(String name)
	{
		theLog = java.util.logging.Logger.getLogger(name);
	}
	
	/**
	 * Converts {@link Level} to {@link java.util.logging.Level}.
	 * 
	 * @param level - the level to convert.
	 * @return - the resulting level.
	 */
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
		StreamHandler handler = new StreamHandler(destination, new SimpleFormatter());
		handlers.add(handler);
		theLog.addHandler(handler);
	}
	
	@Override
	public void addDestination(int format, OutputStream destination)
	{
		StreamHandler handler = new StreamHandler(destination, new SimpleFormatter());
		handlers.add(handler);
		theLog.addHandler(handler);
	}

	@Override
	public void l(Level level, String message)
	{
		theLog.log(toWrapedLevel(level), message);
	}
	
	@Override
	public void exit()
	{
		theLog.setLevel(java.util.logging.Level.OFF);
		for(StreamHandler handler : handlers)
			theLog.removeHandler(handler);
	}
}
