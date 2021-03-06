/*
 * Copyright 2012-2015, the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flipkart.sp.task.impl.thrift.registry;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.flipkart.sp.task.spi.thrift.ThriftProxy;

/**
 * <code>ThriftProxyRegistry</code>  maintains a registry of ThriftProxys. Provides lookup 
 * methods to get a ThriftProxy using a command string.
 * 
 * @author devashishshankar
 * @version 1.0, 3rd April, 2013
 */
public class ThriftProxyRegistry {

	/** Map storing the mapping of a commandString to ThriftProxy */
	private Map<String,ThriftProxy> stringToThriftHandler = new ConcurrentHashMap<String, ThriftProxy>();
	
	/**
	 * Register a new {@link ThriftProxy} to the registry. Note: If the CommandString
	 * exists, it overwrites
	 * @param commandString The command String identifying the command
	 * @param ThriftProxy {@link ThriftProxy} instance
	 */
	public void registerThriftProxy(String commandString, ThriftProxy thriftProxy) {
		this.stringToThriftHandler.put(commandString, thriftProxy);
	}
	
	/**
	 * Register a new ThriftProxy. The commandString is defaulted to the name defined
	 * in {@link ThriftProxy}
	 * @param thriftProxy The {@link ThriftProxy} instance to be added
	 */
	public void registerThriftProxy(ThriftProxy thriftProxy) {
		this.stringToThriftHandler.put(thriftProxy.getName(), thriftProxy);
	}
	
	/**
	 * Returns the {@link ThriftProxy} instance for the given Command String
	 * @param commandString The command string
	 * @return ThriftProxy, if found, null otherwise
	 */
	public ThriftProxy getThriftProxy(String commandString) {
		return this.stringToThriftHandler.get(commandString);
	}
	
	/**
	 * Returns all the ThriftProxy instances present in the registry
	 * @return Array of ThriftProxy
	 */
	public ThriftProxy[] getAllThriftProxies() {
		return (new HashSet<ThriftProxy>(this.stringToThriftHandler.values())).toArray(new ThriftProxy[0]);
	}
}
