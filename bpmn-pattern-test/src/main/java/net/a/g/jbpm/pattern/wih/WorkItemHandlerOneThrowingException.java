package net.a.g.jbpm.pattern.wih;

import java.util.Collections;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
//import org.drools.core.process.instance.impl.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.a.g.jbpm.pattern.wih.WorkItemHandlerThrowingException;

public class WorkItemHandlerOneThrowingException implements org.kie.api.runtime.process.WorkItemHandler {

	private static Logger LOG = LoggerFactory.getLogger(WorkItemHandlerOneThrowingException.class);
	
	private java.lang.Exception exceptionToThrow;

	private boolean thrauw;

	public WorkItemHandlerOneThrowingException() {
	}
	
	public WorkItemHandlerOneThrowingException(java.lang.Exception exceptionToThrow) {
		this.exceptionToThrow = exceptionToThrow;
	}
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		LOG.info("Nominal {}", WorkItemHandlerThrowingException.class);

		if (!thrauw) {
			thrauw = true;
			LOG.info("Nominal (RuntimeException) exceptionToThrow");
			throw (RuntimeException) exceptionToThrow;
		}

		manager.completeWorkItem(workItem.getId(), Collections.emptyMap());
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		LOG.info("Abort {}", WorkItemHandlerOneThrowingException.class);
		manager.abortWorkItem(workItem.getId());
	}

}
