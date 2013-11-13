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

import net.xqhs.util.logging.Log;
import net.xqhs.util.logging.Logger.Level;

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
