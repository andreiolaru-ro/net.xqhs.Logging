package testing;

import java.io.OutputStream;

import net.xqhs.util.logging.Log;

public class ConsoleWrapper extends Log
{
	
	Level currentLevel = Level.ERROR;
	
	@Override
	public void setLevel(Level level)
	{
		currentLevel = level;
	}
	
	@Override
	public void addDestination(String format, OutputStream destination)
	{
		// unsupported
	}
	
	@Override
	public void l(Level level, String message)
	{
		if(level.compareTo(currentLevel) >= 0)
			System.out.println("[" + level.toString() + "][" + message + "]");
	}
	
}
