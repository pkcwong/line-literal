package com.pwned.line.job;

import org.quartz.*;

public class DefaultJob implements Job {

	public DefaultJob() {

	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println(this.getClass().getSimpleName());
		System.out.println("Running");
		PushWeather.updateWeather();
		//this.run();
	}

	public void run() {

	}

	public static JobDetail buildJob(Class <? extends Job> job) {
		return JobBuilder.newJob(DefaultJob.class).build();
	}

	public static Trigger buildTrigger(int seconds) {
		return TriggerBuilder
				.newTrigger()
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInSeconds(seconds).repeatForever())
				.build();
	}

}
