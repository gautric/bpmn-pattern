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

import net.a.g.jbpm.pattern.wih.WorkItemHandlerThrowingException;

public class ExceptionToErrorTest extends JbpmJUnitBaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionToErrorTest.class);

    @Test
    public void test() {
        ExceptionToErrorTest.LOG.debug("jBPM unit test sample");

        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/ExceptionToErrorProcess.bpmn");
        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
        final KieSession kieSession = runtimeEngine.getKieSession();
        kieSession.getWorkItemManager().registerWorkItemHandler("WorkItemHandler", new WorkItemHandlerThrowingException());

        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());


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