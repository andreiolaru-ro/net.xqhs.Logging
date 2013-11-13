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
package net.xqhs.util.logging;

import java.io.OutputStream;

import net.xqhs.util.logging.Debug.DebugItem;
import net.xqhs.util.logging.Logger.Level;

/**
 * Use this abstract class to implement any [wrapper of a] logging structure that is returned by {@link Logging}.
 * <p>
 * All types of wrappers available in the project should be stated in {@link LoggerType}.
 * <p>
 * It should work with a reduced set of levels (see {@link Level}).
 * <p>
 * When extending this class, there is an absolute need that only the <code>l</code> method is implemented. The
 * implementations in this class all end up by calling <code>l</code>.
 * 
 * @author Andrei Olaru
 * 
 */
public abstract class Log
{
	/**
	 * Supported logger types. Wrappers for these will extend the {@link Log} class.
	 * 
	 * @author Andrei Olaru
	 * 
	 */
	public static enum LoggerType {
		LOG4J("tatami.pc.util.logging.Log4JWrapper"),
		
		JADE("tatami.core.util.logging.JadeLogWrapper"),
		
		JAVA("tatami.core.util.logging.JavaLogWrapper")
		
		;
		
		String	className;
		
		private LoggerType(String className)
		{
			this.className = className;
		}
		
		public String getClassName()
		{
			return className;
		}
	}
	
	protected Level	DEFAULT_LEVEL	= Level.INFO;
	
	public abstract void setLevel(Level level);
	
	/**
	 * @param format
	 *            : a pattern, in a format that is potentially characteristic to the wrapper
	 * @param destination
	 *            : a destination stream
	 */
	protected abstract void addDestination(String format, OutputStream destination);
	
	/**
	 * The logging function to override in the implementation of the class.
	 * 
	 * @param level
	 *            : the {@link Level} of the message.
	 * @param message
	 *            : the message.
	 */
	public abstract void l(Level level, String message);
	
}
