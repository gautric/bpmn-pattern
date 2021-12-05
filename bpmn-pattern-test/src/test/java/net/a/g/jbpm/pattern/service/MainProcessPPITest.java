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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jbpm.kie.services.impl.KModuleDeploymentUnit;
import org.jbpm.services.api.model.DeploymentUnit;
import org.jbpm.services.api.model.VariableDesc;
import org.jbpm.test.services.AbstractKieServicesTest;
import org.junit.Test;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.runtime.conf.NamedObjectModel;
import org.kie.internal.runtime.conf.ObjectModel;
import org.kie.internal.runtime.conf.RuntimeStrategy;

import net.a.g.jbpm.pattern.wih.MainProcessWIH;

public class MainProcessPPITest extends AbstractKieServicesTest {

	protected static final String ARTIFACT_ID = "test-module";
	protected static final String GROUP_ID = "org.jbpm.test";
	protected static final String VERSION = "1.0.0";

	static {

		// Only with 7.11 RHPAM

		// System.setProperty("org.kie.jbpm.runtime.manager.lock.factory",
		// DebugRuntimeManagerLockFactory.class.getCanonicalName());

		// System.setProperty("org.kie.jbpm.runtime.manager.lock.strategy",
		// FreeRuntimeManagerLockStrategy.class.getCanonicalName());

		// System.setProperty("org.kie.jbpm.runtime.manager.lock.strategy",TimeoutRuntimeManagerLockStrategy.class.getCanonicalName());
		// System.setProperty("org.kie.jbpm.runtime.manager.lock.timeout", "3000");

		// System.setProperty("org.kie.jbpm.runtime.manager.lock.strategy",InterruptibleRuntimeManagerLockStrategy.class.getCanonicalName());
		// System.setProperty("org.kie.jbpm.runtime.manager.lock.strategy",SerializableRuntimeManagerLockStrategy.class.getCanonicalName());

		// System.setProperty("org.kie.jbpm.runtime.manager.lock.strategy",
		// FreeRuntimeManagerLockStrategy.class.getCanonicalName());

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
		om.setName("MainProcessWIH");
		om.setResolver("mvel");
		om.setIdentifier("new " + MainProcessWIH.class.getName() + "(ksession)");
		processes.add(om);

		return processes;
	}

	@Override
	protected List<String> getProcessDefinitionFiles() {
		List<String> processes = new ArrayList<String>();

		processes.add("net/a/g/jbpm/pattern/MainProcessus.bpmn");

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
	public void mainProcessus2BranchTest() throws InterruptedException {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("integerIn", 42);
		param.put("booleanIn", true);
		param.put("stringIn", "machaine");

		long processInstanceId = processService.startProcess(deploymentUnit.getIdentifier(), "MainProcessus", param);

		processService.signalProcessInstance(deploymentUnit.getIdentifier(), processInstanceId, "SignalMainProcessus",
				null);

		Collection<VariableDesc> c = this.runtimeDataService.getVariablesCurrentState(processInstanceId);

		assertEquals((int) ProcessInstance.STATE_ABORTED,
				(int) this.runtimeDataService.getProcessInstanceById(processInstanceId).getState());

	}

}
