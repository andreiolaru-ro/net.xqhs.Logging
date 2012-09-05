package net.xqhs.util.logging;

import java.io.OutputStream;

import net.xqhs.util.logging.Debug.DebugItem;
import net.xqhs.util.logging.Debug.LocalDebugItem;


/**
 * Use this abstract class to implement any [wrapper of a] logging structure that is returned by {@link Logging}.
 * <p>
 * All types of wrappers available in the project should be stated in {@link LoggerType}.
 * <p>
 * It works with a reduced set of levels (see {@link Level}): trace, info, warning, error, plus <code>OFF</code> and
 * <code>ALL</code> for log settings.
 * <p>
 * Implementing classes should offer three sets of methods:
 * <ul>
 * <li>Classic: <code>error</code>, <code>warn</code>, <code>info</code>, <code>trace</code> - also available with
 * {@link Object} parameters;
 * <li>Short: <code>le lw li lf</code>, <code>l(Level, String)</code>;
 * <li>Special: <code>lr</code>, to be used in return statements, and <code>dbg</code>. See the documentation of the
 * methods.
 * </ul>
 * <p>
 * When extending this class, there is an absolute need that only the <code>l</code> method is implemented. The
 * implementations in this class all end up by calling <code>l</code>.
 * 
 * @author Andrei Olaru
 * 
 */
public abstract class Log
{
	/**
	 * Supported logger types. Wrappers for these will extend the {@link Log} class.
	 * 
	 * @author Andrei Olaru
	 * 
	 */
	public static enum LoggerType {
		JAVA, LOG4J
	}
	
	/**
	 * Indicates the level of the log. Mimics {@link org.apache.log4j.Level}.
	 * 
	 * @author Andrei Olaru
	 * 
	 */
	public enum Level {
		
		OFF,
		
		ERROR,
		
		WARN,
		
		INFO,
		
		TRACE,
		
		ALL,
	}
	
	protected Level	DEFAULT_LEVEL	= Level.INFO;
	
	public abstract void setLevel(Level level);
	
	/**
	 * @param format
	 *            : a pattern, in a format that is potentially characteristic to the wrapper
	 * @param destination
	 *            : a destination stream
	 */
	public abstract void addDestination(String format, OutputStream destination);
	
	public void error(String message, Object... objects)
	{
		error(compose(message, objects));
	}
	
	public void warn(String message, Object... objects)
	{
		warn(compose(message, objects));
	}
	
	public void info(String message, Object... objects)
	{
		info(compose(message, objects));
	}
	
	public void trace(String message, Object... objects)
	{
		trace(compose(message, objects));
	}
	
	public void error(String message)
	{
		le(message);
	}
	
	public void warn(String message)
	{
		lw(message);
	}
	
	public void info(String message)
	{
		li(message);
	}
	
	public void trace(String message)
	{
		lf(message);
	}
	
	public void le(String message)
	{
		l(Level.ERROR, message);
	}
	
	public void lw(String message)
	{
		l(Level.WARN, message);
	}
	
	public void li(String message)
	{
		l(Level.INFO, message);
	}
	
	public void lf(String message)
	{
		l(Level.TRACE, message);
	}
	
	/**
	 * The logging function to override in the implementation of the class.
	 * 
	 * @param level
	 *            : the {@link Level} of the message.
	 * @param message
	 *            : the message.
	 */
	public abstract void l(Level level, String message);
	
	/**
	 * This should be used in return statements. It adds a log message just before returning the {@link Object} in the
	 * argument, displaying the {@link Object}.
	 * 
	 * @param ret
	 *            : the {@link Object} to return.
	 * 
	 * @return the {@link Object} passed as argument.
	 */
	public Object lr(Object ret)
	{
		return lr(ret, null);
	}
	
	/**
	 * This should be used in return statements. It adds a log message just before returning the {@link Object} in the
	 * argument.
	 * <p>
	 * The {@link Object} in the argument is also put in the log message.
	 * 
	 * @param ret
	 *            : the {@link Object} to return and to display.
	 * @param message
	 *            : the message to display beside the {@link Object}.
	 * 
	 * @return the {@link Object} passed as argument.
	 */
	public Object lr(Object ret, String message)
	{
		lf(ret.toString() + (message != null ? ":[" + message + "]" : ""));
		return ret;
	}
	
	/**
	 * This displays a log message (with the level <code>TRACE</code>) only if the specified {@link LocalDebugItem} is
	 * activated.
	 * 
	 * @param debug
	 *            : the {@link LocalDebugItem}
	 * @param message
	 *            : the log message
	 */
	public void dbg(DebugItem debug, String message)
	{
		if(debug.toBool())
			lf(message);
	}
	
	/**
	 * Composes a message with an array of {@link Object} instances.
	 * 
	 * @param message
	 *            : the message
	 * @param objects
	 *            : the objects
	 * @return the concatenated string
	 */
	protected static String compose(String message, Object[] objects)
	{
		String ret = message;
		for(Object object : objects)
			ret += "," + object.toString();
		return ret;
	}
}
