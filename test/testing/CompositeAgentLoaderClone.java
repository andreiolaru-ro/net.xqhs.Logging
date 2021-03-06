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
package testing;

import net.xqhs.util.logging.LoggerSimple.Level;
import net.xqhs.util.logging.logging.Logging;
import net.xqhs.util.logging.UnitComponentExt;

public class CompositeAgentLoaderClone
{
	public static void main(String[] args)
	{
		Logging.getMasterLogging().setLogLevel(Level.ALL);
		UnitComponentExt log = (UnitComponentExt) new UnitComponentExt().setUnitName("agent loader").setLogLevel(
				Level.ALL);
		
		log.trace("component [] loaded for agent [].");
		
		log.trace("agent [] loaded.");
		log.doExit();
		
	}
}
