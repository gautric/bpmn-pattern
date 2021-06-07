package net.a.g.jbpm.pattern.command;

import org.jbpm.process.instance.command.SuspendProcessInstanceCommand;
import org.kie.api.executor.Command;
import org.kie.api.executor.CommandContext;
import org.kie.api.executor.ExecutionResults;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SuspendCommand implements Command {

	public static Logger LOG = LoggerFactory.getLogger(SuspendCommand.class);

	@Override
	public ExecutionResults execute(CommandContext ctx) throws Exception {

		LOG.warn("SuspendCommand {}", ctx);

		String deploymentId = (String) ctx.getData("deploymentId");
		if (deploymentId == null) {
			deploymentId = (String) ctx.getData("DeploymentId");
		}
		Long processInstanceId = new Long((Integer) ctx.getData("processInstanceId"));
		if (processInstanceId == null) {
			processInstanceId = new Long((Integer) ctx.getData("processInstanceId"));
		}

		if (deploymentId == null || processInstanceId == null) {
			throw new IllegalArgumentException("Deployment id and signal name is required");
		}

		RuntimeManager runtimeManager = RuntimeManagerRegistry.get().getManager(deploymentId);
		if (runtimeManager == null) {
			throw new IllegalArgumentException("No runtime manager found for deployment id " + deploymentId);
		}
		RuntimeEngine engine = null;
		try {

			engine = runtimeManager.getRuntimeEngine(ProcessInstanceIdContext.get(processInstanceId));
			
			SuspendProcessInstanceCommand spic = new SuspendProcessInstanceCommand();
			spic.setProcessInstanceId(processInstanceId);
			
			engine.getKieSession().execute(spic);
			
			return new ExecutionResults();
		} catch (Exception e) {
			LOG.warn("SuspendCommand {}", e);

		} finally {
			runtimeManager.disposeRuntimeEngine(engine);
		}
		return new ExecutionResults();
	}

}
