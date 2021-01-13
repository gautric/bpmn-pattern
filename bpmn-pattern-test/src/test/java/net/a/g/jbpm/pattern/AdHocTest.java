package net.a.g.jbpm.pattern;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.a.g.jbpm.pattern.listener.MDCProcessListener;

public class AdHocTest extends JbpmJUnitBaseTestCase {

	private static final Logger LOG = LoggerFactory.getLogger(AdHocTest.class);

	public AdHocTest() {
		super(true, true);

	}

	@Test
	public void testAdHocWMilestoneBranch() throws IOException {
		LOG.debug("jBPM unit test sample");

		Map<String, ResourceType> app = new HashMap<String, ResourceType>();
		app.put("net/a/g/jbpm/pattern/AdHocProcess.bpmn2", ResourceType.BPMN2);

		final RuntimeManager runtimeManager = createRuntimeManager(app);

		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();
		kieSession.addEventListener((ProcessEventListener)new MDCProcessListener());
		kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());

		

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("complete",false);
		
	
		final ProcessInstance processInstance = kieSession.startProcess("AdHocProcess", params);

		
		InputStream is =this.getClass().getResourceAsStream("/usergroups.properties");
		
	    IOUtils.copyLarge(is, System.out);
	    System.out.println("");
	    
	  	
  		kieSession.signalEvent("Milestone",null,processInstance.getId());
	
  	
  		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
  		
		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}
	
	@Test
	public void testAdHocwithHumanTaskBranch() throws IOException {
		LOG.debug("jBPM unit test sample");

		Map<String, ResourceType> app = new HashMap<String, ResourceType>();
		app.put("net/a/g/jbpm/pattern/AdHocProcess.bpmn2", ResourceType.BPMN2);

		final RuntimeManager runtimeManager = createRuntimeManager(app);

		final RuntimeEngine runtimeEngine = getRuntimeEngine(null);
		final KieSession kieSession = runtimeEngine.getKieSession();
		kieSession.addEventListener((ProcessEventListener)new MDCProcessListener());
		kieSession.addEventListener((ProcessEventListener)new PatternProcessListener());

		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("complete",false);
			
		final ProcessInstance processInstance = kieSession.startProcess("AdHocProcess", params);

		InputStream is =this.getClass().getResourceAsStream("/usergroups.properties");
		
	    IOUtils.copyLarge(is, System.out);
	    System.out.println("");
	    
	    kieSession.signalEvent("HumanTask",null,processInstance.getId());
		
		TaskService taskService = runtimeEngine.getTaskService();
  		List taskList = taskService.getTasksByProcessInstanceId(processInstance.getId());
		
  		Task task = taskService.getTaskById((long)1);
  		
  		System.out.println(task);

  		taskService.claim(1, "admin");
  		taskService.start(1, "admin");
  		
  		
		params = new HashMap<String, Object>();
		params.put("complete",true);
  		
  		taskService.complete(1, "admin", params);

  		assertProcessInstanceNotActive(processInstance.getId(), kieSession);
  		
		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}
	
	
}