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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.kie.services.impl.KModuleDeploymentUnit;
import org.jbpm.kie.services.impl.query.SqlQueryDefinition;
import org.jbpm.kie.services.impl.query.mapper.RawListQueryMapper;
import org.jbpm.kie.test.objects.Image;
import org.jbpm.kie.test.util.AbstractKieServicesBaseTest;
import org.jbpm.services.api.ProcessInstanceNotFoundException;
import org.jbpm.services.api.model.DeploymentUnit;
import org.jbpm.services.api.model.ProcessInstanceDesc;
import org.jbpm.services.api.model.UserTaskInstanceDesc;
import org.jbpm.services.api.query.model.QueryDefinition;
import org.jbpm.services.task.HumanTaskServiceFactory;
import org.jbpm.services.task.audit.TaskAuditServiceFactory;
import org.jbpm.services.task.audit.service.TaskAuditService;
import org.junit.Test;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.query.QueryContext;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.internal.runtime.conf.ObjectModel;
import org.kie.internal.runtime.conf.RuntimeStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HumanTaskTestProcessTest extends AbstractKieServicesBaseTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(HumanTaskTestProcessTest.class);

	private Long processInstanceId = null;

	protected static final String ARTIFACT_ID = "test-module";
	protected static final String GROUP_ID = "org.jbpm.test";
	protected static final String VERSION = "1.0.0";

	@Override
	protected DeploymentUnit createDeploymentUnit(String groupId, String artifactid, String version) throws Exception {
		DeploymentUnit unit = super.createDeploymentUnit(groupId, artifactid, version);
		((KModuleDeploymentUnit) unit).setStrategy(RuntimeStrategy.PER_PROCESS_INSTANCE);
		// super.createDeploymentDescriptor().
		// ((KModuleDeploymentUnit)
		// unit).getDeploymentDescriptor().getEnvironmentEntries().add(e);

		return unit;
	}

	@Override
	protected List<String> getProcessDefinitionFiles() {
		List<String> processes = new ArrayList<String>();

		processes.add("net/a/g/jbpm/pattern/HumanTaskTestProcess.bpmn");

		return processes;
	}

	@Override
	protected boolean createDescriptor() {
		return true;
	}

	protected List<ObjectModel> getProcessListeners() {
		List<ObjectModel> processes = new ArrayList<ObjectModel>();

		ObjectModel om = new ObjectModel();
		om.setResolver("mvel");
		om.setIdentifier("new " + net.a.g.jbpm.pattern.PatternProcessListener.class.getName() + "()");
		processes.add(om);

		return processes;
	}

	@Override
	public DeploymentUnit prepareDeploymentUnit() throws Exception {
		return createAndDeployUnit(GROUP_ID, ARTIFACT_ID, VERSION);
	}

	public void tearDown() {
		if (processInstanceId != null) {
			try {
				// let's abort process instance to leave the system in clear state
				processService.abortProcessInstance(processInstanceId);

				ProcessInstance pi = processService.getProcessInstance(processInstanceId);
				assertNull(pi);
			} catch (ProcessInstanceNotFoundException e) {
				// ignore it as it might already be completed/aborted
			}
		}
		super.tearDown();
	}

	protected TaskAuditService getTaskAuditService() {
		TaskService taskService = HumanTaskServiceFactory.newTaskServiceConfigurator().entityManagerFactory(emf)
				.getTaskService();
		return TaskAuditServiceFactory.newTaskAuditServiceConfigurator().setTaskService(taskService)
				.getTaskAuditService();
	}

	
	
	@Test
	public void testStartAndCompleteNominal() {
		// start a new process instance
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("timerIn", "R/PT5S");
		params.put("taskUserIn","john");
		params.put("reassignmentsUserIn","mary");
		params.put("reassignmentsGroupIn","");

		QueryDefinition qd = new SqlQueryDefinition("DL", "");
		qd.setExpression("SELECT Deadline.* FROM Deadline");
		qd.setSource("jdbc/testDS1");
		queryService.registerQuery(qd);

		processInstanceId = processService.startProcess(deploymentUnit.getIdentifier(), "HumanTaskTestProcess", params);
		assertNotNull(processInstanceId);
		List<Long> taskIds = runtimeDataService.getTasksByProcessInstanceId(processInstanceId);
		assertNotNull(taskIds);
		assertEquals(1, taskIds.size());

		Long taskId = taskIds.get(0);

		QueryContext qc = new QueryContext(0,1);

		
		
		userTaskService.claim(taskId, "john");
		userTaskService.start(taskId, "john");
		UserTaskInstanceDesc task = runtimeDataService.getTaskById(taskId);
		assertNotNull(task);
		assertEquals(Status.InProgress.toString(), task.getStatus());

		Map<String, Object> results = new HashMap<String, Object>();
		userTaskService.complete(taskId, "john", results);
		task = runtimeDataService.getTaskById(taskId);
		assertNotNull(task);
		assertEquals(Status.Completed.toString(), task.getStatus());
		
		ProcessInstanceDesc pi = runtimeDataService.getProcessInstanceById(processInstanceId);
		
		assertEquals(2, (int)pi.getState());

	}

	@Test
	public void testStartAndCompletePeriodic() {
		// start a new process instance
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("timerIn", "R/PT5S");
		params.put("taskUserIn","john");
		params.put("reassignmentsUserIn","mary");
		params.put("reassignmentsGroupIn","");

		QueryDefinition qd = new SqlQueryDefinition("DL", "");
		qd.setExpression("SELECT Deadline.* FROM Deadline");
		qd.setSource("jdbc/testDS1");
		queryService.registerQuery(qd);

		processInstanceId = processService.startProcess(deploymentUnit.getIdentifier(), "HumanTaskTestProcess", params);
		assertNotNull(processInstanceId);
		List<Long> taskIds = runtimeDataService.getTasksByProcessInstanceId(processInstanceId);
		assertNotNull(taskIds);
		assertEquals(1, taskIds.size());

		Long taskId = taskIds.get(0);

		QueryContext qc = new QueryContext(0,1);

		for (int i = 1; i <= 3; i++) {
		
			List<List<Object>>  test = queryService.query("DL", RawListQueryMapper.get(), qc);		
			test.stream().forEach(System.out::println);		
			assertEquals("",Double.valueOf("" +i), test.get(0).get(0));
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		userTaskService.claim(taskId, "mary");
		userTaskService.start(taskId, "mary");
		UserTaskInstanceDesc task = runtimeDataService.getTaskById(taskId);
		assertNotNull(task);
		assertEquals(Status.InProgress.toString(), task.getStatus());

		Map<String, Object> results = new HashMap<String, Object>();
		userTaskService.complete(taskId, "mary", results);
		task = runtimeDataService.getTaskById(taskId);
		assertNotNull(task);
		assertEquals(Status.Completed.toString(), task.getStatus());
		
		
		ProcessInstanceDesc pi = runtimeDataService.getProcessInstanceById(processInstanceId);
		
		assertEquals(2, (int)pi.getState());

	}
	

	@Test
	public void testStartAndCompleteNonPeriodic() {
		// start a new process instance
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("timerIn", "PT5S");
		params.put("taskUserIn","john");
		params.put("reassignmentsUserIn","mary");
		params.put("reassignmentsGroupIn","");

		QueryDefinition qd = new SqlQueryDefinition("DL", "");
		qd.setExpression("SELECT Deadline.* FROM Deadline");
		qd.setSource("jdbc/testDS1");
		queryService.registerQuery(qd);

		processInstanceId = processService.startProcess(deploymentUnit.getIdentifier(), "HumanTaskTestProcess", params);
		assertNotNull(processInstanceId);
		List<Long> taskIds = runtimeDataService.getTasksByProcessInstanceId(processInstanceId);
		assertNotNull(taskIds);
		assertEquals(1, taskIds.size());

		Long taskId = taskIds.get(0);

		QueryContext qc = new QueryContext(0,1);

		for (int i = 1; i <= 3; i++) {
		
			List<List<Object>>  test = queryService.query("DL", RawListQueryMapper.get(), qc);		
			test.stream().forEach(System.out::println);		
			assertEquals("",Double.valueOf(1), test.get(0).get(0));
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		userTaskService.claim(taskId, "mary");
		userTaskService.start(taskId, "mary");
		UserTaskInstanceDesc task = runtimeDataService.getTaskById(taskId);
		assertNotNull(task);
		assertEquals(Status.InProgress.toString(), task.getStatus());

		Map<String, Object> results = new HashMap<String, Object>();
		userTaskService.complete(taskId, "mary", results);
		task = runtimeDataService.getTaskById(taskId);
		assertNotNull(task);
		assertEquals(Status.Completed.toString(), task.getStatus());
		
		
		ProcessInstanceDesc pi = runtimeDataService.getProcessInstanceById(processInstanceId);
		
		assertEquals(2, (int)pi.getState());

	}

	protected void assertImageVariable(Image processVar, String name) {
		assertNotNull(processVar);
		assertNotNull(processVar.getName());
		assertEquals(name, processVar.getName());
		assertNotNull(processVar.getContent());
		assertEquals(1, processVar.getContent().length);
	}

}
