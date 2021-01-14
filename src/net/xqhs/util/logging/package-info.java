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
 * This package contains the classes and interfaces for the net.xqhs.Logging logging infrastructure.
 * <p>
 * Classes using this infrastructure should either extend {@link net.xqhs.util.logging.Unit} or 
 * {@link net.xqhs.util.logging.UnitExt} or should create instances of {@link net.xqhs.util.logging.UnitComponent} or 
 * {@link net.xqhs.util.logging.UnitComponentExt}. These classes act as logs that can be configured and then used (they 
 * implement {@link net.xqhs.util.config.Configurable}).
 * <p>
 * Classes without the *Ext particle in the name provide a reduced, simple set of logging primitives. Classes with the 
 * *Ext particle provide an extended set of primitives, more appropriate for code previously using other logging 
 * infrastructures. 
 * <p>
 * Since it may be desired to pass a log to a method / class without the callee being able to change settings, and only 
 * having access to the logging primitives, all Unit* classes implement {@link net.xqhs.util.logging.Logger} or 
 * {@link net.xqhs.util.logging.LoggerClassic} interfaces.
 * <p>
 * See the classes linked above for more details.
 */
package net.xqhs.util.logging;

