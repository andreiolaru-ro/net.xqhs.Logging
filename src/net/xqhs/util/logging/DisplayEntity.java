package net.xqhs.util.logging;

/**
 * Interface for an entity that is able to display the log (e.g. in a visual interface). Updates will be posted
 * immediately. The {@link #output(String)} method is called with the entire log as argument, using logging messages
 * with just the level ad the content.
 * 
 * @author Andrei Olaru
 * 
 */
public interface DisplayEntity
{
	/**
	 * The method is called whenever new logging information is posted.
	 * 
	 * @param string
	 *            - the entire contents of the log.
	 */
	void output(String string);
	
}