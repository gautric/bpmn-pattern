package net.a.g.jbpm.pattern;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.jbpm.workflow.instance.WorkflowRuntimeException;
import org.junit.Test;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.ProcessWorkItemHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.a.g.jbpm.pattern.wih.WorkItemHandlerThrowingException;

public class HandlerGlobalExceptionProcessTest extends JbpmJUnitBaseTestCase {
	private static final Logger LOG = LoggerFactory.getLogger(HandlerGlobalExceptionProcessTest.class);

	@Test
	public void testRetry() {
		HandlerGlobalExceptionProcessTest.LOG.debug("jBPM unit test sample");

		addWorkItemHandler("WorkItemHandler", new WorkItemHandlerThrowingException(new ProcessWorkItemHandlerException(
				"HandlerGlobalExceptionProcess", "RETRY", new RuntimeException("Exception inside Test Case"))));

		final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/ExceptionToErrorProcess.bpmn",
				"net/a/g/jbpm/pattern/HandlerGlobalExceptionProcess.bpmn");
		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();

		kieSession.addEventListener((ProcessEventListener) new PatternProcessListener());

		// kieSession.getPro

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);

		try {
			ProcessInstance processInstance = kieSession.startProcess("ExceptionToErrorProcess", params);
			fail("Ne doit pas passer ici");
		} catch (WorkflowRuntimeException e) {
			
		}
//		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
//		// assertNodeTriggered(processInstance.getId(), "ScriptTask");
//		assertNodeTriggered(processInstance.getId(), "Nominal End");

		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}

	@Test
	public void testComplete() {
		HandlerGlobalExceptionProcessTest.LOG.debug("jBPM unit test sample");

		addWorkItemHandler("WorkItemHandler", new WorkItemHandlerThrowingException(new ProcessWorkItemHandlerException(
				"HandlerGlobalExceptionProcess", "COMPLETE", new RuntimeException("Exception inside Test Case"))));

		final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/ExceptionToErrorProcess.bpmn",
				"net/a/g/jbpm/pattern/HandlerGlobalExceptionProcess.bpmn");
		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();

		kieSession.addEventListener((ProcessEventListener) new PatternProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);

		ProcessInstance processInstance = kieSession.startProcess("ExceptionToErrorProcess", params);

		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
		// assertNodeTriggered(processInstance.getId(), "ScriptTask");
		assertNodeTriggered(processInstance.getId(), "Nominal End");

		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}

	@Test
	public void testAbort() {
		HandlerGlobalExceptionProcessTest.LOG.debug("jBPM unit test sample");

		addWorkItemHandler("WorkItemHandler", new WorkItemHandlerThrowingException(new ProcessWorkItemHandlerException(
				"HandlerGlobalExceptionProcess", "ABORT", new RuntimeException("Exception inside Test Case"))));

		final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/ExceptionToErrorProcess.bpmn",
				"net/a/g/jbpm/pattern/HandlerGlobalExceptionProcess.bpmn");
		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();

		kieSession.addEventListener((ProcessEventListener) new PatternProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);

		ProcessInstance processInstance = kieSession.startProcess("ExceptionToErrorProcess", params);

		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
		// assert
		// assertNodeTriggered(processInstance.getId(), "ScriptTask");
		assertNodeTriggered(processInstance.getId(), "Nominal End");

		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}

}