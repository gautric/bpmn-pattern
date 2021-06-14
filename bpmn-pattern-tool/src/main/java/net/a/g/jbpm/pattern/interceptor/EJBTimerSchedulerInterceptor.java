package net.a.g.jbpm.pattern.interceptor;


import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.drools.core.time.impl.TimerJobInstance;
import org.jbpm.services.ejb.timer.EjbGlobalJobHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJBTimerSchedulerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EJBTimerSchedulerInterceptor.class);

	
	public EJBTimerSchedulerInterceptor() {
		LOGGER.info("Intercept all EJBTimerScheduler.internalSchedule");
	}
	
	@AroundInvoke
	private Object internalScheduleAroundInvoke(final InvocationContext invocationContext) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		String clazz = invocationContext.getMethod().getDeclaringClass().getName();
		String method = invocationContext.getMethod().getName();

		LOGGER.error(">>> @AroundInvoke {} {} {}", clazz, method, invocationContext.getParameters());
		try {

			TimerJobInstance tji = (TimerJobInstance) invocationContext.getParameters()[0];
			EjbGlobalJobHandle gjtji = (EjbGlobalJobHandle) tji.getJobHandle();
			LOGGER.error(">>> {}", gjtji.getUuid());

			LOGGER.error(">>> {}", sdf.format(tji.getTrigger().hasNextFireTime()));

			Object result = invocationContext.proceed();
			LOGGER.error("<<< @AroundInvoke {} {} {}", clazz, method, invocationContext.getParameters());

			return result;
		} catch (Exception e) {
			LOGGER.error("<<< @AroundInvoke Exception {} {} {}", clazz, method, e);

			throw e;
		} finally {
			LOGGER.error("<<< @AroundInvoke {} {}", clazz, method);
		}

	}
}
