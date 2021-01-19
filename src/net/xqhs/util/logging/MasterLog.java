package net.xqhs.util.logging;

import net.xqhs.util.logging.Logger.Level;

/**
 * The class manages static, global settings that span over all logs.
 * <p>
 * It contains a singleton {@link Unit} (the "master" unit) which is added automatically as parent to all logs.
 * 
 * @author Andrei Olaru
 */
public class MasterLog {
	/**
	 * The unit which is given as parent to all logs.
	 */
	protected static Unit masterLog = new Unit().setUnitName("M-Log");
	
	/**
	 * The level of the master unit, which may propagate to other logs.
	 * 
	 * @param level - the {@link Level}.
	 */
	public static void setLogLevel(Level level) {
		masterLog.setLogLevel(level);
	}
}
