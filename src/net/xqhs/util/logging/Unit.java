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
import net.xqhs.util.logging.Log.LoggerType;
import net.xqhs.util.logging.Logger.Level;
import net.xqhs.util.logging.Logging.DisplayEntity;
import net.xqhs.util.logging.Logging.ReportingEntity;

/**
 * The Unit class should be extended by any class using a log obtained from {@link Logging}.
 * <p>
 * It is characterized by the unitName, which also gives the name of (and helps refer) the log.
 * <p>
 * Although not abstract, the Unit should not be used as a member of a class, as no logging functions are available.
 * This purpose is fulfilled by {@link UnitComponent}.
 * <p>
 * On construction, the name can be the <code>DEFAULT_UNIT_NAME</code>, in which case the class name is used;
 * <code>null</code>, in which case a log is not created; or another name that should be unique across the JVM.
 * <p>
 * It is guaranteed that after construction the Unit has a non-null, valid {@link Config}.
 * <p>
 * The unit also serves as access to logging functions offered by the contained {@link Log}.
 * <p>
 * The sole purpose of the {@link Unit} layer of an instance is to handle logging.
 * 
 * @author Andrei Olaru
 * 
 */
public class Unit extends Config
{
	public final static String	DEFAULT_UNIT_NAME	= "theDefaulUnitName";
	public final static Level	DEFAULT_LEVEL		= Level.ALL;
	
	/**
	 * The name of the {@link Unit}. See {@link Unit} for details.
	 */
	String						unitName			= null;
	
	String						logName				= null;
	
	/**
	 * If <code>true</code>, then if another log exists with the same name, an error will be produced. If
	 * <code>false</code> and another log with the same name exists, that log will be used.
	 */
	boolean						ensureNew			= false;
	
	Level						level				= null;
	
	DisplayEntity				display				= null;
	ReportingEntity				reporter			= null;
	
	LoggerType					loggerWrapperType	= null;
	
	UnitLinkData				linkData			= new UnitLinkData();
	
	/**
	 * The {@link Log} that will be used for logging.
	 */
	Log							log					= null;
	
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
	
	@Override
	public void locked()
	{
		try
		{
			super.locked();
		} catch(ConfigLockedException e)
		{
			le(e.toString());
		}
	}
	
	/**
	 * Constructs a {@link Unit} according to the previously given parameters.
	 * <p>
	 * If the configured name is <code>null</code>, but the instance overrides the <code>getDefaultUnitName()</code>
	 * method, then the value returned by that method will be used.
	 * <p>
	 * If the name is <code>null</code> and there is no override of <code>getDefaultUnitName()</code>, no log will be
	 * created and the elements in the configuration will be of no effect.
	 * <p>
	 * If the name is <code>DEFAULT_UNIT_NAME</code>, the name of the class will be used.
	 * <p>
	 * In all the special cases above, the name in the configuration will be modified to the new value.
	 * <p>
	 * See {@link UnitConfigData} for other settings that can be used.
	 */
	@Override
	public Unit lock()
	{
		if(unitName == null || logName == null)
			setUnitName(unitName);
		
		super.lock();
		
		if(unitName != null && logName != null)
		{
			log = Logging.getLogger(logName, linkData.parentLogName, display, reporter, ensureNew, loggerWrapperType);
			if(level != null)
				log.setLevel(level);
			// FIXME: level setting actually happens after the new log logging message
		}
		
		return this;
	}
	
	/**
	 * Produces the actual name of the log, based on the unit name.
	 * <p>
	 * This only differs from the unit name if <code>config.addClassName</code> is specified.
	 * 
	 * @return the name of the log.
	 */
	private String makeLogName(String name, boolean addClassName, boolean postfix, boolean useLongClassName)
	{
		String className = makeClassName(useLongClassName);
		if(addClassName)
		{
			if(postfix)
				name = name + className;
			else
				name = className + name;
		}
		return name;
	}
	
	private String makeClassName(boolean classNameLong)
	{
		return (classNameLong ? this.getClass().getCanonicalName() : this.getClass().getSimpleName());
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
	 * Aggregates parameters from the two other version of the method.
	 * 
	 * @param name
	 * @param addClassName
	 * @param postfix
	 * @param useLongClassName
	 * @return
	 */
	private Unit setUnitName(String name, boolean addClassName, boolean postfix, boolean useLongClassName)
	{
		locked();
		
		unitName = name; // special cases for the unit name (default unit name) below
		
		if(unitName == null && getDefaultUnitName() != DEFAULT_UNIT_NAME)
		{// the inheriting class has overridden the getDefaultUnitName() method
			unitName = getDefaultUnitName(); // this works around the setter, which now has been locked
			setLogLevel(DEFAULT_LEVEL);
		}
		
		if(unitName == DEFAULT_UNIT_NAME)
			unitName = makeClassName(useLongClassName);
		
		logName = makeLogName(name, addClassName, postfix, useLongClassName);
		
		return this;
	}
	
	/**
	 * If called, then if another log exists with the same name, an error will be produced. Otherwise, if another log
	 * with the same name exists, that log will be used.
	 */
	public Unit setLogEnsureNew()
	{
		locked();
		ensureNew = true;
		return this;
	}
	
	public Unit setLoggingLink(String parentLogName)
	{
		return setLink(new UnitLinkData().setparentLogName(parentLogName));
	}
	
	public Unit setLogType(LoggerType loggerType)
	{
		locked();
		this.loggerWrapperType = loggerType;
		return this;
	}
	
	public Unit setLink(String parentLogName)
	{
		return setLink(new UnitLinkData().setparentLogName(parentLogName));
	}
	
	public Unit setLink(UnitLinkData unitLinkData)
	{
		locked();
		this.linkData = unitLinkData;
		return this;
	}
	
	public Unit setLogDisplay(DisplayEntity logDisplay)
	{
		this.display = logDisplay;
		return this;
	}
	
	public Unit setLogReporter(ReportingEntity reportingEntity)
	{
		this.reporter = reportingEntity;
		return this;
	}
	
	public Unit setLogLevel(Level logLevel)
	{
		this.level = logLevel;
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
	 * As the Unit layer only manages logging, the only thing that happens is that the log exists (if any).
	 */
	protected void doExit()
	{
		if(log != null && unitName != null && logName != null)
		{
			Logging.exitLogger(logName);
			log = null;
		}
	}
	
	/**
	 * Post an error message. See {@link Log}.
	 * 
	 * @param message
	 *            : the message to display
	 */
	protected void le(String message)
	{
		ensureLocked();
		if(log != null)
			log.l(Level.ERROR, message);
	}
	
	/**
	 * Post a warning message. See {@link Log}.
	 * 
	 * @param message
	 *            : the message to display
	 */
	protected void lw(String message)
	{
		ensureLocked();
		if(log != null)
			log.l(Level.WARN, message);
	}
	
	/**
	 * Post an info message. See {@link Log}.
	 * 
	 * @param message
	 *            : the message to display
	 */
	protected void li(String message)
	{
		ensureLocked();
		if(log != null)
			log.l(Level.INFO, message);
	}
	
	/**
	 * Post a trace message. See {@link Log}.
	 * 
	 * @param message
	 *            : the message to display
	 */
	protected void lf(String message)
	{
		ensureLocked();
		if(log != null)
			log.l(Level.TRACE, message);
	}
	
	/**
	 * This method should be used in return statements. It adds a log message just before returning the {@link Object}
	 * in the argument, displaying the {@link Object}.
	 * 
	 * @param ret
	 *            : the {@link Object} to return.
	 * 
	 * @return the {@link Object} passed as argument.
	 */
	protected Object lr(Object ret)
	{
		return lr(ret, null);
	}
	
	/**
	 * This method should be used in return statements. It adds a log message just before returning the {@link Object}
	 * in the argument.
	 * <p>
	 * The {@link Object} in the argument is also put in the log message.
	 * 
	 * @param ret
	 *            : the {@link Object} to return and to display.
	 * @param message
	 *            : the message to display beside the {@link Object}.
	 * 
	 * @return the {@link Object} passed as argument.
	 */
	protected Object lr(Object ret, String message)
	{
		lf(ret.toString() + (message != null ? ":[" + message + "]" : ""));
		return ret;
	}
	
	/**
	 * This method displays a log message (with the level <code>TRACE</code>) only if the specified {@link DebugItem} is
	 * activated.
	 * 
	 * @param debug
	 *            : the {@link DebugItem}
	 * @param message
	 *            : the log message
	 */
	protected void dbg(DebugItem debug, String message)
	{
		if(debug.toBool())
			lf(message);
	}
	
	/**
	 * Composes a message with an array of {@link Object} instances.
	 * 
	 * @param message
	 *            : the message
	 * @param objects
	 *            : the objects
	 * @return the concatenated string
	 */
	protected static String compose(String message, Object[] objects)
	{
		String ret = message;
		for(Object object : objects)
			ret += "," + object.toString();
		return ret;
	}
}
