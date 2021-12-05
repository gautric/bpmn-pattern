package net.a.g.jbpm.pattern.listener;

import org.jbpm.process.core.async.AsyncExecutionMarker;
import org.jbpm.process.instance.impl.ProcessInstanceImpl;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieRuntime;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.process.CorrelationKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextListener implements ProcessEventListener, RuleRuntimeEventListener {

	private static final String JBPM_MDC_PREFIX = System.getProperty("jbpm.mdc.prefix", "jbpm") + ".";
	private static final String NODE_TYPE = JBPM_MDC_PREFIX + "NodeType";
	private static final String NODE_NAME = JBPM_MDC_PREFIX + "NodeName";
	private static final String NEUTRAL = "";
	private static final String RUNTIME_MANAGER = JBPM_MDC_PREFIX + "RuntimeManager";
	private static final String RUNTIME_STRATEGY = JBPM_MDC_PREFIX + "RuntimeStrategy";
	private static final String EXTERNAL_ID = JBPM_MDC_PREFIX + "ExternalId";
	private static final String VERSION = JBPM_MDC_PREFIX + "Version";
	private static final String CORRELATION_KEY = JBPM_MDC_PREFIX + "CorrelationKey";
	private static final String STATE = JBPM_MDC_PREFIX + "State";
	private static final String PARENT_PROCESS_INSTANCE_ID = JBPM_MDC_PREFIX + "ParentProcessInstanceId";
	private static final String PROCESS_INSTANCE_ID = JBPM_MDC_PREFIX + "ProcessInstanceId";
	private static final String PROCESS_NAME = JBPM_MDC_PREFIX + "ProcessName";
	private static final String PROCESS_ID = JBPM_MDC_PREFIX + "ProcessId";
	private static final String ASYNC = JBPM_MDC_PREFIX + "Async";

	private Logger LOG = LoggerFactory.getLogger("net.a.g.jbpm.pattern.Context");

	private void injectMDC(ProcessInstance pse, KieRuntime kieSession) {
		ProcessInstanceImpl pi = (ProcessInstanceImpl) pse;
		org.slf4j.MDC.put(PROCESS_ID, pi.getProcessId());
		org.slf4j.MDC.put(PROCESS_NAME, pi.getProcessName());
		org.slf4j.MDC.put(PROCESS_INSTANCE_ID, NEUTRAL + pi.getId());
		if (pi.getParentProcessInstanceId() != -1) {
			org.slf4j.MDC.put(PARENT_PROCESS_INSTANCE_ID, NEUTRAL + pi.getParentProcessInstanceId());
		}
		org.slf4j.MDC.put(STATE, NEUTRAL + pi.getState());

		CorrelationKey correlationKey = (CorrelationKey) pi.getMetaData().get(CORRELATION_KEY);
		if (correlationKey != null)
			org.slf4j.MDC.put(CORRELATION_KEY, correlationKey.getName());

		org.slf4j.MDC.put(VERSION, pi.getProcess().getVersion());
		org.slf4j.MDC.put(EXTERNAL_ID, ((RuleFlowProcessInstance) pse).getDeploymentId());

		if (kieSession != null) {
			org.slf4j.MDC.put(RUNTIME_STRATEGY, ((RuntimeManager) kieSession.getEnvironment().get(RUNTIME_MANAGER))
					.getClass().getSimpleName().replace(RUNTIME_MANAGER, NEUTRAL));
		}

		org.slf4j.MDC.put(ASYNC, NEUTRAL + AsyncExecutionMarker.isAsync());

	}

	private void removeMDC(Object l) {
		org.slf4j.MDC.remove(PROCESS_ID);
		org.slf4j.MDC.remove(PROCESS_NAME);
		org.slf4j.MDC.remove(PROCESS_INSTANCE_ID);
		org.slf4j.MDC.remove(PARENT_PROCESS_INSTANCE_ID);
		org.slf4j.MDC.remove(STATE);
		org.slf4j.MDC.remove(CORRELATION_KEY);
		org.slf4j.MDC.remove(VERSION);
		org.slf4j.MDC.remove(EXTERNAL_ID);
		org.slf4j.MDC.remove(RUNTIME_STRATEGY);

		org.slf4j.MDC.remove(ASYNC);
		// For node only
		org.slf4j.MDC.remove(NODE_TYPE);
		org.slf4j.MDC.remove(NODE_NAME);
	}

	@Override
	public void beforeProcessStarted(ProcessStartedEvent event) {
		injectMDC(event.getProcessInstance(), ((KieRuntime) event.getKieRuntime()));
		LOG.info("Processus Start - {}", event.getProcessInstance().getProcessName());
		removeMDC(event.getProcessInstance());
	}

	@Override
	public void beforeProcessCompleted(ProcessCompletedEvent event) {
	}

	@Override
	public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
		injectMDC(event.getProcessInstance(), ((KieRuntime) event.getKieRuntime()));
		if (event.getNodeInstance().getNodeName() != null) {
			org.slf4j.MDC.put(NODE_NAME, event.getNodeInstance().getNodeName());
			org.slf4j.MDC.put(NODE_TYPE, event.getNodeInstance().getNode().getNodeType().name());
		}
		LOG.info("Node - {}", event.getNodeInstance().getNodeName());
	}

	@Override
	public void beforeNodeLeft(ProcessNodeLeftEvent event) {
	}

	@Override
	public void afterVariableChanged(ProcessVariableChangedEvent event) {
	}

	@Override
	public void afterProcessStarted(ProcessStartedEvent event) {
	}

	@Override
	public void afterProcessCompleted(ProcessCompletedEvent event) {
		injectMDC(event.getProcessInstance(), ((KieRuntime) event.getKieRuntime()));
		LOG.info("Processus End ({}) - {}", event.getProcessInstance().getId(),
				event.getProcessInstance().getProcessName());
		removeMDC(event.getProcessInstance());
	}

	@Override
	public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
		removeMDC(event.getProcessInstance());
	}

	@Override
	public void afterNodeLeft(ProcessNodeLeftEvent event) {
		removeMDC(event.getProcessInstance());
	}

	@Override
	public void beforeVariableChanged(ProcessVariableChangedEvent event) {
		injectMDC(event.getProcessInstance(), ((KieRuntime) event.getKieRuntime()));
		LOG.info("Update Variable ({}) - {} = {} <- {}", event.getProcessInstance().getId(), event.getVariableId(),
				event.getNewValue(), event.getOldValue());
		removeMDC(event.getProcessInstance());
	}

	@Override
	public void objectInserted(ObjectInsertedEvent event) {
	}

	@Override
	public void objectUpdated(ObjectUpdatedEvent event) {
	}

	@Override
	public void objectDeleted(ObjectDeletedEvent event) {

	}

}