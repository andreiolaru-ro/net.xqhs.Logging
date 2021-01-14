package net.xqhs.util.logging.output;

import java.io.OutputStream;

import net.xqhs.util.logging.Logger;
import net.xqhs.util.logging.Logger.Level;
import net.xqhs.util.logging.logging.LogWrapper;

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
	
}
