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

package org.springframework.cloud.stream.app.python.jython.processor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.app.python.jython.JythonScriptExecutor;
import org.springframework.cloud.stream.app.python.shell.PythonAppDeployer;
import org.springframework.cloud.stream.app.python.shell.PythonAppDeployerConfiguration;
import org.springframework.cloud.stream.app.python.shell.PythonShellCommandProcessorConfiguration;
import org.springframework.cloud.stream.app.python.wrapper.JythonWrapperConfiguration;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.shell.ShellCommandProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

/**
 * A Processor that forks a shell to run a Python app configured as processor, sending and receiving messages via
 * stdin/stdout. Optionally this may use a Jython wrapper script to transform data to and from the remote app. If no
 * wrapper is configured, the payload must be String.
 *
 * @author David Turanski
 **/
@EnableBinding(Processor.class)
@Import({ PythonShellCommandProcessorConfiguration.class, JythonWrapperConfiguration.class,
		PythonAppDeployerConfiguration.class})
public class PythonLocalProcessorConfiguration implements InitializingBean {

	@Autowired
	private PythonAppDeployer pythonAppDeployer;

	@Autowired
	private ShellCommandProcessor shellCommandProcessor;

	@Autowired(required = false)
	private JythonScriptExecutor jythonWrapper;

	@Override
	public void afterPropertiesSet() throws Exception {
		pythonAppDeployer.deploy();
		shellCommandProcessor.start();
	}

	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Object process(Message<?> message) {
		if (jythonWrapper != null) {
			return jythonWrapper.execute(message);
		}
		else {
			if (message.getPayload() instanceof String) {
				return shellCommandProcessor.sendAndReceive((String) message.getPayload());
			}
			else {
				throw new IllegalArgumentException(
						"Only String payloads are supported with no Jython wrapper " + "configured");
			}
		}
	}
}