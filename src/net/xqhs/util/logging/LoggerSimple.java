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

import net.xqhs.util.logging.Debug.DebugItem;

/**
 * Any class that offers simple logging services (lf-li-lw-le-lr-dbg) to an external entity should implement this
 * interface.
 * <p>
 * As with other logging infrastructures, the name of the primitive used indicates the level of importance of the
 * message.
 * <p>
 * All logging primitives (except the single-argument {@link #lr(Object)}) accept a message as argument, as well as a
 * number of {@link Object} arguments that will be placed, in the order in which they were passed, in the placeholders
 * in the message. The placeholder string is specified by the constant {@link #ARGUMENT_PLACEHOLDER} (currently
 * {@value #ARGUMENT_PLACEHOLDER}). The result of call to the <code>toString()</code> method of the argument is placed
 * between {@link #ARGUMENT_BEGIN} and {@link #ARGUMENT_END}.
 * <p>
 * The performance of an implementation is strongly influenced by when string concatenation is performed. The role of
 * the <code>arguments</code> argument in the logging primitives is to delay string concatenation (components of the
 * message string and the various objects to place in it). Concatenation should only happen if the message is going to
 * be displayed. While the level of the message will be relayed to the wrapped logger (depending on wrapper), messages
 * should also be filtered in the class implementing this interface, such that the message string is only assembled if
 * necessary.
 * <p>
 * It works with a reduced set of levels: {@link Level#TRACE}, {@link Level#INFO}, {@link Level#WARN},
 * {@link Level#ERROR}, plus {@link Level#OFF} and {@link Level#ALL} for log settings.
 * 
 * @author Andrei Olaru
 * 
 */
public interface LoggerSimple
{
	/**
	 * Indicates the level of the log. Mimics the level in Log4j.
	 * <p>
	 * When calling the wrapped logger, the level will be mapped to a value supported by the wrapped logger, depending
	 * on the wrapper implementation.
	 * 
	 * @author Andrei Olaru
	 * 
	 */
	public enum Level {
		
		/**
		 * Setting for the level of the logger specifying no logging messages should be shown.
		 */
		OFF(10),
		
		/**
		 * Error messages.
		 */
		ERROR(8),
		
		/**
		 * Warning messages.
		 */
		WARN(7),
		
		/**
		 * Information messages.
		 */
		INFO(5),
		
		/**
		 * Finer, tracing messages.
		 */
		TRACE(1),
		
		/**
		 * Setting for the level of the logger specifying all logging messages should be shown.
		 */
		ALL(0),
		
		;
		
		/**
		 * The priority of the level. The higher, the most important.
		 */
		int priority;
		
		/**
		 * Creates a new level, with a set priority.
		 * 
		 * @param levelPriority
		 *            - the priority (the higher, the most important).
		 */
		private Level(int levelPriority)
		{
			priority = levelPriority;
		}
		
		/**
		 * Returns <code>true</code> if a message with this level should be displayed if the level of the log is
		 * <code>logLevel</code>.
		 * 
		 * @param logLevel
		 *            - the level of the log.
		 * @return <code>true</code> if the message should be displayed, <code>false</code>otherwise.
		 */
		public boolean displayWith(Level logLevel)
		{
			return (logLevel != null) ? (priority >= logLevel.priority) : false;
		}
	}
	
	/**
	 * The substring in the message string that will be replaced with the various specified arguments. This must be a
	 * regular expression.
	 */
	public static final String	ARGUMENT_PLACEHOLDER	= "\\[\\]";
	
	/**
	 * The string to be added before the rendition of the argument in the message string.
	 */
	public static final String	ARGUMENT_BEGIN			= "[";
	
	/**
	 * The string to be added after the rendition of the argument in the message string.
	 */
	public static final String	ARGUMENT_END			= "]";
	
	/**
	 * Outputs an error message.
	 * 
	 * @param message
	 *            : the message string. Apparitions of the {@link #ARGUMENT_PLACEHOLDER} will be replaced with instances
	 *            in <code>arguments</code>, in the order in which they appear, by calling their
	 *            {@link Object#toString()} method.
	 * @param arguments
	 *            : the arguments to be placed in the placeholders of the message string. Remaining arguments will be
	 *            added after the message.
	 */
	public void le(String message, Object... arguments);
	
	/**
	 * Outputs a warning message.
	 * 
	 * @param message
	 *            : the message string. Apparitions of the {@link #ARGUMENT_PLACEHOLDER} will be replaced with instances
	 *            in <code>arguments</code>, in the order in which they appear, by calling their
	 *            {@link Object#toString()} method.
	 * @param arguments
	 *            : the arguments to be placed in the placeholders of the message string. Remaining arguments will be
	 *            added after the message.
	 */
	public void lw(String message, Object... arguments);
	
	/**
	 * Outputs an info message.
	 * 
	 * @param message
	 *            : the message string. Apparitions of the {@link #ARGUMENT_PLACEHOLDER} will be replaced with instances
	 *            in <code>arguments</code>, in the order in which they appear, by calling their
	 *            {@link Object#toString()} method.
	 * @param arguments
	 *            : the arguments to be placed in the placeholders of the message string. Remaining arguments will be
	 *            added after the message.
	 */
	public void li(String message, Object... arguments);
	
	/**
	 * Outputs a 'fine' logging message.
	 * 
	 * @param message
	 *            : the message string. Apparitions of the {@link #ARGUMENT_PLACEHOLDER} will be replaced with instances
	 *            in <code>arguments</code>, in the order in which they appear, by calling their
	 *            {@link Object#toString()} method.
	 * @param arguments
	 *            : the arguments to be placed in the placeholders of the message string. Remaining arguments will be
	 *            added after the message.
	 */
	public void lf(String message, Object... arguments);
	
	/**
	 * Outputs a fine logging message just before returning the {@link Object} in the argument.
	 * <p>
	 * This method is supposed to be used in return statements, e.g. <code>return lr(value);</code>
	 * <p>
	 * The message will contain the {@link Object} in the argument.
	 * 
	 * @param ret
	 *            : the {@link Object} to return and to display.
	 * @return the {@link Object} passed as argument.
	 */
	public Object lr(Object ret);
	
	/**
	 * Outputs a fine logging message just before returning the {@link Object} in the argument.
	 * <p>
	 * This method is supposed to be used in return statements, e.g. <code>return lr(value, message);</code>
	 * <p>
	 * The {@link Object} in the argument is also put in the log message, in a form decided by the implementation.
	 * 
	 * @param ret
	 *            : the {@link Object} to return and to display.
	 * @param message
	 *            : the message to display beside the {@link Object}. Apparitions of the {@link #ARGUMENT_PLACEHOLDER}
	 *            will be replaced with instances in <code>arguments</code>, in the order in which they appear, by
	 *            calling their {@link Object#toString()} method.
	 * @param arguments
	 *            : the arguments to be placed in the placeholders of the message string. Remaining arguments will be
	 *            added after the message.
	 * @return the {@link Object} passed as argument.
	 */
	public Object lr(Object ret, String message, Object... arguments);
	
	/**
	 * This method should be used in return statements for error cases. It adds an error log message just before
	 * returning the value in the first argument.
	 * 
	 * @param ret
	 *            : the value to return.
	 * @param message
	 *            : the message to display. {@link Level#ERROR} will be used. Apparitions of the
	 *            {@link #ARGUMENT_PLACEHOLDER} will be replaced with instances in <code>arguments</code>, in the order
	 *            in which they appear, by calling their {@link Object#toString()} method.
	 * @param arguments
	 *            : the arguments to be placed in the placeholders of the message string. Remaining arguments will be
	 *            added after the message.
	 * @return the {@link Object} passed as argument.
	 */
	public boolean ler(boolean ret, String message, Object... arguments);
	
	/**
	 * Outputs a fine logging message only if the specified {@link DebugItem} is activated.
	 * 
	 * @param debug
	 *            : the {@link DebugItem}
	 * @param message
	 *            : the message string. Apparitions of the {@link #ARGUMENT_PLACEHOLDER} will be replaced with instances
	 *            in <code>arguments</code>, in the order in which they appear, by calling their
	 *            {@link Object#toString()} method.
	 * @param arguments
	 *            : the arguments to be placed in the placeholders of the message string. Remaining arguments will be
	 *            added after the message.
	 */
	public void dbg(DebugItem debug, String message, Object... arguments);
}
