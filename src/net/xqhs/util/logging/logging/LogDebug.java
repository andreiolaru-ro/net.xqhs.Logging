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
package net.xqhs.util.logging.logging;

import net.xqhs.util.logging.Debug;

/**
 * Class containing the enumeration of debug constants for the logging infrastructure itself (internal audit).
 * 
 * @author Andrei Olaru
 */
public class LogDebug extends Debug
{
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
		private boolean	isSet;
		
		/**
		 * Default constructor.
		 * 
		 * @param isSet
		 *            - activation state.
		 */
		private LogDebugItem(boolean isSet)
		{
			this.isSet = isSet;
		}
		
		@Override
		public boolean toBool()
		{
			return isSet;
		}
		
	}
}
