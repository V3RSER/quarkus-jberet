package io.quarkiverse.jberet.it.transaccion.dummy;

//import jakarta.persistence.*;
//
//import io.quarkus.hibernate.orm.panache.PanacheEntity;

//@Entity
//@NamedQueries({
//        @NamedQuery(name = "AuctionItemStatistics.findByRealmsAndItem",
//                query = "SELECT ais FROM AuctionStatistics ais " +
//                        "WHERE ais.connectedRealm.id IN :realmIds AND ais.itemId = :itemId " +
//                        "ORDER BY ais.timestamp ASC")
//})

public class Transaccion {

    public Integer numeroTransaccion;

    public Integer numeroCuenta;

    public String fecha;

    public Double monto;

    public Transaccion() {
    }

    public Integer getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(Integer numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public Integer getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(Integer numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
