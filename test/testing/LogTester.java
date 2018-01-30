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
package testing;

import net.xqhs.util.logging.Debug.DebugItem;
import net.xqhs.util.logging.DumbLogger;
import net.xqhs.util.logging.Logger;
import net.xqhs.util.logging.LoggerSimple.Level;
import net.xqhs.util.logging.UnitComponent;
import net.xqhs.util.logging.logging.LogWrapper;
import net.xqhs.util.logging.logging.LogWrapper.LoggerType;
import net.xqhs.util.logging.logging.Logging;

@SuppressWarnings("javadoc")
public class LogTester
{
	enum LocalDebug implements DebugItem
	{
		DO_DEBUG(true)
		
		;
		
		boolean value;
		
		private LocalDebug(boolean value)
		{
			this.value = value;
		}
		
		@Override
		public boolean toBool()
		{
			return value;
		}
	}
	
	public static String someMethod(UnitComponent log)
	{
		return (String) log.lr("test", "i am [] here", "standing");
	}
	
	public static void main(String[] args)
	{
		Logging.getMasterLogging().setLogLevel(Level.ALL);
		
		String NAME = "log";
		
		// non-recommended use
		System.out.println("\n\n=================== PART 1 ====================\n\n");
		
		LogWrapper log1 = Logging.getLogger(NAME, null, null, null, true, LoggerType.CONSOLE.getClassName(), null);
		LogWrapper log2 = Logging.getLogger(NAME + "-J", NAME, null, null, true, LoggerType.JAVA.getClassName(),
				Level.INFO);
		log1.setLevel(Level.INFO);
		
		log1.l(Level.ERROR, "error 1");
		log2.l(Level.ERROR, "error 2");
		log1.l(Level.INFO, "info 1");
		log2.l(Level.INFO, "info 2");
		
		Logging.exitLogger(NAME);
		
		log1.l(Level.ERROR, "error out");
		
		try
		{
			Thread.sleep(500);
		} catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("\n\n=================== PART 2 ====================\n\n");
		
		UnitComponent testUnit = (UnitComponent) new UnitComponent().setLogLevel(Level.ALL).setLoggerType(
				LoggerType.CONSOLE);
		// testUnit.setUnitName(NAME + "|", true, true);
		// testUnit.setUnitName(Unit.DEFAULT_UNIT_NAME);
		testUnit.setUnitName(NAME);
		
		testUnit.le("error [] here.", "par1", "par2");
		testUnit.li("info");
		
		testUnit.setUnitName("hello");
		
		someMethod(testUnit);
		testUnit.dbg(LocalDebug.DO_DEBUG, "debug with this [] argument", "my");
		
		testUnit.setLogLevel(Level.ERROR);
		testUnit.li("THIS SHOULD NOT SHOW UP");
		
		testUnit.doExit();
		
		testUnit.le("THIS SHOULD NOT SHOW UP");
		
		System.out.println("\n\n=================== PART 3 ====================\n\n");
		
		Logger simpleLog = DumbLogger.get();
		simpleLog.le("some error", "some argument");
		simpleLog.dbg(LocalDebug.DO_DEBUG, "some debug");
		
		try
		{
			Thread.sleep(500);
		} catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("\n\n=================== END ====================\n\n");
	}
}
