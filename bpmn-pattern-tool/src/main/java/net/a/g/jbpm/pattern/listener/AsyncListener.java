package net.a.g.jbpm.pattern.listener;

import org.jbpm.executor.AsynchronousJobEvent;
import org.jbpm.executor.AsynchronousJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncListener implements AsynchronousJobListener {

	private Logger LOG = LoggerFactory.getLogger(AsyncListener.class);

	public AsyncListener() {
	}

	@Override
	public void beforeJobScheduled(AsynchronousJobEvent event) {
		LOG.info("beforeJobScheduled(AsynchronousJobEvent {}", event);
	}

	@Override
	public void afterJobScheduled(AsynchronousJobEvent event) {
		LOG.info("afterJobScheduled(AsynchronousJobEvent {}", event);
	}

	@Override
	public void beforeJobExecuted(AsynchronousJobEvent event) {
		LOG.info("beforeJobExecuted(AsynchronousJobEvent {}", event);
	}

	@Override
	public void afterJobExecuted(AsynchronousJobEvent event) {
		LOG.info("afterJobExecuted(AsynchronousJobEvent {}", event);
	}

	@Override
	public void beforeJobCancelled(AsynchronousJobEvent event) {
		LOG.info("beforeJobCancelled(AsynchronousJobEvent {}", event);
	}

	@Override
	public void afterJobCancelled(AsynchronousJobEvent event) {
		LOG.info("afterJobCancelled(AsynchronousJobEvent {}", event);
	}

}
