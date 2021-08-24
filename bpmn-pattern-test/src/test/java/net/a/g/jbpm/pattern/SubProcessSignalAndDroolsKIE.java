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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.kie.services.impl.KModuleDeploymentUnit;
import org.jbpm.services.api.model.DeploymentUnit;
import org.jbpm.services.api.model.NodeInstanceDesc;
import org.jbpm.services.api.model.VariableDesc;
import org.jbpm.test.services.AbstractKieServicesTest;
import org.junit.Test;
import org.kie.internal.runtime.conf.ObjectModel;
import org.kie.internal.runtime.conf.RuntimeStrategy;

public class SubProcessSignalAndDroolsKIE extends AbstractKieServicesTest {

	protected static final String ARTIFACT_ID = "test-module";
	protected static final String GROUP_ID = "org.jbpm.test";
	protected static final String VERSION = "1.0.0";

	private static final String PROCESS = "SubProcessSignalAndDrools";

	@Override
	protected DeploymentUnit createDeploymentUnit(String groupId, String artifactid, String version) throws Exception {
		DeploymentUnit unit = super.createDeploymentUnit(groupId, artifactid, version);
		((KModuleDeploymentUnit) unit).setStrategy(RuntimeStrategy.PER_PROCESS_INSTANCE);
		return unit;
	}

	@Override
	protected List<String> getProcessDefinitionFiles() {
		List<String> processes = new ArrayList<String>();
		processes.add("net/a/g/jbpm/pattern/SubProcessSignalAndDrools.bpmn");
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
	public void testNominalCasewDelay() {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("timerIn", "PT1S");

		long processInstanceId = processService.startProcess(deploymentUnit.getIdentifier(), PROCESS, params);
		assertNotNull(processInstanceId);

		Collection<VariableDesc> historyList = this.runtimeDataService.getVariableHistory(processInstanceId, "result",
				null);

		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		 Collection<NodeInstanceDesc> history = runtimeDataService.getProcessInstanceFullHistory(processInstanceId, null);

		 assertNotNull(history.stream().filter(o -> o.getName().compareTo("Delay Path")==0).findFirst()); 
		
	}
	
	@Test
	public void testSubProcessSignal() {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("timerIn", "PT2S");

		long processInstanceId = processService.startProcess(deploymentUnit.getIdentifier(), PROCESS, params);
		assertNotNull(processInstanceId);


		
		try {
			Thread.sleep(1000);
			processService.signalEvent(deploymentUnit.getIdentifier(),"SubProcessSignal", "SubProcessSignalData");
			Thread.sleep(3000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
				
		 Collection<NodeInstanceDesc> history = runtimeDataService.getProcessInstanceFullHistory(processInstanceId, null);
		 
		 for (NodeInstanceDesc nodeInstanceDesc : history) {
			System.out.println(nodeInstanceDesc);
		}
		 
		 
		 assertNotNull(history.stream().filter(o -> o.getName().compareTo("Drools Path")==0).findFirst()); 

	}

}
