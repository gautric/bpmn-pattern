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

public class MonitoringProcessTest extends JbpmJUnitBaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(MonitoringProcessTest.class);

    @Test
    public void testInteger() {
        MonitoringProcessTest.LOG.debug("jBPM unit test sample");

        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/MonitoringProcess.bpmn");
        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
        final KieSession kieSession = runtimeEngine.getKieSession();

        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());


		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);
		params.put("timerIn", "PT10S");
		
	
		ProcessInstance processInstance = kieSession.startProcess("MonitoringProcess", params);

        assertProcessInstanceNotActive(processInstance.getId(), kieSession);
		assertNodeTriggered(processInstance.getId(), "First Step Task");
        assertNodeTriggered(processInstance.getId(), "Conditional Task");
		assertNodeTriggered(processInstance.getId(), "Last Step Task");
		
        runtimeManager.disposeRuntimeEngine(runtimeEngine);
        runtimeManager.close();
    }
    
    @Test
    public void testTimer() {
        MonitoringProcessTest.LOG.debug("jBPM unit test sample");

        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/MonitoringProcess.bpmn");
        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
        final KieSession kieSession = runtimeEngine.getKieSession();

        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());


		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 24);
		params.put("timerIn", "PT5S");
		
	
		ProcessInstance processInstance = kieSession.startProcess("MonitoringProcess", params);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertNodeTriggered(processInstance.getId(), "Monitoring");
		assertNodeTriggered(processInstance.getId(), "First Step Task");
		assertNodeTriggered(processInstance.getId(), "Timer Waiting");
		assertNodeTriggered(processInstance.getId(), "Timer Task");

		
        assertProcessInstanceNotActive(processInstance.getId(), kieSession);

		
        runtimeManager.disposeRuntimeEngine(runtimeEngine);
        runtimeManager.close();
    }
    
    
    @Test
    public void testSignal() {
        MonitoringProcessTest.LOG.debug("jBPM unit test sample");

        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/MonitoringProcess.bpmn");
        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
        final KieSession kieSession = runtimeEngine.getKieSession();

        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());


		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 24);
		params.put("timerIn", "PT60S");
		
	
		ProcessInstance processInstance = kieSession.startProcess("MonitoringProcess", params);

		processInstance.signalEvent("monitoringSignal","");
		
		
		assertNodeTriggered(processInstance.getId(), "Monitoring");
		assertNodeTriggered(processInstance.getId(), "First Step Task");
		assertNodeTriggered(processInstance.getId(), "Timer Waiting");
		assertNodeTriggered(processInstance.getId(), "Signal Task");

		
        assertProcessInstanceNotActive(processInstance.getId(), kieSession);

		
        runtimeManager.disposeRuntimeEngine(runtimeEngine);
        runtimeManager.close();
    }
    
    @Test
    public void testMessage() {
        MonitoringProcessTest.LOG.debug("jBPM unit test sample");

        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/MonitoringProcess.bpmn");
        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
        final KieSession kieSession = runtimeEngine.getKieSession();

        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());


		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 24);
		params.put("timerIn", "PT60S");
		
	
		ProcessInstance processInstance = kieSession.startProcess("MonitoringProcess", params);

		processInstance.signalEvent("Message-monitoringMessage","hello");
		
		
		assertNodeTriggered(processInstance.getId(), "Monitoring");
		assertNodeTriggered(processInstance.getId(), "First Step Task");
		assertNodeTriggered(processInstance.getId(), "Timer Waiting");
		assertNodeTriggered(processInstance.getId(), "Message Task");

		
        assertProcessInstanceNotActive(processInstance.getId(), kieSession);

		
        runtimeManager.disposeRuntimeEngine(runtimeEngine);
        runtimeManager.close();
    }
}