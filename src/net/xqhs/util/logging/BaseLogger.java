package net.xqhs.util.logging;

import net.xqhs.util.logging.Debug.DebugItem;

/**
 * This abstract class offers an implementation for the methods specified by the {@link LoggerClassic} interface, leaving only
 * {@link #l} and {@link #lr} to be implemented.
 * <p>
 * The implementation currently replicates the code in {@link Unit} and {@link UnitExt}.
 * 
 * @author andreiolaru
 */
public abstract class BaseLogger implements LoggerClassic
{
	@Override
	public void le(String message, Object... arguments)
	{
		l(Level.ERROR, message, arguments);
	}
	
	@Override
	public void lw(String message, Object... arguments)
	{
		l(Level.WARN, message, arguments);
	}
	
	@Override
	public void li(String message, Object... arguments)
	{
		l(Level.INFO, message, arguments);
	}
	
	@Override
	public void lf(String message, Object... arguments)
	{
		l(Level.TRACE, message, arguments);
	}
	
	@Override
	public Object lr(Object ret)
	{
		return lr(ret, null);
	}
	
	@Override
	public boolean ler(boolean ret, String message, Object... arguments)
	{
		le(message, arguments);
		return ret;
	}

	@Override
	public abstract Object lr(Object ret, String message, Object... arguments);
	
	@Override
	public void dbg(DebugItem debug, String message, Object... arguments)
	{
		if(debug.toBool())
			lf(message, arguments);
	}
	
	/**
	 * This method calls the underlying logging infrastructure to display a message with the specified level, text, and
	 * parameter objects.
	 * 
	 * @param messageLevel
	 *            - the level of the message.
	 * @param message
	 *            - the text of the message.
	 * @param arguments
	 *            - the objects to be inserted in the placeholders of the message text (see {@link Logger}).
	 */
	protected abstract void l(Level messageLevel, String message, Object... arguments);

	
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
