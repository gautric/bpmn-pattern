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

package net.a.g.jbpm.pattern.service;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jbpm.kie.services.impl.KModuleDeploymentUnit;
import org.jbpm.runtime.manager.impl.lock.FreeRuntimeManagerLockStrategy;
import org.jbpm.services.api.ProcessInstanceNotFoundException;
import org.jbpm.services.api.model.DeploymentUnit;
import org.jbpm.test.services.AbstractKieServicesTest;
import org.junit.Test;
import org.kie.internal.runtime.conf.NamedObjectModel;
import org.kie.internal.runtime.conf.ObjectModel;
import org.kie.internal.runtime.conf.RuntimeStrategy;

import net.a.g.jbpm.pattern.wih.WaitWorkItemHandler;

public class ParentChildProcessPPITest extends AbstractKieServicesTest {

	protected static final String ARTIFACT_ID = "test-module";
	protected static final String GROUP_ID = "org.jbpm.test";
	protected static final String VERSION = "1.0.0";

	static {
		
	//	System.setProperty("org.kie.jbpm.runtime.manager.lock.strategy", FreeRuntimeManagerLockStrategy.class.getCanonicalName());
	}
	
	@Override
	protected DeploymentUnit createDeploymentUnit(String groupId, String artifactid, String version) throws Exception {
		DeploymentUnit unit = super.createDeploymentUnit(groupId, artifactid, version);
		((KModuleDeploymentUnit) unit).setStrategy(RuntimeStrategy.PER_PROCESS_INSTANCE);
		return unit;
	}

	@Override
	protected List<NamedObjectModel> getWorkItemHandlers() {
		List<NamedObjectModel> processes = new ArrayList<NamedObjectModel>();

		NamedObjectModel om = new NamedObjectModel();
		om.setName("Human Task");
		om.setResolver("mvel");
		om.setIdentifier("new " + WaitWorkItemHandler.class.getName() + "()");
		processes.add(om);

		return processes;
	}

	@Override
	protected List<String> getProcessDefinitionFiles() {
		List<String> processes = new ArrayList<String>();

		processes.add("net/a/g/jbpm/pattern/parentchild/ParentProcess.bpmn");
		processes.add("net/a/g/jbpm/pattern/parentchild/ChildProcess.bpmn");

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
		om.setIdentifier("new " + net.a.g.jbpm.pattern.listener.ContextListener.class.getName() + "()");
		processes.add(om);

		return processes;
	}

	@Override
	public DeploymentUnit prepareDeploymentUnit() throws Exception {
		return createAndDeployUnit(GROUP_ID, ARTIFACT_ID, VERSION);
	}

	@Test
	public void testAbortAlreadyAbortedParentProcess() throws InterruptedException {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("a", 42);
		param.put("b", 128);
		param.put("result", 1);
		param.put("timer", "10s");

		ExecutorService executor = Executors.newFixedThreadPool(10);

		Runnable runnableTask = () -> {
			long	processInstanceId = processService.startProcess(deploymentUnit.getIdentifier(), "ParentProcess",
					param);
		};

		executor.execute(runnableTask);
		
		
		Thread.sleep(1000);

		processService.signalProcessInstance(deploymentUnit.getIdentifier(),1l,"CustomAbort", null);



		Thread.sleep(150000);

		try {
			// processService.abortProcessInstance(processInstanceId);
			fail("Aborting of already aborted process instance should throw ProcessInstanceNotFoundException.");
		} catch (ProcessInstanceNotFoundException e) {
			// expected
		}
	}

}
