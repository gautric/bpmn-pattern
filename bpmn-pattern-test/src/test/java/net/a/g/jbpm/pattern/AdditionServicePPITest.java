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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.core.command.runtime.process.GetProcessInstanceCommand;
import org.jbpm.kie.services.impl.KModuleDeploymentUnit;
import org.jbpm.services.api.ProcessInstanceNotFoundException;
import org.jbpm.services.api.model.DeploymentUnit;
import org.jbpm.services.api.model.VariableDesc;
import org.jbpm.test.services.AbstractKieServicesTest;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.internal.KieInternalServices;
import org.kie.internal.process.CorrelationKey;
import org.kie.internal.runtime.conf.ObjectModel;
import org.kie.internal.runtime.conf.RuntimeStrategy;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;

import net.a.g.jbpm.pattern.PatternProcessListener;

public class AdditionServicePPITest extends AbstractKieServicesTest {

	protected static final String ARTIFACT_ID = "test-module";
	protected static final String GROUP_ID = "org.jbpm.test";
	protected static final String VERSION = "1.0.0";

	private static final String ADD_PROCESS = "AdditionProcess";

	@Override
	protected DeploymentUnit createDeploymentUnit(String groupId, String artifactid, String version) throws Exception {
		DeploymentUnit unit = super.createDeploymentUnit(groupId, artifactid, version);
		((KModuleDeploymentUnit) unit).setStrategy(RuntimeStrategy.PER_PROCESS_INSTANCE);
		return unit;
	}

	@Override
	protected List<String> getProcessDefinitionFiles() {
		List<String> processes = new ArrayList<String>();
		processes.add("net/a/g/jbpm/pattern/AdditionProcess.bpmn");
		processes.add("net/a/g/jbpm/pattern/AdditionRule.dmn");
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

	@Test
	public void testAdditionProcess() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("a", 1);
		params.put("b", 3);

		long processInstanceId = processService.startProcess(deploymentUnit.getIdentifier(), ADD_PROCESS, params);
		assertNotNull(processInstanceId);

		Collection<VariableDesc> historyList = this.runtimeDataService.getVariableHistory(processInstanceId, "result",
				null);

		if (historyList.isEmpty()) {
			fail("No 'result' value");
		}

		for (VariableDesc variableDesc : historyList) {
			assertEquals(4, Integer.parseInt(variableDesc.getNewValue()));
		}
	}

}
