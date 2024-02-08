package io.quarkiverse.jberet.it.transaccion;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/transacciones")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class TransaccionResource {
    @Inject
    TransaccionDatabase database;

    @GET
    @Path("/{id}")
    public Response getTransaccion(@PathParam("id") final Integer id) {
        return Optional.ofNullable(database.getById(id)).map(Response::ok).orElse(Response.status(NOT_FOUND)).build();
    }

    @GET
    @Path("/all")
    public Response getTransaccionDB() {
        return Optional.ofNullable(database.get()).map(Response::ok).orElse(Response.status(NOT_FOUND)).build();
    }
}
