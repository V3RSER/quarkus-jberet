package io.quarkiverse.jberet.it.transaccion;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.chunk.AbstractItemReader;
import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.batch.runtime.JobExecution;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import io.quarkus.logging.Log;

/**
 * To verify that BatchRuntime.getJobOperator() returns the right operator in native mode.
 */
@Path("/batch")
@Produces(MediaType.APPLICATION_JSON)
public class ThreadPartitionJobResource {

    @GET
    @Path("/partition")
    public Response ejecutarPartition() {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long executionId = jobOperator.start("partition", new Properties());

        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        return Response.ok(new BatchResource.JobData(executionId, jobExecution.getBatchStatus().toString())).build();
    }

    @Named
    @Dependent
    public static class PartitionItemReader extends AbstractItemReader {
        @Inject
        @BatchProperty(name = "start")
        int start;
        @Inject
        @BatchProperty(name = "end")
        int end;

        private PrimitiveIterator.OfInt intStream;

        @Override
        public void open(Serializable e) {
            intStream = IntStream.rangeClosed(start, end).iterator();
        }

        @Override
        public Object readItem() {
            if (intStream.hasNext()) {
                return intStream.next();
            }
            return null;
        }
    }

    @Named
    @Dependent
    public static class PartitionItemProcessor implements ItemProcessor {
        @Override
        public Object processItem(Object t) {
            Log.info("id:" + t);
            return (int) t % 2 == 0 ? null : t;
        }
    }

    @Named
    @Dependent
    public static class PartitionItemWriter extends AbstractItemWriter {
        @Override
        public void writeItems(List list) {
        }
    }
}
