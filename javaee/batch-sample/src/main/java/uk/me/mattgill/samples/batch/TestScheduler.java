package uk.me.mattgill.samples.batch;

import java.util.Properties;

import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

@Stateless
public class TestScheduler {

    @Schedule(hour = "*", minute = "*", second = "*/3")
    public void scheduleTask() {
        BatchRuntime.getJobOperator().start("TestJob", new Properties());
    }

}