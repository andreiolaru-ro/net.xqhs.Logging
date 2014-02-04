/**
 * 
 */
package net.xqhs.util.logging;

import net.xqhs.util.logging.Debug.DebugItem;

/**
 * A class that extends {@link Unit} and exposes the logging methods. See {@link Unit} for details. It implements {@link LoggerSimple}.
 * <p>
 * This allows a class to use the functionality of a {@link Unit} without extending {@link Unit}.
 * 
 * @author Andrei Olaru
 * 
 */
public class UnitComponent extends Unit implements LoggerSimple
{
	@Override
	public void le(String message, Object... arguments)
	{
		super.le(message, arguments);
	}
	
	@Override
	public void lw(String message, Object... arguments)
	{
		super.lw(message, arguments);
	}
	
	@Override
	public void li(String message, Object... arguments)
	{
		super.li(message, arguments);
	}
	
	@Override
	public void lf(String message, Object... arguments)
	{
		super.lf(message, arguments);
	}
	
	@Override
	public Object lr(Object ret)
	{
		return super.lr(ret);
	}
	
	@Override
	public Object lr(Object ret, String message, Object... arguments)
	{
		return super.lr(ret, message, arguments);
	}
	
	@Override
	public void dbg(DebugItem debug, String message, Object... arguments)
	{
		super.dbg(debug, message);
	}
	
	@Override
	public void doExit()
	{
		super.doExit();
	}
	
	@Override
	public String getUnitName()
	{
		return super.getUnitName();
	}
}
