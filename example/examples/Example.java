package examples;

import java.util.Random;

import net.xqhs.util.logging.LoggerSimple.Level;
import net.xqhs.util.logging.Unit;

/**
 * Unfinished example class.
 * 
 * @author Andrei Olaru
 */
public class Example
{
	static Random rand = new Random();
	
	public static Level levelPicker()
	{
		int r = rand.nextInt(10);
		if(r < 1)
			return Level.ERROR;
		if(r < 3)
			return Level.WARN;
		if(r < 7)
			return Level.INFO;
		return Level.TRACE;
	}
	
	/**
	 * For a class that needs a log and doesn't need to extend a specific class, the best way is to extend {@link Unit}.
	 * <p>
	 * If it does, it will have direct access to logging methods.
	 * 
	 * @author Andrei Olaru
	 */
	public static class ExampleUnit extends Unit
	{
		/**
		 * A unit must be initialized with at least a name.
		 * 
		 * @param name
		 *                 the name of the unit.
		 */
		public ExampleUnit(String name)
		{
			super();
			setUnitName(name); // the name of the log; see the documentation of Unit.
			setLogLevel(Level.ALL); // the level of the log
			
			li("unit started");
		}
	}
	
	public static void main(String[] args)
	{
		// nothing implemented
	}
}
