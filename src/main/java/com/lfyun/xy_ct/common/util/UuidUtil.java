package com.lfyun.xy_ct.common.util;

import java.util.UUID;

public class UuidUtil {

	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
