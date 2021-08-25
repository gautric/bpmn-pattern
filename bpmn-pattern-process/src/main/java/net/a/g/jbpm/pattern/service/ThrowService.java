package net.a.g.jbpm.pattern.service;

public class ThrowService {

	public Object callMethod(Boolean throwIn) {

		System.out.println(" Call " + this.getClass().getCanonicalName() + " " + throwIn);

		if (throwIn != null && (boolean) throwIn) {
			throw new RuntimeException("Exception inside a Service Call " + this.getClass().getCanonicalName());

		}
		return "RESULT";
	}
}
