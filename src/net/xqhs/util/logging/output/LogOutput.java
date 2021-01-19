package net.xqhs.util.logging.output;

import net.xqhs.util.logging.LogWrapper;
import net.xqhs.util.logging.Logger;
import net.xqhs.util.logging.Logger.Level;

/**
 * The interface models any instance which can serve as a destination for logging messages.
 * <p>
 * This interface is in a way "abstract", in that it doesn't contain an update method. {@link LogWrapper}s may call an
 * update method depending on the actual interface, e.g. {@link StringLogOutput} or {@link StreamLogOutput}.
 * 
 * @author Andrei Olaru
 */
public interface LogOutput {
	/**
	 * Default {@link LogOutput} for when no output is specified.
	 */
	public final static LogOutput DEFAULT_LOG_OUTPUT = new ConsoleOutput();
	
	/**
	 * An update period equal or lower than 0 means that the output should be updated immediately.
	 * <p>
	 * A positive number represents the number of seconds at which to update the output.
	 * 
	 * @return the update period.
	 */
	public int getUpdatePeriod();
	
	/**
	 * @return the format information, assembled via bitwise operations from constants in {@link Logger}.
	 */
	public int formatData();
	
	/**
	 * @return <code>true</code> if the {@link #format(Level, String, String)} method should be called to format logging
	 *         messages.
	 */
	public boolean useCustomFormat();
	
	/**
	 * if {@link #useCustomFormat()} returns <code>true</code>, this method will be called to format messages.
	 * 
	 * @param level
	 *            - the {@link Level} of the message.
	 * @param source
	 *            - the source of the message.
	 * @param message
	 *            - the message.
	 * @return the formatted message, ready to be sent to the output.
	 */
	public String format(Level level, String source, String message);
}
