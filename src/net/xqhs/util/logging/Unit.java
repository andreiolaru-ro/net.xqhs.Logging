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
package net.xqhs.util.logging;

import java.util.HashSet;
import java.util.Set;

import net.xqhs.util.config.Config;
import net.xqhs.util.logging.Debug.DebugItem;
import net.xqhs.util.logging.LogWrapper.LoggerType;
import net.xqhs.util.logging.Logger.Level;
import net.xqhs.util.logging.output.LogOutput;
import net.xqhs.util.logging.wrappers.LogWrapperFactory;

/**
 * The Unit class should be extended by classes in which logging primitives should be available without creating a
 * specific object for logging. I.e. it will be possible to call primitives directly (e.g. <code>le(message);</code>).
 * Logging primitives will be <code>protected</code>.
 * <p>
 * It is characterized by the <code>unitName</code>, which also gives the name of the log. If the configured name is
 * <code>null</code>, but the instance overrides the <code>getDefaultUnitName()</code> method, then the value returned
 * by that method will be used. If the name is <code>null</code> and there is no override of
 * <code>getDefaultUnitName()</code>, no log will be created and the elements in the configuration will be of no effect.
 * If the name is <code>DEFAULT_UNIT_NAME</code>, the name of the class will be used. In any other case, the given unit
 * name will be used and the log name will be computed accordingly -- will be the equal to the unit name or, if
 * {@link #setUnitName(String, boolean, boolean)} is called, the name of the class will be added.
 * <p>
 * The actual {@link LogWrapper} instance is created when a logging message is posted, when an output is added, or when
 * the {@link #buildLog()} method is called.
 * <p>
 * Although not abstract, the Unit cannot be used as a member of a class, as no logging functions are available
 * publicly. This purpose is fulfilled by {@link UnitComponent}.
 * <p>
 * {@link Unit} only offers the reduced ('simple') set of primitives specified by the {@link Logger} interface. It does
 * not implement the interface however, as that would require the methods to become public.
 * <p>
 * For the "classic" logging methods names, use {@link UnitExt}.
 * <p>
 * The class extends {@link Config}, so extending classes can use the features offered by {@link Config}. In order not
 * to interfere with the functionality of extending classes, the {@link Unit} never {@link #lock}s the configuration.
 * This class implements a {@link #lockedR()} method with the same role as {@link #locked()}, but which transforms the
 * {@link net.xqhs.util.config.Config.ConfigLockedException} into an error message in the log.
 * <p>
 * The sole purpose of the {@link Unit} layer of an instance is to handle logging.
 * 
 * @author Andrei Olaru
 * 
 */
public class Unit extends Config {
	/**
	 * The value that should be used to state that the name of the unit should be computed depending on the class name.
	 */
	public final static String DEFAULT_UNIT_NAME = "theDefaulUnitName";
	
	//////////////// performance mode
	@SuppressWarnings("javadoc")
	static class LogEntry {
		LogWrapper	log;
		Level		level;
		String		message;
		Object[]	objects;
		
		public LogEntry(LogWrapper l, Level lev, String msg, Object[] objs) {
			log = l;
			level = lev;
			message = msg;
			objects = objs;
		}
	}
	
	//////////////// log properties
	/**
	 * The name of the {@link Unit}. See {@link Unit} for details.
	 */
	String		unitName			= null;
	/**
	 * The name that will be given to the log, may be the same with <code>unitName</code>, if that one is not
	 * <code>null</code>. See {@link Unit} for details.
	 */
	String		logName				= null;
	/**
	 * If <code>true</code>, then if another log exists with the same name, an error will be produced. If
	 * <code>false</code> and another log with the same name exists, that log will be used.
	 * <p>
	 * <b>Note:</b> this functionality is not currently implemented.
	 */
	boolean		ensureNew			= false;
	/**
	 * The current level of the log. This may be influenced by the level of parent unit.
	 */
	Level		level				= null;
	/**
	 * Enables performance mode for this log.
	 */
	boolean		performanceMode		= false;
	/**
	 * The level that has been explicitly set by a {@link #setLogLevel(Level)} call for this instance.
	 */
	Level		properLevel			= null;
	/**
	 * A highlighted unit will produce messages shown differently from units which are not highlighted.
	 */
	boolean		highlighted			= false;
	/**
	 * The type of the logging wrapper that will be used.
	 */
	LoggerType	loggerWrapperType	= null;
	/**
	 * The parent units, which may influence the log level or highlighting.
	 */
	Set<Unit>	parentUnits			= new HashSet<>();
	/**
	 * The units having this unit as parent.
	 */
	Set<Unit>	childrenUnits		= new HashSet<>();
	/**
	 * The {@link LogWrapper} that will be used for logging.
	 */
	LogWrapper	log					= null;
	
	/**
	 * This method is meant to be overridden in inheriting classes, so as to give the default name for units of that
	 * type. It is meant to not be static, but the particular implementation in {@link Unit} is not dynamic.
	 * <p>
	 * This member offers a simpler way to give the default unit name, by reinitializing it. It also offers a simple
	 * switch to switch the log level to the default level.
	 * <p>
	 * WARNING: making this dynamic (return different values at different times) is useless; the method will be called
	 * <b>only</b> when the unit is constructed and the name returned will remain set.
	 * 
	 * @return the default unit name for an instance of this class
	 */
	@SuppressWarnings("static-method")
	protected String getDefaultUnitName() {
		return DEFAULT_UNIT_NAME;
	}
	
	/**
	 * Constructs a new {@link Unit}.
	 */
	public Unit() {
		super();
		addParentUnit(MasterLog.masterLog);
	}
	
	/**
	 * Calls the {@link Config#locked()} method and if an exception is thrown it posts an error message to the log.
	 * 
	 * @return <code>true</code> if the configuration was locked, <code>false</code> otherwise.
	 */
	public boolean lockedR() {
		try {
			super.locked();
		} catch(ConfigLockedException e) {
			le(e.toString());
			return true;
		}
		return false;
	}
	
	/**
	 * Constructs a {@link Unit} according to the previously given parameters. More precisely, creates the log,
	 * according to the current configuration.
	 * 
	 * @return the unit itself.
	 */
	protected Unit buildLog() {
		if(log != null)
			return this;
		if(unitName == null || logName == null)
			setUnitName(unitName);
		
		if(unitName == null || logName == null)
			return this;
		if(loggerWrapperType == null)
			loggerWrapperType = Logger.DEFAULT_LOGGER_WRAPPER;
		log = LogWrapperFactory.getLogWrapper(loggerWrapperType, logName);
		setLogLevelInternal(level);
		log.setHighlighted(highlighted);
		if(MasterLog.GLOBAL_PERFORMANCE_MODE)
			setPerformanceMode(true);
		MasterLog.masterLog.lock();
		// MasterLog.masterLog.lf("new log [] (now [] logs).", unitName, nlogs));
		return this;
	}
	
	/**
	 * Sets the name of the unit (and therefore of the log). See {@link Unit} for details on naming.
	 * 
	 * @param name
	 *            - the desired name of the unit
	 * @return the instance itself
	 */
	protected Unit setUnitName(String name) {
		return setUnitName(name, false, false, false);
	}
	
	/**
	 * Sets the name of the unit (and therefore of the log). See {@link Unit} for details on naming.
	 * <p>
	 * In this version of the method, the name of the class will be present in the name of this log, also according to
	 * the other parameters.
	 * 
	 * @param name
	 *            - the desired name of the unit
	 * @param postfix
	 *            - if <code>true</code>, the name of the class will be concatenated after the name of the unit;
	 *            otherwise, before
	 * @param useLongClassName
	 *            - if <code>true</code>, the complete name of the class will be used (including the package).
	 *            Otherwise, only the name obtained by <code>Class.getSimpleName()</code> is used.
	 * @return the instance itself
	 */
	protected Unit setUnitName(String name, boolean postfix, boolean useLongClassName) {
		return setUnitName(name, true, postfix, useLongClassName);
	}
	
	/**
	 * Aggregates parameters from the two other version of the method. See {@link Unit} and the calling methods for
	 * details.
	 * 
	 * @param name
	 *            - the configured name of the unit.
	 * @param addClassName
	 *            - if <code>true</code>, the name of this class will be added to the name of the log.
	 * @param postfix
	 *            - if <code>true</code>, the name of the class will be added after the name of the unit; if
	 *            <code>false</code>, it will be added before.
	 * @param useLongClassName
	 *            - if <code>true</code>, the canonical name of the class will be used (returned by
	 *            <code>getCanonicalName()</code> ; if false, the simple name (returned by <code>getSimpleName()</code>
	 *            ).
	 * @return the instance itself.
	 */
	private Unit setUnitName(String name, boolean addClassName, boolean postfix, boolean useLongClassName) {
		if(lockedR())
			return this;
		
		unitName = name; // special cases for the unit name (default unit name) below
		
		if((unitName == null) && (!DEFAULT_UNIT_NAME.equals(getDefaultUnitName())))
			// the inheriting class has overridden the getDefaultUnitName() method
			unitName = getDefaultUnitName(); // this works around the setter, which now has been locked
			
		if(DEFAULT_UNIT_NAME.equals(unitName)) { // class name should be used
			unitName = makeClassName(useLongClassName);
			logName = unitName;
		}
		else if(addClassName)
			if(postfix)
				logName = unitName + makeClassName(useLongClassName);
			else
				logName = makeClassName(useLongClassName) + unitName;
		else
			logName = unitName;
		if((logName != null) && (level == null))
			// set default level
			setLogLevel(MasterLog.defaultLevel);
		
		return this;
	}
	
	/**
	 * Gets the name of the current class, using either the canonical name or the simple name, depending on the
	 * argument.
	 * 
	 * @param classNameLong
	 *            - if <code>true</code>, the canonical name is used; the simple name otherwise.
	 * @return the name of the class.
	 */
	private String makeClassName(boolean classNameLong) {
		return (classNameLong ? this.getClass().getCanonicalName() : this.getClass().getSimpleName());
	}
	
	/**
	 * If called, when the log is created, if another log exists with the same name, an error will be produced.
	 * Otherwise, if another log with the same name already exists, that log will be used.
	 * <p>
	 * <b>Note:</b> this functionality is not currently implemented.
	 * 
	 * @return the instance itself.
	 */
	protected Unit setLogEnsureNew() {
		if(lockedR())
			return this;
		ensureNew = true;
		return this;
	}
	
	/**
	 * Sets the type of the logging infrastructure and wrapper used by this unit.
	 * 
	 * @param loggerType
	 *            - the type of logging infrastructure, as one of {@link LoggerType}.
	 * @return the instance itself.
	 */
	protected Unit setLoggerType(LoggerType loggerType) {
		if(loggerType == null)
			throw new IllegalArgumentException("Given logger type is null");
		if(lockedR())
			return this;
		loggerWrapperType = loggerType;
		return this;
	}
	
	/**
	 * Sets the level (and also its proper level) of the log, as one of {@link Level}. Messages below this level will
	 * not be shown in the output.
	 * 
	 * @param logLevel
	 *            - the level.
	 * @return the instance itself.
	 */
	protected Unit setLogLevel(Level logLevel) {
		properLevel = logLevel;
		setLogLevelInternal(logLevel);
		for(Unit child : childrenUnits)
			child.setLogLevelInternal(logLevel);
		return this;
	}
	
	/**
	 * Used internally to set the log level without changing the proper level. This is meant to be called by parent
	 * units to change the level when the parent changes level.
	 * 
	 * @param logLevel
	 *            - the level.
	 */
	void setLogLevelInternal(Level logLevel) {
		level = logLevel;
		if(log != null)
			log.setLevel(logLevel);
	}
	
	/**
	 * Sets this log as highlighted. THe exact method of highlighting depends on the implementation of the
	 * {@link LogWrapper}.
	 * 
	 * @return the unit itself.
	 */
	protected Unit setHighlighted() {
		highlighted = true;
		if(log != null)
			log.setHighlighted(highlighted);
		return this;
	}
	
	/**
	 * Sets this log as not highlighted.
	 * 
	 * @see #setHighlighted()
	 * 
	 * @return the unit itself.
	 */
	protected Unit setNotHighlighted() {
		highlighted = false;
		if(log != null)
			log.setHighlighted(highlighted);
		return this;
	}
	
	protected Unit setPerformanceMode(boolean performance) {
		performanceMode = performance;
		if(performanceMode)
			MasterLog.enablePerformanceModeTools();
		return this;
	}
	
	/**
	 * Creates a link between this unit and a parent unit. The parent unit may influence the level and the highlighting.
	 * <p>
	 * By default, all units have the {@link MasterLog} internal log as parent.
	 * 
	 * @param parent
	 *            - the parent {@link Unit}.
	 * @return the unit itself.
	 */
	protected Unit addParentUnit(Unit parent) {
		if(parent == null)
			return this;
		if(parentUnits.add(parent)) {
			parent.registerChild(this);
			if(level != null && level.displayWith(parent.level))
				setLogLevelInternal(parent.level);
		}
		return this;
	}
	
	/**
	 * Removes all links to parent units.
	 * 
	 * @return the unit itself.
	 */
	protected Unit removeAllParentUnits() {
		for(Unit unit : parentUnits)
			unit.unregisterChild(this);
		parentUnits.clear();
		return this;
	}
	
	/**
	 * Internal method to create a link between a unit and its child.
	 * 
	 * @param child
	 *            - the child unit.
	 * @return <code>true</code> if the child had nod been registered before.
	 */
	boolean registerChild(Unit child) {
		return childrenUnits.add(child);
	}
	
	/**
	 * Internal method to remove the link between a unit and its child.
	 * 
	 * @param child
	 *            - the child unit.
	 * @return <code>true</code> if the child had been indeed registered.
	 */
	boolean unregisterChild(Unit child) {
		return childrenUnits.remove(child);
	}
	
	/**
	 * Adds an output to the log.
	 * <p>
	 * If there have been previous log messages, it may be that a default output has been already added.
	 * 
	 * @see LogOutput
	 * 
	 * @param logOutput
	 *            - the output to add.
	 * @return the unit itself.
	 */
	protected Unit addOutput(LogOutput logOutput) {
		buildLog();
		log.addOutput(logOutput);
		return this;
	}
	
	/**
	 * Removes a log output.
	 * <p>
	 * There is no effect if the log has not been built or has been already destroyed.
	 * 
	 * @param logOutput
	 *            - the output to remove.
	 * @return the unit itself.
	 */
	protected Unit removeOutput(LogOutput logOutput) {
		if(log != null)
			log.removeOutput(logOutput);
		return this;
	}
	
	/**
	 * Removes all outputs of the log.
	 * <p>
	 * There is no effect if the log has not been built or has been already destroyed.
	 * 
	 * @return the unit itself.
	 */
	protected Unit removeAllOutputs() {
		if(log != null)
			log.removeAllOutputs();
		return this;
	}
	
	/**
	 * This method is only accessible inside the instance, except if for good reasons an extending class makes it
	 * public.
	 * 
	 * @return the {@link Unit} name, as given to or set in the constructor.
	 */
	protected String getUnitName() {
		return unitName;
	}
	
	/**
	 * Instructs the Unit to exit. This method is only accessible inside the instance, except if for good reasons an
	 * extending class makes it public.
	 * <p>
	 * As the Unit layer only manages logging, the only thing that happens is that the log exits (if any).
	 */
	protected void doExit() {
		l(Level.INFO, "log exit.");
		if(log != null) {
			log.exit();
			log = null;
		}
		if(MasterLog.masterLog == this) {
			MasterLog.closePerformaceElements();
		}
	}
	
	/**
	 * Post an error message. See {@link Logger}.
	 * 
	 * @param message
	 *            : the message to display
	 * @param arguments
	 *            : arguments to insert into the message. See {@link Logger#le}.
	 */
	protected void le(String message, Object... arguments) {
		l(Level.ERROR, message, arguments);
	}
	
	/**
	 * Post a warning message. See {@link Logger}.
	 * 
	 * @param message
	 *            : the message to display
	 * @param arguments
	 *            : arguments to insert into the message. See {@link Logger#lw}.
	 */
	protected void lw(String message, Object... arguments) {
		l(Level.WARN, message, arguments);
	}
	
	/**
	 * Post an informative message. See {@link Logger}.
	 * 
	 * @param message
	 *            : the message to display
	 * @param arguments
	 *            : arguments to insert into the message. See {@link Logger#li}.
	 */
	protected void li(String message, Object... arguments) {
		l(Level.INFO, message, arguments);
	}
	
	/**
	 * Post a tracing message. See {@link Logger}.
	 * 
	 * @param message
	 *            : the message to display
	 * @param arguments
	 *            : arguments to insert into the message. See {@link Logger#lf}.
	 */
	protected void lf(String message, Object... arguments) {
		l(Level.TRACE, message, arguments);
	}
	
	/**
	 * This method should be used in return statements. It adds a log message just before returning the {@link Object}
	 * in the argument, displaying the {@link Object}. See {@link Logger#lr}.
	 * 
	 * @param ret
	 *            : the {@link Object} to return.
	 * @return the {@link Object} passed as argument.
	 */
	protected Object lr(Object ret) {
		return lr(ret, null);
	}
	
	/**
	 * This method should be used in return statements. It adds a log message just before returning the {@link Object}
	 * in the argument. See {@link Logger}.
	 * <p>
	 * The {@link Object} in the argument is also put in the log message.
	 * 
	 * @param ret
	 *            : the {@link Object} to return and to display. {@link Level#TRACE} will be used.
	 * @param message
	 *            : the message to display beside the {@link Object}.
	 * @param arguments
	 *            : arguments to insert into the message. See {@link Logger#lr}.
	 * @return the {@link Object} passed as argument.
	 */
	protected Object lr(Object ret, String message, Object... arguments) {
		if(message != null) {
			Object[] args2 = new Object[arguments.length + 1];
			args2[0] = ret;
			int i = 1;
			for(Object o : arguments)
				args2[i++] = o;
			lf("[]: " + message, ret, arguments);
		}
		else
			lf("[]", ret);
		return ret;
	}
	
	/**
	 * This method should be used in return statements for error cases. It adds an error log message just before
	 * returning the value in the first argument. See {@link Logger}.
	 * 
	 * @param ret
	 *            : the value to return.
	 * @param message
	 *            : the message to display. {@link Level#ERROR} will be used.
	 * @param arguments
	 *            : arguments to insert into the message. See {@link Logger#ler}.
	 * @return the {@link Object} passed as argument.
	 */
	protected boolean ler(boolean ret, String message, Object... arguments) {
		le(message, arguments);
		return ret;
	}
	
	/**
	 * This method displays a log message (with the level <code>TRACE</code>) only if the specified {@link DebugItem} is
	 * activated. See {@link Logger}.
	 * 
	 * @param debug
	 *            : the {@link DebugItem}
	 * @param message
	 *            : the log message
	 * @param arguments
	 *            : arguments to insert into the message. See {@link Logger}.
	 */
	protected void dbg(DebugItem debug, String message, Object... arguments) {
		if(debug.toBool())
			lf(message, arguments);
	}
	
	/**
	 * This method calls the underlying logging infrastructure to display a message with the specified level, text, and
	 * parameter objects.
	 * 
	 * @param messageLevel
	 *            - the level of the message.
	 * @param message
	 *            - the text of the message.
	 * @param arguments
	 *            - the objects to be inserted in the placeholders of the message text (see {@link Logger}).
	 */
	protected void l(Level messageLevel, String message, Object... arguments) {
		buildLog();
		if((log != null) && messageLevel.displayWith(level))
			if(performanceMode && MasterLog.processingQueue != null)
				MasterLog.processingQueue.add(new LogEntry(log, messageLevel, message, arguments));
			else
				log.l(messageLevel, compose(message, arguments));
	}
	
	/**
	 * Composes a message with an array of {@link Object} instances. All apparitions of
	 * {@link Logger#ARGUMENT_PLACEHOLDER} will be replaced with results of calls to the <code>toString()</code> methods
	 * of objects, surrounded by {@link Logger#ARGUMENT_BEGIN} and {@link Logger#ARGUMENT_END}. Remaining objects will
	 * be displayed after the message.
	 * 
	 * @param message
	 *            : the message text.
	 * @param objects
	 *            : the objects.
	 * @return the assembled string.
	 */
	protected static String compose(String message, Object[] objects) {
		String[] parts = message.split(Logger.ARGUMENT_PLACEHOLDER, objects.length + 1);
		// there are enough objects for all parts
		// there may be more objects than parts
		String ret = parts[0];
		for(int i = 0; i < parts.length - 1; i++) {
			ret += Logger.ARGUMENT_BEGIN + objects[i] + Logger.ARGUMENT_END;
			ret += parts[i + 1];
		}
		// deal with the rest of the objects
		for(int i = parts.length - 1; i < objects.length; i++)
			ret += Logger.ARGUMENT_BEGIN + objects[i] + Logger.ARGUMENT_END;
		
		return ret;
	}
	
	/**
	 * Use this method to obtain a Logger instance which relays all calls directly to this {@link Unit} instance. This
	 * should be done when a Unit needs to expose its logger to some other instance.
	 * 
	 * @return a {@link LoggerClassic} instance that exposes the logging methods of this {@link Unit}.
	 */
	protected LoggerClassic getLogger() {
		buildLog();
		return new BaseLogger() {
			
			@Override
			public Object lr(Object ret, String message, Object... arguments) {
				return Unit.this.lr(ret, message, arguments);
			}
			
			@Override
			protected void l(Level messageLevel, String message, Object... arguments) {
				Unit.this.l(messageLevel, message, arguments);
			}
		};
	}
}
