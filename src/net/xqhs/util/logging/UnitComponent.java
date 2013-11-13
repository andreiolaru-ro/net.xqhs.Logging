/**
 * 
 */
package net.xqhs.util.logging;

import net.xqhs.util.logging.Debug.DebugItem;

/**
 * A class that extends {@link Unit} and exposes the logging methods. See {@link Unit} for details.
 * <p>
 * This allows a class to use the functionality of a {@link Unit} without extending {@link Unit}.
 * 
 * @author Andrei Olaru
 * 
 */
public class UnitComponent extends Unit
{
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
