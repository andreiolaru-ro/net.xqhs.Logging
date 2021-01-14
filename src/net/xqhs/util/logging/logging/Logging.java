/*******************************************************************************
 * Copyright (C) 2018 Andrei Olaru.
 * 
 * This file is part of Logging.
 * 
 * Logging is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Logging is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Logging.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.xqhs.util.logging.logging;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import net.xqhs.util.logging.DisplayEntity;
import net.xqhs.util.logging.Logger.Level;
import net.xqhs.util.logging.ReportingEntity;
import net.xqhs.util.logging.UnitComponent;
import net.xqhs.util.logging.logging.LogDebug.LogDebugItem;
import net.xqhs.util.logging.logging.LogWrapper.LoggerType;
import net.xqhs.util.logging.wrappers.LogWrapperFactory;

/**
 * Implements the configuring entity and manager for logs. The idea is to have standard logs and wrappers, but with some
 * additional features, that are accessible via static functions of this class, using the Logger object itself as a
 * unique reference.
 * <p>
 * The class contains two parts: the static part for management of logs, and a non-static part that is instantiated for
 * each log, and contains a log wrapper.
 * <p>
 * There are currently three possible destinations for the logging information: the console output is standard; an
 * optional {@link DisplayEntity} may be specified to be updated with the contents of the log immediately after the log
 * entry is posted; a {@link ReportingEntity} may be specified that will be updated with the logging information at
 * regular intervals of time. The output that is sent to the display entity has a simple form, as it is presumed that
 * the display entity is specific to the unit. The output that is sent to the reporting entity is also time-stamped, as
 * it is presumed that the reports reach a remote machine (or some other centralizing entity).
 * <p>
 * A new log is obtained by {@link #getLogger(String, String, DisplayEntity, ReportingEntity, boolean, String, Level)},
 * where the <code>name</code> should be unique and is the name of the log.
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
	
	// // here be the static components of the class, which by being static are unique for the current JVM.
	
	/**
	 * Contains all the currently active logs, identified by their [unit]Name. Active means that a timer is associated
	 * with them.
	 * <p>
	 * All access to the logs field should be synchronized explicitly.
	 */
	protected static Map<String, Logging>	logs					= Collections
			.synchronizedMap(new HashMap<String, Logging>());
	/**
	 * All access to parents should be synchronized. In this source, all accesses take place inside locks on the logs
	 * field.
	 */
	@SuppressWarnings("unused")
	// Type arguments specified for Java 1.6 compatibility.
	protected static Map<String, String>	parents					= new HashMap<String, String>();
	
	/**
	 * A {@link UnitComponent} for logging messages related to the log management.
	 * <p>
	 * Will automatically start and stop when the first log is created / when the last log exists.
	 */
	protected static UnitComponent			masterLog				= new UnitComponent();
	/**
	 * The name for the log held by {@link #masterLog}.
	 */
	protected static String					masterLogName			= "M-log";
	
	// /////////// here be the components of the log
	/**
	 * The wrapper of the actual log. This instance wraps the wrapper, in turn.
	 */
	protected LogWrapper					logger					= null;
	/**
	 * The class of the implementation for the log wrapper.
	 */
	protected String						wrapperClass			= null;
	/**
	 * The type of the implementation for the log wrapper, as an instance of {@link LoggerType}.
	 */
	protected LoggerType					wrapperType				= null;
	/**
	 * The name of the log (may be the same as the name of the containing Unit).
	 */
	protected String						name					= null;
	/**
	 * The current level of the log. Initialized to a default value of {@link Level#ALL}.
	 */
	protected Level							logLevel				= Level.ALL;
	
	// here be the components of the log related to external reporting (to a text area and to a Jade agent, respectively
	/**
	 * Contains the entire output of the log. Version without time stamps and unit name, just level and message.
	 */
	protected ByteArrayOutputStream			logOutput				= null;
	/**
	 * Contains the entire output of the log. Version with time stamp, level, unit name, and message.
	 */
	protected ByteArrayOutputStream			logOutputStamped		= null;
	/**
	 * Used to trace if there have been modifications to the log, before flushing it into the display entity.
	 */
	protected long							logSize					= 0L;
	/**
	 * The {@link DisplayEntity} that will be kept up to date with the contents of the log.
	 */
	protected DisplayEntity					logDisplay				= null;
	/**
	 * The {@link ReportingEntity} to sent logging information to.
	 */
	protected ReportingEntity				externalReporter		= null;
	/**
	 * Timer to update the external views of this log.
	 */
	protected Timer							logTimer				= null;
	/**
	 * Delay at which to update the display entity.
	 */
	protected long							logUpdateDelay			= 250;
	/**
	 * Delay at which to update the reporting entity.
	 */
	protected long							reportUpdateDelay		= 2000;
	/**
	 * Time left to the next report sent to the reporting entity.
	 */
	protected long							timeToNextReport		= 0;
	/**
	 * Cumulative size of the logging information sent so far to the reporting entity.
	 */
	protected int							lastUpdatedSize			= 0;
	
	/**
	 * Retrieves the <code>masterLog</code> that will be used for log messages regarding global log management, for
	 * configuring. Its configuration will be locked once the first log is created.
	 * 
	 * @return the master log
	 */
	public static UnitComponent getMasterLogging()
	{
		return masterLog;
	}
	
	/**
	 * Provides a logger with the given name. If the name is already in use (and <code>ensureNew</code> is
	 * <code>false</code>), the log corresponding to that name will be returned and all other parameters will be
	 * ignored.
	 * 
	 * @param name
	 *            : is the name to be given to, and to identify the log. Should be not <code>null</code> and unique
	 *            among active logs.
	 * @param link
	 *            : the name of another, 'parent', log that this log is linked to, if any; when the parent log will
	 *            close, this log will be closed as well.
	 * @param display
	 *            : the {@link DisplayEntity} to receive the output of the log.
	 * @param reporter
	 *            : the {@link ReportingEntity} to receive the output of the log.
	 * @param ensureNew
	 *            : <code>false</code> if to return an existing log with the same name, if any; <code>true</code> if to
	 *            throw an exception should another log with the same name exist.
	 * @param logWrapperClass
	 *            : the {@link LogWrapper} class to instantiate. If null, the class of {@link #DEFAULT_LOGGER_WRAPPER} is
	 *            chosen. Some class names can also be obtained from calling {@link LoggerType#getClassName()} on
	 *            various values of {@link LoggerType}.
	 * @param level
	 *            : the initial level of the log, as an instance of {@link Level}.
	 * @return A new, configured, {@link LogWrapper} instance; or an existing instance if the same name already existed
	 *         and <code>ensureNew</code> was set to <code>false</code>.
	 * @throws IllegalArgumentException
	 *             : if the name is not new and <code>ensureNew</code> was set to <code>true</code>.
	 */
	public static LogWrapper getLogger(String name, String link, DisplayEntity display, ReportingEntity reporter,
			boolean ensureNew, String logWrapperClass, Level level)
	{
		boolean erred = false;
		int nlogs = -1;
		
		if((masterLog == null) || (masterLog.getUnitName() == null)) // avoid recursion
		{
			masterLog.setUnitName(masterLogName);
			masterLog.lock();
		}
		
		if(name == null)
			throw new IllegalArgumentException(
					"log name cannot be null. Use Unit.DEFAULT_UNIT_NAME for the default name.");
		
		masterLog.dbg(LogDebugItem.D_LOG_MANAGEMENT, "required: [" + name + "]" + (ensureNew ? "[new]" : "")
				+ "; existing: [" + logs.size() + "]: [" + logs + "]");
		
		Logging thelog = new Logging(name, logWrapperClass, display, reporter);
		Logging alreadyPresent = null;
		synchronized(logs)
		{
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
		if(level != null)
			thelog.logger.setLevel(level);
		thelog.logger.l(Level.TRACE, "new log (count now [" + nlogs + "]).");
		return thelog.getLog();
	}
	
	/**
	 * Get the whole output of the log.
	 * 
	 * @param name
	 *            : the name of the log.
	 * @param shortOutput
	 *            : if <code>true</code>, the output does not contain the log name and time stamps (just the level and
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
	 * Closes the log specified by the name, stops the associated timer, and frees the name so it can be reused. The log
	 * will not be flushed (sent as report) before closing.
	 * 
	 * @param name
	 *            : the name of the log to be freed.
	 */
	public static void exitLogger(String name)
	{
		exitLogger(name, false);
	}
	
	/**
	 * Closes the log specified by the name (stops the associated timer) and frees the name so it can be reused.
	 * 
	 * @param name
	 *            : the name of the log to be freed.
	 * @param flushFirst
	 *            : if <code>true</code>, a last report will be sent to the reporting entity (if any).
	 */
	public static void exitLogger(String name, boolean flushFirst)
	{
		boolean notpresent = false;
		Logging found = null;
		int nlogs = -1;
		@SuppressWarnings("unused")
		// type argument required by Java 1.6.
		Vector<String> toClose = new Vector<String>(); // logs to which this is parent.
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
				parents.remove(name);
				nlogs = logs.size();
			}
		}
		if(notpresent || (found == null))
			throw new IllegalArgumentException("log not present [" + name + "]");
		found.getLog().l(Level.TRACE, "log out (logs remaining [" + nlogs + "]).");
		for(String logName : toClose)
			exitLogger(logName, flushFirst);
		if(flushFirst)
			found.updateLogText();
		found.doexit();
		
		if(nlogs == 1 && masterLog != null) // this was the last non-master log
			masterLog.doExit();
	}
	
	/**
	 * Resets the maps containing the links to the logs.
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
		
		if(masterLog != null)
		{
			masterLog.lf("--------logs cleared-------------");
			masterLog.doExit();
		}
	}
	
	/**
	 * Constructor for an instance of {@link Logging}.
	 * 
	 * @param logName
	 *            - the name of the log.
	 * @param loggerClass
	 *            - the class of the contained wrapper.
	 * @param display
	 *            - the {@link DisplayEntity} to use.
	 * @param reporter
	 *            - the {@link ReportingEntity} to use.
	 */
	protected Logging(String logName, String loggerClass, DisplayEntity display, ReportingEntity reporter)
	{
		name = logName;
		logOutput = new ByteArrayOutputStream();
		logOutputStamped = new ByteArrayOutputStream();
		
		if(loggerClass == null)
		{
			wrapperType = defaultLoggerWrapper;
		}
		else
		{
			for(LoggerType wrapper : LoggerType.values())
				if(loggerClass.equals(wrapper.getClassName()))
					wrapperType = wrapper;
		}
		
		// instantiate wrapper
		logger = LogWrapperFactory.getLogWrapper(wrapperType, name);
		
		if(logger == null)
			throw (new IllegalStateException());
		
		logger.setLevel(logLevel);
		
		logDisplay = display;
		externalReporter = reporter;
		
		if(logDisplay != null)
			logger.addOutput(0, logOutput);
		if(externalReporter != null)
			logger.addOutput(LogWrapper.INCLUDE_TIMESTAMP & LogWrapper.INCLUDE_NAME & LogWrapper.REPLACE_ENDLINES,
					logOutputStamped);
		logger.addOutput(LogWrapper.INCLUDE_NAME, System.out);
		
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
	
	/**
	 * @return the underlying log wrapper.
	 */
	protected LogWrapper getLog()
	{
		return logger;
	}
	
	/**
	 * Method invoked when the output of the log must be displayed or sent elsewhere. THe call of this method is
	 * triggered by the timer created at construction.
	 */
	protected void updateLogText()
	{
		int cSize = logOutput.size();
		if((logDisplay != null) && (logSize != cSize))
		{
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
	
	/**
	 * Reports any changes in the log to the {@link ReportingEntity}, if any.
	 */
	protected void updateReport()
	{
		int cSize2 = logOutputStamped.size();
		if((externalReporter != null) && (cSize2 != lastUpdatedSize))
		{
			// logger.trace("updating reporter...");
			byte[] content = logOutputStamped.toByteArray();
			byte[] update = Arrays.copyOfRange(content, lastUpdatedSize, cSize2);
			if(externalReporter.report(new String(update).trim()))
				lastUpdatedSize = cSize2;
		}
	}
	
	/**
	 * Exists the log. More specifically, it cancels the update timer ({@link #logTimer}).
	 */
	protected void doexit()
	{
		if(logTimer != null)
			logTimer.cancel();
		logger.exit();
	}
}
