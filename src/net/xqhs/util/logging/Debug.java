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
 * with the method in {@link Logger#dbg(DebugItem, String, Object...)} or similar implementations.
 * <p>
 * Each project must contain its own enumerations implementing {@link DebugItem}, with a similar implementation to this
 * one.
 * 
 * @author Andrei Olaru
 */
public class Debug {
	/**
	 * This interface must be implemented by all enumerations of debug constants in order to be able to use them as
	 * arguments of {@link Logger#dbg(DebugItem, String, Object...)} implementations.
	 * 
	 * @author Andrei Olaru
	 */
	public interface DebugItem {
		/**
		 * Computes the activation state of the constant. The method is invoked during the <code>dbg()</code> call for
		 * the specified debug constant.
		 * 
		 * @return the activation state.
		 */
		public boolean toBool();
	}
	
	/**
	 * The internal debug constants.
	 * 
	 * @author Andrei Olaru
	 */
	public enum LogDebugItem implements DebugItem {
		/**
		 * Tracing of log management.
		 */
		D_LOG_MANAGEMENT(true),
		
		;
		
		/**
		 * The activation state.
		 */
		private boolean isSet;
		
		/**
		 * Default constructor.
		 * 
		 * @param isSet
		 *            - activation state.
		 */
		private LogDebugItem(boolean isSet) {
			this.isSet = isSet;
		}
		
		@Override
		public boolean toBool() {
			return isSet;
		}
	}
}
