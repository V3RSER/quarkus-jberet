package io.quarkiverse.jberet.it.transaccion;

import java.time.LocalTime;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.batch.api.Batchlet;
import jakarta.batch.api.listener.AbstractJobListener;
import jakarta.batch.runtime.BatchStatus;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jberet.job.model.JobBuilder;
import org.jberet.job.model.StepBuilder;

import io.quarkiverse.jberet.runtime.QuarkusJobOperator;
import io.quarkus.logging.Log;

/**
 * To verify that BatchRuntime.getJobOperator() returns the right operator in native mode.
 */
@Path("/batch")
@Produces(MediaType.APPLICATION_JSON)
public class ThreadJobResource {

    @Inject
    QuarkusJobOperator quarkusJobOperator;

    @GET
    @Path("/thread")
    public Response ejecutarEnHilos() {

        List<Long> jobIds = Stream.of("job 1", "job 2", "job 3")
                .map(jobName -> new JobBuilder(jobName)
                        .listener("threadJobListener")
                        .step(new StepBuilder("dummyStep")
                                .batchlet("blocking")
                                .itemCount(100)
                                .build())
                        .build())
                .map(job -> quarkusJobOperator.start(job, new Properties()))
                .collect(Collectors.toList());

        return Response.ok(jobIds.stream()
                .map(jobId -> quarkusJobOperator.getJobExecution(jobId).getBatchStatus())
                //                .filter(BatchStatus.COMPLETED::equals)
                .collect(Collectors.toList())).build();
    }

    @Named("blocking")
    @Dependent
    public static class BlockingBatchlet implements Batchlet {

        @Override
        public String process() throws InterruptedException {
            Log.info(LocalTime.now() + " Executing");
            Thread.sleep(700);
            Log.info(LocalTime.now() + " Execution finished");
            return BatchStatus.COMPLETED.toString();
        }

        @Override
        public void stop() {
        }
    }

    @Named
    @Dependent
    public static class ThreadJobListener extends AbstractJobListener {
        @Override
        public void beforeJob() {
            ThreadCounter.incrementJobCounter();
        }

        @Override
        public void afterJob() {
            ThreadCounter.decrementJobCounter();
        }
    }

    public static class ThreadCounter {
        private static volatile int runningJobsCounter = 0;
        private static volatile int maxParallelRunningJobCounter = 0;

        public static synchronized void incrementJobCounter() {
            runningJobsCounter++;
            if (runningJobsCounter > maxParallelRunningJobCounter) {
                maxParallelRunningJobCounter = runningJobsCounter;
            }

        }

        public static synchronized void decrementJobCounter() {
            runningJobsCounter--;
        }
    }

}
