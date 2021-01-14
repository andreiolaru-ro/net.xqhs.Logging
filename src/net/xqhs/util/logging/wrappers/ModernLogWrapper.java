package net.xqhs.util.logging.wrappers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.xqhs.util.logging.Logger;
import net.xqhs.util.logging.Logger.Level;
import net.xqhs.util.logging.logging.LogWrapper;
import net.xqhs.util.logging.output.LogOutput;
import net.xqhs.util.logging.output.StreamLogOutput;
import net.xqhs.util.logging.output.StringLogOutput;

/**
 * A {@link LogWrapper} with improved readability and more flexibility in configuring output stream(s).
 * 
 * @author Andrei Olaru
 */
public class ModernLogWrapper extends LogWrapper {
	/**
	 * The current level for the log.
	 */
	Level			currentLevel		= Level.ERROR;
	/**
	 * The name for the log.
	 */
	String			name				= null;
	/**
	 * The current maximum width of a source name.
	 */
	static int		cSourceWidth		= 0;
	/**
	 * The format for padding the name, at a width of {@link #cSourceWidth}.
	 */
	static String	paddedNameFormat	= "%-1s";
	
	/**
	 * The detailed time format.
	 */
	static SimpleDateFormat			dateFormat		= new SimpleDateFormat("HH:mm:ss.SSSS");
	/**
	 * Update timers for logOutputs with update periods.
	 */
	protected Map<LogOutput, Timer>	updateTimers	= new HashMap<>();
	
	class TTUpdate extends TimerTask {
		LogOutput		logOutput	= null;
		OutputStream	logStream	= null;
		
		public TTUpdate(LogOutput output, OutputStream stream) {
			logOutput = output;
			logStream = stream;
		}
		
		@Override
		public void run() {
			if(logOutput instanceof StreamLogOutput)
				((StreamLogOutput) logOutput).update();
			if(logOutput instanceof StringLogOutput) {
				((StringLogOutput) logOutput).update(logStream.toString());
				if(!((StringLogOutput) logOutput).updateWithEntireLog())
					((ByteArrayOutputStream) logStream).reset();
			}
		}
	}
	
	/**
	 * output strings for the various levels.
	 */
	protected static Map<Level, String> levelWrite = new HashMap<>();
	static {
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
	public ModernLogWrapper(String logName) {
		name = logName;
	}
	
	/**
	 * Unused for this implementation
	 * 
	 * @param level
	 *            the new level
	 */
	@Override
	public void setLevel(Level level) {
		currentLevel = level;
	}
	
	@SuppressWarnings("resource")
	@Override
	public void l(Level level, String message) {
		if(logOutputs.isEmpty())
			addOutput(LogOutput.DEFAULT_LOG_OUTPUT);
		if(level.displayWith(currentLevel))
			for(SimpleEntry<LogOutput, OutputStream> entry : logOutputs)
				try {
					LogOutput logOutput = entry.getKey();
					String logPost = logOutput.useCustomFormat() ? logOutput.format(level, name, message)
							: format(logOutput.formatData(), level, message);
					if(logOutput instanceof StreamLogOutput) {
						((StreamLogOutput) logOutput).getOutputStream().write(logPost.getBytes());
						if(logOutput.getUpdatePeriod() <= 0)
							((StreamLogOutput) logOutput).update();
						else
							postUpdate(logOutput, null);
					}
					else if(logOutput instanceof StringLogOutput)
						if(logOutput.getUpdatePeriod() <= 0)
							((StringLogOutput) logOutput).update(logPost);
						else {
							entry.getValue().write(logPost.getBytes());
							postUpdate(logOutput, entry.getValue());
						}
				} catch(IOException e) {
					System.out.println(format(Logger.INCLUDE_NAME, Level.ERROR, "Log write error."));
				}
	}
	
	protected void postUpdate(LogOutput logOutput, OutputStream stream) {
		if(updateTimers.get(logOutput) != null)
			return;
		updateTimers.put(logOutput, new Timer());
		updateTimers.get(logOutput).schedule(new TTUpdate(logOutput, stream), logOutput.getUpdatePeriod());
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
	protected String format(int format, Level level, String message) {
		String formattedMessage = "";
		Date time = new Date();
		if((format & Logger.INCLUDE_TIMESTAMP) > 0)
			formattedMessage += time.getTime() + " ";
		formattedMessage += levelWrite.get(level) + " ";
		if((format & Logger.INCLUDE_NAME) > 0) {
			if(cSourceWidth < name.length()) {
				cSourceWidth = name.length();
				paddedNameFormat = "%-" + cSourceWidth + "s";
			}
			formattedMessage += "[ " + String.format(paddedNameFormat, name) + " ] ";
		}
		if((format & Logger.INCLUDE_DETAILED_TIME) > 0) {
			formattedMessage += "[" + dateFormat.format(time) + "]";
		}
		formattedMessage += message;
		if((format & Logger.REPLACE_ENDLINES) > 0)
			formattedMessage += Logger.AWESOME_SEPARATOR;
		else
			formattedMessage += "\n";
		return formattedMessage;
	}
	
	@Override
	public void exit() {
		l(Level.INFO, name + " exited");
	}
}
