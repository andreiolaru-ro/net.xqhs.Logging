package net.xqhs.util.logging.output;

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
	private final FileOutputStream outputStream;
	
	/**
	 * Builds a new file output, based on a file.
	 * 
	 * @param path
	 *            - the path to the file, to be given to {@link FileOutputStream#FileOutputStream(String)}.
	 * @throws FileNotFoundException
	 */
	public FileOutput(String path) throws FileNotFoundException {
		outputStream = new FileOutputStream(path);
	}
	
	@Override
	public void update() {
		// nothing to do.
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
	public String format(Logger.Level level, String source, String message) {
		// not necessary, as there is no custom format.
		return null;
	}
}
