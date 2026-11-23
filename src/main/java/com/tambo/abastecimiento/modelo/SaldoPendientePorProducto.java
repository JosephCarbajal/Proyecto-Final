package com.tambo.abastecimiento.modelo;

public final class SaldoPendientePorProducto {
    private final int productoId;
    private final int solicitadas;
    private final int aceptadas;
    private final long costo;

    public SaldoPendientePorProducto(int productoId, int solicitadas,
                                     int aceptadas, long costo) {
        this.productoId = productoId;
        this.solicitadas = solicitadas;
        this.aceptadas = aceptadas;
        this.costo = costo;
    }

    public int productoId() {
        return productoId;
    }

    public int solicitadas() {
        return solicitadas;
    }

    public int aceptadas() {
        return aceptadas;
    }

    public long costo() {
        return costo;
    }

    public int pendiente() {
        return solicitadas - aceptadas;
    }
}