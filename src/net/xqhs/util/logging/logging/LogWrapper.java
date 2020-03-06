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
package net.xqhs.util.logging.logging;

import net.xqhs.util.logging.LoggerSimple.Level;
import net.xqhs.util.logging.wrappers.ConsoleWrapper;
import net.xqhs.util.logging.wrappers.GlobalLogWrapper;
import net.xqhs.util.logging.wrappers.JavaLogWrapper;
import net.xqhs.util.logging.wrappers.Log4JWrapper;

import java.io.OutputStream;

/**
 * Use this abstract class to implement any [wrapper of a] logging structure that is returned by {@link Logging}.
 * <p>
 * All types of wrappers available in the project should be stated in {@link LoggerType}.
 * <p>
 * It should work with a reduced set of levels (see {@link Level}).
 * 
 * @author Andrei Olaru
 */
public abstract class LogWrapper
{
	/**
	 * Logger types included with this library. Wrappers for these will extend the {@link LogWrapper} class.
	 * 
	 * @author Andrei Olaru
	 */
	public static enum LoggerType {
		
		/**
		 * The implementation of a simple wrapper that outputs to the system console.
		 */
		CONSOLE(ConsoleWrapper.class.getName()),
		
		/**
		 * The Log4J wrapper implementation.
		 */
		LOG4J(Log4JWrapper.class.getName()),
		
		/**
		 * The Java wrapper implementation.
		 */
		JAVA(JavaLogWrapper.class.getName()),

		/**
		 * The Global wrapper implementation.
		 */
		GLOBAL(GlobalLogWrapper.class.getName()),
		
		/**
		 * The implementation is of another type than the ones in the enumeration.
		 */
		OTHER(null),
		
		;
		
		/**
		 * The name of the wrapper class.
		 */
		String	className;
		
		/**
		 * Default constructor.
		 * 
		 * @param className
		 *            - the name of the class of the wrapper.
		 */
		private LoggerType(String className)
		{
			this.className = className;
		}
		
		/**
		 * @return the name of the class of the wrapper.
		 */
		public String getClassName()
		{
			return className;
		}
	}
	
	/**
	 * Sets the level of the underlying log to a level that corresponds to the given instance of {@link Level},
	 * according to the implementation.
	 * 
	 * @param level
	 *            - the desired log level.
	 */
	public abstract void setLevel(Level level);
	
	/**
	 * Instructs the underlying log to add a destination for its output. Currently meant for use with Log4J.
	 * 
	 * @param format
	 *            - a pattern, in a format that is potentially characteristic to the wrapper.
	 * @param destination
	 *            - a destination stream.
	 */
	protected abstract void addDestination(String format, OutputStream destination);
	
	/**
	 * The logging function to override in the implementation of the class.
	 * 
	 * @param level
	 *            - the {@link Level} of the message, that will be translated to an equivalent level of the underlying
	 *            log.
	 * @param message
	 *            - the logging message.
	 */
	public abstract void l(Level level, String message);

	/**
	 * Instructs the underlying infrastructure to clear any information and actions related to this log.
	 */
	public abstract void exit();
	
}
