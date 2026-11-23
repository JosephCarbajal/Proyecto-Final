package com.tambo.abastecimiento.modelo;

public final class DetalleOrdenCompra {
    private final int productoId;
    private final int cantidad;

    public DetalleOrdenCompra(int productoId, int cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    public int productoId() {
        return productoId;
    }

    public int cantidad() {
        return cantidad;
    }
}