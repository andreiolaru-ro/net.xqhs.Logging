package net.xqhs.util.logging.wrappers;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xqhs.util.logging.LoggerSimple.Level;
import net.xqhs.util.logging.logging.LogWrapper;
import net.xqhs.util.logging.logging.Logging;

/**
 * A {@link LogWrapper} with improved readability and more flexibility in configuring output stream(s).
 * 
 * @author Andrei Olaru
 */
public class ModernLogWrapper extends LogWrapper
{
	/**
	 * Additional output streams, besides the globalLogStream.
	 */
	List<SimpleEntry<OutputStream, Integer>>	outputStreams;
	/**
	 * Format data for messages sent to {@link #globallogStream}.
	 */
	static int									globalFormat	= INCLUDE_NAME;
	/**
	 * The output stream which gets the output of the log
	 */
	private static OutputStream					globallogStream	= System.out;
	/**
	 * The current level for the log.
	 */
	Level										currentLevel	= Level.ERROR;
	/**
	 * The name for the log.
	 */
	String										name			= null;
	
	/**
	 * output strings for the various levels.
	 */
	protected static Map<Level, String>			levelWrite		= new HashMap<>();
	static
	{
		levelWrite.put(Level.ERROR, "#");
		levelWrite.put(Level.WARN, "*");
		levelWrite.put(Level.INFO, ">");
		levelWrite.put(Level.TRACE, ".");
	}
	
	/**
	 * Creates a new console wrapper log, with the specified name.
	 *
	 * @param logName
	 *            - the name of the log to be created.
	 */
	public ModernLogWrapper(String logName)
	{
		name = logName;
	}
	
	/**
	 * Set a custom, global OutputStream to which the output will get written to. This applies to all log messages.
	 * 
	 * @param stream
	 *            a custom OutputStream; must not be <code>null</code>.
	 */
	public static void setGlobalLogStream(OutputStream stream)
	{
		if(stream == null)
			throw new IllegalArgumentException("The stream must not be null");
		globallogStream = stream;
	}
	
	/**
	 * Unused for this implementation
	 * 
	 * @param level
	 *            the new level
	 */
	@Override
	public void setLevel(Level level)
	{
		currentLevel = level;
	}
	
	/**
	 * Unused in this implementation of the LogWrapper
	 * 
	 * @param format
	 *            - a pattern, in a format that is potentially characteristic to the wrapper.
	 * @param destination
	 */
	@Override
	protected void addDestination(String format, OutputStream destination)
	{
		throw new UnsupportedOperationException("String format is unsupported.");
	}
	
	@Override
	protected void addDestination(int formatData, OutputStream destination)
	{
		if(outputStreams == null)
			outputStreams = new ArrayList<>();
		outputStreams.add(new SimpleEntry<>(destination, Integer.valueOf(formatData)));
	}
	
	@SuppressWarnings("resource")
	@Override
	public void l(Level level, String message)
	{
		if(level.displayWith(currentLevel))
			try
			{
				// TODO: see if synchronization is needed.
				globallogStream.write(format(globalFormat, level, message).getBytes());
				if(outputStreams != null)
					for(SimpleEntry<OutputStream, Integer> entry : outputStreams)
						entry.getKey().write(format(entry.getValue().intValue(), level, message).getBytes());
			} catch(IOException e)
			{
				e.printStackTrace();
			}
	}
	
	/**
	 * Formats a logging message with respect to the given format.
	 * 
	 * @param format
	 *            - formatting data, as a bitwise conjunction of constants in {@link LogWrapper}.
	 * @param level
	 *            - the level of the message.
	 * @param message
	 *            - the message.
	 * @return the formatted string exactly how it should be sent to output.
	 */
	protected String format(int format, Level level, String message)
	{
		String formattedMessage = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSSS");
		
		Date time = new Date();
		if((format & INCLUDE_TIMESTAMP) > 0)
			formattedMessage += time.getTime() + " ";
		formattedMessage += " " + levelWrite.get(level) + " ";
		if((format & INCLUDE_NAME) > 0)
			formattedMessage += "[" + name + "] ";
		if((format & INCLUDE_DETAILED_TIME) > 0)
		{
			formattedMessage += "[" + dateFormat.format(time) + "]";
		}
		formattedMessage += message;
		if((format & REPLACE_ENDLINES) > 0)
			formattedMessage += Logging.AWESOME_SEPARATOR;
		else
			formattedMessage += "\n";
		return formattedMessage;
	}
	
	@Override
	public void exit()
	{
		l(Level.INFO, name + " exited");
	}
}
