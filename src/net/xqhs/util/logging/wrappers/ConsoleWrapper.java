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

import net.xqhs.util.logging.LoggerSimple.Level;
import net.xqhs.util.logging.logging.LogWrapper;

/**
 * Simple, basic {@link LogWrapper} implementation that uses the system console to output logging messages.
 * 
 * @author Andrei Olaru
 */
public class ConsoleWrapper extends LogWrapper
{
	/**
	 * The current level for the log.
	 */
	Level	currentLevel	= Level.ERROR;
	/**
	 * The name fo the log.
	 */
	String	name			= null;
	
	/**
	 * Creates a new console wrapper log, with the specified name.
	 * 
	 * @param logName - the name of the log to be created.
	 */
	public ConsoleWrapper(String logName)
	{
		name = logName;
	}
	
	@Override
	public void setLevel(Level level)
	{
		currentLevel = level;
	}
	
	@Override
	public void addDestination(String format, OutputStream destination)
	{
		// unsupported
		if(Level.ERROR.compareTo(currentLevel) >= 0)
			System.out.println("[" + Level.ERROR.toString() + "][" + name + "]: Alternate destinations not supported.");
	}
	
	@Override
	public void l(Level level, String message)
	{
		if(level.displayWith(currentLevel))
			System.out.println("[" + level.toString() + "][" + name + "]: " + message);
	}
	
	@Override
	public void exit()
	{
		// nothing to do
	}
}
