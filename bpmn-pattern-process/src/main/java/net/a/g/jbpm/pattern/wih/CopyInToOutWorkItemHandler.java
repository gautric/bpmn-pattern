package net.a.g.jbpm.pattern.wih;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopyInToOutWorkItemHandler implements org.kie.api.runtime.process.WorkItemHandler {

	private static Logger LOG = LoggerFactory.getLogger(CopyInToOutWorkItemHandler.class);

	public CopyInToOutWorkItemHandler() {
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		LOG.info("Calling Nominal {}", CopyInToOutWorkItemHandler.class);
		//System.getenv().
		//System.getProperty(null, null)
		manager.completeWorkItem(workItem.getId(), workItem.getParameters());
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		LOG.info("Abort {}", CopyInToOutWorkItemHandler.class);
	}

}
