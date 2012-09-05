package net.xqhs.util.logging;

public class LogDebug extends Debug
{
	public enum LogDebugItem implements DebugItem
	{
		/**
		 * tracing of log management.
		 */
		D_LOG_MANAGEMENT(false),
		
		;
		
		private boolean isSet;

		private LogDebugItem(boolean isSet)
		{	
			this.isSet = isSet;
		}

		@Override
		public boolean toBool()
		{
			return isSet;
		}
		
	}
}
