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

/**
 * Any class that offer logging services to an external entity should implement this interface.
 * <p>
 * This interface should also allow classes like {@link Unit} and {@link UnitComponent} to expose only logging
 * functions, without any configuration facilities.
 * <p>
 * It works with a reduced set of levels (see {@link Level}): trace, info, warning, error, plus <code>OFF</code> and
 * <code>ALL</code> for log settings.
 * <p>
 * Implementing classes should offer three sets of methods:
 * <ul>
 * <li>Classic: <code>error</code>, <code>warn</code>, <code>info</code>, <code>trace</code> - also available with
 * {@link Object} parameters;
 * <li>Short: <code>le lw li lf</code>, <code>l(Level, String)</code>;
 * <li>Special: <code>lr</code>, to be used in return statements, and <code>dbg</code>. See the documentation of the
 * methods.
 * </ul>
 * 
 * @author Andrei Olaru
 * 
 */
public interface Logger
{
	/**
	 * Indicates the level of the log. Mimics {@link org.apache.log4j.Level}.
	 * 
	 * @author Andrei Olaru
	 * 
	 */
	public enum Level {
		
		OFF,
		
		ERROR,
		
		WARN,
		
		INFO,
		
		TRACE,
		
		ALL,
	}

	public void error(String message, Object... objects);
	
	public void warn(String message, Object... objects);
	
	public void info(String message, Object... objects);
	
	public void trace(String message, Object... objects);
	
	public void error(String message);
	
	public void warn(String message);
	
	public void info(String message);
	
	public void trace(String message);
	
	public void le(String message);
	
	public void lw(String message);
	
	public void li(String message);
	
	public void lf(String message);
	
	public Object lr(Object ret);
	
	/**
	 * This should be used in return statements. It adds a log message just before returning the {@link Object} in the
	 * argument.
	 * <p>
	 * The {@link Object} in the argument is also put in the log message.
	 * 
	 * @param ret
	 *            : the {@link Object} to return and to display.
	 * @param message
	 *            : the message to display beside the {@link Object}.
	 * 
	 * @return the {@link Object} passed as argument.
	 */
	public Object lr(Object ret, String message);
	
	/**
	 * This displays a log message (with the level <code>TRACE</code>) only if the specified {@link DebugItem} is
	 * activated.
	 * 
	 * @param debug
	 *            : the {@link DebugItem}
	 * @param message
	 *            : the log message
	 */
	public void dbg(DebugItem debug, String message);
	
	public void doExit();
}
