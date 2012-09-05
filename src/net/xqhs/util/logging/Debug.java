package net.xqhs.util.logging;

/**
 * Class containing different debug items that can be activated or deactivated by editing this class. Works together
 * with function <code>dbg()</code> in {@link Log}.
 * <p>
 * For documentation purposes, classes containing {@link LocalDebugItem} enums should extend the {@link Debug} class.
 * <p>
 * Each project should contain its own class extending {@link Debug}, holding an enum the implements {@link DebugItem},
 * with a similar implementation to this one.
 * 
 * @author Andrei Olaru
 */
public class Debug
{
	public interface DebugItem
	{
		public boolean toBool();
	}
	
	public enum LocalDebugItem implements DebugItem {
		
		/**
		 * Dummy debug item.
		 */
		D_D(false),
		
		;
		
		boolean isset;
		
		private LocalDebugItem(boolean set)
		{
			isset = set;
		}
		
		@Override
		public boolean toBool()
		{
			return isset;
		}
	}
	
}
