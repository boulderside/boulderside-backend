package com.line7studio.boulderside.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class TimeUtil {
	public static LocalDate toSeoulLocalDate(long unixSeconds) {
		return Instant.ofEpochSecond(unixSeconds)
			.atZone(ZoneId.of("Asia/Seoul"))
			.toLocalDate();
	}
}