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
 * Interface for an entity that is able to display the log (e.g. in a visual interface). Updates will be posted
 * immediately. The {@link #output(String)} method is called with the entire log as argument, using logging messages
 * with just the level ad the content.
 * 
 * @author Andrei Olaru
 * 
 */
public interface DisplayEntity
{
	/**
	 * The method is called whenever new logging information is posted.
	 * 
	 * @param string
	 *            - the entire contents of the log.
	 */
	void output(String string);
	
}
