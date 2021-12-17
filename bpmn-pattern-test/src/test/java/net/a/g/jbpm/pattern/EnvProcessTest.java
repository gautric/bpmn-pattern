package net.a.g.jbpm.pattern;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.a.g.jbpm.pattern.wih.CopyInToOutWorkItemHandler;

public class EnvProcessTest extends JbpmJUnitBaseTestCase {
	private static final Logger LOG = LoggerFactory.getLogger(EnvProcessTest.class);

	@Test
	public void testWithEnv_EnvProcess() {
		EnvProcessTest.LOG.debug("jBPM unit test sample");

		final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/EnvProcess.bpmn");
		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();

		kieSession.getWorkItemManager().registerWorkItemHandler("Human Task", new CopyInToOutWorkItemHandler());
		kieSession.addEventListener((ProcessEventListener) new PatternProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);

		System.setProperty("myURL", "http://www.example.com");

		ProcessInstance processInstance = kieSession.startProcess("EnvProcess", params);

		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
		assertNodeTriggered(processInstance.getId(), "Executer Env");
		assertNodeTriggered(processInstance.getId(), "NOP");
		assertNodeTriggered(processInstance.getId(), "Script Node");
		assertNodeTriggered(processInstance.getId(), "Env Exécutée");

		assertEquals(((RuleFlowProcessInstance) processInstance).getVariable("stringIn"), "http://www.example.com");

		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}

	@Test
	public void testWithoutEnv_EnvProcess() {
		EnvProcessTest.LOG.debug("jBPM unit test sample");

		final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/EnvProcess.bpmn");
		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();

		kieSession.getWorkItemManager().registerWorkItemHandler("Human Task", new CopyInToOutWorkItemHandler());
		kieSession.addEventListener((ProcessEventListener) new PatternProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);

		ProcessInstance processInstance = kieSession.startProcess("EnvProcess", params);

		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
		assertNodeTriggered(processInstance.getId(), "Executer Env");
		assertNodeTriggered(processInstance.getId(), "NOP");
		assertNodeTriggered(processInstance.getId(), "Script Node");
		assertNodeTriggered(processInstance.getId(), "Env Exécutée");

		assertEquals(((RuleFlowProcessInstance) processInstance).getVariable("stringIn"), "http://default.example.com");

		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}
}