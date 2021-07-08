package net.a.g.jbpm.thread;

import java.util.Arrays;

import org.slf4j.Logger;

public class Helper {

	public void strackTrace() {

		Logger LOG = org.slf4j.LoggerFactory.getLogger("net.a.g.jbpm.pattern.Logger");
	
		Arrays.stream(Thread.currentThread().getStackTrace()).map(e -> "STRACKTRACE " + e.toString())
				.filter(e -> e.contains(System.getProperty("net.a.g.jbpm.filter", ""))).forEach(LOG::error);
	}
}
