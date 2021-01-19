package net.xqhs.util.logging.output;

import java.io.OutputStream;

import net.xqhs.util.logging.Logger;
import net.xqhs.util.logging.Logger.Level;

/**
 * An implementation of {@link LogOutput} which outputs logging messages to the system console.
 * 
 * @author Andrei Olaru
 */
public class ConsoleOutput implements StreamLogOutput {
	
	@Override
	public int getUpdatePeriod() {
		return 0;
	}
	
	@Override
	public int formatData() {
		return Logger.INCLUDE_NAME;
	}
	
	@Override
	public boolean useCustomFormat() {
		return false;
	}
	
	@Override
	public String format(Level level, String source, String message) {
		return null;
	}
	
	@Override
	public void update() {
		// nothing to do
	}
	
	@Override
	public OutputStream getOutputStream() {
		return System.out;
	}
	
	@Override
	public void exit() {
		// nothing to do
	}
}
