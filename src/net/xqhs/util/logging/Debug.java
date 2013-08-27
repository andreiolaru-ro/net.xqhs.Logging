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
 * Class containing different debug items that can be activated or deactivated by editing this class. Works together
 * with function <code>dbg()</code> in {@link Log}.
 * <p>
 * For documentation purposes, classes containing {@link LocalDebugItem} enums should extend the {@link Debug} class.
 * <p>
 * Each project should contain its own class extending {@link Debug}, holding an enum the implements {@link DebugItem},
 * with a similar implementation to this one.
 * 
 * @author Andrei Olaru
 */
public class Debug
{
	public interface DebugItem
	{
		public boolean toBool();
	}
	
	public enum LocalDebugItem implements DebugItem {
		
		/**
		 * Dummy debug item.
		 */
		D_D(false),
		
		;
		
		boolean isset;
		
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
