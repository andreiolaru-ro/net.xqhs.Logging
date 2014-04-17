/*******************************************************************************
 * Copyright (C) 2013 Andrei Olaru.
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

import net.xqhs.util.config.Config;
import net.xqhs.util.logging.Debug.DebugItem;
import net.xqhs.util.logging.LoggerSimple.Level;
import net.xqhs.util.logging.logging.LogWrapper;
import net.xqhs.util.logging.logging.LogWrapper.LoggerType;
import net.xqhs.util.logging.logging.Logging;

/**
 * The Unit class should be extended by classes in which logging primitives should be available without calling a
 * specific instance. I.e. it will be possible to call primitives directly (e.g. <code>le(message);</code>). Logging
 * primitives will be protected.
 * <p>
 * It is characterized by the unitName, which also gives the name of the log. If the configured name is
 * <code>null</code>, but the instance overrides the <code>getDefaultUnitName()</code> method, then the value returned
 * by that method will be used. If the name is <code>null</code> and there is no override of
 * <code>getDefaultUnitName()</code>, no log will be created and the elements in the configuration will be of no effect.
 * If the name is <code>DEFAULT_UNIT_NAME</code>, the name of the class will be used. In any other case, the given unit
 * name will be used and the log name will be computed accordingly -- will be the equal to the unit name or, if
 * {@link #setUnitName(String, boolean, boolean)} is called, the name of the class will be added.
 * <p>
 * Although not abstract, the Unit cannot be used as a member of a class, as no logging functions are available
 * publicly. This purpose is fulfilled by {@link UnitComponent}.
 * <p>
 * {@link Unit} only offers the reduced ('simple') set of primitives specified by the {@link LoggerSimple} interface. It
 * does not implement the interface however, as that would require the methods to become public.
 * <p>
 * The class extends {@link Config}, so extending classes can use the features offered by {@link Config}.
 * <p>
 * The sole purpose of the {@link Unit} layer of an instance is to handle logging.
 * 
 * @author Andrei Olaru
 * 
 */
public class Unit extends Config
{
	/**
	 * The value that should be used to state that the name of the unit should be computed depending on the class name.
	 */
	public final static String	DEFAULT_UNIT_NAME	= "theDefaulUnitName";
	/**
	 * The default level for the log, if no other level is set.
	 */
	public final static Level	DEFAULT_LEVEL		= Level.ALL;
	
	/**
	 * The name of the {@link Unit}. See {@link Unit} for details.
	 */
	String						unitName			= null;
	
	/**
	 * The name that will be given to the log. See {@link Unit} for details.
	 */
	String						logName				= null;
	
	/**
	 * If <code>true</code>, then if another log exists with the same name, an error will be produced. If
	 * <code>false</code> and another log with the same name exists, that log will be used.
	 */
	boolean						ensureNew			= false;
	
	/**
	 * The current level of the log.
	 */
	Level						level				= null;
	
	/**
	 * A {@link DisplayEntity} implementation to which to post logging information.
	 */
	DisplayEntity				display				= null;
	/**
	 * A {@link ReportingEntity} implementation to which to post logging information.
	 */
	ReportingEntity				reporter			= null;
	
	/**
	 * The type of the logging wrapper that will be used.
	 */
	LoggerType					loggerWrapperType	= null;
	
	/**
	 * The class of the logging wrapper that will be used (used only if different from the default types specified by
	 * {@link LoggerType}.
	 */
	String						loggerWrapperClass	= null;
	
	/**
	 * Information on linking the behavior of this log to other logs. TODO.
	 */
	UnitLinkData				linkData			= new UnitLinkData();
	
	/**
	 * The {@link LogWrapper} that will be used for logging.
	 */
	LogWrapper					log					= null;
	
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
	protected String getDefaultUnitName()
	{
		return DEFAULT_UNIT_NAME;
	}
	
	/**
	 * Constructs a new {@link Unit}.
	 */
	public Unit()
	{
		super();
	}
	
	/**
	 * Calls the {@link Config#locked()} method and if an exception is thrown it posts an error message to the log.
	 * 
	 * @return <code>true</code> if the configuration was locked, <code>false</code> otherwise.
	 */
	public boolean lockedR()
	{
		try
		{
			super.locked();
		} catch(ConfigLockedException e)
		{
			le(e.toString());
			return true;
		}
		return false;
	}
	
	/**
	 * Constructs a {@link Unit} according to the previously given parameters. More precisely, creates the log,
	 * according to the current configuration.
	 */
	@Override
	public Unit lock()
	{
		if(unitName == null || logName == null)
			setUnitName(unitName);
		
		super.lock();
		
		if(unitName != null && logName != null)
			try
			{
				log = Logging.getLogger(logName, linkData.parentLogName, display, reporter, ensureNew,
						loggerWrapperClass, level);
			} catch(ClassNotFoundException e)
			{
				throw new IllegalArgumentException("Failed to instantiate logging wrapper class.", e);
			}
		
		return this;
	}
	
	/**
	 * Sets the name of the unit (and therefore of the log). See {@link Unit} for details on naming.
	 * 
	 * @param name
	 *            - the desired name of the unit
	 * @return the instance itself
	 */
	public Unit setUnitName(String name)
	{
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
	public Unit setUnitName(String name, boolean postfix, boolean useLongClassName)
	{
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
	private Unit setUnitName(String name, boolean addClassName, boolean postfix, boolean useLongClassName)
	{
		if(lockedR())
			return this;
		
		unitName = name; // special cases for the unit name (default unit name) below
		
		if((unitName == null) && (!DEFAULT_UNIT_NAME.equals(getDefaultUnitName())))
			// the inheriting class has overridden the getDefaultUnitName() method
			unitName = getDefaultUnitName(); // this works around the setter, which now has been locked
			
		if(DEFAULT_UNIT_NAME.equals(unitName))
		{ // class name should be used
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
			setLogLevel(DEFAULT_LEVEL);
		
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
	private String makeClassName(boolean classNameLong)
	{
		return (classNameLong ? this.getClass().getCanonicalName() : this.getClass().getSimpleName());
	}
	
	/**
	 * If called, when the log is created, if another log exists with the same name, an error will be produced.
	 * Otherwise, if another log with the same name already exists, that log will be used.
	 * 
	 * @return the instance itself.
	 */
	public Unit setLogEnsureNew()
	{
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
	public Unit setLoggerType(LoggerType loggerType)
	{
		if(loggerType == null)
			throw new IllegalArgumentException("Given logger type is null");
		return setLoggerTypeClass(loggerType, null);
	}
	
	/**
	 * Sets the class of the wrapper for the underlying logger. The class must implement {@link LogWrapper}.
	 * 
	 * @param className
	 *            - the name of the class.
	 * @return the instance itself.
	 */
	public Unit setLoggerClass(String className)
	{
		if(className == null)
			throw new IllegalArgumentException("Given class is null");
		return setLoggerTypeClass(null, className);
	}
	
	/**
	 * Setter for the logger type / logger wrapper class. They are interdependent and therefore must be set
	 * simultaneously. The arguments, however, cannot be both not null at the same time. FIXME
	 * 
	 * @param loggerType
	 *            -- the type of the wrapper, as one of {@link LoggerType}.
	 * @param className
	 *            -- the class of a {@link LogWrapper} implementation.
	 * @return the instance itself.
	 */
	protected Unit setLoggerTypeClass(LoggerType loggerType, String className)
	{
		if(lockedR())
			return this;
		if((loggerType != null) && (className != null))
			throw new IllegalArgumentException("cannot set the logger type and the class name at the same time.");
		if(loggerType != null)
		{
			loggerWrapperType = loggerType;
			if(loggerWrapperType != LoggerType.OTHER)
				loggerWrapperClass = loggerType.getClassName();
		}
		if(className != null)
		{
			loggerWrapperClass = className;
			for(LoggerType wrapper : LoggerType.values())
				if(className.equals(wrapper.getClassName()))
					loggerWrapperType = wrapper;
			loggerWrapperType = LoggerType.OTHER;
		}
		return this;
	}
	
	/**
	 * Sets the level of the log, as one of {@link Level}. Messages below this level will not be shown in the output.
	 * 
	 * @param logLevel
	 *            - the level.
	 * @return the instance itself.
	 */
	public Unit setLogLevel(Level logLevel)
	{
		level = logLevel;
		return this;
	}
	
	/**
	 * Sets the name of the parent log.
	 * 
	 * @param parentLogName
	 *            - the name of the parent log.
	 * @return the instance itself.
	 */
	public Unit setLink(String parentLogName)
	{
		return setLink(new UnitLinkData().setparentLogName(parentLogName));
	}
	
	/**
	 * Sets the information on linking this unit to another log.
	 * 
	 * @param unitLinkData
	 *            - the {@link UnitLinkData} instance configuring the link.
	 * @return the instance itself.
	 */
	public Unit setLink(UnitLinkData unitLinkData)
	{
		if(lockedR())
			return this;
		linkData = unitLinkData;
		return this;
	}
	
	/**
	 * Sets the {@link DisplayEntity} to receive updates form the log.
	 * 
	 * @param logDisplay
	 *            - the display.
	 * @return the instance itself.
	 */
	public Unit setLogDisplay(DisplayEntity logDisplay)
	{
		display = logDisplay;
		return this;
	}
	
	/**
	 * Sets the {@link ReportingEntity} to receive updates from the log.
	 * 
	 * @param reportingEntity
	 *            - the reporter.
	 * @return the instance itself.
	 */
	public Unit setLogReporter(ReportingEntity reportingEntity)
	{
		reporter = reportingEntity;
		return this;
	}
	
	/**
	 * This method is only accessible inside the instance, except if for good reasons an extending class makes it
	 * public.
	 * 
	 * @return the {@link Unit} name, as given to or set in the constructor.
	 */
	protected String getUnitName()
	{
		return unitName;
	}
	
	/**
	 * Instructs the Unit to exit. This method is only accessible inside the instance, except if for good reasons an
	 * extending class makes it public.
	 * <p>
	 * As the Unit layer only manages logging, the only thing that happens is that the log exits (if any).
	 */
	protected void doExit()
	{
		if(log != null && logName != null)
		{
			Logging.exitLogger(logName);
			log = null;
			logName = null;
		}
		unitName = null;
	}
	
	/**
	 * Post an error message. See {@link LoggerSimple}.
	 * 
	 * @param message
	 *            : the message to display
	 * @param arguments
	 *            : arguments to insert into the message. See {@link LoggerSimple}.
	 */
	protected void le(String message, Object... arguments)
	{
		l(Level.ERROR, message, arguments);
	}
	
	/**
	 * Post a warning message. See {@link LoggerSimple}.
	 * 
	 * @param message
	 *            : the message to display
	 * @param arguments
	 *            : arguments to insert into the message. See {@link LoggerSimple}.
	 */
	protected void lw(String message, Object... arguments)
	{
		l(Level.WARN, message, arguments);
	}
	
	/**
	 * Post an informative message. See {@link LoggerSimple}.
	 * 
	 * @param message
	 *            : the message to display
	 * @param arguments
	 *            : arguments to insert into the message. See {@link LoggerSimple}.
	 */
	protected void li(String message, Object... arguments)
	{
		l(Level.INFO, message, arguments);
	}
	
	/**
	 * Post a tracing message. See {@link LoggerSimple}.
	 * 
	 * @param message
	 *            : the message to display
	 * @param arguments
	 *            : arguments to insert into the message. See {@link LoggerSimple}.
	 */
	protected void lf(String message, Object... arguments)
	{
		l(Level.TRACE, message, arguments);
	}
	
	/**
	 * This method should be used in return statements. It adds a log message just before returning the {@link Object}
	 * in the argument, displaying the {@link Object}. See {@link LoggerSimple}.
	 * 
	 * @param ret
	 *            : the {@link Object} to return.
	 * @return the {@link Object} passed as argument.
	 */
	protected Object lr(Object ret)
	{
		return lr(ret, null);
	}
	
	/**
	 * This method should be used in return statements. It adds a log message just before returning the {@link Object}
	 * in the argument. See {@link LoggerSimple}.
	 * <p>
	 * The {@link Object} in the argument is also put in the log message.
	 * 
	 * @param ret
	 *            : the {@link Object} to return and to display.
	 * @param message
	 *            : the message to display beside the {@link Object}.
	 * @param arguments
	 *            : arguments to insert into the message. See {@link LoggerSimple}.
	 * @return the {@link Object} passed as argument.
	 */
	protected Object lr(Object ret, String message, Object... arguments)
	{
		lf(LoggerSimple.ARGUMENT_BEGIN + ((ret != null) ? ret.toString() : "null") + LoggerSimple.ARGUMENT_END
				+ (message != null ? ":[" + message + "]" : ""));
		return ret;
	}
	
	/**
	 * This method displays a log message (with the level <code>TRACE</code>) only if the specified {@link DebugItem} is
	 * activated. See {@link LoggerSimple}.
	 * 
	 * @param debug
	 *            : the {@link DebugItem}
	 * @param message
	 *            : the log message
	 * @param arguments
	 *            : arguments to insert into the message. See {@link LoggerSimple}.
	 */
	protected void dbg(DebugItem debug, String message, Object... arguments)
	{
		if(debug.toBool())
			lf(message);
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
	 *            - the objects to be inserted in the placeholders of the message text (see {@link LoggerSimple}).
	 */
	protected void l(Level messageLevel, String message, Object... arguments)
	{
		ensureLocked();
		if((log != null) && messageLevel.displayWith(level))
			log.l(messageLevel, compose(message, arguments));
	}
	
	/**
	 * Composes a message with an array of {@link Object} instances. All apparitions of
	 * {@link LoggerSimple#ARGUMENT_PLACEHOLDER} will be replaced with results of calls to the <code>toString()</code>
	 * methods of objects, surrounded by {@link LoggerSimple#ARGUMENT_BEGIN} and {@link LoggerSimple#ARGUMENT_END}.
	 * Remaining objects will be displayed after the message.
	 * 
	 * @param message
	 *            : the message text.
	 * @param objects
	 *            : the objects.
	 * @return the assembled string.
	 */
	protected static String compose(String message, Object[] objects)
	{
		String[] parts = message.split(LoggerSimple.ARGUMENT_PLACEHOLDER, objects.length + 1);
		// there are enough objects for all parts
		// there may be more objects than parts
		String ret = parts[0];
		for(int i = 0; i < parts.length - 1; i++)
		{
			ret += LoggerSimple.ARGUMENT_BEGIN + objects[i] + LoggerSimple.ARGUMENT_END;
			ret += parts[i + 1];
		}
		// deal with the rest of the objects
		for(int i = parts.length - 1; i < objects.length; i++)
			ret += LoggerSimple.ARGUMENT_BEGIN + objects[i] + LoggerSimple.ARGUMENT_END;
		
		return ret;
	}
}
