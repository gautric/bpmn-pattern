package net.a.g.jbpm.pattern.util;

import java.util.UUID;

@SuppressWarnings("serial")
public class Exception extends RuntimeException {

	private String uuid = UUID.randomUUID().toString();

	@Override
	public String toString() {
		return "Exception [uuid=" + uuid + "]";
	}
}
