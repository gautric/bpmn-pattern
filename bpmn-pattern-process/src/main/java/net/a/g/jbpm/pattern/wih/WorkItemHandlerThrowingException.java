package net.a.g.jbpm.pattern.wih;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
//import org.drools.core.process.instance.impl.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkItemHandlerThrowingException implements org.kie.api.runtime.process.WorkItemHandler {

	private static Logger LOG = LoggerFactory.getLogger(WorkItemHandlerThrowingException.class);
	
	private java.lang.Exception exceptionToThrow;

	public WorkItemHandlerThrowingException() {
	}
	
	public WorkItemHandlerThrowingException(java.lang.Exception exceptionToThrow) {
		this.exceptionToThrow = exceptionToThrow;
	}
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		LOG.info("Nominal {}", WorkItemHandlerThrowingException.class);
		throw (RuntimeException)exceptionToThrow;
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		LOG.info("Abort {}", WorkItemHandlerThrowingException.class);

	}

}
