package net.xqhs.util.logging.output;

import net.xqhs.util.logging.LogWrapper;
import net.xqhs.util.logging.Logger;

import java.io.*;

/**
 * An implementation of {@link StreamLogOutput} where the stream is a {@link FileOutputStream}. Writing will be done
 * directly to the stream.
 */
public class FileOutput implements StreamLogOutput {
	/**
	 * The stream to write to.
	 */
	protected final FileOutputStream	outputStream;
	/**
	 * The number of milliseconds at which the stream should be flushed.
	 */
	protected long						updatePeriod;
	
	/**
	 * Builds a new file output, based on a file.
	 * 
	 * @param path
	 *            - the path to the file, to be given to {@link FileOutputStream#FileOutputStream(String)}.
	 * @param flushEvery
	 *            - the number of milliseconds at which the stream should be flushed. If zero, the log is flushed every
	 *            time; if negative, the log is <b>never</b> explicitly flushed. This value will be returned by
	 *            {@link #getUpdatePeriod()} to the {@link LogWrapper}.
	 * @throws FileNotFoundException
	 */
	public FileOutput(String path, long flushEvery) throws FileNotFoundException {
		outputStream = new FileOutputStream(path);
	}
	
	/**
	 * Calls {@link #FileOutput(String, long)} with <code>0</code> for the second parameter.
	 * 
	 * @param path
	 * @throws FileNotFoundException
	 */
	public FileOutput(String path) throws FileNotFoundException {
		this(path, -1);
	}
	
	@Override
	public void update() {
		try {
			outputStream.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public FileOutputStream getOutputStream() {
		return outputStream;
	}
	
	@Override
	public void exit() {
		try {
			outputStream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public long getUpdatePeriod() {
		return updatePeriod;
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
	public String format(Logger.Level level, String source, String message) {
		// not necessary, as there is no custom format.
		return null;
	}
}
