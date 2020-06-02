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

import net.xqhs.util.logging.LoggerSimple.Level;
import net.xqhs.util.logging.logging.LogWrapper;

/**
 * A {@link LogWrapper} implementation wrapping Apache's Log4J logger.
 * 
 * @author Andrei Olaru
 */
public class Log4JWrapper extends LogWrapper
{
	/**
	 * Creates a new instance.
	 * 
	 * @param name
	 *            - the name of the log.
	 */
	public Log4JWrapper(String name)
	{
		throw new UnsupportedOperationException("This log type is disabled.");
	}
	
	@Override
	public void setLevel(Level level)
	{
		throw new UnsupportedOperationException("This log type is disabled.");
	}
	
	@Override
	public void addDestination(String format, OutputStream destination)
	{
		throw new UnsupportedOperationException("This log type is disabled.");
	}
	
	@Override
	public void addDestination(int format, OutputStream destination)
	{
		throw new UnsupportedOperationException("This log type is disabled.");
	}
	
	@Override
	public void l(Level level, String message)
	{
		throw new UnsupportedOperationException("This log type is disabled.");
	}
	
	@Override
	public void exit()
	{
		// nothing to do.
	}
	
}
