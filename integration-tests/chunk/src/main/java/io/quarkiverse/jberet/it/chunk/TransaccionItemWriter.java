package io.quarkiverse.jberet.it.transaccion;

import java.util.List;

import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import model.Transaccion;

@Dependent
@Named
public class TransaccionItemWriter extends AbstractItemWriter {
    @Inject
    TransaccionDatabase database;

    @Override
    public void writeItems(List<Object> items) {
        //        items.stream().map(Transaccion.class::cast).forEach(database::put);
        items.stream().map(Transaccion.class::cast).forEach(database::sum);
    }
}
