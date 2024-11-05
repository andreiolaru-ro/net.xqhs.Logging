package net.xqhs.util.logging.output;

import net.xqhs.util.logging.LogWrapper;
import net.xqhs.util.logging.Logger;
import net.xqhs.util.logging.Logger.Level;

/**
 * The interface models any instance which can serve as a destination for logging messages. Instances of this class
 * handle the actual writing of logging messages (constructed by a {@link LogWrapper}) to a destination (e.g. a stream,
 * a file, etc)
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
	 * An update period equal to 0 means that the output should be updated immediately with each new log message.
	 * <p>
	 * A positive number represents the number of milliseconds at which to update the output.
	 * <p>
	 * A negative value means that the log should never / will never be flushed explicitly.
	 * 
	 * @return the update period.
	 */
	public long getUpdatePeriod();
	
	/**
	 * @return the format information, assembled via bitwise operations from constants in {@link Logger}. This
	 *         information is used by the {@link LogWrapper} instance to format messages according to the needs of this
	 *         particular output.
	 */
	public int formatData();
	
	/**
	 * This should return <code>true</code> if there are formatting needs that cannot be fulfilled by using the
	 * specification constants in {@link Logger}.
	 * 
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
