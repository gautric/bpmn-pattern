package net.a.g.jbpm.pattern.wih;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
//import org.drools.core.process.instance.impl.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitWorkItemHandler implements org.kie.api.runtime.process.WorkItemHandler {

	private static Logger LOG = LoggerFactory.getLogger(WaitWorkItemHandler.class);

	public WaitWorkItemHandler() {
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		LOG.info("Calling Nominal {}", WaitWorkItemHandler.class);
		
		
		// Prefer this instead of Thread.sleep
		for(long i = 0; i< 1000000*2; i++) {
			long t = (long) (Math.sin(i)*Math.sin(i)*Math.sin(i)*Math.sin(i));
			long t1 = (long) (Math.sin(i)*Math.sin(i)*Math.sin(i)*Math.sin(i));
			long t2 = (long) (Math.sin(i)*Math.sin(i)*Math.sin(i)*Math.sin(i));
			long t3 = (long) (Math.sin(i)*Math.sin(i)*Math.sin(i)*Math.sin(i));
			long t4 = (long) (Math.sin(i)*Math.sin(i)*Math.sin(i)*Math.sin(i));
			long t5 = (long) (Math.sin(i)*Math.sin(i)*Math.sin(i)*Math.sin(i));
			long t6 = (long) (Math.sin(i)*Math.sin(i)*Math.sin(i)*Math.sin(i));
			long tt = (long) (Math.sqrt(t6)*Math.sqrt(t6)*Math.sqrt(t6)*Math.sqrt(t6)*Math.sqrt(t6)*Math.sqrt(t6));

		}

		Map<String, Object> result = new HashMap<String, Object>();
		int a = (int) workItem.getParameter("a");
		int b = (int) workItem.getParameter("b");
		int r = (int) workItem.getParameter("result");

		
		r = r + 1;

		LOG.info(" a {} b {} result {}", a, b, r);
		
		result.put("result", r);
		LOG.info("End Nominal {}", WaitWorkItemHandler.class);

		manager.completeWorkItem(workItem.getId(), result);
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		LOG.info("Abort {}", WaitWorkItemHandler.class);
	}

}
