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
import java.util.UUID;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.a.g.jbpm.pattern.listener.MDCProcessListener;
import net.a.g.jbpm.pattern.util.Exception;
import net.a.g.jbpm.pattern.wih.WorkItemHandlerThrowingException;

public class ExceptionTest extends JbpmJUnitBaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionToErrorSubProcessTest.class);

    @Test
    public void test() {
        LOG.debug("jBPM unit test sample");

        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/ExceptionToErrorProcess.bpmn");
        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
        final KieSession kieSession = runtimeEngine.getKieSession();
        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());
        kieSession.addEventListener((ProcessEventListener)new MDCProcessListener());
        kieSession.getWorkItemManager().registerWorkItemHandler("WorkItemHandler", new WorkItemHandlerThrowingException(new Exception("Exception inside Test Case")));


        Map<String, Object> params = new HashMap<String, Object>();
		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);
		ProcessInstance processInstance = kieSession.startProcess("ExceptionToErrorProcess", params);
		
		assertProcessInstanceNotActive(processInstance.getId(), kieSession);

        assertNodeTriggered(processInstance.getId(), "ScriptTask");
		assertNodeTriggered(processInstance.getId(), "Error End");
        
        runtimeManager.disposeRuntimeEngine(runtimeEngine);
        runtimeManager.close();
    }
}