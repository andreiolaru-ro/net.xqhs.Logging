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
 * Class containing different debug items that can be activated or deactivated by editing this class. Works together
 * with the method <code>dbg()</code> in {@link LoggerSimple} or similar implementations.
 * <p>
 * For documentation purposes, classes containing {@link LocalDebugItem} enums should extend the {@link Debug} class.
 * <p>
 * Each project must contain its own class(es) extending {@link Debug}, holding an enum the implements {@link DebugItem}
 * , with a similar implementation to this one.
 * 
 * @author Andrei Olaru
 */
public class Debug
{
	/**
	 * This interface must be implemented by all enumerations of debug constants in order to be able to use them as
	 * arguments of method <code>dbg()</code> in {@link LoggerSimple} implementations.
	 * 
	 * @author Andrei Olaru
	 */
	public interface DebugItem
	{
		/**
		 * Computes the activation state of the constant. The method is invoked during the <code>dbg()</code> call for
		 * the specified debug constant.
		 * 
		 * @return the activation state.
		 */
		public boolean toBool();
	}
	
	/**
	 * Example enumeration implementing {@link DebugItem}.
	 * 
	 * @author Andrei Olaru
	 */
	public enum LocalDebugItem implements DebugItem {
		
		/**
		 * Dummy/example debug item.
		 */
		D_D(false),
		
		;
		
		/**
		 * Activation state.
		 */
		boolean	isset;
		
		/**
		 * Default constructor.
		 * 
		 * @param set
		 *            - activation state.
		 */
		private LocalDebugItem(boolean set)
		{
			isset = set;
		}
		
		@Override
		public boolean toBool()
		{
			return isset;
		}
	}
	
}
