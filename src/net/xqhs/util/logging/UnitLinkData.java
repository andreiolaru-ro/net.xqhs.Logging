package net.xqhs.util.logging;

import net.xqhs.util.config.Config;

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
public class UnitLinkData extends Config
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
	
	public UnitLinkData setparentLogName(String parentLogName)
	{
		this.parentLogName = parentLogName;
		return this;
	}
}