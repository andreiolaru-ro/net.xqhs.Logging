package net.xqhs.util.logging.output;

import net.xqhs.util.logging.Logger.Level;

public interface LogOutput {
	
	public final static LogOutput DEFAULT_LOG_OUTPUT = new ConsoleOutput();
	
	public int getUpdatePeriod();
	
	public int formatData();
	
	public boolean useCustomFormat();
	
	public String format(Level level, String source, String message);
}
