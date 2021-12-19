package net.a.g.jbpm.pattern;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.codehaus.plexus.util.StringUtils;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.junit.Test;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.a.g.jbpm.pattern.wih.CopyInToOutWorkItemHandler;

public class EnvProcessTest extends JbpmJUnitBaseTestCase {
	private static final String CONF = "conf";
	private static final Logger LOG = LoggerFactory.getLogger(EnvProcessTest.class);

	public class ConfigurationInjectorListener extends DefaultProcessEventListener {
		@Override
		public void beforeProcessStarted(ProcessStartedEvent event) {
			EnvProcessTest.LOG.info("Inject \"{}\" value \"{}\" ", CONF, Configuration.class);
			((WorkflowProcessInstance) event.getProcessInstance()).setVariable(CONF,
					new Configuration((KieSession) event.getKieRuntime()));
		}
	}

	public class Configuration {

		private volatile KieSession kieSession;

		public Configuration(KieSession kieSession) {
			this.kieSession = kieSession;
		}

		public String getProperty(String name, String def) {

			String ret = null;

			if (StringUtils.isNotBlank((String) kieSession.getEnvironment().get(name))) {
				ret = (String) kieSession.getEnvironment().get(name);
			}

			if (ret == null && StringUtils.isNotBlank(System.getProperty(name))) {
				ret = System.getProperty(name);
			}

			if (ret == null && StringUtils.isNotBlank(System.getenv(name))) {
				ret = System.getenv(name);
			}

			if (ret == null) {
				ret = def;
			}

			LOG.debug("name {} ret {} default {}", name, ret, def);
			return ret;
		}
	}

	@Test
	public void testWithEnv_EnvProcess() {
		EnvProcessTest.LOG.debug("jBPM unit test sample");

		final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/EnvProcess.bpmn");
		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();

		kieSession.getWorkItemManager().registerWorkItemHandler("Human Task", new CopyInToOutWorkItemHandler());
		kieSession.addEventListener((ProcessEventListener) new PatternProcessListener());
		kieSession.addEventListener((ProcessEventListener) new ConfigurationInjectorListener());

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);
		// params.put("conf", new Configuration(kieSession));

		System.setProperty("net.a.g.jbpm.conf.url", "http://www.example.com");

		ProcessInstance processInstance = kieSession.startProcess("EnvProcess", params);

		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
		assertNodeTriggered(processInstance.getId(), "Executer Env");
		assertNodeTriggered(processInstance.getId(), "NOP");
		assertNodeTriggered(processInstance.getId(), "Script Node");
		assertNodeTriggered(processInstance.getId(), "Env Exécutée");

		assertEquals(((RuleFlowProcessInstance) processInstance).getVariable("stringIn"), "http://www.example.com");

		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}

	@Test
	public void testWithoutEnv_EnvProcess() {
		EnvProcessTest.LOG.debug("jBPM unit test sample");

		final RuntimeManager runtimeManager = createRuntimeManager("net/a/g/jbpm/pattern/EnvProcess.bpmn");
		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();

		kieSession.getWorkItemManager().registerWorkItemHandler("Human Task", new CopyInToOutWorkItemHandler());
		kieSession.addEventListener((ProcessEventListener) new PatternProcessListener());
		kieSession.addEventListener((ProcessEventListener) new ConfigurationInjectorListener());

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("booleanIn", true);
		params.put("stringIn", UUID.randomUUID().toString());
		params.put("integerIn", 42);

		ProcessInstance processInstance = kieSession.startProcess("EnvProcess", params);

		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
		assertNodeTriggered(processInstance.getId(), "Executer Env");
		assertNodeTriggered(processInstance.getId(), "NOP");
		assertNodeTriggered(processInstance.getId(), "Script Node");
		assertNodeTriggered(processInstance.getId(), "Env Exécutée");

		assertEquals(((RuleFlowProcessInstance) processInstance).getVariable("stringIn"), "http://default.example.com");

		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}
}