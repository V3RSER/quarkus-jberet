package io.quarkiverse.jberet.it.chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

import model.Transaccion;

@ApplicationScoped
public class TransaccionDatabase {
    private final ConcurrentMap<Integer, Transaccion> database = new ConcurrentHashMap<>();
    private static final Logger LOG = Logger.getLogger(String.valueOf(BatchResource.class));

    void put(Transaccion transaccion) {
        LOG.info("Guardando transacción id: " + transaccion.getNumeroTransaccion());
        database.put(transaccion.getNumeroTransaccion(), transaccion);
    }

    List<Transaccion> get() {
        return new ArrayList<>(database.values());
    }

    Transaccion getById(Integer id) {
        return database.get(id);
    }

    public boolean isEmpty() {
        return database.isEmpty();
    }
}
