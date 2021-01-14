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
 * The class extends {@link UnitExt} to implement all methods required by the {@link LoggerClassic} interface and expose all
 * methods as public.
 * 
 * @author Andrei Olaru
 */
public class UnitComponentExt extends UnitComponent implements LoggerClassic
{
	@Override
	public void error(String message, Object... arguments)
	{
		le(message, arguments);
	}
	
	@Override
	public void warn(String message, Object... arguments)
	{
		lw(message, arguments);
	}
	
	@Override
	public void info(String message, Object... arguments)
	{
		li(message, arguments);
	}
	
	@Override
	public void trace(String message, Object... arguments)
	{
		lf(message, arguments);
	}
}
