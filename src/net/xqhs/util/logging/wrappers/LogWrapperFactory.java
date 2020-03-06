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

import net.xqhs.util.logging.logging.LogWrapper;
import net.xqhs.util.logging.logging.LogWrapper.LoggerType;

/**
 * Replaces dynamic class loading with a factory method.
 * 
 * @author andreiolaru
 */
public class LogWrapperFactory
{
	/**
	 * Returns a new instance of a {@link LogWrapper}-implementing class.
	 * 
	 * @param loggerType - the desired wrapper type.
	 * @param logName - the name for the new log.
	 * @return a fresh {@link LogWrapper} instance.
	 */
	public static LogWrapper getLogWrapper(LoggerType loggerType, String logName)
	{
		switch(loggerType)
		{
		case CONSOLE:
			return new ConsoleWrapper(logName);
		case JAVA:
			return new JavaLogWrapper(logName);
		case GLOBAL:
			return new GlobalLogWrapper(logName);
		default:
			throw new UnsupportedOperationException("wrapper type " + loggerType + " not supported.");
		}
	}
}
