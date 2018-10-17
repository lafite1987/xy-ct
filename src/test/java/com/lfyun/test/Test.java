package com.lfyun.test;
public class Test {

	public static Class<?> deduceMainApplicationClass() {
		try {
			StackTraceElement[] stackTrace = (new RuntimeException()).getStackTrace();
			StackTraceElement[] arg0 = stackTrace;
			int arg1 = stackTrace.length;

			for (int arg2 = 0; arg2 < arg1; ++arg2) {
				StackTraceElement stackTraceElement = arg0[arg2];
				if ("main".equals(stackTraceElement.getMethodName())) {
					return Class.forName(stackTraceElement.getClassName());
				}
			}
		} catch (ClassNotFoundException arg4) {
			;
		}

		return null;
	}

}
