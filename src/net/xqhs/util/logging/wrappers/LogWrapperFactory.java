package net.xqhs.util.logging.wrappers;

import net.xqhs.util.logging.logging.LogWrapper;
import net.xqhs.util.logging.logging.LogWrapper.LoggerType;

/**
 * Replaces dynamic class loading with a factory method.
 * 
 * @author andreiolaru
 */
public class LogWrapperFactory
{
	/**
	 * Returns a new instance of a {@link LogWrapper}-implementing class.
	 * 
	 * @param loggerType - the desired wrapper type.
	 * @param logName - the name for the new log.
	 * @return a fresh {@link LogWrapper} instance.
	 */
	public static LogWrapper getLogWrapper(LoggerType loggerType, String logName)
	{
		switch(loggerType)
		{
		case CONSOLE:
			return new ConsoleWrapper(logName);
		case JAVA:
			return new JavaLogWrapper(logName);
		default:
			throw new UnsupportedOperationException("wrapper type " + loggerType + " not supported.");
		}
	}
}
