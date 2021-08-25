package net.a.g.jbpm.pattern;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jbpm.process.workitem.bpmn2.ServiceTaskHandler;
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
    public void testNominalCasewDelay() {
        SubProcessSignalAndDrools.LOG.debug("jBPM unit test sample");

        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/SubProcessSignalAndDrools.bpmn");
        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
        final KieSession kieSession = runtimeEngine.getKieSession();

        kieSession.getWorkItemManager().registerWorkItemHandler("Service Task", new ServiceTaskHandler());
        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);
		params.put("timerIn", "PT1S");
		
	
		ProcessInstance processInstance = kieSession.startProcess("SubProcessSignalAndDrools", params);

		try {
			Thread.sleep(3000);
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
		assertNodeTriggered(processInstance.getId(), "Join");
		assertNodeTriggered(processInstance.getId(), "Process End");
		
        runtimeManager.disposeRuntimeEngine(runtimeEngine);
        runtimeManager.close();
    }
    
    
    @Test
    public void testSubProcessSignal() {
        SubProcessSignalAndDrools.LOG.debug("jBPM unit test sample");

        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/SubProcessSignalAndDrools.bpmn");
        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
        final KieSession kieSession = runtimeEngine.getKieSession();

        kieSession.getWorkItemManager().registerWorkItemHandler("Service Task", new ServiceTaskHandler());
        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);
		params.put("timerIn", "PT2S");
		
	
		ProcessInstance processInstance = kieSession.startProcess("SubProcessSignalAndDrools", params);

		try {
			Thread.sleep(1000);
			kieSession.signalEvent("SubProcessSignal", "SubProcessSignalData");
			Thread.sleep(3000);

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
		
		assertNodeTriggered(processInstance.getId(), "Drools Path");
		assertNodeTriggered(processInstance.getId(), "Join");
		assertNodeTriggered(processInstance.getId(), "Process End");
		
        runtimeManager.disposeRuntimeEngine(runtimeEngine);
        runtimeManager.close();
    }
    
    
    @Test
    public void testDirectSignal() {
        SubProcessSignalAndDrools.LOG.debug("jBPM unit test sample");

        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/SubProcessSignalAndDrools.bpmn");
        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
        final KieSession kieSession = runtimeEngine.getKieSession();

        kieSession.getWorkItemManager().registerWorkItemHandler("Service Task", new ServiceTaskHandler());
        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);
		params.put("timerIn", "PT2S");
		
	
		ProcessInstance processInstance = kieSession.startProcess("SubProcessSignalAndDrools", params);

		try {
			Thread.sleep(3000);
			kieSession.signalEvent("DirectSignal", "DirectSignalData");
			Thread.sleep(1000);

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
		
		assertNodeTriggered(processInstance.getId(), "Signal Path");
		assertNodeTriggered(processInstance.getId(), "Join");
		assertNodeTriggered(processInstance.getId(), "Process End");
		
        runtimeManager.disposeRuntimeEngine(runtimeEngine);
        runtimeManager.close();
    }
    
    
}