package io.quarkiverse.jberet.it.transaccion.chunk;

import java.io.Serializable;
import java.sql.*;
import java.util.logging.Logger;

import jakarta.batch.api.chunk.ItemReader;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import org.apache.commons.dbutils.DbUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkiverse.jberet.it.transaccion.ThreadPartitionJobResource;

@Dependent
@Named
@Transactional
public class TransaccionItemReader implements ItemReader {
    @Inject
    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    String url;
    @Inject
    @ConfigProperty(name = "quarkus.datasource.username")
    String username;
    @Inject
    @ConfigProperty(name = "quarkus.datasource.password")
    String password;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private static final Logger LOG = Logger.getLogger(String.valueOf(ThreadPartitionJobResource.class));

    @Override
    public void open(Serializable checkpoint) throws Exception {
        connection = DriverManager.getConnection(url, username, password);
        
        preparedStatement = this.connection.prepareStatement(
                "SELECT t.*" +
                        "FROM transacciones t " +
                        "INNER JOIN cuentas c ON t.numero_cuenta = c.numero_cuenta " +
                        "WHERE c.marcada = true;",

                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY,
                ResultSet.HOLD_CURSORS_OVER_COMMIT);
        LOG.info("Preparando consulta para obtener transacciones");
        resultSet = preparedStatement.executeQuery();
        LOG.info("Transacciones obtenidas de la base de datos");
    }

    @Override
    public void close() {
        DbUtils.closeQuietly(resultSet);
        DbUtils.closeQuietly(preparedStatement);
        DbUtils.closeQuietly(connection);
    }

    @Override
    public Object readItem() throws Exception {
        return resultSet.next() ? resultSet : null;
    }

    @Override
    public Serializable checkpointInfo() {
        return null;
    }

}
