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

public class SubProcessSignalAndDrools extends JbpmJUnitBaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(SubProcessSignalAndDrools.class);

    @Test
    public void testInteger() {
        SubProcessSignalAndDrools.LOG.debug("jBPM unit test sample");

        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/SubProcessSignalAndDrools.bpmn");
        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
        final KieSession kieSession = runtimeEngine.getKieSession();

        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);
		params.put("timerIn", "PT5S");
		
	
		ProcessInstance processInstance = kieSession.startProcess("SubProcessSignalAndDrools", params);

		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
        assertProcessInstanceNotActive(processInstance.getId(), kieSession);
		assertNodeTriggered(processInstance.getId(), "Start Processus");
        assertNodeTriggered(processInstance.getId(), "Script");
		assertNodeTriggered(processInstance.getId(), "Wait Timer");
		
		assertNodeTriggered(processInstance.getId(), "Event Switch");
		assertNodeTriggered(processInstance.getId(), "Signal Step");
		assertNodeTriggered(processInstance.getId(), "Delay Step");
		assertNodeTriggered(processInstance.getId(), "Drools Step");
		
		assertNodeTriggered(processInstance.getId(), "Delay Path");
		assertNodeTriggered(processInstance.getId(), "Path Fusion");
		assertNodeTriggered(processInstance.getId(), "Process End");
		
        runtimeManager.disposeRuntimeEngine(runtimeEngine);
        runtimeManager.close();
    }
//    
//    @Test
//    public void testTimer() {
//        SubProcessSignalAndDrools.LOG.debug("jBPM unit test sample");
//
//        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/MonitoringProcess.bpmn");
//        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
//        final KieSession kieSession = runtimeEngine.getKieSession();
//
//        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());
//
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("booleanIn", true);
//		params.put("stringIn", UUID.randomUUID().toString());
//		params.put("integerIn", 24);
//		params.put("timerIn", "PT5S");
//		
//	
//		ProcessInstance processInstance = kieSession.startProcess("MonitoringProcess", params);
//
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		assertNodeTriggered(processInstance.getId(), "Monitoring");
//		assertNodeTriggered(processInstance.getId(), "First Step Task");
//		assertNodeTriggered(processInstance.getId(), "Timer Waiting");
//		assertNodeTriggered(processInstance.getId(), "Timer Task");
//
//		
//        assertProcessInstanceNotActive(processInstance.getId(), kieSession);
//
//		
//        runtimeManager.disposeRuntimeEngine(runtimeEngine);
//        runtimeManager.close();
//    }
//    
//    
//    @Test
//    public void testSignal() {
//        SubProcessSignalAndDrools.LOG.debug("jBPM unit test sample");
//
//        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/MonitoringProcess.bpmn");
//        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
//        final KieSession kieSession = runtimeEngine.getKieSession();
//
//        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());
//
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("booleanIn", true);
//		params.put("stringIn", UUID.randomUUID().toString());
//		params.put("integerIn", 24);
//		params.put("timerIn", "PT60S");
//		
//	
//		ProcessInstance processInstance = kieSession.startProcess("MonitoringProcess", params);
//
//		processInstance.signalEvent("monitoringSignal","");
//		
//		
//		assertNodeTriggered(processInstance.getId(), "Monitoring");
//		assertNodeTriggered(processInstance.getId(), "First Step Task");
//		assertNodeTriggered(processInstance.getId(), "Timer Waiting");
//		assertNodeTriggered(processInstance.getId(), "Signal Task");
//
//		
//        assertProcessInstanceNotActive(processInstance.getId(), kieSession);
//
//		
//        runtimeManager.disposeRuntimeEngine(runtimeEngine);
//        runtimeManager.close();
//    }
//    
//    @Test
//    public void testMessage() {
//        SubProcessSignalAndDrools.LOG.debug("jBPM unit test sample");
//
//        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/MonitoringProcess.bpmn");
//        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
//        final KieSession kieSession = runtimeEngine.getKieSession();
//
//        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());
//
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("booleanIn", true);
//		params.put("stringIn", UUID.randomUUID().toString());
//		params.put("integerIn", 24);
//		params.put("timerIn", "PT60S");
//		
//	
//		ProcessInstance processInstance = kieSession.startProcess("MonitoringProcess", params);
//
//		processInstance.signalEvent("Message-monitoringMessage","hello");
//		
//		
//		assertNodeTriggered(processInstance.getId(), "Monitoring");
//		assertNodeTriggered(processInstance.getId(), "First Step Task");
//		assertNodeTriggered(processInstance.getId(), "Timer Waiting");
//		assertNodeTriggered(processInstance.getId(), "Message Task");
//
//		
//        assertProcessInstanceNotActive(processInstance.getId(), kieSession);
//
//		
//        runtimeManager.disposeRuntimeEngine(runtimeEngine);
//        runtimeManager.close();
//    }
//    
//    @Test
//    public void testTimer50ms() {
//        SubProcessSignalAndDrools.LOG.debug("jBPM unit test sample");
//
//        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/MonitoringProcess.bpmn");
//        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
//        final KieSession kieSession = runtimeEngine.getKieSession();
//
//        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());
//
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("booleanIn", true);
//		params.put("stringIn", UUID.randomUUID().toString());
//		params.put("integerIn", 24);
//		params.put("timerIn", "50");
//		ProcessInstance processInstance = kieSession.startProcess("MonitoringProcess", params);
//
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		
//		assertNodeTriggered(processInstance.getId(), "Monitoring");
//		assertNodeTriggered(processInstance.getId(), "First Step Task");
//		assertNodeTriggered(processInstance.getId(), "Timer Waiting");
//		assertNodeTriggered(processInstance.getId(), "Timer Task");
//
//		
//        assertProcessInstanceNotActive(processInstance.getId(), kieSession);
//
//		
//        runtimeManager.disposeRuntimeEngine(runtimeEngine);
//        runtimeManager.close();
//    }
    
}