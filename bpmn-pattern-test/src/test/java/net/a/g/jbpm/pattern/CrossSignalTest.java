package net.a.g.jbpm.pattern;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.a.g.jbpm.pattern.listener.MDCProcessListener;

public class CrossSignalTest extends JbpmJUnitBaseTestCase {

	private static final Logger LOG = LoggerFactory.getLogger(CrossSignalTest.class);

	public CrossSignalTest() {
		super(false, false);

	}

	@Test
	public void testAddition() {
		LOG.debug("jBPM unit test sample");

		Map<String, ResourceType> app = new HashMap<String, ResourceType>();
		app.put("net/a/g/jbpm/pattern/CrossSignalReceiverTestProcess.bpmn", ResourceType.BPMN2);
		app.put("net/a/g/jbpm/pattern/CrossSignalSenderTestProcess.bpmn", ResourceType.BPMN2);

		final RuntimeManager runtimeManager = createRuntimeManager(app);

		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();
		kieSession.addEventListener((ProcessEventListener)new MDCProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("booleanIn", true);
		params.put("integerIn", 42);
		params.put("stringIn", "receiver");

		final ProcessInstance processInstance = kieSession.startProcess("CrossSignalReceiverTestProcess", params);
		
		params.put("stringIn", "sender");
		
		final ProcessInstance processInstance2 = kieSession.startProcess("CrossSignalSenderTestProcess", params);


	 try {
		Thread.sleep(10000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}
}