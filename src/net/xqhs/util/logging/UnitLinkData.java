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

import net.xqhs.util.config.Config;
import net.xqhs.util.logging.logging.LogWrapper;

/**
 * A sub-configuration for linking the configured {@link LogWrapper} to another the "parent". Currently only the
 * <code>parentLogName</code> is used.
 * <p>
 * There are several effects of linking:
 * <ul>
 * <li>according to settings, when the "parent" log exits, the current log exits as well.
 * <li>other effects are not currently implemented
 * </ul>
 * 
 * @author Andrei Olaru
 */
@SuppressWarnings("javadoc") // FIXME
public class UnitLinkData extends Config
{
	/**
	 * The name of the parent {@link LogWrapper}.
	 */
	String	parentLogName	= null;
	boolean	exitTogether	= true;	// unused
	boolean	includeInParent	= false;	// unused
	String	prefix			= "";		// unused
										
	public UnitLinkData()
	{
		super();
	}
	
	public UnitLinkData setparentLogName(String parentLogName)
	{
		this.parentLogName = parentLogName;
		return this;
	}
}
