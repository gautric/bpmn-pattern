package net.a.g.jbpm.pattern.wih;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.jbpm.process.workitem.core.AbstractWorkItemHandler;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.workflow.core.node.BoundaryEventNode;
import org.jbpm.workflow.instance.impl.NodeInstanceImpl;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainProcessWIH extends AbstractWorkItemHandler implements org.kie.api.runtime.process.WorkItemHandler {

	private static Logger LOG = LoggerFactory.getLogger(MainProcessWIH.class);

	public MainProcessWIH() {
		super(null);
	}

	public MainProcessWIH(KieSession ks) {
		super(ks);
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		LOG.info("Calling Nominal {}", MainProcessWIH.class);

		boolean a = (boolean) workItem.getParameter("bool");
		int b = (int) workItem.getParameter("integer");
		String r = (String) workItem.getParameter("string");

		Map<String, Object> resultParam = new HashMap<String, Object>();

		String result = a + "-" + b + "-" + r;

		LOG.info("String {} = {} +  -  + {} +  -  + {} ", result, a, b, r);

		resultParam.put("result", result);
		LOG.info("End Nominal {}", MainProcessWIH.class);

		if (b == 42) {
			NodeInstanceImpl ni = (NodeInstanceImpl) this.getNodeInstance(workItem);

			String uuid = (String) ni.getNode().getNodeUniqueId();

			ProcessInstance pi = this.getProcessInstance(workItem);

			Optional<Object> boundary = Stream.of(((RuleFlowProcessInstance) pi).getNodeContainer().getNodes())
					.filter(e -> e instanceof BoundaryEventNode).map(e -> (BoundaryEventNode) e)
					.filter(e -> e.getAttachedToNodeId().compareTo(uuid) == 0)
					.filter(e -> e.getMetaData().containsKey("ErrorEvent"))
					.map(e -> e.getMetaData("ErrorEvent"))
					.findFirst();

			if (boundary.isPresent()) {
				String eventType = "Error-" + uuid + "-" + boundary.get();
				((org.drools.core.process.instance.WorkItemManager) manager).signalEvent(eventType,
						(org.drools.core.process.instance.WorkItem) workItem, workItem.getProcessInstanceId());
			}
		}

		manager.completeWorkItem(workItem.getId(), resultParam);
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		LOG.info("Abort {}", MainProcessWIH.class);
	}

}
