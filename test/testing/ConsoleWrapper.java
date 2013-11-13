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
package testing;

import java.io.OutputStream;

import net.xqhs.util.logging.Log;
import net.xqhs.util.logging.Logger.Level;

public class ConsoleWrapper extends Log
{
	
	Level currentLevel = Level.ERROR;
	
	@Override
	public void setLevel(Level level)
	{
		currentLevel = level;
	}
	
	@Override
	public void addDestination(String format, OutputStream destination)
	{
		// unsupported
	}
	
	@Override
	public void l(Level level, String message)
	{
		if(level.compareTo(currentLevel) >= 0)
			System.out.println("[" + level.toString() + "][" + message + "]");
	}
}
