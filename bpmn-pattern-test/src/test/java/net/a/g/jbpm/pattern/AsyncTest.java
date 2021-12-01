package net.a.g.jbpm.pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jbpm.executor.ExecutorServiceFactory;
import org.jbpm.executor.impl.ExecutorServiceImpl;
import org.jbpm.runtime.manager.impl.DefaultRegisterableItemsFactory;
import org.jbpm.services.task.identity.JBossUserGroupCallbackImpl;
import org.jbpm.test.listener.process.NodeLeftCountDownProcessEventListener;
import org.jbpm.test.util.AbstractExecutorBaseTest;
import org.jbpm.test.util.ExecutorTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.executor.ExecutorService;
import org.kie.api.executor.RequestInfo;
import org.kie.api.executor.STATUS;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.query.QueryContext;
import org.kie.api.task.UserGroupCallback;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.test.util.db.PoolingDataSourceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.a.g.jbpm.pattern.listener.MDCProcessListener;

/**
 * Sub sample :
 * https://github.com/kiegroup/jbpm/blob/master/jbpm-services/jbpm-executor/src/test/java/org/jbpm/executor/impl/wih/AsyncContinuationSupportTest.java
 * 
 */
public class AsyncTest extends AbstractExecutorBaseTest {

	private static final Logger logger = LoggerFactory.getLogger(AsyncTest.class);

	private PoolingDataSourceWrapper pds;
	private UserGroupCallback userGroupCallback;
	private RuntimeManager manager;
	private ExecutorService executorService;
	private EntityManagerFactory emf = null;

	@Before
	public void setup() {
		ExecutorTestUtil.cleanupSingletonSessionId();
		pds = ExecutorTestUtil.setupPoolingDataSource();
		Properties properties = new Properties();
		properties.setProperty("mary", "HR");
		properties.setProperty("john", "HR");
		userGroupCallback = new JBossUserGroupCallbackImpl(properties);
		executorService = buildExecutorService();
	}

	@After
	public void teardown() {
		executorService.destroy();
		if (manager != null) {
			RuntimeManagerRegistry.get().remove(manager.getIdentifier());
			manager.close();
		}
		if (emf != null) {
			emf.close();
		}
		pds.close();
	}

	@Test(timeout = 1000000)
	public void fullAsyncTest() throws Exception {
		// JBPM-7414 // there are two paths for end process , therefore it is executed
		// twice
		final NodeLeftCountDownProcessEventListener countDownListener = new NodeLeftCountDownProcessEventListener(
				"Process Async Executé", 1);
		final NodeTriggerCountListener Async = new NodeTriggerCountListener("Task Async");

		final NodeTriggerCountListener Sync = new NodeTriggerCountListener("Task Sync");

		RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder()
				.userGroupCallback(userGroupCallback)
				.addAsset(ResourceFactory.newClassPathResource("net/a/g/jbpm/pattern/AsyncTestProcess.bpmn"),
						ResourceType.BPMN2)
				.addEnvironmentEntry("ExecutorService", executorService).addEnvironmentEntry("AsyncMode", "true")
				.registerableItemsFactory(new DefaultRegisterableItemsFactory() {

					@Override
					public List<ProcessEventListener> getProcessEventListeners(RuntimeEngine runtime) {
						List<ProcessEventListener> listeners = super.getProcessEventListeners(runtime);
						listeners.add(countDownListener);
						listeners.add(Sync);
						listeners.add(Async);
						listeners.add(new PatternProcessListener());
						listeners.add(new MDCProcessListener());

						return listeners;
					}

				}).get();

		manager = RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(environment);
		assertNotNull(manager);

		RuntimeEngine runtime = manager.getRuntimeEngine(EmptyContext.get());
		KieSession ksession = runtime.getKieSession();
		assertNotNull(ksession);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("stringIn", "AAA");
		params.put("booleanIn", true);
		params.put("integerIn", 321);

		ProcessInstance processInstance = ksession.startProcess("AsyncTestProcess", params);

		assertEquals(ProcessInstance.STATE_ACTIVE, processInstance.getState());
		long processInstanceId = processInstance.getId();

		countDownListener.waitTillCompleted();

		processInstance = runtime.getKieSession().getProcessInstance(processInstanceId);
		assertNull(processInstance);

		assertEquals(1, Async.getCount().intValue());
		assertEquals(1, Sync.getCount().intValue());
	}
	
	@Test(timeout = 1000000)
	public void onlyAsyncTest() throws Exception {
		// JBPM-7414 // there are two paths for end process , therefore it is executed
		// twice
		final NodeLeftCountDownProcessEventListener countDownListener = new NodeLeftCountDownProcessEventListener(
				"Process Async Executé", 1);
		final NodeTriggerCountListener Async = new NodeTriggerCountListener("Task Async");

		final NodeTriggerCountListener Sync = new NodeTriggerCountListener("Task Sync");

		RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder()
				.userGroupCallback(userGroupCallback)
				.addAsset(ResourceFactory.newClassPathResource("net/a/g/jbpm/pattern/AsyncTestProcess.bpmn"),
						ResourceType.BPMN2)
				.addEnvironmentEntry("ExecutorService", executorService)
				//.addEnvironmentEntry("AsyncMode", "true")
				.registerableItemsFactory(new DefaultRegisterableItemsFactory() {

					@Override
					public List<ProcessEventListener> getProcessEventListeners(RuntimeEngine runtime) {
						List<ProcessEventListener> listeners = super.getProcessEventListeners(runtime);
						listeners.add(countDownListener);
						listeners.add(Sync);
						listeners.add(Async);
						listeners.add(new PatternProcessListener());
						listeners.add(new MDCProcessListener());

						return listeners;
					}

				}).get();

		manager = RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(environment);
		assertNotNull(manager);

		RuntimeEngine runtime = manager.getRuntimeEngine(EmptyContext.get());
		KieSession ksession = runtime.getKieSession();
		assertNotNull(ksession);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("stringIn", "AAA");
		params.put("booleanIn", true);
		params.put("integerIn", 321);

		ProcessInstance processInstance = ksession.startProcess("AsyncTestProcess", params);

		assertEquals(ProcessInstance.STATE_ACTIVE, processInstance.getState());
		long processInstanceId = processInstance.getId();

		countDownListener.waitTillCompleted();

		processInstance = runtime.getKieSession().getProcessInstance(processInstanceId);
		assertNull(processInstance);

		assertEquals(1, Async.getCount().intValue());
		assertEquals(1, Sync.getCount().intValue());
	}

	private static class NodeTriggerCountListener extends DefaultProcessEventListener {
		private AtomicInteger count = new AtomicInteger(0);
		private String nodeName;

		private NodeTriggerCountListener(String nodeName) {
			this.nodeName = nodeName;
		}

		@Override
		public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
			if (event.getNodeInstance().getNodeName().equals(nodeName)) {
				count.getAndIncrement();
			}
		}

		public AtomicInteger getCount() {
			return count;
		}
	};

	private boolean waitForAllJobsToComplete() throws Exception {
		int attempts = 10;
		do {
			List<RequestInfo> runningOrQueued = executorService
					.getRequestsByStatus(Arrays.asList(STATUS.RUNNING, STATUS.QUEUED), new QueryContext());
			attempts--;

			if (runningOrQueued.isEmpty()) {
				return true;
			}

			Thread.sleep(500);

		} while (attempts > 0);

		return false;
	}

	private ExecutorService buildExecutorService() {
		emf = Persistence.createEntityManagerFactory("org.jbpm.executor");

		executorService = ExecutorServiceFactory.newExecutorService(emf);
		executorService.setThreadPoolSize(2);
		executorService.setInterval(1);
		executorService.setTimeunit(TimeUnit.MINUTES);
		executorService.setRetries(3);

		((ExecutorServiceImpl) executorService).addAsyncJobListener(new PatternProcessListener());

		executorService.init();

		return executorService;
	}
}