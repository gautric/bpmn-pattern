package net.a.g.jbpm.pattern.listener;

import org.jbpm.executor.AsynchronousJobEvent;
import org.jbpm.executor.AsynchronousJobListener;
import org.jbpm.executor.impl.ExecutorServiceImpl;
import org.jbpm.services.api.service.ServiceRegistry;
import org.kie.internal.executor.api.ExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncListener implements AsynchronousJobListener {
	
	private Logger LOG = LoggerFactory.getLogger("net.a.g.jbpm.pattern.Logger");
	
	public AsyncListener() {
		ExecutorService exec = (ExecutorService) ServiceRegistry.get().service(ServiceRegistry.EXECUTOR_SERVICE);
		((ExecutorServiceImpl)exec).addAsyncJobListener(this);
	}
	
	@Override
	public void beforeJobScheduled(AsynchronousJobEvent event) {
		LOG.error("beforeJobScheduled(AsynchronousJobEvent {}" , event);
	}

	@Override
	public void afterJobScheduled(AsynchronousJobEvent event) {
		LOG.error("afterJobScheduled(AsynchronousJobEvent {}" , event);
	}

	@Override
	public void beforeJobExecuted(AsynchronousJobEvent event) {
		LOG.error("beforeJobExecuted(AsynchronousJobEvent {}" , event);
	}

	@Override
	public void afterJobExecuted(AsynchronousJobEvent event) {
		LOG.error("afterJobExecuted(AsynchronousJobEvent {}" , event);
	}

	@Override
	public void beforeJobCancelled(AsynchronousJobEvent event) {
		LOG.error("beforeJobCancelled(AsynchronousJobEvent {}" , event);
	}

	@Override
	public void afterJobCancelled(AsynchronousJobEvent event) {
		LOG.error("afterJobCancelled(AsynchronousJobEvent {}" , event);
	}

}
