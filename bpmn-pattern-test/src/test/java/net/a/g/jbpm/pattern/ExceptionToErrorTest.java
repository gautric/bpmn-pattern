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

public class ExceptionToErrorTest extends JbpmJUnitBaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionToErrorTest.class);

    @Test
    public void testWIHThrowingException() {
        ExceptionToErrorTest.LOG.debug("jBPM unit test sample");

        addWorkItemHandler("WorkItemHandler", new WorkItemHandlerThrowingException(new Exception("For Demonstration")));

        
        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/ExceptionToErrorProcess.bpmn");
        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
        final KieSession kieSession = runtimeEngine.getKieSession();

        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());
        kieSession.addEventListener((ProcessEventListener)new MDCProcessListener());

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
    
    @Test
	public void testWithUniqueId() {
		LOG.debug("jBPM unit test sample");

		addWorkItemHandler("WorkItemHandler", new org.jbpm.bpmn2.handler.SignallingTaskHandlerDecorator(
				new WorkItemHandlerThrowingException(new Exception()), "Error-_72474602-751C-43FD-9D4F-2598A16468D1-ExceptionCode"));				
	//			new WorkItemHandlerThrowingException(new Exception()), "Error-ExceptionCode"));				
		
		final RuntimeManager runtimeManager = createRuntimeManager(
				"net/a/g/jbpm/pattern/ExceptionToErrorV2Process.bpmn");
		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();

		kieSession.addEventListener((ProcessEventListener) new PatternProcessListener());
		kieSession.addEventListener((ProcessEventListener) new MDCProcessListener());


		Map<String, Object> params = new HashMap<String, Object>();

		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);

		ProcessInstance processInstance = kieSession.startProcess("ExceptionToErrorV2Process", params);

		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
		assertNodeTriggered(processInstance.getId(), "ScriptTask");
		assertNodeTriggered(processInstance.getId(), "Error End");

		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}
    
}