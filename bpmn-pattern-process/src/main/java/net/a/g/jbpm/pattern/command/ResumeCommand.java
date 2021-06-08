package net.a.g.jbpm.pattern.command;

import org.jbpm.process.instance.command.ResumeProcessInstanceCommand;
import org.kie.api.executor.Command;
import org.kie.api.executor.CommandContext;
import org.kie.api.executor.ExecutionResults;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResumeCommand implements Command {

	public static Logger LOG = LoggerFactory.getLogger(ResumeCommand.class);

	@Override
	public ExecutionResults execute(CommandContext ctx) throws Exception {
		ExecutionResults ret =  new ExecutionResults();

		LOG.warn("ResumeCommand {}", ctx);

		String deploymentId = (String) ctx.getData("deploymentId");
		if (deploymentId == null) {
			deploymentId = (String) ctx.getData("DeploymentId");
		}
		Long processInstanceId = new Long((Integer) ctx.getData("processInstanceId"));
		if (processInstanceId == null) {
			processInstanceId = new Long((Integer) ctx.getData("processInstanceId"));
		}

		if (deploymentId == null || processInstanceId == null) {
			throw new IllegalArgumentException("Deployment id and processInstanceId is required");
		}

		RuntimeManager runtimeManager = RuntimeManagerRegistry.get().getManager(deploymentId);
		if (runtimeManager == null) {
			throw new IllegalArgumentException("No runtime manager found for deployment id " + deploymentId);
		}
		RuntimeEngine engine = null;
		try {

			engine = runtimeManager.getRuntimeEngine(ProcessInstanceIdContext.get(processInstanceId));

			ResumeProcessInstanceCommand spic = new ResumeProcessInstanceCommand();
			spic.setProcessInstanceId(processInstanceId);
			
			engine.getKieSession().execute(spic);
			
		} catch (Exception e) {
			LOG.warn("ResumeCommand {}", e);
			ret.setData("exception", e);
		} finally {
			runtimeManager.disposeRuntimeEngine(engine);
		}
		return ret;
	}

}