/*******************************************************************************
 * Copyright (C) 2013 Andrei Olaru.
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

import net.xqhs.util.logging.Log;
import net.xqhs.util.logging.Log.Level;
import net.xqhs.util.logging.Log.LoggerType;
import net.xqhs.util.logging.Logging;
import net.xqhs.util.logging.UnitComponent;

public class LogTester
{
	public static void main(String[] args)
	{
		String NAME = "log";
		
//		Logging.getMasterLogging().setUnitName(null);
		
		// non-recommended use
		System.out.println("\n\n=================== PART 1 ====================\n\n");
		
		Log log1 = Logging.getLogger(NAME);
		Log log2 = Logging.getLogger(NAME + "-J", NAME, null, null, true, LoggerType.JAVA);
		log1.setLevel(Level.INFO);
		
		log1.le("error");
		log2.le("error");
		log1.li("info");
		log2.li("info");
		
		Logging.exitLogger(NAME);
		
		try
		{
			Thread.sleep(500);
		} catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("\n\n=================== PART 2 ====================\n\n");
		
		UnitComponent testUnit = (UnitComponent) new UnitComponent().setUnitName(NAME, true, true).setLogLevel(Level.WARN);
		
		testUnit.le("error");
		testUnit.li("info");
		testUnit.doExit();
		
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
