package io.quarkiverse.jberet.it.transaccion;

import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import io.quarkiverse.jberet.runtime.QuarkusJobOperator;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * To verify that BatchRuntime.getJobOperator() returns the right operator in native mode.
 */
@Path("/batch")
@Produces(MediaType.APPLICATION_JSON)
public class BatchResource {

    @Inject
    QuarkusJobOperator quarkusJobOperator;

    @GET
    @Path("/execute")
    public Response ejecutarJobs() {
        int totalRegistros = obtenerTotalRegistros();
        int numJobs = 5;
        int registrosPorHilo = totalRegistros / numJobs;

        var jobIds = IntStream.range(0, numJobs)
                .mapToObj(i -> {
                    Properties jobParameters = new Properties();
                    jobParameters.setProperty("start", Integer.toString(i * registrosPorHilo));
                    jobParameters.setProperty("registrosPorHilo", Integer.toString(registrosPorHilo));
                    return jobParameters;
                })
                .map(jobParameters -> quarkusJobOperator.start("transacciones", jobParameters))
                .collect(Collectors.toList());

        return Response.ok(jobIds.stream()
                .map(jobId -> new JobData(jobId, quarkusJobOperator.getJobExecution(jobId).getBatchStatus().toString()))
                //                .filter(BatchStatus.COMPLETED::equals)
                .collect(Collectors.toList())).build();
    }

    private static int obtenerTotalRegistros() {
        //todo: consulta a la base de datos
        return 61;
    }

    @RegisterForReflection
    public static class JobData {
        private Long job;
        private String status;

        public JobData(final Long job, final String status) {
            this.job = job;
            this.status = status;
        }

        public Long getJob() {
            return job;
        }

        public void setJob(final Long job) {
            this.job = job;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(final String status) {
            this.status = status;
        }
    }
}
