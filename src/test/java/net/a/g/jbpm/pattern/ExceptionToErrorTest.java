package net.a.g.jbpm.pattern;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;

import net.a.g.jbpm.pattern.wih.WorkItemHandler;

public class ExceptionToErrorTest extends JbpmJUnitBaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionToErrorTest.class);

    @Test
    public void test() {
        ExceptionToErrorTest.LOG.debug("jBPM unit test sample");

        final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/ExceptionToErrorProcess.bpmn");
        final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
        final KieSession kieSession = runtimeEngine.getKieSession();
        kieSession.getWorkItemManager().registerWorkItemHandler("WorkItemHandler", new WorkItemHandler());

        kieSession.addEventListener(new ProcessEventListener() {

			private  Logger LOG = LoggerFactory.getLogger("net.a.g.jbpm.pattern.ProcessEventListener");

			@Override
			public void beforeProcessStarted(ProcessStartedEvent event) {
				LOG.info("Start Processus : {}", event.getProcessInstance().getProcessName());
			}

			@Override
			public void beforeProcessCompleted(ProcessCompletedEvent event) {
			}

			@Override
			public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
				LOG.info("Node Called : {}", event.getNodeInstance().getNodeName());
			}

			@Override
			public void beforeNodeLeft(ProcessNodeLeftEvent event) {
			}

			@Override
			public void afterVariableChanged(ProcessVariableChangedEvent event) {
			}

			@Override
			public void afterProcessStarted(ProcessStartedEvent event) {
				LOG.info("End Processus : {}", event.getProcessInstance().getProcessName());

			}

			@Override
			public void afterProcessCompleted(ProcessCompletedEvent event) {
			}

			@Override
			public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
			}

			@Override
			public void afterNodeLeft(ProcessNodeLeftEvent event) {
			}

			@Override
			public void beforeVariableChanged(ProcessVariableChangedEvent event) {
				
			}
		});


        final ProcessInstance processInstance = kieSession.startProcess("ExceptionToErrorProcess");

        assertProcessInstanceNotActive(processInstance.getId(), kieSession);
        assertNodeTriggered(processInstance.getId(), "ScriptTask");
		assertNodeTriggered(processInstance.getId(), "Error End");

        runtimeManager.disposeRuntimeEngine(runtimeEngine);
        runtimeManager.close();
    }
}