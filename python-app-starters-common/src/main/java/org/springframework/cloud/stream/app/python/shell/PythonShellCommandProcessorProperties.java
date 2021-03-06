/*
 * Copyright 2017 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.springframework.cloud.stream.app.python.shell;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Properties for the Local Python Processor.
 *
 * @author David Turanski
 **/
@Validated
@ConfigurationProperties(prefix = "python")
public class PythonShellCommandProcessorProperties extends PythonAppProperties {
	public static enum Encoder {LF, CRLF, BINARY}

	/**
	 * The python command name, e.g., 'python', 'python3'.
	 */
	private String commandName = "python";

	/**
	 * The Python command line args.
	 */
	private String args = "";

	/**
	 * The encoder to use.
	 */
	private Encoder encoder = Encoder.CRLF;

	@NotNull
	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	@NotNull
	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	@NotNull
	public Encoder getEncoder() {
		return encoder;
	}

	public void setEncoder(Encoder encoder) {
		this.encoder = encoder;
	}

}