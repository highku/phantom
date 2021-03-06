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

package com.flipkart.sp.task.impl.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.codehaus.jackson.map.ObjectMapper;
import org.trpr.platform.core.impl.logging.LogFactory;
import org.trpr.platform.core.spi.logging.Logger;

import com.flipkart.sp.task.spi.task.TaskContext;
import com.flipkart.sp.task.spi.task.TaskHandler;
import com.flipkart.sp.task.spi.task.TaskResult;

/**
 * Default implementation of {@link TaskContext}
 * 
 * @author devashishshankar
 * @version 1.0, 20th March, 2013
 */
public class TaskContextImpl implements TaskContext {

	/** Logger for this class */
	private static final Logger LOGGER = LogFactory.getLogger(TaskContextImpl.class);

	/** The default command to get Config  */
	private static final String GET_CONFIG_COMMAND = "getConfig";
	
	/** Host name for this TaskContext */
	private String hostName;
	
	/** ObjectMapper instance */
    private ObjectMapper objectMapper = new ObjectMapper();


	/** The TaskHandlerExecutorRepository instance for getting task handler executor instances */
	private TaskHandlerExecutorRepository executorRepository;

	/**
	 * Gets the config from the ConfigTaskHandler (@link{GET_CONFIG_COMMAND}).
	 * @param group group name of the object to be fetched
	 * @param key the primary key
	 * @return the config as string, empty string if not found/error
	 */
	public String getConfig(String group, String key, int count) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("group", group);
		params.put("key", key);
		params.put("count", Integer.toString(count));
		TaskResult result = this.executeCommand(GET_CONFIG_COMMAND, null, params);
		if (result == null)
			return "";
		return new String((byte[])result.getData());
	}

	/**
	 * Executes a command
	 */
	public TaskResult executeCommand(String commandName, byte[] data, Map<String, String> params) throws UnsupportedOperationException {
		return this.executorRepository.executeCommand(commandName, data, params);
	}
	
	/**
	 * Executes a command asynchronously
	 */
	public Future<TaskResult> executeAsyncCommand(String commandName, byte[] data, Map<String, String> params) throws UnsupportedOperationException {
		return this.executorRepository.executeAsyncCommand(commandName, data, params);
	}

	/**
	 * Interface method implementation. Sends the "sendMetric" command for profiling
	 */
	public void profileCommand(TaskHandler handler, String command, Long diff, String tags) {
		try {
			Map<String, String> tsdbDataParams = new HashMap<String, String>();
			tsdbDataParams.put("key", handler.getName() + "TaskHandler-" + command);
			tsdbDataParams.put("pool", "agent");
			tsdbDataParams.put("type", "measure");
			tsdbDataParams.put("ts", String.valueOf(System.currentTimeMillis() * 1000));
			if (tags != null) {
				tsdbDataParams.put("tags", "host=" + this.hostName + " " + tags);
			} else {
				tsdbDataParams.put("tags", "host=" + this.hostName);
			}

			tsdbDataParams.put("value", String.valueOf(diff));
			// Commenting just for verification will remove this function and reference in the next checkin
			//this.executeCommand("sendMetric", null, tsdbDataParams);
		} catch (Exception e) {
			LOGGER.error("Exception while profiling agent command", e);
		}
	}

	/** Getter/Setter methods */
	public TaskHandlerExecutorRepository getExecutorRepository() {
		return this.executorRepository;
	}
	public void setExecutorRepository(TaskHandlerExecutorRepository executorRepository) {
		this.executorRepository = executorRepository;
	}
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	/** End Getter/Setter methods */
}
