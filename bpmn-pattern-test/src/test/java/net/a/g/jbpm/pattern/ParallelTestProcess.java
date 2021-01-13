package net.a.g.jbpm.pattern;

import static org.junit.Assert.fail;

import java.io.IOException;
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

public class ParallelTestProcess extends JbpmJUnitBaseTestCase {

	private static final Logger LOG = LoggerFactory.getLogger(ParallelTestProcess.class);

	public ParallelTestProcess() {
		super(false, false);
	}

	@Test
	public void testParallelTestProcess() throws IOException {
		LOG.debug("jBPM unit test sample");

		Map<String, ResourceType> app = new HashMap<String, ResourceType>();
		app.put("net/a/g/jbpm/pattern/ParallelTestProcess.bpmn", ResourceType.BPMN2);

		final RuntimeManager runtimeManager = createRuntimeManager(app);

		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();
		kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("stringIn", "Input testParallelTestProcess" );
		params.put("booleanIn", false);
		params.put("integerIn", 123);

			
		final ProcessInstance processInstance = kieSession.startProcess("ParallelTestProcess", params);
		
  		kieSession.signalEvent("branche1",null,processInstance.getId());
  		kieSession.signalEvent("branche2",null,processInstance.getId());

  	
  		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
  		
		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}
	
	
	@Test
	public void testParallelTestProcessBranche1() throws IOException {
		LOG.debug("jBPM unit test sample");

		Map<String, ResourceType> app = new HashMap<String, ResourceType>();
		app.put("net/a/g/jbpm/pattern/ParallelTestProcess.bpmn", ResourceType.BPMN2);

		final RuntimeManager runtimeManager = createRuntimeManager(app);

		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();
		kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("stringIn", "Input testParallelTestProcessBranche1" );
		params.put("booleanIn", false);
		params.put("integerIn", 123);

			
		final ProcessInstance processInstance = kieSession.startProcess("ParallelTestProcess", params);
		
  		kieSession.signalEvent("branche1",null,processInstance.getId());

  	
  		assertProcessInstanceActive(processInstance.getId(), kieSession);
  		
		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}
	
	@Test
	public void testParallelTestProcessBranche2() throws IOException {
		LOG.debug("jBPM unit test sample");

		Map<String, ResourceType> app = new HashMap<String, ResourceType>();
		app.put("net/a/g/jbpm/pattern/ParallelTestProcess.bpmn", ResourceType.BPMN2);

		final RuntimeManager runtimeManager = createRuntimeManager(app);

		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();
		kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("stringIn", "Input testParallelTestProcessBranche2" );
		params.put("booleanIn", false);
		params.put("integerIn", 123);
			
		final ProcessInstance processInstance = kieSession.startProcess("ParallelTestProcess", params);
		
  		kieSession.signalEvent("branche2",null,processInstance.getId());
  	
  		assertProcessInstanceActive(processInstance.getId(), kieSession);
  		
		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}
	
	@Test
	public void testParallelTestProcessCheck() throws IOException {
		LOG.debug("jBPM unit test sample");

		Map<String, ResourceType> app = new HashMap<String, ResourceType>();
		app.put("net/a/g/jbpm/pattern/ParallelTestProcess.bpmn", ResourceType.BPMN2);

		final RuntimeManager runtimeManager = createRuntimeManager(app);

		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();
		kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();
			
		params.put("stringIn", "Input testParallelTestProcessCheck" );
		params.put("booleanIn", false);
		params.put("integerIn", 123);
		
		
		final ProcessInstance processInstance = kieSession.startProcess("ParallelTestProcess", params);
		
  		kieSession.signalEvent("branche2",null,processInstance.getId());
  	
  		assertProcessInstanceActive(processInstance.getId(), kieSession);
  		
  		try {
  			assertNodeTriggered(processInstance.getId(), "Script Node");
  			fail("Error");
		} catch (AssertionError e) {
		}
  		
  		kieSession.signalEvent("branche1",null,processInstance.getId());

  		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
  		
		assertNodeTriggered(processInstance.getId(), "Branche 2 Step 2");
		assertNodeTriggered(processInstance.getId(), "Branche 1 Step 2");
		assertNodeTriggered(processInstance.getId(), "Script Node");



		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}
	
	
}