package net.a.g.jbpm.pattern.wih;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
//import org.drools.core.process.instance.impl.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkItemHandlerWaitException implements org.kie.api.runtime.process.WorkItemHandler {

	private static Logger LOG = LoggerFactory.getLogger(WorkItemHandlerWaitException.class);

	public WorkItemHandlerWaitException() {
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		LOG.info("Calling Nominal {}", WorkItemHandlerWaitException.class);
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
		}

		Map<String, Object> result = new HashMap<String, Object>();
		int a = (int) workItem.getParameter("a");
		int b = (int) workItem.getParameter("b");
		int r = (int) workItem.getParameter("result");

		r = r + 1;

		LOG.info(" a {} b {} result {}", a, b, r);

		System.out.println("" + a + " " + b + " " + r);
		
		result.put("result", r);
		LOG.info("End Nominal {}", WorkItemHandlerWaitException.class);

		manager.completeWorkItem(workItem.getId(), result);
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		LOG.info("Abort {}", WorkItemHandlerWaitException.class);
	}

}
