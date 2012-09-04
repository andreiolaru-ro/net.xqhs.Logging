package net.xqhs.util.logging;

/**
 * Class containing the different debug items that can be activated or deactivated by editing this class. Works together
 * with function <code>dbg()</code> in {@link Log}.
 * 
 * @author Andrei Olaru
 */
public class Debug
{
	public enum DebugItem {
		
		/**
		 * Dummy debug item.
		 */
		D_D(false),
		
		/**
		 * tracing of log management.
		 */
		D_LOG_MANAGEMENT(false),
		
		;
		
		boolean	isset;
		
		private DebugItem(boolean set)
		{
			isset = set;
		}
		
		protected boolean toBool()
		{
			return isset;
		}
	}
	
}
