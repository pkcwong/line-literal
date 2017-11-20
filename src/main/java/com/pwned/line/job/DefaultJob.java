package com.pwned.line.job;

import org.quartz.*;

/***
 * Base class for scheduled process.
 */
public class DefaultJob implements Job {

	/***
	 * Default constructor
	 */
	public DefaultJob() {

	}

	/***
	 * Executes the process once, to be called on Trigger.
	 * @param context context
	 * @throws JobExecutionException
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println(this.getClass().getSimpleName());
		this.run();
	}

	/***
	 * Default job has no payload.
	 */
	public void run() {

	}

	/***
	 * Job builder
	 * @param job job
	 * @return JobDetail
	 */
	public static JobDetail buildJob(Class <? extends Job> job) {
		return JobBuilder.newJob(DefaultJob.class).build();
	}

	/***
	 * Trigger builder
	 * @param seconds Run interval
	 * @return Trigger
	 */
	public static Trigger buildTrigger(int seconds) {
		return TriggerBuilder
				.newTrigger()
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInSeconds(seconds).repeatForever())
				.build();
	}

}
