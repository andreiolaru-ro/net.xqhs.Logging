package net.xqhs.util.logging;

/**
 * This class extends {@link Unit} in order to provide all functionality required by the {@link Logger} interface.
 * <p>
 * The class does not implement the interface, as that would require for the methods to be public, but to policy of
 * {@link Unit} is to have logging methods protected.
 * 
 * @author Andrei Olaru
 */
public class UnitExt extends Unit
{
	protected void error(String message, Object... objects)
	{
		error(compose(message, objects));
	}
	
	protected void warn(String message, Object... objects)
	{
		warn(compose(message, objects));
	}
	
	protected void info(String message, Object... objects)
	{
		info(compose(message, objects));
	}
	
	protected void trace(String message, Object... objects)
	{
		trace(compose(message, objects));
	}
	
	protected void error(String message)
	{
		le(message);
	}
	
	protected void warn(String message)
	{
		lw(message);
	}
	
	protected void info(String message)
	{
		li(message);
	}
	
	protected void trace(String message)
	{
		lf(message);
	}
}
