/*******************************************************************************
 * Copyright (C) 2018 Andrei Olaru.
 * 
 * This file is part of Logging.
 * 
 * Logging is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Logging is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Logging.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.xqhs.util.logging;

import net.xqhs.util.logging.Debug.DebugItem;

/**
 * This class acts as a simple implementation for the {@link Logger} interface, not connected to any of the actual
 * Logging infrastructure.
 * <p>
 * A static instance is also offered, obtained by calling {@link #get()}.
 * <p>
 * The intention for this class is that, should building a true logger fail, the class can use this implementation as a
 * fall-back in order to simplify logging code.
 * <p>
 * A 'preamble' is used for messages to mark the fact that they are printed out by this class. An alternative to the
 * default preamble (#) can be given in the constructor.
 * <p>
 * It prints all messages to the standard output. The only feature is that it assembles messages by replacing
 * {@value LoggerSimple#ARGUMENT_PLACEHOLDER} groups in the message with arguments received by methods (by calling
 * {@link Unit#compose}.
 * 
 * @author Andrei Olaru
 */
public class DumbLogger implements Logger
{
	/**
	 * The static instance.
	 */
	protected final static Logger staticInstance = new DumbLogger();

	/**
	 * Preamble to insert at the beginning of messages.
	 */
	protected String	pre	= "#";
	
	/**
	 * Default constructor.
	 */
	public DumbLogger()
	{
	}
	
	/**
	 * Constructor specifying a preamble.
	 * 
	 * @param preamble
	 *            - the preamble to add at the beginning of messages.
	 */
	public DumbLogger(String preamble)
	{
		pre = preamble;
	}
	
	/**
	 * @return the static {@link DumbLogger} instance.
	 */
	public static Logger get()
	{
		return staticInstance;
	}
	
	/**
	 * The method called by the other methods in the class, to actually print out logging messages.
	 * 
	 * @param message
	 *            - the message.
	 * @param arguments
	 *            - the arguments to put wherever {@value LoggerSimple#ARGUMENT_PLACEHOLDER} appears in the message.
	 */
	protected void l(String message, Object... arguments)
	{
		System.out.println(pre + " " + Unit.compose(message, arguments));
	}
	
	@Override
	public void le(String message, Object... arguments)
	{
		l(message, arguments);
	}
	
	@Override
	public void lw(String message, Object... arguments)
	{
		l(message, arguments);
	}
	
	@Override
	public void li(String message, Object... arguments)
	{
		l(message, arguments);
	}
	
	@Override
	public void lf(String message, Object... arguments)
	{
		l(message, arguments);
	}
	
	@Override
	public Object lr(Object ret)
	{
		return ret;
	}
	
	@Override
	public Object lr(Object ret, String message, Object... arguments)
	{
		l(message, arguments);
		return ret;
	}
	
	@Override
	public void dbg(DebugItem debug, String message, Object... arguments)
	{
		l(message, arguments);
	}
	
	@Override
	public void error(String message, Object... arguments)
	{
		l(message, arguments);
	}
	
	@Override
	public void warn(String message, Object... arguments)
	{
		l(message, arguments);
	}
	
	@Override
	public void info(String message, Object... arguments)
	{
		l(message, arguments);
	}
	
	@Override
	public void trace(String message, Object... arguments)
	{
		l(message, arguments);
	}
	
}
