/*******************************************************************************
 * Copyright (C) 2015 Andrei Olaru.
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
 * Interface for an entity / unit that keeps a log and that needs to report that log to other entities. Updates will
 * be posted at regular intervals of time, and will be incremental (a new post will contain all logging information
 * since the previous successful posting. The messages will contain timestamp, log name, level and content.
 * 
 * @author Andrei Olaru
 */
public interface ReportingEntity
{
	/**
	 * The method will be called at intervals of <code>reportUpdateDelay</code>, if new logging information exist
	 * since the last call.
	 * 
	 * @param content
	 *            - an update from the log containing the new logging information since the last call of this
	 *            function.
	 * @return true if the reporting has completed correctly. Otherwise, the same information will be reported again
	 *         at the next call.
	 */
	public boolean report(String content);
}
