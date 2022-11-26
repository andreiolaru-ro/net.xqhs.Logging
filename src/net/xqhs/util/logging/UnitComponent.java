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
/**
 * 
 */
package net.xqhs.util.logging;

import net.xqhs.util.logging.Debug.DebugItem;
import net.xqhs.util.logging.LogWrapper.LoggerType;
import net.xqhs.util.logging.output.LogOutput;

/**
 * A class that extends {@link Unit} and exposes the logging and configuration methods. See {@link Unit} for details. It
 * implements {@link Logger}.
 * <p>
 * This allows a class to use the functionality of a {@link Unit} without extending {@link Unit}.
 * 
 * @author Andrei Olaru
 * 
 */
public class UnitComponent extends Unit implements Logger {
	/**
	 * This constructor is here only for backwards compatibility as it is usually not useful to add the unit name later.
	 */
	@Deprecated
	public UnitComponent() {
		super();
	}
	
	/**
	 * Creates a new instance and gives it a name.
	 * 
	 * @param unitName
	 *            - the name of the log.
	 */
	public UnitComponent(String unitName) {
		super();
		setUnitName(unitName);
	}

	@Override
	public UnitComponent setUnitName(String name) {
		return (UnitComponent) super.setUnitName(name);
	}
	
	@Override
	public UnitComponent setLoggerType(LoggerType loggerType) {
		return (UnitComponent) super.setLoggerType(loggerType);
	}
	
	@Override
	public UnitComponent setLogEnsureNew() {
		return (UnitComponent) super.setLogEnsureNew();
	}
	
	@Override
	public UnitComponent setLogLevel(Level logLevel) {
		return (UnitComponent) super.setLogLevel(logLevel);
	}
	
	@Override
	public UnitComponent setHighlighted() {
		return (UnitComponent) super.setHighlighted();
	}
	
	@Override
	public UnitComponent setNotHighlighted() {
		return (UnitComponent) super.setNotHighlighted();
	}
	
	@Override
	public UnitComponent setPerformanceMode(boolean performance) {
		return (UnitComponent) super.setPerformanceMode(performance);
	}
	
	@Override
	public UnitComponent addOutput(LogOutput logOutput) {
		return (UnitComponent) super.addOutput(logOutput);
	}
	
	@Override
	public UnitComponent removeOutput(LogOutput logOutput) {
		return (UnitComponent) super.removeOutput(logOutput);
	}
	
	@Override
	public UnitComponent addParentUnit(Unit parent) {
		return (UnitComponent) super.addParentUnit(parent);
	}
	
	@Override
	public UnitComponent removeAllParentUnits() {
		return (UnitComponent) super.removeAllParentUnits();
	}
	
	@Override
	public void le(String message, Object... arguments) {
		super.le(message, arguments);
	}
	
	@Override
	public void lw(String message, Object... arguments) {
		super.lw(message, arguments);
	}
	
	@Override
	public void li(String message, Object... arguments) {
		super.li(message, arguments);
	}
	
	@Override
	public void lf(String message, Object... arguments) {
		super.lf(message, arguments);
	}
	
	@Override
	public Object lr(Object ret) {
		return super.lr(ret);
	}
	
	@Override
	public Object lr(Object ret, String message, Object... arguments) {
		return super.lr(ret, message, arguments);
	}
	
	@Override
	public boolean ler(boolean ret, String message, Object... arguments) {
		return super.ler(ret, message, arguments);
	}
	
	@Override
	public void l(Level messageLevel, String message, Object... arguments) {
		super.l(messageLevel, message, arguments);
	}
	
	@Override
	public void dbg(DebugItem debug, String message, Object... arguments) {
		super.dbg(debug, message, arguments);
	}
	
	@Override
	public void doExit() {
		super.doExit();
	}
	
	@Override
	public String getUnitName() {
		return super.getUnitName();
	}
}
