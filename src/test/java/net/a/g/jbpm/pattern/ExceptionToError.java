/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.a.g.jbpm.pattern;

import java.util.HashMap;
import java.util.Map;

//import org.jbpm.bpmn2.handler.ServiceTaskHandler;
//import org.jbpm.bpmn2.handler.SignallingTaskHandlerDecorator;

import org.kie.api.KieServices;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.a.g.jbpm.pattern.wih.WorkItemHandler;

public class ExceptionToError {

	private static Logger LOG = LoggerFactory.getLogger(ExceptionToError.class);

	public static final void main(String[] args) {

		RuntimeManager manager = createManager();
		KieSession ksession = manager.getRuntimeEngine(null).getKieSession();

		ksession.addEventListener(new ProcessEventListener() {

			private  Logger LOG = LoggerFactory.getLogger("net.a.g.jbpm.pattern.ProcessEventListener");


			@Override
			public void beforeProcessStarted(ProcessStartedEvent event) {
				LOG.info("Start Processus : "+event.getProcessInstance().getProcessName());
			}

			@Override
			public void beforeProcessCompleted(ProcessCompletedEvent event) {
			}

			@Override
			public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
				LOG.info(">> " + event.getNodeInstance().getNodeName());
			}

			@Override
			public void beforeNodeLeft(ProcessNodeLeftEvent event) {
			}

			@Override
			public void afterVariableChanged(ProcessVariableChangedEvent event) {
			}

			@Override
			public void afterProcessStarted(ProcessStartedEvent event) {
				LOG.info("End Processus : "+event.getProcessInstance().getProcessName());

			}

			@Override
			public void afterProcessCompleted(ProcessCompletedEvent event) {
			}

			@Override
			public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
			}

			@Override
			public void afterNodeLeft(ProcessNodeLeftEvent event) {
			}

			@Override
			public void beforeVariableChanged(ProcessVariableChangedEvent event) {
				
			}
		});

		ksession.getWorkItemManager().registerWorkItemHandler("WorkItemHandler", new WorkItemHandler());

		Map<String, Object> params = new HashMap<String, Object>();
		ProcessInstance processInstance = ksession.startProcess("ExceptionToErrorProcess", params);
		
		manager.close();

		System.out.println(processInstance.getState());
		
		

	}

	private static RuntimeManager createManager() {

		RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get().newEmptyBuilder()
				.addAsset(
						KieServices.Factory.get().getResources().newFileSystemResource(
								"./src/main/resources/net/a/g/jbpm/pattern/ExceptionToErrorProcess.bpmn"),
						ResourceType.BPMN2)
				.get();

		return RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(environment);
	}

}
