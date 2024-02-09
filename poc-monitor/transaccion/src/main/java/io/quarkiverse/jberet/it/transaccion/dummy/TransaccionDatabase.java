package io.quarkiverse.jberet.it.transaccion.dummy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.logging.Log;

@ApplicationScoped
public class TransaccionDatabase {
    private final ConcurrentMap<Integer, Transaccion> database = new ConcurrentHashMap<>();

    private Integer datosEnviados = 0;

    public void put(Transaccion transaccion) {
        Log.info("Enviando transacción id: " + transaccion.getNumeroTransaccion());
        database.put(transaccion.getNumeroTransaccion(), transaccion);
    }

    public void sum(Transaccion transaccion) {
        Log.info("Enviando transacción id: " + transaccion.getNumeroTransaccion());
        this.datosEnviados++;
    }

    public TotalTransacciones get() {
        List<Transaccion> transacciones;
        Integer total;
        if (datosEnviados == 0) {
            transacciones = new ArrayList<>(database.values());
            total = transacciones.size();
        } else {
            total = datosEnviados;
        }

        return new TotalTransacciones(total);
    }

    public Transaccion getById(Integer id) {
        return database.get(id);
    }

    public boolean isEmpty() {
        return database.isEmpty();
    }

    public static class TotalTransacciones {
        private Integer total;

        private List<Transaccion> transacciones;

        public TotalTransacciones(Integer total) {
            this.total = total;
        }

        public TotalTransacciones(Integer total, List<Transaccion> transacciones) {
            this.total = total;
            this.transacciones = transacciones;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<Transaccion> getTransacciones() {
            return transacciones;
        }

        public void setTransacciones(List<Transaccion> transacciones) {
            this.transacciones = transacciones;
        }
    }
}
