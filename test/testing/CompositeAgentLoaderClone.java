package testing;

import net.xqhs.util.logging.LoggerSimple.Level;
import net.xqhs.util.logging.logging.Logging;
import net.xqhs.util.logging.UnitComponentExt;

public class CompositeAgentLoaderClone
{
	public static void main(String[] args)
	{
		Logging.getMasterLogging().setLogLevel(Level.ALL);
		UnitComponentExt log = (UnitComponentExt) new UnitComponentExt().setUnitName("agent loader").setLogLevel(
				Level.ALL);
		
		log.trace("component [] loaded for agent [].");
		
		log.trace("agent [] loaded.");
		log.doExit();
		
	}
}
