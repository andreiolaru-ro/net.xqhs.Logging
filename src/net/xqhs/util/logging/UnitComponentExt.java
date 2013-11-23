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

import net.xqhs.util.logging.Debug.DebugItem;

/**
 * The class extends {@link UnitExt} to implement all methods required by the {@link Logger} interface and expose all
 * methods as public.
 * 
 * @author Andrei Olaru
 */
public class UnitComponentExt extends UnitExt implements Logger
{
	@Override
	public void error(String message, Object... objects)
	{
		super.error(message, objects);
	}
	
	@Override
	public void warn(String message, Object... objects)
	{
		super.warn(message, objects);
	}
	
	@Override
	public void info(String message, Object... objects)
	{
		super.info(message, objects);
	}
	
	@Override
	public void trace(String message, Object... objects)
	{
		super.trace(message, objects);
	}
	
	@Override
	public void error(String message)
	{
		super.error(message);
	}
	
	@Override
	public void warn(String message)
	{
		super.warn(message);
	}
	
	@Override
	public void info(String message)
	{
		super.info(message);
	}
	
	@Override
	public void trace(String message)
	{
		super.trace(message);
	}
	
	@Override
	public void le(String message)
	{
		super.le(message);
	}
	
	@Override
	public void lw(String message)
	{
		super.lw(message);
	}
	
	@Override
	public void li(String message)
	{
		super.li(message);
	}
	
	@Override
	public void lf(String message)
	{
		super.lf(message);
	}
	
	@Override
	public Object lr(Object ret)
	{
		return super.lr(ret);
	}
	
	@Override
	public Object lr(Object ret, String message)
	{
		return super.lr(ret, message);
	}
	
	@Override
	public void dbg(DebugItem debug, String message)
	{
		super.dbg(debug, message);
	}
	
	@Override
	public void doExit()
	{
		super.doExit();
	}
}
