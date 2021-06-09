package net.a.g.jbpm.pattern.listener;

import java.util.Collection;

import org.jbpm.kie.services.impl.admin.commands.ListTimersCommand;
import org.jbpm.services.api.admin.TimerInstance;
import org.jbpm.workflow.core.node.TimerNode;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerListener4Process extends DefaultProcessEventListener implements ProcessEventListener {

	private Logger LOG = LoggerFactory.getLogger("net.a.g.jbpm.pattern.Logger");

	private KieSession ksession = null;

	public TimerListener4Process() {
	}

	public TimerListener4Process(KieSession ksession) {
		this.ksession = ksession;
	}

	@Override
	public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
		if (event.getNodeInstance().getNode().getNodeType().name().compareTo("TIMER") == 0) {
			LOG.info("Node Type - {}", event.getNodeInstance().getNode().getNodeType().name());

			org.jbpm.workflow.core.node.TimerNode tn = (TimerNode) event.getNodeInstance().getNode();
			LOG.info("Process Id - {}", event.getProcessInstance().getId());
			LOG.info("Timer - {}", tn.getTimer());
			LOG.info("Timer - {}", tn.getTimer().getName());
			LOG.info("Timer - {}", tn.getTimer().getDelay());
		}
	}

	@Override
	public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
		if (event.getNodeInstance().getNode().getNodeType().name().compareTo("TIMER") == 0) {
			LOG.info("Node Type - {}", event.getNodeInstance().getNode().getNodeType().name());
			org.jbpm.workflow.core.node.TimerNode tn = (TimerNode) event.getNodeInstance().getNode();
			LOG.info("Process Id - {}", event.getProcessInstance().getId());
			LOG.info("Timer - {}", tn.getTimer());
			LOG.info("Timer - {}", tn.getTimer().getName());
			LOG.info("Timer - {}", tn.getTimer().getDelay());
			org.kie.api.runtime.process.ProcessInstance pi = ksession
					.getProcessInstance(event.getProcessInstance().getId(), true);

			Collection<TimerInstance> timers = ksession
					.execute(new ListTimersCommand(event.getProcessInstance().getId()));

			for (TimerInstance timerInstance : timers) {
				LOG.info("TimerInstance > {}", timerInstance);
			}

			LOG.info("Env Entry > {}", ksession.getEnvironment().get("MyEnvEntry"));
			LOG.info("Env Entry > {}", ksession.getEnvironment().get("deploymentId"));

		}

	}

}