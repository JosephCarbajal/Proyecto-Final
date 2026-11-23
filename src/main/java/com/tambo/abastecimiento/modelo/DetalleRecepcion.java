package com.tambo.abastecimiento.modelo;

public final class DetalleRecepcion {
    private final int productoId;
    private final int recibidas;
    private final int aceptadas;

    public DetalleRecepcion(int productoId, int recibidas, int aceptadas) {
        this.productoId = productoId;
        this.recibidas = recibidas;
        this.aceptadas = aceptadas;
    }

    public int productoId() {
        return productoId;
    }

    public int recibidas() {
        return recibidas;
    }

    public int aceptadas() {
        return aceptadas;
    }
}