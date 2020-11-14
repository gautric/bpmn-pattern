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

public class HandlingExceptionProcessTest extends JbpmJUnitBaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(HandlingExceptionProcessTest.class);

    @Test
    public void test() {
        HandlingExceptionProcessTest.LOG.debug("jBPM unit test sample");

        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/HandlingExceptionProcess.bpmn");
        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
        final KieSession kieSession = runtimeEngine.getKieSession();

        kieSession.getWorkItemManager().registerWorkItemHandler("Service Task", new org.jbpm.bpmn2.handler.SignallingTaskHandlerDecorator(new org.jbpm.process.workitem.bpmn2.ServiceTaskHandler(),"Error-ExceptionCode"));
        kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());


		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);

	
		ProcessInstance processInstance = kieSession.startProcess("HandlingExceptionProcess", params);

        assertProcessInstanceNotActive(processInstance.getId(), kieSession);
        assertNodeTriggered(processInstance.getId(), "Démarrer Processus lancer une Exception");
		assertNodeTriggered(processInstance.getId(), "Throw Exception");
        assertNodeTriggered(processInstance.getId(), "Handle Exception");
		assertNodeTriggered(processInstance.getId(), "Fin de gestion de l'Exception");
		
        runtimeManager.disposeRuntimeEngine(runtimeEngine);
        runtimeManager.close();
    }
}