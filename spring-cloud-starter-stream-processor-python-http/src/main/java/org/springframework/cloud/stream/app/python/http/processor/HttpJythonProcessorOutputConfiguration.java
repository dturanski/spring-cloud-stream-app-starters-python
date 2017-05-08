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

package org.springframework.cloud.stream.app.python.http.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.app.python.wrapper.JythonWrapper;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author David Turanski
 **/
@EnableBinding(Processor.class)
public class HttpJythonProcessorOutputConfiguration {

	@Autowired(required = false)
	private JythonWrapper jythonWrapper;

	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Message<?> process(Message<?> message) {
		if (jythonWrapper == null) {
			return message;
		}
		Map<String, Object> channel = new HashMap<>();
		channel.put("channel", Processor.OUTPUT);
		Object result = jythonWrapper.execute(message, channel);
		return MessageBuilder.createMessage(result, message.getHeaders());
	}
}
