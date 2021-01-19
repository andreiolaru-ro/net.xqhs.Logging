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
package net.xqhs.util.logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xqhs.util.logging.Logger.Level;
import net.xqhs.util.logging.output.LogOutput;
import net.xqhs.util.logging.output.StreamLogOutput;
import net.xqhs.util.logging.output.StringLogOutput;
import net.xqhs.util.logging.wrappers.ModernLogWrapper;

/**
 * Use this abstract class to implement any [wrapper of a] logging structure used in a {@link Unit}.
 * <p>
 * All types of wrappers available in the project should be stated in {@link LoggerType}.
 * <p>
 * This class implements a basic management of name, level, highlighting, and also outputs and their associated
 * {@link OutputStream}s. If the output is a {@link StringLogOutput}, a {@link ByteArrayOutputStream} will be created;
 * if the output is a {@link StreamLogOutput}, the stream must be managed by the {@link StreamLogOutput} instance.
 * <p>
 * Extending this class requires at least implementing the {@link #l(Level, String)} method.
 * 
 * @author Andrei Olaru
 */
public abstract class LogWrapper {
	/**
	 * Logger types included with this library. Wrappers for these will extend the {@link LogWrapper} class.
	 * 
	 * @author Andrei Olaru
	 */
	public static enum LoggerType {
		
		/**
		 * The Modern log wrapper implementation.
		 */
		MODERN(ModernLogWrapper.class.getName()),
		
		/**
		 * The implementation is of another type than the ones in the enumeration.
		 */
		OTHER(null),
		
		;
		
		/**
		 * The name of the wrapper class.
		 */
		String className;
		
		/**
		 * Default constructor.
		 * 
		 * @param className
		 *            - the name of the class of the wrapper.
		 */
		private LoggerType(String className) {
			this.className = className;
		}
		
		/**
		 * @return the name of the class of the wrapper.
		 */
		public String getClassName() {
			return className;
		}
	}
	
	/**
	 * The name for the log.
	 */
	protected String						name			= null;
	/**
	 * The current level for the log.
	 */
	protected Level							currentLevel	= Level.ERROR;
	/**
	 * The name for the log.
	 */
	protected boolean						highlight		= false;
	/**
	 * Additional output streams, besides the globalLogStream.
	 */
	protected Map<LogOutput, OutputStream>	logOutputs		= new HashMap<>();
	/**
	 * Default format for output streams, if no other format is specified.
	 */
	protected static int					defaultFormat	= Logger.INCLUDE_NAME;
	
	/**
	 * Sets the level of the underlying log to a level that corresponds to the given instance of {@link Level},
	 * according to the implementation.
	 * 
	 * @param level
	 *            - the desired log level.
	 */
	public void setLevel(Level level) {
		currentLevel = level;
	}
	
	/**
	 * Set whether the log is highlighted.
	 * 
	 * @param isHighlighted
	 *            - <code>true</code> if highlighted, <code>false</code> otherwise.
	 */
	public void setHighlighted(boolean isHighlighted) {
		highlight = isHighlighted;
	}
	
	/**
	 * Adds a log destination, as defined by a {@link LogOutput} instance. If the <code>logOutput</code> is an instance
	 * of {@link StreamLogOutput}, the stream specified by the logOutput will be used; otherwise, a new
	 * {@link ByteArrayOutputStream} is created.
	 * 
	 * @param logOutput
	 *            - a log output.
	 */
	@SuppressWarnings("resource")
	public void addOutput(LogOutput logOutput) {
		logOutputs.put(logOutput, logOutput instanceof StreamLogOutput ? ((StreamLogOutput) logOutput).getOutputStream()
				: new ByteArrayOutputStream());
	}
	
	/**
	 * Remove a log destination.
	 * 
	 * @param logOutput
	 *            - the output to remove.
	 */
	@SuppressWarnings("resource")
	public void removeOutput(LogOutput logOutput) {
		try {
			OutputStream stream = logOutputs.get(logOutput);
			if(stream != null) {
				stream.flush();
				if(logOutput instanceof StringLogOutput) {
					((StringLogOutput) logOutput).update(stream.toString());
					stream.close();
				}
				if(logOutput instanceof StreamLogOutput)
					((StreamLogOutput) logOutput).update();
			}
		} catch(IOException e) {
			System.out.println("[" + name + "] Log write error.");
		}
		logOutputs.remove(logOutput);
	}
	
	/**
	 * Removes all outputs used by the log.
	 */
	public void removeAllOutputs() {
		List<LogOutput> outputs = new LinkedList<>(logOutputs.keySet());
		for(LogOutput output : outputs)
			removeOutput(output);
	}
	
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
	public void exit() {
		removeAllOutputs();
	}
}
