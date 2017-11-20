package com.pwned.line.job;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class PushTimetableTest {

	@Test
	private void convertDay() {
		assertEquals(Calendar.MONDAY, PushTimetable.convertDay("Mo"));
		assertEquals(Calendar.TUESDAY, PushTimetable.convertDay("Tu"));
		assertEquals(Calendar.WEDNESDAY, PushTimetable.convertDay("We"));
		assertEquals(Calendar.THURSDAY, PushTimetable.convertDay("Th"));
		assertEquals(Calendar.FRIDAY, PushTimetable.convertDay("Fr"));
		assertEquals(Calendar.SATURDAY, PushTimetable.convertDay("Sa"));
		assertEquals(Calendar.SUNDAY, PushTimetable.convertDay("Su"));
	}

}
