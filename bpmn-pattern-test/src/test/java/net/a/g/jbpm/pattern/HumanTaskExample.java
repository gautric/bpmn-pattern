package net.a.g.jbpm.pattern;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.UserGroupCallback;
import org.kie.api.task.model.TaskSummary;
import org.kie.test.util.db.PersistenceUtil;

import net.a.g.jbpm.pattern.listener.MDCProcessListener;

public class HumanTaskExample {

	public static final void main(String[] args) {
		try {
			RuntimeManager manager = getRuntimeManager("net/a/g/jbpm/pattern/HumanTaskTestProcess.bpmn");
			RuntimeEngine runtime = manager.getRuntimeEngine(null);
			KieSession ksession = runtime.getKieSession();
			ksession.addEventListener((ProcessEventListener) new MDCProcessListener());

			
			Map<String, Object> params = new HashMap<String, Object>();
		
			params.put("timerIn", "PT5S");
			params.put("taskUserIn","john");
			params.put("reassignmentsUserIn","mary");
			ProcessInstance pi = ksession.startProcess("HumanTaskTestProcess", params);

			// "sales-rep" reviews request
			TaskService taskService = runtime.getTaskService();
			TaskSummary task1 = taskService.getTasksAssignedAsPotentialOwner("john", "en-UK").get(0);
			System.err.println("Sales-rep executing task " + task1.getName() + "(" + task1.getId() + ": "
					+ task1.getDescription() + ")");
			
			Thread.sleep(10*1000);
			try {
				taskService.claim(task1.getId(), "john");
				taskService.start(task1.getId(), "john");
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			 task1 = taskService.getTasksAssignedAsPotentialOwner("mary", "en-UK").get(0);
			
			taskService.claim(task1.getId(), "mary");
			taskService.start(task1.getId(), "mary");
			
			Map<String, Object> results = new HashMap<String, Object>();

			results.put("stringTask", "OK OK");
			results.put("booleanTask", true);
			results.put("integerTask", 42);

			taskService.complete(task1.getId(), "mary", results);
			Thread.sleep(1000);
			
			System.out.println("Process instance completed   " + pi.getState());

			//manager.disposeRuntimeEngine(runtime);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(0);
	}

	private static RuntimeManager getRuntimeManager(String process) {
		// load up the knowledge base
		PersistenceUtil.setupPoolingDataSource();
		RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder()
				.userGroupCallback(new UserGroupCallback() {
					public List<String> getGroupsForUser(String userId) {
						List<String> result = new ArrayList<String>();
						if ("sales-rep".equals(userId)) {
							result.add("sales");
						} else if ("john".equals(userId)) {
							result.add("PM");

						} else if ("krisv".equals(userId)) {
							result.add("sales");
						}
						return result;
					}

					public boolean existsUser(String arg0) {
						return true;
					}

					public boolean existsGroup(String arg0) {
						return true;
					}
				}).addAsset(KieServices.Factory.get().getResources().newClassPathResource(process), ResourceType.BPMN2)
				.get();
		return RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(environment);
	}

}
