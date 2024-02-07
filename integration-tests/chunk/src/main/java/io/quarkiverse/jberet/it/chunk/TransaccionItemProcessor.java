package io.quarkiverse.jberet.it.chunk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;

import model.Transaccion;

@Dependent
@Named
public class TransaccionItemProcessor implements ItemProcessor {
    private static final Logger LOG = Logger.getLogger(String.valueOf(BatchResource.class));

    @Override
    public Object processItem(Object item) throws SQLException {
        ResultSet resultSet = (ResultSet) item;

        Transaccion transaccion = new Transaccion();
        transaccion.setNumeroTransaccion(resultSet.getInt(1));
        transaccion.setNumeroCuenta(resultSet.getInt(2));
        transaccion.setFecha(resultSet.getString(3));
        transaccion.setMonto(resultSet.getDouble(4));

        LOG.info("Procesando transacción id: " + transaccion.getNumeroTransaccion());

        return transaccion;
    }
}
