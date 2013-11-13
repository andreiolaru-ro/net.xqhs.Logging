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
