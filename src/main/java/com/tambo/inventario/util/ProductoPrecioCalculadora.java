package com.tambo.inventario.util;

import com.tambo.compartido.modelo.Producto;

public class ProductoPrecioCalculadora {

    public double calcularGanancia(Producto producto) {
        return (producto.getPrecioCentimos()
                - producto.getCostoCentimos()) / 100.0;
    }

    public double calcularMargenPorcentaje(Producto producto) {
        if (producto.getPrecioCentimos() == 0) {
            return 0;
        }

        return ((producto.getPrecioCentimos()
                - producto.getCostoCentimos()) * 100.0)
                / producto.getPrecioCentimos();
    }

    public double calcularValorStock(Producto producto, int stockActual) {
        return (producto.getCostoCentimos() * stockActual) / 100.0;
    }
}
