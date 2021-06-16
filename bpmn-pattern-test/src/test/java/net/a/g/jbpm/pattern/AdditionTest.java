package net.a.g.jbpm.pattern;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;
import org.junit.Test;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.a.g.jbpm.pattern.listener.ContextListener;
import net.a.g.jbpm.pattern.listener.MDCProcessListener;

public class AdditionTest extends JbpmJUnitBaseTestCase {

	private static final Logger LOG = LoggerFactory.getLogger(AdditionTest.class);

	public AdditionTest() {
		super(false, false);

	}

	@Test
	public void testAddition() {
		LOG.debug("jBPM unit test sample");

		Map<String, ResourceType> app = new HashMap<String, ResourceType>();
		app.put("net/a/g/jbpm/pattern/AdditionProcess.bpmn", ResourceType.BPMN2);
		app.put("net/a/g/jbpm/pattern/AdditionRule.dmn", ResourceType.DMN);

		final RuntimeManager runtimeManager = createRuntimeManager(app);

		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();
		kieSession.addEventListener((ProcessEventListener) new MDCProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("a", 21);
		params.put("b", 21);

		final ProcessInstance processInstance = kieSession.startProcess("AdditionProcess", params);

		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
		assertNodeTriggered(processInstance.getId(), "Addition DMN");
		assertNodeTriggered(processInstance.getId(), "Print Result");
		assertNodeTriggered(processInstance.getId(), "Addition");
		assertNodeTriggered(processInstance.getId(), "Addition done");
		assertProcessVarExists(processInstance, "result", "a", "b");
		
		assertEquals(42, ((BigDecimal) ((WorkflowProcessInstanceImpl) processInstance).getVariable("result")).intValue());

		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}

	@Test
	public void testAdditionParent() throws InterruptedException {
		LOG.debug("jBPM unit test sample");

		Map<String, ResourceType> app = new HashMap<String, ResourceType>();
		app.put("net/a/g/jbpm/pattern/AdditionParentProcess.bpmn", ResourceType.BPMN2);
		app.put("net/a/g/jbpm/pattern/AdditionProcess.bpmn", ResourceType.BPMN2);
		app.put("net/a/g/jbpm/pattern/AdditionRule.dmn", ResourceType.DMN);

		final RuntimeManager runtimeManager = createRuntimeManager(app);

		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();
		kieSession.addEventListener((ProcessEventListener) new ContextListener());
		kieSession.addEventListener((ProcessEventListener) new MDCProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("a", 41);
		params.put("b", 1);
		params.put("timer", "PT1S");

		final ProcessInstance processInstance = kieSession.startProcess("AdditionParentProcess", params);

		Thread.sleep(1500);

		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
		assertNodeTriggered(processInstance.getId(), "Addition DMN");
		assertNodeTriggered(processInstance.getId(), "Print Result");
		assertNodeTriggered(processInstance.getId(), "Addition");
		assertNodeTriggered(processInstance.getId(), "Addition done");
		assertProcessVarExists(processInstance, "result", "a", "b");

		assertEquals(42, ((BigDecimal) ((WorkflowProcessInstanceImpl) processInstance).getVariable("result")).intValue());

		
		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}
}