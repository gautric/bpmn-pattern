package net.a.g.jbpm.pattern.util;

import java.util.UUID;

@SuppressWarnings("serial")
public class Exception extends RuntimeException {

	private String uuid = UUID.randomUUID().toString();

	public Exception() {
		this("Non Defined Message (please use constructor)");
	}

	public Exception(String msg) {
		super(msg);
	}

	@Override
	public String toString() {
		return "Exception [uuid:" + uuid + ", msg:" + super.getMessage() + "]";
	}
}
