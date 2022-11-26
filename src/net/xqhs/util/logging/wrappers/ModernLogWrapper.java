package net.xqhs.util.logging.wrappers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import net.xqhs.util.logging.LogWrapper;
import net.xqhs.util.logging.Logger;
import net.xqhs.util.logging.Logger.Level;
import net.xqhs.util.logging.output.LogOutput;
import net.xqhs.util.logging.output.StreamLogOutput;
import net.xqhs.util.logging.output.StringLogOutput;

/**
 * A {@link LogWrapper} with improved readability and more flexibility in configuring output stream(s).
 * <p>
 * Highlighting is done by un-indenting non-highlighted logs.
 * 
 * @author Andrei Olaru
 */
public class ModernLogWrapper extends LogWrapper {
	/**
	 * The current maximum width of a source name.
	 */
	static int								cSourceWidth		= 0;
	/**
	 * The format for padding the name, at a width of {@link #cSourceWidth}.
	 */
	static String							paddedNameFormat	= "%-1s";
	/**
	 * Indent for entries of not highlighted logs.
	 */
	static String							unhighlightedIndent	= "\t\t\t\t";
	/**
	 * The detailed time format.
	 */
	static SimpleDateFormat					dateFormat			= new SimpleDateFormat("HH:mm:ss.SSSS");
	/**
	 * The set of highlighted logs, used to recognize if there are no highlighted logs.
	 */
	protected static Set<ModernLogWrapper>	highlightedLogs		= new HashSet<>();
	/**
	 * Update timers for logOutputs with update periods.
	 */
	protected Map<LogOutput, TimerTask>		updateTasks			= new HashMap<>();
	/**
	 * The timer for periodic updates.
	 */
	Timer									updateTimer			= null;
	
	/**
	 * The task to update logs.
	 */
	class TTUpdate extends TimerTask {
		/**
		 * The output.
		 */
		LogOutput		logOutput	= null;
		/**
		 * The stream log messages are written to (for {@link StringLogOutput}).
		 */
		OutputStream	logStream	= null;
		
		/**
		 * @param output
		 *            the output.
		 * @param stream
		 *            stream log messages are written to (for {@link StringLogOutput}).
		 */
		public TTUpdate(LogOutput output, OutputStream stream) {
			logOutput = output;
			logStream = stream;
		}
		
		/**
		 * Calls the update method of the log output (depending on {@link LogOutput} type).
		 */
		@Override
		public void run() {
			updateTasks.remove(logOutput);
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
	
	@Override
	public void setHighlighted(boolean isHighlighted) {
		super.setHighlighted(isHighlighted);
		if(isHighlighted)
			highlightedLogs.add(this);
		else
			highlightedLogs.remove(this);
	}
	
	@Override
	public void removeOutput(LogOutput logOutput) {
		if(updateTasks.containsKey(logOutput))
			updateTasks.get(logOutput).cancel();
		updateTasks.remove(logOutput);
		super.removeOutput(logOutput);
		if(updateTasks.isEmpty() && updateTimer != null)
			updateTimer.cancel();
	}
	
	@SuppressWarnings("resource")
	@Override
	public void l(Level level, String message) {
		if(logOutputs.isEmpty())
			addOutput(LogOutput.DEFAULT_LOG_OUTPUT);
		if(level.displayWith(currentLevel))
			for(LogOutput logOutput : logOutputs.keySet())
				try {
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
							logOutputs.get(logOutput).write(logPost.getBytes());
							postUpdate(logOutput, logOutputs.get(logOutput));
						}
				} catch(IOException e) {
					System.out.println(format(Logger.INCLUDE_NAME, Level.ERROR, "Log write error."));
				}
	}
	
	/**
	 * Post an update to a log, either by updating immediately or by creating a timer task so as to update the output
	 * later.
	 * 
	 * @param logOutput
	 * @param stream
	 */
	protected void postUpdate(LogOutput logOutput, OutputStream stream) {
		if(updateTasks.get(logOutput) != null)
			// there is already an update tasks scheduled.
			return;
		if(updateTimer == null) {
			updateTimer = new Timer();
		}
		updateTasks.put(logOutput, new TTUpdate(logOutput, stream));
		updateTimer.schedule(updateTasks.get(logOutput), logOutput.getUpdatePeriod());
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
		String formattedMessage = highlight || highlightedLogs.isEmpty() ? "" : unhighlightedIndent;
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
			formattedMessage += "[" + dateFormat.format(time) + "] ";
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
		super.exit();
		if(updateTimer != null)
			updateTimer.cancel();
	}
}
