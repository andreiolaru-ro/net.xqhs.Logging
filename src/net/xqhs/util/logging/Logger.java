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

/**
 * Any class that offers logging services to an external entity should implement this interface.
 * <p>
 * This interface extends the functionality described by {@link LoggerSimple}.
 * <p>
 * This interface should also allow classes like {@link UnitExt} and {@link UnitComponentExt} to expose only logging
 * functions, without any configuration facilities.
 * <p>
 * Implementing classes should offer three sets of methods:
 * <ul>
 * <li>Classic: <code>error</code>, <code>warn</code>, <code>info</code>, <code>trace</code> - also available with
 * {@link Object} parameters;
 * <li>Short: <code>le, lw, li, lf</code>;
 * <li>Special: <code>lr</code>, to be used in return statements, and <code>dbg</code>. See {@link LoggerSimple}.
 * </ul>
 * 
 * @author Andrei Olaru
 */
public interface Logger extends LoggerSimple
{
	/**
	 * Relay for {@link #le(String, Object...)}.
	 * 
	 * @param message : see the relayed method.
	 * @param arguments : see the relayed method.
	 */
	public void error(String message, Object... arguments);
	
	/**
	 * Relay for {@link #lw(String, Object...)}.
	 * 
	 * @param message : see the relayed method.
	 * @param arguments : see the relayed method.
	 */
	public void warn(String message, Object... arguments);
	
	/**
	 * Relay for {@link #li(String, Object...)}.
	 * 
	 * @param message : see the relayed method.
	 * @param arguments : see the relayed method.
	 */
	public void info(String message, Object... arguments);
	
	/**
	 * Relay for {@link #lf(String, Object...)}.
	 * 
	 * @param message : see the relayed method.
	 * @param arguments : see the relayed method.
	 */
	public void trace(String message, Object... arguments);
}
