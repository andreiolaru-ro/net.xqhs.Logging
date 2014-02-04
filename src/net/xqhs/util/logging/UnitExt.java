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

/**
 * This class extends {@link Unit} in order to provide all functionality required by the {@link Logger} interface.
 * <p>
 * The class does not implement the interface, as that would require for the methods to be public, but to policy of
 * {@link Unit} is to have logging methods protected.
 * 
 * @author Andrei Olaru
 */
public class UnitExt extends Unit
{
	/**
	 * Relay for {@link #le(String, Object...)}.
	 * 
	 * @param message : see the relayed method.
	 * @param arguments : see the relayed method.
	 */
	protected void error(String message, Object... arguments)
	{
		error(compose(message, arguments));
	}
	
	/**
	 * Relay for {@link #lw(String, Object...)}.
	 * 
	 * @param message : see the relayed method.
	 * @param arguments : see the relayed method.
	 */
	protected void warn(String message, Object... arguments)
	{
		warn(compose(message, arguments));
	}
	
	/**
	 * Relay for {@link #li(String, Object...)}.
	 * 
	 * @param message : see the relayed method.
	 * @param arguments : see the relayed method.
	 */
	protected void info(String message, Object... arguments)
	{
		info(compose(message, arguments));
	}
	
	/**
	 * Relay for {@link #lf(String, Object...)}.
	 * 
	 * @param message : see the relayed method.
	 * @param arguments : see the relayed method.
	 */
	protected void trace(String message, Object... arguments)
	{
		trace(compose(message, arguments));
	}
}
