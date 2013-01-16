package net.xqhs.util.logging;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import net.xqhs.util.logging.Log.Level;
import net.xqhs.util.logging.Log.LoggerType;
import net.xqhs.util.logging.LogDebug.LogDebugItem;
import net.xqhs.util.logging.wrappers.JavaLogWrapper;
import net.xqhs.util.logging.wrappers.Log4JWrapper;

/**
 * Implements configuring entity and manager for logs. The idea is to have standard logs and wrappers, but with some
 * additional features, that are accessible via static functions of this class, using the Logger object itself as a
 * unique reference.
 * <p>
 * There are currently three possible destinations for the logging information: the console output is standard; an
 * optional {@link DisplayEntity} may be specified to be updated with the contents of the log; a {@link ReportingEntity}
 * may be specified that will be updated with the logging information.
 * <p>
 * A new log is obtained by <code>getLogger(unitName)</code>, where <code>unitName</code> should be unique and is the
 * name of the log.
 * <p>
 * Other constructors are available, specifying a {@link DisplayEntity} and / or a {@link ReportingEntity}.
 * <p>
 * Using the constructor(s) that contain the 'link' parameter, the log is linked to another log (its 'parent') and when
 * the parent closes, the children close too.
 * <p>
 * When a log is not needed any more, one should <b>_always_</b> call exitLogger() for the that log. (Except if a parent
 * has been given, and it is certain that the log will be closed by its parent.)
 * 
 * @author Andrei Olaru
 * 
 */
public class Logging
{
	/**
	 * Interface for an entity / unit that keeps a log and that needs to report that log to other entities.
	 * 
	 * @author Andrei Olaru
	 * 
	 */
	public interface ReportingEntity
	{
		/**
		 * The function will be called at intervals of reportUpdateDelay, if new logging information exist since the
		 * last call.
		 * 
		 * @param content
		 *            - an update from the log containing the new logging information since the last call of this
		 *            function.
		 * @return true if the reporting has completed correctly. Otherwise, the same information will be reported again
		 *         at the next call.
		 */
		public boolean report(String content);
	}
	
	/**
	 * Interface for an entity that is able to display the log (e.g. in a visual interface).
	 * 
	 * @author Andrei Olaru
	 * 
	 */
	public interface DisplayEntity
	{
		
		void output(String string);
		
	}
	
	private static LoggerType				defaultLoggerWrapper	= LoggerType.LOG4J;
	
	public static Character					AWESOME_SEPARATOR		= new Character((char)30);
	
	// // here be the static components of the class, which by being static are unique for the current JVM.
	
	/**
	 * Contains all the currently active logs, identified by their [unit]Name. Active means that a timer is associated
	 * with them.
	 * 
	 * All access to the logs field should be synchronized explicitly.
	 */
	protected static Map<String, Logging>	logs					= Collections.synchronizedMap(new HashMap<String, Logging>());
	/**
	 * All access to parents should be synchronized. In this source, all accesses take place inside locks on the logs
	 * field.
	 */
	protected static Map<String, String>	parents					= new HashMap<>();
	
	/**
	 * A {@link Unit} or logging messages related to the log management. Can be configured via
	 * <code>configureMasterLogging()</code>.
	 * <p>
	 * Will automatically start and stop when the first log is created / when the last log exists.
	 */
	protected static Unit					masterLog				= null;
	protected static UnitConfigData			masterLogConfig			= new UnitConfigData();
	protected static String					masterLogName			= "M-log";
	
	// /////////// here be the components of the log
	/**
	 * The logger that is being wrapped by the instance.
	 */
	protected Log							logger					= null;
	/**
	 * The logger implementation. Indicates the wrapper to use.
	 */
	protected LoggerType					wrapperType				= null;
	/**
	 * The name of the log and of the Unit.
	 */
	protected String						name					= null;
	/**
	 * The level of the log (see {@link Log} ).
	 */
	protected Level							logLevel				= Level.ALL;
	
	// /////////// here be the components of the log related to external reporting (to a text area and to a Jade agent,
	// respectively
	/**
	 * Contains the entire output of the log. Version without time stamps and unit name, just level and message.
	 */
	protected ByteArrayOutputStream			logOutput				= null;
	/**
	 * Contains the entire output of the log. Version with time stamp, level, unit name, and message.
	 */
	protected ByteArrayOutputStream			logOutputStamped		= null;
	/**
	 * used to trace if there have been modifications to the log, before flushing it into the TextArea.
	 */
	protected long							logSize					= 0L;
	/**
	 * The {@link DisplayEntity} that will be kept up to date with the contents of the log
	 */
	protected DisplayEntity					logDisplay				= null;
	/**
	 * Timer to update the external views of this log.
	 */
	protected Timer							logTimer				= null;
	/**
	 * Delay at which to update the text area.
	 */
	protected long							logUpdateDelay			= 250;
	/**
	 * Delay at which to update the visualizing agent.
	 */
	protected long							reportUpdateDelay		= 2000;
	protected long							timeToNextReport		= 0;
	/**
	 * The {@link AID} of the Jade agent to send logging information to.
	 */
	protected ReportingEntity				externalReporter		= null;
	/**
	 * Cumulative size of the logging information sent so far.
	 */
	protected int							lastUpdatedSize			= 0;
	
	/**
	 * Configures the <code>masterLog</code> that will be used for log messages regarding global log management.
	 * 
	 * @param config
	 *            : the configuration.
	 */
	public static void configureMasterLogging(UnitConfigData config)
	{
		masterLogConfig = config;
	}
	
	/**
	 * Provides a logger with the given name. If the name is already in use (and <code>ensureNew</code> is
	 * <code>false</code>), the log corresponding to that name will be returned and all other parameters will be
	 * ignored.
	 * 
	 * @throws IllegalArgumentException
	 *             : if the name is not new and <code>ensureNew</code> was set to true.
	 * @param name
	 *            : is the name of the associated unit, and of the log. Should be not null unique among active logs. Use
	 *            {@link Unit}.DEFAULT_UNIT_NAME for the default.
	 * @param link
	 *            : the name of another, 'parent', log that this log is linked to; when the other log will close, this
	 *            will be closed as well.
	 * @param textArea
	 *            : a TextArea that will be updated with the contents of the log.
	 * @param loggerType
	 *            : the {@link Log} class to instantiate. If null, one of them is chosen by default.
	 * @return A new, configured, Logger object.
	 */
	public static Log getLogger(String name, String link, DisplayEntity display, ReportingEntity reporter, boolean ensureNew, LoggerType loggerType)
	{
		boolean erred = false;
		int nlogs = -1;
		
		if(masterLog == null)
		{
			masterLog = new Unit(); // avoid recursion by having a non-null masterLog first.
			masterLog = new Unit(masterLogConfig) {
				@Override
				protected String getDefaultUnitName()
				{
					return masterLogName;
				}
			};
		}
		
		if(name == null)
			throw new IllegalArgumentException("log name cannot be null. Use unit.DEFAULT_UNIT_NAME for the default name.");
		
		if(masterLog.log != null)
			masterLog.log.dbg(LogDebugItem.D_LOG_MANAGEMENT, "required: [" + name + "]" + (ensureNew ? "[new]" : "") + "; existing: [" + logs.size() + "]: [" + logs + "]");
		
		Logging thelog = new Logging(name, loggerType, display, reporter);
		Logging alreadyPresent = null;
		synchronized(logs)
		{
			// if(masterLog.log != null) // uncomment only if necessary - it is in a critical region
			// masterLog.log.dbg(DebugItem.D_LOG_MANAGEMENT, "/s/ required: [" + name + "]" + (ensureNew ? "[new]" : "")
			// + "; existing: [" + logs.size() + "]: [" + logs + "]");
			if(logs.containsKey(name))
				if(ensureNew)
					erred = true;
				else
					alreadyPresent = logs.get(name);
			else
			{
				logs.put(name, thelog);
				parents.put(name, link);
				nlogs = logs.size();
			}
		}
		if(erred)
		{
			thelog.doexit();
			throw new IllegalArgumentException("log name already present [" + name + "]");
		}
		if(alreadyPresent != null)
		{
			thelog.doexit();
			thelog = alreadyPresent;
		}
		thelog.logger.trace("new log (count now [" + nlogs + "]).");
		return thelog.getLog();
	}
	
	/**
	 * Provides a logger with the given name. If the name is already in use, the log corresponding to that name will be
	 * returned and all other parameters will be ignored.
	 * 
	 * @throws IllegalArgumentException
	 *             : if the name already exists for an active log.
	 * @param name
	 *            : is the name of the associated unit, and of the log. Should be unique among active logs.
	 * @param link
	 *            : the name of another, 'parent', log that this log is linked to; when the other log will close, this
	 *            will be closed as well.
	 * @return A new, configured, Logger object.
	 */
	public static Log getLogger(String name, String link)
	{
		return getLogger(name, link, null, null, false, null);
	}
	
	/**
	 * Provides a logger with the given name.
	 * 
	 * @throws IllegalArgumentException
	 *             : if the name already exists for an active log.
	 * @param name
	 *            : is the name of the associated unit, and of the log. Should be unique among active logs.
	 * @return A new, configured, Logger object.
	 */
	public static Log getLogger(String name)
	{
		return getLogger(name, null, null, null, false, null);
	}
	
	/**
	 * Provides a logger with the given name. If the name is already in use, the log corresponding to that name will be
	 * returned and all other parameters will be ignored.
	 * 
	 * @throws IllegalArgumentException
	 *             : if the name already exists for an active log.
	 * @param name
	 *            : is the name of the associated unit, and of the log. Should be unique among active logs.
	 * @param textArea
	 *            : a TextArea that will be updated with the contents of the log.
	 * @return A new, configured, Logger object.
	 */
	public static Log getLogger(String name, DisplayEntity display)
	{
		return getLogger(name, null, display, null, false, null);
	}
	
	/**
	 * Get the whole output of the log.
	 * 
	 * @param name
	 *            : the name of the log. If within a normal {@link Unit}, probably the return value of getName().
	 * @param shortOutput
	 *            : if <code>true</code>, the output does not contain the agent name and time stamps (just the level and
	 *            message).
	 * @return the entire contents of the log.
	 */
	public static String getLoggerOutput(String name, boolean shortOutput)
	{
		Logging found = null;
		synchronized(logs)
		{
			found = logs.get(name);
		}
		if(found == null)
			throw new IllegalArgumentException("log not present [" + name + "]");
		if(shortOutput)
			return found.logOutput.toString();
		return found.logOutputStamped.toString();
	}
	
	/**
	 * Closes the log specified by the name (stops the associated timer) and frees the name so it can be reused. The log
	 * with not be flushed (sent as report) before closing.
	 * 
	 * @param name
	 *            : the name of the log to be freed. If within a normal {@link Unit}, probably the return value of
	 *            getName().
	 */
	public static void exitLogger(String name)
	{
		exitLogger(name, false);
	}
	
	/**
	 * Closes the log specified by the name (stops the associated timer) and frees the name so it can be reused.
	 * 
	 * @param name
	 *            the name of the log to be freed. If within a normal {@link Unit}, probably the return value of
	 *            getName().
	 */
	public static void exitLogger(String name, boolean flushFirst)
	{
		boolean notpresent = false;
		Logging found = null;
		int nlogs = -1;
		Vector<String> toClose = new Vector<>();
		synchronized(logs)
		{
			if(!logs.containsKey(name))
				notpresent = true;
			else
			{
				for(Map.Entry<String, String> link : parents.entrySet())
					if(name.equals(link.getValue()))
						toClose.add(link.getKey());
				found = logs.get(name);
				logs.remove(name);
				nlogs = logs.size();
			}
		}
		if(notpresent || (found == null))
			throw new IllegalArgumentException("log not present [" + name + "]");
		found.getLog().trace("log out (logs remaining [" + nlogs + "]).");
		for(String logName : toClose)
			exitLogger(logName, flushFirst);
		if(flushFirst)
			found.updateReport();
		found.doexit();
		
		if(nlogs == 1 && masterLog != null && masterLog.log != null) // this was the last non-master log
			masterLog.doExit();
	}
	
	/**
	 * Resets the maps containing the links to the logs.
	 * 
	 * <p>
	 * SHOULD BE USED WITH EXTREME CAUTION and only in special cases where the application is reset without closing it
	 * (e.g. on Android).
	 * 
	 */
	public static void resetLogging()
	{
		synchronized(logs)
		{
			logs.clear();
			parents.clear();
		}
		
		masterLog.log.info("--------logs cleared-------------");
		masterLog.doExit();
	}
	
	protected Logging(String logName, LoggerType type, DisplayEntity ta, ReportingEntity reporter)
	{
		this.name = logName;
		logOutput = new ByteArrayOutputStream();
		logOutputStamped = new ByteArrayOutputStream();
		
		if(type == null)
			wrapperType = defaultLoggerWrapper;
		else
			wrapperType = type;
		switch(wrapperType)
		{
		case JAVA:
			logger = new JavaLogWrapper(logName);
			break;
		case LOG4J:
			logger = new Log4JWrapper(logName);
			break;
		}
		
		if(logger == null)
			throw (new IllegalStateException());
		
		logger.setLevel(logLevel);
		// level, message: for TextArea
		logger.addDestination("%-5p \t %m%n", logOutput);
		// date level name message (no new line): for reporting (also, obscure reference)
		logger.addDestination(AWESOME_SEPARATOR + "%d{HH:mm:ss:SSSS} %-5p [" + logName + "]:\t %m" + AWESOME_SEPARATOR, logOutputStamped);
		// priority (level), name, message, line break: for console
		switch(wrapperType)
		{
		case LOG4J:
			logger.addDestination("%-5p [" + logName + "]:\t %m%n", System.out);
			break;
		case JAVA:
			logger.addDestination(null, System.out);
			break;
		}
		
		logDisplay = ta;
		
		externalReporter = reporter;
		
		if((logDisplay != null) || (externalReporter != null))
		{
			// logger.trace("setting and starting timer...");
			logTimer = new Timer();
			logTimer.schedule(new TimerTask() {
				@Override
				public void run()
				{
					updateLogText();
				}
			}, 0, logUpdateDelay);
		}
	}
	
	protected Log getLog()
	{
		return logger;
	}
	
	protected void updateLogText()
	{
		int cSize = logOutput.size();
		if((logDisplay != null) && (logSize != cSize))
		{
			// logger.trace("updating text area...");
			logDisplay.output(logOutput.toString());
			logSize = cSize;
		}
		timeToNextReport -= logUpdateDelay;
		if(timeToNextReport <= 0)
		{
			timeToNextReport = reportUpdateDelay;
			updateReport();
		}
	}
	
	protected void updateReport()
	{
		int cSize2 = logOutputStamped.size();
		if((externalReporter != null) && (cSize2 != lastUpdatedSize))
		{
			// logger.trace("updating reporter...");
			byte[] content = logOutputStamped.toByteArray();
			byte[] update = copyOfRange(content, lastUpdatedSize, cSize2);
			if(externalReporter.report(new String(update).trim()))
				lastUpdatedSize = cSize2;
		}
	}
	
	private static byte[] copyOfRange(byte[] content, int start, int end)
	{
		int length = end - start;
		byte[] returnArray = new byte[length];
		
		for(int i = 0; i < length; ++i)
			returnArray[i] = content[start + i];
		
		return returnArray;
	}
	
	protected void doexit()
	{
		if(logTimer != null)
			logTimer.cancel();
	}
}