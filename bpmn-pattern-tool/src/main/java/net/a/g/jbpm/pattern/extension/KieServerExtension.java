package net.a.g.jbpm.pattern.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jbpm.executor.impl.ExecutorServiceImpl;
import org.jbpm.services.api.service.ServiceRegistry;
import org.kie.api.executor.ExecutorService;
import org.kie.server.services.api.KieServerApplicationComponentsService;
import org.kie.server.services.api.KieServerRegistry;
import org.kie.server.services.api.SupportedTransports;
import org.kie.server.services.drools.RulesExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.a.g.jbpm.pattern.listener.AsyncListener;

public class KieServerExtension implements KieServerApplicationComponentsService {

	private static final Logger LOG = LoggerFactory.getLogger("net.a.g.jbpm.pattern.Logger");

	private static final String KIESERVER_EXTENSION = "KieServer";
	private static final String JBPM_EXTENSION = "jBPM";

	public KieServerExtension() {
		LOG.error("Startup");
	}

	public Collection<Object> getAppComponents(String extension, SupportedTransports type, Object... services) {

		ExecutorService exec = null;

		LOG.error("> Extension : {}", extension);

		for (Object object : services) {
			LOG.error(">> Service : {}", object.getClass());
		}

		if (!KIESERVER_EXTENSION.equals(extension) || !JBPM_EXTENSION.equals(extension)) {
			return Collections.emptyList();
		}

		exec = (ExecutorService) ServiceRegistry.get().service(ServiceRegistry.EXECUTOR_SERVICE);
		((ExecutorServiceImpl) exec).addAsyncJobListener(new AsyncListener());

		KieServerRegistry context = null;

		for (Object object : services) {

			if (KieServerRegistry.class.isAssignableFrom(object.getClass())) {
				context = (KieServerRegistry) object;
				continue;
			} else if (ExecutorService.class.isAssignableFrom(object.getClass())) {
				exec = (ExecutorService) object;
				((ExecutorServiceImpl) exec).addAsyncJobListener(new AsyncListener());
				continue;
			}
		}

		List<Object> components = new ArrayList<Object>(1);
		if (SupportedTransports.REST.equals(type)) {
			// components.add(new CustomResource(rulesExecutionService, context));

		}

		return components;
	}

}
