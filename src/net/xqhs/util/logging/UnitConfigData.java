package net.xqhs.util.logging;

import net.xqhs.util.logging.Log.Level;
import net.xqhs.util.logging.Log.LoggerType;
import net.xqhs.util.logging.Logging.DisplayEntity;
import net.xqhs.util.logging.Logging.ReportingEntity;

/**
 * This configures a Unit. All configuration relates to logging.
 * <p>
 * The main feature of the configuration is the name. If there is no name and other conditions are met, no log is
 * created. See the constructor of {@link Unit}.
 * 
 * @author Andrei Olaru
 */
public class UnitConfigData extends Config
{
	/**
	 * A sub-configuration for linking the configured {@link Log} to another the "parent". Currently only the
	 * <code>parentLogName</code> is used.
	 * <p>
	 * There are several effects of linking:
	 * <ul>
	 * <li>according to settings, then the "parent" log exists, the current log axists as well.
	 * <li>other effects are not currently implemented
	 * </ul>
	 * 
	 * @author Andrei Olaru
	 */
	public static class UnitLinkData extends Config
	{
		/**
		 * The name of the parent {@link Log}.
		 */
		String	parentLogName	= null;
		boolean	exitTogether	= true;	// unused
		boolean	includeInParent	= false;	// unused
		String	prefix			= "";		// unused
											
		public UnitLinkData()
		{
			super();
		}
		
		public UnitConfigData.UnitLinkData setparentLogName(String parentLogName)
		{
			this.parentLogName = parentLogName;
			return this;
		}
	}
	
	/**
	 * The name of the {@link Unit}. See {@link Unit} for details.
	 */
	String						unitName			= null;
	
	/**
	 * if <code>true</code>, the name of the class will be present in the name of this log, also according to the other
	 * settings in this group.
	 */
	boolean						addClassName		= false;
	/**
	 * If <code>true</code>, the name of the class will be concatenated after the name of the unit.
	 * <p>
	 * If <code>false</code>, the name of the class will be concatenated before the name of the unit.
	 */
	boolean						classNamePostfix	= false;
	/**
	 * If <code>true</code>, the complete name of the class will be used (including the package). Otherwise, only the
	 * name obtained by <code>Class.getSimpleName()</code> is used.
	 */
	boolean						classNameLong		= false;
	
	/**
	 * If <code>true</code>, then if another log exists with the same name, an error will be produced. If
	 * <code>false</code> and another log with the same name exists, that log will be used.
	 */
	boolean						ensureNew			= false;
	
	Level						level				= null;
	
	DisplayEntity				display				= null;
	ReportingEntity				reporter			= null;
	
	LoggerType					loggerWrapperType	= null;
	
	UnitConfigData.UnitLinkData	linkData			= new UnitConfigData.UnitLinkData();
	
	/**
	 * Reference to the configured {@link Unit} (if applicable), for cases of communication to the Unit after
	 * instantiation.
	 */
	protected Unit				unitLink			= null;								// TODO: implement updating
																							
	/**
	 * Locks the current configuration. See {@link Config}.
	 * 
	 * @param link
	 *            : the unit to notify if there are any updates (not implemented).
	 * @return
	 */
	public UnitConfigData lock(Unit link)
	{
		unitLink = link;
		return (UnitConfigData)super.lock();
	}
	
	/**
	 * Overrides the method in {@link Config} to catch the thrown exception and display it in the log.
	 */
	@Override
	protected void locked()
	{
		try
		{
			super.locked();
		} catch(ConfigLockedException e)
		{
			if(unitLink != null && unitLink.log != null)
				unitLink.log.error(e.toString());
		}
	}
	
	/**
	 * Constructs a configuration with default settings. The name will be <code>null</code>.
	 */
	public UnitConfigData()
	{
	}
	
	public UnitConfigData setName(String name)
	{
		locked();
		unitName = name; // special cases for the unit name (default unit name) will be treated in the Unit constructor.
		return this;
	}
	
	public UnitConfigData addClassName(boolean postfix, boolean useLongName)
	{
		locked();
		addClassName = true;
		classNamePostfix = postfix;
		classNameLong = useLongName;
		return this;
	}
	
	public UnitConfigData setDisplay(DisplayEntity logDisplay)
	{
		this.display = logDisplay;
		return this;
	}
	
	public UnitConfigData setReporter(ReportingEntity reportingEntity)
	{
		this.reporter = reportingEntity;
		return this;
	}
	
	public UnitConfigData setLevel(Level logLevel)
	{
		this.level = logLevel;
		return this;
	}
	
	/**
	 * If called, then if another log exists with the same name, an error will be produced. Otherwise, if another log
	 * with the same name exists, that log will be used.
	 */
	public UnitConfigData ensureNew()
	{
		locked();
		ensureNew = true;
		return this;
	}
	
	public UnitConfigData setLink(UnitConfigData.UnitLinkData unitLinkData)
	{
		locked();
		this.linkData = unitLinkData;
		return this;
	}
	
	public UnitConfigData setLink(String parentLogName)
	{
		return setLink(new UnitConfigData.UnitLinkData().setparentLogName(parentLogName));
	}
	
	public UnitConfigData setType(LoggerType loggerType)
	{
		locked();
		this.loggerWrapperType = loggerType;
		return this;
	}
}