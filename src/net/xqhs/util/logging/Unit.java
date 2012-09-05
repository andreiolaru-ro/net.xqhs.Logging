package net.xqhs.util.logging;

import net.xqhs.util.config.Config;
import net.xqhs.util.logging.Debug.DebugItem;
import net.xqhs.util.logging.Log.Level;

/**
 * The Unit class should be extended by any class using a log obtained from {@link Logging}.
 * <p>
 * It is characterized by the unitName, which also gives the name of (and helps refer) the log.
 * <p>
 * Although not abstract, the Unit should not be used as a member of a class, as no logging functions are available. This purpose is fulfilled by {@link UnitComponent}.
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
public class Unit
{
	public final static String DEFAULT_UNIT_NAME = "theDefaulUnitName";
	public final static Level  DEFAULT_LEVEL	 = Level.ALL;
	
	/**
	 * The configuration of the Unit.
	 */
	protected UnitConfigData   config			= null;
	
	/**
	 * The {@link Log} that will be used for logging.
	 */
	Log						log			   = null;
	
	/**
	 * This member offers a simpler way to give the default unit name, by reinitializing it. It also offers a simple
	 * switch to switch the log level to the default level.
	 * <p>
	 * WARNING: making this dynamic (return different values at different times) is useless; the method will be called
	 * <b>only</b> when the unit is constructed and the name returned will remain set.
	 */
	@SuppressWarnings("static-method")
	protected String getDefaultUnitName()
	{
		return DEFAULT_UNIT_NAME;
	}
	
	/**
	 * Constructs a new {@link Unit}, using a default {@link UnitConfigData} instance. It will not have a name.
	 */
	public Unit()
	{
		this(new UnitConfigData());
	}
	
	/**
	 * Constructs a {@link Unit} according to the given configuration.
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
	 * 
	 * @param configuration
	 *            : the configuration.
	 */
	public Unit(UnitConfigData configuration)
	{
		config = configuration;
		if(config == null)
			config = new UnitConfigData();
		
		if(config.unitName == null && getDefaultUnitName() != DEFAULT_UNIT_NAME)
		{
			config.setName(getDefaultUnitName());
			config.setLevel(DEFAULT_LEVEL);
		}
		if(config.unitName == DEFAULT_UNIT_NAME)
			config.setName((config.classNameLong ? this.getClass().getCanonicalName() : this.getClass().getSimpleName()));
		config.lock(this);
		
		if(config.unitName != null)
		{
			log = Logging.getLogger(makeLogName(), config.linkData.parentLogName, config.display, config.reporter,
					config.ensureNew, config.loggerWrapperType);
			if(config.level != null)
				log.setLevel(config.level);
		}
	}
	
	/**
	 * Produces the actual name of the log, based on the unit name.
	 * <p>
	 * This only differs from the unit name if <code>config.addClassName</code> is specified.
	 * 
	 * @return the name of the log.
	 */
	private String makeLogName()
	{
		String className = (config.classNameLong ? this.getClass().getCanonicalName() : this.getClass().getSimpleName());
		String name = config.unitName;
		if(config.addClassName)
		{
			if(config.classNamePostfix)
				name = name + className;
			else
				name = className + name;
		}
		return name;
	}
	
	/**
	 * This method is only accessible inside the instance, except if for good reasons an extending class makes it
	 * public.
	 * 
	 * @return the {@link Unit} name, as given to or set in the constructor.
	 */
	protected String getUnitName()
	{
		return config.unitName;
	}
	
	/**
	 * Instructs the Unit to exit. This method is only accessible inside the instance, except if for good reasons an
	 * extending class makes it public.
	 * <p>
	 * As the Unit layer only manages logging, the only thing that happens is that the log exists (if any).
	 */
	protected void doExit()
	{
		if(log != null && config.unitName != null)
		{
			Logging.exitLogger(makeLogName());
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
		if(log != null)
			log.le(message);
	}
	
	/**
	 * Post a warning message. See {@link Log}.
	 * 
	 * @param message
	 *            : the message to display
	 */
	protected void lw(String message)
	{
		if(log != null)
			log.lw(message);
	}
	
	/**
	 * Post an info message. See {@link Log}.
	 * 
	 * @param message
	 *            : the message to display
	 */
	protected void li(String message)
	{
		if(log != null)
			log.li(message);
	}
	
	/**
	 * Post a trace message. See {@link Log}.
	 * 
	 * @param message
	 *            : the message to display
	 */
	protected void lf(String message)
	{
		if(log != null)
			log.lf(message);
	}
	
	/**
	 * Post a message based on an {@link Object} and return the value. See {@link Log}.
	 * 
	 * @param ret
	 *            : the value to return
	 */
	protected Object lr(Object ret)
	{
		if(log != null)
			return log.lr(ret);
		return ret;
	}
	
	/**
	 * Post a message and return an {@link Object}. See {@link Log}.
	 * 
	 * @param message
	 *            : the message to display
	 * @param ret
	 *            : the value to return
	 */
	protected Object lr(Object ret, String message)
	{
		if(log != null)
			return log.lr(ret, message);
		return null;
	}
	
	/**
	 * Post a debug message message. See {@link Log}.
	 * 
	 * @param debug
	 *            : the {@link DebugItem} to which this message belongs.
	 * @param message
	 *            : the message to display
	 */
	protected void dbg(DebugItem debug, String message)
	{
		if(log != null)
			log.dbg(debug, message);
	}
	
}
