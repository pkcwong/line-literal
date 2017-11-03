package com.pwned.line.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DefaultJob implements Job {

	public DefaultJob() {

	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println(this.getClass().getSimpleName());
		this.run();
	}

	public void run() {

	}

}
